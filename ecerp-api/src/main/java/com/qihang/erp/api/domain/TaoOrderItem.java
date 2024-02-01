package com.qihang.erp.api.domain;

import java.math.BigDecimal;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 淘宝订单明细对象 s_tao_order_item
 * 
 * @author qihang
 * @date 2024-01-03
 */
public class TaoOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 订单id */
    @Excel(name = "订单id")
    private String orderId;

    /** 天猫子订单id */
    @Excel(name = "天猫子订单id")
    private String subItemId;

    /** 明细总金额 */
    @Excel(name = "明细总金额")
    private BigDecimal itemAmount;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountFee;

    /** 手工调整金额 */
    @Excel(name = "手工调整金额")
    private BigDecimal adjustFee;

    /** 商品标题 */
    @Excel(name = "商品标题")
    private String goodsTitle;

    /** 商品货号，对应系统商品编码 */
    @Excel(name = "商品货号，对应系统商品编码")
    private String goodsNumber;

    /** 商品主图 */
    @Excel(name = "商品主图")
    private String productImgUrl;

    /** 商品链接 */
    @Excel(name = "商品链接")
    private String productUrl;

    /** 天猫的商品Id */
    @Excel(name = "天猫的商品Id")
    private Long productId;

    /** 天猫的SKUID */
    @Excel(name = "天猫的SKUID")
    private Long skuId;

    /** 单品货号，对应系统sku编码 */
    @Excel(name = "单品货号，对应系统sku编码")
    private String specNumber;

    /** SKU字符串 */
    @Excel(name = "SKU字符串")
    private String skuInfo;

    /** 实际单价 */
    @Excel(name = "实际单价")
    private BigDecimal price;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 子订单状态 */
    @Excel(name = "子订单状态")
    private String status;

    /** 子订单状态 */
    @Excel(name = "子订单状态")
    private String statusStr;

    /** 退款状态0无售后1售后中 */
    @Excel(name = "退款状态0无售后1售后中")
    private Long refundStatus;

    /** 退款状态 */
    @Excel(name = "退款状态")
    private String refundStatusStr;

    /** 退款金额 */
    @Excel(name = "退款金额")
    private Long refundAmount;

    /** 退款单id */
    @Excel(name = "退款单id")
    private Long refundId;

    /** 1 未发货 2 已发货 3 已收货 4 已经退货 5 部分发货 8 还未创建物流订单 */
    @Excel(name = "1 未发货 2 已发货 3 已收货 4 已经退货 5 部分发货 8 还未创建物流订单")
    private Long logisticsStatus;

    /** 确认订单最新规格id */
    @Excel(name = "确认订单最新规格id")
    private Long newSpecId;

    /** 确认订单最新规格编码 */
    @Excel(name = "确认订单最新规格编码")
    private String newSpecNumber;

    /** 售后状态0未申请售后1售后申请中(退款待审核)2同意退货(退款待收货)3买家已发货，待收货(待收货)4已收货（待退款）5退款退货成功(退款完成)6退款拒绝7已确认收货，正在退款中 8退款取消 */
    @Excel(name = "售后状态0未申请售后1售后申请中(退款待审核)2同意退货(退款待收货)3买家已发货，待收货(待收货)4已收货", readConverterExp = "待=退款")
    private Long afterSaleState;

    /** erp系统商品id */
    @Excel(name = "erp系统商品id")
    private Long erpGoodsId;

    /** erp系统商品规格id */
    @Excel(name = "erp系统商品规格id")
    private Long erpGoodsSpecId;

    public Long getErpGoodsId() {
        return erpGoodsId;
    }

    public void setErpGoodsId(Long erpGoodsId) {
        this.erpGoodsId = erpGoodsId;
    }

    public Long getErpGoodsSpecId() {
        return erpGoodsSpecId;
    }

    public void setErpGoodsSpecId(Long erpGoodsSpecId) {
        this.erpGoodsSpecId = erpGoodsSpecId;
    }

    /** 是否礼品0否1是 */
    @Excel(name = "是否礼品0否1是")
    private Integer isGift;

    /** 是否换货(0:否1:是) */
    @Excel(name = "是否换货(0:否1:是)")
    private Integer isSwap;

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
    public void setSubItemId(String subItemId)
    {
        this.subItemId = subItemId;
    }

    public String getSubItemId()
    {
        return subItemId;
    }
    public void setItemAmount(BigDecimal itemAmount) 
    {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getItemAmount() 
    {
        return itemAmount;
    }
    public void setDiscountFee(BigDecimal discountFee) 
    {
        this.discountFee = discountFee;
    }

    public BigDecimal getDiscountFee() 
    {
        return discountFee;
    }
    public void setAdjustFee(BigDecimal adjustFee) 
    {
        this.adjustFee = adjustFee;
    }

    public BigDecimal getAdjustFee() 
    {
        return adjustFee;
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
    public void setProductImgUrl(String productImgUrl) 
    {
        this.productImgUrl = productImgUrl;
    }

    public String getProductImgUrl() 
    {
        return productImgUrl;
    }
    public void setProductUrl(String productUrl) 
    {
        this.productUrl = productUrl;
    }

    public String getProductUrl() 
    {
        return productUrl;
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
    public void setSpecNumber(String specNumber) 
    {
        this.specNumber = specNumber;
    }

    public String getSpecNumber() 
    {
        return specNumber;
    }
    public void setSkuInfo(String skuInfo) 
    {
        this.skuInfo = skuInfo;
    }

    public String getSkuInfo() 
    {
        return skuInfo;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setStatusStr(String statusStr) 
    {
        this.statusStr = statusStr;
    }

    public String getStatusStr() 
    {
        return statusStr;
    }
    public void setRefundStatus(Long refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public Long getRefundStatus() 
    {
        return refundStatus;
    }
    public void setRefundStatusStr(String refundStatusStr) 
    {
        this.refundStatusStr = refundStatusStr;
    }

    public String getRefundStatusStr() 
    {
        return refundStatusStr;
    }
    public void setRefundAmount(Long refundAmount) 
    {
        this.refundAmount = refundAmount;
    }

    public Long getRefundAmount() 
    {
        return refundAmount;
    }
    public void setRefundId(Long refundId) 
    {
        this.refundId = refundId;
    }

    public Long getRefundId() 
    {
        return refundId;
    }
    public void setLogisticsStatus(Long logisticsStatus) 
    {
        this.logisticsStatus = logisticsStatus;
    }

    public Long getLogisticsStatus() 
    {
        return logisticsStatus;
    }
    public void setNewSpecId(Long newSpecId) 
    {
        this.newSpecId = newSpecId;
    }

    public Long getNewSpecId() 
    {
        return newSpecId;
    }
    public void setNewSpecNumber(String newSpecNumber) 
    {
        this.newSpecNumber = newSpecNumber;
    }

    public String getNewSpecNumber() 
    {
        return newSpecNumber;
    }
    public void setAfterSaleState(Long afterSaleState) 
    {
        this.afterSaleState = afterSaleState;
    }

    public Long getAfterSaleState() 
    {
        return afterSaleState;
    }
//    public void setErpGoodsId(Long erpGoodsId)
//    {
//        this.erpGoodsId = erpGoodsId;
//    }
//
//    public Long getErpGoodsId()
//    {
//        return erpGoodsId;
//    }
//    public void setErpGoodsSpecId(Long erpGoodsSpecId)
//    {
//        this.erpGoodsSpecId = erpGoodsSpecId;
//    }
//
//    public Long getErpGoodsSpecId()
//    {
//        return erpGoodsSpecId;
//    }
    public void setIsGift(Integer isGift) 
    {
        this.isGift = isGift;
    }

    public Integer getIsGift() 
    {
        return isGift;
    }
    public void setIsSwap(Integer isSwap) 
    {
        this.isSwap = isSwap;
    }

    public Integer getIsSwap() 
    {
        return isSwap;
    }


}
