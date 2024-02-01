package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ScmSupplier;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
public interface IScmSupplierService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ScmSupplier selectScmSupplierById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ScmSupplier> selectScmSupplierList(ScmSupplier scmSupplier);

    /**
     * 新增【请填写功能名称】
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 结果
     */
    public int insertScmSupplier(ScmSupplier scmSupplier);

    /**
     * 修改【请填写功能名称】
     * 
     * @param scmSupplier 【请填写功能名称】
     * @return 结果
     */
    public int updateScmSupplier(ScmSupplier scmSupplier);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteScmSupplierByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteScmSupplierById(Long id);
}
