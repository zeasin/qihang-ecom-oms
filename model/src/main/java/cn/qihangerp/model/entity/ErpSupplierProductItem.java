package cn.qihangerp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商商品表(SKU维度)
 * @TableName erp_supplier_product_item
 */
@TableName(value ="erp_supplier_product_item")
@Data
public class ErpSupplierProductItem {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商SPU表ID(erp_supplier_product外键)
     */
    private Long supplierProductId;

    /**
     * 供应商ID(冗余字段)
     */
    private Long supplierId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品条码
     */
    private String barCode;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 颜色值
     */
    private String colorValue;

    /**
     * 颜色图片
     */
    private String colorImage;

    /**
     * 尺寸ID
     */
    private Long sizeId;

    /**
     * 尺寸值(材质)
     */
    private String sizeValue;

    /**
     * 款式ID
     */
    private Long styleId;

    /**
     * 款式值
     */
    private String styleValue;

    /**
     * 商品规格
     */
    private String standard;

    /**
     * 品牌编码
     */
    private String brandNo;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 商品库SPU ID(o_goods外键)
     */
    private Long erpGoodsId;

    /**
     * 商品库SKU ID(o_goods_sku外键)
     */
    private Long erpGoodsSkuId;

    /**
     * 仓库商品ID(erp_warehouse_goods外键)
     */
    private Long warehouseGoodsId;

    /**
     * 状态(0待审核1已审核2下架)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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
     * 商品库SKU信息（非数据库字段，用于列表展示）
     */
    @TableField(exist = false)
    private OGoodsSku erpGoodsSku;
}