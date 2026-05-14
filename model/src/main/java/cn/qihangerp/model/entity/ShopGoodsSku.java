package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 其他渠道店铺商品SKU
 * @TableName oms_shop_goods_sku
 */
@TableName(value ="oms_shop_goods_sku")
@Data
public class ShopGoodsSku implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 外键id
     */
    private Long shopGoodsId;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 店铺id
     */
    private Long shopId;
    @TableField(exist = false)
    private String shopName;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 平台商品id
     */
    private String productId;

    /**
     * 商品名
     */
    private String productTitle;

    /**
     * 商家商品编码
     */
    private String outerProductId;

    /**
     * skuID
     */
    private String skuId;

    /**
     * 商家自定义skuID。如果添加时没录入，回包可能不包含该字段
     */
    private String outerSkuId;

    /**
     * sku小图
     */
    private String img;

    /**
     * 售卖价格，以分为单位
     */
    private Integer price;

    /**
     * sku库存
     */
    private Integer stockNum;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku名
     */
    private String skuName;

    /**
     * sku状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long addTime;
    private Long modifyTime;

    /**
     * sku_attrs
     */
    private String skuAttrs;

    /**
     * sku库存
     */
    private Integer stock;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品skuid
     */
    private Long erpGoodsSkuId;

    /**
     * 创建时间
     */
    private Date createOn;

    /**
     * 更新时间
     */
    private Date updateOn;
    // 是否绑定有发货实物sku,0没有1有
    private Integer bindShipSku;
    @TableField(exist = false)
    private Integer quantity;
    @TableField(exist = false)
    private double itemAmount;
    @TableField(exist = false)
    private int isGift;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}