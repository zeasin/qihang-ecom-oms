package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ScmSupplierMapper;
import com.qihang.erp.api.domain.ScmSupplier;
import com.qihang.erp.api.service.IScmSupplierService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ScmSupplierServiceImpl implements IScmSupplierService 
{
    @Autowired
    private ScmSupplierMapper scmSupplierMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ScmSupplier selectScmSupplierById(Long id)
    {
        return scmSupplierMapper.selectScmSupplierById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ScmSupplier> selectScmSupplierList(ScmSupplier scmSupplier)
    {
        return scmSupplierMapper.selectScmSupplierList(scmSupplier);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertScmSupplier(ScmSupplier scmSupplier)
    {
        scmSupplier.setCreateTime(DateUtils.getNowDate());
        return scmSupplierMapper.insertScmSupplier(scmSupplier);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateScmSupplier(ScmSupplier scmSupplier)
    {
        return scmSupplierMapper.updateScmSupplier(scmSupplier);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteScmSupplierByIds(Long[] ids)
    {
        return scmSupplierMapper.deleteScmSupplierByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteScmSupplierById(Long id)
    {
        return scmSupplierMapper.deleteScmSupplierById(id);
    }
}
