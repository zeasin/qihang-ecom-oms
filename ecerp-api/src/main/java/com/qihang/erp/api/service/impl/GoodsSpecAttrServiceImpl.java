package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsSpecAttrMapper;
import com.qihang.erp.api.domain.GoodsSpecAttr;
import com.qihang.erp.api.service.IGoodsSpecAttrService;

/**
 * 商品规格属性Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsSpecAttrServiceImpl implements IGoodsSpecAttrService 
{
    @Autowired
    private GoodsSpecAttrMapper goodsSpecAttrMapper;

    /**
     * 查询商品规格属性
     * 
     * @param id 商品规格属性主键
     * @return 商品规格属性
     */
    @Override
    public GoodsSpecAttr selectGoodsSpecAttrById(Long id)
    {
        return goodsSpecAttrMapper.selectGoodsSpecAttrById(id);
    }

    /**
     * 查询商品规格属性列表
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 商品规格属性
     */
    @Override
    public List<GoodsSpecAttr> selectGoodsSpecAttrList(GoodsSpecAttr goodsSpecAttr)
    {
        return goodsSpecAttrMapper.selectGoodsSpecAttrList(goodsSpecAttr);
    }

    /**
     * 新增商品规格属性
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 结果
     */
    @Override
    public int insertGoodsSpecAttr(GoodsSpecAttr goodsSpecAttr)
    {
        return goodsSpecAttrMapper.insertGoodsSpecAttr(goodsSpecAttr);
    }

    /**
     * 修改商品规格属性
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 结果
     */
    @Override
    public int updateGoodsSpecAttr(GoodsSpecAttr goodsSpecAttr)
    {
        return goodsSpecAttrMapper.updateGoodsSpecAttr(goodsSpecAttr);
    }

    /**
     * 批量删除商品规格属性
     * 
     * @param ids 需要删除的商品规格属性主键
     * @return 结果
     */
    @Override
    public int deleteGoodsSpecAttrByIds(Long[] ids)
    {
        return goodsSpecAttrMapper.deleteGoodsSpecAttrByIds(ids);
    }

    /**
     * 删除商品规格属性信息
     * 
     * @param id 商品规格属性主键
     * @return 结果
     */
    @Override
    public int deleteGoodsSpecAttrById(Long id)
    {
        return goodsSpecAttrMapper.deleteGoodsSpecAttrById(id);
    }
}
