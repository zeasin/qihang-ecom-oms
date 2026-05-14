package cn.qihangerp.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商商品添加BO
 * @author qihang
 */
@Data
public class SupplierProductAddBo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 供应商ID */
    private Long supplierId;

    /** 商品名称 */
    private String productName;

    /** 商品图片地址 */
    private String imageUrl;

    /** 商品编号 */
    private String productNum;

    /** 商品分类ID */
    private Long categoryId;

    /** 品牌ID */
    private Long brandId;

    /** 单位名称 */
    private String unitName;

    /** 长 (毫米) */
    private Double length;

    /** 宽 (毫米) */
    private Double width;

    /** 高 (毫米) */
    private Double height;

    /** 重量 (千克) */
    private Double weight;

    /** 备注 */
    private String remark;

    /** SKU列表 */
    private List<SupplierProductItemBo> itemList;

    @Data
    public static class SupplierProductItemBo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** SKU主键ID（修改时传入，新增时为空） */
        private Long id;

        /** SKU编码 */
        private String skuCode;

        /** 商品条码 */
        private String barCode;

        /** 颜色ID */
        private Long colorId;

        /** 颜色值 */
        private String colorValue;

        /** 颜色图片 */
        private String colorImage;

        /** 尺寸ID */
        private Long sizeId;

        /** 尺寸值(材质) */
        private String sizeValue;

        /** 款式ID */
        private Long styleId;

        /** 款式值 */
        private String styleValue;

        /** 商品规格 */
        private String standard;

        /** 品牌编码 */
        private String brandNo;

        /** 品牌名称 */
        private String brandName;

        /** 价格 */
        private BigDecimal price;
    }
}
