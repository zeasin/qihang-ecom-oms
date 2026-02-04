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
import cn.qihangerp.model.entity.PddOrder;
import cn.qihangerp.model.entity.PddOrderItem;
import cn.qihangerp.module.service.PddOrderService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.pdd.PddOrderApiHelper;
import cn.qihangerp.open.pdd.model.OrderListResultVo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 淘系订单更新
 */
@AllArgsConstructor
@RestController
@RequestMapping("/pdd/order")
public class PddOrderApiController {
    private static Logger log = LoggerFactory.getLogger(PddOrderApiController.class);

    private final PddOrderService orderService;
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
    @PostMapping("/pull_order")
    @ResponseBody
    public AjaxResult pullOrder(@RequestBody PddPullRequest req) throws Exception {
        log.info("/**************增量拉取pdd订单****************/");
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if(StringUtils.isEmpty(req.getOrderDate())) return AjaxResult.error("参数错误，没有下单日期");

        String orderDate = null;
        if(StringUtils.hasText(req.getOrderDate())) {
            // 判断时间格式
            Matcher matcher = DATE_FORMAT.matcher(req.getOrderDate());
            boolean b = matcher.find();
            if (!b) {
                return AjaxResult.error("下单日期格式错误");
            }
            orderDate = req.getOrderDate();
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
//        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(req.getShopId(), "ORDER");
//        if(lasttime == null){
//            endTime = LocalDateTime.now();
//            startTime = endTime.minusDays(1);
//        }else {
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
        // 使用条件传过来的时间
        // 将时间字符串转换为 LocalDateTime
        startTime = LocalDateTime.parse(orderDate + " 00:00:01", formatter);
        endTime = LocalDateTime.parse(orderDate + " 23:59:59", formatter);

        String startTimeStr = startTime.format(formatter);
        String endTimeStr = endTime.format(formatter);
        String pullParams = "{startTime:"+startTimeStr+",endTime:"+endTimeStr+"}";

        Long startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
        Long endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));

        log.info("开始循环更新拼多多订单。开始时间：" + startTimeStr + "结束时间：" + endTimeStr);

        //获取
        ApiResultVo<OrderListResultVo> upResult = PddOrderApiHelper.pullOrderList(appKey, appSecret, accessToken, startTimestamp.intValue(), endTimestamp.intValue(), 1, 20);
        if(upResult.getCode() !=0 ){
            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(req.getShopId());
            logs.setShopType(EnumShopType.DOU.getIndex());
            logs.setPullType("ORDER");
            logs.setPullWay("主动拉取订单");
            logs.setPullParams(pullParams);
            logs.setPullResult(upResult.getMsg());
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - beginTime);
            pullLogsService.save(logs);
            return AjaxResult.error(upResult.getCode(),upResult.getMsg());
        }

        log.info("/**************主动更新pdd订单：获取结果：总记录数" + upResult.getTotalRecords() + "****************/");
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数

        //循环插入订单数据到数据库
        for (var trade : upResult.getData().getOrderList()) {
            PddOrder pddOrder = new PddOrder();
            BeanUtils.copyProperties(trade,pddOrder);
            List<PddOrderItem> items = new ArrayList<>();
            for (var it:trade.getItemList()) {
                PddOrderItem item = new PddOrderItem();
                BeanUtils.copyProperties(it,item);
                items.add(item);
            }
            pddOrder.setItems(items);
            //插入订单数据
            var result = orderService.saveOrder(req.getShopId(), pddOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新pdd订单：开始更新数据库：" + pddOrder.getOrderSn() + "存在、更新************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD, MqType.ORDER_MESSAGE,pddOrder.getOrderSn()));
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新pdd订单：开始更新数据库：" + pddOrder.getOrderSn() + "不存在、新增************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD,MqType.ORDER_MESSAGE,pddOrder.getOrderSn()));
                insertSuccess++;
            } else {
                log.info("/**************主动更新pdd订单：开始更新数据库：" + pddOrder.getOrderSn() + "报错****************/");
                totalError++;
            }
        }
//        if(totalError == 0) {
//            if (lasttime == null) {
//                // 新增
//                OShopPullLasttime insertLasttime = new OShopPullLasttime();
//                insertLasttime.setShopId(req.getShopId());
//                insertLasttime.setCreateTime(new Date());
//                insertLasttime.setLasttime(endTime);
//                insertLasttime.setPullType("ORDER");
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
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.PDD.getIndex());
        logs.setShopId(req.getShopId());
        logs.setPullType("ORDER");
        logs.setPullWay("主动拉取订单");
        logs.setPullParams(pullParams);
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);

        String msg = "成功{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}总共找到：" + upResult.getData().getOrderList().size() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("/**************主动更新pdd订单：END：" + msg + "****************/");
        return AjaxResult.success(msg);
    }


    /**
     * 更新单个订单
     *
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/pull_order_detail")
    @ResponseBody
    public AjaxResult getOrderPullDetail(@RequestBody PddPullRequest req) throws Exception {
        log.info("/**************主动更新pdd订单by number****************/");
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if (StringUtils.isEmpty(req.getOrderId())) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
        }

        var checkResult = pddApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();


        var resultVo = PddOrderApiHelper.pullOrderDetail(appKey, appSecret, accessToken,req.getOrderId());
        if (resultVo.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
            PddOrder pddOrder = new PddOrder();
            BeanUtils.copyProperties(resultVo.getData(),pddOrder);
            List<PddOrderItem> orderItemList = new ArrayList<>();
            if(resultVo.getData().getItemList()!=null&&resultVo.getData().getItemList().size()>0){
                for(var item:resultVo.getData().getItemList()){
                    PddOrderItem pddOrderItem = new PddOrderItem();
                    BeanUtils.copyProperties(item,pddOrderItem);
                    orderItemList.add(pddOrderItem);
                }
            }
            pddOrder.setItems(orderItemList);
            var result = orderService.saveOrder(req.getShopId(), pddOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新PDD订单：开始更新数据库：" + resultVo.getData().getOrderSn() + "存在、更新****************/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD, MqType.ORDER_MESSAGE,resultVo.getData().getOrderSn()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新PDD订单：开始更新数据库：" + resultVo.getData().getOrderSn() + "不存在、新增****************/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD,MqType.ORDER_MESSAGE,resultVo.getData().getOrderSn()));
            }

            return AjaxResult.success();
        } else {
            return AjaxResult.error(resultVo.getCode(), resultVo.getMsg());
        }
    }
}
