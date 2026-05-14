package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class ShopGoodsSkuUpdateRequest {
    private Long id;//店铺商品skuId
//    private Long erpGoodsId;
    private Long erpGoodsSkuId;
    private String productTitle;
    private String skuName;
    private String img;
    private String skuId;//外部系统商品skuID
    private Double price;

}
