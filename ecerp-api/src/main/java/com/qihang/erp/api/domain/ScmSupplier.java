package com.qihang.erp.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 scm_supplier
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class ScmSupplier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String name;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String number;

    /** 税率 */
    @Excel(name = "税率")
    private Long taxRate;

    /** 期初应付款 */
    @Excel(name = "期初应付款")
    private Long amount;

    /** 期初预付款 */
    @Excel(name = "期初预付款")
    private Long periodMoney;

    /** 初期往来余额 */
    @Excel(name = "初期往来余额")
    private Long difMoney;

    /** 余额日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "余额日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginDate;

    /** 职位 */
    @Excel(name = "职位")
    private String place;

    /** 联系人 */
    @Excel(name = "联系人")
    private String linkMan;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contact;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区县 */
    @Excel(name = "区县")
    private String county;

    /** 收货地址详情 */
    @Excel(name = "收货地址详情")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String pinYin;

    /** 0启用   1禁用 */
    @Excel(name = "0启用   1禁用")
    private Integer disable;

    /** 0正常 1删除 */
    @Excel(name = "0正常 1删除")
    private Integer isDelete;

    /** 分管采购员 */
    @Excel(name = "分管采购员")
    private String purchaserName;

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
    public void setTaxRate(Long taxRate) 
    {
        this.taxRate = taxRate;
    }

    public Long getTaxRate() 
    {
        return taxRate;
    }
    public void setAmount(Long amount) 
    {
        this.amount = amount;
    }

    public Long getAmount() 
    {
        return amount;
    }
    public void setPeriodMoney(Long periodMoney) 
    {
        this.periodMoney = periodMoney;
    }

    public Long getPeriodMoney() 
    {
        return periodMoney;
    }
    public void setDifMoney(Long difMoney) 
    {
        this.difMoney = difMoney;
    }

    public Long getDifMoney() 
    {
        return difMoney;
    }
    public void setBeginDate(Date beginDate) 
    {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() 
    {
        return beginDate;
    }
    public void setPlace(String place) 
    {
        this.place = place;
    }

    public String getPlace() 
    {
        return place;
    }
    public void setLinkMan(String linkMan) 
    {
        this.linkMan = linkMan;
    }

    public String getLinkMan() 
    {
        return linkMan;
    }
    public void setContact(String contact) 
    {
        this.contact = contact;
    }

    public String getContact() 
    {
        return contact;
    }
    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setCounty(String county) 
    {
        this.county = county;
    }

    public String getCounty() 
    {
        return county;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setPinYin(String pinYin) 
    {
        this.pinYin = pinYin;
    }

    public String getPinYin() 
    {
        return pinYin;
    }
    public void setDisable(Integer disable) 
    {
        this.disable = disable;
    }

    public Integer getDisable() 
    {
        return disable;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }
    public void setPurchaserName(String purchaserName) 
    {
        this.purchaserName = purchaserName;
    }

    public String getPurchaserName() 
    {
        return purchaserName;
    }

}
