package com.qihang.erp.api.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购订单对象 scm_purchase_order
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class PurchaseOrderAddBo extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 供应商id */
    private Long contactId;

    /** 订单编号 */
    private String orderNo;

    /** 订单日期 */
    private Date orderDate;


    /** 订单总金额 */
    private BigDecimal orderAmount;

    private List<PurchaseOrderAddItemBo> goodsList;

    public List<PurchaseOrderAddItemBo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PurchaseOrderAddItemBo> goodsList) {
        this.goodsList = goodsList;
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
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }

}
