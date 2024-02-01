package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.GoodsAttributeConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性配置Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface GoodsAttributeConfigMapper 
{
    /**
     * 查询商品属性配置
     * 
     * @param id 商品属性配置主键
     * @return 商品属性配置
     */
    public GoodsAttributeConfig selectGoodsAttributeConfigById(Long id);

    /**
     * 查询商品属性配置列表
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 商品属性配置集合
     */
    public List<GoodsAttributeConfig> selectGoodsAttributeConfigList(GoodsAttributeConfig goodsAttributeConfig);

    /**
     * 新增商品属性配置
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 结果
     */
    public int insertGoodsAttributeConfig(GoodsAttributeConfig goodsAttributeConfig);

    /**
     * 修改商品属性配置
     * 
     * @param goodsAttributeConfig 商品属性配置
     * @return 结果
     */
    public int updateGoodsAttributeConfig(GoodsAttributeConfig goodsAttributeConfig);

    /**
     * 删除商品属性配置
     * 
     * @param id 商品属性配置主键
     * @return 结果
     */
    public int deleteGoodsAttributeConfigById(Long id);

    /**
     * 批量删除商品属性配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsAttributeConfigByIds(Long[] ids);
}
