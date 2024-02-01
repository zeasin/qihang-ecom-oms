package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 拼多多订单明细对象 s_pdd_order_item
 * 
 * @author qihang
 * @date 2024-01-02
 */
public class PddOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id，自增 */
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** erp系统商品id */
    @Excel(name = "erp系统商品id")
    private Long erpGoodsId;

    /** erp系统商品规格id */
    @Excel(name = "erp系统商品规格id")
    private Long erpSpecId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String goodsImage;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 商品规格 */
    @Excel(name = "商品规格")
    private String goodsSpec;

    /** 商品规格编码 */
    @Excel(name = "商品规格编码")
    private String specNum;

    /** 商品单价 */
    @Excel(name = "商品单价")
    private Double goodsPrice;

    /** 子订单金额 */
    @Excel(name = "子订单金额")
    private Double itemAmount;

    /** 商品数量 */
    @Excel(name = "商品数量")
    private Long quantity;

    /** 是否礼品0否1是 */
    @Excel(name = "是否礼品0否1是")
    private Long isGift;

    /** 拼多多商品id */
    @Excel(name = "拼多多商品id")
    private Long goodId;

    /** 拼多多商品skuid */
    @Excel(name = "拼多多商品skuid")
    private Long skuId;

    /** 已退货数量 */
    @Excel(name = "已退货数量")
    private Long refundCount;
    /** 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功  */
    private Long refundStatus;

    public Long getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Long refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
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
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setGoodsImage(String goodsImage) 
    {
        this.goodsImage = goodsImage;
    }

    public String getGoodsImage() 
    {
        return goodsImage;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
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
    public void setGoodsPrice(Double goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    public Double getGoodsPrice()
    {
        return goodsPrice;
    }
    public void setItemAmount(Double itemAmount)
    {
        this.itemAmount = itemAmount;
    }

    public Double getItemAmount()
    {
        return itemAmount;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setIsGift(Long isGift) 
    {
        this.isGift = isGift;
    }

    public Long getIsGift() 
    {
        return isGift;
    }
    public void setGoodId(Long goodId) 
    {
        this.goodId = goodId;
    }

    public Long getGoodId() 
    {
        return goodId;
    }
    public void setSkuId(Long skuId) 
    {
        this.skuId = skuId;
    }

    public Long getSkuId() 
    {
        return skuId;
    }
    public void setRefundCount(Long refundCount) 
    {
        this.refundCount = refundCount;
    }

    public Long getRefundCount() 
    {
        return refundCount;
    }

}
