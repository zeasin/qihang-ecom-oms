package cn.qihangerp.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsSkuInventoryVo {
//    private String batchId;
    private Long skuId;
    private Long goodsId;

    /** 商品名称 */
    private String goodsName;
    private String skuName;
    private String goodsNum;
    private String goodsImg;
    private BigDecimal purPrice;

//    /** 商品编号 */
    private String skuCode;
//    private String colorValue;
//    /** 商品图片地址 */
//    private String colorImage;
//    private String sizeValue;
//    private String styleValue;
//
//    private String batchNum;
//    private Long currentQty;
//    private String warehouseName;
//    private String positionNum;
//    private Long positionId;
    private Integer quantity;//出库数量
}
