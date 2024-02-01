package com.qihang.erp.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.qihang.erp.api.domain.*;
import com.qihang.erp.api.mapper.FmsPayableShipFeeMapper;
import com.qihang.erp.api.mapper.FmsReceivableOrderMapper;
import com.qihang.erp.api.mapper.WmsOrderShippingMapper;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.mapper.ErpOrderMapper;
import com.qihang.erp.api.service.IErpOrderService;

/**
 * 订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-04
 */
@Service
public class ErpOrderServiceImpl implements IErpOrderService 
{
    @Autowired
    private ErpOrderMapper erpOrderMapper;

    @Autowired
    private WmsOrderShippingMapper wmsOrderShippingMapper;
    @Autowired
    private FmsReceivableOrderMapper fmsReceivableOrderMapper;
    @Autowired
    private FmsPayableShipFeeMapper fmsPayableShipFeeMapper;

    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public ErpOrder selectErpOrderById(Long id)
    {
        return erpOrderMapper.selectErpOrderById(id);
    }

    /**
     * 查询订单列表
     * 
     * @param erpOrder 订单
     * @return 订单
     */
    @Override
    public List<ErpOrder> selectErpOrderList(ErpOrder erpOrder)
    {
        List<ErpOrder> orderList = erpOrderMapper.selectErpOrderList(erpOrder);
        for (var o:orderList) {
            List<ErpOrderItem> items = erpOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setItemList(items);
        }
        return orderList;
    }

    /**
     * 新增订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertErpOrder(ErpOrder erpOrder)
    {
        ErpOrder order = erpOrderMapper.selectErpOrderByNum(erpOrder.getOrderNum());
        if (order!=null&& order.getId()>0) return -1;// 订单号已存在
//        erpOrder.setCreateTime(DateUtils.getNowDate());
//        int rows = erpOrderMapper.insertErpOrder(erpOrder);
//        insertErpOrderItem(erpOrder);
//        return rows;
        if(erpOrder.getItemList() == null || erpOrder.getItemList().size() == 0) return -1;

        if(erpOrder.getShopId() == 1) erpOrder.setShopType(99);
        else if(erpOrder.getShopId() == 5) erpOrder.setShopType(5);
        else if(erpOrder.getShopId() == 6) erpOrder.setShopType(4);
        else if(erpOrder.getShopId() == 13) erpOrder.setShopType(13);
        else if(erpOrder.getShopId() == 21) erpOrder.setShopType(7);
        else if(erpOrder.getShopId() == 22) erpOrder.setShopType(6);

        erpOrder.setOrderStatus(1);
        erpOrder.setRefundStatus(1);
        if(erpOrder.getPostage() == null) erpOrder.setPostage(BigDecimal.ZERO);
        if(erpOrder.getDiscountAmount() == null) erpOrder.setDiscountAmount(BigDecimal.ZERO);

        // 实际金额 = 商品金额 - 折扣金额 + 运费
        erpOrder.setAmount(erpOrder.getGoodsAmount().subtract(erpOrder.getDiscountAmount()).add(erpOrder.getPostage()));

//        if(erpOrder.getPayAmount() == null)shopOrder.setPayAmount(0L);
//        if(erpOrder.getAuditStatus() == null) shopOrder.setAuditStatus(1L);

        erpOrder.setCreateBy(erpOrder.getCreateBy());
        erpOrder.setCreateTime(DateUtils.getNowDate());
        int rows = erpOrderMapper.insertErpOrder(erpOrder);
        insertErpOrderItem(erpOrder);
//        insertSShopOrderItem(shopOrder);
        return rows;
    }

    /**
     * 发货
     * @param erpOrder
     * @return
     */
    @Transactional
    @Override
    public int shipErpOrder(ErpOrder erpOrder) {
        ErpOrder order = erpOrderMapper.selectErpOrderById(erpOrder.getId());
        if (order==null) return -1;// 订单不存在
        else if(order.getOrderStatus() != 2) return -2;//状态不对
        // 发货
        ErpOrder update = new ErpOrder();
        update.setId(order.getId());
        update.setUpdateTime(new Date());
        update.setUpdateBy(erpOrder.getUpdateBy());
        update.setShippingTime(new Date());
        update.setShippingMan(erpOrder.getShippingMan());
        update.setShippingCompany(erpOrder.getShippingCompany());
        update.setShippingNumber(erpOrder.getShippingNumber());
        update.setShippingCost(erpOrder.getShippingCost());
        update.setWidth(erpOrder.getWidth());
        update.setWeight(erpOrder.getWeight());
        update.setHeight(erpOrder.getHeight());
        update.setLength(erpOrder.getLength());
        update.setOrderStatus(3);
        erpOrderMapper.updateErpOrder(update);

        // 更新 wms_order_shipping
        WmsOrderShipping select = new WmsOrderShipping();
        select.setErpOrderId(order.getId());
        List<WmsOrderShipping> shipList = wmsOrderShippingMapper.selectWmsOrderShippingList(select);
        if(shipList!=null){
            for (WmsOrderShipping ship:shipList) {
                WmsOrderShipping up = new WmsOrderShipping();
                up.setId(ship.getId());
                up.setStatus(3L);
                up.setUpdateTime(new Date());
                up.setUpdateBy(erpOrder.getUpdateBy());
                wmsOrderShippingMapper.updateWmsOrderShipping(up);
            }
        }

        // 生成订单收入fms_receivable_order
        List<ErpOrderItem> erpOrderItems = erpOrderMapper.selectOrderItemByOrderId(erpOrder.getId());
        for (ErpOrderItem item : erpOrderItems) {
            FmsReceivableOrder fro = new FmsReceivableOrder();
            fro.setDate(new Date());
            fro.setOrderNum(order.getOrderNum());
            fro.setOrderId(order.getId());
            fro.setOrderItemId(item.getId());
            fro.setGoodsId(item.getGoodsId());
            fro.setGoodsName(item.getGoodsTitle());
            fro.setSpecId(item.getSpecId());
            fro.setSpecName(item.getGoodsSpec());
            fro.setPrice(item.getGoodsPrice());
            fro.setQuantity(item.getQuantity().longValue());
            fro.setAmount(item.getItemAmount());
            fro.setStatus(0L);
            fro.setCreateTime(new Date());
            fro.setCreateBy(erpOrder.getUpdateBy());
            fmsReceivableOrderMapper.insertFmsReceivableOrder(fro);
        }

        // 生成物流费用 fms_payable_ship_fee
        FmsPayableShipFee sf = new FmsPayableShipFee();
        sf.setDate(new Date());
        sf.setOrderNum(order.getOrderNum());
        sf.setShopId(order.getShopId().longValue());
        sf.setLogisticsCompany(erpOrder.getShippingCompany());
        sf.setLogisticsNum(erpOrder.getShippingNumber());
        sf.setAmount(erpOrder.getShippingCost());
        sf.setStatus(0L);
        sf.setCreateTime(new Date());
        sf.setCreateBy(erpOrder.getUpdateBy());
        sf.setWidth(erpOrder.getWidth());
        sf.setWeight(erpOrder.getWeight());
        sf.setHeight(erpOrder.getHeight());
        sf.setLength(erpOrder.getLength());
        sf.setReceiverName(order.getReceiverName());
        sf.setReceiverPhone(order.getReceiverPhone());
        sf.setProvince(order.getProvince());
        sf.setCity(order.getCity());
        sf.setTown(order.getTown());

        fmsPayableShipFeeMapper.insertFmsPayableShipFee(sf);
        return 1;
    }

    /**
     * 修改订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateErpOrder(ErpOrder erpOrder)
    {
        erpOrder.setUpdateTime(DateUtils.getNowDate());
        erpOrderMapper.deleteErpOrderItemByOrderId(erpOrder.getId());
        insertErpOrderItem(erpOrder);
        return erpOrderMapper.updateErpOrder(erpOrder);
    }

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpOrderByIds(Long[] ids)
    {
        erpOrderMapper.deleteErpOrderItemByOrderIds(ids);
        return erpOrderMapper.deleteErpOrderByIds(ids);
    }

    /**
     * 删除订单信息
     * 
     * @param id 订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpOrderById(Long id)
    {
        erpOrderMapper.deleteErpOrderItemByOrderId(id);
        return erpOrderMapper.deleteErpOrderById(id);
    }

    /**
     * 新增订单明细信息
     * 
     * @param erpOrder 订单对象
     */
    public void insertErpOrderItem(ErpOrder erpOrder)
    {
        List<ErpOrderItem> erpOrderItemList = erpOrder.getItemList();
        Long id = erpOrder.getId();
        if (StringUtils.isNotNull(erpOrderItemList))
        {
            List<ErpOrderItem> list = new ArrayList<ErpOrderItem>();
            for (ErpOrderItem erpOrderItem : erpOrderItemList)
            {


                erpOrderItem.setOrderId(id);
                erpOrderItem.setRefundCount(0);
                erpOrderItem.setRefundStatus(1);
                erpOrderItem.setCreateBy(erpOrder.getCreateBy());
                erpOrderItem.setCreateTime(new Date());
                list.add(erpOrderItem);
            }
            if (list.size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(list);
            }
        }
    }
}
