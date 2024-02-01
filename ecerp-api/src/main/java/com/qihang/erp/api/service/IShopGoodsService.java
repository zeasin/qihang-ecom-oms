package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ShopGoods;

/**
 * 店铺商品Service接口
 * 
 * @author qihang
 * @date 2023-12-31
 */
public interface IShopGoodsService 
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
     * 批量删除店铺商品
     * 
     * @param ids 需要删除的店铺商品主键集合
     * @return 结果
     */
    public int deleteShopGoodsByIds(Long[] ids);

    /**
     * 删除店铺商品信息
     * 
     * @param id 店铺商品主键
     * @return 结果
     */
    public int deleteShopGoodsById(Long id);
}
