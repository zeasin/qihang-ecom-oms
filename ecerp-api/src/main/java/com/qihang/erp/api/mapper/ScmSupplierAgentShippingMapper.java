package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ScmSupplierAgentShipping;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商代发货Mapper接口
 * 
 * @author qihang
 * @date 2024-01-07
 */
@Mapper
public interface ScmSupplierAgentShippingMapper 
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
     * 删除供应商代发货
     * 
     * @param id 供应商代发货主键
     * @return 结果
     */
    public int deleteScmSupplierAgentShippingById(Long id);

    /**
     * 批量删除供应商代发货
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScmSupplierAgentShippingByIds(Long[] ids);
}
