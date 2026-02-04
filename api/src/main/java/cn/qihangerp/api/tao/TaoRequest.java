package cn.qihangerp.api.tao;

import lombok.Data;

@Data
public class TaoRequest {
    private Long shopId;//店铺Id
    private Long orderId;//订单id
    private Long refundId;
    private Integer updType;//更新类型0拉取新订单1更新订单

    private Integer pullType;//拉取类型：0或不传全量；1更新（用于拉取商品的条件）

    private String updateTime;


}
