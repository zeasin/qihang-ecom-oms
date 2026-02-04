package cn.qihangerp.api.tao.controller;


import cn.qihangerp.api.tao.OrderAssembleHelper;
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
import cn.qihangerp.model.entity.TaoOrder;
import cn.qihangerp.module.service.TaoOrderService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;

import cn.qihangerp.open.tao.TaoOrderApiHelper;

import cn.qihangerp.open.tao.response.TaoOrderDetailResponse;
import cn.qihangerp.open.tao.response.TaoOrderListResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 淘系订单更新
 */
@AllArgsConstructor
@RestController
@RequestMapping("/tao/order")
public class TaoOrderApiController {
    private static Logger log = LoggerFactory.getLogger(TaoOrderApiController.class);

    private final TaoOrderService orderService;
    private final TaoApiCommon taoApiCommon;
    private final MqUtils mqUtils;
    private final OShopPullLogsService pullLogsService;
    private final OShopPullLasttimeService pullLasttimeService;
    /**
     * 增量更新订单
     * @param req
     * @return
     * @throws
     */
    @PostMapping("/pull_order_tao")
    @ResponseBody
    public AjaxResult pullIncrementOrder(@RequestBody TaoRequest req) throws IOException {
        log.info("/**************增量拉取tao订单****************/");
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = taoApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        log.info("/**************增量更新tao订单，条件判断完成，开始更新。。。。。。****************/");
        Long pageSize = 100l;
        Long pageIndex = 1l;
        // 取当前时间30分钟前
//        LocalDateTime endTime = LocalDateTime.now();
//        LocalDateTime startTime = endTime.minus(60*24, ChronoUnit.MINUTES);
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(req.getShopId(), "ORDER");
        if(lasttime == null){
            endTime = LocalDateTime.now();
            startTime = endTime.minusDays(1);
        }else {
            startTime = lasttime.getLasttime().minusHours(1);//取上次结束一个小时前
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            long hours = duration.toHours();
            if (hours > 24) {
                // 大于24小时，只取24小时
                endTime = startTime.plusHours(24);
            } else {
                endTime = LocalDateTime.now();
            }
//            endTime = startTime.plusDays(1);//取24小时
//            if(endTime.isAfter(LocalDateTime.now())){
//                endTime = LocalDateTime.now();
//            }
        }

        //第一次获取
//        ApiResultVo<TaoOrderResponse> upResult = OrderApiHelper.pullIncrementOrder(startTime,endTime,pageIndex, pageSize, url, appKey, appSecret, sessionKey);

        ApiResultVo<TaoOrderListResponse> upResult = TaoOrderApiHelper.pullTradeList(startTime,endTime,appKey, appSecret, sessionKey);
        if (upResult.getCode() !=  ResultVoEnum.SUCCESS.getIndex()) {
            log.info("/**************主动更新tao订单：第一次获取结果失败：" + upResult.getMsg() + "****************/");
            if(upResult.getCode() == HttpStatus.UNAUTHORIZED){
                return AjaxResult.error(HttpStatus.UNAUTHORIZED, "Token已过期，请重新授权",checkResult.getData());
            }
            return AjaxResult.error(HttpStatus.SYSTEM_EXCEPTION, upResult.getMsg());
        }

        log.info("/**************主动更新tao订单：第一次获取结果：总记录数" + upResult.getTotalRecords() + "****************/");
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数

        //循环插入订单数据到数据库
        for (var order : upResult.getList()) {
            TaoOrder taoOrder = OrderAssembleHelper.assembleOrder(order);
            //插入订单数据
            var result = orderService.saveOrder(req.getShopId(), taoOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新tao订单：开始更新数据库：" + order.getTid() + "存在、更新************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.ORDER_MESSAGE,order.getTid()));
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新tao订单：开始更新数据库：" + order.getTid() + "不存在、新增************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO,MqType.ORDER_MESSAGE,order.getTid()));
                insertSuccess++;
            } else {
                log.info("/**************主动更新tao订单：开始更新数据库：" + order.getTid() + "报错****************/");
                totalError++;
            }
        }
        if(totalError == 0) {
            if (lasttime == null) {
                // 新增
                OShopPullLasttime insertLasttime = new OShopPullLasttime();
                insertLasttime.setShopId(req.getShopId());
                insertLasttime.setCreateTime(new Date());
                insertLasttime.setLasttime(endTime);
                insertLasttime.setPullType("ORDER");
                pullLasttimeService.save(insertLasttime);

            } else {
                // 修改
                OShopPullLasttime updateLasttime = new OShopPullLasttime();
                updateLasttime.setId(lasttime.getId());
                updateLasttime.setUpdateTime(new Date());
                updateLasttime.setLasttime(endTime);
                pullLasttimeService.updateById(updateLasttime);
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.TAO.getIndex());
        logs.setShopId(req.getShopId());
        logs.setPullType("ORDER");
        logs.setPullWay("主动拉取");
        logs.setPullParams("{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}");
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);

        String msg = "成功{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}总共找到：" + upResult.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("/**************主动更新tao订单：END：" + msg + "****************/");
        return AjaxResult.success(msg);
    }


    /**
     * 更新单个订单
     *
     * @param taoRequest
     * @return
     * @throws
     */
    @RequestMapping("/pull_order_detail")
    @ResponseBody
    public AjaxResult getOrderPullDetail(@RequestBody TaoRequest taoRequest) throws IOException {
        log.info("/**************主动更新tao订单by number****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if (taoRequest.getOrderId() == null || taoRequest.getOrderId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
        }

        var checkResult = taoApiCommon.checkBefore(taoRequest.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }
        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();


//        ApiResultVo<TaoOrderResponse> resultVo = OrderApiHelper.pullOrderDetail(taoRequest.getOrderId(), url, appKey, appSecret, sessionKey);
        ApiResultVo<TaoOrderDetailResponse> resultVo = TaoOrderApiHelper.pullOrderDetail(taoRequest.getOrderId().toString(), appKey, appSecret, sessionKey);

        if (resultVo.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
            TaoOrder taoOrder = new TaoOrder();
            BeanUtils.copyProperties(resultVo.getData(),taoOrder);
//            List<TaoOrderItem> orderItems = new ArrayList<>();
//            if(resultVo.getData().getItems()!=null && resultVo.getData().getItems().size()>0){
//                for (var item : resultVo.getData().getItems()) {
//                    TaoOrderItem orderItem = new TaoOrderItem();
//                    BeanUtils.copyProperties(item,orderItem);
//                    orderItems.add(orderItem);
//                }
//            }
//            taoOrder.setItems(orderItems);
            var result = orderService.saveOrder(taoRequest.getShopId(), taoOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新tao订单：开始更新数据库：" + result.getData() + "存在、更新****************/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.ORDER_MESSAGE,resultVo.getData().getTid()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新tao订单：开始更新数据库：" + resultVo.getData() + "不存在、新增****************/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO,MqType.ORDER_MESSAGE,resultVo.getData().getTid()));
            }
//            var result = orderService.updateOrder(resultVo.getData());
//            if (result.getCode() == ResultVoEnum.NotFound.getIndex()) {
//                //已经存在
//                log.info("/**************主动更新tao订单：开始更新数据库：" + resultVo.getData().getId() + "存在、更新****************/");
//            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.ORDER_MESSAGE,resultVo.getData().getTid()));
//                log.info("/**************主动更新tao订单：开始更新数据库：" + resultVo.getData().getId() + "不存在、新增****************/");
//            } else {
//                log.info("/**************主动更新tao订单：开始更新数据库：" + resultVo.getData().getId() + "报错****************/");
//            }
            return AjaxResult.success();
        } else {
            return AjaxResult.error(resultVo.getCode(), resultVo.getMsg());
        }
    }
}
