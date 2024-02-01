package com.qihang.erp.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 退换货对象 erp_order_returned
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class ErpOrderReturned extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 退货单号 */
    @Excel(name = "退货单号")
    private String returnedNum;

    /** 退货类型（1退货2换货） */
    @Excel(name = "退货类型", readConverterExp = "1=退货2换货")
    private Long returnedType;

    /** 源订单号 */
    @Excel(name = "源订单号")
    private String orderNum;

    /** 店铺id */
    @Excel(name = "店铺id")
    private Long shopId;

    /** 店铺类型 */
    @Excel(name = "店铺类型")
    private Long shopType;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 子订单id */
    @Excel(name = "子订单id")
    private Long orderItemId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 规格id */
    @Excel(name = "规格id")
    private Long specId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 规格编码 */
    @Excel(name = "规格编码")
    private String specNum;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品规格 */
    @Excel(name = "商品规格")
    private String goodsSpec;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String goodsImage;

    /** 退货数量 */
    @Excel(name = "退货数量")
    private Long quantity;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsCode;

    /** 收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 发货人 */
    @Excel(name = "发货人")
    private String contactPerson;

    /** 发货人手机号 */
    @Excel(name = "发货人手机号")
    private String mobile;

    /** 发货地址 */
    @Excel(name = "发货地址")
    private String address;

    /** 状态（0待发货1待收货2已收货3作废） */
    @Excel(name = "状态", readConverterExp = "0=待发货1待收货2已收货3作废")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReturnedNum(String returnedNum) 
    {
        this.returnedNum = returnedNum;
    }

    public String getReturnedNum() 
    {
        return returnedNum;
    }
    public void setReturnedType(Long returnedType) 
    {
        this.returnedType = returnedType;
    }

    public Long getReturnedType() 
    {
        return returnedType;
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
    public void setShopType(Long shopType) 
    {
        this.shopType = shopType;
    }

    public Long getShopType() 
    {
        return shopType;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setGoodsId(Long goodsId)
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setGoodsSpec(String goodsSpec) 
    {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsSpec() 
    {
        return goodsSpec;
    }
    public void setGoodsImage(String goodsImage) 
    {
        this.goodsImage = goodsImage;
    }

    public String getGoodsImage() 
    {
        return goodsImage;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setLogisticsCompany(String logisticsCompany) 
    {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsCompany() 
    {
        return logisticsCompany;
    }
    public void setLogisticsCode(String logisticsCode) 
    {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCode() 
    {
        return logisticsCode;
    }
    public void setReceiveTime(Date receiveTime) 
    {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() 
    {
        return receiveTime;
    }
    public void setContactPerson(String contactPerson) 
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() 
    {
        return contactPerson;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

}
