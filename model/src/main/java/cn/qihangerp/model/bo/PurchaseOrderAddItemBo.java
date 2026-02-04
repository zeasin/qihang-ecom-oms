package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购订单对象 scm_purchase_order
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Data
public class PurchaseOrderAddItemBo {
    private static final long serialVersionUID = 1L;


    private Long id;
    private String colorImage;
    private String goodsName;
    private BigDecimal purPrice;
    private Long quantity;
    private BigDecimal amount;
    private Long goodsId;
    private String colorValue;
    private String number;
    private String sizeValue;
    private String skuCode;
    private String styleValue;

}
