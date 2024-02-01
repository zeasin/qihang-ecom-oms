package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttributeValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类属性值Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface ErpGoodsCategoryAttributeValueMapper 
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
     * 删除商品分类属性值
     * 
     * @param id 商品分类属性值主键
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeValueById(Long id);

    /**
     * 批量删除商品分类属性值
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpGoodsCategoryAttributeValueByIds(Long[] ids);
}
