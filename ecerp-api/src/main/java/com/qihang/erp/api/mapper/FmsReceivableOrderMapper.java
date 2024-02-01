package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.FmsReceivableOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 财务管理-应收款-订单收入Mapper接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Mapper
public interface FmsReceivableOrderMapper 
{
    /**
     * 查询财务管理-应收款-订单收入
     * 
     * @param id 财务管理-应收款-订单收入主键
     * @return 财务管理-应收款-订单收入
     */
    public FmsReceivableOrder selectFmsReceivableOrderById(Long id);

    /**
     * 查询财务管理-应收款-订单收入列表
     * 
     * @param fmsReceivableOrder 财务管理-应收款-订单收入
     * @return 财务管理-应收款-订单收入集合
     */
    public List<FmsReceivableOrder> selectFmsReceivableOrderList(FmsReceivableOrder fmsReceivableOrder);

    /**
     * 新增财务管理-应收款-订单收入
     * 
     * @param fmsReceivableOrder 财务管理-应收款-订单收入
     * @return 结果
     */
    public int insertFmsReceivableOrder(FmsReceivableOrder fmsReceivableOrder);

    /**
     * 修改财务管理-应收款-订单收入
     * 
     * @param fmsReceivableOrder 财务管理-应收款-订单收入
     * @return 结果
     */
    public int updateFmsReceivableOrder(FmsReceivableOrder fmsReceivableOrder);

    /**
     * 删除财务管理-应收款-订单收入
     * 
     * @param id 财务管理-应收款-订单收入主键
     * @return 结果
     */
    public int deleteFmsReceivableOrderById(Long id);

    /**
     * 批量删除财务管理-应收款-订单收入
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsReceivableOrderByIds(Long[] ids);
}
