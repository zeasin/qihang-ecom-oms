package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品分类属性值对象 erp_goods_category_attribute_value
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ErpGoodsCategoryAttributeValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键，属性值id */
    private Long id;

    /** 属性id */
    @Excel(name = "属性id")
    private Long categoryAttributeId;

    /** 属性值文本 */
    @Excel(name = "属性值文本")
    private String value;

    /** 生成SKU的编码 */
    @Excel(name = "生成SKU的编码")
    private String skuCode;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Long isDelete;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryAttributeId(Long categoryAttributeId) 
    {
        this.categoryAttributeId = categoryAttributeId;
    }

    public Long getCategoryAttributeId() 
    {
        return categoryAttributeId;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setSkuCode(String skuCode) 
    {
        this.skuCode = skuCode;
    }

    public String getSkuCode() 
    {
        return skuCode;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }

}
