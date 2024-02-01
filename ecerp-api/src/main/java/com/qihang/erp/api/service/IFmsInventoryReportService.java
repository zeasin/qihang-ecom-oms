package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.FmsInventoryReport;

/**
 * 库存存货报Service接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
public interface IFmsInventoryReportService 
{
    /**
     * 查询库存存货报
     * 
     * @param id 库存存货报主键
     * @return 库存存货报
     */
    public FmsInventoryReport selectFmsInventoryReportById(Long id);

    /**
     * 查询库存存货报列表
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 库存存货报集合
     */
    public List<FmsInventoryReport> selectFmsInventoryReportList(FmsInventoryReport fmsInventoryReport);

    /**
     * 新增库存存货报
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 结果
     */
    public int insertFmsInventoryReport(FmsInventoryReport fmsInventoryReport);

    /**
     * 修改库存存货报
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 结果
     */
    public int updateFmsInventoryReport(FmsInventoryReport fmsInventoryReport);

    /**
     * 批量删除库存存货报
     * 
     * @param ids 需要删除的库存存货报主键集合
     * @return 结果
     */
    public int deleteFmsInventoryReportByIds(Long[] ids);

    /**
     * 删除库存存货报信息
     * 
     * @param id 库存存货报主键
     * @return 结果
     */
    public int deleteFmsInventoryReportById(Long id);
}
