package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ErpGoodsCategoryAttributeValueMapper;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttributeValue;
import com.qihang.erp.api.service.IErpGoodsCategoryAttributeValueService;

/**
 * 商品分类属性值Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ErpGoodsCategoryAttributeValueServiceImpl implements IErpGoodsCategoryAttributeValueService 
{
    @Autowired
    private ErpGoodsCategoryAttributeValueMapper erpGoodsCategoryAttributeValueMapper;

    /**
     * 查询商品分类属性值
     * 
     * @param id 商品分类属性值主键
     * @return 商品分类属性值
     */
    @Override
    public ErpGoodsCategoryAttributeValue selectErpGoodsCategoryAttributeValueById(Long id)
    {
        return erpGoodsCategoryAttributeValueMapper.selectErpGoodsCategoryAttributeValueById(id);
    }

    /**
     * 查询商品分类属性值列表
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 商品分类属性值
     */
    @Override
    public List<ErpGoodsCategoryAttributeValue> selectErpGoodsCategoryAttributeValueList(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        return erpGoodsCategoryAttributeValueMapper.selectErpGoodsCategoryAttributeValueList(erpGoodsCategoryAttributeValue);
    }

    /**
     * 新增商品分类属性值
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 结果
     */
    @Override
    public int insertErpGoodsCategoryAttributeValue(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        return erpGoodsCategoryAttributeValueMapper.insertErpGoodsCategoryAttributeValue(erpGoodsCategoryAttributeValue);
    }

    /**
     * 修改商品分类属性值
     * 
     * @param erpGoodsCategoryAttributeValue 商品分类属性值
     * @return 结果
     */
    @Override
    public int updateErpGoodsCategoryAttributeValue(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        return erpGoodsCategoryAttributeValueMapper.updateErpGoodsCategoryAttributeValue(erpGoodsCategoryAttributeValue);
    }

    /**
     * 批量删除商品分类属性值
     * 
     * @param ids 需要删除的商品分类属性值主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryAttributeValueByIds(Long[] ids)
    {
        return erpGoodsCategoryAttributeValueMapper.deleteErpGoodsCategoryAttributeValueByIds(ids);
    }

    /**
     * 删除商品分类属性值信息
     * 
     * @param id 商品分类属性值主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryAttributeValueById(Long id)
    {
        return erpGoodsCategoryAttributeValueMapper.deleteErpGoodsCategoryAttributeValueById(id);
    }
}
