package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttributeValue;

/**
 * 商品分类属性值Service接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
public interface IErpGoodsCategoryAttributeValueService 
{
    /**
     * 查询商品分类属性值
     * 
     * @param id 商品分类属性值主键
     * @return 商品分类属性值
     */
    public ErpGoodsCategoryAttributeValue selectErpGoodsCategoryAttributeValueById(Long id);

    /**
     * 查询商品分类属性值列表
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 商品分类属性值集合
     */
    public List<ErpGoodsCategoryAttributeValue> selectErpGoodsCategoryAttributeValueList(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue);

    /**
     * 新增商品分类属性值
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 结果
     */
    public int insertErpGoodsCategoryAttributeValue(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue);

    /**
     * 修改商品分类属性值
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 结果
     */
    public int updateErpGoodsCategoryAttributeValue(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue);

    /**
     * 批量删除商品分类属性值
     * 
     * @param ids 需要删除的商品分类属性值主键集合
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeValueByIds(Long[] ids);

    /**
     * 删除商品分类属性值信息
     * 
     * @param id 商品分类属性值主键
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeValueById(Long id);
}
