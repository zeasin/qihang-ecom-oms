package cn.qihangerp.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单明细表
 * @TableName o_order_item
 */
@Data
public class OrderItemQuery implements Serializable {
    /**
     * id
     */
    private Long shopId;
    private Integer shopType;
    private Integer shipType;//发货类型（0本地仓库发货；100京东云仓发货；200系统云仓发货；300供应商发货）
    private Long shipSupplier;
    private Integer refundStatus;
    private Integer orderStatus;
    private Integer shipStatus;//0未处理1已分配发货2已发货10已取号
    private Integer hasPushErp;
    private String orderNum;
    private String startTime;
    private String endTime;

    /**
     * 第三方平台skuId
     */
    private String skuId;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品规格id
     */
    private Long erpSkuId;

    /**
     * 商品编码
     */
    private String goodsNum;

    /**
     * 商品规格编码
     */
    private String skuNum;

    private Integer hasLink;//是否关联ERP商品
    private static final long serialVersionUID = 1L;

    private Long merchantId;
}