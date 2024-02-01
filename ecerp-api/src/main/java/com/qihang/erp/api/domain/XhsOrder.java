package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 小红书订单对象 s_xhs_order
 * 
 * @author qihang
 * @date 2024-01-03
 */
public class XhsOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id（自增长） */
    private Long id;
    private Integer shipType;

    /** 订单号（第三方平台orderId） */
    @Excel(name = "订单号", readConverterExp = "第=三方平台orderId")
    private String orderId;

    /** 订单来源（dc_sys_third_setting表id） */
    @Excel(name = "订单来源", readConverterExp = "d=c_sys_third_setting表id")
    private Long shopType;

    /** 店铺ID（dc_shop表id） */
    @Excel(name = "店铺ID", readConverterExp = "d=c_shop表id")
    private Long shopId;

    /** 订单类型：（小红书：订单类型，1普通 2定金预售 3全款预售 4延迟发货 5换货补发） */
    @Excel(name = "订单类型：", readConverterExp = "小=红书：订单类型，1普通,2=定金预售,3=全款预售,4=延迟发货,5=换货补发")
    private Long orderType;

    /** 小红书订单状态，1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中 */
    @Excel(name = "小红书订单状态，1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中")
    private Long orderStatus;

    /** 小红书售后状态，1无售后 2售后处理中 3售后完成(含取消) */
    @Excel(name = "小红书售后状态，1无售后 2售后处理中 3售后完成(含取消)")
    private Long afterSalesStatus;

    /** 申请取消状态，0未申请取消 1取消处理中 */
    @Excel(name = "申请取消状态，0未申请取消 1取消处理中")
    private Long cancelStatus;

    /** 订单创建时间 单位ms */
    @Excel(name = "订单创建时间 单位ms")
    private Long orderCreatedTime;


    /** 订单支付时间 单位ms */
    @Excel(name = "订单支付时间 单位ms")
    private Long orderPaidTime;

    /** 订单更新时间 单位ms */
    @Excel(name = "订单更新时间 单位ms")
    private Long orderUpdateTime;

    /** 订单发货时间 单位ms */
    @Excel(name = "订单发货时间 单位ms")
    private Long orderDeliveryTime;

    /** 订单取消时间 单位ms */
    @Excel(name = "订单取消时间 单位ms")
    private Long orderCancelTime;

    /** 订单完成时间 单位ms */
    @Excel(name = "订单完成时间 单位ms")
    private Long orderFinishTime;

    /** 承诺最晚发货时间 单位ms */
    @Excel(name = "承诺最晚发货时间 单位ms")
    private Long promiseLastDeliveryTime;

    /** 用户备注 */
    @Excel(name = "用户备注")
    private String customerRemark;

    /** 商家标记备注 */
    @Excel(name = "商家标记备注")
    private String sellerRemark;

    /** 商家标记优先级，ark订单列表展示旗子颜色 1灰旗 2红旗 3黄旗 4绿旗 5蓝旗 6紫旗 */
    @Excel(name = "商家标记优先级，ark订单列表展示旗子颜色 1灰旗 2红旗 3黄旗 4绿旗 5蓝旗 6紫旗")
    private Long sellerRemarkFlag;

    /** 预售最早发货时间 单位ms */
    @Excel(name = "预售最早发货时间 单位ms")
    private Long presaleDeliveryStartTime;

    /** 预售最晚发货时间 单位ms */
    @Excel(name = "预售最晚发货时间 单位ms")
    private Long presaleDeliveryEndTime;

    /** 原始关联订单号(退换订单的原订单) */
    @Excel(name = "原始关联订单号(退换订单的原订单)")
    private String originalPackageId;

    /** 订单实付金额(包含运费) 单位分 */
    @Excel(name = "订单实付金额(包含运费) 单位分")
    private Long totalPayAmount;

    private Double goodsAmount;
    private Double shippingFree;

    public Double getShippingFree() {
        return shippingFree;
    }

    public void setShippingFree(Double shippingFree) {
        this.shippingFree = shippingFree;
    }

    public Double getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Double goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    /** 订单运费 单位分 */
    @Excel(name = "订单运费 单位分")
    private Long totalShippingFree;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String expressTrackingNo;

    /** 快递公司编码 */
    @Excel(name = "快递公司编码")
    private String expressCompanyCode;

    /** 收件人姓名+手机+地址等计算得出，用来查询收件人详情 */
    @Excel(name = "收件人姓名+手机+地址等计算得出，用来查询收件人详情")
    private String openAddressId;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区县 */
    @Excel(name = "区县")
    private String district;

    private String receiver;
    private String phone;
    private String address;

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    /** 订单审核状态（0待审核1已审核） */
    @Excel(name = "订单审核状态", readConverterExp = "0=待审核1已审核")
    private Long auditStatus;

    /** 订单审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 结算状态0未结算1已结算 */
    @Excel(name = "结算状态0未结算1已结算")
    private Long settleStatus;

    /** 结算金额 */
    @Excel(name = "结算金额")
    private BigDecimal settleAmount;

    /** ERP发货状态0待处理1出库中2已出库3已发货 */
    @Excel(name = "ERP发货状态0待处理1出库中2已出库3已发货")
    private Long sendStatus;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 小红书订单明细信息 */
    private List<XhsOrderItem> xhsOrderItemList;

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
    public void setShopType(Long shopType) 
    {
        this.shopType = shopType;
    }

    public Long getShopType() 
    {
        return shopType;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setOrderType(Long orderType) 
    {
        this.orderType = orderType;
    }

    public Long getOrderType() 
    {
        return orderType;
    }
    public void setOrderStatus(Long orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus() 
    {
        return orderStatus;
    }
    public void setAfterSalesStatus(Long afterSalesStatus) 
    {
        this.afterSalesStatus = afterSalesStatus;
    }

    public Long getAfterSalesStatus() 
    {
        return afterSalesStatus;
    }
    public void setCancelStatus(Long cancelStatus) 
    {
        this.cancelStatus = cancelStatus;
    }

    public Long getCancelStatus() 
    {
        return cancelStatus;
    }
    public void setOrderCreatedTime(Long orderCreatedTime) 
    {
        this.orderCreatedTime = orderCreatedTime;
    }

    public Long getOrderCreatedTime() 
    {
        return orderCreatedTime;
    }
    public void setOrderPaidTime(Long orderPaidTime) 
    {
        this.orderPaidTime = orderPaidTime;
    }

    public Long getOrderPaidTime() 
    {
        return orderPaidTime;
    }
    public void setOrderUpdateTime(Long orderUpdateTime) 
    {
        this.orderUpdateTime = orderUpdateTime;
    }

    public Long getOrderUpdateTime() 
    {
        return orderUpdateTime;
    }
    public void setOrderDeliveryTime(Long orderDeliveryTime) 
    {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public Long getOrderDeliveryTime() 
    {
        return orderDeliveryTime;
    }
    public void setOrderCancelTime(Long orderCancelTime) 
    {
        this.orderCancelTime = orderCancelTime;
    }

    public Long getOrderCancelTime() 
    {
        return orderCancelTime;
    }
    public void setOrderFinishTime(Long orderFinishTime) 
    {
        this.orderFinishTime = orderFinishTime;
    }

    public Long getOrderFinishTime() 
    {
        return orderFinishTime;
    }
    public void setPromiseLastDeliveryTime(Long promiseLastDeliveryTime) 
    {
        this.promiseLastDeliveryTime = promiseLastDeliveryTime;
    }

    public Long getPromiseLastDeliveryTime() 
    {
        return promiseLastDeliveryTime;
    }
    public void setCustomerRemark(String customerRemark) 
    {
        this.customerRemark = customerRemark;
    }

    public String getCustomerRemark() 
    {
        return customerRemark;
    }
    public void setSellerRemark(String sellerRemark) 
    {
        this.sellerRemark = sellerRemark;
    }

    public String getSellerRemark() 
    {
        return sellerRemark;
    }
    public void setSellerRemarkFlag(Long sellerRemarkFlag) 
    {
        this.sellerRemarkFlag = sellerRemarkFlag;
    }

    public Long getSellerRemarkFlag() 
    {
        return sellerRemarkFlag;
    }
    public void setPresaleDeliveryStartTime(Long presaleDeliveryStartTime) 
    {
        this.presaleDeliveryStartTime = presaleDeliveryStartTime;
    }

    public Long getPresaleDeliveryStartTime() 
    {
        return presaleDeliveryStartTime;
    }
    public void setPresaleDeliveryEndTime(Long presaleDeliveryEndTime) 
    {
        this.presaleDeliveryEndTime = presaleDeliveryEndTime;
    }

    public Long getPresaleDeliveryEndTime() 
    {
        return presaleDeliveryEndTime;
    }
    public void setOriginalPackageId(String originalPackageId) 
    {
        this.originalPackageId = originalPackageId;
    }

    public String getOriginalPackageId() 
    {
        return originalPackageId;
    }
    public void setTotalPayAmount(Long totalPayAmount) 
    {
        this.totalPayAmount = totalPayAmount;
    }

    public Long getTotalPayAmount() 
    {
        return totalPayAmount;
    }
    public void setTotalShippingFree(Long totalShippingFree) 
    {
        this.totalShippingFree = totalShippingFree;
    }

    public Long getTotalShippingFree() 
    {
        return totalShippingFree;
    }
    public void setExpressTrackingNo(String expressTrackingNo) 
    {
        this.expressTrackingNo = expressTrackingNo;
    }

    public String getExpressTrackingNo() 
    {
        return expressTrackingNo;
    }
    public void setExpressCompanyCode(String expressCompanyCode) 
    {
        this.expressCompanyCode = expressCompanyCode;
    }

    public String getExpressCompanyCode() 
    {
        return expressCompanyCode;
    }
    public void setOpenAddressId(String openAddressId) 
    {
        this.openAddressId = openAddressId;
    }

    public String getOpenAddressId() 
    {
        return openAddressId;
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
    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
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
    public void setSettleStatus(Long settleStatus) 
    {
        this.settleStatus = settleStatus;
    }

    public Long getSettleStatus() 
    {
        return settleStatus;
    }
    public void setSettleAmount(BigDecimal settleAmount) 
    {
        this.settleAmount = settleAmount;
    }

    public BigDecimal getSettleAmount() 
    {
        return settleAmount;
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

    public List<XhsOrderItem> getXhsOrderItemList()
    {
        return xhsOrderItemList;
    }

    public void setXhsOrderItemList(List<XhsOrderItem> xhsOrderItemList)
    {
        this.xhsOrderItemList = xhsOrderItemList;
    }

}
