package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺账单
 * @TableName erp_shop_bill
 */
@TableName(value ="erp_bill_shop_order")
@Data
public class ErpBillShopOrder implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long shopId;

    /**
     * 
     */
    private Integer shopType;

    /**
     * 
     */
    private Long tenantId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 1收入2支出
     */
    private Integer type;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 业务时间
     */
    private String bizTime;

    /**
     * 服务类型
     */
    private String bizType;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private String detail;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}