package com.qihang.erp.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 出库明细详情对象 wms_stock_out_entry_item_detail
 * 
 * @author qihang
 * @date 2024-01-10
 */
public class WmsStockOutEntryItemDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 出库单ID */
    @Excel(name = "出库单ID")
    private Long entryId;

    /** 出库单ItemID */
    @Excel(name = "出库单ItemID")
    private Long entryItemId;

    /** 库存ID */
    @Excel(name = "库存ID")
    private Long goodsInventoryId;

    /** 库存详情ID */
    @Excel(name = "库存详情ID")
    private Long goodsInventoryDetailId;

    /** 出库数量 */
    @Excel(name = "出库数量")
    private Long quantity;

    /** 出库仓位ID */
    @Excel(name = "出库仓位ID")
    private Long locationId;

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

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEntryId(Long entryId) 
    {
        this.entryId = entryId;
    }

    public Long getEntryId() 
    {
        return entryId;
    }
    public void setEntryItemId(Long entryItemId) 
    {
        this.entryItemId = entryItemId;
    }

    public Long getEntryItemId() 
    {
        return entryItemId;
    }
    public void setGoodsInventoryId(Long goodsInventoryId) 
    {
        this.goodsInventoryId = goodsInventoryId;
    }

    public Long getGoodsInventoryId() 
    {
        return goodsInventoryId;
    }
    public void setGoodsInventoryDetailId(Long goodsInventoryDetailId) 
    {
        this.goodsInventoryDetailId = goodsInventoryDetailId;
    }

    public Long getGoodsInventoryDetailId() 
    {
        return goodsInventoryDetailId;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setLocationId(Long locationId) 
    {
        this.locationId = locationId;
    }

    public Long getLocationId() 
    {
        return locationId;
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

}
