package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 库存存货报明细对象 fms_inventory_report_detail
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsInventoryReportDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** Report外键ID */
    @Excel(name = "Report外键ID")
    private Long reportId;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 商品规格ID */
    @Excel(name = "商品规格ID")
    private Long specId;

    /** 总数量 */
    @Excel(name = "总数量")
    private Long total;

    /** 总货值 */
    @Excel(name = "总货值")
    private BigDecimal amount;

    /** 库存分布 */
    @Excel(name = "库存分布")
    private String inventoryDist;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReportId(Long reportId) 
    {
        this.reportId = reportId;
    }

    public Long getReportId() 
    {
        return reportId;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setTotal(Long total) 
    {
        this.total = total;
    }

    public Long getTotal() 
    {
        return total;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setInventoryDist(String inventoryDist) 
    {
        this.inventoryDist = inventoryDist;
    }

    public String getInventoryDist() 
    {
        return inventoryDist;
    }

}
