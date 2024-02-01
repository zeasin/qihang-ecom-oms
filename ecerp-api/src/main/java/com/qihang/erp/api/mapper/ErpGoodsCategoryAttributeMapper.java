package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttribute;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类属性Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface ErpGoodsCategoryAttributeMapper 
{
    /**
     * 查询商品分类属性
     * 
     * @param id 商品分类属性主键
     * @return 商品分类属性
     */
    public ErpGoodsCategoryAttribute selectErpGoodsCategoryAttributeById(Long id);

    /**
     * 查询商品分类属性列表
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 商品分类属性集合
     */
    public List<ErpGoodsCategoryAttribute> selectErpGoodsCategoryAttributeList(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute);

    /**
     * 新增商品分类属性
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 结果
     */
    public int insertErpGoodsCategoryAttribute(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute);

    /**
     * 修改商品分类属性
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 结果
     */
    public int updateErpGoodsCategoryAttribute(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute);

    /**
     * 删除商品分类属性
     * 
     * @param id 商品分类属性主键
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeById(Long id);

    /**
     * 批量删除商品分类属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeByIds(Long[] ids);
}
