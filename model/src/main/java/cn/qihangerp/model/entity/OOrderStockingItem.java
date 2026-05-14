package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName o_supplier_ship_order_item
 */
@Data
public class OOrderStockingItem implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商发货订单id
     */
    private Long shipOrderId;

    /**
     * 订单号
     */
    private String orderNum;
    //子订单号
    private String subOrderNum;

    /**
     * o_order_id
     */
    private Long oOrderId;

    private Long oOrderItemId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 第三方平台skuId
     */
    private String skuId;
    /**
     * 第三方平台skuId
     */
    private String productId;

    /**
     * 商品id(o_goods外键)
     */
    private Long goodsId;

    /**
     * 商品skuid(o_goods_sku外键)
     */
    private Long goodsSkuId;

    /**
     * 商品标题
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品编码
     */
    private String goodsNum;

    /**
     * 商品规格
     */
    private String skuName;

    /**
     * 商品规格编码
     */
    private String skuCode;
    private String barcode;

    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 未发货数量
     */
    private Integer unshippedQuantity;


    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String sendStatusStr;
    /**
     * 发货状态 0：待发货 1：部分发货，2：全部发货
     */
    private Integer sendStatus;
    //状态0待备货(待出库)1部分备货(出库中)2全部备货(已出库)
    private Integer stockingStatus;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    private LocalDateTime orderTime;

    /**
     * 更新人
     */
    private String updateBy;
    private String waybillCode;
    private Integer waybillStatus;
    private Integer refundStatus;
    private Long merchantId;
    private String warehouseType;
    private Long warehouseId;
    private String warehouseName;
    
    /**
     * 仓库商品ID（erp_warehouse_goods.id）
     */
    private Long warehouseGoodsId;

    @TableField(exist = false)
    private String refundStatusStr;

    @TableField(exist = false)
    private String orderTimeStr;

    @TableField(exist = false)
    private Integer shipQuantity;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}