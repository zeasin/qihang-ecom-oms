package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 发货记录表
 * @TableName erp_shipment
 */
@TableName(value ="o_shipment")
@Data
public class OShipment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * o_order表id
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
     * 发货类型（1订单发货2商品补发3商品换货）
     */
    private Integer shipType;

    /**
     * 物流公司
     */
    private String shipCompany;

    /**
     * 物流公司code
     */
    private String shipCompanyCode;

    /**
     * 物流单号
     */
    private String shipCode;

    /**
     * 物流费用
     */
    private BigDecimal shipFee;

    /**
     * 发货时间
     */
    private Date shipTime;

    /**
     * 发货操作人
     */
    private String shipOperator;

    /**
     * 物流状态（0 待发货1已发货2已完成）
     */
    private Integer shipStatus;

    /**
     * 包裹重量
     */
    private Double packageWeight;

    /**
     * 包裹长度
     */
    private Double packageLength;

    /**
     * 包裹宽度
     */
    private Double packageWidth;

    /**
     * 包裹高度
     */
    private Double packageHeight;

    /**
     * 打包操作人
     */
    private String packsgeOperator;

    /**
     * 打包时间
     */
    private Date packsgeTime;

    /**
     * 包裹内容JSON
     */
    private String packages;

    /**
     * 备注
     */
    private String remark;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String updateBy;
    @TableField(exist = false)
    private List<OShipmentItem> itemList;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}