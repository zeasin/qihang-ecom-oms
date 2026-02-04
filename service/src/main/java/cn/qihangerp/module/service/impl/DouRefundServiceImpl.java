package cn.qihangerp.module.service.impl;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.mapper.DouOrderItemMapper;
import cn.qihangerp.mapper.DouRefundMapper;
import cn.qihangerp.model.entity.DouOrder;
import cn.qihangerp.model.entity.DouOrderItem;
import cn.qihangerp.model.entity.DouRefund;
import cn.qihangerp.model.bo.DouRefundBo;
import cn.qihangerp.module.service.DouRefundService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
* @description 针对表【dou_refund(抖店退款表)】的数据库操作Service实现
* @createDate 2024-06-06 16:48:38
*/
@AllArgsConstructor
@Service
public class DouRefundServiceImpl extends ServiceImpl<DouRefundMapper, DouRefund>
    implements DouRefundService {
    private final DouRefundMapper mapper;
    private final DouOrderItemMapper orderItemMapper;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);

    @Override
    public PageResult<DouRefund> queryPageList(DouRefundBo bo, PageQuery pageQuery) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if(StringUtils.hasText(bo.getCreateTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getCreateTime());
            boolean b = matcher.find();
            if(!b){
                bo.setCreateTime("");
            }
        }
        if(StringUtils.hasText(bo.getCreateTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(bo.getCreateTime() +" 00:00:00", formatter);
            LocalDateTime endTime = LocalDateTime.parse(bo.getCreateTime()+" 23:59:59", formatter);

            startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
            endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));
        }

        LambdaQueryWrapper<DouRefund> queryWrapper = new LambdaQueryWrapper<DouRefund>()
                .eq(bo.getShopId()!=null,DouRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getAftersaleId()),DouRefund::getAftersaleId,bo.getAftersaleId())
                .eq(StringUtils.hasText(bo.getOrderId()),DouRefund::getRelatedId,bo.getOrderId())
                .eq(StringUtils.hasText(bo.getAftersaleType()),DouRefund::getAftersaleType,bo.getAftersaleType())
                .ge(StringUtils.hasText(bo.getCreateTime()), DouRefund::getApplyTime, startTimestamp)
                .le(StringUtils.hasText(bo.getCreateTime()),DouRefund::getApplyTime,endTimestamp)
                ;

        Page<DouRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }
    @Override
    public ResultVo<Integer> saveRefund(Long shopId, DouRefund refund){
        List<DouRefund> origin = mapper.selectList(new LambdaQueryWrapper<DouRefund>().eq(DouRefund::getAftersaleId,refund.getAftersaleId()));
        if(origin!=null&&origin.size()>0){
            // 存在，修改
            DouRefund update = new DouRefund();
            update.setId(origin.get(0).getId());
            List<DouOrderItem> douOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getOrderId, refund.getOrderSkuOrderId()));
            if(!douOrderItems.isEmpty()){
                update.setSkuId(douOrderItems.get(0).getSkuId().toString());
                update.setOGoodsId(douOrderItems.get(0).getOGoodsId());
                update.setOGoodsSkuId(douOrderItems.get(0).getOGoodsSkuId());
            }
            update.setApplyTime(refund.getApplyTime());
            update.setAftersaleOrderType(refund.getAftersaleOrderType());
            update.setAftersaleType(refund.getAftersaleType());
            update.setAftersaleStatus(refund.getAftersaleStatus());
            update.setAftersaleStatusToFinalTime(refund.getAftersaleStatusToFinalTime());
            update.setOrderStatus(refund.getOrderStatus());
            update.setUpdateTime(refund.getUpdateTime());
            update.setStatusDeadline(refund.getStatusDeadline());
            update.setRefundAmount(refund.getRefundAmount());
            update.setRefundPostAmount(refund.getRefundPostAmount());
            update.setAftersaleNum(refund.getAftersaleNum());
            update.setPartType(refund.getPartType());
            update.setAftersaleRefundType(refund.getAftersaleRefundType());
            update.setRefundType(refund.getRefundType());
            update.setArbitrateStatus(refund.getArbitrateStatus());
            update.setRefundStatus(refund.getRefundStatus());
            update.setRiskDecisionReason(refund.getRiskDecisionReason());
            update.setRiskDecisionDescription(refund.getRiskDecisionDescription());
            update.setArbitrateBlame(refund.getArbitrateBlame());
            update.setReturnLogisticsCode(refund.getReturnLogisticsCode());
            update.setReturnLogisticsCompanyName(refund.getReturnLogisticsCompanyName());
            update.setExchangeSkuInfo(refund.getExchangeSkuInfo());
            update.setExchangeLogisticsCompanyName(refund.getExchangeLogisticsCompanyName());
            update.setRemark(refund.getRemark());
            update.setGotPkg(refund.getGotPkg());
            update.setIsAgreeRefuseSign(refund.getIsAgreeRefuseSign());
            update.setOrderLogistics(refund.getOrderLogistics());
            update.setAftersaleTags(refund.getAftersaleTags());
            update.setAftersaleSubType(refund.getAftersaleSubType());
            update.setAutoAuditBits(refund.getAutoAuditBits());
            update.setTextPart(refund.getTextPart());
            update.setRelatedOrderInfo(refund.getRelatedOrderInfo());
            update.setPullLastTime(new Date());
            mapper.updateById(update);
            return ResultVo.error(ResultVoEnum.DataExist, "退款已经存在，更新成功");

        }else{
            //查询子订单
            List<DouOrderItem> douOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<DouOrderItem>().eq(DouOrderItem::getOrderId, refund.getOrderSkuOrderId()));
            if(!douOrderItems.isEmpty()){
                refund.setSkuId(douOrderItems.get(0).getSkuId().toString());
                refund.setOGoodsId(douOrderItems.get(0).getOGoodsId());
                refund.setOGoodsSkuId(douOrderItems.get(0).getOGoodsSkuId());
            }
            refund.setShopId(shopId);
            refund.setPullTime(new Date());
            mapper.insert(refund);
            return ResultVo.success();
        }

    }

    @Override
    public DouRefund queryByAftersaleId(String aftersaleId) {
        LambdaQueryWrapper<DouRefund> queryWrapper = new LambdaQueryWrapper<DouRefund>()
                .eq(DouRefund::getAftersaleId,aftersaleId);
        List<DouRefund> douRefunds = mapper.selectList(queryWrapper);
        if(douRefunds.isEmpty())
            return null;
        else
            return douRefunds.get(0);
    }
}




