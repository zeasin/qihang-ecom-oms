package com.qihang.erp.api.domain.bo;

import com.zhijian.common.annotation.Excel;

public class StockOutBo {
    /** entry_item 主键ID */
    private Long entryItemId;

    /** 出库单id */
    private Long entryId;
    /** 商品规格id */
    private Long specId;
    /** 库存详情id**/
    private Long inventoryDetailId;
    private Long outQty;

    private Long operatorId;
    private String operatorName;

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getEntryItemId() {
        return entryItemId;
    }

    public Long getInventoryDetailId() {
        return inventoryDetailId;
    }

    public void setInventoryDetailId(Long inventoryDetailId) {
        this.inventoryDetailId = inventoryDetailId;
    }

    public void setEntryItemId(Long entryItemId) {
        this.entryItemId = entryItemId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }


    public Long getOutQty() {
        return outQty;
    }

    public void setOutQty(Long outQty) {
        this.outQty = outQty;
    }
}
