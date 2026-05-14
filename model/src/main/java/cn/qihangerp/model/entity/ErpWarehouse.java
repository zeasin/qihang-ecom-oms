package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName erp_cloud_warehouse
 */
@TableName(value ="erp_warehouse")
@Data
public class ErpWarehouse implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 拥有者
     */
    private String ownerNo;

    /**
     * 类型（LOCAL本地仓CLOUD系统云仓JDYC京东云仓Other其他）
     */
    private String warehouseType;

    /**
     * 云仓编码
     */
    private String warehouseNo;

    /**
     * 云仓名称
     */
    private String warehouseName;
    private Integer warehouseSource;
    private String erpWarehouseName;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 类型（1本地仓2云仓）
     */
    private Integer type;

    /**
     * status 0禁用 1启用2正常
     */
    private String status;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 街道
     */
    private String town;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 第三方云仓appkey
     */
    private String appKey;

    /**
     * 第三方云仓appsecret
     */
    private String appSecret;

    /**
     * 第三方云仓accountToken
     */
    private String accountToken;

    /**
     * 第三方云仓refreshToken
     */
    private String refreshToken;

    /**
     * 京东云仓pin
     */
    private String bizPin;

    /**
     * 京东云仓事业部编码
     */
    private String bizId;
    private String sourceNo;

    /**
     * 京东云仓接口类型0仓配一体1erp
     */
    private Integer jdlApiType;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 商户id(云仓库和供应商仓库不受该字段现在)
     */
    private Long merchantId;
    /**
     * 商户店铺id，0代表商户自己(云仓库和供应商仓库不受该字段现在)
     */
    private Long shopId;
    private String remark;
    private String merchantIds;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}