package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 供应商代发货对象 scm_supplier_agent_shipping
 * 
 * @author qihang
 * @date 2024-01-07
 */
public class ScmSupplierAgentShipping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 店铺ID */
    @Excel(name = "店铺ID")
    private Long shopId;

    /** 店铺平台 */
    @Excel(name = "店铺平台")
    private Long shopType;

    /** 供应商ID */
    @Excel(name = "供应商ID")
    private Long supplierId;


    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNum;

    /** 子订单编号 */
    @Excel(name = "子订单ID")
    private Long erpOrderItemId;
    private Long erpOrderId;

    /** 订单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** erp系统商品id */
    @Excel(name = "erp系统商品id")
    private Long goodsId;

    /** erp系统商品规格id */
    @Excel(name = "erp系统商品规格id")
    private Long specId;

    /** 商品标题 */
    @Excel(name = "商品标题")
    private String goodsTitle;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String goodsImg;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 商品规格 */
    @Excel(name = "商品规格")
    private String goodsSpec;

    /** 商品规格编码 */
    @Excel(name = "商品规格编码")
    private String specNum;

    /** 商品单价 */
    @Excel(name = "商品单价")
    private BigDecimal goodsPrice;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Long quantity;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String shipCompany;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String shipNo;

    /** 运费 */
    @Excel(name = "运费")
    private Float shipCost;

    public Long getErpOrderItemId() {
        return erpOrderItemId;
    }

    public void setErpOrderItemId(Long erpOrderItemId) {
        this.erpOrderItemId = erpOrderItemId;
    }

    public Long getErpOrderId() {
        return erpOrderId;
    }

    public void setErpOrderId(Long erpOrderId) {
        this.erpOrderId = erpOrderId;
    }

    /** 运送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shipTime;

    /** 状态（0未发货1已发货2已结算） */
    @Excel(name = "状态", readConverterExp = "0=未发货1已发货2已结算")
    private Long status;

    /** 子订单金额 */
    @Excel(name = "子订单金额")
    private BigDecimal itemAmount;
    
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setShopType(Long shopType) 
    {
        this.shopType = shopType;
    }

    public Long getShopType() 
    {
        return shopType;
    }
    public void setSupplierId(Long supplierId) 
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() 
    {
        return supplierId;
    }
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
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
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setGoodsTitle(String goodsTitle) 
    {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsTitle() 
    {
        return goodsTitle;
    }
    public void setGoodsImg(String goodsImg) 
    {
        this.goodsImg = goodsImg;
    }

    public String getGoodsImg() 
    {
        return goodsImg;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setGoodsSpec(String goodsSpec) 
    {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsSpec() 
    {
        return goodsSpec;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setGoodsPrice(BigDecimal goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsPrice()
    {
        return goodsPrice;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
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
    public void setShipCost(Float shipCost)
    {
        this.shipCost = shipCost;
    }

    public Float getShipCost()
    {
        return shipCost;
    }
    public void setShipTime(Date shipTime) 
    {
        this.shipTime = shipTime;
    }

    public Date getShipTime() 
    {
        return shipTime;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setItemAmount(BigDecimal itemAmount)
    {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getItemAmount()
    {
        return itemAmount;
    }

}
