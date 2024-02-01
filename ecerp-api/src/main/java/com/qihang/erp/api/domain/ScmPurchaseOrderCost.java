package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 采购订单费用对象 scm_purchase_order_cost
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class ScmPurchaseOrderCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 采购单ID（主键） */
    private Long id;

    /** 采购单金额 */
    @Excel(name = "采购单金额")
    private BigDecimal orderAmount;

    /** 采购订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 采购订单编号 */
    @Excel(name = "采购订单编号")
    private String orderNo;

    /** 采购订单商品规格数 */
    @Excel(name = "采购订单商品规格数")
    private Integer orderSpecUnit;

    /** 采购订单商品数 */
    @Excel(name = "采购订单商品数")
    private Integer orderGoodsUnit;

    /** 采购订单总件数 */
    @Excel(name = "采购订单总件数")
    private Long orderSpecUnitTotal;

    /** 实际金额 */
    @Excel(name = "实际金额")
    private BigDecimal actualAmount;

    /** 运费 */
    @Excel(name = "运费")
    private BigDecimal freight;

    /** 确认人 */
    @Excel(name = "确认人")
    private String confirmUser;

    /** 确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmTime;

    /** 已支付金额 */
    @Excel(name = "已支付金额")
    private BigDecimal payAmount;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 支付次数 */
    @Excel(name = "支付次数")
    private Long payCount;

    /** 状态（0未支付1已支付） */
    @Excel(name = "状态", readConverterExp = "0=未支付1已支付")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }
    public void setOrderDate(Date orderDate) 
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() 
    {
        return orderDate;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setOrderSpecUnit(Integer orderSpecUnit)
    {
        this.orderSpecUnit = orderSpecUnit;
    }

    public Integer getOrderSpecUnit()
    {
        return orderSpecUnit;
    }
    public void setOrderGoodsUnit(Integer orderGoodsUnit)
    {
        this.orderGoodsUnit = orderGoodsUnit;
    }

    public Integer getOrderGoodsUnit()
    {
        return orderGoodsUnit;
    }
    public void setOrderSpecUnitTotal(Long orderSpecUnitTotal) 
    {
        this.orderSpecUnitTotal = orderSpecUnitTotal;
    }

    public Long getOrderSpecUnitTotal() 
    {
        return orderSpecUnitTotal;
    }
    public void setActualAmount(BigDecimal actualAmount) 
    {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getActualAmount() 
    {
        return actualAmount;
    }
    public void setFreight(BigDecimal freight) 
    {
        this.freight = freight;
    }

    public BigDecimal getFreight() 
    {
        return freight;
    }
    public void setConfirmUser(String confirmUser) 
    {
        this.confirmUser = confirmUser;
    }

    public String getConfirmUser() 
    {
        return confirmUser;
    }
    public void setConfirmTime(Date confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() 
    {
        return confirmTime;
    }
    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setPayCount(Long payCount) 
    {
        this.payCount = payCount;
    }

    public Long getPayCount() 
    {
        return payCount;
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
