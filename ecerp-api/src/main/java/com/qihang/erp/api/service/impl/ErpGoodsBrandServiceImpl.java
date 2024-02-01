package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ErpGoodsBrandMapper;
import com.qihang.erp.api.domain.ErpGoodsBrand;
import com.qihang.erp.api.service.IErpGoodsBrandService;

/**
 * 商品品牌Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ErpGoodsBrandServiceImpl implements IErpGoodsBrandService 
{
    @Autowired
    private ErpGoodsBrandMapper erpGoodsBrandMapper;

    /**
     * 查询商品品牌
     * 
     * @param id 商品品牌主键
     * @return 商品品牌
     */
    @Override
    public ErpGoodsBrand selectErpGoodsBrandById(Long id)
    {
        return erpGoodsBrandMapper.selectErpGoodsBrandById(id);
    }

    /**
     * 查询商品品牌列表
     * 
     * @param erpGoodsBrand 商品品牌
     * @return 商品品牌
     */
    @Override
    public List<ErpGoodsBrand> selectErpGoodsBrandList(ErpGoodsBrand erpGoodsBrand)
    {
        return erpGoodsBrandMapper.selectErpGoodsBrandList(erpGoodsBrand);
    }

    /**
     * 新增商品品牌
     * 
     * @param erpGoodsBrand 商品品牌
     * @return 结果
     */
    @Override
    public int insertErpGoodsBrand(ErpGoodsBrand erpGoodsBrand)
    {
        erpGoodsBrand.setCreateTime(DateUtils.getNowDate());
        return erpGoodsBrandMapper.insertErpGoodsBrand(erpGoodsBrand);
    }

    /**
     * 修改商品品牌
     * 
     * @param erpGoodsBrand 商品品牌
     * @return 结果
     */
    @Override
    public int updateErpGoodsBrand(ErpGoodsBrand erpGoodsBrand)
    {
        erpGoodsBrand.setUpdateTime(DateUtils.getNowDate());
        return erpGoodsBrandMapper.updateErpGoodsBrand(erpGoodsBrand);
    }

    /**
     * 批量删除商品品牌
     * 
     * @param ids 需要删除的商品品牌主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsBrandByIds(Long[] ids)
    {
        return erpGoodsBrandMapper.deleteErpGoodsBrandByIds(ids);
    }

    /**
     * 删除商品品牌信息
     * 
     * @param id 商品品牌主键
     * @return 结果
     */
    @Override
    public int deleteErpGoodsBrandById(Long id)
    {
        return erpGoodsBrandMapper.deleteErpGoodsBrandById(id);
    }
}
