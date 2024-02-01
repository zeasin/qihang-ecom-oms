package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.qihang.erp.api.domain.bo.GoodsSpecAddBo;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品管理对象 erp_goods
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class Goods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 商品图片地址 */
    @Excel(name = "商品图片地址")
    private String image;

    /** 商品编号 */
    @Excel(name = "商品编号")
    private String number;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String unitName;

    /** 商品分类ID */
    @Excel(name = "商品分类ID")
    private Long categoryId;

    /** 条码 */
    @Excel(name = "条码")
    private String barCode;

    /** 状态1销售中2已下架 */
    @Excel(name = "状态1销售中2已下架")
    private Integer status;

    /** 衣长/裙长/裤长 */
    @Excel(name = "衣长/裙长/裤长")
    private Long length;

    /** 高度/袖长 */
    @Excel(name = "高度/袖长")
    private Long height;

    /** 宽度/胸阔(围) */
    @Excel(name = "宽度/胸阔(围)")
    private Long width;

    /** 肩阔 */
    @Excel(name = "肩阔")
    private Long width1;

    /** 腰阔 */
    @Excel(name = "腰阔")
    private Long width2;

    /** 臀阔 */
    @Excel(name = "臀阔")
    private Long width3;

    /** 重量 */
    @Excel(name = "重量")
    private Long weight;

    /** 0启用   1禁用 */
    @Excel(name = "0启用   1禁用")
    private Integer disable;

    /** 保质期 */
    @Excel(name = "保质期")
    private String period;

    /** 预计采购价格 */
    @Excel(name = "预计采购价格")
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

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long supplierId;

    /** 品牌id */
    @Excel(name = "品牌id")
    private Long brandId;

    /** 属性1：季节 */
    @Excel(name = "属性1：季节")
    private String attr1;

    /** 属性2：分类 */
    @Excel(name = "属性2：分类")
    private String attr2;

    /** 属性3：风格 */
    @Excel(name = "属性3：风格")
    private String attr3;

    /** 属性4：年份 */
    @Excel(name = "属性4：年份")
    private String attr4;

    /** 属性5：面料 */
    @Excel(name = "属性5：面料")
    private String attr5;

    /** 外链url */
    @Excel(name = "外链url")
    private String linkUrl;

    /** 最低库存（预警） */
    @Excel(name = "最低库存", readConverterExp = "预=警")
    private Long lowQty;

    /** 最高库存（预警） */
    @Excel(name = "最高库存", readConverterExp = "预=警")
    private Long highQty;

    private Long[] colorValues;
    private Map<Long,String> colorImages;
//    private Map<Long,String> colorNames;
    private Long[] sizeValues;
    private Long[] styleValues;
    private List<GoodsSpecAddBo> specList;

    public Map<Long, String> getColorImages() {
        return colorImages;
    }

    public void setColorImages(Map<Long, String> colorImages) {
        this.colorImages = colorImages;
    }

    public Long[] getColorValues() {
        return colorValues;
    }

    public void setColorValues(Long[] colorValues) {
        this.colorValues = colorValues;
    }

    public Long[] getSizeValues() {
        return sizeValues;
    }

    public void setSizeValues(Long[] sizeValues) {
        this.sizeValues = sizeValues;
    }

    public Long[] getStyleValues() {
        return styleValues;
    }

    public void setStyleValues(Long[] styleValues) {
        this.styleValues = styleValues;
    }

    public List<GoodsSpecAddBo> getSpecList() {
        return specList;
    }

    public void setSpecList(List<GoodsSpecAddBo> specList) {
        this.specList = specList;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }
    public void setUnitName(String unitName) 
    {
        this.unitName = unitName;
    }

    public String getUnitName() 
    {
        return unitName;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setBarCode(String barCode) 
    {
        this.barCode = barCode;
    }

    public String getBarCode() 
    {
        return barCode;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setLength(Long length) 
    {
        this.length = length;
    }

    public Long getLength() 
    {
        return length;
    }
    public void setHeight(Long height) 
    {
        this.height = height;
    }

    public Long getHeight() 
    {
        return height;
    }
    public void setWidth(Long width) 
    {
        this.width = width;
    }

    public Long getWidth() 
    {
        return width;
    }
    public void setWidth1(Long width1) 
    {
        this.width1 = width1;
    }

    public Long getWidth1() 
    {
        return width1;
    }
    public void setWidth2(Long width2) 
    {
        this.width2 = width2;
    }

    public Long getWidth2() 
    {
        return width2;
    }
    public void setWidth3(Long width3) 
    {
        this.width3 = width3;
    }

    public Long getWidth3() 
    {
        return width3;
    }
    public void setWeight(Long weight) 
    {
        this.weight = weight;
    }

    public Long getWeight() 
    {
        return weight;
    }
    public void setDisable(Integer disable) 
    {
        this.disable = disable;
    }

    public Integer getDisable() 
    {
        return disable;
    }
    public void setPeriod(String period) 
    {
        this.period = period;
    }

    public String getPeriod() 
    {
        return period;
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
    public void setSupplierId(Long supplierId) 
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() 
    {
        return supplierId;
    }
    public void setBrandId(Long brandId) 
    {
        this.brandId = brandId;
    }

    public Long getBrandId() 
    {
        return brandId;
    }
    public void setAttr1(String attr1) 
    {
        this.attr1 = attr1;
    }

    public String getAttr1() 
    {
        return attr1;
    }
    public void setAttr2(String attr2) 
    {
        this.attr2 = attr2;
    }

    public String getAttr2() 
    {
        return attr2;
    }
    public void setAttr3(String attr3) 
    {
        this.attr3 = attr3;
    }

    public String getAttr3() 
    {
        return attr3;
    }
    public void setAttr4(String attr4) 
    {
        this.attr4 = attr4;
    }

    public String getAttr4() 
    {
        return attr4;
    }
    public void setAttr5(String attr5) 
    {
        this.attr5 = attr5;
    }

    public String getAttr5() 
    {
        return attr5;
    }
    public void setLinkUrl(String linkUrl) 
    {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrl() 
    {
        return linkUrl;
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

}
