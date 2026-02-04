package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.JdApiCommon;
import cn.qihangerp.api.jd.JdPullRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.JdRefund;
import cn.qihangerp.module.service.JdRefundService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;

import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.jd.JdAfterSaleApiHelper;

import cn.qihangerp.open.jd.model.AfterSale;
import cn.qihangerp.open.jd.model.Refund;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequestMapping("/jd/refund")
@RestController
@AllArgsConstructor
public class JdOrderAfterSaleApiController {
    private final JdApiCommon jdApiCommon;
    private final OShopPullLogsService pullLogsService;
    private final MqUtils mqUtils;
    private final OShopPullLasttimeService pullLasttimeService;
    private final JdRefundService afterSaleService;

    /**
     * 拉取售后数据
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_after_list", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody JdPullRequest req) throws Exception {
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if(StringUtils.isBlank(req.getCreateTime())) return AjaxResult.error("缺少参数：createTime");

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();
        var checkResult = jdApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        Long sellerId = checkResult.getData().getSellerId();

//        // 获取最后更新时间
//        LocalDateTime startTime = null;
//        LocalDateTime endTime = null;
//        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(params.getShopId(), "REFUND");
//        if (lasttime == null) {
//            endTime = LocalDateTime.now();
//            startTime = endTime.minusDays(1);
//        } else {
//            startTime = lasttime.getLasttime().minusHours(1);//取上次结束一个小时前
//            endTime = startTime.plusDays(1);//取24小时
//            if (endTime.isAfter(LocalDateTime.now())) {
//                endTime = LocalDateTime.now();
//            }
//        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        // 将时间字符串转换为 LocalDateTime
        startTime = LocalDateTime.parse(req.getCreateTime() + " 00:00:01", formatter);
        endTime = LocalDateTime.parse(req.getCreateTime() + " 23:59:59", formatter);

//        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //获取退款
        ApiResultVo<Refund> refundVo = JdAfterSaleApiHelper.pullRefundList(startTime, endTime, appKey, appSecret, accessToken);

        int insertSuccess = 0;
        int totalError = 0;
        int hasExist = 0;

        //循环插入订单数据到数据库
        /*******处理取消订单*****/
        for (var item : refundVo.getList()) {
            JdRefund afterSale = new JdRefund();
            afterSale.setApplyId(item.getId());
            afterSale.setServiceId(item.getId());
            afterSale.setRefundId(item.getId());
            afterSale.setOrderId(item.getOrderId());
            afterSale.setApplyTime(item.getApplyTime());
            afterSale.setCustomerExpect(1);//售前退款
            afterSale.setCustomerName("售前退款");
            afterSale.setApplyRefundSum(item.getApplyRefundSum());
            afterSale.setBuyerId(item.getBuyerId());
            afterSale.setBuyerName(item.getBuyerName());
            afterSale.setRefundCheckTime(item.getCheckTime());
            afterSale.setRefundStatus(item.getStatus());
            afterSale.setRefundCheckUsername(item.getCheckUserName());
            afterSale.setRefundCheckRemark(item.getCheckRemark());
            afterSale.setRefundReason(item.getReason());
            afterSale.setRefundSystemId(item.getSystemId());
//            jdAfterSaleList.add(afterSale);
            var result = afterSaleService.saveRefund(req.getShopId(), afterSale);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExist++;

//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, afterSale.getServiceId().toString()));
//                kafkaTemplate.send(MqType.REFUND_MQ, JSONObject.toJSONString(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE,afterSale.getServiceId().toString())));
            } else {
                totalError++;
            }
        }

        //获取售后
        ApiResultVo<AfterSale> afterSaleVo = JdAfterSaleApiHelper.pullAfterSaleList(sellerId, startTime, endTime, appKey, appSecret, accessToken);
        /*******处理售后list*****/
        for (var after : afterSaleVo.getList()) {
            JdRefund afterSale = new JdRefund();
            BeanUtils.copyProperties(after, afterSale);
//            afterSale.setOrderId(after.getOrderId() + "");
            afterSale.setApplyTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date(after.getApplyTime())));
            afterSale.setRefundId(0L);
            afterSale.setShopId(req.getShopId());
            var result = afterSaleService.saveRefund(req.getShopId(), afterSale);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExist++;
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
//                kafkaTemplate.send(MqType.REFUND_MQ, JSONObject.toJSONString(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE,afterSale.getServiceId().toString())));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
//                kafkaTemplate.send(MqType.REFUND_MQ, JSONObject.toJSONString(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE,afterSale.getServiceId().toString())));
            } else {
                totalError++;
            }
        }

//        if (lasttime == null) {
//            // 新增
//            OShopPullLasttime insertLasttime = new OShopPullLasttime();
//            insertLasttime.setShopId(params.getShopId());
//            insertLasttime.setCreateTime(new Date());
//            insertLasttime.setLasttime(endTime);
//            insertLasttime.setPullType("REFUND");
//            pullLasttimeService.save(insertLasttime);
//
//        } else {
//            // 修改
//            OShopPullLasttime updateLasttime = new OShopPullLasttime();
//            updateLasttime.setId(lasttime.getId());
//            updateLasttime.setUpdateTime(new Date());
//            updateLasttime.setLasttime(endTime);
//            pullLasttimeService.updateById(updateLasttime);
//        }
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopId(req.getShopId());
        logs.setShopType(EnumShopType.JD.getIndex());
        logs.setPullType("REFUND");
        logs.setPullWay("主动拉取");
        logs.setPullParams("{applyTimeStart:" + req.getCreateTime() + " 00:00:01" + ",applyTimeEnd:" + req.getCreateTime() + " 23:59:59" + ",PageIndex:1,PageSize:100}");
        logs.setPullResult("{total:" + insertSuccess + ",hasExist:" + hasExist + ",totalError:" + totalError + "}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);
//        mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE,item.getId()));
        return AjaxResult.success();
    }


}
