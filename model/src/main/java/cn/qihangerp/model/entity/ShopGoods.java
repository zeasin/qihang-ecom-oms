package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 其他渠道店铺商品
 * @TableName oms_shop_goods
 */
@TableName(value ="oms_shop_goods")
@Data
public class ShopGoods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 平台商品id
     */
    private String productId;

    /**
     * 商家编码id
     */
    private String outerProductId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 
     */
    private String subTitle;

    /**
     * 主图集合
     */
    private String imgs;

    /**
     * 第一张主图
     */
    private String img;

    /**
     * 商品详情字符串
     */
    private String descInfo;

    /**
     * 属性字符串
     */
    private String attrs;

    /**
     * 状态 0下架1上架2已售完3删除4违规下架
     */
    private Integer status;

    /**
     * 商品 SKU 最小价格（单位：分）
     */
    private Integer minPrice;

    /**
     * 市场价单位分
     */
    private Integer marketPrice;

    /**
     * 商品编码
     */
    private String spuCode;

    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 修改时间
     */
    private Long editTime;

    /**
     * 商品库商品id
     */
    private Long erpGoodsId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 系统创建时间
     */
    private Date createOn;

    /**
     * 系统更新时间
     */
    private Date updateOn;

    private Integer deliverMethod;
    // 是否绑定有发货实物sku,0没有1有
    private Integer bindShipSku;

    /**
     * 平台卖家id
     */
    @TableField(exist = false)
    private String platformSellerId;

    @TableField(exist = false)
    private List<ShopGoodsSku> skuList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}