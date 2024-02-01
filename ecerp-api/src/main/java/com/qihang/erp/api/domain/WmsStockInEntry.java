package com.qihang.erp.api.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 入库单对象 wms_stock_in_entry
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class WmsStockInEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 单据编号 */
    @Excel(name = "单据编号")
    private String no;

    /** 来源单号 */
    @Excel(name = "来源单号")
    private String sourceNo;

    /** 来源单id */
    @Excel(name = "来源单id")
    private Long sourceId;

    /** 来源类型（1采购订单2退货订单） */
    @Excel(name = "来源类型", readConverterExp = "1=采购订单2退货订单")
    private Long sourceType;

    /** 采购订单商品数 */
    @Excel(name = "采购订单商品数")
    private Integer sourceGoodsUnit;

    /** 采购订单总件数 */
    @Excel(name = "采购订单总件数")
    private Long sourceSpecUnitTotal;

    /** 采购订单商品规格数 */
    @Excel(name = "采购订单商品规格数")
    private Integer sourceSpecUnit;

    /** 操作入库人id */
    @Excel(name = "操作入库人id")
    private Long stockInOperatorId;

    /** 操作入库人 */
    @Excel(name = "操作入库人")
    private String stockInOperator;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date stockInTime;
    private Integer status;//状态（0待入库1部分入库2全部入库）

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /** 入库单明细信息 */
    private List<WmsStockInEntryItem> wmsStockInEntryItemList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setSourceNo(String sourceNo) 
    {
        this.sourceNo = sourceNo;
    }

    public String getSourceNo() 
    {
        return sourceNo;
    }
    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }
    public void setSourceType(Long sourceType) 
    {
        this.sourceType = sourceType;
    }

    public Long getSourceType() 
    {
        return sourceType;
    }
    public void setSourceGoodsUnit(Integer sourceGoodsUnit)
    {
        this.sourceGoodsUnit = sourceGoodsUnit;
    }

    public Integer getSourceGoodsUnit()
    {
        return sourceGoodsUnit;
    }
    public void setSourceSpecUnitTotal(Long sourceSpecUnitTotal) 
    {
        this.sourceSpecUnitTotal = sourceSpecUnitTotal;
    }

    public Long getSourceSpecUnitTotal() 
    {
        return sourceSpecUnitTotal;
    }
    public void setSourceSpecUnit(Integer sourceSpecUnit)
    {
        this.sourceSpecUnit = sourceSpecUnit;
    }

    public Integer getSourceSpecUnit()
    {
        return sourceSpecUnit;
    }
    public void setStockInOperatorId(Long stockInOperatorId) 
    {
        this.stockInOperatorId = stockInOperatorId;
    }

    public Long getStockInOperatorId() 
    {
        return stockInOperatorId;
    }
    public void setStockInOperator(String stockInOperator) 
    {
        this.stockInOperator = stockInOperator;
    }

    public String getStockInOperator() 
    {
        return stockInOperator;
    }
    public void setStockInTime(Date stockInTime) 
    {
        this.stockInTime = stockInTime;
    }

    public Date getStockInTime() 
    {
        return stockInTime;
    }

    public List<WmsStockInEntryItem> getWmsStockInEntryItemList()
    {
        return wmsStockInEntryItemList;
    }

    public void setWmsStockInEntryItemList(List<WmsStockInEntryItem> wmsStockInEntryItemList)
    {
        this.wmsStockInEntryItemList = wmsStockInEntryItemList;
    }

}
