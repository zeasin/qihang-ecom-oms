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
import com.qihang.erp.api.domain.XhsOrderReceiver;
import com.qihang.erp.api.service.IXhsOrderReceiverService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 订单收件人信息Controller
 * 
 * @author qihang
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/xhs/orderReceiver")
public class XhsOrderReceiverController extends BaseController
{
    @Autowired
    private IXhsOrderReceiverService xhsOrderReceiverService;

    /**
     * 查询订单收件人信息列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:list')")
    @GetMapping("/list")
    public TableDataInfo list(XhsOrderReceiver xhsOrderReceiver)
    {
        startPage();
        List<XhsOrderReceiver> list = xhsOrderReceiverService.selectXhsOrderReceiverList(xhsOrderReceiver);
        return getDataTable(list);
    }

    /**
     * 导出订单收件人信息列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:export')")
    @Log(title = "订单收件人信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XhsOrderReceiver xhsOrderReceiver)
    {
        List<XhsOrderReceiver> list = xhsOrderReceiverService.selectXhsOrderReceiverList(xhsOrderReceiver);
        ExcelUtil<XhsOrderReceiver> util = new ExcelUtil<XhsOrderReceiver>(XhsOrderReceiver.class);
        util.exportExcel(response, list, "订单收件人信息数据");
    }

    /**
     * 获取订单收件人信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(xhsOrderReceiverService.selectXhsOrderReceiverById(id));
    }

    /**
     * 新增订单收件人信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:add')")
    @Log(title = "订单收件人信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XhsOrderReceiver xhsOrderReceiver)
    {
        return toAjax(xhsOrderReceiverService.insertXhsOrderReceiver(xhsOrderReceiver));
    }

    /**
     * 修改订单收件人信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:edit')")
    @Log(title = "订单收件人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XhsOrderReceiver xhsOrderReceiver)
    {
        return toAjax(xhsOrderReceiverService.updateXhsOrderReceiver(xhsOrderReceiver));
    }

    /**
     * 删除订单收件人信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:orderReceiver:remove')")
    @Log(title = "订单收件人信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(xhsOrderReceiverService.deleteXhsOrderReceiverByIds(ids));
    }
}
