package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qihang.erp.api.domain.ErpOrder;
import com.qihang.erp.api.service.IErpOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhijian.common.annotation.Log;
import com.zhijian.common.core.controller.BaseController;
import com.zhijian.common.core.domain.AjaxResult;
import com.zhijian.common.enums.BusinessType;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 店铺订单Controller
 *
 * @author qihang
 * @date 2023-12-31
 */
@RestController
@RequestMapping("/api/order")
public class ErpOrderController extends BaseController
{
    @Autowired
    private IErpOrderService orderService;

    /**
     * 查询店铺订单列表
     */
    @PreAuthorize("@ss.hasPermi('shop:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpOrder order)
    {
        startPage();
        List<ErpOrder> list = orderService.selectErpOrderList(order);
        return getDataTable(list);
    }

    /**
     * 导出店铺订单列表
     */
    @PreAuthorize("@ss.hasPermi('shop:order:export')")
    @Log(title = "店铺订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpOrder order)
    {
        List<ErpOrder> list = orderService.selectErpOrderList(order);
        ExcelUtil<ErpOrder> util = new ExcelUtil<ErpOrder>(ErpOrder.class);
        util.exportExcel(response, list, "店铺订单数据");
    }

    /**
     * 获取店铺订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.selectErpOrderById(id));
    }

    /**
     * 新增店铺订单
     */
    @PreAuthorize("@ss.hasPermi('shop:order:add')")
    @Log(title = "店铺订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpOrder order)
    {
        order.setCreateBy(getUsername());
        int result = orderService.insertErpOrder(order);
        if(result == -1) return new AjaxResult(501,"订单号已存在！");
        return toAjax(result);
    }

    /**
     * 订单发货
     * @param order
     * @return
     */
    @Log(title = "店铺订单", businessType = BusinessType.UPDATE)
    @PostMapping("/ship")
    public AjaxResult ship(@RequestBody ErpOrder order)
    {
        order.setUpdateBy(getUsername());
        int result = orderService.shipErpOrder(order);
        if(result == -1) return new AjaxResult(501,"订单不存在！");
        else if(result == -2) return new AjaxResult(502,"订单号已存在！");
        return toAjax(result);
    }

}
