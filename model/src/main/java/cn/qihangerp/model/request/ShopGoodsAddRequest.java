package cn.qihangerp.model.request;

import lombok.Data;

import java.util.List;

@Data
public class ShopGoodsAddRequest {
    private Long shopId;
    private String goodsName;
    private String goodsImg;
    private String goodsNum;
    private String productId;//平台商品ID
    private String outerProductId;//外部系统商品ID
    private Double minPrice;

    private List<sku> skuList;

    @Data
    public static class sku{
        private String skuId;//平台ID
        private String outerSkuId;//平台ID
        private String skuName;
        private String skuCode;
        private String img;
        private Integer stockNum;
        private Double price;
    }
}
