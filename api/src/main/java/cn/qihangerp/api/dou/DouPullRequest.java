package cn.qihangerp.api.dou;

import lombok.Data;

@Data
public class DouPullRequest {
    private Long shopId;//店铺Id
    private Integer pullType;//拉取类型：0或不传全量；1更新（用于拉取商品的条件）
    private String orderId;
    private String orderDate;
    private String createTime;
}
