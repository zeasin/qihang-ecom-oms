package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 店铺订单优惠明细表
 * @TableName oms_shop_order_promotion
 */
@TableName(value ="oms_shop_order_promotion")
@Data
public class ShopOrderPromotion {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺订单id
     */
    private Long shopOrderId;

    /**
     * 优惠信息的名称
     */
    private String promotionName;

    /**
     * 优惠金额（免运费、限时打折时为空）,单位：元
     */
    private String discountFee;

    /**
     * 优惠活动的描述
     */
    private String promotionDesc;

    /**
     * 优惠id，(由营销工具id、优惠活动id和优惠详情id组成，结构为：营销工具id-优惠活动id_优惠详情id，如mjs-123024_211143）
     */
    private String promotionId;

    /**
     * 创建时间
     */
    private Date createOn;
}