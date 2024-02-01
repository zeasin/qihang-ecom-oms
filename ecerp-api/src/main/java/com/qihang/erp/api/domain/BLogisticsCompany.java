package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 物流公司对象 b_logistics_company
 * 
 * @author qihang
 * @date 2024-01-12
 */
public class BLogisticsCompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 快递公司 */
    @Excel(name = "快递公司")
    private String name;

    /** 快递公司编码 */
    @Excel(name = "快递公司编码")
    private String number;

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
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }

}
