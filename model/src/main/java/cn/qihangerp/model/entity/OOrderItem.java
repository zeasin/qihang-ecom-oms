package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * OMS订单明细表
 * @TableName o_order_item
 */
@TableName(value ="o_order_item")
@Data
public class OOrderItem implements Serializable {
    /**
     * id，自增
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 订单ID（o_order外键）
     */
    private String orderId;

    /**
     * 订单号（第三方平台）
     */
    private String orderNum;

    /**
     * 子订单号（第三方平台）
     */
    private String subOrderNum;

    /**
     * 第三方平台skuId
     */
    private String skuId;

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
    private String goodsTitle;

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
    private String goodsSpec;

    /**
     * 商品规格编码
     */
    private String skuNum;

    /**
     * 商品单价
     */
    private Double goodsPrice;

    /**
     * 子订单金额
     */
    private Double itemAmount;

    /**
     * 子订单优惠金额
     */
    private Double discountAmount;

    /**
     * 实际支付金额
     */
    private Double payment;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已退货数量
     */
    private Integer refundCount;

    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 
     */
    private Integer refundStatus;

    /**
     * 订单状态1：待发货，2：已发货，3：已完成，11已取消；21待付款
     */
    private Integer orderStatus;

    //发货人
    private Long shipper;
    /**
     * 发货方式 0 自己发货1联合发货2供应商发货
     */
    private Integer shipType;//发货方式1电子面单发货2手动发货

    /**
     * 发货状态 0 待发货 1 已发货
     */
    private Integer shipStatus;

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

    /**
     * 更新人
     */
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}