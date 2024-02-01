package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.FmsPayableAgentShip;
import org.apache.ibatis.annotations.Mapper;

/**
 * 财务管理-应付款-代发账单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Mapper
public interface FmsPayableAgentShipMapper 
{
    /**
     * 查询财务管理-应付款-代发账单
     * 
     * @param id 财务管理-应付款-代发账单主键
     * @return 财务管理-应付款-代发账单
     */
    public FmsPayableAgentShip selectFmsPayableAgentShipById(Long id);

    /**
     * 查询财务管理-应付款-代发账单列表
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 财务管理-应付款-代发账单集合
     */
    public List<FmsPayableAgentShip> selectFmsPayableAgentShipList(FmsPayableAgentShip fmsPayableAgentShip);

    /**
     * 新增财务管理-应付款-代发账单
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 结果
     */
    public int insertFmsPayableAgentShip(FmsPayableAgentShip fmsPayableAgentShip);

    /**
     * 修改财务管理-应付款-代发账单
     * 
     * @param fmsPayableAgentShip 财务管理-应付款-代发账单
     * @return 结果
     */
    public int updateFmsPayableAgentShip(FmsPayableAgentShip fmsPayableAgentShip);

    /**
     * 删除财务管理-应付款-代发账单
     * 
     * @param id 财务管理-应付款-代发账单主键
     * @return 结果
     */
    public int deleteFmsPayableAgentShipById(Long id);

    /**
     * 批量删除财务管理-应付款-代发账单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsPayableAgentShipByIds(Long[] ids);
}
