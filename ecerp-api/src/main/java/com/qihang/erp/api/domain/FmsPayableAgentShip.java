package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 财务管理-应付款-代发账单对象 fms_payable_agent_ship
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsPayableAgentShip extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNum;

    /** 店铺id */
    @Excel(name = "店铺id")
    private Long shopId;

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String shipCompany;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String shipNo;

    /** 应付总金额 */
    @Excel(name = "应付总金额")
    private BigDecimal amount;

    /** 物流费用 */
    @Excel(name = "物流费用")
    private Float shipAmount;

    /** 商品金额 */
    @Excel(name = "商品金额")
    private BigDecimal goodsAmount;

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
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
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
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setShipCompany(String shipCompany) 
    {
        this.shipCompany = shipCompany;
    }

    public String getShipCompany() 
    {
        return shipCompany;
    }
    public void setShipNo(String shipNo) 
    {
        this.shipNo = shipNo;
    }

    public String getShipNo() 
    {
        return shipNo;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setShipAmount(Float shipAmount)
    {
        this.shipAmount = shipAmount;
    }

    public Float getShipAmount()
    {
        return shipAmount;
    }
    public void setGoodsAmount(BigDecimal goodsAmount) 
    {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsAmount() 
    {
        return goodsAmount;
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
