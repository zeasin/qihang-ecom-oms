package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class StockOutItemRequest {
    private Long entryItemId;
    private Long entryId;
    private Long skuId;
    private Long inventoryBatchId;
    private Integer outQty;
//    private Integer originalQuantity;
//    private Integer outQuantity;

}
