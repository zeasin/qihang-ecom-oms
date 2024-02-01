package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttribute;

/**
 * 商品分类属性Service接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
public interface IErpGoodsCategoryAttributeService 
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
     * 批量删除商品分类属性
     * 
     * @param ids 需要删除的商品分类属性主键集合
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeByIds(Long[] ids);

    /**
     * 删除商品分类属性信息
     * 
     * @param id 商品分类属性主键
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeById(Long id);
}
