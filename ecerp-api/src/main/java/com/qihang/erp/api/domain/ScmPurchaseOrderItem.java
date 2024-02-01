package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 采购订单明细对象 scm_purchase_order_item
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ScmPurchaseOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 150501采购 150502退货 */
    @Excel(name = "150501采购 150502退货")
    private String transType;

    /** 购货金额 */
    @Excel(name = "购货金额")
    private Double amount;

    /** 订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long goodsId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 商品规格id */
    @Excel(name = "商品规格id")
    private Long specId;

    /** 商品规格编码 */
    @Excel(name = "商品规格编码")
    private String specNum;

    private String goodsName;
    private String colorValue;
    private String colorImage;
    private String sizeValue;
    private String styleValue;


    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 折扣额 */
    @Excel(name = "折扣额")
    private BigDecimal disAmount;

    /** 折扣率 */
    @Excel(name = "折扣率")
    private BigDecimal disRate;

    /** 数量(采购单据) */
    @Excel(name = "数量(采购单据)")
    private Long quantity;

    /** 已入库数量 */
    @Excel(name = "已入库数量")
    private Long inQty;

    /** 入库的仓库id */
    @Excel(name = "入库的仓库id")
    private Long locationId;

    /** 1删除 0正常 */
    @Excel(name = "1删除 0正常")
    private Integer isDelete;

    /** 状态（同billStatus）0待审核1正常2已作废3已入库 */
    @Excel(name = "状态", readConverterExp = "同=billStatus")
    private Integer status;

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
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setTransType(String transType) 
    {
        this.transType = transType;
    }

    public String getTransType() 
    {
        return transType;
    }
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public Double getAmount()
    {
        return amount;
    }
    public void setOrderDate(Date orderDate) 
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() 
    {
        return orderDate;
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
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setDisAmount(BigDecimal disAmount) 
    {
        this.disAmount = disAmount;
    }

    public BigDecimal getDisAmount() 
    {
        return disAmount;
    }
    public void setDisRate(BigDecimal disRate) 
    {
        this.disRate = disRate;
    }

    public BigDecimal getDisRate() 
    {
        return disRate;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setInQty(Long inQty) 
    {
        this.inQty = inQty;
    }

    public Long getInQty() 
    {
        return inQty;
    }
    public void setLocationId(Long locationId) 
    {
        this.locationId = locationId;
    }

    public Long getLocationId() 
    {
        return locationId;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

}
