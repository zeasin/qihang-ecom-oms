//package com.qihang.erp.api.service.impl;
//
//import java.util.Date;
//import java.util.List;
//import com.zhijian.common.utils.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import com.zhijian.common.utils.StringUtils;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * 店铺订单Service业务层处理
// *
// * @author qihang
// * @date 2023-12-31
// */
//@Service
//public class ShopOrderServiceImpl implements IShopOrderService
//{
//    @Autowired
//    private ShopOrderMapper shopOrderMapper;
//
//    /**
//     * 查询店铺订单
//     *
//     * @param id 店铺订单主键
//     * @return 店铺订单
//     */
//    @Override
//    public ShopOrder selectShopOrderById(Long id)
//    {
//        return shopOrderMapper.selectShopOrderById(id);
//    }
//
//    /**
//     * 查询店铺订单列表
//     *
//     * @param shopOrder 店铺订单
//     * @return 店铺订单
//     */
//    @Override
//    public List<ShopOrder> selectShopOrderList(ShopOrder shopOrder)
//    {
//        return shopOrderMapper.selectShopOrderList(shopOrder);
//    }
//
//    /**
//     * 新增店铺订单
//     *
//     * @param shopOrder 店铺订单
//     * @return 结果
//     */
//    @Transactional
//    @Override
//    public int insertShopOrder(ShopOrder shopOrder)
//    {
//        if(shopOrder.getItemList() == null || shopOrder.getItemList().size() == 0) return -1;
//        shopOrder.setOrderStatus(1L);
//        shopOrder.setRefundStatus(1L);
//        if(shopOrder.getPostage() == null)shopOrder.setPostage(0L);
//        if(shopOrder.getDiscountAmount() == null)shopOrder.setDiscountAmount(0L);
//        if(shopOrder.getPayAmount() == null)shopOrder.setPayAmount(0L);
//        if(shopOrder.getAuditStatus() == null) shopOrder.setAuditStatus(1L);
//        shopOrder.setCreateBy(shopOrder.getCreateBy());
//        shopOrder.setCreateTime(DateUtils.getNowDate());
//        int rows = shopOrderMapper.insertShopOrder(shopOrder);
//        insertSShopOrderItem(shopOrder);
//        return rows;
//    }
//
//    /**
//     * 修改店铺订单
//     *
//     * @param shopOrder 店铺订单
//     * @return 结果
//     */
//    @Transactional
//    @Override
//    public int updateShopOrder(ShopOrder shopOrder)
//    {
//        if(shopOrder.getItemList() == null || shopOrder.getItemList().size() == 0) return -1;
//        shopOrder.setUpdateTime(DateUtils.getNowDate());
//        shopOrderMapper.deleteSShopOrderItemByOrderId(shopOrder.getId());
//        insertSShopOrderItem(shopOrder);
//        return shopOrderMapper.updateShopOrder(shopOrder);
//    }
//
//    /**
//     * 批量删除店铺订单
//     *
//     * @param ids 需要删除的店铺订单主键
//     * @return 结果
//     */
//    @Transactional
//    @Override
//    public int deleteShopOrderByIds(Long[] ids)
//    {
//        shopOrderMapper.deleteSShopOrderItemByOrderIds(ids);
//        return shopOrderMapper.deleteShopOrderByIds(ids);
//    }
//
//    /**
//     * 删除店铺订单信息
//     *
//     * @param id 店铺订单主键
//     * @return 结果
//     */
//    @Transactional
//    @Override
//    public int deleteShopOrderById(Long id)
//    {
//        shopOrderMapper.deleteSShopOrderItemByOrderId(id);
//        return shopOrderMapper.deleteShopOrderById(id);
//    }
//
//    /**
//     * 新增${subTable.functionName}信息
//     *
//     * @param shopOrder 店铺订单对象
//     */
//    public void insertSShopOrderItem(ShopOrder shopOrder)
//    {
//        List<SShopOrderItem> sShopOrderItemList = shopOrder.getItemList();
//        Long id = shopOrder.getId();
//        if (StringUtils.isNotNull(sShopOrderItemList))
//        {
//            List<SShopOrderItem> list = new ArrayList<SShopOrderItem>();
//            for (SShopOrderItem sShopOrderItem : sShopOrderItemList)
//            {
//                sShopOrderItem.setOrderId(id);
//                sShopOrderItem.setRefundCount(0L);
//                sShopOrderItem.setRefundStatus(1L);
//                sShopOrderItem.setCreateBy(shopOrder.getCreateBy());
//                sShopOrderItem.setCreateTime(new Date());
//                list.add(sShopOrderItem);
//            }
//            if (list.size() > 0)
//            {
//                shopOrderMapper.batchSShopOrderItem(list);
//            }
//        }
//    }
//}
