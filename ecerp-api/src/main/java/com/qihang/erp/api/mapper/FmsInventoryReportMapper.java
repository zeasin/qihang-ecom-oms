package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.FmsInventoryReport;
import com.qihang.erp.api.domain.FmsInventoryReportDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存存货报Mapper接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Mapper
public interface FmsInventoryReportMapper 
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
     * 删除库存存货报
     * 
     * @param id 库存存货报主键
     * @return 结果
     */
    public int deleteFmsInventoryReportById(Long id);

    /**
     * 批量删除库存存货报
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsInventoryReportByIds(Long[] ids);

    /**
     * 批量删除库存存货报明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsInventoryReportDetailByReportIds(Long[] ids);
    
    /**
     * 批量新增库存存货报明细
     * 
     * @param fmsInventoryReportDetailList 库存存货报明细列表
     * @return 结果
     */
    public int batchFmsInventoryReportDetail(List<FmsInventoryReportDetail> fmsInventoryReportDetailList);
    

    /**
     * 通过库存存货报主键删除库存存货报明细信息
     * 
     * @param id 库存存货报ID
     * @return 结果
     */
    public int deleteFmsInventoryReportDetailByReportId(Long id);
}
