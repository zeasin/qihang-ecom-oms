package cn.qihangerp.model.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopGoodsSkuQuery implements Serializable {
    private Long id;//店铺商品skuid主键
    private Long goodsId;//商品主键ID
    /**
     * 平台商品ID
     */
    private String productId;
    // 商家商品编码
    private String outerProductId;
    // 平台skuId
    private String skuId;
    private String skuCode;
    // 商家sku编码
    private String outerSkuId;
    private String erpGoodsSkuId;
    // 店铺id
    private Long shopId;
    // 店铺类型
    private Long shopType;
    // 商户id
    private Long merchantId;
    private Integer hasLink;//是否关联 0 未关联 1已关联
    private Integer status;//状态

}
