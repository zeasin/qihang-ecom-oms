package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 小红书订单退款明细对象 s_xhs_refund_item
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class XhsRefundItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 外键 */
    @Excel(name = "外键")
    private Long refundId;

    /** 商品id */
    @Excel(name = "商品id")
    private String itemId;

    /** 商品名 */
    @Excel(name = "商品名")
    private String itemName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String image;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long price;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long boughtCount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long appliedCount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long returnedCount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long refundedCount;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long returnPrice;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String exchangeItemId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String exchangeItemName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String exchangeItemImage;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String skucode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String exchangeSkucode;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRefundId(Long refundId) 
    {
        this.refundId = refundId;
    }

    public Long getRefundId() 
    {
        return refundId;
    }
    public void setItemId(String itemId) 
    {
        this.itemId = itemId;
    }

    public String getItemId() 
    {
        return itemId;
    }
    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }
    public void setBoughtCount(Long boughtCount) 
    {
        this.boughtCount = boughtCount;
    }

    public Long getBoughtCount() 
    {
        return boughtCount;
    }
    public void setAppliedCount(Long appliedCount) 
    {
        this.appliedCount = appliedCount;
    }

    public Long getAppliedCount() 
    {
        return appliedCount;
    }
    public void setReturnedCount(Long returnedCount) 
    {
        this.returnedCount = returnedCount;
    }

    public Long getReturnedCount() 
    {
        return returnedCount;
    }
    public void setRefundedCount(Long refundedCount) 
    {
        this.refundedCount = refundedCount;
    }

    public Long getRefundedCount() 
    {
        return refundedCount;
    }
    public void setReturnPrice(Long returnPrice) 
    {
        this.returnPrice = returnPrice;
    }

    public Long getReturnPrice() 
    {
        return returnPrice;
    }
    public void setExchangeItemId(String exchangeItemId) 
    {
        this.exchangeItemId = exchangeItemId;
    }

    public String getExchangeItemId() 
    {
        return exchangeItemId;
    }
    public void setExchangeItemName(String exchangeItemName) 
    {
        this.exchangeItemName = exchangeItemName;
    }

    public String getExchangeItemName() 
    {
        return exchangeItemName;
    }
    public void setExchangeItemImage(String exchangeItemImage) 
    {
        this.exchangeItemImage = exchangeItemImage;
    }

    public String getExchangeItemImage() 
    {
        return exchangeItemImage;
    }
    public void setSkucode(String skucode) 
    {
        this.skucode = skucode;
    }

    public String getSkucode() 
    {
        return skucode;
    }
    public void setExchangeSkucode(String exchangeSkucode) 
    {
        this.exchangeSkucode = exchangeSkucode;
    }

    public String getExchangeSkucode() 
    {
        return exchangeSkucode;
    }

}
