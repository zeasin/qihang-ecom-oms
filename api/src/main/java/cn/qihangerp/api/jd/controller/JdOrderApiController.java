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
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.JdOrder;
import cn.qihangerp.model.entity.JdOrderItem;
import cn.qihangerp.module.service.JdOrderService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;

import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.jd.JdOrderApiHelper;
import cn.qihangerp.open.jd.response.JdOrderDetailResponse;
import cn.qihangerp.open.jd.response.JdOrderListResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/jd/order")
@RestController
@AllArgsConstructor
public class JdOrderApiController {
    private final JdApiCommon jdApiCommon;
//    private final RedisCache redisCache;
    private final MqUtils mqUtils;
    private final JdOrderService orderService;
    private final OShopPullLasttimeService pullLasttimeService;
    private final OShopPullLogsService pullLogsService;

    @RequestMapping(value = "/pull_order_jd", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody JdPullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = jdApiCommon.checkBefore(params.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(params.getShopId(), "ORDER");
        if(lasttime == null){
            endTime = LocalDateTime.now();
            startTime = endTime.minusDays(1);
        }else{
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
        ApiResultVo<JdOrderListResponse> upResult = JdOrderApiHelper.pullOrder(startTime,endTime,appKey,appSecret,accessToken);
        if(upResult.getCode() !=0) return AjaxResult.error(1500,upResult.getMsg());
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        //循环插入订单数据到数据库
        for (var orderInfo : upResult.getList()) {
            JdOrder jdOrder = new JdOrder();
            BeanUtils.copyProperties(orderInfo,jdOrder);
            jdOrder.setFullname(orderInfo.getConsigneeInfo().getFullname());
            jdOrder.setFullAddress(orderInfo.getConsigneeInfo().getFullAddress());
            jdOrder.setTelephone(orderInfo.getConsigneeInfo().getTelephone());
            jdOrder.setMobile(orderInfo.getConsigneeInfo().getMobile());
            jdOrder.setProvince(orderInfo.getConsigneeInfo().getProvince());
            jdOrder.setProvinceId(orderInfo.getConsigneeInfo().getProvinceId());
            jdOrder.setCity(orderInfo.getConsigneeInfo().getCity());
            jdOrder.setCityId(orderInfo.getConsigneeInfo().getCityId());
            jdOrder.setTown(orderInfo.getConsigneeInfo().getTown());
            jdOrder.setTownId(orderInfo.getConsigneeInfo().getTownId());
            List<JdOrderItem> jdOrderItemList = new ArrayList<>();
            if(orderInfo.getItemInfoList()!=null && orderInfo.getItemInfoList().size()>0){
                for (var item:orderInfo.getItemInfoList()){
                    JdOrderItem jdOrderItem = new JdOrderItem();
                    BeanUtils.copyProperties(item,jdOrderItem);
                    jdOrderItem.setOrderId(orderInfo.getOrderId());
                    jdOrderItemList.add(jdOrderItem);
                }
            }
            jdOrder.setItems(jdOrderItemList);
            //插入订单数据
            var result = orderService.saveOrder(params.getShopId(), jdOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExistOrder++;
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,orderInfo.getOrderId()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,orderInfo.getOrderId()));
            } else {
                totalError++;
            }
        }
        if(totalError==0) {
            if (lasttime == null) {
                // 新增
                OShopPullLasttime insertLasttime = new OShopPullLasttime();
                insertLasttime.setShopId(params.getShopId());
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
        String paramsStr = "{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}";
        String resultStr ="{insertSuccess:"+insertSuccess+",hasExistOrder:"+hasExistOrder+",totalError:"+totalError+"}";

        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.JD.getIndex());
        logs.setShopId(params.getShopId());
        logs.setPullType("ORDER");
        logs.setPullWay("主动拉取");
        logs.setPullParams(paramsStr);
        logs.setPullResult(resultStr);
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);
        String msg = "成功{startTime:"+startTime.format(df)+",endTime:"+endTime.format(df)+"}总共找到：" + upResult.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        return AjaxResult.success(msg);
    }

    /**
     * 拉取详情
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_order_detail", method = RequestMethod.POST)
    public AjaxResult pullDetail(@RequestBody JdPullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if (params.getOrderId() == null) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
        }
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = jdApiCommon.checkBefore(params.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        ApiResultVo<JdOrderDetailResponse> upResult = JdOrderApiHelper.pullOrderDetail(Long.parseLong(params.getOrderId()),appKey,appSecret,accessToken);
        if(upResult.getCode() == ResultVoEnum.SUCCESS.getIndex()){
            JdOrder jdOrder = new JdOrder();
            BeanUtils.copyProperties(jdOrder,upResult.getData());
            List<JdOrderItem> jdOrderItemList = new ArrayList<>();
            if(upResult.getData().getItemInfoList()!=null && upResult.getData().getItemInfoList().size()>0){
                for (var item:upResult.getData().getItemInfoList()){
                    JdOrderItem jdOrderItem = new JdOrderItem();
                    BeanUtils.copyProperties(item,jdOrderItem);
                    jdOrderItemList.add(jdOrderItem);
                }
            }
            jdOrder.setItems(jdOrderItemList);
            // 更新Order
            var result = orderService.saveOrder(params.getShopId(), jdOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,upResult.getData().getOrderId()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,upResult.getData().getOrderId()));
            }
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }
    }
}


