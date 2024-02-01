package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 财务管理-应收款-订单收入对象 fms_receivable_order
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsReceivableOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNum;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 子订单id */
    @Excel(name = "子订单id")
    private Long orderItemId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 规格id */
    @Excel(name = "规格id")
    private Long specId;

    /** 规格名称 */
    @Excel(name = "规格名称")
    private String specName;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 应收金额 */
    @Excel(name = "应收金额")
    private BigDecimal amount;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNo;

    /** 订单说明 */
    @Excel(name = "订单说明")
    private String orderDesc;

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
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setOrderItemId(Long orderItemId) 
    {
        this.orderItemId = orderItemId;
    }

    public Long getOrderItemId() 
    {
        return orderItemId;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setSpecName(String specName) 
    {
        this.specName = specName;
    }

    public String getSpecName() 
    {
        return specName;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setInvoiceNo(String invoiceNo) 
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() 
    {
        return invoiceNo;
    }
    public void setOrderDesc(String orderDesc) 
    {
        this.orderDesc = orderDesc;
    }

    public String getOrderDesc() 
    {
        return orderDesc;
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
