package com.qihang.erp.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.qihang.erp.api.domain.*;
import com.qihang.erp.api.mapper.*;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.service.IPddOrderService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 拼多多订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-02
 */
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Service
public class PddOrderServiceImpl implements IPddOrderService 
{
    @Autowired
    private PddOrderMapper pddOrderMapper;
    @Autowired
    private ErpOrderMapper erpOrderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsSpecMapper goodsSpecMapper;
    @Autowired
    private ScmSupplierAgentShippingMapper agentShippingMapper;

    @Autowired
    private WmsOrderShippingMapper orderShippingMapper;
    /**
     * 查询拼多多订单
     * 
     * @param id 拼多多订单主键
     * @return 拼多多订单
     */
    @Override
    public PddOrder selectPddOrderById(Long id)
    {
        return pddOrderMapper.selectPddOrderById(id);
    }

    /**
     * 查询拼多多订单列表
     * 
     * @param pddOrder 拼多多订单
     * @return 拼多多订单
     */
    @Override
    public List<PddOrder> selectPddOrderList(PddOrder pddOrder)
    {
        List<PddOrder> orderList = pddOrderMapper.selectPddOrderList(pddOrder);
        for (var o:orderList) {
            List<PddOrderItem> items = pddOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setPddOrderItemList(items);
        }
        return orderList;
    }

    /**
     * 新增拼多多订单
     * 
     * @param pddOrder 拼多多订单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertPddOrder(PddOrder pddOrder) {
        PddOrder order = pddOrderMapper.selectByOrderSn(pddOrder.getOrderSn());
        if (order != null) return -1;

//        if (StringUtils.isNotNull(pddOrder.getPddOrderItemList())) {
//            for (PddOrderItem pddOrderItem : pddOrder.getPddOrderItemList()) {
//                if (StringUtils.isNull(pddOrderItem.getErpSpecId())) {
//                    return -3;
//                }
//            }
//        } else return -2;

        pddOrder.setTradeType(0L);
        pddOrder.setConfirmStatus(1L);
        pddOrder.setGroupStatus(1L);
        pddOrder.setRefundStatus(1L);
        pddOrder.setOrderStatus(1L);
        double discountAmount = pddOrder.getPlatformDiscount() + pddOrder.getSellerDiscount() + pddOrder.getCapitalFreeDiscount();
        pddOrder.setDiscountAmount(discountAmount);
        double payAmount = pddOrder.getGoodsAmount() - pddOrder.getDiscountAmount() + pddOrder.getPostage();
        pddOrder.setPayAmount(payAmount);
        pddOrder.setAfterSalesStatus(0L);
        pddOrder.setOrderConfirmTime(0L);
        pddOrder.setAuditStatus(0L);
        pddOrder.setSettlementStatus(0L);
        pddOrder.setShipStatus(0L);
        int rows = pddOrderMapper.insertPddOrder(pddOrder);
//        insertPddOrderItem(pddOrder);
        Long id = pddOrder.getId();
        List<PddOrderItem> list = new ArrayList<PddOrderItem>();
        for (PddOrderItem pddOrderItem : pddOrder.getPddOrderItemList()) {
            pddOrderItem.setOrderId(id);
            pddOrderItem.setRefundStatus(pddOrder.getRefundStatus());
            list.add(pddOrderItem);
        }

        if (list.size() > 0) {
            try {
                pddOrderMapper.batchPddOrderItem(list);
            } catch (Exception e) {
                //手工回滚异常
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return -3;
            }
        }

        return rows;
    }

    /**
     * 确认订单
     * @param pddOrder
     * @return
     */
    @Transactional
    @Override
    public int confirmOrder(PddOrder pddOrder) {
        PddOrder original = pddOrderMapper.selectPddOrderById(pddOrder.getId());
        if(original == null) return -1;
        else if(original.getAuditStatus() != 0) return -2;
        else if(original.getRefundStatus() != 1) return -3;
        if(pddOrder.getShipType() != 0 && pddOrder.getShipType() != 1){
            // 1 供应商发货 0 仓库发货
            return -5;
        }
        // 判断是否存在
        ErpOrder erpo = erpOrderMapper.selectErpOrderByNum(original.getOrderSn());
        if(erpo !=null ) return -4;

        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
        ErpOrder so = new ErpOrder();
        so.setOrderNum(original.getOrderSn());
        so.setShopId(original.getShopId().intValue());
        so.setShopType(5);
        so.setShipType(pddOrder.getShipType());
        so.setRemark(original.getRemark());
        so.setBuyerMemo(original.getBuyerMemo());
        so.setTag(original.getTag());
        so.setRefundStatus(1);
        so.setOrderStatus(1);
        so.setGoodsAmount(BigDecimal.valueOf(original.getGoodsAmount()));
        so.setDiscountAmount(BigDecimal.valueOf(original.getDiscountAmount()));
        so.setAmount(BigDecimal.valueOf(original.getPayAmount()));
        so.setPostage(BigDecimal.valueOf(original.getPostage()));
        try {
            //2022-07-17 17:10:57
            Date payDate = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", original.getPayTime());
            so.setPayTime(payDate);
        }catch (Exception e){}

        so.setReceiverName(original.getReceiverName());
        so.setReceiverPhone(original.getReceiverPhone());
        so.setAddress(original.getAddress());
        so.setCountry("中国");
        so.setProvince(original.getProvince());
        so.setCity(original.getCity());
        so.setTown(original.getTown());
        so.setConfirmTime(new Date());
        so.setCreateTime(new Date());
        so.setCreateBy(pddOrder.getUpdateBy());
        erpOrderMapper.insertErpOrder(so);

        // 添加Erp_order_item
        List<PddOrderItem> orderItems = pddOrderMapper.selectOrderItemByOrderId(pddOrder.getId());
        List<ErpOrderItem> items = new ArrayList<>();
        for (var i:orderItems) {
            if(StringUtils.isEmpty(i.getSpecNum())) return -11;
            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNum());
            if (spec == null) return -11;
            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
            if(goods == null) return -12;

            ErpOrderItem item = new ErpOrderItem();
            item.setOrderId(so.getId());
            item.setOrderItemNum(i.getId()+"");
            item.setSupplierId(goods.getSupplierId().intValue());
            item.setGoodsId(spec.getGoodsId());
            item.setSpecId(spec.getId());
            item.setGoodsTitle(i.getGoodsName());
            item.setGoodsImg(i.getGoodsImage());
            item.setGoodsNum(i.getGoodsNum());
            item.setSpecNum(i.getSpecNum());
            item.setGoodsSpec(i.getGoodsSpec());
            item.setGoodsPrice(BigDecimal.valueOf(i.getGoodsPrice()));
            item.setGoodsPurPrice(spec.getPurPrice());
            item.setItemAmount(BigDecimal.valueOf(i.getItemAmount()));
            item.setQuantity(i.getQuantity().intValue());
            item.setIsGift(i.getIsGift().intValue());
            item.setRefundCount(0);
            item.setRefundStatus(1);
            item.setCreateBy(pddOrder.getUpdateBy());
            item.setCreateTime(new Date());
            items.add(item);
        }
        // 添加了赠品
        if(pddOrder.getPddOrderItemList()!=null && !pddOrder.getPddOrderItemList().isEmpty()) {
            for (var i : pddOrder.getPddOrderItemList()) {
                if(StringUtils.isEmpty(i.getSpecNum())) return -11;
                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNum());
                if (spec == null) return -11;
                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
                if(goods == null) return -12;

                ErpOrderItem item = new ErpOrderItem();
                item.setOrderId(so.getId());
                item.setOrderItemNum(pddOrder.getId()+"_");
                item.setSupplierId(goods.getSupplierId().intValue());
                item.setGoodsId(spec.getGoodsId());
                item.setSpecId(spec.getId());
                item.setGoodsTitle(i.getGoodsName());
                item.setGoodsImg(i.getGoodsImage());
                item.setGoodsNum(i.getGoodsNum());
                item.setSpecNum(i.getSpecNum());
                item.setGoodsSpec(i.getGoodsSpec());
                item.setGoodsPrice(BigDecimal.valueOf(i.getGoodsPrice()));
                item.setGoodsPurPrice(spec.getPurPrice());
                item.setItemAmount(BigDecimal.valueOf(i.getItemAmount()));
                item.setQuantity(i.getQuantity().intValue());
                item.setIsGift(1);
                item.setRefundCount(0);
                item.setRefundStatus(1);
                item.setCreateBy(pddOrder.getUpdateBy());
                item.setCreateTime(new Date());
                items.add(item);
            }
        }
//        erpOrderMapper.batchErpOrderItem(items);
        // 新增代发表scm_supplier_agent_shipping
        if(pddOrder.getShipType() == 1){
            for (ErpOrderItem it: items) {
                // 添加Erp_order_item
                erpOrderMapper.insertErpOrderItem(it);
                ScmSupplierAgentShipping agentShipping = new ScmSupplierAgentShipping();
                agentShipping.setShopId(original.getShopId());
                agentShipping.setShopType(5L);
                agentShipping.setSupplierId(it.getSupplierId().longValue());
                agentShipping.setOrderNum(original.getOrderSn());
                agentShipping.setErpOrderId(so.getId());
                agentShipping.setErpOrderItemId(it.getId());
                try {
                    agentShipping.setOrderDate(original.getCreatedTime());
                }catch (Exception e){}

                agentShipping.setGoodsId(it.getGoodsId());
                agentShipping.setSpecId(it.getSpecId());
                agentShipping.setGoodsTitle(it.getGoodsTitle());
                agentShipping.setGoodsImg(it.getGoodsImg());
                agentShipping.setGoodsNum(it.getGoodsNum());
                agentShipping.setGoodsSpec(it.getGoodsSpec());
                agentShipping.setSpecNum(it.getSpecNum());
                agentShipping.setGoodsPrice(it.getGoodsPurPrice());
                agentShipping.setQuantity(it.getQuantity().longValue());
                agentShipping.setItemAmount(it.getItemAmount());
                agentShipping.setStatus(0L);
                agentShipping.setCreateTime(new Date());
                agentShipping.setCreateBy(pddOrder.getUpdateBy());

                agentShippingMapper.insertScmSupplierAgentShipping(agentShipping);
            }
        }else {
            // 仓库发货
            for (ErpOrderItem it: items) {
                erpOrderMapper.insertErpOrderItem(it);

                WmsOrderShipping shipping = new WmsOrderShipping();
                shipping.setShopId(original.getShopId());
                shipping.setShopType(5L);
                shipping.setOrderNum(original.getOrderSn());
                shipping.setErpOrderId(so.getId());
                shipping.setErpOrderItemId(it.getId());
                try {
                    shipping.setOrderDate(original.getCreatedTime());
                }catch (Exception e){}
                shipping.setGoodsId(it.getGoodsId());
                shipping.setSpecId(it.getSpecId());
                shipping.setGoodsTitle(it.getGoodsTitle());
                shipping.setGoodsImg(it.getGoodsImg());
                shipping.setGoodsNum(it.getGoodsNum());
                shipping.setGoodsSpec(it.getGoodsSpec());
                shipping.setSpecNum(it.getSpecNum());
                shipping.setQuantity(it.getQuantity().longValue());
                shipping.setStatus(0L);
                shipping.setCreateTime(new Date());
                shipping.setCreateBy(pddOrder.getUpdateBy());
                orderShippingMapper.insertWmsOrderShipping(shipping);
            }
        }
        //更新自己

        //更新自己
        PddOrder po =new PddOrder();
        po.setId(original.getId());
        po.setAuditStatus(1L);
        po.setUpdateBy(pddOrder.getUpdateBy());
        po.setUpdateTime(new Date());
        pddOrderMapper.updatePddOrder(po);
        return 1;
    }

    /**
     * 修改拼多多订单
     * 
     * @param pddOrder 拼多多订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePddOrder(PddOrder pddOrder)
    {
        pddOrderMapper.deletePddOrderItemByOrderId(pddOrder.getId());
        insertPddOrderItem(pddOrder);
        return pddOrderMapper.updatePddOrder(pddOrder);
    }

    /**
     * 批量删除拼多多订单
     * 
     * @param ids 需要删除的拼多多订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePddOrderByIds(Long[] ids)
    {
        pddOrderMapper.deletePddOrderItemByOrderIds(ids);
        return pddOrderMapper.deletePddOrderByIds(ids);
    }

    /**
     * 删除拼多多订单信息
     * 
     * @param id 拼多多订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePddOrderById(Long id)
    {
        pddOrderMapper.deletePddOrderItemByOrderId(id);
        return pddOrderMapper.deletePddOrderById(id);
    }

    /**
     * 新增拼多多订单明细信息
     * 
     * @param pddOrder 拼多多订单对象
     */
    public void insertPddOrderItem(PddOrder pddOrder)
    {
        List<PddOrderItem> pddOrderItemList = pddOrder.getPddOrderItemList();
        Long id = pddOrder.getId();
        if (StringUtils.isNotNull(pddOrderItemList))
        {
            List<PddOrderItem> list = new ArrayList<PddOrderItem>();
            for (PddOrderItem pddOrderItem : pddOrderItemList)
            {
                pddOrderItem.setOrderId(id);
                list.add(pddOrderItem);
            }
            if (list.size() > 0)
            {
                pddOrderMapper.batchPddOrderItem(list);
            }
        }
    }
}
