package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.domain.SShopGoodsSku;
import com.qihang.erp.api.mapper.ShopGoodsMapper;
import com.qihang.erp.api.domain.ShopGoods;
import com.qihang.erp.api.service.IShopGoodsService;

/**
 * 店铺商品Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-31
 */
@Service
public class ShopGoodsServiceImpl implements IShopGoodsService 
{
    @Autowired
    private ShopGoodsMapper shopGoodsMapper;

    /**
     * 查询店铺商品
     * 
     * @param id 店铺商品主键
     * @return 店铺商品
     */
    @Override
    public ShopGoods selectShopGoodsById(Long id)
    {
        return shopGoodsMapper.selectShopGoodsById(id);
    }

    /**
     * 查询店铺商品列表
     * 
     * @param shopGoods 店铺商品
     * @return 店铺商品
     */
    @Override
    public List<ShopGoods> selectShopGoodsList(ShopGoods shopGoods)
    {
        return shopGoodsMapper.selectShopGoodsList(shopGoods);
    }

    /**
     * 新增店铺商品
     * 
     * @param shopGoods 店铺商品
     * @return 结果
     */
    @Transactional
    @Override
    public int insertShopGoods(ShopGoods shopGoods)
    {
        shopGoods.setCreateTime(DateUtils.getNowDate());
        int rows = shopGoodsMapper.insertShopGoods(shopGoods);
        insertSShopGoodsSku(shopGoods);
        return rows;
    }

    /**
     * 修改店铺商品
     * 
     * @param shopGoods 店铺商品
     * @return 结果
     */
    @Transactional
    @Override
    public int updateShopGoods(ShopGoods shopGoods)
    {
        shopGoodsMapper.deleteSShopGoodsSkuByShopGoodsId(shopGoods.getId());
        insertSShopGoodsSku(shopGoods);
        return shopGoodsMapper.updateShopGoods(shopGoods);
    }

    /**
     * 批量删除店铺商品
     * 
     * @param ids 需要删除的店铺商品主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteShopGoodsByIds(Long[] ids)
    {
        shopGoodsMapper.deleteSShopGoodsSkuByShopGoodsIds(ids);
        return shopGoodsMapper.deleteShopGoodsByIds(ids);
    }

    /**
     * 删除店铺商品信息
     * 
     * @param id 店铺商品主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteShopGoodsById(Long id)
    {
        shopGoodsMapper.deleteSShopGoodsSkuByShopGoodsId(id);
        return shopGoodsMapper.deleteShopGoodsById(id);
    }

    /**
     * 新增${subTable.functionName}信息
     * 
     * @param shopGoods 店铺商品对象
     */
    public void insertSShopGoodsSku(ShopGoods shopGoods)
    {
        List<SShopGoodsSku> sShopGoodsSkuList = shopGoods.getSShopGoodsSkuList();
        Long id = shopGoods.getId();
        if (StringUtils.isNotNull(sShopGoodsSkuList))
        {
            List<SShopGoodsSku> list = new ArrayList<SShopGoodsSku>();
            for (SShopGoodsSku sShopGoodsSku : sShopGoodsSkuList)
            {
                sShopGoodsSku.setShopGoodsId(id);
                list.add(sShopGoodsSku);
            }
            if (list.size() > 0)
            {
                shopGoodsMapper.batchSShopGoodsSku(list);
            }
        }
    }
}
