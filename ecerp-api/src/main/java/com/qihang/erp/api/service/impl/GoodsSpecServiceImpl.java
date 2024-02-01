package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsSpecMapper;
import com.qihang.erp.api.domain.GoodsSpec;
import com.qihang.erp.api.service.IGoodsSpecService;

/**
 * 商品规格Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsSpecServiceImpl implements IGoodsSpecService 
{
    @Autowired
    private GoodsSpecMapper goodsSpecMapper;

    /**
     * 查询商品规格
     * 
     * @param id 商品规格主键
     * @return 商品规格
     */
    @Override
    public GoodsSpec selectGoodsSpecById(Long id)
    {
        return goodsSpecMapper.selectGoodsSpecById(id);
    }

    /**
     * 查询商品规格列表
     * 
     * @param goodsSpec 商品规格
     * @return 商品规格
     */
    @Override
    public List<GoodsSpec> selectGoodsSpecList(GoodsSpec goodsSpec)
    {
        return goodsSpecMapper.selectGoodsSpecList(goodsSpec);
    }

    /**
     * 新增商品规格
     * 
     * @param goodsSpec 商品规格
     * @return 结果
     */
    @Override
    public int insertGoodsSpec(GoodsSpec goodsSpec)
    {
        return goodsSpecMapper.insertGoodsSpec(goodsSpec);
    }

    /**
     * 修改商品规格
     * 
     * @param goodsSpec 商品规格
     * @return 结果
     */
    @Override
    public int updateGoodsSpec(GoodsSpec goodsSpec)
    {
        return goodsSpecMapper.updateGoodsSpec(goodsSpec);
    }

    /**
     * 批量删除商品规格
     * 
     * @param ids 需要删除的商品规格主键
     * @return 结果
     */
    @Override
    public int deleteGoodsSpecByIds(Long[] ids)
    {
        return goodsSpecMapper.deleteGoodsSpecByIds(ids);
    }

    /**
     * 删除商品规格信息
     * 
     * @param id 商品规格主键
     * @return 结果
     */
    @Override
    public int deleteGoodsSpecById(Long id)
    {
        return goodsSpecMapper.deleteGoodsSpecById(id);
    }
}
