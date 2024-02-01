package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 抖店订单对象 s_dou_order
 * 
 * @author qihang
 * @date 2024-01-02
 */
public class DouOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id，自增 */
    private Long id;

    /** 抖音订单id */
    @Excel(name = "抖音订单id")
    private String orderId;

    /** 订单所属商户id */
    @Excel(name = "订单所属商户id")
    private Long shopId;

    /** 买家用户名 */
    @Excel(name = "买家用户名")
    private String userName;

    /** 邮寄地址 (展开为省市区json, 格式参考 订单-获取订单列表 返回示例) */
    @Excel(name = "邮寄地址 (展开为省市区json, 格式参考 订单-获取订单列表 返回示例)")
    private String postAddr;

    /** 邮政编码 */
    @Excel(name = "邮政编码")
    private String postCode;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String postReceiver;

    /** 收件人电话 */
    @Excel(name = "收件人电话")
    private String postTel;

    /** 买家备注 */
    @Excel(name = "买家备注")
    private String buyerWords;

    /** 卖家备注 */
    @Excel(name = "卖家备注")
    private String sellerWords;

    /** 物流公司id */
    @Excel(name = "物流公司id")
    private String logisticsId;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsCode;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date logisticsTime;

    /** 收货时间 */
    @Excel(name = "收货时间")
    private Long receiptTime;

    /** 订单状态1 待确认/待支付（订单创建完毕）105 已支付 2 备货中 101 部分发货 3 已发货（全部发货）4 已取消5 已完成（已收货） */
    @Excel(name = "订单状态1 待确认/待支付", readConverterExp = "订=单创建完毕")
    private Long orderStatus;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String orderStatusStr;

    /** 订单创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderCreateTime;

    /** 最晚发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最晚发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expShipTime;

    /** 订单取消原因 */
    @Excel(name = "订单取消原因")
    private String cancelReason;

    /** 【支付类型】 0、货到付款 1 、微信 2、支付宝 3、小程序 4、银行卡 5、余额 7、无需支付（0元单） 8、DOU分期（信用支付） 9、新卡支付 12、先用后付 */
    @Excel(name = "【支付类型】 0、货到付款 1 、微信 2、支付宝 3、小程序 4、银行卡 5、余额 7、无需支付", readConverterExp = "0=元单")
    private Long payType;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private String payTypeName;

    /** 支付时间 (pay_type为0货到付款时, 此字段为空) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间 (pay_type为0货到付款时, 此字段为空)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 邮费金额 (单位: 分) */
    @Excel(name = "邮费金额 (单位: 分)")
    private BigDecimal postAmount;

    /** 平台优惠券金额 (单位: 分) */
    @Excel(name = "平台优惠券金额 (单位: 分)")
    private BigDecimal couponAmount;

    /** 商家优惠券金额 (单位: 分) */
    @Excel(name = "商家优惠券金额 (单位: 分)")
    private BigDecimal shopCouponAmount;

    /** 优惠券详情 (type为优惠券类型, credit为优惠金额,单位分) */
    @Excel(name = "优惠券详情 (type为优惠券类型, credit为优惠金额,单位分)")
    private String couponInfo;

    /** 父订单总金额 (单位: 分) 即用户实际支付金额, 不包含运费 */
    @Excel(name = "父订单总金额 (单位: 分) 即用户实际支付金额, 不包含运费")
    private BigDecimal orderTotalAmount;

    /** 运费险金额（单位：分） */
    @Excel(name = "运费险金额", readConverterExp = "单=位：分")
    private Long postInsuranceAmount;

    /** 是否评价 (1:已评价) */
    @Excel(name = "是否评价 (1:已评价)")
    private Long isComment;

    /** 订单佣金 (详情见附录) */
    @Excel(name = "订单佣金 (详情见附录)")
    private Long cType;

    /** 订单渠道 (站外0 火山1 抖音2 头条3 西瓜4 微信5 闪购6 头条lite版本7 懂车帝8 皮皮虾9) */
    @Excel(name = "订单渠道 (站外0 火山1 抖音2 头条3 西瓜4 微信5 闪购6 头条lite版本7 懂车帝8 皮皮虾9)")
    private Long bType;

    /** app渠道 */
    @Excel(name = "app渠道")
    private String appSource;

    /** 流量来源 */
    @Excel(name = "流量来源")
    private String trafficeSource;

    /** 佣金率 */
    @Excel(name = "佣金率")
    private BigDecimal cosRatio;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 发货状态（0待出库1拣货中2已拣货3已出库4已发货） */
    @Excel(name = "发货状态", readConverterExp = "0=待出库1拣货中2已拣货3已出库4已发货")
    private Long sendStatus;

    /** 发货时间（仓库真实发货时间） */
    @Excel(name = "发货时间", readConverterExp = "仓=库真实发货时间")
    private Date sendTime;

    /** 订单审核状态（0待审核1已审核） */
    @Excel(name = "订单审核状态", readConverterExp = "0=待审核1已审核")
    private Long auditStatus;

    /** 加密地址详情 */
    @Excel(name = "加密地址详情")
    private String encryptDetail;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String province;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String city;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String town;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String street;

    /** 发货时间 */
    @Excel(name = "发货时间")
    private Long shipTime;

    /** 0、普通 1、拼团 2、定金预售 3、订金找贷 4、拍卖 5、0元单 6、回收 7、寄卖 */
    @Excel(name = "0、普通 1、拼团 2、定金预售 3、订金找贷 4、拍卖 5、0元单 6、回收 7、寄卖")
    private Long tradeType;

    /** 加密电话 */
    @Excel(name = "加密电话")
    private String encryptPostTel;

    /** 加密联系人 */
    @Excel(name = "加密联系人")
    private String encryptPostReceiver;

    /** 打单结果 */
    @Excel(name = "打单结果")
    private String result;

    /** 打印状态（0：未打印1已取号2已打印3已回收4已取消） */
    @Excel(name = "打印状态", readConverterExp = "0=：未打印1已取号2已打印3已回收4已取消")
    private Integer printStatus;

    /** 打印日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打印日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date printTime;

    /** 号码检索串 */
    @Excel(name = "号码检索串")
    private String phoneKey;

    /** 地址检索串 */
    @Excel(name = "地址检索串")
    private String addressKey;

    /** 达人id */
    @Excel(name = "达人id")
    private Long authorId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String authorName;

    /** 是否结算（0:未结算1：已结算,2.已退款） */
    @Excel(name = "是否结算", readConverterExp = "0=:未结算1：已结算,2.已退款")
    private Long settlementStatus;

    /** 抖店订单明细信息 */
    private List<DouOrderItem> douOrderItemList;

    private Integer shipType;

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setPostAddr(String postAddr) 
    {
        this.postAddr = postAddr;
    }

    public String getPostAddr() 
    {
        return postAddr;
    }
    public void setPostCode(String postCode) 
    {
        this.postCode = postCode;
    }

    public String getPostCode() 
    {
        return postCode;
    }
    public void setPostReceiver(String postReceiver) 
    {
        this.postReceiver = postReceiver;
    }

    public String getPostReceiver() 
    {
        return postReceiver;
    }
    public void setPostTel(String postTel) 
    {
        this.postTel = postTel;
    }

    public String getPostTel() 
    {
        return postTel;
    }
    public void setBuyerWords(String buyerWords) 
    {
        this.buyerWords = buyerWords;
    }

    public String getBuyerWords() 
    {
        return buyerWords;
    }
    public void setSellerWords(String sellerWords) 
    {
        this.sellerWords = sellerWords;
    }

    public String getSellerWords() 
    {
        return sellerWords;
    }
    public void setLogisticsId(String logisticsId) 
    {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsId() 
    {
        return logisticsId;
    }
    public void setLogisticsCode(String logisticsCode) 
    {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCode() 
    {
        return logisticsCode;
    }
    public void setLogisticsCompany(String logisticsCompany) 
    {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsCompany() 
    {
        return logisticsCompany;
    }
    public void setLogisticsTime(Date logisticsTime) 
    {
        this.logisticsTime = logisticsTime;
    }

    public Date getLogisticsTime() 
    {
        return logisticsTime;
    }
    public void setReceiptTime(Long receiptTime) 
    {
        this.receiptTime = receiptTime;
    }

    public Long getReceiptTime() 
    {
        return receiptTime;
    }
    public void setOrderStatus(Long orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus() 
    {
        return orderStatus;
    }
    public void setOrderStatusStr(String orderStatusStr) 
    {
        this.orderStatusStr = orderStatusStr;
    }

    public String getOrderStatusStr() 
    {
        return orderStatusStr;
    }
    public void setOrderCreateTime(Date orderCreateTime) 
    {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderCreateTime() 
    {
        return orderCreateTime;
    }
    public void setExpShipTime(Date expShipTime) 
    {
        this.expShipTime = expShipTime;
    }

    public Date getExpShipTime() 
    {
        return expShipTime;
    }
    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
    }
    public void setPayType(Long payType) 
    {
        this.payType = payType;
    }

    public Long getPayType() 
    {
        return payType;
    }
    public void setPayTypeName(String payTypeName) 
    {
        this.payTypeName = payTypeName;
    }

    public String getPayTypeName() 
    {
        return payTypeName;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setPostAmount(BigDecimal postAmount)
    {
        this.postAmount = postAmount;
    }

    public BigDecimal getPostAmount()
    {
        return postAmount;
    }
    public void setCouponAmount(BigDecimal couponAmount)
    {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getCouponAmount()
    {
        return couponAmount;
    }
    public void setShopCouponAmount(BigDecimal shopCouponAmount)
    {
        this.shopCouponAmount = shopCouponAmount;
    }

    public BigDecimal getShopCouponAmount()
    {
        return shopCouponAmount;
    }
    public void setCouponInfo(String couponInfo) 
    {
        this.couponInfo = couponInfo;
    }

    public String getCouponInfo() 
    {
        return couponInfo;
    }
    public void setOrderTotalAmount(BigDecimal orderTotalAmount)
    {
        this.orderTotalAmount = orderTotalAmount;
    }

    public BigDecimal getOrderTotalAmount()
    {
        return orderTotalAmount;
    }
    public void setPostInsuranceAmount(Long postInsuranceAmount) 
    {
        this.postInsuranceAmount = postInsuranceAmount;
    }

    public Long getPostInsuranceAmount() 
    {
        return postInsuranceAmount;
    }
    public void setIsComment(Long isComment) 
    {
        this.isComment = isComment;
    }

    public Long getIsComment() 
    {
        return isComment;
    }
    public void setcType(Long cType) 
    {
        this.cType = cType;
    }

    public Long getcType() 
    {
        return cType;
    }
    public void setbType(Long bType) 
    {
        this.bType = bType;
    }

    public Long getbType() 
    {
        return bType;
    }
    public void setAppSource(String appSource) 
    {
        this.appSource = appSource;
    }

    public String getAppSource() 
    {
        return appSource;
    }
    public void setTrafficeSource(String trafficeSource) 
    {
        this.trafficeSource = trafficeSource;
    }

    public String getTrafficeSource() 
    {
        return trafficeSource;
    }
    public void setCosRatio(BigDecimal cosRatio) 
    {
        this.cosRatio = cosRatio;
    }

    public BigDecimal getCosRatio() 
    {
        return cosRatio;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setSendStatus(Long sendStatus) 
    {
        this.sendStatus = sendStatus;
    }

    public Long getSendStatus() 
    {
        return sendStatus;
    }
    public void setSendTime(Date sendTime) 
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime() 
    {
        return sendTime;
    }
    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }
    public void setEncryptDetail(String encryptDetail) 
    {
        this.encryptDetail = encryptDetail;
    }

    public String getEncryptDetail() 
    {
        return encryptDetail;
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
    public void setStreet(String street) 
    {
        this.street = street;
    }

    public String getStreet() 
    {
        return street;
    }
    public void setShipTime(Long shipTime) 
    {
        this.shipTime = shipTime;
    }

    public Long getShipTime() 
    {
        return shipTime;
    }
    public void setTradeType(Long tradeType) 
    {
        this.tradeType = tradeType;
    }

    public Long getTradeType() 
    {
        return tradeType;
    }
    public void setEncryptPostTel(String encryptPostTel) 
    {
        this.encryptPostTel = encryptPostTel;
    }

    public String getEncryptPostTel() 
    {
        return encryptPostTel;
    }
    public void setEncryptPostReceiver(String encryptPostReceiver) 
    {
        this.encryptPostReceiver = encryptPostReceiver;
    }

    public String getEncryptPostReceiver() 
    {
        return encryptPostReceiver;
    }
    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }
    public void setPrintStatus(Integer printStatus) 
    {
        this.printStatus = printStatus;
    }

    public Integer getPrintStatus() 
    {
        return printStatus;
    }
    public void setPrintTime(Date printTime) 
    {
        this.printTime = printTime;
    }

    public Date getPrintTime() 
    {
        return printTime;
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
    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }
    public void setAuthorName(String authorName) 
    {
        this.authorName = authorName;
    }

    public String getAuthorName() 
    {
        return authorName;
    }
    public void setSettlementStatus(Long settlementStatus) 
    {
        this.settlementStatus = settlementStatus;
    }

    public Long getSettlementStatus() 
    {
        return settlementStatus;
    }

    public List<DouOrderItem> getDouOrderItemList()
    {
        return douOrderItemList;
    }

    public void setDouOrderItemList(List<DouOrderItem> douOrderItemList)
    {
        this.douOrderItemList = douOrderItemList;
    }

}
