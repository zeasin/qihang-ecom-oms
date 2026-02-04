package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 发货-备货表（打单加入备货清单）
 * @TableName o_order_ship_list_item
 */
@TableName(value ="o_order_ship_list_item")
@Data
public class OOrderShipListItem implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 外键id
     */
    private Long listId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺类型
     */
    private Integer shopType;

    /**
     * 发货方 0 仓库发货 1 供应商发货
     */
    private Integer shipper;

    /**
     * 发货供应商ID（0自己发货）
     */
    private Long shipSupplierId;

    /**
     * 发货供应商
     */
    private String shipSupplier;

    /**
     * erp订单id
     */
    private Long orderId;

    /**
     * erp订单itemid
     */
    private Long orderItemId;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 原始订单skuid
     */
    private String originalSkuId;

    /**
     * erp系统商品id
     */
    private Long goodsId;

    /**
     * erp系统商品规格id
     */
    private Long skuId;

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
    private String skuName;

    /**
     * 商品规格编码
     */
    private String skuNum;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 状态0待备货1备货中2备货完成3已发货
     */
    private Integer status;

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