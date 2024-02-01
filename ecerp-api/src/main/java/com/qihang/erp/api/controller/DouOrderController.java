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
import com.qihang.erp.api.domain.DouOrder;
import com.qihang.erp.api.service.IDouOrderService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 抖店订单Controller
 * 
 * @author qihang
 * @date 2024-01-02
 */
@RestController
@RequestMapping("/dou/order")
public class DouOrderController extends BaseController
{
    @Autowired
    private IDouOrderService douOrderService;

    /**
     * 查询抖店订单列表
     */
    @PreAuthorize("@ss.hasPermi('dou:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(DouOrder douOrder)
    {
        startPage();
        List<DouOrder> list = douOrderService.selectDouOrderList(douOrder);
        return getDataTable(list);
    }

    /**
     * 导出抖店订单列表
     */
    @PreAuthorize("@ss.hasPermi('dou:order:export')")
    @Log(title = "抖店订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DouOrder douOrder)
    {
        List<DouOrder> list = douOrderService.selectDouOrderList(douOrder);
        ExcelUtil<DouOrder> util = new ExcelUtil<DouOrder>(DouOrder.class);
        util.exportExcel(response, list, "抖店订单数据");
    }

    /**
     * 获取抖店订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('dou:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(douOrderService.selectDouOrderById(id));
    }

    /**
     * 新增抖店订单
     */
    @PreAuthorize("@ss.hasPermi('dou:order:add')")
    @Log(title = "抖店订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DouOrder douOrder)
    {
        douOrder.setCreateBy(getUsername());
        return toAjax(douOrderService.insertDouOrder(douOrder));
    }

    /**
     * 修改抖店订单
     */
    @PreAuthorize("@ss.hasPermi('dou:order:edit')")
    @Log(title = "抖店订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody DouOrder douOrder)
    {
        douOrder.setUpdateBy(getUsername());
        Integer result = douOrderService.confirmOrder(douOrder);
        if(result == -1) return new AjaxResult(505,"订单不存在");
        else if(result == -2) return new AjaxResult(506,"订单已确认过了");
        else if(result == -3) return new AjaxResult(507,"订单售后中！无法操作！");
        else if(result == -4) return new AjaxResult(508,"订单号确认过了！请检查订单号是否正确！");
        else if(result == -5) return new AjaxResult(509,"不支持的发货方式！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");
        return toAjax(result);
    }
//
//    /**
//     * 删除抖店订单
//     */
//    @PreAuthorize("@ss.hasPermi('dou:order:remove')")
//    @Log(title = "抖店订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(douOrderService.deleteDouOrderByIds(ids));
//    }
}
