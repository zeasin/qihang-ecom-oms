package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * OMS商品SKU表
 * @TableName o_goods_sku
 */
@TableName(value ="o_goods_sku")
@Data
public class OGoodsSku implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 计价方式：0一口价；1金包银+工费；
     */
    private Integer priceType;

    /**
     * 外键（o_goods）
     */
    private String goodsId;

    /**
     * 外部erp系统商品id
     */
    private String outerErpGoodsId;

    /**
     * 外部erp系统skuId(唯一)
     */
    private String outerErpSkuId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 商品编码
     */
    private String goodsNum;
    private String unit;
    /**
     * 库存模式：0-传统SKU模式，1-一物一码模式（珠宝）
     */
    private Integer inventoryMode;

    /**
     * 规格名
     */
    private String skuName;

    /**
     * 规格编码
     */
    private String skuCode;

    /**
     * 颜色id
     */
    private Long colorId;

    /**
     * 颜色值
     */
    private String colorValue;

    /**
     * 颜色图片
     */
    private String colorImage;

    /**
     * 尺码id
     */
    private Long sizeId;

    /**
     * 尺码值(材质)
     */
    private String sizeValue;

    /**
     * 款式id
     */
    private Long styleId;

    /**
     * 款式值
     */
    private String styleValue;

    /**
     * 库存条形码
     */
    private String barCode;

    /**
     * 预计采购价格
     */
    private BigDecimal purPrice;

    /**
     * 建议零售价
     */
    private BigDecimal retailPrice;

    /**
     * 单位成本
     */
    private BigDecimal unitCost;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 最低库存（预警）
     */
    private Integer lowQty;

    /**
     * 最高库存（预警）
     */
    private Integer highQty;

    /**
     * 发货类型10自营发货20供应商发货
     */
    private Integer shipType;

    /**
     * 商品体积
     */
    private String volume;

    /**
     * 衣长
     */
    private Double length;

    /**
     * 高度
     */
    private Double height;

    /**
     * 宽度
     */
    private Double width;

    /**
     * 重量
     */
    private Double weight;
    /**
     * price_type=1启用，金重（g)
     */
    private Double weight1;
    /**
     * price_type=1启用，银重（g)
     */
    private Double weight2;
    /**
     * price_type=1启用，工时
     */
    private Double weight3;

    /**
     * 商户ID
     */
    private Long merchantId;
    private Long shopId;
    private String sellerId;//卖家ID(外部系统使用)
    private String sellerBrandId;//卖家品牌ID(外部系统使用)

    @TableField(exist=false)
    private Integer quantity=1;
    @TableField(exist=false)
    private Integer isGift=0;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}