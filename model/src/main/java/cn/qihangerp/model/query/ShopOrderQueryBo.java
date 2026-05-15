package cn.qihangerp.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName wei_order
 */

@Data
public class ShopOrderQueryBo implements Serializable {

    private Long shopId;
    private Integer shopType;
    private Long merchantId;
    private Integer deliverMethod;//订单发货方式，0：普通物流，1：虚拟发货，
    private Integer orderMode;//订单模式0店铺订单1手工订单
    private int isAll;//1查询全部订单0只查询有绑定发货实物的商品订单
    /**
     * 订单号
     */
    private String orderId;
    private String goodsId;
    private String platformAccount;;


    /**
     * 状态
     */
    private Integer orderStatus;
    private Integer refundStatus;
    private String platformOrderStatus;
    private Integer confirmStatus;
    private String startTime;
    private String endTime;

    private static final long serialVersionUID = 1L;
}