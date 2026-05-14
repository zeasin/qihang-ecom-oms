package cn.qihangerp.model.query;

import lombok.Data;

@Data
public class MerchantQuery {
    private String name;
    private String number;
    private String usci;
    private String mobile;
    private Long merchantId;
    private Long shopId;
    private Integer status;
}
