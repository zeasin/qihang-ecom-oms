package com.qihang.erp.api.req;

public class TaoRequest {
    private Long shopId;//店铺Id
    private Long orderId;//订单id
    private Integer updType;//更新类型0拉取新订单1更新订单

    public Integer getUpdType() {
        return updType;
    }

    public void setUpdType(Integer updType) {
        this.updType = updType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
