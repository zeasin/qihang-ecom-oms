package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsAttributeConfigMapper;
import com.qihang.erp.api.domain.GoodsAttributeConfig;
import com.qihang.erp.api.service.IGoodsAttributeConfigService;

/**
 * 商品属性配置Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsAttributeConfigServiceImpl implements IGoodsAttributeConfigService 
{
    @Autowired
    private GoodsAttributeConfigMapper goodsAttributeConfigMapper;

    /**
     * 查询商品属性配置
     * 
     * @param id 商品属性配置主键
     * @return 商品属性配置
     */
    @Override
    public GoodsAttributeConfig selectGoodsAttributeConfigById(Long id)
    {
        return goodsAttributeConfigMapper.selectGoodsAttributeConfigById(id);
    }

    /**
     * 查询商品属性配置列表
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 商品属性配置
     */
    @Override
    public List<GoodsAttributeConfig> selectGoodsAttributeConfigList(GoodsAttributeConfig goodsAttributeConfig)
    {
        return goodsAttributeConfigMapper.selectGoodsAttributeConfigList(goodsAttributeConfig);
    }

    /**
     * 新增商品属性配置
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 结果
     */
    @Override
    public int insertGoodsAttributeConfig(GoodsAttributeConfig goodsAttributeConfig)
    {
        return goodsAttributeConfigMapper.insertGoodsAttributeConfig(goodsAttributeConfig);
    }

    /**
     * 修改商品属性配置
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 结果
     */
    @Override
    public int updateGoodsAttributeConfig(GoodsAttributeConfig goodsAttributeConfig)
    {
        return goodsAttributeConfigMapper.updateGoodsAttributeConfig(goodsAttributeConfig);
    }

    /**
     * 批量删除商品属性配置
     * 
     * @param ids 需要删除的商品属性配置主键
     * @return 结果
     */
    @Override
    public int deleteGoodsAttributeConfigByIds(Long[] ids)
    {
        return goodsAttributeConfigMapper.deleteGoodsAttributeConfigByIds(ids);
    }

    /**
     * 删除商品属性配置信息
     * 
     * @param id 商品属性配置主键
     * @return 结果
     */
    @Override
    public int deleteGoodsAttributeConfigById(Long id)
    {
        return goodsAttributeConfigMapper.deleteGoodsAttributeConfigById(id);
    }
}
