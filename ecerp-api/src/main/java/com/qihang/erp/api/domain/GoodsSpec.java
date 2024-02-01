package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品规格对象 erp_goods_spec
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class GoodsSpec extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 规格名 */
    @Excel(name = "规格名")
    private String specName;

    /** 规格编码 */
    @Excel(name = "规格编码")
    private String specNum;

    /** 颜色id */
    @Excel(name = "颜色id")
    private Long colorId;

    /** 颜色值 */
    @Excel(name = "颜色值")
    private String colorValue;

    /** 颜色图片 */
    @Excel(name = "颜色图片")
    private String colorImage;

    /** 尺码id */
    @Excel(name = "尺码id")
    private Long sizeId;

    /** 尺码值 */
    @Excel(name = "尺码值")
    private String sizeValue;

    /** 款式id */
    @Excel(name = "款式id")
    private Long styleId;

    /** 款式值 */
    @Excel(name = "款式值")
    private String styleValue;

    /** 库存条形码 */
    @Excel(name = "库存条形码")
    private String barCode;

    /** 预计采购价 */
    @Excel(name = "预计采购价")
    private BigDecimal purPrice;

    /** 建议批发价 */
    @Excel(name = "建议批发价")
    private BigDecimal wholePrice;

    /** 建议零售价 */
    @Excel(name = "建议零售价")
    private BigDecimal retailPrice;

    /** 单位成本 */
    @Excel(name = "单位成本")
    private BigDecimal unitCost;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 最低库存（预警） */
    @Excel(name = "最低库存", readConverterExp = "预=警")
    private Long lowQty;

    /** 最高库存（预警） */
    @Excel(name = "最高库存", readConverterExp = "预=警")
    private Long highQty;

    /** 0启用   1禁用 */
    @Excel(name = "0启用   1禁用")
    private Integer disable;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setSpecName(String specName) 
    {
        this.specName = specName;
    }

    public String getSpecName() 
    {
        return specName;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setColorId(Long colorId) 
    {
        this.colorId = colorId;
    }

    public Long getColorId() 
    {
        return colorId;
    }
    public void setColorValue(String colorValue) 
    {
        this.colorValue = colorValue;
    }

    public String getColorValue() 
    {
        return colorValue;
    }
    public void setColorImage(String colorImage) 
    {
        this.colorImage = colorImage;
    }

    public String getColorImage() 
    {
        return colorImage;
    }
    public void setSizeId(Long sizeId) 
    {
        this.sizeId = sizeId;
    }

    public Long getSizeId() 
    {
        return sizeId;
    }
    public void setSizeValue(String sizeValue) 
    {
        this.sizeValue = sizeValue;
    }

    public String getSizeValue() 
    {
        return sizeValue;
    }
    public void setStyleId(Long styleId) 
    {
        this.styleId = styleId;
    }

    public Long getStyleId() 
    {
        return styleId;
    }
    public void setStyleValue(String styleValue) 
    {
        this.styleValue = styleValue;
    }

    public String getStyleValue() 
    {
        return styleValue;
    }
    public void setBarCode(String barCode) 
    {
        this.barCode = barCode;
    }

    public String getBarCode() 
    {
        return barCode;
    }
    public void setPurPrice(BigDecimal purPrice) 
    {
        this.purPrice = purPrice;
    }

    public BigDecimal getPurPrice() 
    {
        return purPrice;
    }
    public void setWholePrice(BigDecimal wholePrice) 
    {
        this.wholePrice = wholePrice;
    }

    public BigDecimal getWholePrice() 
    {
        return wholePrice;
    }
    public void setRetailPrice(BigDecimal retailPrice) 
    {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getRetailPrice() 
    {
        return retailPrice;
    }
    public void setUnitCost(BigDecimal unitCost) 
    {
        this.unitCost = unitCost;
    }

    public BigDecimal getUnitCost() 
    {
        return unitCost;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setLowQty(Long lowQty) 
    {
        this.lowQty = lowQty;
    }

    public Long getLowQty() 
    {
        return lowQty;
    }
    public void setHighQty(Long highQty) 
    {
        this.highQty = highQty;
    }

    public Long getHighQty() 
    {
        return highQty;
    }
    public void setDisable(Integer disable) 
    {
        this.disable = disable;
    }

    public Integer getDisable() 
    {
        return disable;
    }

}
