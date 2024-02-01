package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 淘宝退款订单对象 s_tao_order_refund
 * 
 * @author qihang
 * @date 2024-01-13
 */
public class TaoOrderRefund extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 退款id */
    @Excel(name = "退款id")
    private String refundId;

    /** 类型（1退货3换货） */
    @Excel(name = "类型", readConverterExp = "1=退货3换货")
    private Long afterSalesType;

    /** 店铺id */
    @Excel(name = "店铺id")
    private Long shopId;

    /** 淘宝交易单号（订单号） */
    @Excel(name = "淘宝交易单号", readConverterExp = "订=单号")
    private Long tid;

    /** 子订单号。如果是单笔交易oid会等于tid */
    @Excel(name = "子订单号。如果是单笔交易oid会等于tid")
    private Long oid;

    /** 退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分 */
    @Excel(name = "退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分")
    private BigDecimal refundFee;

    /** 退款申请时间 */
    @Excel(name = "退款申请时间")
    private Long created;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private Long modified;

    /** 退款状态。可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功) */
    @Excel(name = "退款状态。可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)")
    private String status;

    /** 货物状态。可选值BUYER_NOT_RECEIVED (买家未收到货) BUYER_RECEIVED (买家已收到货) BUYER_RETURNED_GOODS (买家已退货) */
    @Excel(name = "货物状态。可选值BUYER_NOT_RECEIVED (买家未收到货) BUYER_RECEIVED (买家已收到货) BUYER_RETURNED_GOODS (买家已退货)")
    private String goodStatus;

    /** 退货数量 */
    @Excel(name = "退货数量")
    private Long num;

    /** 买家是否需要退货。可选值:true(是),false(否) */
    @Excel(name = "买家是否需要退货。可选值:true(是),false(否)")
    private Integer hasGoodReturn;

    /** 退款原因 */
    @Excel(name = "退款原因")
    private String reason;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String logisticsCompany;

    /** 物流单号 */
    @Excel(name = "物流单号")
    private String logisticsCode;

    /** 买家发货时间 */
    @Excel(name = "买家发货时间")
    private String sendTime;

    /** 2已签收9供应商已退款 */
    @Excel(name = "2已签收9供应商已退款")
    private Long auditStatus;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receivedTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long erpGoodsId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long erpGoodsSpecId;

    /** 天猫的商品Id */
    @Excel(name = "天猫的商品Id")
    private Long productId;

    /** 天猫的SKUID */
    @Excel(name = "天猫的SKUID")
    private Long skuId;

    /** 商品标题 */
    @Excel(name = "商品标题")
    private String goodsTitle;

    /** 商品货号，对应系统商品编码 */
    @Excel(name = "商品货号，对应系统商品编码")
    private String goodsNumber;

    /** sku编号 */
    @Excel(name = "sku编号")
    private String specNumber;

    /** 商品主图 */
    @Excel(name = "商品主图")
    private String productImgUrl;

    /** SKU字符串 */
    @Excel(name = "SKU字符串")
    private String skuInfo;

    /** 退款阶段，可选值：onsale/aftersale */
    @Excel(name = "退款阶段，可选值：onsale/aftersale")
    private String refundPhase;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRefundId(String refundId) 
    {
        this.refundId = refundId;
    }

    public String getRefundId() 
    {
        return refundId;
    }
    public void setAfterSalesType(Long afterSalesType) 
    {
        this.afterSalesType = afterSalesType;
    }

    public Long getAfterSalesType() 
    {
        return afterSalesType;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setTid(Long tid) 
    {
        this.tid = tid;
    }

    public Long getTid() 
    {
        return tid;
    }
    public void setOid(Long oid) 
    {
        this.oid = oid;
    }

    public Long getOid() 
    {
        return oid;
    }
    public void setRefundFee(BigDecimal refundFee) 
    {
        this.refundFee = refundFee;
    }

    public BigDecimal getRefundFee() 
    {
        return refundFee;
    }
    public void setCreated(Long created) 
    {
        this.created = created;
    }

    public Long getCreated() 
    {
        return created;
    }
    public void setModified(Long modified) 
    {
        this.modified = modified;
    }

    public Long getModified() 
    {
        return modified;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setGoodStatus(String goodStatus) 
    {
        this.goodStatus = goodStatus;
    }

    public String getGoodStatus() 
    {
        return goodStatus;
    }
    public void setNum(Long num) 
    {
        this.num = num;
    }

    public Long getNum() 
    {
        return num;
    }
    public void setHasGoodReturn(Integer hasGoodReturn)
    {
        this.hasGoodReturn = hasGoodReturn;
    }

    public Integer getHasGoodReturn()
    {
        return hasGoodReturn;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
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
    public void setSendTime(String sendTime) 
    {
        this.sendTime = sendTime;
    }

    public String getSendTime() 
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
    public void setAuditTime(Date auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() 
    {
        return auditTime;
    }
    public void setReceivedTime(Date receivedTime) 
    {
        this.receivedTime = receivedTime;
    }

    public Date getReceivedTime() 
    {
        return receivedTime;
    }
    public void setErpGoodsId(Long erpGoodsId) 
    {
        this.erpGoodsId = erpGoodsId;
    }

    public Long getErpGoodsId() 
    {
        return erpGoodsId;
    }
    public void setErpGoodsSpecId(Long erpGoodsSpecId) 
    {
        this.erpGoodsSpecId = erpGoodsSpecId;
    }

    public Long getErpGoodsSpecId() 
    {
        return erpGoodsSpecId;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setSkuId(Long skuId) 
    {
        this.skuId = skuId;
    }

    public Long getSkuId() 
    {
        return skuId;
    }
    public void setGoodsTitle(String goodsTitle) 
    {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsTitle() 
    {
        return goodsTitle;
    }
    public void setGoodsNumber(String goodsNumber) 
    {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsNumber() 
    {
        return goodsNumber;
    }
    public void setSpecNumber(String specNumber) 
    {
        this.specNumber = specNumber;
    }

    public String getSpecNumber() 
    {
        return specNumber;
    }
    public void setProductImgUrl(String productImgUrl) 
    {
        this.productImgUrl = productImgUrl;
    }

    public String getProductImgUrl() 
    {
        return productImgUrl;
    }
    public void setSkuInfo(String skuInfo) 
    {
        this.skuInfo = skuInfo;
    }

    public String getSkuInfo() 
    {
        return skuInfo;
    }
    public void setRefundPhase(String refundPhase) 
    {
        this.refundPhase = refundPhase;
    }

    public String getRefundPhase() 
    {
        return refundPhase;
    }

}
