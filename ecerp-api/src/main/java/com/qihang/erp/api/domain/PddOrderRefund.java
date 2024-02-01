package com.qihang.erp.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 拼多多订单退款对象 s_pdd_order_refund
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class PddOrderRefund extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 售后编号 */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderSn;

    private Long orderItemId;
    private Integer refundQty;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getRefundQty() {
        return refundQty;
    }

    public void setRefundQty(Integer refundQty) {
        this.refundQty = refundQty;
    }

    /** 内部店铺ID */
    @Excel(name = "内部店铺ID")
    private Long shopId;

    /** 必填，售后类型2：仅退款 3：退货退款 4：换货 5：缺货补寄 9:拦截退货 */
    @Excel(name = "必填，售后类型2：仅退款 3：退货退款 4：换货 5：缺货补寄 9:拦截退货")
    private Long afterSalesType;

    /** 必填，售后状态 1：全部 2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中 5：平台同意退款，退款中 6：驳回退款， 待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒 绝退款，退款关闭 10：退款成功 11：买家撤销 12：买家逾期未处 理，退款失败 13：买家逾期，超过有效期 14 : 换货补寄待商家处理 15:换货补寄待用户处理 16:换货补寄成功 17:换货补寄失败 18:换货补寄待用户确认完成 31：商家同意拒收退款，待用户拒收;32: 待商家补寄发货 */
    @Excel(name = "必填，售后状态 1：全部 2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中 5：平台同意退款，退款中 6：驳回退款， 待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒 绝退款，退款关闭 10：退款成功 11：买家撤销 12：买家逾期未处 理，退款失败 13：买家逾期，超过有效期 14 : 换货补寄待商家处理 15:换货补寄待用户处理 16:换货补寄成功 17:换货补寄失败 18:换货补寄待用户确认完成 31：商家同意拒收退款，待用户拒收;32: 待商家补寄发货")
    private Long afterSalesStatus;

    /** 售后原因 */
    @Excel(name = "售后原因")
    private String afterSaleReason;

    /** 订单成团时间 */
    @Excel(name = "订单成团时间")
    private Long confirmTime;

    /** 时间 */
    @Excel(name = "时间")
    private Long createdTime;

    /** 订单折扣金额（元） */
    @Excel(name = "订单折扣金额", readConverterExp = "元=")
    private Double discountAmount;

    /** 订单金额（元） */
    @Excel(name = "订单金额", readConverterExp = "元=")
    private Double orderAmount;

    /** 退款金额（元） */
    @Excel(name = "退款金额", readConverterExp = "元=")
    private Long refundAmount;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String goodsImage;

    /** 拼多多商品id */
    @Excel(name = "拼多多商品id")
    private Long goodsId;

    /** 拼多多商品skuid */
    @Excel(name = "拼多多商品skuid")
    private Long skuId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNumber;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String specNumber;

    /** 商品规格 */
    @Excel(name = "商品规格")
    private String goodsSpec;

    /** 数量 */
    @Excel(name = "数量")
    private Integer quantity;

    /** 商品价格，单位：元 */
    @Excel(name = "商品价格，单位：元")
    private Double goodsPrice;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private String updatedTime;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String trackingNumber;

    /** 快递公司 */
    @Excel(name = "快递公司")
    private String trackingCompany;

    /** 审核状态2已签收9供应商已退款 */
    @Excel(name = "审核状态2已签收9供应商已退款")
    private Long auditStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 备注 */
    @Excel(name = "备注")
    private String describe;

    /** 订单发货状态 0:未发货， 1:已发货（包含：已发货，已揽收） */
    @Excel(name = "订单发货状态 0:未发货， 1:已发货", readConverterExp = "包=含：已发货，已揽收")
    private Long shippingStatus;

    /** 0-未勾选 1-消费者选择的收货状态为未收到货 2-消费者选择的收货状态为已收到货 */
    @Excel(name = "0-未勾选 1-消费者选择的收货状态为未收到货 2-消费者选择的收货状态为已收到货")
    private Long userShippingStatus;

    /** erp退货单号 */
    @Excel(name = "erp退货单号")
    private String erpOrderReturnNum;

    /** erp退货单id */
    @Excel(name = "erp退货单id")
    private Long erpOrderReturnId;

    /** 标记 */
    @Excel(name = "标记")
    private String sign;

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
    public void setAfterSalesType(Long afterSalesType) 
    {
        this.afterSalesType = afterSalesType;
    }

    public Long getAfterSalesType() 
    {
        return afterSalesType;
    }
    public void setAfterSalesStatus(Long afterSalesStatus) 
    {
        this.afterSalesStatus = afterSalesStatus;
    }

    public Long getAfterSalesStatus() 
    {
        return afterSalesStatus;
    }
    public void setAfterSaleReason(String afterSaleReason) 
    {
        this.afterSaleReason = afterSaleReason;
    }

    public String getAfterSaleReason() 
    {
        return afterSaleReason;
    }
    public void setConfirmTime(Long confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public Long getConfirmTime() 
    {
        return confirmTime;
    }
    public void setCreatedTime(Long createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Long getCreatedTime() 
    {
        return createdTime;
    }
    public void setDiscountAmount(Double discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountAmount()
    {
        return discountAmount;
    }
    public void setOrderAmount(Double orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public Double getOrderAmount()
    {
        return orderAmount;
    }
    public void setRefundAmount(Long refundAmount) 
    {
        this.refundAmount = refundAmount;
    }

    public Long getRefundAmount() 
    {
        return refundAmount;
    }
    public void setGoodsImage(String goodsImage) 
    {
        this.goodsImage = goodsImage;
    }

    public String getGoodsImage() 
    {
        return goodsImage;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setSkuId(Long skuId) 
    {
        this.skuId = skuId;
    }

    public Long getSkuId() 
    {
        return skuId;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setGoodsNumber(String goodsNumber) 
    {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsNumber() 
    {
        return goodsNumber;
    }

    public String getSpecNumber() {
        return specNumber;
    }

    public void setSpecNumber(String specNumber) {
        this.specNumber = specNumber;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getQuantity()
    {
        return quantity;
    }
    public void setGoodsPrice(Double goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public Double getGoodsPrice()
    {
        return goodsPrice;
    }
    public void setUpdatedTime(String updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedTime() 
    {
        return updatedTime;
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
    public void setDescribe(String describe) 
    {
        this.describe = describe;
    }

    public String getDescribe() 
    {
        return describe;
    }
    public void setShippingStatus(Long shippingStatus) 
    {
        this.shippingStatus = shippingStatus;
    }

    public Long getShippingStatus() 
    {
        return shippingStatus;
    }
    public void setUserShippingStatus(Long userShippingStatus) 
    {
        this.userShippingStatus = userShippingStatus;
    }

    public Long getUserShippingStatus() 
    {
        return userShippingStatus;
    }
    public void setErpOrderReturnNum(String erpOrderReturnNum) 
    {
        this.erpOrderReturnNum = erpOrderReturnNum;
    }

    public String getErpOrderReturnNum() 
    {
        return erpOrderReturnNum;
    }
    public void setErpOrderReturnId(Long erpOrderReturnId) 
    {
        this.erpOrderReturnId = erpOrderReturnId;
    }

    public Long getErpOrderReturnId() 
    {
        return erpOrderReturnId;
    }
    public void setSign(String sign) 
    {
        this.sign = sign;
    }

    public String getSign() 
    {
        return sign;
    }

}
