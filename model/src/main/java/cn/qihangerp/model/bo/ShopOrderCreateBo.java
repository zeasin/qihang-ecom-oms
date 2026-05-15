package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单对象 erp_order
 *
 * @author qihang
 * @date 2024-01-05
 */
@Data
public class ShopOrderCreateBo
{

    /** 订单编号 */
    private String orderNum;

    /** 店铺ID */
    private Long shopId;


    /** 买家留言信息 */
    private String buyerMemo;

    /** 备注 */
    private String remark;
    private String orderDate;


    /** 商品金额 */
    private BigDecimal goodsAmount;
    // 订单金额
//    private BigDecimal orderAmount;

    /** 卖家优惠金额 */
//    private BigDecimal sellerDiscount;


    /** 运费 */
    private BigDecimal postage;
    /**
     * 手动改价
     */
    private BigDecimal changePrice;
    /**
     * 优惠折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 折扣ID
     */
    private Long discountId;

    /**
     * 抵扣金额
     */
    private BigDecimal deductionPrice;

    /**
     * 会员id
     */
    private Long shopMemberId;

    /** 收件人姓名 */
    private String receiverName;

    /** 收件人手机号 */
    private String receiverPhone;

    /** 收件人地址 */
    private String address;


    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String town;

    /**
     * 支付方式
     */
    private  String payMethod;

    /**
     * 是否锁库（0不锁库存 1锁库存）
     */
    private Integer isLock;

    /**
     * 销售员ID
     */
    private Long finderId;

    /****抵扣***/
    /**
     * 是否回收抵扣（0否1是）
     */
    private Integer isRecoveryDeduction;
    /**
     * 金重(g)
     */
    private BigDecimal goldWeight;
    /**
     * 金价
     */
    private BigDecimal goldPrice;
    /**
     * 银重(g)
     */
    private BigDecimal silverWeight;
    /**
     * 银价
     */
    private BigDecimal silverPrice;
    /**
     * 抵扣备注
     */
    private String deductionRemark;

    /** 订单明细信息 */
    private List<ShopOrderCreateItemBo> itemList;

}
