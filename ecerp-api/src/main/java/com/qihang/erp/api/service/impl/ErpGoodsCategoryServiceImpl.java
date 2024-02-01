package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ErpGoodsCategoryMapper;
import com.qihang.erp.api.domain.ErpGoodsCategory;
import com.qihang.erp.api.service.IErpGoodsCategoryService;

/**
 * 商品分类Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ErpGoodsCategoryServiceImpl implements IErpGoodsCategoryService 
{
    @Autowired
    private ErpGoodsCategoryMapper erpGoodsCategoryMapper;

    /**
     * 查询商品分类
     * 
     * @param id 商品分类主键
     * @return 商品分类
     */
    @Override
    public ErpGoodsCategory selectErpGoodsCategoryById(Long id)
    {
        return erpGoodsCategoryMapper.selectErpGoodsCategoryById(id);
    }

    /**
     * 查询商品分类列表
     * 
     * @param erpGoodsCategory 商品分类
     * @return 商品分类
     */
    @Override
    public List<ErpGoodsCategory> selectErpGoodsCategoryList(ErpGoodsCategory erpGoodsCategory)
    {
        return erpGoodsCategoryMapper.selectErpGoodsCategoryList(erpGoodsCategory);
    }

    /**
     * 新增商品分类
     * 
     * @param erpGoodsCategory 商品分类
     * @return 结果
     */
    @Override
    public int insertErpGoodsCategory(ErpGoodsCategory erpGoodsCategory)
    {
        erpGoodsCategory.setCreateTime(DateUtils.getNowDate());
        return erpGoodsCategoryMapper.insertErpGoodsCategory(erpGoodsCategory);
    }

    /**
     * 修改商品分类
     * 
     * @param erpGoodsCategory 商品分类
     * @return 结果
     */
    @Override
    public int updateErpGoodsCategory(ErpGoodsCategory erpGoodsCategory)
    {
        erpGoodsCategory.setUpdateTime(DateUtils.getNowDate());
        return erpGoodsCategoryMapper.updateErpGoodsCategory(erpGoodsCategory);
    }

    /**
     * 批量删除商品分类
     * 
     * @param ids 需要删除的商品分类主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryByIds(Long[] ids)
    {
        return erpGoodsCategoryMapper.deleteErpGoodsCategoryByIds(ids);
    }

    /**
     * 删除商品分类信息
     * 
     * @param id 商品分类主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsCategoryById(Long id)
    {
        return erpGoodsCategoryMapper.deleteErpGoodsCategoryById(id);
    }
}
