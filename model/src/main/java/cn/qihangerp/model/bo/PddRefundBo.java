package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class PddRefundBo {
    private Integer shopId;
    private String refundId;
    private String orderSn;
    private String afterSalesType;
    private String updateTime;
}
