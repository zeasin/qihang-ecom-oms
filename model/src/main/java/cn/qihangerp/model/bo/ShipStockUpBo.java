package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class ShipStockUpBo {
    private String orderNum;
    private String outSkuId;
    private Long shopId;
    private Integer status;
    private Long shipSupplierId;
}
