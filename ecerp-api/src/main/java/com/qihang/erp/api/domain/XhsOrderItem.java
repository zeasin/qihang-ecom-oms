package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 小红书订单明细对象 s_xhs_order_item
 * 
 * @author qihang
 * @date 2024-01-03
 */
public class XhsOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 商品id */
    @Excel(name = "商品id")
    private String itemId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String itemName;

    /** 商家编码(若为组合品，暂不支持组合品的商家编码，但skulist会返回子商品商家编码) */
    @Excel(name = "商家编码(若为组合品，暂不支持组合品的商家编码，但skulist会返回子商品商家编码)")
    private String erpCode;

    private String itemSpecCode;

    /** 规格 */
    @Excel(name = "规格")
    private String itemSpec;

    /** 商品图片url */
    @Excel(name = "商品图片url")
    private String itemImage;
    private BigDecimal price;
    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 总支付金额（考虑总件数）商品总实付 */
    @Excel(name = "总支付金额", readConverterExp = "考=虑总件数")
    private Long totalPaidAmount;
    private Double itemAmount;

    /** 商家承担总优惠 */
    @Excel(name = "商家承担总优惠")
    private Long totalMerchantDiscount;

    /** 平台承担总优惠 */
    @Excel(name = "平台承担总优惠")
    private Long totalRedDiscount;

    /** 是否赠品，1 赠品 0 普通商品 */
    @Excel(name = "是否赠品，1 赠品 0 普通商品")
    private Long itemTag;

    /** ERP发货状态0待处理1出库中2已出库3已发货 */
    @Excel(name = "ERP发货状态0待处理1出库中2已出库3已发货")
    private Long erpSendStatus;

    private Long erpGoodsId;
    private Long erpGoodsSpecId;

    public Double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(Double itemAmount) {
        this.itemAmount = itemAmount;
    }

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

    public String getItemSpecCode() {
        return itemSpecCode;
    }

    public void setItemSpecCode(String itemSpecCode) {
        this.itemSpecCode = itemSpecCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
    public void setItemId(String itemId) 
    {
        this.itemId = itemId;
    }

    public String getItemId() 
    {
        return itemId;
    }
    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public void setItemSpec(String itemSpec)
    {
        this.itemSpec = itemSpec;
    }

    public String getItemSpec() 
    {
        return itemSpec;
    }
    public void setItemImage(String itemImage) 
    {
        this.itemImage = itemImage;
    }

    public String getItemImage() 
    {
        return itemImage;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }
    public void setTotalPaidAmount(Long totalPaidAmount) 
    {
        this.totalPaidAmount = totalPaidAmount;
    }

    public Long getTotalPaidAmount() 
    {
        return totalPaidAmount;
    }
    public void setTotalMerchantDiscount(Long totalMerchantDiscount) 
    {
        this.totalMerchantDiscount = totalMerchantDiscount;
    }

    public Long getTotalMerchantDiscount() 
    {
        return totalMerchantDiscount;
    }
    public void setTotalRedDiscount(Long totalRedDiscount) 
    {
        this.totalRedDiscount = totalRedDiscount;
    }

    public Long getTotalRedDiscount() 
    {
        return totalRedDiscount;
    }
    public void setItemTag(Long itemTag) 
    {
        this.itemTag = itemTag;
    }

    public Long getItemTag() 
    {
        return itemTag;
    }
    public void setErpSendStatus(Long erpSendStatus) 
    {
        this.erpSendStatus = erpSendStatus;
    }

    public Long getErpSendStatus() 
    {
        return erpSendStatus;
    }

}
