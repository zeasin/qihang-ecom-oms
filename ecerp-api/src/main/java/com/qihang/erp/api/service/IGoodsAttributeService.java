package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.GoodsAttribute;

/**
 * 商品属性Service接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
public interface IGoodsAttributeService 
{
    /**
     * 查询商品属性
     * 
     * @param id 商品属性主键
     * @return 商品属性
     */
    public GoodsAttribute selectGoodsAttributeById(Long id);

    /**
     * 查询商品属性列表
     * 
     * @param goodsAttribute 商品属性
     * @return 商品属性集合
     */
    public List<GoodsAttribute> selectGoodsAttributeList(GoodsAttribute goodsAttribute);

    /**
     * 新增商品属性
     * 
     * @param goodsAttribute 商品属性
     * @return 结果
     */
    public int insertGoodsAttribute(GoodsAttribute goodsAttribute);

    /**
     * 修改商品属性
     * 
     * @param goodsAttribute 商品属性
     * @return 结果
     */
    public int updateGoodsAttribute(GoodsAttribute goodsAttribute);

    /**
     * 批量删除商品属性
     * 
     * @param ids 需要删除的商品属性主键集合
     * @return 结果
     */
    public int deleteGoodsAttributeByIds(Long[] ids);

    /**
     * 删除商品属性信息
     * 
     * @param id 商品属性主键
     * @return 结果
     */
    public int deleteGoodsAttributeById(Long id);
}
