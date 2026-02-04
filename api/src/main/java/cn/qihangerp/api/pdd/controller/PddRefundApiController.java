package cn.qihangerp.api.pdd.controller;


import cn.qihangerp.api.pdd.PddApiCommon;
import cn.qihangerp.api.pdd.PddPullRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.PddRefund;
import cn.qihangerp.module.service.PddRefundService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;

import cn.qihangerp.open.common.ApiResultVo;

import cn.qihangerp.open.pdd.PddRefundApiHelper;
import cn.qihangerp.open.pdd.model.AfterSale;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 更新
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/pdd/refund")
public class PddRefundApiController {
    private final PddRefundService refundService;
    private final PddApiCommon pddApiCommon;
    private final MqUtils mqUtils;
    private final OShopPullLogsService pullLogsService;
    private final OShopPullLasttimeService pullLasttimeService;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);


    /**
     * 增量更新订单
     * @param req
     * @
     * @throws
     */
    @PostMapping("/pull_list")
    @ResponseBody
    public AjaxResult pullRefundList(@RequestBody PddPullRequest req) throws Exception {
        log.info("/**************增量拉取pdd退款****************/");
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if(StringUtils.isEmpty(req.getUpdateTime())) return AjaxResult.error("参数错误，没有更新时间");
        else  {
            // 判断时间格式
            Matcher matcher = DATE_FORMAT.matcher(req.getUpdateTime());
            boolean b = matcher.find();
            if (!b) {
                return AjaxResult.error("更新日期格式错误");
            }
        }

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = pddApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        // 将时间字符串转换为 LocalDateTime
        startTime = LocalDateTime.parse(req.getUpdateTime() + " 00:00:01", formatter);
        endTime = LocalDateTime.parse(req.getUpdateTime() + " 23:59:59", formatter);

//        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(req.getShopId(), "REFUND");
//        if(lasttime == null){
//            endTime = LocalDateTime.now();
////            startTime = endTime.minusDays(1);
//            startTime = endTime.minusMinutes(30);
//        }else {
//            startTime = lasttime.getLasttime().minusMinutes(5);//取上次结束5分钟前
//            endTime = startTime.plusMinutes(30);//结束时间取开始时间之后30分钟
//            if(endTime.isAfter(LocalDateTime.now())){
//                endTime = LocalDateTime.now();
//            }
//        }
        String pullParams = "{startTime:"+startTime.format(formatter)+",endTime:"+endTime.format(formatter)+"}";
        Long startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
        Long endTimestamp = startTimestamp + 30 * 60;
        LocalDateTime start = Instant.ofEpochSecond(startTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = Instant.ofEpochSecond(endTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.info("=====拉取售后：{}-{}",start.format(formatter),end.format(formatter));
        //获取
        ApiResultVo<AfterSale> upResult = PddRefundApiHelper.pullRefundList(appKey, appSecret, accessToken, startTimestamp.intValue(), endTimestamp.intValue(), 1, 100);
        if (upResult.getCode() != 0) {
            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(req.getShopId());
            logs.setShopType(EnumShopType.PDD.getIndex());
            logs.setPullType("REFUND");
            logs.setPullWay("主动拉取退款");
            logs.setPullParams(pullParams);
            logs.setPullResult(upResult.getMsg());
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - beginTime);
            pullLogsService.save(logs);
            return AjaxResult.error(upResult.getCode(), upResult.getMsg());
        }
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        //循环插入订单数据到数据库
        for (var refund : upResult.getList()) {
            PddRefund pddRefund = new PddRefund();
            BeanUtils.copyProperties(refund,pddRefund);
            //插入订单数据
            var result = refundService.saveRefund(req.getShopId(), pddRefund);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "存在、更新************开始通知****/");
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "不存在、新增************开始通知****/");
                insertSuccess++;
            } else {
                log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "报错****************/");
                totalError++;
            }
        }

        for (int i = 1; i < 48; i++) {
            startTimestamp = endTimestamp;
            endTimestamp = startTimestamp + 30 * 60;
             start = Instant.ofEpochSecond(startTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
             end = Instant.ofEpochSecond(endTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            log.info("=====拉取售后：{}-{}",start.format(formatter),end.format(formatter));
            ApiResultVo<AfterSale> upResultFor = PddRefundApiHelper.pullRefundList(appKey, appSecret, accessToken, startTimestamp.intValue(), endTimestamp.intValue(), 1, 100);
            if (upResultFor.getCode() == 0) {
                //循环插入订单数据到数据库
                for (var refund : upResultFor.getList()) {
                    PddRefund pddRefund = new PddRefund();
                    BeanUtils.copyProperties(refund,pddRefund);
                    //插入订单数据
                    var result = refundService.saveRefund(req.getShopId(), pddRefund);
                    if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                        //已经存在
                        log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "存在、更新************开始通知****/");
                        hasExistOrder++;
                    } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                        log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "不存在、新增************开始通知****/");
                        insertSuccess++;
                    } else {
                        log.info("/**************主动更新pdd退款：开始更新数据库：" + refund.getId() + "报错****************/");
                        totalError++;
                    }
                }
            }
        }

//        if(totalError==0) {
//            if (lasttime == null) {
//                // 新增
//                OShopPullLasttime insertLasttime = new OShopPullLasttime();
//                insertLasttime.setShopId(req.getShopId());
//                insertLasttime.setCreateTime(new Date());
//                insertLasttime.setLasttime(endTime);
//                insertLasttime.setPullType("REFUND");
//                pullLasttimeService.save(insertLasttime);
//
//            } else {
//                // 修改
//                OShopPullLasttime updateLasttime = new OShopPullLasttime();
//                updateLasttime.setId(lasttime.getId());
//                updateLasttime.setUpdateTime(new Date());
//                updateLasttime.setLasttime(endTime);
//                pullLasttimeService.updateById(updateLasttime);
//            }
//        }

        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.PDD.getIndex());
        logs.setShopId(req.getShopId());
        logs.setPullType("REFUND");
        logs.setPullWay("主动拉取退款");
        logs.setPullParams(pullParams);
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);

        String msg = "成功{startTime:"+startTime.format(formatter)+",endTime:"+endTime.format(formatter)+"} ，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("/**************主动更新PDD退款：END：" + msg + "****************/");
        return AjaxResult.success(msg);
    }


}
