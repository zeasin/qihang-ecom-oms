package com.qihang.erp.api.domain.bo;

import java.util.Date;

public class StockingAddVo {
    private String stockOutNum;
    private Date createTime;
    private String createBy;
    private Long[] ids;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getStockOutNum() {
        return stockOutNum;
    }

    public void setStockOutNum(String stockOutNum) {
        this.stockOutNum = stockOutNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
}
