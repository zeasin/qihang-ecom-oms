package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 仓库货架对象 wms_stock_location
 * 
 * @author qihang
 * @date 2024-01-09
 */
public class WmsStockLocation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 货架编号 */
    @Excel(name = "货架编号")
    private String number;

    /** 货架名称 */
    @Excel(name = "货架名称")
    private String name;

    /** 上级id */
    @Excel(name = "上级id")
    private Long parentId;

    /** 层级深度1级2级3级 */
    @Excel(name = "层级深度1级2级3级")
    private Long depth;

    /** 一级类目id */
    @Excel(name = "一级类目id")
    private Long parentId1;

    /** 二级类目id */
    @Excel(name = "二级类目id")
    private Long parentId2;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 0正常  1删除 */
    @Excel(name = "0正常  1删除")
    private Long isDelete;

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
    public void setDepth(Long depth) 
    {
        this.depth = depth;
    }

    public Long getDepth() 
    {
        return depth;
    }
    public void setParentId1(Long parentId1) 
    {
        this.parentId1 = parentId1;
    }

    public Long getParentId1() 
    {
        return parentId1;
    }
    public void setParentId2(Long parentId2) 
    {
        this.parentId2 = parentId2;
    }

    public Long getParentId2() 
    {
        return parentId2;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
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
