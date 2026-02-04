package cn.qihangerp.api.wei;

import lombok.Data;

@Data
public class PullRequest {
    private Long shopId;//店铺Id
    private Long orderId;//订单编号
    private Integer pullType;
    private String orderDate;//下单日期
    private String createTime;
}
