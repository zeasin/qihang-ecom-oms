package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhijian.common.annotation.Log;
import com.zhijian.common.core.controller.BaseController;
import com.zhijian.common.core.domain.AjaxResult;
import com.zhijian.common.enums.BusinessType;
import com.qihang.erp.api.domain.FmsInventoryReport;
import com.qihang.erp.api.service.IFmsInventoryReportService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 库存存货报Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/inventoryReport")
public class FmsInventoryReportController extends BaseController
{
    @Autowired
    private IFmsInventoryReportService fmsInventoryReportService;

    /**
     * 查询库存存货报列表
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(FmsInventoryReport fmsInventoryReport)
    {
        startPage();
        List<FmsInventoryReport> list = fmsInventoryReportService.selectFmsInventoryReportList(fmsInventoryReport);
        return getDataTable(list);
    }

    /**
     * 导出库存存货报列表
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:export')")
    @Log(title = "库存存货报", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FmsInventoryReport fmsInventoryReport)
    {
        List<FmsInventoryReport> list = fmsInventoryReportService.selectFmsInventoryReportList(fmsInventoryReport);
        ExcelUtil<FmsInventoryReport> util = new ExcelUtil<FmsInventoryReport>(FmsInventoryReport.class);
        util.exportExcel(response, list, "库存存货报数据");
    }

    /**
     * 获取库存存货报详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fmsInventoryReportService.selectFmsInventoryReportById(id));
    }

    /**
     * 新增库存存货报
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:add')")
    @Log(title = "库存存货报", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FmsInventoryReport fmsInventoryReport)
    {
        return toAjax(fmsInventoryReportService.insertFmsInventoryReport(fmsInventoryReport));
    }

    /**
     * 修改库存存货报
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:edit')")
    @Log(title = "库存存货报", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FmsInventoryReport fmsInventoryReport)
    {
        return toAjax(fmsInventoryReportService.updateFmsInventoryReport(fmsInventoryReport));
    }

    /**
     * 删除库存存货报
     */
    @PreAuthorize("@ss.hasPermi('fms:inventoryReport:remove')")
    @Log(title = "库存存货报", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fmsInventoryReportService.deleteFmsInventoryReportByIds(ids));
    }
}
