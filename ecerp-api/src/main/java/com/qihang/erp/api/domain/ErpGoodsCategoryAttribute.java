package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品分类属性对象 erp_goods_category_attribute
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ErpGoodsCategoryAttribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 分类id（0为公共） */
    @Excel(name = "分类id", readConverterExp = "0=为公共")
    private Long categoryId;

    /** 类型：0属性1规格 */
    @Excel(name = "类型：0属性1规格")
    private Long type;

    /** '属性名' */
    @Excel(name = "'属性名'")
    private String title;

    /** 固定值color颜色size尺码style款式 */
    @Excel(name = "固定值color颜色size尺码style款式")
    private String code;

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
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

}
