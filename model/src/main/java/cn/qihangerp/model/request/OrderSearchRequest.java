package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class OrderSearchRequest {
    private Integer shopId;
    private Integer shopType;
    private Integer shipper;
    private Integer shipType;

    private String orderNum;
    private String orderStatus;
    private String startTime;
    private String endTime;
    private Integer erpPushStatus;
    private String receiverName;
    private String receiverMobile;
    private String shippingNumber;
}
