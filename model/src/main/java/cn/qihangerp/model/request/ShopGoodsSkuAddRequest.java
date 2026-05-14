package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class ShopGoodsSkuAddRequest {
    private Long shopId;
    private String goodsName;
    private String image;
    private String skuName;
    private Long goodsSkuId;//商品库skuId
    private Long goodsId;//商品库skuId
    private String skuId;//外部系统商品skuID
    private Double price;

}
