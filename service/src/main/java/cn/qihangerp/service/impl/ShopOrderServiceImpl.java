package cn.qihangerp.service.impl;

import cn.qihangerp.common.*;
import cn.qihangerp.enums.EnumShopType;
import cn.qihangerp.mapper.*;
import cn.qihangerp.model.bo.PosOrderCreateBo;
import cn.qihangerp.model.bo.ShopOrderCreateBo;
import cn.qihangerp.model.entity.*;
import cn.qihangerp.model.query.ShopOrderQueryBo;
import cn.qihangerp.service.ShopOrderService;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【oms_shop_order】的数据库操作Service实现
* @createDate 2025-07-15 11:36:42
*/
@Slf4j
@AllArgsConstructor
@Service
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder>
    implements ShopOrderService {
    private final OOrderMapper orderMapper;
//    private final OOrderStockingMapper orderStockMapper;
    private final ShopOrderItemMapper itemMapper;
    private final ShopGoodsSkuMapper shopGoodsSkuMapper;
    private final ShopGoodsMapper shopGoodsMapper;
//    private final ShopMemberMapper shopMemberMapper;
    private final OShopMapper shopMapper;
    private final ErpMerchantMapper merchantMapper;
//    private final OMarketingDiscountRuleMapper discountRuleMapper;
    private final ShopOrderPromotionMapper shopOrderPromotionMapper;


    /**
     * 普通发货订单
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult<ShopOrder> queryOrderPageList(ShopOrderQueryBo bo, PageQuery pageQuery) {
        Long startTimestamp = null;
        Long endTimestamp = null;

        if(StringUtils.hasText(bo.getStartTime())){
            boolean b = DateHelper.isValidDate(bo.getStartTime());
            if(!b){
//                bo.setStartTime(bo.getStartTime()+" 00:00:00");
                bo.setStartTime("");
            }
        }
        if(StringUtils.hasText(bo.getEndTime())){
            boolean b = DateHelper.isValidDate(bo.getEndTime());
            if(b){
//                bo.setEndTime(bo.getEndTime()+" 23:59:59");
                bo.setEndTime("");
            }
        }else{
            bo.setEndTime(bo.getStartTime());
        }


        if(StringUtils.hasText(bo.getStartTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(bo.getStartTime()+" 00:00:00", formatter);
            LocalDateTime endTime = LocalDateTime.parse(bo.getEndTime()+" 23:59:59", formatter);

            startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
            endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));
        }
        pageQuery.setOrderByColumn("order_time");
        pageQuery.setIsAsc("desc");


        LambdaQueryWrapper<ShopOrder> queryWrapper = new LambdaQueryWrapper<ShopOrder>()
                .eq(bo.getOrderStatus()!=null, ShopOrder::getOrderStatus,bo.getOrderStatus())
                .eq(bo.getRefundStatus()!=null, ShopOrder::getRefundStatus,bo.getRefundStatus())
                .eq(bo.getShopId()!=null, ShopOrder::getShopId,bo.getShopId())
                .eq(bo.getShopType()!=null, ShopOrder::getShopType,bo.getShopType())
                .eq(bo.getMerchantId()!=null, ShopOrder::getMerchantId,bo.getMerchantId())
                .ge(startTimestamp!=null, ShopOrder::getOrderTime,startTimestamp)
                .le(endTimestamp!=null, ShopOrder::getOrderTime,endTimestamp)
                .like(StringUtils.hasText(bo.getOrderId()), ShopOrder::getOrderId,bo.getOrderId())
                .eq(ShopOrder::getDeliverMethod,0)
                ;

        Page<ShopOrder> orderPage = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        if(orderPage.getRecords()!=null){
            for (var order:orderPage.getRecords()) {
                order.setItems(itemMapper.selectList(
                        new LambdaQueryWrapper<ShopOrderItem>()
                                .eq(ShopOrderItem::getShopOrderId,order.getId()))
                );
            }
        }
        return PageResult.build(orderPage);
    }


    @Override
    public ShopOrder queryDetailById(Long orderId) {
        ShopOrder shopOrder = this.baseMapper.selectById(orderId);
        if(shopOrder!=null){
            shopOrder.setItems(itemMapper.selectList(
                    new LambdaQueryWrapper<ShopOrderItem>()
                            .eq(ShopOrderItem::getShopOrderId,shopOrder.getId())));



//            if(StringUtils.hasText(shopOrder.getPlatformUserId())) {
//                List<ShopMember> shopMembers = shopMemberMapper.selectList(new LambdaQueryWrapper<ShopMember>().eq(ShopMember::getPlatformUserId, shopOrder.getPlatformUserId()));
//                if(shopMembers!=null&&shopMembers.size()>0) {
//                    shopOrder.setMember(shopMembers.get(0));
//                }
//            }
        }
        return shopOrder;
    }

    @Override
    public ShopOrder queryDetailByOrderNo(String orderNo) {
        var shopOrders = this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>().eq(ShopOrder::getOrderId, orderNo));
        if(shopOrders!=null&&shopOrders.size()>0){
            return shopOrders.get(0);
        }else return null;
    }

    @Override
    public List<ShopOrderItem> queryOrderItemList(Long shopOrderId) {
        return itemMapper.selectList(
                new LambdaQueryWrapper<ShopOrderItem>()
                        .eq(ShopOrderItem::getShopOrderId,shopOrderId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> updateShopOrderLogistic(String shopOrderNo, String tradeNo, String logisticCompany, String logisticCode, Date consignTime) {
        List<ShopOrder> shopOrderList = this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>().eq(ShopOrder::getOrderId, shopOrderNo));
        if(shopOrderList!=null&&shopOrderList.size()>0){
            log.info("======更新店铺订单物流，找到订单了：{}",shopOrderList.size());
            ShopOrder shopOrder = new  ShopOrder();
            shopOrder.setId(shopOrderList.get(0).getId());
            shopOrder.setOrderStatus(2);
            shopOrder.setLogisticsPartnerCode(logisticCompany);
            shopOrder.setLogisticsOrderNo(logisticCode);
            if(consignTime!=null){
                shopOrder.setShipDoneTime(consignTime.toInstant().getEpochSecond());
            }
            shopOrder.setErpShipStatus(2);
            shopOrder.setErpShipCompany(logisticCompany);
            shopOrder.setErpShipCode(logisticCode);
            shopOrder.setErpShipTime(consignTime);
            shopOrder.setUpdateOn(new Date());
            this.baseMapper.updateById(shopOrder);

            // 更新订单库
            List<OOrder> oOrders = orderMapper.selectList(new LambdaQueryWrapper<OOrder>().eq(OOrder::getOrderNum, shopOrderNo).eq(OOrder::getShopId, shopOrder.getShopId()));
            if(oOrders!=null&&oOrders.size()>0){
                log.info("======更新店铺订单物流，找到了订单库数据：{}",oOrders.get(0).getId());
                OOrder oOrder = new OOrder();
                oOrder.setId(oOrders.get(0).getId());
                oOrder.setOrderStatus(oOrders.get(0).getOrderStatus()==1?2:null);
                oOrder.setWaybillCompany(logisticCompany);
                oOrder.setWaybillCode(logisticCode);
                oOrder.setUpdateBy("自动更新物流");
                oOrder.setUpdateTime(new Date());
                orderMapper.updateById(oOrder);
            }else{
                log.error("======更新店铺订单物流，未找到订单库订单");
            }

            //  更新 发货订单表
            // 先处理发货订单（其实就是推送过去发货的订单）
//            List<OOrderStocking> oOrderStockings = orderStockMapper.selectList(new LambdaQueryWrapper<OOrderStocking>().eq(OOrderStocking::getShippingErpOrderCode, tradeNo));
//            if(oOrderStockings!=null&&oOrderStockings.size()>0){
//                log.info("======更新店铺订单物流，找到了tradeNO订单：{}",tradeNo);
//                OOrderStocking oOrderStocking = new OOrderStocking();
//                oOrderStocking.setId(oOrderStockings.get(0).getId());
//                oOrderStocking.setShippingCompany(logisticCompany);
//                oOrderStocking.setShippingNumber(logisticCode);
//                oOrderStocking.setShippingTime(consignTime);
//                oOrderStocking.setSendStatus(2);
//                orderStockMapper.updateById(oOrderStocking);
//            }else{
//                // 继续查找发货单
//                List<OOrderStocking> oOrderStockings1 = orderStockMapper.selectList(new LambdaQueryWrapper<OOrderStocking>().eq(OOrderStocking::getOrderNum, shopOrderNo));
//                if(oOrderStockings1!=null&&oOrderStockings1.size()>0){
//                    log.info("======更新店铺订单物流，找到了shopOrderNO订单：{}",shopOrderNo);
//                    OOrderStocking oOrderStocking = new OOrderStocking();
//                    oOrderStocking.setId(oOrderStockings1.get(0).getId());
//                    oOrderStocking.setShippingCompany(logisticCompany);
//                    oOrderStocking.setShippingNumber(logisticCode);
//                    oOrderStocking.setShippingTime(consignTime);
//                    oOrderStocking.setSendStatus(2);
//                    orderStockMapper.updateById(oOrderStocking);
//                }else{
//                    log.info("======更新店铺订单物流，未找到发货单");
//                }
//            }
            return ResultVo.success();
        }else{
            log.error("======更新店铺订单物流，没有找到订单:{}",shopOrderNo);
            return ResultVo.error("不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> saveOrder(Long shopId,Long merchantId,Integer shopType, ShopOrder order) {
        if (order == null) return ResultVo.error(ResultVoEnum.SystemException);

//        OShop shop = shopService.getById(shopId);
//        if (shop == null) return ResultVo.error("店铺不存在");
        order.setShopId(shopId);
        order.setMerchantId(merchantId);
        order.setShopType(shopType);

        List<ShopOrder> shopOrderList = this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>()
                .eq(ShopOrder::getOrderId, order.getOrderId())
                .eq(ShopOrder::getShopId, shopId)
        );
        Long shopOrderId=null;
        if (shopOrderList != null && shopOrderList.size() > 0) {
            // 存在，修改
//            ShopOrder update = new ShopOrder();
//            update.setId(shopOrderList.get(0).getId());
//            update.setOrderStatus(order.getOrderStatus());
//            update.setPlatformOrderStatusText(order.getPlatformOrderStatusText());
//            update.setPlatformOrderStatus(order.getPlatformOrderStatus());
//            update.setSellerMemo(order.getSellerMemo());
//            update.setBuyerMemo(order.getBuyerMemo());
//            update.setRemark(order.getRemark());
//            update.setRefundStatus(order.getRefundStatus());
//            update.setUpdateTime(order.getUpdateTime());
//            update.setUpdateTimeText(order.getUpdateTimeText());
//            update.setUpdateOn(new Date());
            if(StringUtils.isEmpty(order.getProvince())) order.setProvince(null);
            if(StringUtils.isEmpty(order.getCity())) order.setCity(null);
            if(StringUtils.isEmpty(order.getTown())) order.setTown(null);
            if(StringUtils.isEmpty(order.getCounty())) order.setCounty(null);
            if(StringUtils.isEmpty(order.getReceiverName())) order.setReceiverName(null);
            if(StringUtils.isEmpty(order.getReceiverPhone())) order.setReceiverPhone(null);
            if(StringUtils.isEmpty(order.getAddress())) order.setAddress(null);
            if(StringUtils.isEmpty(order.getEncryptPostAddress())) order.setEncryptPostAddress(null);
            if(StringUtils.isEmpty(order.getEncryptPostTel())) order.setEncryptPostTel(null);
            if(StringUtils.isEmpty(order.getEncryptPostReceiver())) order.setEncryptPostReceiver(null);
            order.setId(shopOrderList.get(0).getId());
            order.setUpdateOn(new Date());
            this.baseMapper.updateById(order);
            shopOrderId = order.getId();
        } else {
            // 不存在，新增
            order.setOrderType(0);
            order.setOrderMode(0);
            order.setErpShipStatus(0);
            order.setConfirmStatus(0);
            order.setCreateOn(new Date());
            this.baseMapper.insert(order);
            shopOrderId = order.getId();
            // 如果平台会员ID存在，则添加会员到会员表
//            if(StringUtils.hasText(order.getPlatformUserId())) {
//                ShopMember shopMember = new ShopMember();
//                shopMember.setShopType(order.getShopType());
//                shopMember.setShopId(order.getShopId());
//                shopMember.setMerchantId(order.getMerchantId());
//                shopMember.setPlatformUserId(order.getPlatformUserId());
//                shopMember.setPlatformAccount(order.getPlatformAccount());
//                shopMember.setName(order.getReceiverName());
//                shopMember.setPhone(order.getReceiverPhone());
//                shopMember.setProvince(order.getProvince());
//                shopMember.setCity(order.getCity());
//                shopMember.setCounty(order.getCounty());
//                shopMember.setTown(order.getTown());
//                shopMember.setAddress(order.getAddress());
//                shopMember.setStatus(0);
//                shopMember.setCreateOn(new Date());
//                shopMemberMapper.insert(shopMember);
//            }
        }
        // 添加订单item
        for (ShopOrderItem item : order.getItems()) {
            item.setShopId(shopId);
            item.setShopType(order.getShopType());
            item.setMerchantId(order.getMerchantId());
            if (StringUtils.hasText(item.getSkuId())) {
                List<ShopGoodsSku> shopGoodsSkuList = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>()
                        .eq(ShopGoodsSku::getSkuId, item.getSkuId())
                        .eq(ShopGoodsSku::getShopId, shopId)
                );
                if (shopGoodsSkuList != null&&!shopGoodsSkuList.isEmpty()) {
                    item.setErpGoodsId(shopGoodsSkuList.get(0).getErpGoodsId());
                    item.setErpGoodsSkuId(shopGoodsSkuList.get(0).getErpGoodsSkuId());
                }
            } else {
                List<ShopGoods> shopGoodsList = shopGoodsMapper.selectList(new LambdaQueryWrapper<ShopGoods>().eq(ShopGoods::getProductId, item.getProductId()));
                if (!shopGoodsList.isEmpty()) {
                    item.setErpGoodsId(shopGoodsList.get(0).getErpGoodsId());
                    item.setErpGoodsSkuId(0L);
                }
            }
            List<ShopOrderItem> orderItems = null;

            if(StringUtils.hasText(item.getSubOrderId())){
                // 用子订单号查询订单item
                orderItems = itemMapper.selectList(
                        new LambdaQueryWrapper<ShopOrderItem>()
                                .eq(ShopOrderItem::getOrderId, item.getOrderId())
                                .eq(ShopOrderItem::getSubOrderId, item.getSubOrderId())
                );
            }else {
                // 有skuid查询订单item
                orderItems = itemMapper.selectList(
                        new LambdaQueryWrapper<ShopOrderItem>()
                                .eq(ShopOrderItem::getOrderId, item.getOrderId())
                                .eq(ShopOrderItem::getProductId, item.getProductId())
                                .eq(StringUtils.hasText(item.getSkuId()), ShopOrderItem::getSkuId, item.getSkuId())
                );
            }

            if (orderItems != null && orderItems.size() > 0) {
                // 更新
                item.setId(orderItems.get(0).getId());
                item.setShopOrderId(shopOrderId);
                item.setUpdateOn(new Date());
                itemMapper.updateById(item);
            } else {
                // 新增
                item.setShopOrderId(shopOrderId);
                item.setCreateOn(new Date());
                itemMapper.insert(item);
            }
        }

        if (shopOrderList != null && shopOrderList.size() > 0) {
            return ResultVo.error(ResultVoEnum.DataExist.getIndex(), "订单已经存在，更新成功",shopOrderId);
        } else {
            return ResultVo.success(shopOrderId);
        }

    }



    /**
     * 取消订单
     * @param id 店铺订单id
     * @param cancelReason 取消原因
     * @param man 操作人
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo cancelOrder(Long id, String cancelReason, String man) {
        ShopOrder shopOrder = this.baseMapper.selectById(id);
        if (shopOrder == null) return ResultVo.error("找不到订单数据");
        else if (shopOrder.getOrderStatus().intValue() == 3) return ResultVo.error("已完成的单不可以取消");
        // 取消订单
        ShopOrder update = new ShopOrder();
        update.setId(id);
        update.setCancelReason(man + " 操作取消订单，取消原因：" + cancelReason);
        update.setUpdateOn(new Date());
        update.setOrderStatus(11);
        this.baseMapper.updateById(update);

        // 回滚确认订单状态
        List<ShopOrder> shopOrders = this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>()
                .eq(ShopOrder::getOrderId, shopOrder.getOrderId())
                .eq(ShopOrder::getDeliverMethod, 1));
        if (shopOrders != null && shopOrders.size() > 0) {
            for (ShopOrder order : shopOrders) {
                ShopOrder shopOrderUpdate = new ShopOrder();
                shopOrderUpdate.setId(order.getId());
                shopOrderUpdate.setConfirmStatus(0);
                shopOrderUpdate.setUpdateOn(new Date());
                this.baseMapper.updateById(shopOrderUpdate);
            }
        }

        return ResultVo.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> createOrder(ShopOrderCreateBo order, String man) {
        if (order == null) return ResultVo.error("数据错误");
        if(order.getItemList() == null || order.getItemList().isEmpty()) return ResultVo.error("数据错误：没有商品");
        OShop shop = shopMapper.selectById(order.getShopId());
        if (shop == null) return ResultVo.error("店铺不存在");
        // 查订单号
        List<ShopOrder> shopOrders = this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>()
                .eq(ShopOrder::getShopId, shop.getId())
                .eq(ShopOrder::getOrderId, order.getOrderNum()));
        if(shopOrders!=null&&shopOrders.size()>0) {
            return ResultVo.error("订单号已存在");
        }
        // 查商品，并组合shopOrderItem
        List<ShopOrderItem> shopOrderItemList = new ArrayList<>();
        for(var item : order.getItemList()){
            // 查商品
            ShopGoodsSku shopGoodsSku = shopGoodsSkuMapper.selectById(item.getId());
            if(shopGoodsSku==null) return ResultVo.error("找不到商品数据，id:"+item.getId());
            if(shopGoodsSku.getShopId()!=shop.getId()) return ResultVo.error("店铺商品不属于你");
            if(shopGoodsSku.getErpGoodsSkuId()==null||shopGoodsSku.getErpGoodsSkuId()==0) return ResultVo.error("店铺skuId："+item.getId()+"未关联商品库商品sku");
            // 组合
            ShopOrderItem orderItem = new ShopOrderItem();
            orderItem.setMerchantId(shop.getMerchantId());
            orderItem.setShopId(shop.getId());
            orderItem.setShopType(shop.getType());
            orderItem.setProductId(shopGoodsSku.getProductId());
            orderItem.setSkuId(shopGoodsSku.getSkuId());
            orderItem.setOuterProductId(shopGoodsSku.getOuterProductId());
            orderItem.setOuterSkuId(shopGoodsSku.getOuterSkuId());
            orderItem.setTitle(shopGoodsSku.getProductTitle());
            orderItem.setSkuName(shopGoodsSku.getSkuName());
            orderItem.setSkuCode(shopGoodsSku.getSkuCode());
            orderItem.setBarcode(item.getBarcode());
            orderItem.setImg(shopGoodsSku.getImg());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSalePrice(shopGoodsSku.getPrice());
            orderItem.setMarketPrice(shopGoodsSku.getPrice());
            orderItem.setOnAftersaleSkuCnt(0);
            orderItem.setFinishAftersaleSkuCnt(0);
            orderItem.setRealPrice(item.getItemAmount().multiply(BigDecimal.valueOf(100)).intValue());
            orderItem.setItemAmount(item.getItemAmount().multiply(BigDecimal.valueOf(100)).intValue());
            orderItem.setIsChangePrice("false");
            orderItem.setIsDiscounted("false");
            orderItem.setDiscountAmount(0);
            orderItem.setErpGoodsId(shopGoodsSku.getErpGoodsId());
            orderItem.setErpGoodsSkuId(shopGoodsSku.getErpGoodsSkuId());
            orderItem.setOrderId(order.getOrderNum());
            orderItem.setOrderTime(System.currentTimeMillis()/1000);
            orderItem.setCreateOn(new Date());
            orderItem.setRefundStatus(1);
            orderItem.setRefundAmount(0);
            orderItem.setSubOrderId(order.getOrderNum()+"-"+item.getId());
            orderItem.setOrderStatus(1);
            orderItem.setShipStatus(0);
            orderItem.setIsGift(item.getIsGift());
            shopOrderItemList.add(orderItem);
        }
        // 添加订单
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setMerchantId(shop.getMerchantId());
        shopOrder.setShopId(shop.getId());
        shopOrder.setShopType(shop.getType());
        shopOrder.setOrderId(order.getOrderNum());
        shopOrder.setOrderTime(System.currentTimeMillis()/1000);
        shopOrder.setUpdateTime(System.currentTimeMillis()/1000);
        shopOrder.setOrderStatus(1);
        shopOrder.setRefundStatus(1);
        shopOrder.setGoodsAmount(order.getGoodsAmount().multiply(BigDecimal.valueOf(100)).intValue());
        shopOrder.setFreight(order.getPostage()==null? 0 : order.getPostage().multiply(BigDecimal.valueOf(100)).intValue());
        shopOrder.setChangePrice(order.getChangePrice()==null?0:order.getChangePrice().multiply(BigDecimal.valueOf(100)).intValue());
        shopOrder.setPlatformDiscount(0);
        // 折扣优惠
        shopOrder.setDiscountAmount(order.getDiscountAmount()==null?0:order.getDiscountAmount().multiply(BigDecimal.valueOf(100)).intValue());
        // 商家优惠=手动改价+折扣优惠
        shopOrder.setSellerDiscount(shopOrder.getChangePrice()+shopOrder.getDiscountAmount());

        // 订单金额=商品金额+运费-手动改价-优惠折扣
        Integer orderAmount = shopOrder.getGoodsAmount() +shopOrder.getFreight()-shopOrder.getChangePrice()-shopOrder.getDiscountAmount();
        shopOrder.setOrderAmount(orderAmount);
        // 抵扣金额
        Integer deductionPrice =order.getDeductionPrice()==null?0: order.getDeductionPrice().multiply(BigDecimal.valueOf(100)).intValue();
        /** 抵扣金额和订单金额对比*/
        if(orderAmount >= deductionPrice) {
            // 订单金额 >= 抵扣金额
            shopOrder.setDeductionPrice(deductionPrice);
        }else{
            // 剩余抵扣不能用 (只抵扣订单金额部分，剩下的需要继续结算)
            shopOrder.setDeductionPrice(orderAmount);
        }
        // 支付金额=订单金额-抵扣
        shopOrder.setPaymentAmount(shopOrder.getOrderAmount()-shopOrder.getDeductionPrice());
        shopOrder.setPaymentMethod(order.getPayMethod());
        shopOrder.setBuyerMemo(order.getBuyerMemo());
        shopOrder.setProvince(order.getProvince());
        shopOrder.setCity(order.getCity());
        shopOrder.setCounty(order.getTown());
        shopOrder.setAddress(order.getAddress());
        shopOrder.setReceiverName(order.getReceiverName());
        shopOrder.setReceiverPhone(order.getReceiverPhone());
        shopOrder.setShipDoneTime(0L);
        shopOrder.setConfirmStatus(0);
        shopOrder.setErpShipStatus(0);
        shopOrder.setCreateOn(new Date());
        shopOrder.setShopMemberId(0L);
        shopOrder.setPlatformUserId("0");
        shopOrder.setDeliverMethod(0);
        shopOrder.setOrderMode(1);
        shopOrder.setOrderType(0);
        shopOrder.setFinishTime(0L);
        // 处理订单会员信息，如果会员存在直接加入
        if(order.getShopMemberId()==null) order.setShopMemberId(0L);
//        if(order.getShopMemberId()>0){
//            ShopMember shopMember = shopMemberMapper.selectById(order.getShopMemberId());
//            if(shopMember==null){
//                // 不存在，重新设置为0
//                order.setShopMemberId(0L);
//            }
//        }
//        if(order.getShopMemberId()==0){
//            // 不存在，插入并更新
//            ShopMember newMember = new ShopMember();
//            newMember.setMerchantId(shop.getMerchantId());
//            newMember.setShopId(shop.getId());
//            newMember.setShopType(shop.getType());
//            newMember.setName(order.getReceiverName());
//            newMember.setPhone(order.getReceiverPhone());
//            newMember.setProvince(order.getProvince());
//            newMember.setCity(order.getCity());
//            newMember.setCounty(order.getTown());
//            newMember.setAddress(order.getAddress());
//            newMember.setStatus(1);
//            newMember.setCreateOn(new Date());
//            shopMemberMapper.insert(newMember);
//            order.setShopMemberId(newMember.getId());
//        }
        shopOrder.setShopMemberId(order.getShopMemberId());
        //处理销售员
        shopOrder.setFinderId(order.getFinderId()==null?"":order.getFinderId().toString());

        this.baseMapper.insert(shopOrder);
        for(var item : shopOrderItemList){
            item.setShopOrderId(shopOrder.getId());
            itemMapper.insert(item);
        }

        // 添加优惠明细
        /***1 折扣 */
        if(order.getDiscountAmount()!=null&&order.getDiscountAmount().intValue()>0){
            // 折扣有使用
            if(order.getDiscountId()!=null){
//                OMarketingDiscountRule discount = discountRuleMapper.selectById(order.getDiscountId());
//                if(discount!=null){
//                    ShopOrderPromotion promotion1 = new ShopOrderPromotion();
//                    promotion1.setShopOrderId(shopOrder.getId());
//                    promotion1.setPromotionId(discount.getId().toString());
//                    promotion1.setPromotionName(discount.getRuleName());
//                    promotion1.setDiscountFee(order.getDiscountAmount().toString());
//                    promotion1.setPromotionDesc(discount.getRemark());
//                    shopOrderPromotionMapper.insert(promotion1);
//                }else{
                    ShopOrderPromotion promotion0 = new ShopOrderPromotion();
                    promotion0.setShopOrderId(shopOrder.getId());
                    promotion0.setPromotionId("O");
                    promotion0.setPromotionName("手动折扣");
                    promotion0.setDiscountFee(order.getDiscountAmount().toString());
                    promotion0.setPromotionDesc("");
                    shopOrderPromotionMapper.insert(promotion0);
//                }
            }else{
                ShopOrderPromotion promotion0 = new ShopOrderPromotion();
                promotion0.setShopOrderId(shopOrder.getId());
                promotion0.setPromotionId("O");
                promotion0.setPromotionName("手动折扣");
                promotion0.setDiscountFee(order.getDiscountAmount().toString());
                promotion0.setPromotionDesc("");
                shopOrderPromotionMapper.insert(promotion0);
            }
        }
        /**2 手动改价*/
        if(order.getChangePrice()!=null&&order.getChangePrice().intValue()>0){
            ShopOrderPromotion promotion0 = new ShopOrderPromotion();
            promotion0.setShopOrderId(shopOrder.getId());
            promotion0.setPromotionId("O");
            promotion0.setPromotionName("手动改价");
            promotion0.setDiscountFee(order.getChangePrice().toString());
            promotion0.setPromotionDesc("");
            shopOrderPromotionMapper.insert(promotion0);
        }

        return ResultVo.success(shopOrder.getId());
    }


    @Override
    public ShopOrder queryByLogisticsNo(String logisticsNo) {
        List<ShopOrder> oShipments =  this.baseMapper.selectList(new LambdaQueryWrapper<ShopOrder>().eq(ShopOrder::getLogisticsOrderNo, logisticsNo));
        if(oShipments.size()>0){
            return oShipments.get(0);
        }else
            return null;
    }
}