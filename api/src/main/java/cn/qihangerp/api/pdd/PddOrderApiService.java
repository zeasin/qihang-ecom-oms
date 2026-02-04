package cn.qihangerp.api.pdd;

import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.PddOrder;
import cn.qihangerp.model.entity.PddOrderItem;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.module.service.PddOrderService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.pdd.PddOrderApiHelper;
import cn.qihangerp.open.pdd.model.OrderListResultVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PddOrderApiService {
    private final OShopPullLasttimeService pullLasttimeService;
    private final OShopPullLogsService pullLogsService;
    private final PddOrderService orderService;
    private final MqUtils mqUtils;

    /**
     * 拉取订单
     * @param shopId
     * @param appKey
     * @param appSecret
     * @param accessToken
     * @throws Exception
     */
    public void pullOrder(Long shopId, String appKey, String appSecret, String accessToken ) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();
        LocalDateTime startTime = null;
        LocalDateTime  endTime = null;
        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(shopId, "ORDER");
        if(lasttime == null){
            startTime = LocalDateTime.now().minusDays(90);// 从90天前开始
            endTime = startTime.plusHours(24);
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
        String pullParams = "{startTime:"+startTime+",endTime:"+endTime+"}";
        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Long startTimestamp = startTime.toEpochSecond(ZoneOffset.ofHours(8));
        Long endTimestamp = endTime.toEpochSecond(ZoneOffset.ofHours(8));
        log.info("==========自动任务拉取PDD订单,startTime:{},endTime:{}=====",startTimeStr,endTimeStr);

        //获取
        ApiResultVo<OrderListResultVo> upResult = PddOrderApiHelper.pullOrderList(appKey, appSecret, accessToken, startTimestamp.intValue(), endTimestamp.intValue(), 1, 20);

        if(upResult.getCode() !=0 ){
            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(shopId);
            logs.setShopType(EnumShopType.PDD.getIndex());
            logs.setPullType("ORDER");
            logs.setPullWay("自动任务拉取订单");
            logs.setPullParams(pullParams);
            logs.setPullResult(upResult.getMsg());
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - beginTime);
            pullLogsService.save(logs);
            log.error("==========自动任务拉取PDD订单,接口拉取错误：{}",upResult.getMsg());
            return;
        }

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
            var result = orderService.saveOrder(shopId, pddOrder);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                log.info("==========自动任务拉取PDD订单,：开始更新数据库：" + pddOrder.getOrderSn() + "存在、更新============开始通知");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD, MqType.ORDER_MESSAGE,pddOrder.getOrderSn()));
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("==========自动任务拉取PDD订单,：开始更新数据库：" + pddOrder.getOrderSn() + "不存在、新增=============开始通知");
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.PDD,MqType.ORDER_MESSAGE,pddOrder.getOrderSn()));
                insertSuccess++;
            } else {
                log.info("==========自动任务拉取PDD订单,：开始更新数据库：" + pddOrder.getOrderSn() + "报错:{}",result.getMsg());
                totalError++;
            }
        }
        if(totalError == 0) {
            if (lasttime == null) {
                // 新增
                OShopPullLasttime insertLasttime = new OShopPullLasttime();
                insertLasttime.setShopId(shopId);
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
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopType(EnumShopType.PDD.getIndex());
        logs.setShopId(shopId);
        logs.setPullType("ORDER");
        logs.setPullWay("主动拉取订单");
        logs.setPullParams(pullParams);
        logs.setPullResult("{insert:"+insertSuccess+",update:"+hasExistOrder+",fail:"+totalError+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - beginTime);
        pullLogsService.save(logs);

        String msg = "成功{startTime:"+startTime.format(formatter)+",endTime:"+endTime.format(formatter)+"}，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("==========自动任务拉取PDD订单,END：" + msg);
    }
}
