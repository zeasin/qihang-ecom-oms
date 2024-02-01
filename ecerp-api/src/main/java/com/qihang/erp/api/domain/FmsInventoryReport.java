package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 库存存货报对象 fms_inventory_report
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsInventoryReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 库存总数量 */
    @Excel(name = "库存总数量")
    private Long total;

    /** 商品总数 */
    @Excel(name = "商品总数")
    private Long goodsCount;

    /** SKU总数 */
    @Excel(name = "SKU总数")
    private Long skuCount;

    /** 总货值 */
    @Excel(name = "总货值")
    private BigDecimal amount;

    /** 库存存货报明细信息 */
    private List<FmsInventoryReportDetail> fmsInventoryReportDetailList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setTotal(Long total) 
    {
        this.total = total;
    }

    public Long getTotal() 
    {
        return total;
    }
    public void setGoodsCount(Long goodsCount) 
    {
        this.goodsCount = goodsCount;
    }

    public Long getGoodsCount() 
    {
        return goodsCount;
    }
    public void setSkuCount(Long skuCount) 
    {
        this.skuCount = skuCount;
    }

    public Long getSkuCount() 
    {
        return skuCount;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public List<FmsInventoryReportDetail> getFmsInventoryReportDetailList()
    {
        return fmsInventoryReportDetailList;
    }

    public void setFmsInventoryReportDetailList(List<FmsInventoryReportDetail> fmsInventoryReportDetailList)
    {
        this.fmsInventoryReportDetailList = fmsInventoryReportDetailList;
    }

}
