package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ScmSupplier;
import org.apache.ibatis.annotations.Mapper;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface ScmSupplierMapper 
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
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteScmSupplierById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScmSupplierByIds(Long[] ids);
}
