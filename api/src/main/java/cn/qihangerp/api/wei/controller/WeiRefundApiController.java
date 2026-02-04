package cn.qihangerp.api.wei.controller;


import cn.qihangerp.api.wei.PullRequest;
import cn.qihangerp.api.wei.WeiApiCommon;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.model.entity.OmsWeiRefund;
import cn.qihangerp.module.service.OmsWeiRefundService;

import cn.qihangerp.open.common.ApiResultVo;

import cn.qihangerp.open.wei.WeiRefundApiHelper;
import cn.qihangerp.open.wei.model.AfterSaleOrder;
import cn.qihangerp.security.common.BaseController;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequestMapping("/wei/refund")
@RestController
@AllArgsConstructor
public class WeiRefundApiController extends BaseController {
    private final WeiApiCommon apiCommon;
    private final OmsWeiRefundService refundService;

    @RequestMapping(value = "/pull_list", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest req) throws Exception {
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if(StringUtils.isBlank(req.getCreateTime())) return AjaxResult.error("缺少参数：createTime");
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = apiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != ResultVoEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取最后更新时间
        LocalDateTime startTime = LocalDateTime.parse(req.getCreateTime() + " 00:00:01", formatter);
        LocalDateTime endTime = LocalDateTime.parse(req.getCreateTime() + " 23:59:59", formatter);
        String pullParams = "{startTime:"+startTime.format(formatter)+",endTime:"+endTime.format(formatter)+"}";

        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数

        ApiResultVo<AfterSaleOrder> apiResultVo = WeiRefundApiHelper.pullRefundList(startTime, endTime, accessToken);

        if(apiResultVo.getCode() == 0) {
            // 拉取到了数据 拉取详情
            if(apiResultVo.getList()!=null) {
                for (var refundInfo : apiResultVo.getList()) {

                    OmsWeiRefund refund = new OmsWeiRefund();
                    refund.setOrderId(refundInfo.getOrder_id());
                    refund.setAfterSaleOrderId(refundInfo.getAfter_sale_order_id());
                    refund.setShopId(req.getShopId());
                    refund.setStatus(refundInfo.getStatus());
                    refund.setOpenid(refundInfo.getOpenid());
                    refund.setUnionid(refundInfo.getUnionid());
                    refund.setProductId(refundInfo.getProduct_info().getProduct_id());
                    refund.setSkuId(refundInfo.getProduct_info().getSku_id());
                    refund.setCount(refundInfo.getProduct_info().getCount());
                    refund.setFastRefund(refundInfo.getProduct_info().getFast_refund().toString());
                    refund.setRefundAmount(refundInfo.getRefund_info().getInteger("amount"));
                    refund.setRefundReason(refundInfo.getRefund_info().getInteger("refund_reason"));
                    refund.setReturnWaybillId(refundInfo.getReturn_info().getWaybill_id());
                    refund.setReturnDeliveryName(refundInfo.getReturn_info().getDelivery_name());
                    refund.setReturnDeliveryId(refundInfo.getReturn_info().getDelivery_id());
                    refund.setMerchantUploadInfo(JSONObject.toJSONString(refundInfo.getMerchant_upload_info()));
                    refund.setCreateTime(refundInfo.getCreate_time());
                    refund.setUpdateTime(refundInfo.getUpdate_time());
                    refund.setReason(refundInfo.getReason());
                    refund.setReasonText(refundInfo.getReason_text());
                    refund.setType(refundInfo.getType());
                    refund.setComplaintId(refundInfo.getComplaint_id());
                    refund.setRefundResp(JSONObject.toJSONString(refundInfo.getRefund_resp()));
                    refund.setDetails(JSONObject.toJSONString(refundInfo.getDetails()));

                    var result = refundService.saveRefund(req.getShopId(), refund);
                    if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                        //已经存在
//                        kafkaTemplate.send(MqType.REFUND_MQ, JSONObject.toJSONString(MqMessage.build(EnumShopType.DOU, MqType.REFUND_MESSAGE,refund.getAfterSaleOrderId())));
                        hasExistOrder++;
                    } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
//                        kafkaTemplate.send(MqType.REFUND_MQ, JSONObject.toJSONString(MqMessage.build(EnumShopType.DOU, MqType.REFUND_MESSAGE,refund.getAfterSaleOrderId())));
                        insertSuccess++;
                    } else {
                        totalError++;
                    }

                }
            }

        }

        return AjaxResult.success();
    }
}
