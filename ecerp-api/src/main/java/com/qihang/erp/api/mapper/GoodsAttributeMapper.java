package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.GoodsAttribute;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface GoodsAttributeMapper 
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
     * 删除商品属性
     * 
     * @param id 商品属性主键
     * @return 结果
     */
    public int deleteGoodsAttributeById(Long id);

    /**
     * 批量删除商品属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsAttributeByIds(Long[] ids);
}
