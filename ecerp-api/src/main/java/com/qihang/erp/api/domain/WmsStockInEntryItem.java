package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 入库单明细对象 wms_stock_in_entry_item
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class WmsStockInEntryItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 入库单id */
    @Excel(name = "入库单id")
    private Long entryId;

    /** 来源类型（1采购订单2退货订单） */
    @Excel(name = "来源类型", readConverterExp = "1=采购订单2退货订单")
    private Long sourceType;

    /** 来源单id */
    @Excel(name = "来源单id")
    private Long sourceId;

    /** 来源单itemId */
    @Excel(name = "来源单itemId")
    private Long sourceItemId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;
    private String goodsName;

    /** 商品规格id */
    @Excel(name = "商品规格id")
    private Long specId;

    /** 商品规格编码 */
    @Excel(name = "商品规格编码")
    private String specNum;

    private String colorValue;
    private String colorImage;
    private String sizeValue;
    private String styleValue;

    /** 原始数量 */
    @Excel(name = "原始数量")
    private Long originalQuantity;

    /** 入库数量 */
    @Excel(name = "已入库数量")
    private Long inQuantity;
    private Long quantity;
    private Long totalQuantity;

    /** 入库仓位 */
    @Excel(name = "入库仓位")
    private Long locationId;

    /** 入库仓位编码 */
    @Excel(name = "入库仓位编码")
    private String locationNum;
    private Integer status;//状态（0待入库1部分入库2全部入库）

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public String getColorImage() {
        return colorImage;
    }

    public void setColorImage(String colorImage) {
        this.colorImage = colorImage;
    }

    public String getSizeValue() {
        return sizeValue;
    }

    public void setSizeValue(String sizeValue) {
        this.sizeValue = sizeValue;
    }

    public String getStyleValue() {
        return styleValue;
    }

    public void setStyleValue(String styleValue) {
        this.styleValue = styleValue;
    }

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
    public void setSourceType(Long sourceType) 
    {
        this.sourceType = sourceType;
    }

    public Long getSourceType() 
    {
        return sourceType;
    }
    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }
    public void setSourceItemId(Long sourceItemId) 
    {
        this.sourceItemId = sourceItemId;
    }

    public Long getSourceItemId() 
    {
        return sourceItemId;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setOriginalQuantity(Long originalQuantity) 
    {
        this.originalQuantity = originalQuantity;
    }

    public Long getOriginalQuantity() 
    {
        return originalQuantity;
    }
    public void setInQuantity(Long inQuantity) 
    {
        this.inQuantity = inQuantity;
    }

    public Long getInQuantity() 
    {
        return inQuantity;
    }
    public void setLocationId(Long locationId) 
    {
        this.locationId = locationId;
    }

    public Long getLocationId() 
    {
        return locationId;
    }
    public void setLocationNum(String locationNum) 
    {
        this.locationNum = locationNum;
    }

    public String getLocationNum() 
    {
        return locationNum;
    }

}
