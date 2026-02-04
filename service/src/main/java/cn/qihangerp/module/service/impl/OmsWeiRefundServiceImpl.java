package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.model.bo.WeiRefundBo;
import cn.qihangerp.model.entity.DouRefund;
import cn.qihangerp.model.entity.OmsWeiGoodsSku;
import cn.qihangerp.model.entity.OmsWeiRefund;
import cn.qihangerp.mapper.OmsWeiGoodsSkuMapper;
import cn.qihangerp.mapper.OmsWeiOrderItemMapper;
import cn.qihangerp.mapper.OmsWeiRefundMapper;
import cn.qihangerp.module.service.OmsWeiRefundService;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author TW
* @description 针对表【oms_wei_refund(视频号小店退款)】的数据库操作Service实现
* @createDate 2024-06-20 17:07:27
*/
@AllArgsConstructor
@Service
public class OmsWeiRefundServiceImpl extends ServiceImpl<OmsWeiRefundMapper, OmsWeiRefund>
    implements OmsWeiRefundService {
    private final OmsWeiRefundMapper mapper;
    private final OmsWeiGoodsSkuMapper goodsSkuMapper;
    private final OmsWeiOrderItemMapper orderItemMapper;
//    private final MQClientService mqClientService;
    private final String DATE_PATTERN =
        "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);

    @Override
    public PageResult<OmsWeiRefund> queryPageList(WeiRefundBo bo, PageQuery pageQuery) {
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

        LambdaQueryWrapper<OmsWeiRefund> queryWrapper = new LambdaQueryWrapper<OmsWeiRefund>()
                .eq(bo.getShopId()!=null,OmsWeiRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsWeiRefund::getOrderId,bo.getOrderId())
                .ge(StringUtils.hasText(bo.getCreateTime()), OmsWeiRefund::getCreateTime, startTimestamp)
                .le(StringUtils.hasText(bo.getCreateTime()),OmsWeiRefund::getCreateTime,endTimestamp)
                ;

        Page<OmsWeiRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveRefund(Long shopId, OmsWeiRefund refund) {
        try {
            List<OmsWeiRefund> refunds = mapper.selectList(new LambdaQueryWrapper<OmsWeiRefund>().eq(OmsWeiRefund::getAfterSaleOrderId, refund.getAfterSaleOrderId()));
            OmsWeiRefund newRefund = new OmsWeiRefund();
            if (refunds != null && refunds.size() > 0) {
                newRefund = refunds.get(0);
                // 存在，修改
                OmsWeiRefund update = new OmsWeiRefund();
                update.setId(refunds.get(0).getId());
                update.setOrderId(refund.getOrderId());
                update.setStatus(refund.getStatus());
                update.setUpdateTime(refund.getUpdateTime());
                update.setReturnWaybillId(refund.getReturnWaybillId());
                update.setReturnDeliveryName(refund.getReturnDeliveryName());
                update.setReturnDeliveryId(refund.getReturnDeliveryId());
                update.setComplaintId(refund.getComplaintId());
                if(refund.getSkuId()!=null) {
                    List<OmsWeiGoodsSku> pddGoodsSku = goodsSkuMapper.selectList(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getSkuId, refund.getSkuId()));
                    if (pddGoodsSku != null && !pddGoodsSku.isEmpty()) {
                        update.setOGoodsId(pddGoodsSku.get(0).getErpGoodsId());
                        update.setOGoodsSkuId(pddGoodsSku.get(0).getErpGoodsSkuId());
                    }
                }
                mapper.updateById(update);

                return ResultVo.error(ResultVoEnum.DataExist, "退款已经存在，更新成功");
            } else {
                newRefund = refund;
                // 不存在，新增
                if(refund.getSkuId()!=null) {
                    List<OmsWeiGoodsSku> pddGoodsSku = goodsSkuMapper.selectList(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getSkuId, refund.getSkuId()));
                    if (pddGoodsSku != null && !pddGoodsSku.isEmpty()) {
                        refund.setOGoodsId(pddGoodsSku.get(0).getErpGoodsId());
                        refund.setOGoodsSkuId(pddGoodsSku.get(0).getErpGoodsSkuId());
                    }
                }
                refund.setShopId(shopId);
                mapper.insert(refund);
                return ResultVo.success();
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }
}




