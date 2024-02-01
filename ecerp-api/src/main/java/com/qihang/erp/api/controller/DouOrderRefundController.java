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
import com.qihang.erp.api.domain.DouOrderRefund;
import com.qihang.erp.api.service.IDouOrderRefundService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 抖店订单退款Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/dou/douRefund")
public class DouOrderRefundController extends BaseController
{
    @Autowired
    private IDouOrderRefundService douOrderRefundService;

    /**
     * 查询抖店订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('dou:douRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(DouOrderRefund douOrderRefund)
    {
        startPage();
        List<DouOrderRefund> list = douOrderRefundService.selectDouOrderRefundList(douOrderRefund);
        return getDataTable(list);
    }

    /**
     * 导出抖店订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('dou:douRefund:export')")
    @Log(title = "抖店订单退款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DouOrderRefund douOrderRefund)
    {
        List<DouOrderRefund> list = douOrderRefundService.selectDouOrderRefundList(douOrderRefund);
        ExcelUtil<DouOrderRefund> util = new ExcelUtil<DouOrderRefund>(DouOrderRefund.class);
        util.exportExcel(response, list, "抖店订单退款数据");
    }

    /**
     * 获取抖店订单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('dou:douRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(douOrderRefundService.selectDouOrderRefundById(id));
    }

    /**
     * 新增抖店订单退款
     */
    @PreAuthorize("@ss.hasPermi('dou:douRefund:add')")
    @Log(title = "抖店订单退款", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DouOrderRefund douOrderRefund)
    {
        int result = douOrderRefundService.insertDouOrderRefund(douOrderRefund);
        if(result == -1) return new AjaxResult(501,"子订单数据不存在");
        else if(result == -2) return new AjaxResult(502,"子订单已经在售后中！请勿重复提交！");
        return toAjax(result);
    }
//
//    /**
//     * 修改抖店订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('dou:douRefund:edit')")
//    @Log(title = "抖店订单退款", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody DouOrderRefund douOrderRefund)
//    {
//        return toAjax(douOrderRefundService.updateDouOrderRefund(douOrderRefund));
//    }
//
//    /**
//     * 删除抖店订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('dou:douRefund:remove')")
//    @Log(title = "抖店订单退款", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(douOrderRefundService.deleteDouOrderRefundByIds(ids));
//    }
}
