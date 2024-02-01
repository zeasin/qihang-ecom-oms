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
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.service.IDouOrderService;

/**
 * 抖店订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-02
 */
@Service
public class DouOrderServiceImpl implements IDouOrderService 
{
    @Autowired
    private DouOrderMapper douOrderMapper;
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
     * 查询抖店订单
     * 
     * @param id 抖店订单主键
     * @return 抖店订单
     */
    @Override
    public DouOrder selectDouOrderById(Long id)
    {
        return douOrderMapper.selectDouOrderById(id);
    }

    /**
     * 查询抖店订单列表
     * 
     * @param douOrder 抖店订单
     * @return 抖店订单
     */
    @Override
    public List<DouOrder> selectDouOrderList(DouOrder douOrder)
    {
        List<DouOrder> douOrders = douOrderMapper.selectDouOrderList(douOrder);
        for (var o:douOrders) {
            List<DouOrderItem> items = douOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setDouOrderItemList(items);
        }
        return douOrders;
    }

    /**
     * 新增抖店订单
     * 
     * @param douOrder 抖店订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertDouOrder(DouOrder douOrder)
    {
        douOrder.setOrderStatus(2L);
        douOrder.setOrderStatusStr("备货中");
        douOrder.setPostInsuranceAmount(0L);
        douOrder.setIsComment(0L);
        douOrder.setCreatedTime(new Date());
        douOrder.setSendStatus(0L);
        douOrder.setAuditStatus(0L);

        int rows = douOrderMapper.insertDouOrder(douOrder);
        insertDouOrderItem(douOrder);
        return rows;
    }

    /**
     * 修改抖店订单
     * 
     * @param douOrder 抖店订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDouOrder(DouOrder douOrder)
    {
        douOrder.setUpdateTime(DateUtils.getNowDate());
        douOrderMapper.deleteDouOrderItemByDouyinOrderId(douOrder.getId());
        insertDouOrderItem(douOrder);
        return douOrderMapper.updateDouOrder(douOrder);
    }

    @Transactional
    @Override
    public int confirmOrder(DouOrder bo) {
        DouOrder original = douOrderMapper.selectDouOrderById(bo.getId());
        if(original == null) return -1;
        else if(original.getAuditStatus() != 0) return -2;
//        else if(original.getRefundStatus() != 1) return -3;
        if(bo.getShipType() != 0 && bo.getShipType() != 1){
            // 1 供应商发货 0 仓库发货
            return -5;
        }
        // 判断是否存在
        ErpOrder erpo = erpOrderMapper.selectErpOrderByNum(original.getOrderId());
        if(erpo !=null ) return -4;

        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
        ErpOrder so = new ErpOrder();
        so.setOrderNum(original.getOrderId());
        so.setShopId(original.getShopId().intValue());
        so.setShopType(6);
        so.setShipType(bo.getShipType());
        so.setRemark(original.getSellerWords());
        so.setBuyerMemo(original.getBuyerWords());
        so.setTag("");
        so.setRefundStatus(1);
        so.setOrderStatus(1);
        so.setGoodsAmount(original.getOrderTotalAmount().subtract(original.getShopCouponAmount()).subtract(original.getCouponAmount()));
        so.setDiscountAmount(original.getShopCouponAmount().add(original.getCouponAmount()));
        so.setAmount(original.getOrderTotalAmount().add(original.getPostAmount()));
        so.setPostage(original.getPostAmount());
        try {
            so.setPayTime(original.getPayTime());
        }catch (Exception e){}

        so.setReceiverName(bo.getPostReceiver());
        so.setReceiverPhone(bo.getPostTel());
        so.setAddress(bo.getPostAddr());
        so.setCountry("中国");
        so.setProvince(bo.getProvince());
        so.setCity(bo.getCity());
        so.setTown(bo.getTown());
        so.setConfirmTime(new Date());
        so.setCreateTime(new Date());
        so.setCreateBy(bo.getUpdateBy());
        erpOrderMapper.insertErpOrder(so);

        // 添加Erp_order_item
        List<DouOrderItem> orderItems = douOrderMapper.selectOrderItemByOrderId(original.getId());
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
            item.setGoodsTitle(i.getProductName());
            item.setGoodsImg(i.getProductPic());
            item.setGoodsNum(i.getGoodsNum());
            item.setSpecNum(i.getSpecNum());
            item.setGoodsSpec(i.getGoodsSpec());
            item.setGoodsPrice(i.getPrice());
            item.setGoodsPurPrice(spec.getPurPrice());
            item.setItemAmount(i.getTotalAmount());
            item.setQuantity(i.getComboNum().intValue());
            item.setIsGift(i.getIsGift().intValue());
            item.setRefundCount(0);
            item.setRefundStatus(1);
            item.setCreateBy(bo.getUpdateBy());
            item.setCreateTime(new Date());
            items.add(item);
        }
        // 添加了赠品
        if(bo.getDouOrderItemList()!=null && !bo.getDouOrderItemList().isEmpty()) {
            for (var i : bo.getDouOrderItemList()) {
                if(StringUtils.isEmpty(i.getSpecNum())) return -11;
                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNum());
                if (spec == null) return -11;
                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
                if(goods == null) return -12;

                ErpOrderItem item = new ErpOrderItem();
                item.setOrderId(so.getId());
                item.setOrderItemNum(original.getOrderId()+"_");
                item.setSupplierId(goods.getSupplierId().intValue());
                item.setGoodsId(spec.getGoodsId());
                item.setSpecId(spec.getId());
                item.setGoodsTitle(i.getProductName());
                item.setGoodsImg(i.getProductPic());
                item.setGoodsNum(i.getGoodsNum());
                item.setSpecNum(i.getSpecNum());
                item.setGoodsSpec(i.getGoodsSpec());
                item.setGoodsPrice(i.getPrice());
                item.setGoodsPurPrice(spec.getPurPrice());
                item.setItemAmount(i.getTotalAmount());
                item.setQuantity(i.getComboNum().intValue());
                item.setIsGift(1);
                item.setRefundCount(0);
                item.setRefundStatus(1);
                item.setCreateBy(bo.getUpdateBy());
                item.setCreateTime(new Date());
                items.add(item);
            }
        }
//        erpOrderMapper.batchErpOrderItem(items);
        // 新增代发表
        if(bo.getShipType() == 1){
            for (ErpOrderItem it: items) {
                // 添加Erp_order_item
                erpOrderMapper.insertErpOrderItem(it);
                ScmSupplierAgentShipping agentShipping = new ScmSupplierAgentShipping();
                agentShipping.setShopId(original.getShopId());
                agentShipping.setShopType(6L);
                agentShipping.setSupplierId(it.getSupplierId().longValue());
                agentShipping.setOrderNum(original.getOrderId());
                agentShipping.setErpOrderId(so.getId());
                agentShipping.setErpOrderItemId(it.getId());
//                agentShipping.setOrderItemId(it.getId().toString());
                try {
                    agentShipping.setOrderDate(original.getOrderCreateTime());
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
                agentShipping.setCreateBy(bo.getUpdateBy());

                agentShippingMapper.insertScmSupplierAgentShipping(agentShipping);
            }
        }else {
            // 仓库发货
            for (ErpOrderItem it: items) {
                erpOrderMapper.insertErpOrderItem(it);

                WmsOrderShipping shipping = new WmsOrderShipping();
                shipping.setShopId(original.getShopId());
                shipping.setShopType(6L);
                shipping.setOrderNum(original.getOrderId());
                shipping.setErpOrderId(so.getId());
                shipping.setErpOrderItemId(it.getId());
//                shipping.setOrderItemId(it.getId().toString());
                try {
                    shipping.setOrderDate(original.getOrderCreateTime());
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
                shipping.setCreateBy(bo.getUpdateBy());
                orderShippingMapper.insertWmsOrderShipping(shipping);
            }
        }
        //更新自己

        //更新自己
        DouOrder up =new DouOrder();
        up.setId(original.getId());
        up.setAuditStatus(1L);
        up.setUpdateBy(bo.getUpdateBy());
        up.setUpdateTime(new Date());
        douOrderMapper.updateDouOrder(up);
        return 1;
    }

    /**
     * 批量删除抖店订单
     * 
     * @param ids 需要删除的抖店订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDouOrderByIds(Long[] ids)
    {
        douOrderMapper.deleteDouOrderItemByDouyinOrderIds(ids);
        return douOrderMapper.deleteDouOrderByIds(ids);
    }

    /**
     * 删除抖店订单信息
     * 
     * @param id 抖店订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDouOrderById(Long id)
    {
        douOrderMapper.deleteDouOrderItemByDouyinOrderId(id);
        return douOrderMapper.deleteDouOrderById(id);
    }

    /**
     * 新增抖店订单明细信息
     * 
     * @param douOrder 抖店订单对象
     */
    public void insertDouOrderItem(DouOrder douOrder)
    {
        List<DouOrderItem> douOrderItemList = douOrder.getDouOrderItemList();
        Long id = douOrder.getId();
        if (StringUtils.isNotNull(douOrderItemList))
        {
            List<DouOrderItem> list = new ArrayList<DouOrderItem>();
            for (DouOrderItem douOrderItem : douOrderItemList)
            {
                douOrderItem.setOrderId(douOrder.getOrderId());
                douOrderItem.setSubOrderId("");
                if(StringUtils.isNull(douOrderItem.getPostAmount())) {
                    douOrderItem.setPostAmount(BigDecimal.ZERO);
                }
                if(StringUtils.isNull(douOrderItem.getCouponAmount())) {
                    douOrderItem.setCouponAmount(BigDecimal.ZERO);
                }
                douOrderItem.setIsComment(0L);
                if(StringUtils.isNull(douOrderItem.getIsGift())) { douOrderItem.setIsGift(0L);}
                douOrderItem.setItemStatus(douOrder.getOrderStatus());
                douOrderItem.setDouyinOrderId(id);
                list.add(douOrderItem);
            }
            if (list.size() > 0)
            {
                douOrderMapper.batchDouOrderItem(list);
            }
        }
    }
}
