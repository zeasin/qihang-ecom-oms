package com.qihang.erp.api.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 店铺商品对象 s_shop_goods
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class ShopGoods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 商品id，PDD商品id */
    @Excel(name = "商品id，PDD商品id")
    private Long goodsId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long erpGoodsId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long shopId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long shopType;

    /** 商品货号，erp系统商品编码 */
    @Excel(name = "商品货号，erp系统商品编码")
    private String goodsNum;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String goodsName;

    /** 参考价格，返回价格区间，可能为空 */
    @Excel(name = "参考价格，返回价格区间，可能为空")
    private String price;

    /** 商品标题 */
    @Excel(name = "商品标题")
    private String title;

    /** 主图 */
    @Excel(name = "主图")
    private String thumbUrl;

    /** 商品图片json */
    @Excel(name = "商品图片json")
    private String imageUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long isMoreSku;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long isOnsale;

    /** 累计销量 */
    @Excel(name = "累计销量")
    private Long totalSales;

    /** 发布日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;

    /** $table.subTable.functionName信息 */
    private List<SShopGoodsSku> sShopGoodsSkuList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setErpGoodsId(Long erpGoodsId) 
    {
        this.erpGoodsId = erpGoodsId;
    }

    public Long getErpGoodsId() 
    {
        return erpGoodsId;
    }
    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setShopType(Long shopType) 
    {
        this.shopType = shopType;
    }

    public Long getShopType() 
    {
        return shopType;
    }
    public void setGoodsNum(String goodsNum) 
    {
        this.goodsNum = goodsNum;
    }

    public String getGoodsNum() 
    {
        return goodsNum;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setPrice(String price) 
    {
        this.price = price;
    }

    public String getPrice() 
    {
        return price;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setThumbUrl(String thumbUrl) 
    {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbUrl() 
    {
        return thumbUrl;
    }
    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }
    public void setIsMoreSku(Long isMoreSku) 
    {
        this.isMoreSku = isMoreSku;
    }

    public Long getIsMoreSku() 
    {
        return isMoreSku;
    }
    public void setIsOnsale(Long isOnsale) 
    {
        this.isOnsale = isOnsale;
    }

    public Long getIsOnsale() 
    {
        return isOnsale;
    }
    public void setTotalSales(Long totalSales) 
    {
        this.totalSales = totalSales;
    }

    public Long getTotalSales() 
    {
        return totalSales;
    }
    public void setPublishTime(Date publishTime) 
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime() 
    {
        return publishTime;
    }

    public List<SShopGoodsSku> getSShopGoodsSkuList()
    {
        return sShopGoodsSkuList;
    }

    public void setSShopGoodsSkuList(List<SShopGoodsSku> sShopGoodsSkuList)
    {
        this.sShopGoodsSkuList = sShopGoodsSkuList;
    }

}
