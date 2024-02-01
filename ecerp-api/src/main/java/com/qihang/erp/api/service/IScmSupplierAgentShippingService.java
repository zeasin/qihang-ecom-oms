package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ScmSupplierAgentShipping;

/**
 * 供应商代发货Service接口
 * 
 * @author qihang
 * @date 2024-01-07
 */
public interface IScmSupplierAgentShippingService 
{
    /**
     * 查询供应商代发货
     * 
     * @param id 供应商代发货主键
     * @return 供应商代发货
     */
    public ScmSupplierAgentShipping selectScmSupplierAgentShippingById(Long id);

    /**
     * 查询供应商代发货列表
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 供应商代发货集合
     */
    public List<ScmSupplierAgentShipping> selectScmSupplierAgentShippingList(ScmSupplierAgentShipping scmSupplierAgentShipping);

    /**
     * 新增供应商代发货
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 结果
     */
    public int insertScmSupplierAgentShipping(ScmSupplierAgentShipping scmSupplierAgentShipping);

    /**
     * 修改供应商代发货
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 结果
     */
    public int updateScmSupplierAgentShipping(ScmSupplierAgentShipping scmSupplierAgentShipping);

    /**
     * 批量删除供应商代发货
     * 
     * @param ids 需要删除的供应商代发货主键集合
     * @return 结果
     */
    public int deleteScmSupplierAgentShippingByIds(Long[] ids);

    /**
     * 删除供应商代发货信息
     * 
     * @param id 供应商代发货主键
     * @return 结果
     */
    public int deleteScmSupplierAgentShippingById(Long id);
}
