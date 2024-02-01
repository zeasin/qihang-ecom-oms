package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 淘宝订单对象 s_tao_order
 * 
 * @author qihang
 * @date 2024-01-03
 */
public class TaoOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id（天猫订单id） */
    private String id;

    /** 店铺id */
    @Excel(name = "店铺id")
    private Long shopId;

    /** 订单来源0天猫1淘宝 */
    @Excel(name = "订单来源0天猫1淘宝")
    private Long orderSource;

    /** 买家昵称 */
    @Excel(name = "买家昵称")
    private String buyerName;

    /** 应付款总金额，totalAmount = ∑itemAmount + shippingFee，单位为元 */
    @Excel(name = "应付款总金额，totalAmount = ∑itemAmount + shippingFee，单位为元")
    private BigDecimal totalAmount;

    /** 运费 */
    @Excel(name = "运费")
    private BigDecimal shippingFee;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountAmount;

    /** 实际支付金额 */
    @Excel(name = "实际支付金额")
    private BigDecimal payAmount;

    /** 优惠描述 */
    @Excel(name = "优惠描述")
    private String discountRemark;

    /** 订单创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderCreateTime;

    /** 订单修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderModifyTime;

    /** 付款时间，如果有多次付款，这里返回的是首次付款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "付款时间，如果有多次付款，这里返回的是首次付款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmedTime;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveredTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 卖家备忘信息 */
    @Excel(name = "卖家备忘信息")
    private String sellerMemo;

    /** 买家留言，不超过500字 */
    @Excel(name = "买家留言，不超过500字")
    private String buyerFeedback;

    /** 关闭原因。buyerCancel:买家取消订单，sellerGoodsLack:卖家库存不足，other:其它 */
    @Excel(name = "关闭原因。buyerCancel:买家取消订单，sellerGoodsLack:卖家库存不足，other:其它")
    private String closeReason;

    /** 收货时间，这里返回的是完全收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收货时间，这里返回的是完全收货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receivingTime;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String statusStr;

    /** 交易状态，waitbuyerpay:等待买家付款;waitsellersend:等待卖家发货;waitlogisticstakein:等待物流公司揽件;waitbuyerreceive:等待买家收货;waitbuyersign:等待买家签收;signinsuccess:买家已签收;confirm_goods:已收货;success:交易成功;cancel:交易取消;terminated:交易终止;未枚举:其他状态 */
    @Excel(name = "交易状态，waitbuyerpay:等待买家付款;waitsellersend:等待卖家发货;waitlogisticstakein:等待物流公司揽件;waitbuyerreceive:等待买家收货;waitbuyersign:等待买家签收;signinsuccess:买家已签收;confirm_goods:已收货;success:交易成功;cancel:交易取消;terminated:交易终止;未枚举:其他状态")
    private Long status;

    /** 快递公司 */
    @Excel(name = "快递公司")
    private String logisticsCompany;

    /** 快递公司编码 */
    @Excel(name = "快递公司编码")
    private String logisticsCompanyCode;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String logisticsCode;

    /** 退款单ID */
    @Excel(name = "退款单ID")
    private String refundId;

    /** 退款金额，单位为元 */
    @Excel(name = "退款金额，单位为元")
    private BigDecimal refundAmount;

    /** 订单的售中退款状态，等待卖家同意：waitselleragree ，待买家修改：waitbuyermodify，等待买家退货：waitbuyersend，等待卖家确认收货：waitsellerreceive，退款成功：refundsuccess，退款失败：refundclose */
    @Excel(name = "订单的售中退款状态，等待卖家同意：waitselleragree ，待买家修改：waitbuyermodify，等待买家退货：waitbuyersend，等待卖家确认收货：waitsellerreceive，退款成功：refundsuccess，退款失败：refundclose")
    private String refundStatus;

    /** 订单审核状态（0待审核1已审核） */
    @Excel(name = "订单审核状态", readConverterExp = "0=待审核1已审核")
    private Long auditStatus;

    /** 订单审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 发货状态（0待出库1拣货中2已拣货3已出库4已发货） */
    @Excel(name = "发货状态", readConverterExp = "0=待出库1拣货中2已拣货3已出库4已发货")
    private Long sendStatus;

    /** 仓库发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "仓库发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 标签(1：实售2：淘宝客3：刷单4：返现) */
    @Excel(name = "标签(1：实售2：淘宝客3：刷单4：返现)")
    private String tag;

    /** 是否评价 */
    @Excel(name = "是否评价")
    private Integer isComment;

    /** 是否合并发货(0:否1:是) */
    @Excel(name = "是否合并发货(0:否1:是)")
    private Integer isMerge;

    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String address;
    private Integer shipType;

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    /** 淘宝订单明细信息 */
    private List<TaoOrderItem> taoOrderItemList;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setOrderSource(Long orderSource) 
    {
        this.orderSource = orderSource;
    }

    public Long getOrderSource() 
    {
        return orderSource;
    }
    public void setBuyerName(String buyerName) 
    {
        this.buyerName = buyerName;
    }

    public String getBuyerName() 
    {
        return buyerName;
    }
    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }
    public void setShippingFee(BigDecimal shippingFee) 
    {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getShippingFee() 
    {
        return shippingFee;
    }
    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }
    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }
    public void setDiscountRemark(String discountRemark) 
    {
        this.discountRemark = discountRemark;
    }

    public String getDiscountRemark() 
    {
        return discountRemark;
    }
    public void setOrderCreateTime(Date orderCreateTime) 
    {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderCreateTime() 
    {
        return orderCreateTime;
    }
    public void setOrderModifyTime(Date orderModifyTime) 
    {
        this.orderModifyTime = orderModifyTime;
    }

    public Date getOrderModifyTime() 
    {
        return orderModifyTime;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setConfirmedTime(Date confirmedTime) 
    {
        this.confirmedTime = confirmedTime;
    }

    public Date getConfirmedTime() 
    {
        return confirmedTime;
    }
    public void setDeliveredTime(Date deliveredTime) 
    {
        this.deliveredTime = deliveredTime;
    }

    public Date getDeliveredTime() 
    {
        return deliveredTime;
    }
    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }
    public void setSellerMemo(String sellerMemo) 
    {
        this.sellerMemo = sellerMemo;
    }

    public String getSellerMemo() 
    {
        return sellerMemo;
    }
    public void setBuyerFeedback(String buyerFeedback) 
    {
        this.buyerFeedback = buyerFeedback;
    }

    public String getBuyerFeedback() 
    {
        return buyerFeedback;
    }
    public void setCloseReason(String closeReason) 
    {
        this.closeReason = closeReason;
    }

    public String getCloseReason() 
    {
        return closeReason;
    }
    public void setReceivingTime(Date receivingTime) 
    {
        this.receivingTime = receivingTime;
    }

    public Date getReceivingTime() 
    {
        return receivingTime;
    }
    public void setStatusStr(String statusStr) 
    {
        this.statusStr = statusStr;
    }

    public String getStatusStr() 
    {
        return statusStr;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setLogisticsCompany(String logisticsCompany) 
    {
        this.logisticsCompany = logisticsCompany;
    }

    public String getLogisticsCompany() 
    {
        return logisticsCompany;
    }
    public void setLogisticsCompanyCode(String logisticsCompanyCode) 
    {
        this.logisticsCompanyCode = logisticsCompanyCode;
    }

    public String getLogisticsCompanyCode() 
    {
        return logisticsCompanyCode;
    }
    public void setLogisticsCode(String logisticsCode) 
    {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCode() 
    {
        return logisticsCode;
    }
    public void setRefundId(String refundId) 
    {
        this.refundId = refundId;
    }

    public String getRefundId() 
    {
        return refundId;
    }
    public void setRefundAmount(BigDecimal refundAmount) 
    {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundAmount() 
    {
        return refundAmount;
    }
    public void setRefundStatus(String refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatus() 
    {
        return refundStatus;
    }
    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }
    public void setAuditTime(Date auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() 
    {
        return auditTime;
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
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }
    public void setIsComment(Integer isComment) 
    {
        this.isComment = isComment;
    }

    public Integer getIsComment() 
    {
        return isComment;
    }
    public void setIsMerge(Integer isMerge) 
    {
        this.isMerge = isMerge;
    }

    public Integer getIsMerge() 
    {
        return isMerge;
    }

    public List<TaoOrderItem> getTaoOrderItemList()
    {
        return taoOrderItemList;
    }

    public void setTaoOrderItemList(List<TaoOrderItem> taoOrderItemList)
    {
        this.taoOrderItemList = taoOrderItemList;
    }

}
