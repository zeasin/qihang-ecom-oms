package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName oms_shop_order_item
 */
@TableName(value ="oms_shop_order_item")
@Data
public class ShopOrderItem implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 外键id
     */
    private Long shopOrderId;

    /**
     * 商品spuid
     */
    private String productId;

    /**
     * 商品skuid

     */
    private String skuId;

    /**
     * sku图
     */
    private String img;

    /**
     * sku数量
     */
    private Integer quantity;

    /**
     * 售卖单价（单位：分）
     */
    private Integer salePrice;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 正在售后/退款流程中的 sku 数量
     */
    private Integer onAftersaleSkuCnt;

    /**
     * 完成售后/退款的 sku 数量
     */
    private Integer finishAftersaleSkuCnt;

    /**
     * 商品编码
     */
    private String skuCode;
    /**
     * 条形码
     */
    private String barcode;

    /**
     * 市场单价（单位：分）
     */
    private Integer marketPrice;

    /**
     * sku属性名称
     */
    private String skuName;

    /**
     * sku实付总价，取estimate_price和change_price中较小值
     */
    private Integer realPrice;

    /**
     * 商品外部spuid
     */
    private String outerProductId;

    /**
     * 商品外部skuid
     */
    private String outerSkuId;

    /**
     * 是否有优惠金额，非必填，默认为false
     */
    private String isDiscounted;

    /**
     * 优惠后sku总价，非必填，is_discounted为true时有值
     */
    private Integer itemAmount;

    /**
     * 是否修改过价格，非必填，默认为false
     */
    private String isChangePrice;

    /**
     * 改价后sku总价，非必填，is_change_price为true时有值
     */
    private Integer changePrice;
    /**
     * 卖家优惠金额
     */
    private Integer sellerDiscount;
    /**
     * 达人优惠
     */
    private Integer finderDiscount;
    /**
     * 积分抵扣金额，单位为分
     */
    private Integer deductionPrice;

    /**
     * 区域库存id
     */
    private String outWarehouseId;

    /**
     * 优惠金额，单位为分
     */
    private Integer discountAmount;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品规格id
     */
    private Long erpGoodsSkuId;

    /**
     * 订单号
     */
    private String orderId;
    /**
     * 子订单号
     */
    private String subOrderId;

    /**
     * 下单时间
     */
    private Long orderTime;

    /**
     * 创建时间
     */
    private Date createOn;

    /**
     * 更新时间
     */
    private Date updateOn;
    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功
     */
    private Integer refundStatus;
    /**
     * 订单状态0：新订单，1：待发货，2：已发货，3：已完成，11已取消；12退款中；21待付款；22锁定，29删除，101部分发货
     */
    private Integer orderStatus;
    /**
     * 发货状态 0：待发货 1：部分发货，2：全部发货
     */
    private Integer shipStatus;
    private Integer refundAmount;
    private Integer payment;
    private String updateBy;
    private String shippingType;
    private String logisticsCompany;
    private String logisticsCode;
    /**
     * 是否是赠品：0否；1是；
     */
    private Integer isGift;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}