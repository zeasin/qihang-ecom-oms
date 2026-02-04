package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class TaoRefundBo {
    private Integer shopId;
    private String refundId;
    private String tid;
    private String disputeType;
    private String updateTime;
}
