package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.DouApiCommon;
import cn.qihangerp.api.dou.DouPullRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.DouOrder;
import cn.qihangerp.model.entity.DouOrderItem;
import cn.qihangerp.module.service.DouOrderService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.dou.DouOrderApiHelper;
import cn.qihangerp.open.dou.model.order.Order;
import com.alibaba.fastjson2.JSONObject;
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
 * 订单更新
 */
@AllArgsConstructor
@RestController
@RequestMapping("/dou/order")
public class DouOrderApiController {
    private static Logger log = LoggerFactory.getLogger(DouOrderApiController.class);

    private final DouOrderService orderService;
    private final DouApiCommon douApiCommon;
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
    public AjaxResult pullOrder(@RequestBody DouPullRequest req)   {
        log.info("/**************增量拉取dou订单****************/");
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
        Long currTimeMillis = System.currentTimeMillis();

        var checkResult = douApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }

        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        Long douShopId = checkResult.getData().getSellerId();
        String accessToken = checkResult.getData().getAccessToken();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        startTime = LocalDateTime.parse(orderDate + " 00:00:01", formatter);
        endTime = LocalDateTime.parse(orderDate + " 23:59:59", formatter);
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
//        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Long startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
        Long endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));

        String pullParams = "{startTime:"+startTime.format(formatter)+",endTime:"+endTime.format(formatter)+"}";
//        ApiResultVo<Token> token = DouTokenApiHelper.getToken(appKey, appSecret,checkResult.getData().getSellerId());
//
//        if(token.getCode()==0) {
//            accessToken = token.getData().getAccessToken();
//        }else{
//            return AjaxResult.error(token.getMsg());
//        }
        //第一次获取
//        ApiResultVo<DouOrderResponse> resultVo = OrderApiHelper.pullOrderList(appKey,appSecret,douShopId,startTime, endTime);
        ApiResultVo<Order> resultVo = DouOrderApiHelper.pullOrderList(startTimestamp, endTimestamp, 0, 20, appKey, appSecret, accessToken);
        if(resultVo.getCode() !=0 ){
            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(req.getShopId());
            logs.setShopType(EnumShopType.DOU.getIndex());
            logs.setPullType("ORDER");
            logs.setPullWay("主动拉取订单");
            logs.setPullParams(pullParams);
            logs.setPullResult(resultVo.getMsg());
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - currTimeMillis);
            pullLogsService.save(logs);
            return AjaxResult.error(resultVo.getCode(),resultVo.getMsg());
        }


        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数

        //循环插入订单数据到数据库
        for (var gitem : resultVo.getList()) {
            DouOrder douOrder = new DouOrder();
            BeanUtils.copyProperties(gitem, douOrder);
            douOrder.setOrderPhaseList(JSONObject.toJSONString(gitem.getOrderPhaseList()));
            douOrder.setEncryptPostAddress(gitem.getPostAddr().getEncryptDetail());
            douOrder.setProvinceName(gitem.getPostAddr().getProvince()!=null?gitem.getPostAddr().getProvince().getName():"");
            douOrder.setProvinceId(gitem.getPostAddr().getProvince()!=null?gitem.getPostAddr().getProvince().getId():"0");
            douOrder.setCityName(gitem.getPostAddr().getCity()!=null?gitem.getPostAddr().getCity().getName():"0");
            douOrder.setCityId(gitem.getPostAddr().getCity()!=null?gitem.getPostAddr().getCity().getId():"");
            douOrder.setTownName(gitem.getPostAddr().getTown()!=null?gitem.getPostAddr().getTown().getName():"");
            douOrder.setTownId(gitem.getPostAddr().getTown()!=null?gitem.getPostAddr().getTown().getId():"0");
            douOrder.setStreetName(gitem.getPostAddr().getStreet()!=null?gitem.getPostAddr().getStreet().getName():"");
            douOrder.setStreetId(gitem.getPostAddr().getStreet()!=null?gitem.getPostAddr().getStreet().getId():"0");
            douOrder.setMaskPostAddress(gitem.getMaskPostAddr()!=null?gitem.getMaskPostAddr().getDetail():"");
            douOrder.setLogisticsInfo(JSONObject.toJSONString(gitem.getLogisticsInfo()));
            List<DouOrderItem> items = new ArrayList<>();
            if (gitem.getSkuOrderList() != null) {
                for (var i : gitem.getSkuOrderList()) {
                    DouOrderItem item = new DouOrderItem();
                    BeanUtils.copyProperties(i, item);
                    item.setAfterSaleStatus(i.getAfterSaleInfo().getAfterSaleStatus());
                    item.setAfterSaleType(i.getAfterSaleInfo().getAfterSaleType());
                    item.setRefundStatus(i.getAfterSaleInfo().getRefundStatus());
                    item.setSpec(JSONObject.toJSONString(i.getSpec()));
                    items.add(item);
                }
                douOrder.setItems(items);
            }
            //插入订单数据
            var result = orderService.saveOrder(req.getShopId(), douOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("/**************主动更新dou订单：开始更新数据库：" + douOrder.getOrderId() + "存在、更新************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.DOU, MqType.ORDER_MESSAGE,douOrder.getOrderId()));
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新dou订单：开始更新数据库：" + douOrder.getOrderId() + "不存在、新增************开始通知****/");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.DOU,MqType.ORDER_MESSAGE,douOrder.getOrderId()));
                insertSuccess++;
            } else {
                log.info("/**************主动更新dou订单：开始更新数据库：" + douOrder.getOrderId() + "报错****************/");
                totalError++;
            }
        }

//        if(totalError==0) {
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
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.DOU.getIndex());
        logs.setShopId(req.getShopId());
        logs.setPullType("ORDER");
        logs.setPullWay("主动拉取订单");
        logs.setPullParams(pullParams);
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - currTimeMillis);
        pullLogsService.save(logs);

        String msg = "成功{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}总共找到：" + resultVo.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("/**************主动更新DOU订单：END：" + msg + "****************/");
        return AjaxResult.success(msg);
    }


    /**
     * 更新单个订单
     *
     * @param
     * @return
     * @throws
     */
//    @RequestMapping("/pull_order_detail")
//    @ResponseBody
//    public AjaxResult getOrderPullDetail(@RequestBody PullRequest req)  {
//        log.info("/**************主动更新dou订单by number****************/");
//        if (req.getShopId() == null || req.getShopId() <= 0) {
//            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
//        }
//        if (req.getOrderId()==null) {
//            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
//        }
//
//        var checkResult = douApiCommon.checkBefore(req.getShopId());
//        if (checkResult.getCode() != HttpStatus.SUCCESS) {
//            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
//        }
//
//        String appKey = checkResult.getData().getAppKey();
//        String appSecret = checkResult.getData().getAppSecret();
//        Long douShopId = checkResult.getData().getSellerId();
//
//        ApiResultVo<DouOrderResponse> resultVo = OrderApiHelper.pullOrderDetail( appKey, appSecret, douShopId,req.getOrderId());
//        if (resultVo.getCode() == ResultVoEnum.SUCCESS.getIndex() && resultVo.getData()!=null) {
//            DouOrder douOrder = new DouOrder();
//            BeanUtils.copyProperties(resultVo.getData(),douOrder);
//            List<DouOrderItem> items = new ArrayList<>();
//            if(resultVo.getData().getItems()!=null && resultVo.getData().getItems().size()>0){
//                for (var item : resultVo.getData().getItems()) {
//                    DouOrderItem douOrderItem = new DouOrderItem();
//                    BeanUtils.copyProperties(item,douOrderItem);
//                    items.add(douOrderItem);
//                }
//            }
//            douOrder.setItems(items);
//
//            var result = orderService.saveOrder(req.getShopId(), douOrder);
//            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
//                //已经存在
//                log.info("/**************主动更新dou订单：开始更新数据库：" + resultVo.getData().getId() + "存在、更新****************/");
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.DOU, MqType.ORDER_MESSAGE,resultVo.getData().getOrderId()));
//            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
//                log.info("/**************主动更新dou订单：开始更新数据库：" + resultVo.getData().getId() + "不存在、新增****************/");
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.DOU,MqType.ORDER_MESSAGE,resultVo.getData().getOrderId()));
//            }
//            return AjaxResult.success();
//        } else {
//            return AjaxResult.error(resultVo.getCode(), resultVo.getMsg());
//        }
//    }
}
