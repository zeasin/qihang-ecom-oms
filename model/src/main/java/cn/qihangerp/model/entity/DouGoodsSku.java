package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 抖店商品Sku表
 * @TableName oms_dou_goods_sku
 */
@TableName(value ="oms_dou_goods_sku")
@Data
public class DouGoodsSku implements Serializable {
    /**
     * 商品sku_id;抖店系统生成。
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 商品ID；抖店系统生成。
     */
    private Long productId;

    /**
     * 商品标题。
     */
    private String name;

    /**
     * 商品主图的第一张图
     */
    private String img;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * sku外部编码
     */
    private String code;

    /**
     * 库存类型；0-普通；1-区域库存；10-阶梯库存；
     */
    private Integer skuType;

    /**
     * sku状态 true上架 false下架
     */
    private String skuStatus;

    /**
     * 外部的skuId；商家自定义；未设置返回为0。
     */
    private Long outSkuId;

    /**
     * 第一级子规格
     */
    private Long specDetailId1;

    /**
     * 第二级子规格
     */
    private Long specDetailId2;

    /**
     * 
第三级子规格
     */
    private Long specDetailId3;

    /**
     * 第一级子规格名
     */
    private String specDetailName1;

    /**
     * 第二级子规格名
     */
    private String specDetailName2;

    /**
     * 
第三级子规格名
     */
    private String specDetailName3;

    /**
     * 商品价格，单位：分
     */
    private Integer price;

    /**
     * 创建时间，时间戳：单位秒；
     */
    private Integer createTime;

    /**
     * ku_type=0时，表示普通库存数量 ;sku_type=1时，使用stock_map，表示区域库存数量
     */
    private Integer stockNum;

    /**
     * sku_type=0时，表示预占库存数量； sku_type=1时，表示区域库存数量，使用prehold_stock_map
     */
    private Integer preholdStockNum;

    /**
     * 活动库存
     */
    private Integer promStockNum;

    /**
     * 
阶梯库存
     */
    private Integer stepStockNum;

    /**
     * 预占阶梯库存
     */
    private Integer preholdStepStockNum;

    /**
     * 活动阶梯库存
     */
    private Integer promStepStockNum;

    /**
     * 库存模型V2新增 普通库存 非活动可售
     */
    private Integer normalStockNum;

    /**
     * 库存模型V2新增 渠道库存
     */
    private Integer channelStockNum;

    /**
     * 销售属性，代替spec_detail_id123、spec_detail_name123
     */
    private String sellProperties;

    /**
     * 商品id(o_goods外键)
     */
    private Long erpGoodsId;

    /**
     * 商品skuid(o_goods_sku外键)
     */
    private Long erpGoodsSkuId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 拉取时间
     */
    private Date pullTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}