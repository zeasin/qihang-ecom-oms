package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 抖店订单明细对象 s_dou_order_item
 * 
 * @author qihang
 * @date 2024-01-02
 */
public class DouOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id，自增 */
    private Long id;

    /** 订单主键ID */
    @Excel(name = "订单主键ID")
    private Long douyinOrderId;

    /** 抖音订单id */
    @Excel(name = "抖音订单id")
    private String orderId;

    /** 子订单id */
    @Excel(name = "子订单id")
    private String subOrderId;

    /** 该子订单购买的商品id */
    @Excel(name = "该子订单购买的商品id")
    private String productId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 商品图片 (spu维度的商品主图) */
    @Excel(name = "商品图片 (spu维度的商品主图)")
    private String productPic;

    /** 该子订单购买的商品 sku_id */
    @Excel(name = "该子订单购买的商品 sku_id")
    private String comboId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNum;

    /** 该子订单购买的商品的编码 code */
    @Excel(name = "该子订单购买的商品的编码 code")
    private String specNum;

    /** 该子订单所属商品规格描述 */
    @Excel(name = "该子订单所属商品规格描述")
    private String goodsSpec;

    /** 该子订单所购买的sku的数量 */
    @Excel(name = "该子订单所购买的sku的数量")
    private Long comboNum;

    /** 邮费金额 (单位: 分) */
    @Excel(name = "邮费金额 (单位: 分)")
    private BigDecimal postAmount;

    /** 平台优惠券金额 (单位: 分) */
    @Excel(name = "平台优惠券金额 (单位: 分)")
    private BigDecimal couponAmount;

    /** 优惠券id */
    @Excel(name = "优惠券id")
    private Long couponMetaId;

    /** 优惠券详情 (type为优惠券类型, credit为优惠金额,单位分) */
    @Excel(name = "优惠券详情 (type为优惠券类型, credit为优惠金额,单位分)")
    private String couponInfo;

    /** 活动细则 (活动可能会导致商品成交价combo_amount变成活动sku价格 ,活动campaign_info字段中的title为活动标题) */
    @Excel(name = "活动细则 (活动可能会导致商品成交价combo_amount变成活动sku价格 ,活动campaign_info字段中的title为活动标题)")
    private String campaignInfo;

    /** 该子订单总金额 (单位: 分) */
    @Excel(name = "该子订单总金额 (单位: 分)")
    private BigDecimal totalAmount;

    /** 是否评价 (1:已评价) */
    @Excel(name = "是否评价 (1:已评价)")
    private Long isComment;

    /** erp系统商品id */
    @Excel(name = "erp系统商品id")
    private Long erpGoodsId;

    /** erp系统商品规格id */
    @Excel(name = "erp系统商品规格id")
    private Long erpSpecId;

    /** 商品单价 */
    @Excel(name = "商品单价")
    private BigDecimal price;

    /** 是否赠品0否1是 */
    @Excel(name = "是否赠品0否1是")
    private Long isGift;

    /** 子订单状态4已取消2已退货 */
    @Excel(name = "子订单状态4已取消2已退货")
    private Long itemStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDouyinOrderId(Long douyinOrderId) 
    {
        this.douyinOrderId = douyinOrderId;
    }

    public Long getDouyinOrderId() 
    {
        return douyinOrderId;
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
    public void setProductPic(String productPic) 
    {
        this.productPic = productPic;
    }

    public String getProductPic() 
    {
        return productPic;
    }
    public void setComboId(String comboId) 
    {
        this.comboId = comboId;
    }

    public String getComboId() 
    {
        return comboId;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setSpecNum(String specNum) 
    {
        this.specNum = specNum;
    }

    public String getSpecNum() 
    {
        return specNum;
    }
    public void setGoodsSpec(String goodsSpec) 
    {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsSpec() 
    {
        return goodsSpec;
    }
    public void setComboNum(Long comboNum) 
    {
        this.comboNum = comboNum;
    }

    public Long getComboNum() 
    {
        return comboNum;
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
    public void setCouponMetaId(Long couponMetaId) 
    {
        this.couponMetaId = couponMetaId;
    }

    public Long getCouponMetaId() 
    {
        return couponMetaId;
    }
    public void setCouponInfo(String couponInfo) 
    {
        this.couponInfo = couponInfo;
    }

    public String getCouponInfo() 
    {
        return couponInfo;
    }
    public void setCampaignInfo(String campaignInfo) 
    {
        this.campaignInfo = campaignInfo;
    }

    public String getCampaignInfo() 
    {
        return campaignInfo;
    }
    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }
    public void setIsComment(Long isComment) 
    {
        this.isComment = isComment;
    }

    public Long getIsComment() 
    {
        return isComment;
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
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setIsGift(Long isGift) 
    {
        this.isGift = isGift;
    }

    public Long getIsGift() 
    {
        return isGift;
    }
    public void setItemStatus(Long itemStatus) 
    {
        this.itemStatus = itemStatus;
    }

    public Long getItemStatus() 
    {
        return itemStatus;
    }


}
