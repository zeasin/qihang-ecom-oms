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
import com.qihang.erp.api.domain.XhsRefund;
import com.qihang.erp.api.service.IXhsRefundService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 小红书订单退款Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/xhs/xhsRefund")
public class XhsRefundController extends BaseController
{
    @Autowired
    private IXhsRefundService xhsRefundService;

    /**
     * 查询小红书订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(XhsRefund xhsRefund)
    {
        startPage();
        List<XhsRefund> list = xhsRefundService.selectXhsRefundList(xhsRefund);
        return getDataTable(list);
    }

    /**
     * 导出小红书订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:export')")
    @Log(title = "小红书订单退款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XhsRefund xhsRefund)
    {
        List<XhsRefund> list = xhsRefundService.selectXhsRefundList(xhsRefund);
        ExcelUtil<XhsRefund> util = new ExcelUtil<XhsRefund>(XhsRefund.class);
        util.exportExcel(response, list, "小红书订单退款数据");
    }

    /**
     * 获取小红书订单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(xhsRefundService.selectXhsRefundById(id));
    }

//    /**
//     * 新增小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:add')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody XhsRefund xhsRefund)
//    {
//        return toAjax(xhsRefundService.insertXhsRefund(xhsRefund));
//    }
//
//    /**
//     * 修改小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:edit')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody XhsRefund xhsRefund)
//    {
//        return toAjax(xhsRefundService.updateXhsRefund(xhsRefund));
//    }
//
//    /**
//     * 删除小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:remove')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(xhsRefundService.deleteXhsRefundByIds(ids));
//    }
}
