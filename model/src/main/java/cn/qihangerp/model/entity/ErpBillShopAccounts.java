package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺账目表
 * @TableName erp_bill_shop_accounts
 */
@TableName(value ="erp_bill_shop_accounts")
@Data
public class ErpBillShopAccounts implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账单类型1支出2收入
     */
    private Integer type;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 交易时间
     */
    private String tradeTime;

    /**
     * 日期
     */
    private String date;

    /**
     * 应付总金额
     */
    private BigDecimal amount;

    /**
     * 用途
     */
    private String usageScenario;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0已生成1已结算)
     */
    private Integer status;

    /**
     * 订单创建时间
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