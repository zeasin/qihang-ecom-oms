package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户用户表
 * @TableName o_merchant
 */
@Data
public class ErpMerchant implements Serializable {
    /**
     * 商户ID(tenantId)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户登录账号
     */
    private String loginName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户编码
     */
    private String number;

    /**
     * 社会信用代码
     */
    private String usci;

    /**
     * 法人
     */
    private String faren;

    /**
     * 开户行
     */
    private String bank;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系地址
     */
    private String address;
    private String supplierIds;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}