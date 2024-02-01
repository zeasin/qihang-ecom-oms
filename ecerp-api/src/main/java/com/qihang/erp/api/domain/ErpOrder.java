package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 订单对象 erp_order
 * 
 * @author qihang
 * @date 2024-01-05
 */
public class ErpOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id，自增 */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNum;

    /** 店铺类型 */
    @Excel(name = "店铺类型")
    private Integer shopType;

    /** 店铺ID */
    @Excel(name = "店铺ID")
    private Integer shopId;

    /** 买家留言信息 */
    @Excel(name = "买家留言信息")
    private String buyerMemo;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部 */
    @Excel(name = "售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部")
    private Integer refundStatus;

    /** 订单状态1：待发货，2：已发货待签收，3：已签收 */
    @Excel(name = "订单状态1：待发货，2：已发货待签收，3：已签收")
    private Integer orderStatus;

    /** 商品金额 */
    @Excel(name = "商品金额")
    private BigDecimal goodsAmount;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discountAmount;

    /** 运费 */
    @Excel(name = "运费")
    private BigDecimal postage;

    /** 支付金额，单位：元，支付金额=商品金额-折扣金额+邮费 */
    @Excel(name = "支付金额，单位：元，支付金额=商品金额-折扣金额+邮费")
    private BigDecimal amount;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String receiverName;

    /** 收件人手机号 */
    @Excel(name = "收件人手机号")
    private String receiverPhone;

    /** 收件人地址 */
    @Excel(name = "收件人地址")
    private String address;

    /** 国家/地区 */
    @Excel(name = "国家/地区")
    private String country;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String town;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 订单确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmTime;

    private Integer shipType;

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shippingTime;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String shippingNumber;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String shippingCompany;
    private BigDecimal shippingCost;
    private String shippingMan;
    private Float length;
    private Float width;
    private Float height;
    private Float weight;

    /** 订单明细信息 */
    private List<ErpOrderItem> itemList;
    private List<ErpOrderItem> erpOrderItemList;

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public List<ErpOrderItem> getErpOrderItemList() {
        return erpOrderItemList;
    }

    public void setErpOrderItemList(List<ErpOrderItem> erpOrderItemList) {
        this.erpOrderItemList = erpOrderItemList;
    }

    public String getShippingMan() {
        return shippingMan;
    }

    public void setShippingMan(String shippingMan) {
        this.shippingMan = shippingMan;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
    }
    public void setShopType(Integer shopType) 
    {
        this.shopType = shopType;
    }

    public Integer getShopType() 
    {
        return shopType;
    }
    public void setShopId(Integer shopId) 
    {
        this.shopId = shopId;
    }

    public Integer getShopId() 
    {
        return shopId;
    }
    public void setBuyerMemo(String buyerMemo) 
    {
        this.buyerMemo = buyerMemo;
    }

    public String getBuyerMemo() 
    {
        return buyerMemo;
    }
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }
    public void setRefundStatus(Integer refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public Integer getRefundStatus() 
    {
        return refundStatus;
    }
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setGoodsAmount(BigDecimal goodsAmount) 
    {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsAmount() 
    {
        return goodsAmount;
    }
    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }
    public void setPostage(BigDecimal postage) 
    {
        this.postage = postage;
    }

    public BigDecimal getPostage() 
    {
        return postage;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
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
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
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
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setConfirmTime(Date confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() 
    {
        return confirmTime;
    }
    public void setShippingTime(Date shippingTime) 
    {
        this.shippingTime = shippingTime;
    }

    public Date getShippingTime() 
    {
        return shippingTime;
    }
    public void setShippingNumber(String shippingNumber) 
    {
        this.shippingNumber = shippingNumber;
    }

    public String getShippingNumber() 
    {
        return shippingNumber;
    }
    public void setShippingCompany(String shippingCompany) 
    {
        this.shippingCompany = shippingCompany;
    }

    public String getShippingCompany() 
    {
        return shippingCompany;
    }

    public List<ErpOrderItem> getItemList()
    {
        return itemList;
    }

    public void setItemList(List<ErpOrderItem> itemList)
    {
        this.itemList = itemList;
    }

}
