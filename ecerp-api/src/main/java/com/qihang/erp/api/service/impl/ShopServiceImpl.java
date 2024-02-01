package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ShopMapper;
import com.qihang.erp.api.domain.Shop;
import com.qihang.erp.api.service.IShopService;

/**
 * 店铺Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-31
 */
@Service
public class ShopServiceImpl implements IShopService 
{
    @Autowired
    private ShopMapper shopMapper;

    /**
     * 查询店铺
     * 
     * @param id 店铺主键
     * @return 店铺
     */
    @Override
    public Shop selectShopById(Long id)
    {
        return shopMapper.selectShopById(id);
    }

    /**
     * 查询店铺列表
     * 
     * @param shop 店铺
     * @return 店铺
     */
    @Override
    public List<Shop> selectShopList(Shop shop)
    {
        return shopMapper.selectShopList(shop);
    }

    /**
     * 新增店铺
     * 
     * @param shop 店铺
     * @return 结果
     */
    @Override
    public int insertShop(Shop shop)
    {
        return shopMapper.insertShop(shop);
    }

    /**
     * 修改店铺
     * 
     * @param shop 店铺
     * @return 结果
     */
    @Override
    public int updateShop(Shop shop)
    {
        return shopMapper.updateShop(shop);
    }

    /**
     * 批量删除店铺
     * 
     * @param ids 需要删除的店铺主键
     * @return 结果
     */
    @Override
    public int deleteShopByIds(Long[] ids)
    {
        return shopMapper.deleteShopByIds(ids);
    }

    /**
     * 删除店铺信息
     * 
     * @param id 店铺主键
     * @return 结果
     */
    @Override
    public int deleteShopById(Long id)
    {
        return shopMapper.deleteShopById(id);
    }

    @Override
    public void updateSessionKey(Long shopId, Long mallId, String sessionKey) {
        Shop update = new Shop();
        update.setId(shopId);
        update.setSessionKey(sessionKey);
        update.setModifyOn(System.currentTimeMillis()/1000);
        shopMapper.updateShop(update);
//        StringBuilder sb = new StringBuilder();
//        sb.append("UPDATE "+Tables.DcShop);
//        sb.append(" SET ");
//        sb.append(" sessionKey=?,modify_on=?");
//        sb.append(" WHERE id= ? and sellerUserId = ?");
//        jdbcTemplate.update(sb.toString(),sessionKey,System.currentTimeMillis()/1000,shopId,mallId);

    }
}
