package cn.qihangerp.module.service.impl;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.mapper.*;
import cn.qihangerp.model.bo.DouOrderConfirmBo;
import cn.qihangerp.model.entity.OOrder;
import cn.qihangerp.model.entity.OOrderItem;
import cn.qihangerp.model.entity.DouGoodsSku;
import cn.qihangerp.model.entity.DouOrder;
import cn.qihangerp.model.entity.DouOrderItem;
import cn.qihangerp.model.bo.DouOrderBo;
import cn.qihangerp.module.service.DouOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author TW
* @description 针对表【dou_order(抖店订单表)】的数据库操作Service实现
* @createDate 2024-06-05 15:06:31
*/
@Log
@AllArgsConstructor
@Service
public class DouOrderServiceImpl extends ServiceImpl<DouOrderMapper, DouOrder>
    implements DouOrderService {
    private final DouOrderMapper mapper;
    private final DouOrderItemMapper itemMapper;
    private final DouGoodsSkuMapper goodsSkuMapper;
    private final ErpOrderMapper oOrderMapper;
    private final ErpOrderItemMapper oOrderItemMapper;

    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);
    @Override
    public PageResult<DouOrder> queryPageList(DouOrderBo bo, PageQuery pageQuery) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if(StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(b){
                bo.setEndTime(bo.getEndTime()+" 23:59:59");
            }
        }else{
            bo.setEndTime(bo.getStartTime()+" 23:59:59");
        }
        if(StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(b){
                bo.setStartTime(bo.getStartTime()+" 00:00:00");
            }
        }
        if(StringUtils.hasText(bo.getStartTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(bo.getStartTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(bo.getEndTime(), formatter);

            startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
            endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));
        }
        LambdaQueryWrapper<DouOrder> queryWrapper = new LambdaQueryWrapper<DouOrder>()
                .eq(bo.getShopId()!=null,DouOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),DouOrder::getOrderId,bo.getOrderId())
                .eq(StringUtils.hasText(bo.getOrderStatus()),DouOrder::getOrderStatus,bo.getOrderStatus())
                .ge(StringUtils.hasText(bo.getStartTime()),DouOrder::getCreateTime, startTimestamp)
                .le(StringUtils.hasText(bo.getEndTime()),DouOrder::getCreateTime,endTimestamp)
                ;
        pageQuery.setOrderByColumn("create_time");
        pageQuery.setIsAsc("desc");
        Page<DouOrder> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(taoGoodsPage.getRecords()!=null){
            for (DouOrder order:taoGoodsPage.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getParentOrderId,order.getOrderId())));
            }
        }
        return PageResult.build(taoGoodsPage);
    }

    @Override
    public DouOrder queryDetailById(Long id) {
        DouOrder douOrder = mapper.selectById(id);
        if(douOrder!=null) {
            douOrder.setItems(itemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getParentOrderId,douOrder.getOrderId())));
        }
        return  douOrder;
    }

    @Override
    public DouOrder queryDetailByOrderId(String orderId) {
        List<DouOrder> douOrders = mapper.selectList(new LambdaQueryWrapper<DouOrder>().eq(DouOrder::getOrderId,orderId));
        if(!douOrders.isEmpty()) {
            douOrders.get(0).setItems(itemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getParentOrderId,douOrders.get(0).getOrderId())));
            return douOrders.get(0);
        }else return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, DouOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        try {

            List<DouOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<DouOrder>().eq(DouOrder::getOrderId, order.getOrderId()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                DouOrder update = new DouOrder();
                update.setId(taoOrders.get(0).getId());
                update.setOrderStatus(order.getOrderStatus());
                update.setOrderStatusDesc(order.getOrderStatusDesc());
                update.setBuyerWords(order.getBuyerWords());
                update.setSellerWords(order.getSellerWords());
                update.setSellerRemarkStars(order.getSellerRemarkStars());
                update.setCancelReason(order.getCancelReason());
                update.setChannelPaymentNo(order.getChannelPaymentNo());
                update.setPayTime(order.getPayTime());
                update.setUpdateTime(order.getUpdateTime());
                update.setFinishTime(order.getFinishTime());
                update.setOrderExpireTime(order.getOrderExpireTime());
                update.setLogisticsInfo(order.getLogisticsInfo());
                update.setMainStatus(order.getMainStatus());
                update.setMainStatusDesc(order.getMainStatusDesc());
                update.setMaskPostReceiver(order.getMaskPostReceiver());
                update.setMaskPostTel(order.getMaskPostTel());
                update.setMaskPostAddress(order.getMaskPostAddress());
                update.setPayAmount(order.getPayAmount());
                update.setPayType(order.getPayType());
                update.setShipTime(order.getShipTime());
                update.setTotalPromotionAmount(order.getTotalPromotionAmount());
                update.setModifyAmount(order.getModifyAmount());
                update.setModifyPostAmount(order.getModifyPostAmount());
                update.setLastPullTime(new Date());

                mapper.updateById(update);
                // 删除item
                itemMapper.delete(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getParentOrderId,order.getOrderId()));
                for (DouOrderItem item : order.getItems()) {
                    // 新增
                    DouGoodsSku goodsSku = goodsSkuMapper.selectById(item.getSkuId());
                    if (goodsSku != null) {
                        item.setOGoodsId(goodsSku.getErpGoodsId().toString());
                        item.setOGoodsSkuId(goodsSku.getErpGoodsSkuId().toString());
                    }
                    itemMapper.insert(item);
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                order.setAuditStatus(0);
                order.setPullTime(new Date());
                mapper.insert(order);
                // 添加item
                for (DouOrderItem item : order.getItems()) {
                    DouGoodsSku goodsSku = goodsSkuMapper.selectById(item.getSkuId());
                    if (goodsSku != null) {
                        item.setOGoodsId(goodsSku.getErpGoodsId().toString());
                        item.setOGoodsSkuId(goodsSku.getErpGoodsSkuId().toString());
                    }
                    itemMapper.insert(item);
                }

                return ResultVo.success();
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
    public ResultVo<Long> confirmOrder(DouOrderConfirmBo confirmBo) {
        DouOrder douOrder = mapper.selectById(confirmBo.getOrderId());
        if(douOrder==null) return ResultVo.error("订单数据不存在");
        if(douOrder.getAuditStatus()!=0) return ResultVo.error("已经确认过了！");

        List<DouOrderItem> douOrderItems = itemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getParentOrderId, douOrder.getOrderId()));
        if(douOrderItems==null || douOrderItems.isEmpty()){
            return ResultVo.error("找不到订单item");
        }

        OOrder erpOrder = oOrderMapper.selectOne(new LambdaQueryWrapper<OOrder>().eq(OOrder::getOrderNum,douOrder.getOrderId()));
        if(erpOrder!=null) {
            // 已经确认过了，更新自己
            DouOrder douOrderUpdate = new DouOrder();
            douOrderUpdate.setId(douOrder.getId());
            douOrderUpdate.setAuditStatus(1);
            douOrderUpdate.setAuditTime(new Date());
            mapper.updateById(douOrderUpdate);

            return ResultVo.error("已经确认过了");
        }

        OOrder order = new OOrder();
        order.setOrderNum(douOrder.getOrderId());
        order.setShopType(EnumShopType.DOU.getIndex());
        order.setShopId(douOrder.getSShopId());
//        order.setShipType(confirmBo.getShipType());
        order.setShipType(0);
        order.setBuyerMemo(douOrder.getBuyerWords());
        order.setSellerMemo(douOrder.getSellerWords());
        order.setRefundStatus(1);
        order.setOrderStatus(1);
        order.setGoodsAmount(douOrder.getOrderAmount()!=null?douOrder.getOrderAmount().doubleValue()/100:0.0);
        order.setPostFee(douOrder.getPostAmount()!=null?douOrder.getPostAmount().doubleValue()/100:0.0);
        order.setSellerDiscount(douOrder.getPromotionShopAmount()!=null?douOrder.getPromotionShopAmount().doubleValue()/100:0.0);
        order.setPlatformDiscount(douOrder.getPromotionPlatformAmount()!=null?douOrder.getPromotionPlatformAmount().doubleValue()/100:0.0);
        order.setAmount(douOrder.getOrderAmount()!=null?douOrder.getOrderAmount().doubleValue()/100:0.0);
        order.setPayment(douOrder.getPayAmount()!=null?douOrder.getPayAmount().doubleValue()/100:0.0);
        order.setReceiverName(confirmBo.getReceiver());
        order.setReceiverMobile(confirmBo.getMobile());
        order.setAddress(confirmBo.getAddress());
        order.setProvince(confirmBo.getProvince());
        order.setCity(confirmBo.getCity());
        order.setTown(confirmBo.getTown());
        order.setOrderTime(douOrder.getCreateTime()!=null?new Date(douOrder.getCreateTime()*1000):new Date());
        order.setShipper(-1L);
        order.setShipStatus(0);
        order.setCreateTime(new Date());
        order.setCreateBy("手动确认订单");
        oOrderMapper.insert(order);
        //插入item
        for (var item : douOrderItems) {
            OOrderItem oOrderItem = new OOrderItem();
            oOrderItem.setOrderId(order.getId());
            oOrderItem.setOrderNum(douOrder.getOrderId());
            oOrderItem.setSubOrderNum(item.getOrderId());
            oOrderItem.setShopType(EnumShopType.DOU.getIndex());
            oOrderItem.setShopId(douOrder.getSShopId());
            oOrderItem.setSkuId(item.getSkuId().toString());
            oOrderItem.setGoodsId(StringUtils.hasText(item.getOGoodsId())?Long.parseLong(item.getOGoodsId()):0L);
            oOrderItem.setGoodsSkuId(StringUtils.hasText(item.getOGoodsSkuId())?Long.parseLong(item.getOGoodsSkuId()):0L);
            oOrderItem.setGoodsTitle(item.getProductName());
            oOrderItem.setGoodsImg(item.getProductPic());
            oOrderItem.setGoodsNum(item.getOutProductId());
            oOrderItem.setGoodsSpec(item.getSpec());
            oOrderItem.setSkuNum(item.getOutSkuId());
            oOrderItem.setGoodsPrice(item.getGoodsPrice()!=null?item.getGoodsPrice().doubleValue()/100:0.0);
            oOrderItem.setItemAmount(item.getOrderAmount()!=null?item.getOrderAmount().doubleValue()/100:0.0);
            oOrderItem.setDiscountAmount(item.getPromotionAmount()!=null?item.getPromotionAmount().doubleValue()/100:0.0);
            oOrderItem.setPayment(item.getPayAmount()!=null?item.getPayAmount().doubleValue()/100:0.0);
            oOrderItem.setQuantity(item.getItemNum());
            oOrderItem.setRefundCount(0);
            oOrderItem.setRefundStatus(1);
            oOrderItem.setShipper(-1L);
            oOrderItem.setShipType(order.getShipType());
            oOrderItem.setShipStatus(0);
            oOrderItem.setCreateTime(new Date());
            oOrderItem.setCreateBy("手动确认订单");
            oOrderItemMapper.insert(oOrderItem);
        }
        // 已经确认过了，更新自己
        DouOrder douOrderUpdate = new DouOrder();
        douOrderUpdate.setId(douOrder.getId());
        douOrderUpdate.setAuditStatus(1);
        douOrderUpdate.setAuditTime(new Date());
        mapper.updateById(douOrderUpdate);
        return ResultVo.success();
    }
}




