package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.GoodsSpec;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品规格Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface GoodsSpecMapper 
{
    /**
     * 查询商品规格
     * 
     * @param id 商品规格主键
     * @return 商品规格
     */
    public GoodsSpec selectGoodsSpecById(Long id);
    public GoodsSpec selectGoodsSpecBySpecNum(String specNum);

    /**
     * 查询商品规格列表
     * 
     * @param goodsSpec 商品规格
     * @return 商品规格集合
     */
    public List<GoodsSpec> selectGoodsSpecList(GoodsSpec goodsSpec);

    /**
     * 新增商品规格
     * 
     * @param goodsSpec 商品规格
     * @return 结果
     */
    public int insertGoodsSpec(GoodsSpec goodsSpec);

    /**
     * 修改商品规格
     * 
     * @param goodsSpec 商品规格
     * @return 结果
     */
    public int updateGoodsSpec(GoodsSpec goodsSpec);

    /**
     * 删除商品规格
     * 
     * @param id 商品规格主键
     * @return 结果
     */
    public int deleteGoodsSpecById(Long id);

    /**
     * 批量删除商品规格
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsSpecByIds(Long[] ids);
}
