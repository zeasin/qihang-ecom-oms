package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.domain.FmsInventoryReportDetail;
import com.qihang.erp.api.mapper.FmsInventoryReportMapper;
import com.qihang.erp.api.domain.FmsInventoryReport;
import com.qihang.erp.api.service.IFmsInventoryReportService;

/**
 * 库存存货报Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Service
public class FmsInventoryReportServiceImpl implements IFmsInventoryReportService 
{
    @Autowired
    private FmsInventoryReportMapper fmsInventoryReportMapper;

    /**
     * 查询库存存货报
     * 
     * @param id 库存存货报主键
     * @return 库存存货报
     */
    @Override
    public FmsInventoryReport selectFmsInventoryReportById(Long id)
    {
        return fmsInventoryReportMapper.selectFmsInventoryReportById(id);
    }

    /**
     * 查询库存存货报列表
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 库存存货报
     */
    @Override
    public List<FmsInventoryReport> selectFmsInventoryReportList(FmsInventoryReport fmsInventoryReport)
    {
        return fmsInventoryReportMapper.selectFmsInventoryReportList(fmsInventoryReport);
    }

    /**
     * 新增库存存货报
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFmsInventoryReport(FmsInventoryReport fmsInventoryReport)
    {
        fmsInventoryReport.setCreateTime(DateUtils.getNowDate());
        int rows = fmsInventoryReportMapper.insertFmsInventoryReport(fmsInventoryReport);
        insertFmsInventoryReportDetail(fmsInventoryReport);
        return rows;
    }

    /**
     * 修改库存存货报
     * 
     * @param fmsInventoryReport 库存存货报
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFmsInventoryReport(FmsInventoryReport fmsInventoryReport)
    {
        fmsInventoryReport.setUpdateTime(DateUtils.getNowDate());
        fmsInventoryReportMapper.deleteFmsInventoryReportDetailByReportId(fmsInventoryReport.getId());
        insertFmsInventoryReportDetail(fmsInventoryReport);
        return fmsInventoryReportMapper.updateFmsInventoryReport(fmsInventoryReport);
    }

    /**
     * 批量删除库存存货报
     * 
     * @param ids 需要删除的库存存货报主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFmsInventoryReportByIds(Long[] ids)
    {
        fmsInventoryReportMapper.deleteFmsInventoryReportDetailByReportIds(ids);
        return fmsInventoryReportMapper.deleteFmsInventoryReportByIds(ids);
    }

    /**
     * 删除库存存货报信息
     * 
     * @param id 库存存货报主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFmsInventoryReportById(Long id)
    {
        fmsInventoryReportMapper.deleteFmsInventoryReportDetailByReportId(id);
        return fmsInventoryReportMapper.deleteFmsInventoryReportById(id);
    }

    /**
     * 新增库存存货报明细信息
     * 
     * @param fmsInventoryReport 库存存货报对象
     */
    public void insertFmsInventoryReportDetail(FmsInventoryReport fmsInventoryReport)
    {
        List<FmsInventoryReportDetail> fmsInventoryReportDetailList = fmsInventoryReport.getFmsInventoryReportDetailList();
        Long id = fmsInventoryReport.getId();
        if (StringUtils.isNotNull(fmsInventoryReportDetailList))
        {
            List<FmsInventoryReportDetail> list = new ArrayList<FmsInventoryReportDetail>();
            for (FmsInventoryReportDetail fmsInventoryReportDetail : fmsInventoryReportDetailList)
            {
                fmsInventoryReportDetail.setReportId(id);
                list.add(fmsInventoryReportDetail);
            }
            if (list.size() > 0)
            {
                fmsInventoryReportMapper.batchFmsInventoryReportDetail(list);
            }
        }
    }
}
