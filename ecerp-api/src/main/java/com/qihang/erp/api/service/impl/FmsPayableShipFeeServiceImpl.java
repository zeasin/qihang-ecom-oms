package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.FmsPayableShipFeeMapper;
import com.qihang.erp.api.domain.FmsPayableShipFee;
import com.qihang.erp.api.service.IFmsPayableShipFeeService;

/**
 * 财务管理-应付款-物流费用Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Service
public class FmsPayableShipFeeServiceImpl implements IFmsPayableShipFeeService 
{
    @Autowired
    private FmsPayableShipFeeMapper fmsPayableShipFeeMapper;

    /**
     * 查询财务管理-应付款-物流费用
     * 
     * @param id 财务管理-应付款-物流费用主键
     * @return 财务管理-应付款-物流费用
     */
    @Override
    public FmsPayableShipFee selectFmsPayableShipFeeById(Long id)
    {
        return fmsPayableShipFeeMapper.selectFmsPayableShipFeeById(id);
    }

    /**
     * 查询财务管理-应付款-物流费用列表
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 财务管理-应付款-物流费用
     */
    @Override
    public List<FmsPayableShipFee> selectFmsPayableShipFeeList(FmsPayableShipFee fmsPayableShipFee)
    {
        return fmsPayableShipFeeMapper.selectFmsPayableShipFeeList(fmsPayableShipFee);
    }

    /**
     * 新增财务管理-应付款-物流费用
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 结果
     */
    @Override
    public int insertFmsPayableShipFee(FmsPayableShipFee fmsPayableShipFee)
    {
        fmsPayableShipFee.setCreateTime(DateUtils.getNowDate());
        return fmsPayableShipFeeMapper.insertFmsPayableShipFee(fmsPayableShipFee);
    }

    /**
     * 修改财务管理-应付款-物流费用
     * 
     * @param fmsPayableShipFee 财务管理-应付款-物流费用
     * @return 结果
     */
    @Override
    public int updateFmsPayableShipFee(FmsPayableShipFee fmsPayableShipFee)
    {
        fmsPayableShipFee.setUpdateTime(DateUtils.getNowDate());
        return fmsPayableShipFeeMapper.updateFmsPayableShipFee(fmsPayableShipFee);
    }

    /**
     * 批量删除财务管理-应付款-物流费用
     * 
     * @param ids 需要删除的财务管理-应付款-物流费用主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayableShipFeeByIds(Long[] ids)
    {
        return fmsPayableShipFeeMapper.deleteFmsPayableShipFeeByIds(ids);
    }

    /**
     * 删除财务管理-应付款-物流费用信息
     * 
     * @param id 财务管理-应付款-物流费用主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayableShipFeeById(Long id)
    {
        return fmsPayableShipFeeMapper.deleteFmsPayableShipFeeById(id);
    }
}
