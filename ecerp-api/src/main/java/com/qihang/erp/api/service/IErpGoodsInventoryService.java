package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsInventory;

/**
 * 商品库存Service接口
 * 
 * @author qihang
 * @date 2024-01-09
 */
public interface IErpGoodsInventoryService 
{
    /**
     * 查询商品库存
     * 
     * @param id 商品库存主键
     * @return 商品库存
     */
    public ErpGoodsInventory selectErpGoodsInventoryById(Long id);

    /**
     * 查询商品库存列表
     * 
     * @param erpGoodsInventory 商品库存
     * @return 商品库存集合
     */
    public List<ErpGoodsInventory> selectErpGoodsInventoryList(ErpGoodsInventory erpGoodsInventory);

    /**
     * 新增商品库存
     * 
     * @param erpGoodsInventory 商品库存
     * @return 结果
     */
    public int insertErpGoodsInventory(ErpGoodsInventory erpGoodsInventory);

    /**
     * 修改商品库存
     * 
     * @param erpGoodsInventory 商品库存
     * @return 结果
     */
    public int updateErpGoodsInventory(ErpGoodsInventory erpGoodsInventory);

    /**
     * 批量删除商品库存
     * 
     * @param ids 需要删除的商品库存主键集合
     * @return 结果
     */
    public int deleteErpGoodsInventoryByIds(Long[] ids);

    /**
     * 删除商品库存信息
     * 
     * @param id 商品库存主键
     * @return 结果
     */
    public int deleteErpGoodsInventoryById(Long id);
}
