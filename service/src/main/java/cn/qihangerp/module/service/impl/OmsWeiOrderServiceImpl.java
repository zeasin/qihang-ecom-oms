package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.mapper.ErpOrderItemMapper;
import cn.qihangerp.mapper.ErpOrderMapper;
import cn.qihangerp.model.bo.WeiOrderBo;
import cn.qihangerp.model.entity.OOrder;
import cn.qihangerp.model.entity.OOrderItem;
import cn.qihangerp.model.entity.OmsWeiGoodsSku;
import cn.qihangerp.model.entity.OmsWeiOrder;
import cn.qihangerp.model.entity.OmsWeiOrderItem;
import cn.qihangerp.model.bo.WeiOrderConfirmBo;
import cn.qihangerp.mapper.OmsWeiGoodsSkuMapper;
import cn.qihangerp.mapper.OmsWeiOrderItemMapper;
import cn.qihangerp.mapper.OmsWeiOrderMapper;
import cn.qihangerp.module.service.OmsWeiOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.AllArgsConstructor;
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
* @description 针对表【oms_wei_order】的数据库操作Service实现
* @createDate 2024-06-03 16:48:31
*/
@AllArgsConstructor
@Service
public class OmsWeiOrderServiceImpl extends ServiceImpl<OmsWeiOrderMapper, OmsWeiOrder>
    implements OmsWeiOrderService {
    private final OmsWeiOrderMapper mapper;
    private final OmsWeiOrderItemMapper itemMapper;
    private final OmsWeiGoodsSkuMapper goodsSkuMapper;
    private final ErpOrderMapper erpOrderMapper;
    private final ErpOrderItemMapper erpOrderItemMapper;
//    private final MQClientService mqClientService;
    private final String DATE_PATTERN =
        "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);
    @Override
    public PageResult<OmsWeiOrder> queryPageList(WeiOrderBo bo, PageQuery pageQuery) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if(StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(!b){
                bo.setStartTime("");
            }
        }
        if(StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(!b){
                bo.setEndTime("");
            }
        }else{
            if(StringUtils.hasText(bo.getStartTime())) {
                bo.setEndTime(bo.getStartTime());
            }
        }
        if(StringUtils.hasText(bo.getStartTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(bo.getStartTime()+" 00:00:00", formatter);
            LocalDateTime endTime = LocalDateTime.parse(bo.getEndTime()+" 23:59:59", formatter);

            startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
            endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));
        }
        LambdaQueryWrapper<OmsWeiOrder> queryWrapper = new LambdaQueryWrapper<OmsWeiOrder>()
                .eq(bo.getShopId()!=null,OmsWeiOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsWeiOrder::getOrderId,bo.getOrderId())
                .eq(bo.getStatus()!=null,OmsWeiOrder::getStatus,bo.getStatus())
                .ge(StringUtils.hasText(bo.getStartTime()), OmsWeiOrder::getCreateTime, startTimestamp)
                .le(StringUtils.hasText(bo.getEndTime()),OmsWeiOrder::getCreateTime,endTimestamp)
                ;
//        if(bo.getErpSendStatus()!=null){
//            if(bo.getErpSendStatus()==-1) {
//                queryWrapper.lt(OmsWeiOrder::getErpSendStatus,3);
//            }else {
//                queryWrapper.eq(OmsWeiOrder::getErpSendStatus, bo.getErpSendStatus());
//            }
//        }

        Page<OmsWeiOrder> page = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(page.getRecords()!=null){
            for (OmsWeiOrder order:page.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId,order.getOrderId())));
            }
        }
        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, OmsWeiOrder order) {
        try {
            List<OmsWeiOrder> orders = mapper.selectList(new LambdaQueryWrapper<OmsWeiOrder>().eq(OmsWeiOrder::getOrderId, order.getOrderId()));
            if (orders != null && orders.size() > 0) {
                // 存在，修改
                OmsWeiOrder update = new OmsWeiOrder();
                update.setId(orders.get(0).getId());
                update.setOrderId(order.getOrderId());
                update.setStatus(order.getStatus());
                update.setUpdateTime(order.getUpdateTime());
                update.setPayInfo(order.getPayInfo());
                update.setAftersaleDetail(order.getAftersaleDetail());
                update.setDeliveryProductInfo(order.getDeliveryProductInfo());

                mapper.updateById(update);
                // 更新item
                for (OmsWeiOrderItem item : order.getItems()) {
                    List<OmsWeiOrderItem> taoOrderItems = itemMapper.selectList(
                            new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getSkuId, item.getSkuId()).eq(OmsWeiOrderItem::getOrderId,order.getOrderId())
                    );

                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 更新
                        item.setId(taoOrderItems.get(0).getId());
                        List<OmsWeiGoodsSku> skus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getSkuId, item.getSkuId()));
                        if (skus != null && !skus.isEmpty()) {
                            item.setOGoodsId(skus.get(0).getErpGoodsId());
                            item.setOGoodsSkuId(skus.get(0).getErpGoodsSkuId());
                        }
                        itemMapper.updateById(item);
                    } else {
                        // 新增
                        List<OmsWeiGoodsSku> skus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getSkuId, item.getSkuId()));
                        if (skus != null && !skus.isEmpty()) {
                            item.setOGoodsId(skus.get(0).getErpGoodsId());
                            item.setOGoodsSkuId(skus.get(0).getErpGoodsSkuId());
                        }
                        item.setShopId(shopId);
                        item.setOrderId(order.getOrderId());
                        itemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增

                order.setShopId(shopId);
                order.setAuditStatus(0);
                mapper.insert(order);
                // 添加item
                for (OmsWeiOrderItem item : order.getItems()) {
                    List<OmsWeiGoodsSku> skus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getSkuId, item.getSkuId()));
                    if (skus != null && !skus.isEmpty()) {
                        item.setOGoodsId(skus.get(0).getErpGoodsId());
                        item.setOGoodsSkuId(skus.get(0).getErpGoodsSkuId());
                    }
                    item.setShopId(shopId);
                    item.setOrderId(order.getOrderId());
                    itemMapper.insert(item);
                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }


    @Override
    public OmsWeiOrder queryDetailById(Long id) {
        OmsWeiOrder weiOrder = mapper.selectById(id);
        if(weiOrder!=null){
            weiOrder.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId,weiOrder.getOrderId())));
        }
        return weiOrder;
    }
    @Override
    public OmsWeiOrder queryDetailByOrderId(String orderId) {
        List<OmsWeiOrder> weiOrders = mapper.selectList(new LambdaQueryWrapper<OmsWeiOrder>().eq(OmsWeiOrder::getOrderId,orderId));
        if(weiOrders!=null&&weiOrders.size()>0){
            weiOrders.get(0).setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId,orderId)));
            return weiOrders.get(0);
        }else return null;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> confirmOrder(WeiOrderConfirmBo confirmBo) {
        OmsWeiOrder pddOrder = mapper.selectById(confirmBo.getOrderId());
        if(pddOrder==null) return ResultVo.error("订单数据不存在");
        if(pddOrder.getAuditStatus()!=0) return ResultVo.error("已经确认过了！");

        List<OmsWeiOrderItem> pddOrderItems = itemMapper.selectList(
                new LambdaQueryWrapper<OmsWeiOrderItem>()
                        .eq(OmsWeiOrderItem::getOrderId, pddOrder.getOrderId()));
        if(pddOrderItems==null || pddOrderItems.isEmpty()){
            return ResultVo.error("找不到订单item");
        }

        OOrder erpOrder = erpOrderMapper.selectOne(new LambdaQueryWrapper<OOrder>().eq(OOrder::getOrderNum,pddOrder.getOrderId()));
        if(erpOrder!=null) {
            // 已经确认过了，更新自己
            OmsWeiOrder douOrderUpdate = new OmsWeiOrder();
            douOrderUpdate.setId(pddOrder.getId());
            douOrderUpdate.setAuditStatus(1);
            douOrderUpdate.setAuditTime(new Date());
            mapper.updateById(douOrderUpdate);

            return ResultVo.error("已经确认过了");
        }
        OOrder order = new OOrder();
        order.setOrderNum(pddOrder.getOrderId());
        order.setShopType(EnumShopType.WEI.getIndex());
        order.setShopId(pddOrder.getShopId());
//        order.setShipType(confirmBo.getShipType());
        order.setShipType(0);
        order.setBuyerMemo("");
        order.setSellerMemo("");
        order.setRefundStatus(1);
        order.setOrderStatus(1);
        order.setGoodsAmount(pddOrder.getProductPrice()!=null?pddOrder.getProductPrice().doubleValue()/100:0.0);
        order.setPostFee(pddOrder.getFreight()!=null?pddOrder.getFreight().doubleValue()/100:0.0);
        order.setSellerDiscount(pddOrder.getDiscountedPrice()!=null?pddOrder.getDiscountedPrice().doubleValue()/100:0.0);
        order.setPlatformDiscount(0.0);
        order.setAmount(pddOrder.getOrderPrice()!=null?pddOrder.getOrderPrice().doubleValue()/100:0.0);
        order.setPayment(order.getAmount());
        order.setReceiverName(confirmBo.getReceiver());
        order.setReceiverMobile(confirmBo.getMobile());
        order.setAddress(confirmBo.getAddress());
        order.setProvince(confirmBo.getProvince());
        order.setCity(confirmBo.getCity());
        order.setTown(confirmBo.getTown());
        order.setOrderTime(pddOrder.getCreateTime()!=null?new Date(pddOrder.getCreateTime()*1000):new Date());
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
            oOrderItem.setSubOrderNum(order.getOrderNum()+"-"+item.getSkuId());
            oOrderItem.setShopType(EnumShopType.WEI.getIndex());
            oOrderItem.setShopId(pddOrder.getShopId());
            oOrderItem.setSkuId(item.getSkuId());
            oOrderItem.setGoodsId(item.getOGoodsId()!=null?item.getOGoodsId():0L);
            oOrderItem.setGoodsSkuId(item.getOGoodsSkuId()!=null?item.getOGoodsSkuId():0L);
            oOrderItem.setGoodsTitle(item.getTitle());
            oOrderItem.setGoodsImg(item.getThumbImg());
            oOrderItem.setGoodsNum(item.getOutProductId());
            oOrderItem.setGoodsSpec("");
            oOrderItem.setSkuNum(item.getOutSkuId());
            oOrderItem.setGoodsPrice(item.getSalePrice()!=null?item.getSalePrice().doubleValue()/100:0.0);
            oOrderItem.setQuantity(item.getSkuCnt());
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
        OmsWeiOrder douOrderUpdate = new OmsWeiOrder();
        douOrderUpdate.setId(pddOrder.getId());
        douOrderUpdate.setAuditStatus(1);
        douOrderUpdate.setAuditTime(new Date());
        mapper.updateById(douOrderUpdate);
        return ResultVo.success();
    }
}




