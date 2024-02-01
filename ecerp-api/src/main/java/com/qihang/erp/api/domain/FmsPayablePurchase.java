package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 财务管理-应付款-采购货款对象 fms_payable_purchase
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsPayablePurchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 应付金额 */
    @Excel(name = "应付金额")
    private BigDecimal amount;

    /** 应付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "应付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNo;

    /** 采购单号 */
    @Excel(name = "采购单号")
    private String purchaseOrderNo;

    /** 采购说明 */
    @Excel(name = "采购说明")
    private String purchaseDesc;

    /** 状态（0已生成1已结算) */
    @Excel(name = "状态", readConverterExp = "状态（0已生成1已结算)")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSupplierId(Long supplierId) 
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() 
    {
        return supplierId;
    }
    public void setSupplierName(String supplierName) 
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName() 
    {
        return supplierName;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setInvoiceNo(String invoiceNo) 
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() 
    {
        return invoiceNo;
    }
    public void setPurchaseOrderNo(String purchaseOrderNo) 
    {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseOrderNo() 
    {
        return purchaseOrderNo;
    }
    public void setPurchaseDesc(String purchaseDesc) 
    {
        this.purchaseDesc = purchaseDesc;
    }

    public String getPurchaseDesc() 
    {
        return purchaseDesc;
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
