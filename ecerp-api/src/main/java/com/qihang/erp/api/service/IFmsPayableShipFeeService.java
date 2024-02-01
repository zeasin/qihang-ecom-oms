package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.FmsPayableShipFee;

/**
 * 财务管理-应付款-物流费用Service接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
public interface IFmsPayableShipFeeService 
{
    /**
     * 查询财务管理-应付款-物流费用
     * 
     * @param id 财务管理-应付款-物流费用主键
     * @return 财务管理-应付款-物流费用
     */
    public FmsPayableShipFee selectFmsPayableShipFeeById(Long id);

    /**
     * 查询财务管理-应付款-物流费用列表
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 财务管理-应付款-物流费用集合
     */
    public List<FmsPayableShipFee> selectFmsPayableShipFeeList(FmsPayableShipFee fmsPayableShipFee);

    /**
     * 新增财务管理-应付款-物流费用
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 结果
     */
    public int insertFmsPayableShipFee(FmsPayableShipFee fmsPayableShipFee);

    /**
     * 修改财务管理-应付款-物流费用
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 结果
     */
    public int updateFmsPayableShipFee(FmsPayableShipFee fmsPayableShipFee);

    /**
     * 批量删除财务管理-应付款-物流费用
     * 
     * @param ids 需要删除的财务管理-应付款-物流费用主键集合
     * @return 结果
     */
    public int deleteFmsPayableShipFeeByIds(Long[] ids);

    /**
     * 删除财务管理-应付款-物流费用信息
     * 
     * @param id 财务管理-应付款-物流费用主键
     * @return 结果
     */
    public int deleteFmsPayableShipFeeById(Long id);
}
