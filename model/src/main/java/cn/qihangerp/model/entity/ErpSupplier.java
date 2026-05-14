package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName o_goods_supplier
 */
@Data
public class ErpSupplier implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 供应商编码
     */
    private String number;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String loginPwd;

    /**
     * 
     */
    private String loginSlat;

    /**
     * 税率
     */
    private Double taxRate;

    /**
     * 期初应付款
     */
    private Double amount;

    /**
     * 期初预付款
     */
    private Double periodMoney;

    /**
     * 初期往来余额
     */
    private Double difMoney;

    /**
     * 余额日期
     */
    private Date beginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 职位
     */
    private String place;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 收货地址详情
     */
    private String address;

    /**
     * 
     */
    private String pinYin;

    /**
     * 0启用   1禁用
     */
    private Integer disable;

    /**
     * 0正常 1删除
     */
    private Integer isDelete;

    /**
     * 分管采购员
     */
    private String purchaserName;

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

    /**
     * 企业社会信用代码
     */
    private String usci;

    /**
     * 营业执照
     */
    private String bl;

    /**
     * 营业执照有效期
     */
    private String blPeriod;

    /**
     * 法人
     */
    private String blFaren;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 账户名称
     */
    private String bankAccountName;

    /**
     * 银行账户
     */
    private String bankAccount;

    private Long merchantId;
    private Integer isShipper;
    /**
     * 商户店铺id，0代表商户自己
     */
    private Long shopId;
    /**
     * 仓库id
     */
    private Long warehouseId;
    private String merchantIds;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}