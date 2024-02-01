package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.FmsPayableAgentShipMapper;
import com.qihang.erp.api.domain.FmsPayableAgentShip;
import com.qihang.erp.api.service.IFmsPayableAgentShipService;

/**
 * 财务管理-应付款-代发账单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Service
public class FmsPayableAgentShipServiceImpl implements IFmsPayableAgentShipService 
{
    @Autowired
    private FmsPayableAgentShipMapper fmsPayableAgentShipMapper;

    /**
     * 查询财务管理-应付款-代发账单
     * 
     * @param id 财务管理-应付款-代发账单主键
     * @return 财务管理-应付款-代发账单
     */
    @Override
    public FmsPayableAgentShip selectFmsPayableAgentShipById(Long id)
    {
        return fmsPayableAgentShipMapper.selectFmsPayableAgentShipById(id);
    }

    /**
     * 查询财务管理-应付款-代发账单列表
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 财务管理-应付款-代发账单
     */
    @Override
    public List<FmsPayableAgentShip> selectFmsPayableAgentShipList(FmsPayableAgentShip fmsPayableAgentShip)
    {
        return fmsPayableAgentShipMapper.selectFmsPayableAgentShipList(fmsPayableAgentShip);
    }

    /**
     * 新增财务管理-应付款-代发账单
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 结果
     */
    @Override
    public int insertFmsPayableAgentShip(FmsPayableAgentShip fmsPayableAgentShip)
    {
        fmsPayableAgentShip.setCreateTime(DateUtils.getNowDate());
        return fmsPayableAgentShipMapper.insertFmsPayableAgentShip(fmsPayableAgentShip);
    }

    /**
     * 修改财务管理-应付款-代发账单
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 结果
     */
    @Override
    public int updateFmsPayableAgentShip(FmsPayableAgentShip fmsPayableAgentShip)
    {
        fmsPayableAgentShip.setUpdateTime(DateUtils.getNowDate());
        return fmsPayableAgentShipMapper.updateFmsPayableAgentShip(fmsPayableAgentShip);
    }

    /**
     * 批量删除财务管理-应付款-代发账单
     * 
     * @param ids 需要删除的财务管理-应付款-代发账单主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayableAgentShipByIds(Long[] ids)
    {
        return fmsPayableAgentShipMapper.deleteFmsPayableAgentShipByIds(ids);
    }

    /**
     * 删除财务管理-应付款-代发账单信息
     * 
     * @param id 财务管理-应付款-代发账单主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayableAgentShipById(Long id)
    {
        return fmsPayableAgentShipMapper.deleteFmsPayableAgentShipById(id);
    }
}
