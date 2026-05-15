package cn.qihangerp.service.impl;

import cn.qihangerp.common.DateHelper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ShopOrderItemMapper;
import cn.qihangerp.model.entity.ShopOrderItem;
import cn.qihangerp.model.query.ShopOrderQueryBo;
import cn.qihangerp.service.ShopOrderItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
* @author qilip
* @description 针对表【oms_shop_order_item】的数据库操作Service实现
* @createDate 2025-07-15 11:36:42
*/
@Service
public class ShopOrderItemServiceImpl extends ServiceImpl<ShopOrderItemMapper, ShopOrderItem>
    implements ShopOrderItemService {

    @Override
    public PageResult<ShopOrderItem> queryPageList(ShopOrderQueryBo bo, PageQuery pageQuery) {
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

        LambdaQueryWrapper<ShopOrderItem> queryWrapper = new LambdaQueryWrapper<ShopOrderItem>()
                .eq(bo.getOrderStatus()!=null, ShopOrderItem::getOrderStatus,bo.getOrderStatus())
                .eq(bo.getRefundStatus()!=null, ShopOrderItem::getRefundStatus,bo.getRefundStatus())
                .eq(bo.getShopId()!=null, ShopOrderItem::getShopId,bo.getShopId())
                .eq(bo.getShopType()!=null, ShopOrderItem::getShopType,bo.getShopType())
                .eq(bo.getMerchantId()!=null, ShopOrderItem::getMerchantId,bo.getMerchantId())
                .ge(startTimestamp!=null, ShopOrderItem::getOrderTime,startTimestamp)
                .le(endTimestamp!=null, ShopOrderItem::getOrderTime,endTimestamp)
                .like(StringUtils.hasText(bo.getOrderId()), ShopOrderItem::getOrderId,bo.getOrderId())
                ;

        Page<ShopOrderItem> orderPage = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(orderPage);
    }
}




