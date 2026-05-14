package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class ShopGoodsSkuInsertRequest {

    private Long shopGoodsId;//店铺商品id
    private Long erpGoodsSkuId;//商品库商品SkuId
    private String skuId;//平台商品skuID
    private String title;
    private String img;
    private String skuName;
    private Double price;
}
