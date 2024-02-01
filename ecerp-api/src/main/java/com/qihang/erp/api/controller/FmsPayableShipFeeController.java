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
import com.qihang.erp.api.domain.FmsPayableShipFee;
import com.qihang.erp.api.service.IFmsPayableShipFeeService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 财务管理-应付款-物流费用Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/shipFee")
public class FmsPayableShipFeeController extends BaseController
{
    @Autowired
    private IFmsPayableShipFeeService fmsPayableShipFeeService;

    /**
     * 查询财务管理-应付款-物流费用列表
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:list')")
    @GetMapping("/list")
    public TableDataInfo list(FmsPayableShipFee fmsPayableShipFee)
    {
        startPage();
        List<FmsPayableShipFee> list = fmsPayableShipFeeService.selectFmsPayableShipFeeList(fmsPayableShipFee);
        return getDataTable(list);
    }

    /**
     * 导出财务管理-应付款-物流费用列表
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:export')")
    @Log(title = "财务管理-应付款-物流费用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FmsPayableShipFee fmsPayableShipFee)
    {
        List<FmsPayableShipFee> list = fmsPayableShipFeeService.selectFmsPayableShipFeeList(fmsPayableShipFee);
        ExcelUtil<FmsPayableShipFee> util = new ExcelUtil<FmsPayableShipFee>(FmsPayableShipFee.class);
        util.exportExcel(response, list, "财务管理-应付款-物流费用数据");
    }

    /**
     * 获取财务管理-应付款-物流费用详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fmsPayableShipFeeService.selectFmsPayableShipFeeById(id));
    }

    /**
     * 新增财务管理-应付款-物流费用
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:add')")
    @Log(title = "财务管理-应付款-物流费用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FmsPayableShipFee fmsPayableShipFee)
    {
        return toAjax(fmsPayableShipFeeService.insertFmsPayableShipFee(fmsPayableShipFee));
    }

    /**
     * 修改财务管理-应付款-物流费用
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:edit')")
    @Log(title = "财务管理-应付款-物流费用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FmsPayableShipFee fmsPayableShipFee)
    {
        return toAjax(fmsPayableShipFeeService.updateFmsPayableShipFee(fmsPayableShipFee));
    }

    /**
     * 删除财务管理-应付款-物流费用
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:remove')")
    @Log(title = "财务管理-应付款-物流费用", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fmsPayableShipFeeService.deleteFmsPayableShipFeeByIds(ids));
    }
}
