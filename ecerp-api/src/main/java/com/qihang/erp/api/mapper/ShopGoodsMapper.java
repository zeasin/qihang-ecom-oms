package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ShopGoods;
import com.qihang.erp.api.domain.SShopGoodsSku;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺商品Mapper接口
 * 
 * @author qihang
 * @date 2023-12-31
 */
@Mapper
public interface ShopGoodsMapper 
{
    /**
     * 查询店铺商品
     * 
     * @param id 店铺商品主键
     * @return 店铺商品
     */
    public ShopGoods selectShopGoodsById(Long id);

    /**
     * 查询店铺商品列表
     * 
     * @param shopGoods 店铺商品
     * @return 店铺商品集合
     */
    public List<ShopGoods> selectShopGoodsList(ShopGoods shopGoods);

    /**
     * 新增店铺商品
     * 
     * @param shopGoods 店铺商品
     * @return 结果
     */
    public int insertShopGoods(ShopGoods shopGoods);

    /**
     * 修改店铺商品
     * 
     * @param shopGoods 店铺商品
     * @return 结果
     */
    public int updateShopGoods(ShopGoods shopGoods);

    /**
     * 删除店铺商品
     * 
     * @param id 店铺商品主键
     * @return 结果
     */
    public int deleteShopGoodsById(Long id);

    /**
     * 批量删除店铺商品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShopGoodsByIds(Long[] ids);

    /**
     * 批量删除${subTable.functionName}
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSShopGoodsSkuByShopGoodsIds(Long[] ids);
    
    /**
     * 批量新增${subTable.functionName}
     * 
     * @param sShopGoodsSkuList ${subTable.functionName}列表
     * @return 结果
     */
    public int batchSShopGoodsSku(List<SShopGoodsSku> sShopGoodsSkuList);
    

    /**
     * 通过店铺商品主键删除${subTable.functionName}信息
     * 
     * @param id 店铺商品ID
     * @return 结果
     */
    public int deleteSShopGoodsSkuByShopGoodsId(Long id);
}
