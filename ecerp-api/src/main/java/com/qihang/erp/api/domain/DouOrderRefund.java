package com.qihang.erp.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 抖店订单退款对象 s_dou_order_refund
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class DouOrderRefund extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 售后订单id，自增 */
    private Long id;

    /** 售后id */
    @Excel(name = "售后id")
    private Long aftersaleId;

    /** 售后类型，枚举为0(退货退款),1(已发货仅退款),2(未发货仅退款),3(换货) */
    @Excel(name = "售后类型，枚举为0(退货退款),1(已发货仅退款),2(未发货仅退款),3(换货)")
    private Long aftersaleType;

    /** 抖音订单id */
    @Excel(name = "抖音订单id")
    private String orderId;

    /** 抖音子订单id */
    @Excel(name = "抖音子订单id")
    private String subOrderId;

    /** 订单所属商户id */
    @Excel(name = "订单所属商户id")
    private Long shopId;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String productPic;

    /** 商品id */
    @Excel(name = "商品id")
    private String productId;

    /** 商品名 */
    @Excel(name = "商品名")
    private String productName;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 该子订单购买的商品 sku_id */
    @Excel(name = "该子订单购买的商品 sku_id")
    private String comboId;

    /** 商品规格 */
    @Excel(name = "商品规格")
    private String goodsSpec;

    /** 商品规格编码 */
    @Excel(name = "商品规格编码")
    private String specNum;

    /** 退货数量 */
    @Excel(name = "退货数量")
    private Long comboNum;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsCode;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 发货时间 */
    @Excel(name = "发货时间")
    private String logisticsTime;

    /** 收货时间 */
    @Excel(name = "收货时间")
    private String receiptTime;

    /** 订单取消原因 */
    @Excel(name = "订单取消原因")
    private String cancelReason;

    /** 父订单总金额 (单位: 分) 即用户实际支付金额, 不包含运费 */
    @Excel(name = "父订单总金额 (单位: 分) 即用户实际支付金额, 不包含运费")
    private Double orderAmount;

    /** 金额 */
    @Excel(name = "金额")
    private Double comboAmount;

    /** 售后理由 */
    @Excel(name = "售后理由")
    private String questionDesc;

    /** 申请退货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请退货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 确认状态0:未处理2已签收9供应商已退款 */
    @Excel(name = "确认状态0:未处理2已签收9供应商已退款")
    private Long auditStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 枚举为6(待商家同意),7(待买家退货),11(待商家二次同意),12(售后成功),13(换货待买家收货),14(换货成功),27(商家一次拒绝),28(售后失败),29(商家二次拒绝) */
    @Excel(name = "枚举为6(待商家同意),7(待买家退货),11(待商家二次同意),12(售后成功),13(换货待买家收货),14(换货成功),27(商家一次拒绝),28(售后失败),29(商家二次拒绝)")
    private Long refundStatus;

    /** erp商品ID */
    @Excel(name = "erp商品ID")
    private Long erpGoodsId;

    /** erp商品规格ID */
    @Excel(name = "erp商品规格ID")
    private Long erpSpecId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAftersaleId(Long aftersaleId) 
    {
        this.aftersaleId = aftersaleId;
    }

    public Long getAftersaleId() 
    {
        return aftersaleId;
    }
    public void setAftersaleType(Long aftersaleType) 
    {
        this.aftersaleType = aftersaleType;
    }

    public Long getAftersaleType() 
    {
        return aftersaleType;
    }
    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }
    public void setSubOrderId(String subOrderId) 
    {
        this.subOrderId = subOrderId;
    }

    public String getSubOrderId() 
    {
        return subOrderId;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setProductPic(String productPic) 
    {
        this.productPic = productPic;
    }

    public String getProductPic() 
    {
        return productPic;
    }
    public void setProductId(String productId) 
    {
        this.productId = productId;
    }

    public String getProductId() 
    {
        return productId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setComboId(String comboId) 
    {
        this.comboId = comboId;
    }

    public String getComboId() 
    {
        return comboId;
    }
    public void setGoodsSpec(String goodsSpec) 
    {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsSpec() 
    {
        return goodsSpec;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setComboNum(Long comboNum) 
    {
        this.comboNum = comboNum;
    }

    public Long getComboNum() 
    {
        return comboNum;
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
    public void setLogisticsTime(String logisticsTime) 
    {
        this.logisticsTime = logisticsTime;
    }

    public String getLogisticsTime() 
    {
        return logisticsTime;
    }
    public void setReceiptTime(String receiptTime) 
    {
        this.receiptTime = receiptTime;
    }

    public String getReceiptTime() 
    {
        return receiptTime;
    }
    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
    }
    public void setOrderAmount(Double orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public Double getOrderAmount()
    {
        return orderAmount;
    }
    public void setComboAmount(Double comboAmount)
    {
        this.comboAmount = comboAmount;
    }

    public Double getComboAmount()
    {
        return comboAmount;
    }
    public void setQuestionDesc(String questionDesc) 
    {
        this.questionDesc = questionDesc;
    }

    public String getQuestionDesc() 
    {
        return questionDesc;
    }
    public void setApplyTime(Date applyTime) 
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() 
    {
        return applyTime;
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
    public void setRefundStatus(Long refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public Long getRefundStatus() 
    {
        return refundStatus;
    }
    public void setErpGoodsId(Long erpGoodsId) 
    {
        this.erpGoodsId = erpGoodsId;
    }

    public Long getErpGoodsId() 
    {
        return erpGoodsId;
    }
    public void setErpSpecId(Long erpSpecId) 
    {
        this.erpSpecId = erpSpecId;
    }

    public Long getErpSpecId() 
    {
        return erpSpecId;
    }

}
