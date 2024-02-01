package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品属性配置对象 erp_goods_attribute_config
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class GoodsAttributeConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 分类id（0为所有共用） */
    @Excel(name = "分类id", readConverterExp = "0=为所有共用")
    private Long categoryId;

    /** 属性名 */
    @Excel(name = "属性名")
    private String name;

    /** 属性值 */
    @Excel(name = "属性值")
    private String value;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

}
