package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 出库单明细对象 wms_stock_out_entry_item
 * 
 * @author qihang
 * @date 2024-01-10
 */
public class WmsStockOutEntryItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 出库单id */
    @Excel(name = "出库单id")
    private Long entryId;

    /** 来源订单id */
    @Excel(name = "来源订单id")
    private Long sourceOrderId;

    /** 来源订单号 */
    @Excel(name = "来源订单号")
    private String sourceOrderNo;

    /** 来源订单itemId出库对应的itemId，如：order_item表id、invoice_info表id */
    @Excel(name = "来源订单itemId出库对应的itemId，如：order_item表id、invoice_info表id")
    private Long sourceOrderItemId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 商品规格id */
    @Excel(name = "商品规格id")
    private Long specId;

    private Integer inventoryDetailId;
    private Long outQty;

    private String colorImage;
    private String colorValue;
    private String sizeValue;
    private String styleValue;

    public Integer getInventoryDetailId() {
        return inventoryDetailId;
    }

    public void setInventoryDetailId(Integer inventoryDetailId) {
        this.inventoryDetailId = inventoryDetailId;
    }

    public Long getOutQty() {
        return outQty;
    }

    public void setOutQty(Long outQty) {
        this.outQty = outQty;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
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

    public String getColorImage() {
        return colorImage;
    }

    public void setColorImage(String colorImage) {
        this.colorImage = colorImage;
    }

    /** 规格编码 */
    @Excel(name = "规格编码")
    private String specNum;

    /** 总数量 */
    @Excel(name = "总数量")
    private Long originalQuantity;

    /** 已出库数量 */
    @Excel(name = "已出库数量")
    private Long outQuantity;

    /** 完成出库时间 */
    @Excel(name = "完成出库时间")
    private Date completeTime;

    /** 完成拣货时间 */
    @Excel(name = "完成拣货时间")
    private Date pickedTime;

    /** 状态：0待拣货1拣货中2拣货完成3已出库 */
    @Excel(name = "状态：0待拣货1拣货中2拣货完成3已出库")
    private Long status;

    private List<ErpGoodsInventoryDetail> inventoryDetails;

    public List<ErpGoodsInventoryDetail> getInventoryDetails() {
        return inventoryDetails;
    }

    public void setInventoryDetails(List<ErpGoodsInventoryDetail> inventoryDetails) {
        this.inventoryDetails = inventoryDetails;
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
    public void setSourceOrderId(Long sourceOrderId) 
    {
        this.sourceOrderId = sourceOrderId;
    }

    public Long getSourceOrderId() 
    {
        return sourceOrderId;
    }
    public void setSourceOrderNo(String sourceOrderNo) 
    {
        this.sourceOrderNo = sourceOrderNo;
    }

    public String getSourceOrderNo() 
    {
        return sourceOrderNo;
    }
    public void setSourceOrderItemId(Long sourceOrderItemId) 
    {
        this.sourceOrderItemId = sourceOrderItemId;
    }

    public Long getSourceOrderItemId() 
    {
        return sourceOrderItemId;
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
    public void setOutQuantity(Long outQuantity) 
    {
        this.outQuantity = outQuantity;
    }

    public Long getOutQuantity() 
    {
        return outQuantity;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getPickedTime() {
        return pickedTime;
    }

    public void setPickedTime(Date pickedTime) {
        this.pickedTime = pickedTime;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }


}
