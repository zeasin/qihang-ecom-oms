package cn.qihangerp.api.tao.controller;

import cn.qihangerp.api.tao.TaoApiCommon;
import cn.qihangerp.api.tao.TaoRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.TaoRefund;
import cn.qihangerp.module.service.TaoRefundService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.tao.TaoRefundApiHelper;
import cn.qihangerp.open.tao.response.TaoRefundResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@RestController
@RequestMapping("/tao/refund")
public class TaoRefundApiController {
    private static Logger log = LoggerFactory.getLogger(TaoRefundApiController.class);
    private final TaoApiCommon taoApiCommon;
    private final TaoRefundService refundService;
    private final MqUtils mqUtils;
    private final OShopPullLogsService pullLogsService;
    private final OShopPullLasttimeService pullLasttimeService;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);

    /**
     * 更新退货订单
     *
     * @return
     * @throws
     */
    @RequestMapping("/pull_refund")
    @ResponseBody
    public AjaxResult refundOrderPull(@RequestBody TaoRequest taoRequest) throws IOException {
        log.info("/**************主动更新tao退货订单****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
//            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误，没有店铺Id");
            return AjaxResult.error(HttpStatus.PARAMS_ERROR,  "参数错误，没有店铺Id");
        }
        if(StringUtils.isEmpty(taoRequest.getUpdateTime())) return AjaxResult.error("参数错误，没有更新时间");
        else  {
            // 判断时间格式
            Matcher matcher = DATE_FORMAT.matcher(taoRequest.getUpdateTime());
            boolean b = matcher.find();
            if (!b) {
                return AjaxResult.error("更新日期格式错误");
            }
        }

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        Long shopId = taoRequest.getShopId();
        var checkResult = taoApiCommon.checkBefore(shopId);

        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg());
        }

        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        // 将时间字符串转换为 LocalDateTime
        startTime = LocalDateTime.parse(taoRequest.getUpdateTime() + " 00:00:01", formatter);
        endTime = LocalDateTime.parse(taoRequest.getUpdateTime() + " 23:59:59", formatter);
//        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(taoRequest.getShopId(), "REFUND");
//        if(lasttime == null){
//            endTime = LocalDateTime.now();
//            startTime = endTime.minusDays(1);
//        }else{
//            startTime = lasttime.getLasttime().minusHours(1);//取上次结束一个小时前
//            Duration duration = Duration.between(startTime, LocalDateTime.now());
//            long hours = duration.toHours();
//            if (hours > 24) {
//                // 大于24小时，只取24小时
//                endTime = startTime.plusHours(24);
//            } else {
//                endTime = LocalDateTime.now();
//            }
////            endTime = startTime.plusDays(1);//取24小时
////            if(endTime.isAfter(LocalDateTime.now())){
////                endTime = LocalDateTime.now();
////            }
//        }
//        Long pageSize = 100l;
//        Long pageIndex = 1l;

        //第一次获取
//        ApiResultVo<TaoRefundResponse> upResult = RefundApiHelper.pullRefund(startTime,endTime, url, appKey, appSecret, sessionKey);
        ApiResultVo<TaoRefundResponse> upResult = TaoRefundApiHelper.pullRefund(startTime, endTime, appKey, appSecret, sessionKey);
        if (upResult.getCode() != 0) {
            log.info("/**************主动更新tao退货订单：第一次获取结果失败：" + upResult.getMsg() + "****************/");
//            return new ApiResult<>(EnumResultVo.SystemException.getIndex(), upResult.getMsg());
            return AjaxResult.error(HttpStatus.ERROR ,upResult.getMsg());
        }

        log.info("/**************主动更新tao退货订单：第一次获取结果：总记录数" + upResult.getTotalRecords() + "****************/");
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数

        //循环插入订单数据到数据库
        for (var refund : upResult.getList()) {
            TaoRefund taoRefund = new TaoRefund();
            BeanUtils.copyProperties(refund,taoRefund);
            taoRefund.setShopId(taoRequest.getShopId());
            taoRefund.setDesc1(refund.getDesc());
            //插入订单数据
            var result = refundService.saveAndUpdateRefund(shopId, taoRefund);
            if (result == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.REFUND_MESSAGE,refund.getRefundId()));
                log.info("/**************主动更新tao退货订单：开始更新数据库：" + refund.getRefundId() + "存在、更新****************/");
                hasExistOrder++;
            } else if (result == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新tao退货订单：开始插入数据库：" + refund.getRefundId() + "不存在、新增****************/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.REFUND_MESSAGE,refund.getRefundId()));
                insertSuccess++;
            } else {
                log.info("/**************主动更新tao退货订单：开始更新数据库：" + refund.getRefundId() + "报错****************/");
                totalError++;
            }
        }
//        if(totalError ==0) {
//            if (lasttime == null) {
//                // 新增
//                OShopPullLasttime insertLasttime = new OShopPullLasttime();
//                insertLasttime.setShopId(taoRequest.getShopId());
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
        String msg = "成功，总共找到：" + upResult.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.TAO.getIndex());
        logs.setShopId(taoRequest.getShopId());
        logs.setPullType("REFUND");
        logs.setPullWay("主动拉取");
        logs.setPullParams("{startTime:"+startTime+",endTime:"+endTime+"}");
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);
        log.info("/**************主动更新tao订单：END：" + msg + "****************/");
//        return new ApiResult<>(EnumResultVo.SUCCESS.getIndex(), msg);
        return AjaxResult.success(msg);
    }

    @RequestMapping("/pull_refund_detail")
    @ResponseBody
    public AjaxResult refundDetailPull(@RequestBody TaoRequest taoRequest) throws IOException {
        log.info("/**************主动更新tao退货订单****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if (taoRequest.getRefundId() == null || taoRequest.getRefundId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有refundId");
        }
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        Long shopId = taoRequest.getShopId();
        var checkResult = taoApiCommon.checkBefore(shopId);

        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg());
        }

        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        //获取
        ApiResultVo<TaoRefundResponse> upResult = TaoRefundApiHelper.pullRefundDetail(taoRequest.getRefundId(), appKey, appSecret, sessionKey);

        if (upResult.getCode() != 0) {
            log.info("/**************主动更新tao退货订单：第一次获取结果失败：" + upResult.getMsg() + "****************/");
            return AjaxResult.error(HttpStatus.ERROR, upResult.getMsg());
        }
        TaoRefund taoRefund = new TaoRefund();
        BeanUtils.copyProperties(upResult.getData(),taoRefund);
        //插入订单数据
        var result = refundService.saveAndUpdateRefund(shopId, taoRefund);
        if (result == ResultVoEnum.DataExist.getIndex()) {
            //已经存在
            mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.REFUND_MESSAGE, upResult.getData().getRefundId()));
        } else if (result == ResultVoEnum.SUCCESS.getIndex()) {
            mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.REFUND_MESSAGE, upResult.getData().getRefundId()));
        }


        return AjaxResult.success();
    }

}
