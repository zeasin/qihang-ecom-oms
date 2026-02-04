package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.mapper.*;
import cn.qihangerp.model.bo.OfflineOrderConfirmBo;
import cn.qihangerp.model.bo.OfflineOrderCreateBo;
import cn.qihangerp.model.bo.OfflineOrderCreateItemBo;
import cn.qihangerp.model.bo.OfflineOrderShipBo;
import cn.qihangerp.model.entity.*;
import cn.qihangerp.module.service.OfflineOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;

import cn.qihangerp.request.OrderSearchRequest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author qilip
* @description 针对表【offline_order(线下渠道订单表)】的数据库操作Service实现
* @createDate 2024-07-27 23:03:38
*/
@AllArgsConstructor
@Service
public class OfflineOrderServiceImpl extends ServiceImpl<OfflineOrderMapper, OfflineOrder>
    implements OfflineOrderService {
    private final OOrderMapper oOrderMapper;
    private final OOrderItemMapper oOrderItemMapper;
    private final OShopMapper shopMapper;
    private final OfflineOrderMapper orderMapper;
    private final OfflineOrderItemMapper orderItemMapper;
    private final MqUtils mqUtils;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);

    @Override
    public PageResult<OfflineOrder> queryPageList(OrderSearchRequest bo, PageQuery pageQuery) {
        if(org.springframework.util.StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(b){
                bo.setStartTime(bo.getStartTime()+" 00:00:00");
            }
        }
        if(org.springframework.util.StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(b){
                bo.setEndTime(bo.getEndTime()+" 23:59:59");
            }
        }

        LambdaQueryWrapper<OfflineOrder> queryWrapper = new LambdaQueryWrapper<OfflineOrder>()
                .eq(bo.getShopId()!=null,OfflineOrder::getShopId,bo.getShopId())
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),OfflineOrder::getOrderNum,bo.getOrderNum())
                .eq(bo.getOrderStatus()!=null,OfflineOrder::getOrderStatus,bo.getOrderStatus())
                .ge(org.springframework.util.StringUtils.hasText(bo.getStartTime()),OfflineOrder::getOrderTime,bo.getStartTime()+" 00:00:00")
                .le(org.springframework.util.StringUtils.hasText(bo.getEndTime()),OfflineOrder::getOrderTime,bo.getEndTime()+" 23:59:59")
                .like(org.springframework.util.StringUtils.hasText(bo.getReceiverName()),OfflineOrder::getReceiverName,bo.getReceiverName())
                .like(org.springframework.util.StringUtils.hasText(bo.getReceiverMobile()),OfflineOrder::getReceiverMobile,bo.getReceiverMobile())
                .like(org.springframework.util.StringUtils.hasText(bo.getShippingNumber()),OfflineOrder::getShippingNumber,bo.getShippingNumber())
                ;

        pageQuery.setOrderByColumn("order_time");
        pageQuery.setIsAsc("desc");
        Page<OfflineOrder> pages = orderMapper.selectPage(pageQuery.build(), queryWrapper);

        // 查询子订单
        if(pages.getRecords()!=null){
            for (OfflineOrder order:pages.getRecords()) {
                order.setItemList(orderItemMapper.selectList(new LambdaQueryWrapper<OfflineOrderItem>().eq(OfflineOrderItem::getOrderId, order.getId())));
            }
        }

        return PageResult.build(pages);
    }

    @Override
    public OfflineOrder queryDetailById(Long id) {
        OfflineOrder oOrder = orderMapper.selectById(id);
        if(oOrder!=null) {
           oOrder.setItemList(orderItemMapper.selectList(new LambdaQueryWrapper<OfflineOrderItem>().eq(OfflineOrderItem::getOrderId, oOrder.getId())));
        }

        return oOrder;
    }


    /**
     * 新增订单
     *
     * @param bo 订单
     * @return 结果
     */
    @Transactional
    @Override
    public Long insertOfflineOrder(OfflineOrderCreateBo bo, String createBy)
    {
        List<OfflineOrder> oOrders = orderMapper.selectList(new LambdaQueryWrapper<OfflineOrder>()
                .eq(OfflineOrder::getOrderNum, bo.getOrderNum())
                .eq(OfflineOrder::getShopId,bo.getShopId()));

        if (oOrders!=null&& oOrders.size()>0) return -1L;// 订单号已存在
//        erpOrder.setCreateTime(DateUtils.getNowDate());
//        int rows = erpOrderMapper.insertErpOrder(erpOrder);
//        insertErpOrderItem(erpOrder);
//        return rows;
        if(bo.getItemList() == null || bo.getItemList().size() == 0) return -2L;
        else{
            // 循环查找是否缺少specId
            for (OfflineOrderCreateItemBo itemBo : bo.getItemList()) {
                if(itemBo.getSkuId()==null || itemBo.getQuantity()<=0) return -3L;
            }
        }

        OShop shop = shopMapper.selectById(bo.getShopId());
        Integer shopType = 0;
        if(shop!=null){
            shopType = shop.getType();
        }else return -4L;

        // 开始组合订单信息
        OfflineOrder order = new OfflineOrder();
        order.setOrderNum(bo.getOrderNum());
        order.setShopId(bo.getShopId());
        order.setShopType(shopType);
        order.setBuyerMemo(bo.getBuyerMemo());
        order.setRemark(bo.getRemark());
        order.setRefundStatus(1);
        order.setOrderStatus(1);
        order.setGoodsAmount(bo.getGoodsAmount());
        order.setPostFee(bo.getPostage());
        order.setAmount(bo.getGoodsAmount()+bo.getPostage());
        order.setSellerDiscount(bo.getSellerDiscount());
        order.setPlatformDiscount(0.0);
        order.setPayment(bo.getGoodsAmount()+bo.getPostage()-bo.getSellerDiscount());
        order.setReceiverName(bo.getReceiverName());
        order.setReceiverMobile(bo.getReceiverPhone());
        order.setProvince(bo.getProvince());
        order.setCity(bo.getCity());
        order.setTown(bo.getTown());
        order.setAddress(bo.getAddress());
        order.setOrderTime(new Date());
        order.setCreateTime(new Date());
        order.setShipType(0);
        order.setCreateBy(createBy);
        order.setAuditStatus(0);
        orderMapper.insert(order);

//        List<OOrderItem> itemList = new ArrayList<OOrderItem>();
        for (int i = 0; i < bo.getItemList().size(); i++) {
            OfflineOrderCreateItemBo itemBo = bo.getItemList().get(i);
            OfflineOrderItem orderItem = new OfflineOrderItem();

            orderItem.setOrderId(order.getId());
            orderItem.setOrderNum(bo.getOrderNum());
            if(bo.getItemList().size()==1) {
                orderItem.setSubOrderNum(bo.getOrderNum());
            }else{
                orderItem.setSubOrderNum(bo.getOrderNum()+(i+1));
            }
            orderItem.setSkuId(itemBo.getSkuId());
            orderItem.setGoodsId(0L);
            orderItem.setGoodsSkuId(Long.parseLong(itemBo.getSkuId()));
            orderItem.setGoodsTitle(itemBo.getGoodsName());
            orderItem.setGoodsImg(itemBo.getGoodsImg());
            orderItem.setGoodsSpec(itemBo.getSkuName());
            orderItem.setSkuNum(itemBo.getSkuCode());
            orderItem.setGoodsPrice(itemBo.getSalePrice().doubleValue());
            orderItem.setItemAmount(itemBo.getItemAmount().doubleValue());
            orderItem.setPayment(itemBo.getItemAmount().doubleValue());
            orderItem.setQuantity(itemBo.getQuantity());
            orderItem.setRefundCount(0);
            orderItem.setRefundStatus(1);
            orderItem.setOrderStatus(order.getOrderStatus());
            orderItem.setShipStatus(0);
            orderItem.setCreateTime(new Date());
            orderItem.setCreateBy(createBy);
            orderItemMapper.insert(orderItem);
//            itemList.add(orderItem);
        }

        return Long.parseLong(order.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> confirmOrder(OfflineOrderConfirmBo orderConfirmBo) {
        OfflineOrder offlineOrder = orderMapper.selectById(orderConfirmBo.getId());
        if(offlineOrder==null) return ResultVo.error("订单数据不存在");
        if(offlineOrder.getAuditStatus()!=0) return ResultVo.error("已经确认过了！");
        List<OfflineOrderItem> offlineOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OfflineOrderItem>()
                .eq(OfflineOrderItem::getOrderId, offlineOrder.getId())
                .eq(OfflineOrderItem::getShipStatus, 0));
        if(offlineOrderItems==null || offlineOrderItems.isEmpty()){
            return ResultVo.error("找不到订单item");
        }
        OOrder erpOrder = oOrderMapper.selectOne(new LambdaQueryWrapper<OOrder>().eq(OOrder::getOrderNum,offlineOrder.getOrderNum()));
        if(erpOrder!=null) {
            // 已经确认过了，更新自己
            OfflineOrder orderUpdate = new OfflineOrder();
            orderUpdate.setId(offlineOrder.getId());
            orderUpdate.setAuditStatus(1);
            orderUpdate.setAuditTime(new Date());
            orderMapper.updateById(orderUpdate);

            return ResultVo.error("已经确认过了");
        }
        // 开始确认
        OOrder order = new OOrder();
        order.setOrderNum(offlineOrder.getOrderNum());
        order.setShopType(offlineOrder.getShopType());
        order.setShopId(offlineOrder.getShopId());
        order.setShipType(0);
        order.setBuyerMemo(offlineOrder.getBuyerMemo());
        order.setSellerMemo(offlineOrder.getSellerMemo());
        order.setRefundStatus(offlineOrder.getRefundStatus());
        order.setOrderStatus(offlineOrder.getOrderStatus());
        order.setGoodsAmount(offlineOrder.getGoodsAmount());
        order.setPostFee(offlineOrder.getPostFee());
        order.setSellerDiscount(offlineOrder.getSellerDiscount());
        order.setPlatformDiscount(offlineOrder.getPlatformDiscount());
        order.setAmount(offlineOrder.getAmount());
        order.setPayment(offlineOrder.getPayment());
        order.setReceiverName(orderConfirmBo.getReceiver());
        order.setReceiverMobile(orderConfirmBo.getMobile());
        order.setAddress(orderConfirmBo.getAddress());
        order.setProvince(orderConfirmBo.getProvince());
        order.setCity(orderConfirmBo.getCity());
        order.setTown(orderConfirmBo.getTown());
        order.setOrderTime(offlineOrder.getOrderTime());
        order.setShipper(-1L);
        order.setShipStatus(0);
        order.setCreateTime(new Date());
        order.setCreateBy("手动确认订单");
        oOrderMapper.insert(order);
        //插入item
        for (var item : offlineOrderItems) {
            OOrderItem oOrderItem = new OOrderItem();
            oOrderItem.setOrderId(order.getId());
            oOrderItem.setOrderNum(item.getOrderNum());
            oOrderItem.setSubOrderNum(item.getSubOrderNum());
            oOrderItem.setShopType(order.getShopType());
            oOrderItem.setShopId(order.getShopId());
            oOrderItem.setSkuId(item.getSkuId());
            oOrderItem.setGoodsId(item.getGoodsId());
            oOrderItem.setGoodsSkuId(item.getGoodsSkuId());
            oOrderItem.setGoodsTitle(item.getGoodsTitle());
            oOrderItem.setGoodsImg(item.getGoodsImg());
            oOrderItem.setGoodsNum(item.getGoodsNum());
            oOrderItem.setGoodsSpec(item.getGoodsSpec());
            oOrderItem.setSkuNum(item.getSkuNum());
            oOrderItem.setGoodsPrice(item.getGoodsPrice());
            oOrderItem.setItemAmount(item.getItemAmount());
            oOrderItem.setDiscountAmount(0.0);
            oOrderItem.setPayment(item.getPayment());
            oOrderItem.setQuantity(item.getQuantity());
            oOrderItem.setRefundCount(item.getRefundCount());
            oOrderItem.setRefundStatus(item.getRefundStatus());
            oOrderItem.setShipper(-1L);
            oOrderItem.setShipType(order.getShipType());
            oOrderItem.setShipStatus(0);
            oOrderItem.setCreateTime(new Date());
            oOrderItem.setCreateBy("手动确认订单");
            oOrderItemMapper.insert(oOrderItem);
        }

        // 已经确认过了，更新自己
        OfflineOrder douOrderUpdate = new OfflineOrder();
        douOrderUpdate.setId(offlineOrder.getId());
        douOrderUpdate.setAuditStatus(1);
        douOrderUpdate.setAuditTime(new Date());
        orderMapper.updateById(douOrderUpdate);
        return ResultVo.success();

    }

    @Transactional
    @Override
    public int orderLogistics(OfflineOrderShipBo bo, String operator) {
        if(!StringUtils.hasText(bo.getOrderNum())) return -1;
        if(!StringUtils.hasText(bo.getShippingNumber()) || !StringUtils.hasText(bo.getShippingCompany())) return -2;
        List<OfflineOrder> offlineOrders = orderMapper.selectList(new LambdaQueryWrapper<OfflineOrder>().eq(OfflineOrder::getOrderNum, bo.getOrderNum()));
        if(offlineOrders==null || offlineOrders.size()==0) return -3;
        else if(offlineOrders.get(0).getOrderStatus()!=1) return -4;
        if(offlineOrders.get(0).getRefundStatus()!=1) return -5;

        OfflineOrder update = new OfflineOrder();
        update.setId(offlineOrders.get(0).getId());
        update.setShippingCompany(bo.getShippingCompany());
        update.setShippingNumber(bo.getShippingNumber());
        update.setUpdateBy(operator);
        update.setUpdateTime(new Date());
        int rows = orderMapper.updateById(update);
        if (rows > 0){
            // TODO 通知OMS系统备货
            mqUtils.sendApiMessage(MqMessage.build(EnumShopType.OFFLINE, MqType.SHIP_STOCKUP_MESSAGE, bo.getOrderNum()));
        }
        return 1;
    }
}




