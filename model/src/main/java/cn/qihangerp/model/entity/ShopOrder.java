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
 * 
 * @TableName oms_shop_order
 */
@TableName(value ="oms_shop_order")
@Data
public class ShopOrder implements Serializable {
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
    private Integer orderType;//订单类型：0普通订单，1螳螂电销订单，2螳螂网销订单
    private String orderSource;
    private Integer orderMode;

    /**
     * 平台订单id
     */
    private String orderId;

    /**
     * 平台订单创建时间，秒级时间戳
     */
    private Long orderTime;
    private String orderTimeText;

    /**
     * 平台订单更新时间，秒级时间戳
     */
    private Long updateTime;
    private String updateTimeText;

    /**
     * 订单状态0：新订单，1：待发货，2：已发货，3：已完成，11已取消；12退款中；13已关闭；21待付款；22锁定，29删除，101部分发货
     */
    private Integer orderStatus;

    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功
     */
    private Integer refundStatus;

    /**
     * 商品总价，单位为分
     */
    private Integer goodsAmount;

    /**
     * 订单金额，单位为分，order_price=original_order_price-discounted_price-deduction_price-change_down_price
     */
    private Integer orderAmount;

    /**
     * 运费，单位为分
     */
    private Integer freight;

    /**
     * 支付金额，单位：分
     */
    private Integer paymentAmount;
    /**
     * 支付方式：XYHF先用后付，WEIXIN微信支持，ALIPAY支付宝，reward奖品订单， points积分兑换
     */
    private String paymentMethod;
    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 优惠金额，单位为分
     */
    private Integer discountAmount;

    /**
     * 商家优惠金额，单位：分
     */
    private Integer sellerDiscount;

    /**
     * 平台优惠金额，单位：分
     */
    private Integer platformDiscount;
    /**
     * 达人(店员)优惠金额，单位为分
     */
    private Integer finderDiscount;
    /**
     * 改价
     */
    private Integer changePrice;

    /**
     * 积分抵扣金额，单位为分
     */
    private Integer deductionPrice;
    /**
     * '商家实收金额，单位为分
     */
    private Integer merchantReceievePrice;

    /**
     * 买家留言信息
     */
    private String buyerMemo;

    /**
     * 卖家留言信息
     */
    private String sellerMemo;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 街道
     */
    private String town;

    private String receiverId;

    /**
     * 收件人地址，不拼接省市区。加密
     */
    private String address;

    /**
     * 收件人姓名。订单状态为待发货状态，且订单未在审核中的情况下返回密文数据；
     */
    private String receiverName;

    /**
     * 收件人电话。订单状态为待发货状态，且订单未在审核中的情况下返回密文数据；
     */
    private String receiverPhone;
    private String encryptPostReceiver;
    private String encryptPostTel;
    private String encryptPostAddress;

    /**
     * 虚拟发货订单联系方式(deliver_method=1时返回)
     */
    private String virtualOrderTelNumber;

    /**
     * 发货完成时间，秒级时间戳
     */
    private Long shipDoneTime;
    /**
     * 订单完成时间
     */
    private Long finishTime;

    /**
     * 电子面单代发时的订单密文

     */
    private String ewaybillOrderCode;

    /**
     * 订单确认状态（0未确认1已确认）
     */
    private Integer confirmStatus;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * erp发货状态 0 待发货 1 部分发货 2全部发货
     */
    private Integer erpShipStatus;

    /**
     * ERP发货时间
     */
    private Date erpShipTime;
    private String erpShipCompany;
    private String erpShipCode;

    /**
     * 系统创建时间
     */
    private Date createOn;

    /**
     * 系统更新时间
     */
    private Date updateOn;

    private Long shopMemberId;
    private String platformUserId;
    private String platformAccount;
    private String platformOrderStatus;
    private String platformOrderStatusText;
    /**
     * 订单发货方式，0：普通物流，1：虚拟发货，
     */
    private Integer deliverMethod;
    private String cancelReason;
    private String logisticsPartnerCode;//商家物流编码
    private String logisticsOrderNo;//物流单号
    private Long latestDeliveryTime;//最晚发货时间

    /**
     * 平台卖家id
     */
    private String platformSellerId;
    private String platformSellerName;
    /**
     * 平台类型
     */
    private String platformType;
    /**
     * 视频号id
     */
    private String finderId;
    /**
     * 直播间id
     */
    private String liveId;
    /**
     * 下单场景：0未知 1其他 2	直播间 3 短视频 4商品分享 5商品橱窗主页 6公众号文章商品卡片
     */
    private Integer orderScene;

    private String openAddressId;

    @TableField(exist = false)
    private List<ShopOrderItem> items;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}