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
import com.qihang.erp.api.domain.TaoOrderRefund;
import com.qihang.erp.api.service.ITaoOrderRefundService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 淘宝退款订单Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/tao/taoRefund")
public class TaoOrderRefundController extends BaseController
{
    @Autowired
    private ITaoOrderRefundService taoOrderRefundService;

    /**
     * 查询淘宝退款订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoOrderRefund taoOrderRefund)
    {
        startPage();
        List<TaoOrderRefund> list = taoOrderRefundService.selectTaoOrderRefundList(taoOrderRefund);
        return getDataTable(list);
    }

    /**
     * 导出淘宝退款订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:export')")
    @Log(title = "淘宝退款订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaoOrderRefund taoOrderRefund)
    {
        List<TaoOrderRefund> list = taoOrderRefundService.selectTaoOrderRefundList(taoOrderRefund);
        ExcelUtil<TaoOrderRefund> util = new ExcelUtil<TaoOrderRefund>(TaoOrderRefund.class);
        util.exportExcel(response, list, "淘宝退款订单数据");
    }

    /**
     * 获取淘宝退款订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(taoOrderRefundService.selectTaoOrderRefundById(id));
    }

    /**
     * 新增淘宝退款订单
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:add')")
    @Log(title = "淘宝退款订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaoOrderRefund taoOrderRefund)
    {
        taoOrderRefund.setCreateBy(getUsername());
        int result = taoOrderRefundService.insertTaoOrderRefund(taoOrderRefund);
        if(result == -1) return new AjaxResult(501,"子订单数据不存在");
        else if(result == -2) return new AjaxResult(502,"子订单已经在售后中！请勿重复提交！");
        return toAjax(result);
    }

    /**
     * 修改淘宝退款订单
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:edit')")
    @Log(title = "淘宝退款订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaoOrderRefund taoOrderRefund)
    {
        taoOrderRefund.setUpdateBy(getUsername());
        int result = taoOrderRefundService.confirmRefund(taoOrderRefund);
        if(result == -1) return new AjaxResult(501,"数据不存在");
        else if(result == -2) return new AjaxResult(502,"已处理！请勿重复提交！");
        else if(result == -11) return new AjaxResult(511,"数据错误：specNumber编码找不到数据！");
        else if(result == -21) return new AjaxResult(521,"数据错误：ErpOrderItem未找到！");
        return toAjax(1);
    }
//
//    /**
//     * 删除淘宝退款订单
//     */
//    @PreAuthorize("@ss.hasPermi('tao:taoRefund:remove')")
//    @Log(title = "淘宝退款订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(taoOrderRefundService.deleteTaoOrderRefundByIds(ids));
//    }
}
