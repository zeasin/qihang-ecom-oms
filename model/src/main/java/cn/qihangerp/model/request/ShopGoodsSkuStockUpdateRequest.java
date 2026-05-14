package cn.qihangerp.model.request;

import lombok.Data;

@Data
public class ShopGoodsSkuStockUpdateRequest {
    private Long id;
    private Integer stockNum;
}
