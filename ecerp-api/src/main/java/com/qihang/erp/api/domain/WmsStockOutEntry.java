package com.qihang.erp.api.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 出库单对象 wms_stock_out_entry
 * 
 * @author qihang
 * @date 2024-01-10
 */
public class WmsStockOutEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 出库单编号 */
    @Excel(name = "出库单编号")
    private String stockOutNum;

    /** 来源单据号 */
    @Excel(name = "来源单据号")
    private String sourceNo;

    /** 来源单据Id */
    @Excel(name = "来源单据Id")
    private Long sourceId;

    /** 出库类型1订单拣货出库2采购退货出库3盘点出库4报损出库 */
    @Excel(name = "出库类型1订单拣货出库2采购退货出库3盘点出库4报损出库")
    private Long stockOutType;

    /** 状态：0待拣货1拣货中2拣货完成3已出库 */
    @Excel(name = "状态：0待拣货1拣货中2拣货完成3已出库")
    private Long status;

    /** 打印状态：是否打印1已打印0未打印 */
    @Excel(name = "打印状态：是否打印1已打印0未打印")
    private Long printStatus;

    /** 打印时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打印时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date printTime;

    /** 完成出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 出库操作人userid */
    @Excel(name = "出库操作人userid")
    private Long stockOutOperatorId;

    /** 出库操作人 */
    @Excel(name = "出库操作人")
    private String stockOutOperatorName;

    /** 出库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date stockOutTime;

    /** 是否删除0未删除1已删除 */
    @Excel(name = "是否删除0未删除1已删除")
    private Long isDelete;

    /** 商品数 */
    @Excel(name = "商品数")
    private Integer goodsUnit;

    /** 商品规格数 */
    @Excel(name = "商品规格数")
    private Integer specUnit;

    /** 总件数 */
    @Excel(name = "总件数")
    private Integer specUnitTotal;

    /** 出库单明细信息 */
    private List<WmsStockOutEntryItem> wmsStockOutEntryItemList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStockOutNum(String stockOutNum) 
    {
        this.stockOutNum = stockOutNum;
    }

    public String getStockOutNum() 
    {
        return stockOutNum;
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
    public void setStockOutType(Long stockOutType) 
    {
        this.stockOutType = stockOutType;
    }

    public Long getStockOutType() 
    {
        return stockOutType;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setPrintStatus(Long printStatus) 
    {
        this.printStatus = printStatus;
    }

    public Long getPrintStatus() 
    {
        return printStatus;
    }
    public void setPrintTime(Date printTime) 
    {
        this.printTime = printTime;
    }

    public Date getPrintTime() 
    {
        return printTime;
    }
    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }
    public void setStockOutOperatorId(Long stockOutOperatorId) 
    {
        this.stockOutOperatorId = stockOutOperatorId;
    }

    public Long getStockOutOperatorId() 
    {
        return stockOutOperatorId;
    }
    public void setStockOutOperatorName(String stockOutOperatorName) 
    {
        this.stockOutOperatorName = stockOutOperatorName;
    }

    public String getStockOutOperatorName() 
    {
        return stockOutOperatorName;
    }
    public void setStockOutTime(Date stockOutTime) 
    {
        this.stockOutTime = stockOutTime;
    }

    public Date getStockOutTime() 
    {
        return stockOutTime;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }
    public void setGoodsUnit(Integer goodsUnit)
    {
        this.goodsUnit = goodsUnit;
    }

    public Integer getGoodsUnit()
    {
        return goodsUnit;
    }
    public void setSpecUnit(Integer specUnit)
    {
        this.specUnit = specUnit;
    }

    public Integer getSpecUnit()
    {
        return specUnit;
    }
    public void setSpecUnitTotal(Integer specUnitTotal)
    {
        this.specUnitTotal = specUnitTotal;
    }

    public Integer getSpecUnitTotal()
    {
        return specUnitTotal;
    }

    public List<WmsStockOutEntryItem> getWmsStockOutEntryItemList()
    {
        return wmsStockOutEntryItemList;
    }

    public void setWmsStockOutEntryItemList(List<WmsStockOutEntryItem> wmsStockOutEntryItemList)
    {
        this.wmsStockOutEntryItemList = wmsStockOutEntryItemList;
    }

}
