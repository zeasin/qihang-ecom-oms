package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsInventory;
import com.qihang.erp.api.domain.ErpGoodsInventoryDetail;
import com.qihang.erp.api.domain.WmsStockOutEntryItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存Mapper接口
 * 
 * @author qihang
 * @date 2024-01-09
 */
@Mapper
public interface ErpGoodsInventoryMapper 
{
    /**
     * 查询商品库存
     * 
     * @param id 商品库存主键
     * @return 商品库存
     */
    public ErpGoodsInventory selectErpGoodsInventoryById(Long id);
    public ErpGoodsInventory selectErpGoodsInventoryBySpecId(Long id);
    List<ErpGoodsInventoryDetail> selectErpGoodsInventoryDetailBySpecId(Long specId);
    ErpGoodsInventoryDetail selectErpGoodsInventoryDetailById(Long id);

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
    public int updateErpGoodsInventoryDetail(ErpGoodsInventoryDetail detail);

    /**
     * 删除商品库存
     * 
     * @param id 商品库存主键
     * @return 结果
     */
    public int deleteErpGoodsInventoryById(Long id);

    /**
     * 批量删除商品库存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpGoodsInventoryByIds(Long[] ids);

    /**
     * 批量删除商品库存明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpGoodsInventoryDetailByInventoryIds(Long[] ids);
    
    /**
     * 批量新增商品库存明细
     * 
     * @param erpGoodsInventoryDetailList 商品库存明细列表
     * @return 结果
     */
    public int batchErpGoodsInventoryDetail(List<ErpGoodsInventoryDetail> erpGoodsInventoryDetailList);
    

    /**
     * 通过商品库存主键删除商品库存明细信息
     * 
     * @param id 商品库存ID
     * @return 结果
     */
    public int deleteErpGoodsInventoryDetailByInventoryId(Long id);
}
