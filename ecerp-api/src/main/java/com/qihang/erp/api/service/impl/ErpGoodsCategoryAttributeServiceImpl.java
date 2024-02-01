package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ErpGoodsCategoryAttributeMapper;
import com.qihang.erp.api.domain.ErpGoodsCategoryAttribute;
import com.qihang.erp.api.service.IErpGoodsCategoryAttributeService;

/**
 * 商品分类属性Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ErpGoodsCategoryAttributeServiceImpl implements IErpGoodsCategoryAttributeService 
{
    @Autowired
    private ErpGoodsCategoryAttributeMapper erpGoodsCategoryAttributeMapper;

    /**
     * 查询商品分类属性
     * 
     * @param id 商品分类属性主键
     * @return 商品分类属性
     */
    @Override
    public ErpGoodsCategoryAttribute selectErpGoodsCategoryAttributeById(Long id)
    {
        return erpGoodsCategoryAttributeMapper.selectErpGoodsCategoryAttributeById(id);
    }

    /**
     * 查询商品分类属性列表
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 商品分类属性
     */
    @Override
    public List<ErpGoodsCategoryAttribute> selectErpGoodsCategoryAttributeList(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        return erpGoodsCategoryAttributeMapper.selectErpGoodsCategoryAttributeList(erpGoodsCategoryAttribute);
    }

    /**
     * 新增商品分类属性
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 结果
     */
    @Override
    public int insertErpGoodsCategoryAttribute(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        return erpGoodsCategoryAttributeMapper.insertErpGoodsCategoryAttribute(erpGoodsCategoryAttribute);
    }

    /**
     * 修改商品分类属性
     * 
     * @param erpGoodsCategoryAttribute 商品分类属性
     * @return 结果
     */
    @Override
    public int updateErpGoodsCategoryAttribute(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        return erpGoodsCategoryAttributeMapper.updateErpGoodsCategoryAttribute(erpGoodsCategoryAttribute);
    }

    /**
     * 批量删除商品分类属性
     * 
     * @param ids 需要删除的商品分类属性主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryAttributeByIds(Long[] ids)
    {
        return erpGoodsCategoryAttributeMapper.deleteErpGoodsCategoryAttributeByIds(ids);
    }

    /**
     * 删除商品分类属性信息
     * 
     * @param id 商品分类属性主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryAttributeById(Long id)
    {
        return erpGoodsCategoryAttributeMapper.deleteErpGoodsCategoryAttributeById(id);
    }
}
