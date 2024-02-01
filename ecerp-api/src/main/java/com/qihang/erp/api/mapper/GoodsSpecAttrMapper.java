package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.GoodsSpecAttr;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品规格属性Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface GoodsSpecAttrMapper 
{
    /**
     * 查询商品规格属性
     * 
     * @param id 商品规格属性主键
     * @return 商品规格属性
     */
    public GoodsSpecAttr selectGoodsSpecAttrById(Long id);

    /**
     * 查询商品规格属性列表
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 商品规格属性集合
     */
    public List<GoodsSpecAttr> selectGoodsSpecAttrList(GoodsSpecAttr goodsSpecAttr);

    /**
     * 新增商品规格属性
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 结果
     */
    public int insertGoodsSpecAttr(GoodsSpecAttr goodsSpecAttr);

    /**
     * 修改商品规格属性
     * 
     * @param goodsSpecAttr 商品规格属性
     * @return 结果
     */
    public int updateGoodsSpecAttr(GoodsSpecAttr goodsSpecAttr);

    /**
     * 删除商品规格属性
     * 
     * @param id 商品规格属性主键
     * @return 结果
     */
    public int deleteGoodsSpecAttrById(Long id);

    /**
     * 批量删除商品规格属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsSpecAttrByIds(Long[] ids);
}
