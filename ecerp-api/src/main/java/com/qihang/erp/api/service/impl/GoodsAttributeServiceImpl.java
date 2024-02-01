package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsAttributeMapper;
import com.qihang.erp.api.domain.GoodsAttribute;
import com.qihang.erp.api.service.IGoodsAttributeService;

/**
 * 商品属性Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsAttributeServiceImpl implements IGoodsAttributeService 
{
    @Autowired
    private GoodsAttributeMapper goodsAttributeMapper;

    /**
     * 查询商品属性
     * 
     * @param id 商品属性主键
     * @return 商品属性
     */
    @Override
    public GoodsAttribute selectGoodsAttributeById(Long id)
    {
        return goodsAttributeMapper.selectGoodsAttributeById(id);
    }

    /**
     * 查询商品属性列表
     * 
     * @param goodsAttribute 商品属性
     * @return 商品属性
     */
    @Override
    public List<GoodsAttribute> selectGoodsAttributeList(GoodsAttribute goodsAttribute)
    {
        return goodsAttributeMapper.selectGoodsAttributeList(goodsAttribute);
    }

    /**
     * 新增商品属性
     * 
     * @param goodsAttribute 商品属性
     * @return 结果
     */
    @Override
    public int insertGoodsAttribute(GoodsAttribute goodsAttribute)
    {
        return goodsAttributeMapper.insertGoodsAttribute(goodsAttribute);
    }

    /**
     * 修改商品属性
     * 
     * @param goodsAttribute 商品属性
     * @return 结果
     */
    @Override
    public int updateGoodsAttribute(GoodsAttribute goodsAttribute)
    {
        return goodsAttributeMapper.updateGoodsAttribute(goodsAttribute);
    }

    /**
     * 批量删除商品属性
     * 
     * @param ids 需要删除的商品属性主键
     * @return 结果
     */
    @Override
    public int deleteGoodsAttributeByIds(Long[] ids)
    {
        return goodsAttributeMapper.deleteGoodsAttributeByIds(ids);
    }

    /**
     * 删除商品属性信息
     * 
     * @param id 商品属性主键
     * @return 结果
     */
    @Override
    public int deleteGoodsAttributeById(Long id)
    {
        return goodsAttributeMapper.deleteGoodsAttributeById(id);
    }
}
