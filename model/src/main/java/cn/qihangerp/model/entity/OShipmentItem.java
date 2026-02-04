package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 发货明细表
 * @TableName erp_shipment_item
 */
@TableName(value ="o_shipment_item")
@Data
public class OShipmentItem implements Serializable {
    /**
     * id，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发货id
     */
    private Long shipmentId;

    /**
     * 发货方 0 仓库发货 1 供应商发货
     */
    private Integer shipper;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单 id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 订单itemID（o_order_item外键）
     */
    private Long orderItemId;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品规格id
     */
    private Long erpSkuId;

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
     * 商品数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 仓库状态 0 备货中 1 已出库 2 已发走
     */
    private Integer stockStatus;

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