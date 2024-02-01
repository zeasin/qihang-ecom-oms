package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.Shop;

/**
 * 店铺Service接口
 * 
 * @author qihang
 * @date 2023-12-31
 */
public interface IShopService 
{
    /**
     * 查询店铺
     * 
     * @param id 店铺主键
     * @return 店铺
     */
    public Shop selectShopById(Long id);

    /**
     * 查询店铺列表
     * 
     * @param shop 店铺
     * @return 店铺集合
     */
    public List<Shop> selectShopList(Shop shop);

    /**
     * 新增店铺
     * 
     * @param shop 店铺
     * @return 结果
     */
    public int insertShop(Shop shop);

    /**
     * 修改店铺
     * 
     * @param shop 店铺
     * @return 结果
     */
    public int updateShop(Shop shop);

    /**
     * 批量删除店铺
     * 
     * @param ids 需要删除的店铺主键集合
     * @return 结果
     */
    public int deleteShopByIds(Long[] ids);

    /**
     * 删除店铺信息
     * 
     * @param id 店铺主键
     * @return 结果
     */
    public int deleteShopById(Long id);

    void updateSessionKey(Long shopId, Long mallId, String sessionKey);
}
