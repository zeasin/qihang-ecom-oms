package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qihang.erp.api.domain.DouOrder;
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
import com.qihang.erp.api.domain.XhsOrder;
import com.qihang.erp.api.service.IXhsOrderService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 小红书订单Controller
 * 
 * @author qihang
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/xhs/order")
public class XhsOrderController extends BaseController
{
    @Autowired
    private IXhsOrderService xhsOrderService;

    /**
     * 查询小红书订单列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(XhsOrder xhsOrder)
    {
        startPage();
        List<XhsOrder> list = xhsOrderService.selectXhsOrderList(xhsOrder);
        return getDataTable(list);
    }

    /**
     * 导出小红书订单列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:order:export')")
    @Log(title = "小红书订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XhsOrder xhsOrder)
    {
        List<XhsOrder> list = xhsOrderService.selectXhsOrderList(xhsOrder);
        ExcelUtil<XhsOrder> util = new ExcelUtil<XhsOrder>(XhsOrder.class);
        util.exportExcel(response, list, "小红书订单数据");
    }

    /**
     * 获取小红书订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(xhsOrderService.selectXhsOrderById(id));
    }

    /**
     * 新增小红书订单
     */
    @PreAuthorize("@ss.hasPermi('xhs:order:add')")
    @Log(title = "小红书订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XhsOrder xhsOrder)
    {
        return toAjax(xhsOrderService.insertXhsOrder(xhsOrder));
    }

    @PreAuthorize("@ss.hasPermi('xhs:order:edit')")
    @Log(title = "小红书订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody XhsOrder bo)
    {
        bo.setUpdateBy(getUsername());
        Integer result = xhsOrderService.confirmOrder(bo);
        if(result == -1) return new AjaxResult(505,"订单不存在");
        else if(result == -2) return new AjaxResult(506,"订单已确认过了");
        else if(result == -3) return new AjaxResult(507,"订单售后中！无法操作！");
        else if(result == -4) return new AjaxResult(508,"订单号确认过了！请检查订单号是否正确！");
        else if(result == -5) return new AjaxResult(509,"不支持的发货方式！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");
        return toAjax(result);
    }
//    /**
//     * 修改小红书订单
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:order:edit')")
//    @Log(title = "小红书订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody XhsOrder xhsOrder)
//    {
//        return toAjax(xhsOrderService.updateXhsOrder(xhsOrder));
//    }
//
//    /**
//     * 删除小红书订单
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:order:remove')")
//    @Log(title = "小红书订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(xhsOrderService.deleteXhsOrderByIds(ids));
//    }
}
