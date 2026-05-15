package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PosOrderCreateBo {
    private Long memberId;
    private Long salespersonId;
    private Long recoveryId;
    private BigDecimal recoveryDeductionAmount;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private DiscountInfo discountInfo;
    private List<PosOrderItemBo> items;

    @Data
    public static class DiscountInfo {
        private Long id;
        private Integer type;
        private BigDecimal value;
        private BigDecimal amount;
    }

    @Data
    public static class PosOrderItemBo {
        private Long skuId;
        private String skuCode;
        private String goodsName;
        private String skuName;
        private String barcode;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private BigDecimal goldWeight;
        private BigDecimal silverWeight;
        private BigDecimal laborCost;
    }
}
