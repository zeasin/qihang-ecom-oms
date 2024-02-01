package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品分类对象 erp_goods_category
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ErpGoodsCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 分类编码 */
    @Excel(name = "分类编码")
    private String number;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 上架分类id */
    @Excel(name = "上架分类id")
    private Long parentId;

    /** 分类路径 */
    @Excel(name = "分类路径")
    private String path;

    /** 排序值 */
    @Excel(name = "排序值")
    private Long sort;

    /** 图片 */
    @Excel(name = "图片")
    private String image;

    /** 0正常  1删除 */
    @Excel(name = "0正常  1删除")
    private Integer isDelete;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }

}
