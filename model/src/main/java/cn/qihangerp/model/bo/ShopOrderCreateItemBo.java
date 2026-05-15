package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopOrderCreateItemBo {
    /** skuId */
    private String id;
    private String shopGoodsId;//店铺商品id
    private Long erpGoodsSkuId;
    private Long erpGoodsId;
    /**
     * 条形码
     */
    private String barcode;

    private BigDecimal itemAmount;//子订单金额

    private Integer quantity;//数量
    private Integer isGift;//是否是礼品
}
