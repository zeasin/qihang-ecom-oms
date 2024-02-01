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
import com.qihang.erp.api.domain.PddOrderRefund;
import com.qihang.erp.api.service.IPddOrderRefundService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 拼多多订单退款Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/pdd/pddRefund")
public class PddOrderRefundController extends BaseController
{
    @Autowired
    private IPddOrderRefundService pddOrderRefundService;

    /**
     * 查询拼多多订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(PddOrderRefund pddOrderRefund)
    {
        startPage();
        List<PddOrderRefund> list = pddOrderRefundService.selectPddOrderRefundList(pddOrderRefund);
        return getDataTable(list);
    }

    /**
     * 导出拼多多订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:export')")
    @Log(title = "拼多多订单退款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PddOrderRefund pddOrderRefund)
    {
        List<PddOrderRefund> list = pddOrderRefundService.selectPddOrderRefundList(pddOrderRefund);
        ExcelUtil<PddOrderRefund> util = new ExcelUtil<PddOrderRefund>(PddOrderRefund.class);
        util.exportExcel(response, list, "拼多多订单退款数据");
    }

    /**
     * 获取拼多多订单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pddOrderRefundService.selectPddOrderRefundById(id));
    }

    /**
     * 新增拼多多订单退款
     */
    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:add')")
    @Log(title = "拼多多订单退款", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PddOrderRefund pddOrderRefund)
    {
        int result = pddOrderRefundService.insertPddOrderRefund(pddOrderRefund);
        if(result == -1) return new AjaxResult(501,"子订单数据不存在");
        else if(result == -2) return new AjaxResult(502,"子订单已经在售后中！请勿重复提交！");
        return toAjax(result);
    }
//
//    /**
//     * 修改拼多多订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:edit')")
//    @Log(title = "拼多多订单退款", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody PddOrderRefund pddOrderRefund)
//    {
//        return toAjax(pddOrderRefundService.updatePddOrderRefund(pddOrderRefund));
//    }
//
//    /**
//     * 删除拼多多订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('pdd:pddRefund:remove')")
//    @Log(title = "拼多多订单退款", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(pddOrderRefundService.deletePddOrderRefundByIds(ids));
//    }
}
