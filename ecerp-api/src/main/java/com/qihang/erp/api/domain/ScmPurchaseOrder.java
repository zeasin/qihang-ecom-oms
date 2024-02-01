package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 采购订单对象 scm_purchase_order
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ScmPurchaseOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long contactId;
    @TableField(exist = false)
    private String supplier;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 订单创建时间 */
    @Excel(name = "订单创建时间")
    private Long orderTime;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal orderAmount;
    private BigDecimal shipAmount;

    /** 订单状态 0待审核1已审核101供应商已确认102供应商已发货200已入库 */
    @Excel(name = "订单状态 0待审核1已审核101供应商已确认102供应商已发货200已入库")
    private Integer status;

    /** 采购单审核人 */
    @Excel(name = "采购单审核人")
    private String auditUser;

    /** 审核时间 */
    @Excel(name = "审核时间")
    private Long auditTime;

    /** 供应商确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "供应商确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date supplierConfirmTime;

    /** 供应商发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "供应商发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date supplierDeliveryTime;

    /** 收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receivedTime;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date stockInTime;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setContactId(Long contactId) 
    {
        this.contactId = contactId;
    }

    public Long getContactId() 
    {
        return contactId;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setOrderDate(Date orderDate) 
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() 
    {
        return orderDate;
    }
    public void setOrderTime(Long orderTime) 
    {
        this.orderTime = orderTime;
    }

    public Long getOrderTime() 
    {
        return orderTime;
    }
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setAuditUser(String auditUser) 
    {
        this.auditUser = auditUser;
    }

    public String getAuditUser() 
    {
        return auditUser;
    }
    public void setAuditTime(Long auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Long getAuditTime() 
    {
        return auditTime;
    }
    public void setSupplierConfirmTime(Date supplierConfirmTime) 
    {
        this.supplierConfirmTime = supplierConfirmTime;
    }

    public Date getSupplierConfirmTime() 
    {
        return supplierConfirmTime;
    }
    public void setSupplierDeliveryTime(Date supplierDeliveryTime) 
    {
        this.supplierDeliveryTime = supplierDeliveryTime;
    }

    public Date getSupplierDeliveryTime() 
    {
        return supplierDeliveryTime;
    }
    public void setReceivedTime(Date receivedTime) 
    {
        this.receivedTime = receivedTime;
    }

    public Date getReceivedTime() 
    {
        return receivedTime;
    }
    public void setStockInTime(Date stockInTime) 
    {
        this.stockInTime = stockInTime;
    }

    public Date getStockInTime() 
    {
        return stockInTime;
    }

}
