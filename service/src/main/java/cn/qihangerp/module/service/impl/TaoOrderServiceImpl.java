package cn.qihangerp.module.service.impl;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.mapper.*;
import cn.qihangerp.model.entity.OOrder;
import cn.qihangerp.model.entity.OOrderItem;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.model.entity.TaoOrder;
import cn.qihangerp.model.entity.TaoOrderItem;
import cn.qihangerp.model.entity.TaoOrderPromotion;
import cn.qihangerp.model.bo.TaoOrderBo;
import cn.qihangerp.model.bo.TaoOrderConfirmBo;
import cn.qihangerp.module.service.TaoGoodsSkuService;
import cn.qihangerp.module.service.TaoOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author TW
* @description 针对表【tao_order(淘宝订单表)】的数据库操作Service实现
* @createDate 2024-02-29 19:01:11
*/
@Log
@AllArgsConstructor
@Service
public class TaoOrderServiceImpl extends ServiceImpl<TaoOrderMapper, TaoOrder>
    implements TaoOrderService {
    private final TaoOrderMapper mapper;
    private final TaoOrderItemMapper itemMapper;
    private final TaoOrderPromotionMapper promotionDetailsMapper;
    private final TaoGoodsSkuService goodsSkuService;
    private final ErpOrderMapper erpOrderMapper;
    private final ErpOrderItemMapper erpOrderItemMapper;


    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);
    @Override
    public PageResult<TaoOrder> queryPageList(TaoOrderBo bo, PageQuery pageQuery) {
        if(StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(b){
                bo.setStartTime(bo.getStartTime()+" 00:00:00");
            }
        }
        if(StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(b){
                bo.setEndTime(bo.getEndTime()+" 23:59:59");
            }
        }

        LambdaQueryWrapper<TaoOrder> queryWrapper = new LambdaQueryWrapper<TaoOrder>()
                .eq(bo.getShopId()!=null,TaoOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getTid()),TaoOrder::getTid,bo.getTid())
                .eq(StringUtils.hasText(bo.getStatus()),TaoOrder::getStatus,bo.getStatus())
                .ge(StringUtils.hasText(bo.getStartTime()),TaoOrder::getCreated,bo.getStartTime()+" 00:00:00")
                .le(StringUtils.hasText(bo.getEndTime()),TaoOrder::getCreated,bo.getEndTime()+" 23:59:59")
                ;
        pageQuery.setOrderByColumn("created");
        pageQuery.setIsAsc("desc");
        Page<TaoOrder> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(taoGoodsPage.getRecords()!=null){
            for (TaoOrder order:taoGoodsPage.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<TaoOrderItem>().eq(TaoOrderItem::getTid,order.getTid())));
            }
        }
        return PageResult.build(taoGoodsPage);
    }

    @Override
    public TaoOrder queryDetailById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public TaoOrder queryDetailByTid(String tid) {
        List<TaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<TaoOrder>().eq(TaoOrder::getTid, tid));
        if(taoOrders.isEmpty())
            return null;
        else{
            taoOrders.get(0).setItems(itemMapper.selectList(new LambdaQueryWrapper<TaoOrderItem>().eq(TaoOrderItem::getTid,tid)));
            taoOrders.get(0).setPromotions(promotionDetailsMapper.selectList(new LambdaQueryWrapper<TaoOrderPromotion>().eq(TaoOrderPromotion::getId,tid)));
            return taoOrders.get(0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> saveOrder(Long shopId, TaoOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        try {

            List<TaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<TaoOrder>().eq(TaoOrder::getTid, order.getTid()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                TaoOrder update = new TaoOrder();
                update.setId(taoOrders.get(0).getId());
                if(order.getStatus().equals("WAIT_SELLER_SEND_GOODS")) {
                    update.setReceiverName(order.getReceiverName());
                    update.setReceiverMobile(order.getReceiverMobile());
                    update.setReceiverAddress(order.getReceiverAddress());
                }
                update.setSid(order.getSid());
                update.setSellerRate(order.getSellerRate());
                update.setBuyerRate(order.getBuyerRate());
                update.setStatus(order.getStatus());
                update.setModified(order.getModified());
                update.setEndTime(order.getEndTime());
                update.setConsignTime(order.getConsignTime());

                update.setUpdateTime(new Date());
                update.setReceivedPayment(order.getReceivedPayment());
                update.setAvailableConfirmFee(order.getAvailableConfirmFee());
                mapper.updateById(update);
                // 更新item
                for (TaoOrderItem item : order.getItems()) {
                    List<TaoOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<TaoOrderItem>().eq(TaoOrderItem::getOid, item.getOid()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 更新
                        TaoOrderItem itemUpdate = new TaoOrderItem();
                        itemUpdate.setId(taoOrderItems.get(0).getId());
                        itemUpdate.setRefundId(item.getRefundId());
                        itemUpdate.setRefundStatus(item.getRefundStatus());
                        itemUpdate.setStatus(item.getStatus());
                        itemUpdate.setBuyerRate(item.getBuyerRate());
                        itemUpdate.setSellerRate(item.getSellerRate());
                        itemUpdate.setEndTime(item.getEndTime());
                        itemUpdate.setConsignTime(item.getConsignTime());
                        itemUpdate.setTotalFee(item.getTotalFee());
                        itemUpdate.setDiscountFee(item.getDiscountFee());
                        itemUpdate.setAdjustFee(item.getAdjustFee());
                        itemUpdate.setDivideOrderFee(item.getDivideOrderFee());
                        itemUpdate.setPartMjzDiscount(item.getPartMjzDiscount());
                        itemUpdate.setPayment(item.getPayment());
                        itemUpdate.setShippingType(item.getShippingType());
                        itemUpdate.setLogisticsCompany(item.getLogisticsCompany());
                        itemUpdate.setInvoiceNo(item.getInvoiceNo());
                        List<TaoGoodsSku> skus = goodsSkuService.list(new LambdaQueryWrapper<TaoGoodsSku>().eq(TaoGoodsSku::getSkuId, item.getSkuId()));
                        if (skus != null && !skus.isEmpty()) {
                            itemUpdate.setoGoodsId(skus.get(0).getErpGoodsId().toString());
                            itemUpdate.setoGoodsSkuId(skus.get(0).getErpGoodsSkuId().toString());
                        }

                        itemMapper.updateById(itemUpdate);
                    } else {
                        // 新增
                        List<TaoGoodsSku> skus = goodsSkuService.list(new LambdaQueryWrapper<TaoGoodsSku>().eq(TaoGoodsSku::getSkuId, item.getSkuId()));
                        if (skus != null && !skus.isEmpty()) {
                            item.setoGoodsId(skus.get(0).getErpGoodsId().toString());
                            item.setoGoodsSkuId(skus.get(0).getErpGoodsSkuId().toString());
                        }
                        itemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist.getIndex(), "订单已经存在，更新成功",Long.parseLong(update.getId()));
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                order.setCreateTime(new Date());
                mapper.insert(order);
                // 添加item
                for (TaoOrderItem item : order.getItems()) {
                    itemMapper.insert(item);
                }
                // 添加 优惠信息
                if(order.getPromotions()!=null){
                    for (TaoOrderPromotion p:order.getPromotions()) {
                        promotionDetailsMapper.insert(p);
                    }
                }
                return ResultVo.success(Long.parseLong(order.getId()));
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            log.info("保存订单数据错误："+e.getMessage());
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Integer> updateOrder(TaoOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        List<TaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<TaoOrder>().eq(TaoOrder::getTid, order.getTid()));
        if (taoOrders != null && taoOrders.size() > 0) {
            TaoOrder update = new TaoOrder();
            update.setId(taoOrders.get(0).getId());
            update.setSid(order.getSid());
            update.setSellerRate(order.getSellerRate());
            update.setBuyerRate(order.getBuyerRate());
            update.setStatus(order.getStatus());
            update.setModified(order.getModified());
            update.setEndTime(order.getEndTime());
            update.setConsignTime(order.getConsignTime());
            update.setUpdateTime(new Date());
            mapper.updateById(update);

            return ResultVo.error(ResultVoEnum.SUCCESS, "订单更新成功");
        } else {
            // 不存在，新增
            return ResultVo.error(ResultVoEnum.NotFound, "订单不存在");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> confirmOrder(TaoOrderConfirmBo confirmBo) {
        TaoOrder pddOrder = mapper.selectById(confirmBo.getOrderId());
        if(pddOrder==null) return ResultVo.error("订单数据不存在");
        if(pddOrder.getAuditStatus()!=0) return ResultVo.error("已经确认过了！");

        List<TaoOrderItem> pddOrderItems = itemMapper.selectList(
                new LambdaQueryWrapper<TaoOrderItem>()
                        .eq(TaoOrderItem::getTid, pddOrder.getTid()));
        if(pddOrderItems==null || pddOrderItems.isEmpty()){
            return ResultVo.error("找不到订单item");
        }

        OOrder erpOrder = erpOrderMapper.selectOne(new LambdaQueryWrapper<OOrder>().eq(OOrder::getOrderNum,pddOrder.getTid()));
        if(erpOrder!=null) {
            // 已经确认过了，更新自己
            TaoOrder douOrderUpdate = new TaoOrder();
            douOrderUpdate.setId(pddOrder.getId());
            douOrderUpdate.setAuditStatus(1);
            douOrderUpdate.setAuditTime(new Date());
            mapper.updateById(douOrderUpdate);

            return ResultVo.error("已经确认过了");
        }
        OOrder order = new OOrder();
        order.setOrderNum(pddOrder.getTid());
        order.setShopType(EnumShopType.TAO.getIndex());
        order.setShopId(pddOrder.getShopId());
//        order.setShipType(confirmBo.getShipType());
        order.setShipType(0);
        order.setBuyerMemo(pddOrder.getBuyerMemo());
        order.setSellerMemo(pddOrder.getSellerMemo());
        order.setRefundStatus(1);
        order.setOrderStatus(1);
        order.setGoodsAmount(pddOrder.getTotalFee()!=null?pddOrder.getTotalFee():0.0);
        order.setPostFee(pddOrder.getPostFee()!=null?pddOrder.getPostFee().doubleValue():0.0);
        order.setSellerDiscount(pddOrder.getDiscountFee()!=null?pddOrder.getDiscountFee().doubleValue():0.0);
        order.setPlatformDiscount(0.0);
        order.setAmount(pddOrder.getTotalFee()!=null?pddOrder.getTotalFee().doubleValue():0.0);
        order.setPayment(pddOrder.getPayment()!=null?pddOrder.getPayment().doubleValue():0.0);
        order.setReceiverName(confirmBo.getReceiver());
        order.setReceiverMobile(confirmBo.getMobile());
        order.setAddress(confirmBo.getAddress());
        order.setProvince(confirmBo.getProvince());
        order.setCity(confirmBo.getCity());
        order.setTown(confirmBo.getTown());
        order.setOrderTime(StringUtils.hasText(pddOrder.getCreated())? DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",pddOrder.getCreated()):new Date());
        order.setShipper(-1L);
        order.setShipStatus(0);
        order.setCreateTime(new Date());
        order.setCreateBy("手动确认订单");
        erpOrderMapper.insert(order);
        //插入item
        for (var item : pddOrderItems) {
            OOrderItem oOrderItem = new OOrderItem();
            oOrderItem.setOrderId(order.getId());
            oOrderItem.setOrderNum(order.getOrderNum());
            oOrderItem.setSubOrderNum(item.getOid().toString());
            oOrderItem.setShopType(EnumShopType.TAO.getIndex());
            oOrderItem.setShopId(pddOrder.getShopId());
            oOrderItem.setSkuId(item.getSkuId().toString());
            oOrderItem.setGoodsId(StringUtils.hasText(item.getoGoodsId())?Long.parseLong(item.getoGoodsId()):0L);
            oOrderItem.setGoodsSkuId(StringUtils.hasText(item.getoGoodsSkuId())?Long.parseLong(item.getoGoodsSkuId()):0L);
            oOrderItem.setGoodsTitle(item.getTitle());
            oOrderItem.setGoodsImg(item.getPicPath());
            oOrderItem.setGoodsNum(item.getOuterIid());
            oOrderItem.setGoodsSpec(item.getSkuPropertiesName());
            oOrderItem.setSkuNum(item.getOuterSkuId());
            oOrderItem.setGoodsPrice(item.getPrice()!=null?item.getPrice().doubleValue():0.0);
            oOrderItem.setQuantity(item.getNum());
            oOrderItem.setItemAmount(oOrderItem.getGoodsPrice()*oOrderItem.getQuantity());
            oOrderItem.setDiscountAmount(0.0);
            oOrderItem.setPayment(0.0);

            oOrderItem.setRefundCount(0);
            oOrderItem.setRefundStatus(1);
            oOrderItem.setShipper(-1L);
            oOrderItem.setShipType(order.getShipType());
            oOrderItem.setShipStatus(0);
            oOrderItem.setCreateTime(new Date());
            oOrderItem.setCreateBy("手动确认订单");
            erpOrderItemMapper.insert(oOrderItem);
        }
        // 更新自己
        TaoOrder douOrderUpdate = new TaoOrder();
        douOrderUpdate.setId(pddOrder.getId());
        douOrderUpdate.setAuditStatus(1);
        douOrderUpdate.setAuditTime(new Date());
        mapper.updateById(douOrderUpdate);
        return ResultVo.success();
    }
    //    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public ResultVo<Integer> manualShipmentOrder(ShopOrderShipBo shipBo, String createBy) {
//        if (StringUtils.isEmpty(shipBo.getId()) || shipBo.getId().equals("0"))
//            return ResultVo.error(ResultVoEnum.ParamsError, "缺少参数：id");
//
//        ErpOrder erpOrder = mapper.selectById(shipBo.getId());
//        if (erpOrder == null) {
//            return ResultVo.error("找不到订单数据");
//        } else if (erpOrder.getOrderStatus().intValue() != 1 && erpOrder.getRefundStatus().intValue() != 1) {
//            return ResultVo.error("订单状态不对，不允许发货");
//        }
//        ErpLogisticsCompany erpLogisticsCompany = erpLogisticsCompanyMapper.selectById(shipBo.getShippingCompany());
//        if(erpLogisticsCompany==null) return ResultVo.error("快递公司选择错误");
//
//        // 自己发货的list
//        List<ErpOrderItem> oOrderItems = orderItemMapper.selectList(
//                new LambdaQueryWrapper<ErpOrderItem>()
//                        .eq(ErpOrderItem::getOrderId, erpOrder.getId())
//                        .eq(ErpOrderItem::getShipStatus,0)
//                        .eq(ErpOrderItem::getShipType,0)
//        );
//        if(oOrderItems==null) return ResultVo.error("订单 item 数据错误，无法发货！");
//        // 添加发货记录
//        ErpShipment erpShipment = new ErpShipment();
//        erpShipment.setShipper(0);//发货方 0 仓库发货 1 供应商发货】
//        erpShipment.setTenantId(erpOrder.getTenantId());
//        erpShipment.setShopId(erpOrder.getShopId());
//        erpShipment.setShopType(erpOrder.getShopType());
//        erpShipment.setOrderId(erpOrder.getId());
//        erpShipment.setOrderNum(erpOrder.getOrderNum());
//        erpShipment.setOrderTime(erpOrder.getOrderTime());
//        erpShipment.setShipType(1);//发货类型（1订单发货2商品补发3商品换货）
//        erpShipment.setShipCompany(erpLogisticsCompany.getName());
//        erpShipment.setShipCompanyCode(erpLogisticsCompany.getCode());
//        erpShipment.setShipCode(shipBo.getShippingNumber());
//        erpShipment.setShipFee(shipBo.getShippingCost());
//        erpShipment.setShipTime(new Date());
//        erpShipment.setShipOperator(shipBo.getShippingMan());
//        erpShipment.setShipStatus(1);//物流状态（0 待发货1已发货2已完成）
//
//        erpShipment.setPackageHeight(shipBo.getHeight());
//        erpShipment.setPackageWeight(shipBo.getWeight());
//        erpShipment.setPackageLength(shipBo.getLength());
//        erpShipment.setPackageWidth(shipBo.getWidth());
//        erpShipment.setPacksgeOperator(shipBo.getShippingMan());
////        erpShipment.setPackages(JSONObject.toJSONString(oOrderItems));
//        erpShipment.setRemark(shipBo.getRemark());
//        erpShipment.setCreateBy(createBy);
//        erpShipment.setCreateTime(new Date());
//
//        shipmentMapper.insert(erpShipment);
//
//        for(ErpOrderItem orderItem:oOrderItems){
//            ErpShipmentItem erpShipmentItem = new ErpShipmentItem();
//            erpShipmentItem.setShipper(erpShipment.getShipper());
//            erpShipmentItem.setTenantId(erpShipment.getTenantId());
//            erpShipmentItem.setShopId(erpShipment.getShopId());
//            erpShipmentItem.setShopType(erpShipment.getShopType());
//            erpShipmentItem.setShipmentId(erpShipment.getId());
//            erpShipmentItem.setOrderId(erpShipment.getOrderId());
//            erpShipmentItem.setOrderNum(erpShipment.getOrderNum());
//            erpShipmentItem.setOrderTime(erpShipment.getOrderTime());
//            erpShipmentItem.setOrderItemId(orderItem.getId());
//            erpShipmentItem.setErpGoodsId(orderItem.getErpGoodsId());
//            erpShipmentItem.setErpSkuId(orderItem.getErpSkuId());
//            erpShipmentItem.setGoodsTitle(orderItem.getGoodsTitle());
//            erpShipmentItem.setGoodsNum(orderItem.getGoodsNum());
//            erpShipmentItem.setGoodsImg(orderItem.getGoodsImg());
//            erpShipmentItem.setGoodsSpec(orderItem.getGoodsSpec());
//            erpShipmentItem.setSkuNum(orderItem.getSkuNum());
//            erpShipmentItem.setQuantity(orderItem.getQuantity());
//            erpShipmentItem.setRemark(orderItem.getRemark());
//            erpShipmentItem.setStockStatus(0);
//            erpShipmentItem.setCreateBy(createBy);
//            erpShipmentItem.setCreateTime(new Date());
//            shipmentItemMapper.insert(erpShipmentItem);
//
//            // 更新订单item发货状态
//            ErpOrderItem orderItemUpdate = new ErpOrderItem();
//            orderItemUpdate.setId( orderItem.getId());
//            orderItemUpdate.setUpdateBy("手动发货");
//            orderItemUpdate.setUpdateTime(new Date());
//            orderItemUpdate.setShipStatus(1);//发货状态 0 待发货 1 已发货
//            orderItemMapper.updateById(orderItemUpdate);
//        }
//
//
//        // 更新状态、发货方式
//        ErpOrder update = new ErpOrder();
//        update.setId(erpOrder.getId());
//        update.setShipStatus(2);
//        update.setOrderStatus(2);
//        update.setUpdateTime(new Date());
//        update.setUpdateBy("手动发货");
//        mapper.updateById(update);
//
//        return ResultVo.success();
//    }
}




