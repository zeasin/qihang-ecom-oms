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
import com.qihang.erp.api.domain.FmsPayablePurchase;
import com.qihang.erp.api.service.IFmsPayablePurchaseService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 财务管理-应付款-采购货款Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/payablePurchase")
public class FmsPayablePurchaseController extends BaseController
{
    @Autowired
    private IFmsPayablePurchaseService fmsPayablePurchaseService;

    /**
     * 查询财务管理-应付款-采购货款列表
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(FmsPayablePurchase fmsPayablePurchase)
    {
        startPage();
        List<FmsPayablePurchase> list = fmsPayablePurchaseService.selectFmsPayablePurchaseList(fmsPayablePurchase);
        return getDataTable(list);
    }

    /**
     * 导出财务管理-应付款-采购货款列表
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:export')")
    @Log(title = "财务管理-应付款-采购货款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FmsPayablePurchase fmsPayablePurchase)
    {
        List<FmsPayablePurchase> list = fmsPayablePurchaseService.selectFmsPayablePurchaseList(fmsPayablePurchase);
        ExcelUtil<FmsPayablePurchase> util = new ExcelUtil<FmsPayablePurchase>(FmsPayablePurchase.class);
        util.exportExcel(response, list, "财务管理-应付款-采购货款数据");
    }

    /**
     * 获取财务管理-应付款-采购货款详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fmsPayablePurchaseService.selectFmsPayablePurchaseById(id));
    }

    /**
     * 新增财务管理-应付款-采购货款
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:add')")
    @Log(title = "财务管理-应付款-采购货款", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FmsPayablePurchase fmsPayablePurchase)
    {
        return toAjax(fmsPayablePurchaseService.insertFmsPayablePurchase(fmsPayablePurchase));
    }

    /**
     * 修改财务管理-应付款-采购货款
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:edit')")
    @Log(title = "财务管理-应付款-采购货款", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FmsPayablePurchase fmsPayablePurchase)
    {
        return toAjax(fmsPayablePurchaseService.updateFmsPayablePurchase(fmsPayablePurchase));
    }

    /**
     * 删除财务管理-应付款-采购货款
     */
    @PreAuthorize("@ss.hasPermi('fms:payablePurchase:remove')")
    @Log(title = "财务管理-应付款-采购货款", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fmsPayablePurchaseService.deleteFmsPayablePurchaseByIds(ids));
    }
}
