package com.qihang.erp.api.domain;

import java.util.List;
import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 商品库存对象 erp_goods_inventory
 * 
 * @author qihang
 * @date 2024-01-09
 */
public class ErpGoodsInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 商品编码 */
    @Excel(name = "商品编码")
    private String goodsNumber;

    /** 商品规格id */
    @Excel(name = "商品规格id")
    private Long specId;

    /** 规格编码（唯一） */
    @Excel(name = "规格编码", readConverterExp = "唯=一")
    private String specNumber;

    /** 当前库存 */
    @Excel(name = "当前库存")
    private Long currentQty;

    /** 锁定库存 */
    @Excel(name = "锁定库存")
    private Long lockedQty;

    /** 0正常  1删除 */
    @Excel(name = "0正常  1删除")
    private Integer isDelete;

    /** 商品库存明细信息 */
    private List<ErpGoodsInventoryDetail> erpGoodsInventoryDetailList;

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
    public void setGoodsNumber(String goodsNumber) 
    {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsNumber() 
    {
        return goodsNumber;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setSpecNumber(String specNumber) 
    {
        this.specNumber = specNumber;
    }

    public String getSpecNumber() 
    {
        return specNumber;
    }
    public void setCurrentQty(Long currentQty) 
    {
        this.currentQty = currentQty;
    }

    public Long getCurrentQty() 
    {
        return currentQty;
    }
    public void setLockedQty(Long lockedQty) 
    {
        this.lockedQty = lockedQty;
    }

    public Long getLockedQty() 
    {
        return lockedQty;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }

    public List<ErpGoodsInventoryDetail> getErpGoodsInventoryDetailList()
    {
        return erpGoodsInventoryDetailList;
    }

    public void setErpGoodsInventoryDetailList(List<ErpGoodsInventoryDetail> erpGoodsInventoryDetailList)
    {
        this.erpGoodsInventoryDetailList = erpGoodsInventoryDetailList;
    }

}
