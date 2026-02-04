package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class WeiOrderConfirmBo {
    private Long orderId;
    /**
     * 收货人的姓名
     */
    private String receiver;

    /**
     * 收货人的手机号码
     */
    private String mobile;

    private String province;
    private String city;
    private String town;
    /**
     * 收货人的详细地址
     */
    private String address;

    /**
     * 发货类型 0仓库发货1供应商代发
     */
//    private Integer shipType;

}
