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
import com.qihang.erp.api.domain.FmsReceivableOrder;
import com.qihang.erp.api.service.IFmsReceivableOrderService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 财务管理-应收款-订单收入Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/receivableOrder")
public class FmsReceivableOrderController extends BaseController
{
    @Autowired
    private IFmsReceivableOrderService fmsReceivableOrderService;

    /**
     * 查询财务管理-应收款-订单收入列表
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(FmsReceivableOrder fmsReceivableOrder)
    {
        startPage();
        List<FmsReceivableOrder> list = fmsReceivableOrderService.selectFmsReceivableOrderList(fmsReceivableOrder);
        return getDataTable(list);
    }

    /**
     * 导出财务管理-应收款-订单收入列表
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:export')")
    @Log(title = "财务管理-应收款-订单收入", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FmsReceivableOrder fmsReceivableOrder)
    {
        List<FmsReceivableOrder> list = fmsReceivableOrderService.selectFmsReceivableOrderList(fmsReceivableOrder);
        ExcelUtil<FmsReceivableOrder> util = new ExcelUtil<FmsReceivableOrder>(FmsReceivableOrder.class);
        util.exportExcel(response, list, "财务管理-应收款-订单收入数据");
    }

    /**
     * 获取财务管理-应收款-订单收入详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fmsReceivableOrderService.selectFmsReceivableOrderById(id));
    }

    /**
     * 新增财务管理-应收款-订单收入
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:add')")
    @Log(title = "财务管理-应收款-订单收入", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FmsReceivableOrder fmsReceivableOrder)
    {
        return toAjax(fmsReceivableOrderService.insertFmsReceivableOrder(fmsReceivableOrder));
    }

    /**
     * 修改财务管理-应收款-订单收入
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:edit')")
    @Log(title = "财务管理-应收款-订单收入", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FmsReceivableOrder fmsReceivableOrder)
    {
        return toAjax(fmsReceivableOrderService.updateFmsReceivableOrder(fmsReceivableOrder));
    }

    /**
     * 删除财务管理-应收款-订单收入
     */
    @PreAuthorize("@ss.hasPermi('fms:receivableOrder:remove')")
    @Log(title = "财务管理-应收款-订单收入", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fmsReceivableOrderService.deleteFmsReceivableOrderByIds(ids));
    }
}
