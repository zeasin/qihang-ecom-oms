package com.qihang.erp.api.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 小红书订单退款对象 s_xhs_refund
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class XhsRefund extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 小红书店铺售后id */
    @Excel(name = "小红书店铺售后id")
    private String returnsId;

    /** 退货类型 1-退货退款, 2-换货, 3:仅退款(old) 4:仅退款(new) 理论上不会有3出现 5:未发货仅退款 */
    @Excel(name = "退货类型 1-退货退款, 2-换货, 3:仅退款(old) 4:仅退款(new) 理论上不会有3出现 5:未发货仅退款")
    private Long returnType;

    /** 售后原因id */
    @Excel(name = "售后原因id")
    private Long reasonId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long shopId;

    /** 售后原因说明 */
    @Excel(name = "售后原因说明")
    private String reason;

    /** 售后状态 1:待审核 2:待用户寄回 3:待收货 4:完成 5:取消 6:关闭 9:拒绝 9999:删除 */
    @Excel(name = "售后状态 1:待审核 2:待用户寄回 3:待收货 4:完成 5:取消 6:关闭 9:拒绝 9999:删除")
    private Long status;

    /** 售后子状态 301-待审核 302-快递已签收 304-收货异常 */
    @Excel(name = "售后子状态 301-待审核 302-快递已签收 304-收货异常")
    private Long subStatus;

    /** 收货异常类型 */
    @Excel(name = "收货异常类型")
    private Long receiveAbnormalType;

    /** 订单id */
    @Excel(name = "订单id")
    private String packageId;

    /** 换货订单id */
    @Excel(name = "换货订单id")
    private String exchangePackageId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long createdTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String returnExpressNo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String returnExpressCompany;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String returnAddress;

    /** 是否需要寄回 1-需要 0-不需要 */
    @Excel(name = "是否需要寄回 1-需要 0-不需要")
    private Long shipNeeded;

    /** 是否已退款 */
    @Excel(name = "是否已退款")
    private Long refunded;

    /** 退款状态 108触发退款 1退款中 3退款失败 2退款成功 401已取消 101已创建 201待审核 301审核通过 302审核不通过 402自动关闭 */
    @Excel(name = "退款状态 108触发退款 1退款中 3退款失败 2退款成功 401已取消 101已创建 201待审核 301审核通过 302审核不通过 402自动关闭")
    private Long refundStatus;

    /** 退款时间 */
    @Excel(name = "退款时间")
    private Long refundTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long fillExpressTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long expressSignTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long refundFee;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long returnExpressRefundable;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long returnExpressRefunded;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long expectRefundFee;

    /** 自动确认收货时间 */
    @Excel(name = "自动确认收货时间")
    private Long autoReceiveDeadline;

    /** erp系统发货状态（判断是否出库是否需要拦截）0未处理2已出库 */
    @Excel(name = "erp系统发货状态", readConverterExp = "判=断是否出库是否需要拦截")
    private Long erpSendStatus;

    /** 退货快递公司编号 */
    @Excel(name = "退货快递公司编号")
    private String returnExpressCompanyCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createOn;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date modifyOn;

    /** 小红书订单退款明细信息 */
    private List<XhsRefundItem> xhsRefundItemList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReturnsId(String returnsId) 
    {
        this.returnsId = returnsId;
    }

    public String getReturnsId() 
    {
        return returnsId;
    }
    public void setReturnType(Long returnType) 
    {
        this.returnType = returnType;
    }

    public Long getReturnType() 
    {
        return returnType;
    }
    public void setReasonId(Long reasonId) 
    {
        this.reasonId = reasonId;
    }

    public Long getReasonId() 
    {
        return reasonId;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setSubStatus(Long subStatus) 
    {
        this.subStatus = subStatus;
    }

    public Long getSubStatus() 
    {
        return subStatus;
    }
    public void setReceiveAbnormalType(Long receiveAbnormalType) 
    {
        this.receiveAbnormalType = receiveAbnormalType;
    }

    public Long getReceiveAbnormalType() 
    {
        return receiveAbnormalType;
    }
    public void setPackageId(String packageId) 
    {
        this.packageId = packageId;
    }

    public String getPackageId() 
    {
        return packageId;
    }
    public void setExchangePackageId(String exchangePackageId) 
    {
        this.exchangePackageId = exchangePackageId;
    }

    public String getExchangePackageId() 
    {
        return exchangePackageId;
    }
    public void setCreatedTime(Long createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Long getCreatedTime() 
    {
        return createdTime;
    }
    public void setReturnExpressNo(String returnExpressNo) 
    {
        this.returnExpressNo = returnExpressNo;
    }

    public String getReturnExpressNo() 
    {
        return returnExpressNo;
    }
    public void setReturnExpressCompany(String returnExpressCompany) 
    {
        this.returnExpressCompany = returnExpressCompany;
    }

    public String getReturnExpressCompany() 
    {
        return returnExpressCompany;
    }
    public void setReturnAddress(String returnAddress) 
    {
        this.returnAddress = returnAddress;
    }

    public String getReturnAddress() 
    {
        return returnAddress;
    }
    public void setShipNeeded(Long shipNeeded) 
    {
        this.shipNeeded = shipNeeded;
    }

    public Long getShipNeeded() 
    {
        return shipNeeded;
    }
    public void setRefunded(Long refunded) 
    {
        this.refunded = refunded;
    }

    public Long getRefunded() 
    {
        return refunded;
    }
    public void setRefundStatus(Long refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public Long getRefundStatus() 
    {
        return refundStatus;
    }
    public void setRefundTime(Long refundTime) 
    {
        this.refundTime = refundTime;
    }

    public Long getRefundTime() 
    {
        return refundTime;
    }
    public void setFillExpressTime(Long fillExpressTime) 
    {
        this.fillExpressTime = fillExpressTime;
    }

    public Long getFillExpressTime() 
    {
        return fillExpressTime;
    }
    public void setExpressSignTime(Long expressSignTime) 
    {
        this.expressSignTime = expressSignTime;
    }

    public Long getExpressSignTime() 
    {
        return expressSignTime;
    }
    public void setRefundFee(Long refundFee) 
    {
        this.refundFee = refundFee;
    }

    public Long getRefundFee() 
    {
        return refundFee;
    }
    public void setReturnExpressRefundable(Long returnExpressRefundable) 
    {
        this.returnExpressRefundable = returnExpressRefundable;
    }

    public Long getReturnExpressRefundable() 
    {
        return returnExpressRefundable;
    }
    public void setReturnExpressRefunded(Long returnExpressRefunded) 
    {
        this.returnExpressRefunded = returnExpressRefunded;
    }

    public Long getReturnExpressRefunded() 
    {
        return returnExpressRefunded;
    }
    public void setExpectRefundFee(Long expectRefundFee) 
    {
        this.expectRefundFee = expectRefundFee;
    }

    public Long getExpectRefundFee() 
    {
        return expectRefundFee;
    }
    public void setAutoReceiveDeadline(Long autoReceiveDeadline) 
    {
        this.autoReceiveDeadline = autoReceiveDeadline;
    }

    public Long getAutoReceiveDeadline() 
    {
        return autoReceiveDeadline;
    }
    public void setErpSendStatus(Long erpSendStatus) 
    {
        this.erpSendStatus = erpSendStatus;
    }

    public Long getErpSendStatus() 
    {
        return erpSendStatus;
    }
    public void setReturnExpressCompanyCode(String returnExpressCompanyCode) 
    {
        this.returnExpressCompanyCode = returnExpressCompanyCode;
    }

    public String getReturnExpressCompanyCode() 
    {
        return returnExpressCompanyCode;
    }
    public void setCreateOn(Date createOn) 
    {
        this.createOn = createOn;
    }

    public Date getCreateOn() 
    {
        return createOn;
    }
    public void setModifyOn(Date modifyOn) 
    {
        this.modifyOn = modifyOn;
    }

    public Date getModifyOn() 
    {
        return modifyOn;
    }

    public List<XhsRefundItem> getXhsRefundItemList()
    {
        return xhsRefundItemList;
    }

    public void setXhsRefundItemList(List<XhsRefundItem> xhsRefundItemList)
    {
        this.xhsRefundItemList = xhsRefundItemList;
    }

}
