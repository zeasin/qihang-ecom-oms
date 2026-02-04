package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class RefundProcessingBo {
    private Long refundId;
    private String refundNum;
    private String orderNum;
    private Integer hasGoodsSend;//订单是否发货 0-未发货 1-已发货
    private Integer type;//处理方式 0-无需处理 10-退货 20-换货 30-维修 80-补发 99-订单拦截
    private Integer hasGoodsReturn;//货物是否需要退回 0-无需退回 1-需要退回

    private String returnLogisticsCode;//退回物流单号
    private String reissueLogisticsCode;
    private String remark;//备注
    private String receiverName;//
    private String receiverTel;
    private String receiverAddress;
}
