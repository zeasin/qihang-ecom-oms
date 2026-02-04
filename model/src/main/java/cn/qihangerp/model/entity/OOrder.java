package cn.qihangerp.model.entity;


import cn.qihangerp.model.vo.OrderDiscountVo;
import cn.qihangerp.model.vo.OrderItemListVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * OMS订单表
 * @TableName o_order
 */
@TableName(value ="o_order")
@Data
public class OOrder implements Serializable {
    /**
     * 订单id，自增
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 订单编号（第三方平台订单号）
     */
    private String orderNum;

    /**
     * 店铺类型
     */
    private Integer shopType;


    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 买家留言信息
     */
    private String buyerMemo;

    /**
     * 卖家留言信息
     */
    private String sellerMemo;

    /**
     * 标签
     */
    private String tag;

    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 
     */
    private Integer refundStatus;

    /**
     * 订单状态0：新订单，1：待发货，2：已发货，3：已完成，11已取消；12退款中；21待付款；22锁定，29删除，101部分发货
     */
    private Integer orderStatus;

    /**
     * 订单商品金额
     */
    private Double goodsAmount;

    /**
     * 订单运费
     */
    private Double postFee;

    /**
     * 商家优惠金额，单位：元
     */
    private Double sellerDiscount;

    /**
     * 平台优惠金额，单位：元
     */
    private Double platformDiscount;

    /**
     * 订单实际金额
     */
    private Double amount;

    /**
     * 实付金额
     */
    private Double payment;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人手机号
     */
    private String receiverMobile;

    /**
     * 收件人地址
     */
    private String address;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String town;

    /**
     * 订单时间
     */
    private Date orderTime;

    //发货人
    private Long shipper;
    /**
     * 发货方式 0 自己发货1联合发货2供应商发货
     */
    private Integer shipType;

    /**
     * 发货状态 0 待发货 1 部分发货 2全部发货
     */
    private Integer shipStatus;

    /**
     * 发货快递公司
     */
    private String shipCompany;

    /**
     * 发货物流公司
     */
    private String shipCode;

    /**
     * 发货时间
     */
    private Date shipTime;

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
    private String cancelReason;
    @TableField(exist = false)
    private List<OOrderItem> itemList;

//    @TableField(exist = false)
//    private List<OrderItemListVo> itemVoList;

    @TableField(exist = false)
    private List<OrderDiscountVo> discounts;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}