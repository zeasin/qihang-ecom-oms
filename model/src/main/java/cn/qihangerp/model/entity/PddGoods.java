package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * pdd商品表
 * @TableName oms_pdd_goods
 */
@TableName(value ="oms_pdd_goods")
@Data
public class PddGoods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品编码
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品总数量
     */
    private Long goodsQuantity;

    /**
     * 商家外部编码（goods）
     */
    private String outerGoodsId;

    /**
     * 是否多sku，0-单sku，1-多sku
     */
    private Integer isMoreSku;

    /**
     * 是否在架上，0-下架中，1-架上
     */
    private Integer isOnsale;

    /**
     * 商品缩略图
     */
    private String thumbUrl;

    /**
     * 市场价，单位分
     */
    private Long marketPrice;

    /**
     * 商品创建时间的时间戳
     */
    private Long createdAt;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * erp商品id
     */
    private Long erpGoodsId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    @TableField(exist = false)
    private List<PddGoodsSku> skuList;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}