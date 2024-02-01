package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 财务管理-应付款-物流费用对象 fms_payable_ship_fee
 * 
 * @author qihang
 * @date 2024-01-28
 */
public class FmsPayableShipFee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 物流公司id */
    @Excel(name = "物流公司id")
    private String logisticsCompanyId;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsNum;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNum;

    /** 店铺id */
    @Excel(name = "店铺id")
    private Long shopId;

    /** 应付金额 */
    @Excel(name = "应付金额")
    private BigDecimal amount;

    /** 应付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "应付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 状态（0已生成1已结算) */
    @Excel(name = "状态", readConverterExp = "状态（0已生成1已结算)")
    private Long status;

    /** 长 */
    @Excel(name = "长")
    private Float length;

    /** 宽 */
    @Excel(name = "宽")
    private Float width;

    /** 高 */
    @Excel(name = "高")
    private Float height;

    /** 重量 */
    @Excel(name = "重量")
    private Float weight;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String receiverName;

    /** 收件人手机号 */
    @Excel(name = "收件人手机号")
    private String receiverPhone;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String town;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticsCompany(String logisticsCompany) 
    {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsCompany() 
    {
        return logisticsCompany;
    }
    public void setLogisticsCompanyId(String logisticsCompanyId) 
    {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public String getLogisticsCompanyId() 
    {
        return logisticsCompanyId;
    }
    public void setLogisticsNum(String logisticsNum) 
    {
        this.logisticsNum = logisticsNum;
    }

    public String getLogisticsNum() 
    {
        return logisticsNum;
    }
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setLength(Float length)
    {
        this.length = length;
    }

    public Float getLength()
    {
        return length;
    }
    public void setWidth(Float width)
    {
        this.width = width;
    }

    public Float getWidth()
    {
        return width;
    }
    public void setHeight(Float height)
    {
        this.height = height;
    }

    public Float getHeight()
    {
        return height;
    }
    public void setWeight(Float weight)
    {
        this.weight = weight;
    }

    public Float getWeight()
    {
        return weight;
    }
    public void setReceiverName(String receiverName) 
    {
        this.receiverName = receiverName;
    }

    public String getReceiverName() 
    {
        return receiverName;
    }
    public void setReceiverPhone(String receiverPhone) 
    {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverPhone() 
    {
        return receiverPhone;
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
    public void setTown(String town) 
    {
        this.town = town;
    }

    public String getTown() 
    {
        return town;
    }

}
