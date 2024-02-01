package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * ${subTable.functionName}对象 s_shop_goods_sku
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class SShopGoodsSku extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 外键id */
    @Excel(name = "外键id")
    private Long shopGoodsId;

    /** 商品id，阿里productID */
    @Excel(name = "商品id，阿里productID")
    private Long goodsId;

    /** 商品skuid，阿里 */
    @Excel(name = "商品skuid，阿里")
    private Long skuId;

    /** specId */
    @Excel(name = "specId")
    private String spec;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long erpGoodsSpecId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long erpGoodsId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String erpGoodsSpecCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long skuQuantity;

    /** 分销基准价。代销场景均使用该价格。无SKU商品查看saleInfo中的consignPrice */
    @Excel(name = "分销基准价。代销场景均使用该价格。无SKU商品查看saleInfo中的consignPrice")
    private Long consignPrice;

    /** sku编码 */
    @Excel(name = "sku编码")
    private String outerId;

    /** spu编码 */
    @Excel(name = "spu编码")
    private String outerGoodsId;

    /** 报价时该规格的单价，国际站注意要点：含有SKU属性的在线批发产品设定具体价格时使用此值，若设置阶梯价格则使用priceRange */
    @Excel(name = "报价时该规格的单价，国际站注意要点：含有SKU属性的在线批发产品设定具体价格时使用此值，若设置阶梯价格则使用priceRange")
    private Long price;

    /** 建议零售价 */
    @Excel(name = "建议零售价")
    private Long retailPrice;

    /** 可销售数量 */
    @Excel(name = "可销售数量")
    private Long amountOnSale;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long isSkuOnsale;

    /** 指定规格的货号,对应ERP系统商品specNumber */
    @Excel(name = "指定规格的货号,对应ERP系统商品specNumber")
    private String cargoNumber;

    /** SKU属性值json */
    @Excel(name = "SKU属性值json")
    private String attributes;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShopGoodsId(Long shopGoodsId) 
    {
        this.shopGoodsId = shopGoodsId;
    }

    public Long getShopGoodsId() 
    {
        return shopGoodsId;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setSkuId(Long skuId) 
    {
        this.skuId = skuId;
    }

    public Long getSkuId() 
    {
        return skuId;
    }
    public void setSpec(String spec) 
    {
        this.spec = spec;
    }

    public String getSpec() 
    {
        return spec;
    }
    public void setErpGoodsSpecId(Long erpGoodsSpecId) 
    {
        this.erpGoodsSpecId = erpGoodsSpecId;
    }

    public Long getErpGoodsSpecId() 
    {
        return erpGoodsSpecId;
    }
    public void setErpGoodsId(Long erpGoodsId) 
    {
        this.erpGoodsId = erpGoodsId;
    }

    public Long getErpGoodsId() 
    {
        return erpGoodsId;
    }
    public void setErpGoodsSpecCode(String erpGoodsSpecCode) 
    {
        this.erpGoodsSpecCode = erpGoodsSpecCode;
    }

    public String getErpGoodsSpecCode() 
    {
        return erpGoodsSpecCode;
    }
    public void setSkuQuantity(Long skuQuantity) 
    {
        this.skuQuantity = skuQuantity;
    }

    public Long getSkuQuantity() 
    {
        return skuQuantity;
    }
    public void setConsignPrice(Long consignPrice) 
    {
        this.consignPrice = consignPrice;
    }

    public Long getConsignPrice() 
    {
        return consignPrice;
    }
    public void setOuterId(String outerId) 
    {
        this.outerId = outerId;
    }

    public String getOuterId() 
    {
        return outerId;
    }
    public void setOuterGoodsId(String outerGoodsId) 
    {
        this.outerGoodsId = outerGoodsId;
    }

    public String getOuterGoodsId() 
    {
        return outerGoodsId;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }
    public void setRetailPrice(Long retailPrice) 
    {
        this.retailPrice = retailPrice;
    }

    public Long getRetailPrice() 
    {
        return retailPrice;
    }
    public void setAmountOnSale(Long amountOnSale) 
    {
        this.amountOnSale = amountOnSale;
    }

    public Long getAmountOnSale() 
    {
        return amountOnSale;
    }
    public void setIsSkuOnsale(Long isSkuOnsale) 
    {
        this.isSkuOnsale = isSkuOnsale;
    }

    public Long getIsSkuOnsale() 
    {
        return isSkuOnsale;
    }
    public void setCargoNumber(String cargoNumber) 
    {
        this.cargoNumber = cargoNumber;
    }

    public String getCargoNumber() 
    {
        return cargoNumber;
    }
    public void setAttributes(String attributes) 
    {
        this.attributes = attributes;
    }

    public String getAttributes() 
    {
        return attributes;
    }

}
