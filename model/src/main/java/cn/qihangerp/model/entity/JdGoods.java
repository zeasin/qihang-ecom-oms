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
 * 京东商品表
 * @TableName oms_jd_goods
 */
@TableName(value ="oms_jd_goods")
@Data
public class JdGoods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long wareId;

    /**
     * 商品名称
     */
    private String title;

    /**
     * 商品状态 -1：删除 1:从未上架 2:自主下架 4:系统下架 8:上架 513:从未上架待审 514:自主下架待审 516:系统下架待审 520:上架待审核 1028:系统下架审核失败
     */
    private Integer wareStatus;

    /**
     * 	商品外部ID,商家自行设置的ID（便于关联京东商品）
     */
    private String outerId;

    /**
     * 商品货号
     */
    private String itemNum;

    /**
     * 商品的条形码.UPC码,SN码,PLU码统称为条形码
     */
    private String barCode;

    /**
     * 商品最后一次修改时间
     */
    private String modified;

    /**
     * 商品创建时间，只读属性
     */
    private String created;

    /**
     * 最后下架时间
     */
    private Date offlineTime;

    /**
     * 最后上架时间
     */
    private Date onlineTime;

    /**
     * 发货地
     */
    private String delivery;

    /**
     * 包装清单
     */
    private String packListing;

    /**
     * 包装规格
     */
    private String wrap;

    /**
     * 
     */
    private Double weight;

    /**
     * 
     */
    private Integer width;

    /**
     * 
     */
    private Integer height;

    /**
     * 
     */
    private Integer length;

    /**
     * 
     */
    private String mobileDesc;

    /**
     * 
     */
    private String introduction;

    /**
     * 
     */
    private String afterSales;

    /**
     * 
     */
    private String logo;

    /**
     * 
     */
    private BigDecimal marketPrice;

    /**
     * 
     */
    private BigDecimal costPrice;

    /**
     * 
     */
    private BigDecimal jdPrice;

    /**
     * 
     */
    private String brandName;

    /**
     * 
     */
    private Integer stockNum;

    /**
     * 
     */
    private String sellPoint;

    /**
     * 
     */
    private String afterSaleDesc;

    /**
     * 
     */
    private String spuId;

    /**
     * 店铺id（sys_shop表id）
     */
    private Long shopId;

    /**
     * 商品id(o_goods外键)
     */
    private Long erpGoodsId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    @TableField(exist = false)
    private List<JdGoodsSku> skuList;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        JdGoods other = (JdGoods) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWareId() == null ? other.getWareId() == null : this.getWareId().equals(other.getWareId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getWareStatus() == null ? other.getWareStatus() == null : this.getWareStatus().equals(other.getWareStatus()))
            && (this.getOuterId() == null ? other.getOuterId() == null : this.getOuterId().equals(other.getOuterId()))
            && (this.getItemNum() == null ? other.getItemNum() == null : this.getItemNum().equals(other.getItemNum()))
            && (this.getBarCode() == null ? other.getBarCode() == null : this.getBarCode().equals(other.getBarCode()))
            && (this.getModified() == null ? other.getModified() == null : this.getModified().equals(other.getModified()))
            && (this.getCreated() == null ? other.getCreated() == null : this.getCreated().equals(other.getCreated()))
            && (this.getOfflineTime() == null ? other.getOfflineTime() == null : this.getOfflineTime().equals(other.getOfflineTime()))
            && (this.getOnlineTime() == null ? other.getOnlineTime() == null : this.getOnlineTime().equals(other.getOnlineTime()))
            && (this.getDelivery() == null ? other.getDelivery() == null : this.getDelivery().equals(other.getDelivery()))
            && (this.getPackListing() == null ? other.getPackListing() == null : this.getPackListing().equals(other.getPackListing()))
            && (this.getWrap() == null ? other.getWrap() == null : this.getWrap().equals(other.getWrap()))
            && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
            && (this.getWidth() == null ? other.getWidth() == null : this.getWidth().equals(other.getWidth()))
            && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
            && (this.getLength() == null ? other.getLength() == null : this.getLength().equals(other.getLength()))
            && (this.getMobileDesc() == null ? other.getMobileDesc() == null : this.getMobileDesc().equals(other.getMobileDesc()))
            && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
            && (this.getAfterSales() == null ? other.getAfterSales() == null : this.getAfterSales().equals(other.getAfterSales()))
            && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
            && (this.getMarketPrice() == null ? other.getMarketPrice() == null : this.getMarketPrice().equals(other.getMarketPrice()))
            && (this.getCostPrice() == null ? other.getCostPrice() == null : this.getCostPrice().equals(other.getCostPrice()))
            && (this.getJdPrice() == null ? other.getJdPrice() == null : this.getJdPrice().equals(other.getJdPrice()))
            && (this.getBrandName() == null ? other.getBrandName() == null : this.getBrandName().equals(other.getBrandName()))
            && (this.getStockNum() == null ? other.getStockNum() == null : this.getStockNum().equals(other.getStockNum()))
            && (this.getSellPoint() == null ? other.getSellPoint() == null : this.getSellPoint().equals(other.getSellPoint()))
            && (this.getAfterSaleDesc() == null ? other.getAfterSaleDesc() == null : this.getAfterSaleDesc().equals(other.getAfterSaleDesc()))
            && (this.getSpuId() == null ? other.getSpuId() == null : this.getSpuId().equals(other.getSpuId()))
            && (this.getShopId() == null ? other.getShopId() == null : this.getShopId().equals(other.getShopId()))
            && (this.getErpGoodsId() == null ? other.getErpGoodsId() == null : this.getErpGoodsId().equals(other.getErpGoodsId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWareId() == null) ? 0 : getWareId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getWareStatus() == null) ? 0 : getWareStatus().hashCode());
        result = prime * result + ((getOuterId() == null) ? 0 : getOuterId().hashCode());
        result = prime * result + ((getItemNum() == null) ? 0 : getItemNum().hashCode());
        result = prime * result + ((getBarCode() == null) ? 0 : getBarCode().hashCode());
        result = prime * result + ((getModified() == null) ? 0 : getModified().hashCode());
        result = prime * result + ((getCreated() == null) ? 0 : getCreated().hashCode());
        result = prime * result + ((getOfflineTime() == null) ? 0 : getOfflineTime().hashCode());
        result = prime * result + ((getOnlineTime() == null) ? 0 : getOnlineTime().hashCode());
        result = prime * result + ((getDelivery() == null) ? 0 : getDelivery().hashCode());
        result = prime * result + ((getPackListing() == null) ? 0 : getPackListing().hashCode());
        result = prime * result + ((getWrap() == null) ? 0 : getWrap().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getWidth() == null) ? 0 : getWidth().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getLength() == null) ? 0 : getLength().hashCode());
        result = prime * result + ((getMobileDesc() == null) ? 0 : getMobileDesc().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getAfterSales() == null) ? 0 : getAfterSales().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getMarketPrice() == null) ? 0 : getMarketPrice().hashCode());
        result = prime * result + ((getCostPrice() == null) ? 0 : getCostPrice().hashCode());
        result = prime * result + ((getJdPrice() == null) ? 0 : getJdPrice().hashCode());
        result = prime * result + ((getBrandName() == null) ? 0 : getBrandName().hashCode());
        result = prime * result + ((getStockNum() == null) ? 0 : getStockNum().hashCode());
        result = prime * result + ((getSellPoint() == null) ? 0 : getSellPoint().hashCode());
        result = prime * result + ((getAfterSaleDesc() == null) ? 0 : getAfterSaleDesc().hashCode());
        result = prime * result + ((getSpuId() == null) ? 0 : getSpuId().hashCode());
        result = prime * result + ((getShopId() == null) ? 0 : getShopId().hashCode());
        result = prime * result + ((getErpGoodsId() == null) ? 0 : getErpGoodsId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wareId=").append(wareId);
        sb.append(", title=").append(title);
        sb.append(", wareStatus=").append(wareStatus);
        sb.append(", outerId=").append(outerId);
        sb.append(", itemNum=").append(itemNum);
        sb.append(", barCode=").append(barCode);
        sb.append(", modified=").append(modified);
        sb.append(", created=").append(created);
        sb.append(", offlineTime=").append(offlineTime);
        sb.append(", onlineTime=").append(onlineTime);
        sb.append(", delivery=").append(delivery);
        sb.append(", packListing=").append(packListing);
        sb.append(", wrap=").append(wrap);
        sb.append(", weight=").append(weight);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", length=").append(length);
        sb.append(", mobileDesc=").append(mobileDesc);
        sb.append(", introduction=").append(introduction);
        sb.append(", afterSales=").append(afterSales);
        sb.append(", logo=").append(logo);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", costPrice=").append(costPrice);
        sb.append(", jdPrice=").append(jdPrice);
        sb.append(", brandName=").append(brandName);
        sb.append(", stockNum=").append(stockNum);
        sb.append(", sellPoint=").append(sellPoint);
        sb.append(", afterSaleDesc=").append(afterSaleDesc);
        sb.append(", spuId=").append(spuId);
        sb.append(", shopId=").append(shopId);
        sb.append(", erpGoodsId=").append(erpGoodsId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}