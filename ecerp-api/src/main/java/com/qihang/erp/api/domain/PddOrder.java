package com.qihang.erp.api.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 拼多多订单对象 s_pdd_order
 * 
 * @author qihang
 * @date 2024-01-02
 */
public class PddOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id，自增 */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderSn;

    /** 内部店铺ID */
    @Excel(name = "内部店铺ID")
    private Long shopId;

    /** 订单类型 0-普通订单 ，1- 定金订单 */
    @Excel(name = "订单类型 0-普通订单 ，1- 定金订单")
    private Long tradeType;

    /** 成交状态：0：未成交、1：已成交、2：已取消、 */
    @Excel(name = "成交状态：0：未成交、1：已成交、2：已取消、")
    private Long confirmStatus;

    /** 是否顺丰包邮，1-是 0-否 */
    @Excel(name = "是否顺丰包邮，1-是 0-否")
    private Long freeSf;

    /** 成团状态：0：拼团中、1：已成团、2：团失败 */
    @Excel(name = "成团状态：0：拼团中、1：已成团、2：团失败")
    private Long groupStatus;

    /** 团长免单金额，单位：元 */
    @Excel(name = "团长免单金额，单位：元")
    private Double capitalFreeDiscount;

    /** 商家优惠金额，单位：元 */
    @Excel(name = "商家优惠金额，单位：元")
    private Double sellerDiscount;

    /** 平台优惠金额，单位：元 */
    @Excel(name = "平台优惠金额，单位：元")
    private Double platformDiscount;

    /** 订单的更新时间 */
    @Excel(name = "订单的更新时间")
    private String updatedAt;

    /** 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部 */
    @Excel(name = "售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部")
    private Long refundStatus;

    /** 是否是抽奖订单，1-非抽奖订单，2-抽奖订单 */
    @Excel(name = "是否是抽奖订单，1-非抽奖订单，2-抽奖订单")
    private Long isLuckyFlag;

    /** 订单状态1：待发货，2：已发货待签收，3：已签收 */
    @Excel(name = "订单状态1：待发货，2：已发货待签收，3：已签收")
    private Long orderStatus;

    private Integer shipType;

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    /** 发货时间 */
    @Excel(name = "发货时间")
    private String shippingTime;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String trackingNumber;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String trackingCompany;

    /** 支付方式，枚举值：QQ,WEIXIN,ALIPAY,LIANLIANPAY */
    @Excel(name = "支付方式，枚举值：QQ,WEIXIN,ALIPAY,LIANLIANPAY")
    private String payType;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String payNo;

    /** 邮费，单位：元 */
    @Excel(name = "邮费，单位：元")
    private Double postage;

    /** 折扣金额，单位：元，折扣金额=平台优惠+商家优惠+团长免单优惠金额 */
    @Excel(name = "折扣金额，单位：元，折扣金额=平台优惠+商家优惠+团长免单优惠金额")
    private Double discountAmount;

    /** 商品金额，单位：元，商品金额=商品销售价格*商品数量-改价金额（接口暂无该字段） */
    @Excel(name = "商品金额，单位：元，商品金额=商品销售价格*商品数量-改价金额", readConverterExp = "接=口暂无该字段")
    private Double goodsAmount;

    /** 支付金额，单位：元，支付金额=商品金额-折扣金额+邮费 */
    @Excel(name = "支付金额，单位：元，支付金额=商品金额-折扣金额+邮费")
    private Double payAmount;

    /** 收件人电话 */
    @Excel(name = "收件人电话")
    private String receiverPhone;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String receiverName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String receiverName1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String receiverPhone1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String receiverAddress1;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 区县 */
    @Excel(name = "区县")
    private String town;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 国家地区 */
    @Excel(name = "国家地区")
    private String country;

    /** 订单创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 支付时间 */
    @Excel(name = "支付时间")
    private String payTime;

    /** 成交时间 */
    @Excel(name = "成交时间")
    private String confirmTime;

    /** 确认收货时间 */
    @Excel(name = "确认收货时间")
    private String receiveTime;

    /** 买家留言信息 */
    @Excel(name = "买家留言信息")
    private String buyerMemo;

    /** 售后状态 0：无售后 2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中 5：平台同意退款，退款中 6：驳回退款， 待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒 绝退款，退款关闭 10：退款成功 11：买家撤销 12：买家逾期未处 理，退款失败 13：买家逾期，超过有效期 14 : 换货补寄待商家处理 15:换货补寄待用户处理 16:换货补寄成功 17:换货补寄失败 18:换货补寄待用户确认完成 */
    @Excel(name = "售后状态 0：无售后 2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中 5：平台同意退款，退款中 6：驳回退款， 待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒 绝退款，退款关闭 10：退款成功 11：买家撤销 12：买家逾期未处 理，退款失败 13：买家逾期，超过有效期 14 : 换货补寄待商家处理 15:换货补寄待用户处理 16:换货补寄成功 17:换货补寄失败 18:换货补寄待用户确认完成")
    private Long afterSalesStatus;

    /** 订单成交时间 */
    @Excel(name = "订单成交时间")
    private Long orderConfirmTime;

    /** 订单承诺发货时间 */
    @Excel(name = "订单承诺发货时间")
    private String lastShipTime;

    /** 0待确认，1已确认2已拦截-9未拉取 */
    @Excel(name = "0待确认，1已确认2已拦截-9未拉取")
    private Long auditStatus;

    /** 结算状态（0未结算1已结算） */
    @Excel(name = "结算状态", readConverterExp = "0=未结算1已结算")
    private Long settlementStatus;

    /** 发货状态（0待出库1拣货中2已拣货3已出库4已发货） */
    @Excel(name = "发货状态", readConverterExp = "0=待出库1拣货中2已拣货3已出库4已发货")
    private Long shipStatus;

    /** 发货时间（仓库真实发货时间） */
    @Excel(name = "发货时间", readConverterExp = "仓=库真实发货时间")
    private Long shipTime;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 导入文件id */
    @Excel(name = "导入文件id")
    private Long excelLogId;

    /** 导入结果 */
    @Excel(name = "导入结果")
    private String excelMsg;

    /** 打印密文 */
    @Excel(name = "打印密文")
    private String encryptedData;

    /** 打印签名 */
    @Excel(name = "打印签名")
    private String signature;

    /** 打印状态（0：未打印1已打印2已取号3已回收） */
    @Excel(name = "打印状态", readConverterExp = "0=：未打印1已打印2已取号3已回收")
    private Integer printStatus;

    /** 打印时间 */
    @Excel(name = "打印时间")
    private String printTime;

    /** 收件人检索 */
    @Excel(name = "收件人检索")
    private String nameKey;

    /** 手机号检索 */
    @Excel(name = "手机号检索")
    private String phoneKey;

    /** 地址检索 */
    @Excel(name = "地址检索")
    private String addressKey;

    /** 订单处理结果 */
    @Excel(name = "订单处理结果")
    private String result;

    /** API拉取时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "API拉取时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pullTime;

    /** 拼多多订单明细信息 */
    private List<PddOrderItem> pddOrderItemList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderSn(String orderSn) 
    {
        this.orderSn = orderSn;
    }

    public String getOrderSn() 
    {
        return orderSn;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setTradeType(Long tradeType) 
    {
        this.tradeType = tradeType;
    }

    public Long getTradeType() 
    {
        return tradeType;
    }
    public void setConfirmStatus(Long confirmStatus) 
    {
        this.confirmStatus = confirmStatus;
    }

    public Long getConfirmStatus() 
    {
        return confirmStatus;
    }
    public void setFreeSf(Long freeSf) 
    {
        this.freeSf = freeSf;
    }

    public Long getFreeSf() 
    {
        return freeSf;
    }
    public void setGroupStatus(Long groupStatus) 
    {
        this.groupStatus = groupStatus;
    }

    public Long getGroupStatus() 
    {
        return groupStatus;
    }
    public void setCapitalFreeDiscount(Double capitalFreeDiscount)
    {
        this.capitalFreeDiscount = capitalFreeDiscount;
    }

    public Double getCapitalFreeDiscount()
    {
        return capitalFreeDiscount;
    }
    public void setSellerDiscount(Double sellerDiscount)
    {
        this.sellerDiscount = sellerDiscount;
    }

    public Double getSellerDiscount()
    {
        return sellerDiscount;
    }
    public void setPlatformDiscount(Double platformDiscount)
    {
        this.platformDiscount = platformDiscount;
    }

    public Double getPlatformDiscount()
    {
        return platformDiscount;
    }
    public void setUpdatedAt(String updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() 
    {
        return updatedAt;
    }
    public void setRefundStatus(Long refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public Long getRefundStatus() 
    {
        return refundStatus;
    }
    public void setIsLuckyFlag(Long isLuckyFlag) 
    {
        this.isLuckyFlag = isLuckyFlag;
    }

    public Long getIsLuckyFlag() 
    {
        return isLuckyFlag;
    }
    public void setOrderStatus(Long orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus() 
    {
        return orderStatus;
    }
    public void setShippingTime(String shippingTime) 
    {
        this.shippingTime = shippingTime;
    }

    public String getShippingTime() 
    {
        return shippingTime;
    }
    public void setTrackingNumber(String trackingNumber) 
    {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() 
    {
        return trackingNumber;
    }
    public void setTrackingCompany(String trackingCompany) 
    {
        this.trackingCompany = trackingCompany;
    }

    public String getTrackingCompany() 
    {
        return trackingCompany;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setPayNo(String payNo) 
    {
        this.payNo = payNo;
    }

    public String getPayNo() 
    {
        return payNo;
    }
    public void setPostage(Double postage)
    {
        this.postage = postage;
    }

    public Double getPostage()
    {
        return postage;
    }
    public void setDiscountAmount(Double discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountAmount()
    {
        return discountAmount;
    }
    public void setGoodsAmount(Double goodsAmount)
    {
        this.goodsAmount = goodsAmount;
    }

    public Double getGoodsAmount()
    {
        return goodsAmount;
    }
    public void setPayAmount(Double payAmount)
    {
        this.payAmount = payAmount;
    }

    public Double getPayAmount()
    {
        return payAmount;
    }
    public void setReceiverPhone(String receiverPhone) 
    {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverPhone() 
    {
        return receiverPhone;
    }
    public void setReceiverName(String receiverName) 
    {
        this.receiverName = receiverName;
    }

    public String getReceiverName() 
    {
        return receiverName;
    }
    public void setReceiverName1(String receiverName1) 
    {
        this.receiverName1 = receiverName1;
    }

    public String getReceiverName1() 
    {
        return receiverName1;
    }
    public void setReceiverPhone1(String receiverPhone1) 
    {
        this.receiverPhone1 = receiverPhone1;
    }

    public String getReceiverPhone1() 
    {
        return receiverPhone1;
    }
    public void setReceiverAddress1(String receiverAddress1) 
    {
        this.receiverAddress1 = receiverAddress1;
    }

    public String getReceiverAddress1() 
    {
        return receiverAddress1;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setTown(String town) 
    {
        this.town = town;
    }

    public String getTown() 
    {
        return town;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }

    public String getCountry() 
    {
        return country;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setPayTime(String payTime) 
    {
        this.payTime = payTime;
    }

    public String getPayTime() 
    {
        return payTime;
    }
    public void setConfirmTime(String confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public String getConfirmTime() 
    {
        return confirmTime;
    }
    public void setReceiveTime(String receiveTime) 
    {
        this.receiveTime = receiveTime;
    }

    public String getReceiveTime() 
    {
        return receiveTime;
    }
    public void setBuyerMemo(String buyerMemo) 
    {
        this.buyerMemo = buyerMemo;
    }

    public String getBuyerMemo() 
    {
        return buyerMemo;
    }
    public void setAfterSalesStatus(Long afterSalesStatus) 
    {
        this.afterSalesStatus = afterSalesStatus;
    }

    public Long getAfterSalesStatus() 
    {
        return afterSalesStatus;
    }
    public void setOrderConfirmTime(Long orderConfirmTime) 
    {
        this.orderConfirmTime = orderConfirmTime;
    }

    public Long getOrderConfirmTime() 
    {
        return orderConfirmTime;
    }
    public void setLastShipTime(String lastShipTime) 
    {
        this.lastShipTime = lastShipTime;
    }

    public String getLastShipTime() 
    {
        return lastShipTime;
    }
    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }
    public void setSettlementStatus(Long settlementStatus) 
    {
        this.settlementStatus = settlementStatus;
    }

    public Long getSettlementStatus() 
    {
        return settlementStatus;
    }
    public void setShipStatus(Long shipStatus) 
    {
        this.shipStatus = shipStatus;
    }

    public Long getShipStatus() 
    {
        return shipStatus;
    }
    public void setShipTime(Long shipTime) 
    {
        this.shipTime = shipTime;
    }

    public Long getShipTime() 
    {
        return shipTime;
    }
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }
    public void setExcelLogId(Long excelLogId) 
    {
        this.excelLogId = excelLogId;
    }

    public Long getExcelLogId() 
    {
        return excelLogId;
    }
    public void setExcelMsg(String excelMsg) 
    {
        this.excelMsg = excelMsg;
    }

    public String getExcelMsg() 
    {
        return excelMsg;
    }
    public void setEncryptedData(String encryptedData) 
    {
        this.encryptedData = encryptedData;
    }

    public String getEncryptedData() 
    {
        return encryptedData;
    }
    public void setSignature(String signature) 
    {
        this.signature = signature;
    }

    public String getSignature() 
    {
        return signature;
    }
    public void setPrintStatus(Integer printStatus) 
    {
        this.printStatus = printStatus;
    }

    public Integer getPrintStatus() 
    {
        return printStatus;
    }
    public void setPrintTime(String printTime) 
    {
        this.printTime = printTime;
    }

    public String getPrintTime() 
    {
        return printTime;
    }
    public void setNameKey(String nameKey) 
    {
        this.nameKey = nameKey;
    }

    public String getNameKey() 
    {
        return nameKey;
    }
    public void setPhoneKey(String phoneKey) 
    {
        this.phoneKey = phoneKey;
    }

    public String getPhoneKey() 
    {
        return phoneKey;
    }
    public void setAddressKey(String addressKey) 
    {
        this.addressKey = addressKey;
    }

    public String getAddressKey() 
    {
        return addressKey;
    }
    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }
    public void setPullTime(Date pullTime) 
    {
        this.pullTime = pullTime;
    }

    public Date getPullTime() 
    {
        return pullTime;
    }

    public List<PddOrderItem> getPddOrderItemList()
    {
        return pddOrderItemList;
    }

    public void setPddOrderItemList(List<PddOrderItem> pddOrderItemList)
    {
        this.pddOrderItemList = pddOrderItemList;
    }

}
