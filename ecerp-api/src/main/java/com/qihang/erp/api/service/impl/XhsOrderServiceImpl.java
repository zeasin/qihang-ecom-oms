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
import com.qihang.erp.api.service.IXhsOrderService;

/**
 * 小红书订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Service
public class XhsOrderServiceImpl implements IXhsOrderService 
{
    @Autowired
    private XhsOrderMapper xhsOrderMapper;
    @Autowired
    private XhsOrderReceiverMapper receiverMapper;
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
     * 查询小红书订单
     * 
     * @param id 小红书订单主键
     * @return 小红书订单
     */
    @Override
    public XhsOrder selectXhsOrderById(Long id)
    {
        XhsOrder xhsOrder = xhsOrderMapper.selectXhsOrderById(id);
        XhsOrderReceiver receiver = receiverMapper.selectXhsOrderReceiverByOrderId(id);
        if(receiver!=null) {
            xhsOrder.setReceiver(receiver.getReceiver());
            xhsOrder.setPhone(receiver.getPhone());
            xhsOrder.setAddress(receiver.getAddress());
            xhsOrder.setProvince(receiver.getProvince());
            xhsOrder.setCity(receiver.getCity());
            xhsOrder.setDistrict(receiver.getDistrict());
        }
        return xhsOrder;
    }

    /**
     * 查询小红书订单列表
     * 
     * @param xhsOrder 小红书订单
     * @return 小红书订单
     */
    @Override
    public List<XhsOrder> selectXhsOrderList(XhsOrder xhsOrder)
    {
        var orderList = xhsOrderMapper.selectXhsOrderList(xhsOrder);
        for (var o:orderList) {
            List<XhsOrderItem> items = xhsOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setXhsOrderItemList(items);
        }
        return orderList;
    }

    /**
     * 新增小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertXhsOrder(XhsOrder xhsOrder)
    {
        xhsOrder.setShopType(7L);
        xhsOrder.setOrderType(1L);
        xhsOrder.setOrderStatus(2L);
        xhsOrder.setAfterSalesStatus(1L);
        xhsOrder.setCancelStatus(0L);
        xhsOrder.setOrderPaidTime(0L);
        xhsOrder.setOrderUpdateTime(0L);
        xhsOrder.setOrderDeliveryTime(0L);
        xhsOrder.setOrderCancelTime(0L);
        xhsOrder.setOrderFinishTime(0L);
        xhsOrder.setPromiseLastDeliveryTime(0L);
        xhsOrder.setOrderCreatedTime(xhsOrder.getCreateTime().getTime());
        xhsOrder.setPresaleDeliveryStartTime(0L);
        xhsOrder.setPresaleDeliveryEndTime(0L);
        Double totalAmount = (xhsOrder.getGoodsAmount() + xhsOrder.getShippingFree()) *100;
        xhsOrder.setTotalPayAmount(totalAmount.longValue());
        xhsOrder.setTotalShippingFree(xhsOrder.getShippingFree().longValue());
        xhsOrder.setOpenAddressId("0");
        xhsOrder.setAuditStatus(0L);
        xhsOrder.setSettleStatus(0L);
        xhsOrder.setSettleAmount(BigDecimal.ZERO);
        xhsOrder.setSendStatus(0L);
        xhsOrder.setCreateTime(DateUtils.getNowDate());

        int rows = xhsOrderMapper.insertXhsOrder(xhsOrder);
        insertXhsOrderItem(xhsOrder);

        // 地址
        XhsOrderReceiver receiver = new XhsOrderReceiver();
        receiver.setOrderId(xhsOrder.getId());
        receiver.setReceiver(xhsOrder.getReceiver());
        receiver.setPhone(xhsOrder.getPhone());
        receiver.setCountry("中国");
        receiver.setProvince(xhsOrder.getProvince());
        receiver.setCity(xhsOrder.getCity());
        receiver.setDistrict(xhsOrder.getDistrict());
        receiver.setAddress(xhsOrder.getAddress());
        receiverMapper.insertXhsOrderReceiver(receiver);

        return rows;
    }

    @Transactional
    @Override
    public int confirmOrder(XhsOrder bo) {
        XhsOrder original = xhsOrderMapper.selectXhsOrderById(bo.getId());
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
        so.setRemark(original.getSellerRemark());
        so.setBuyerMemo(original.getCustomerRemark());
        so.setTag("");
        so.setRefundStatus(1);
        so.setOrderStatus(1);
        BigDecimal shipFee = BigDecimal.valueOf(original.getTotalShippingFree()).divide(BigDecimal.valueOf(100));
        BigDecimal payAmount = BigDecimal.valueOf(original.getTotalPayAmount()).divide(BigDecimal.valueOf(100));
        so.setGoodsAmount(payAmount.subtract(shipFee));
        so.setDiscountAmount(BigDecimal.ZERO);
        so.setAmount(payAmount);
        so.setPostage(shipFee);
        try {
            so.setPayTime(new Date(original.getOrderPaidTime()));
        }catch (Exception e){}

//        XhsOrderReceiver receiver = receiverMapper.selectXhsOrderReceiverByOrderId(original.getId());
        so.setReceiverName(bo.getReceiver());
        so.setReceiverPhone(bo.getPhone());
        so.setAddress(bo.getAddress());
        so.setCountry("中国");
        so.setProvince(bo.getProvince());
        so.setCity(bo.getCity());
        so.setTown(bo.getDistrict());
        so.setConfirmTime(new Date());
        so.setCreateTime(new Date());
        so.setCreateBy(bo.getUpdateBy());
        erpOrderMapper.insertErpOrder(so);

        // 添加Erp_order_item
        List<XhsOrderItem> orderItems = xhsOrderMapper.selectOrderItemByOrderId(original.getId());
        List<ErpOrderItem> items = new ArrayList<>();
        for (var i:orderItems) {
            if(StringUtils.isEmpty(i.getItemSpecCode())) return -11;
            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getItemSpecCode());
            if (spec == null) return -11;
            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
            if(goods == null) return -12;

            ErpOrderItem item = new ErpOrderItem();
            item.setOrderId(so.getId());
            item.setOrderItemNum(i.getId()+"");
            item.setSupplierId(goods.getSupplierId().intValue());
            item.setGoodsId(spec.getGoodsId());
            item.setSpecId(spec.getId());
            item.setGoodsTitle(i.getItemName());
            item.setGoodsImg(i.getItemImage());
            item.setGoodsNum(i.getErpCode());
            item.setSpecNum(i.getItemSpecCode());
            item.setGoodsSpec(i.getItemSpec());
            item.setGoodsPrice(i.getPrice());
            item.setGoodsPurPrice(spec.getPurPrice());
            item.setItemAmount(BigDecimal.valueOf(i.getTotalPaidAmount()));
            item.setQuantity(i.getQuantity().intValue());
            item.setIsGift(i.getItemTag().intValue());
            item.setRefundCount(0);
            item.setRefundStatus(1);
            item.setCreateBy(bo.getUpdateBy());
            item.setCreateTime(new Date());
            items.add(item);
        }
        // 添加了赠品
        if(bo.getXhsOrderItemList()!=null && !bo.getXhsOrderItemList().isEmpty()) {
            for (var i : bo.getXhsOrderItemList()) {
                if(StringUtils.isEmpty(i.getItemSpecCode())) return -11;
                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getItemSpecCode());
                if (spec == null) return -11;
                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
                if(goods == null) return -12;

                ErpOrderItem item = new ErpOrderItem();
                item.setOrderId(so.getId());
                item.setOrderItemNum(original.getOrderId()+"_");
                item.setSupplierId(goods.getSupplierId().intValue());
                item.setGoodsId(spec.getGoodsId());
                item.setSpecId(spec.getId());
                item.setGoodsTitle(i.getItemName());
                item.setGoodsImg(i.getItemImage());
                item.setGoodsNum(i.getErpCode());
                item.setSpecNum(i.getItemSpecCode());
                item.setGoodsSpec(i.getItemSpec());
                item.setGoodsPrice(i.getPrice());
                item.setGoodsPurPrice(spec.getPurPrice());
                item.setItemAmount(BigDecimal.valueOf(i.getItemAmount()));
                item.setQuantity(i.getQuantity().intValue());
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
//                agentShipping.setOrderItemId(it.getId().toString());
                agentShipping.setErpOrderId(so.getId());
                agentShipping.setErpOrderItemId(it.getId());
                try {
                    agentShipping.setOrderDate(new Date(original.getOrderCreatedTime()));
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
                    shipping.setOrderDate(new Date(original.getOrderCreatedTime()));
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
        XhsOrder up =new XhsOrder();
        up.setId(original.getId());
        up.setAuditStatus(1L);
        up.setUpdateBy(bo.getUpdateBy());
        up.setUpdateTime(new Date());
        xhsOrderMapper.updateXhsOrder(up);
        return 1;
    }

    /**
     * 修改小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateXhsOrder(XhsOrder xhsOrder)
    {
        xhsOrder.setUpdateTime(DateUtils.getNowDate());
        xhsOrderMapper.deleteXhsOrderItemByOrderId(xhsOrder.getId());
        insertXhsOrderItem(xhsOrder);
        return xhsOrderMapper.updateXhsOrder(xhsOrder);
    }

    /**
     * 批量删除小红书订单
     * 
     * @param ids 需要删除的小红书订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteXhsOrderByIds(Long[] ids)
    {
        xhsOrderMapper.deleteXhsOrderItemByOrderIds(ids);
        return xhsOrderMapper.deleteXhsOrderByIds(ids);
    }

    /**
     * 删除小红书订单信息
     * 
     * @param id 小红书订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteXhsOrderById(Long id)
    {
        xhsOrderMapper.deleteXhsOrderItemByOrderId(id);
        return xhsOrderMapper.deleteXhsOrderById(id);
    }

    /**
     * 新增小红书订单明细信息
     * 
     * @param xhsOrder 小红书订单对象
     */
    public void insertXhsOrderItem(XhsOrder xhsOrder)
    {
        List<XhsOrderItem> xhsOrderItemList = xhsOrder.getXhsOrderItemList();
        Long id = xhsOrder.getId();
        if (StringUtils.isNotNull(xhsOrderItemList))
        {
            List<XhsOrderItem> list = new ArrayList<XhsOrderItem>();
            for (XhsOrderItem xhsOrderItem : xhsOrderItemList)
            {
                Double t = xhsOrderItem.getItemAmount()*100;
                xhsOrderItem.setTotalPaidAmount(t.longValue());
                xhsOrderItem.setTotalMerchantDiscount(0L);
                xhsOrderItem.setTotalRedDiscount(0L);
                xhsOrderItem.setItemTag(0L);
                xhsOrderItem.setErpSendStatus(0L);
                xhsOrderItem.setOrderId(id);
                list.add(xhsOrderItem);
            }
            if (list.size() > 0)
            {
                xhsOrderMapper.batchXhsOrderItem(list);
            }
        }
    }
}
