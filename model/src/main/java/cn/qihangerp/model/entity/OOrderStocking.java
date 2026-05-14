package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 供应商发货订单
 * @TableName o_supplier_ship_order
 */

@Data
public class OOrderStocking implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发货人id,本地仓库发货固定值：0,供应商发货值：供应商ID，云仓发货值：云仓ID
     */
    private Long shipperId;

    /**
     * 类型：0本地仓库备货  300供应商发货 20商户发货 100京东云仓发货 110吉客云云仓发货 200系统云仓发货
     * EnumShipType
     */
    private Integer type;

    /**
     * 订单类型0正常订单20换货订单80补发订单99采购订单
     */
    private int orderType;
    /**
     * 发货方式：0手动发货  1电子面单发货
     */
    private Integer shipMode;

    /**
     * 关联订单id（订单库订单id或者采购单id）
     */
    private Long oOrderId;

    /**
     * 订单编号（第三方平台订单号）
     */
    private String orderNum;

    /**
     * 订单下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 买家留言信息
     */
    private String buyerMemo;

    /**
     * 卖家留言信息
     */
    private String sellerMemo;

    /**
     * 发货状态 0代发货 1：部分发货，2：全部发货，
     */
    private Integer sendStatus;
    /**
     * 状态0待备货(待出库)1部分备货(出库中)2全部备货(已出库)
     */
    private Integer stockingStatus;

    /**
     * 发货时间
     */
    private Date shippingTime;

    /**
     * 快递单号
     */

    private String shippingNumber;

    private String shippingOrderCode;
    private String shippingErpOrderCode;

    /**
     * 物流公司
     */
    private String shippingCompany;
    private String shippingCompanyCode;

    /**
     * 发货人
     */
    private String shippingMan;

    /**
     * 发货费用
     */
    private BigDecimal shippingCost;

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
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;
    private String waybillCode;
    private Integer waybillStatus;
    private String waybillCompany;
    private Integer orderStatus;

    /**
     * 打印数据
     */
    @TableField(exist = false)
    private String printData;

    /**
     * 电子面单订单id(仅视频号)
     */
    private String waybillOrderId;
    /**
     * 区
     */
    private String town;
    private String address;
    private String receiverName;
    private String receiverMobile;


    private Long merchantId;
    private String warehouseType;//
    private Long warehouseId;
    private String warehouseName;
    private String warehouseNo;
    private String shipperNo;
    private String shopNo;
    private String platformNo;

    private Integer erpPushStatus;//推送状态(0推送失败1推送成功)
    private String erpPushResult;//推送返回结果
    private String erpPushParam1;//推送参数1-京东云仓：
    private String erpPushParam2;//推送参数2-京东云仓：
    private String erpPushParam3;//推送参数3-京东云仓：

    @TableField(exist = false)
    private List<OOrderStockingItem> itemList;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}