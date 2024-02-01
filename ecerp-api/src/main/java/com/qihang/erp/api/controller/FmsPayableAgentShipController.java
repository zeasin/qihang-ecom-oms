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
import com.qihang.erp.api.domain.FmsPayableAgentShip;
import com.qihang.erp.api.service.IFmsPayableAgentShipService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 财务管理-应付款-代发账单Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/agentShip")
public class FmsPayableAgentShipController extends BaseController
{
    @Autowired
    private IFmsPayableAgentShipService fmsPayableAgentShipService;

    /**
     * 查询财务管理-应付款-代发账单列表
     */
    @PreAuthorize("@ss.hasPermi('fms:agentShip:list')")
    @GetMapping("/list")
    public TableDataInfo list(FmsPayableAgentShip fmsPayableAgentShip)
    {
        startPage();
        List<FmsPayableAgentShip> list = fmsPayableAgentShipService.selectFmsPayableAgentShipList(fmsPayableAgentShip);
        return getDataTable(list);
    }

    /**
     * 导出财务管理-应付款-代发账单列表
     */
    @PreAuthorize("@ss.hasPermi('fms:agentShip:export')")
    @Log(title = "财务管理-应付款-代发账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FmsPayableAgentShip fmsPayableAgentShip)
    {
        List<FmsPayableAgentShip> list = fmsPayableAgentShipService.selectFmsPayableAgentShipList(fmsPayableAgentShip);
        ExcelUtil<FmsPayableAgentShip> util = new ExcelUtil<FmsPayableAgentShip>(FmsPayableAgentShip.class);
        util.exportExcel(response, list, "财务管理-应付款-代发账单数据");
    }

    /**
     * 获取财务管理-应付款-代发账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:agentShip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fmsPayableAgentShipService.selectFmsPayableAgentShipById(id));
    }

//    /**
//     * 新增财务管理-应付款-代发账单
//     */
//    @PreAuthorize("@ss.hasPermi('fms:agentShip:add')")
//    @Log(title = "财务管理-应付款-代发账单", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody FmsPayableAgentShip fmsPayableAgentShip)
//    {
//        return toAjax(fmsPayableAgentShipService.insertFmsPayableAgentShip(fmsPayableAgentShip));
//    }
//
//    /**
//     * 修改财务管理-应付款-代发账单
//     */
//    @PreAuthorize("@ss.hasPermi('fms:agentShip:edit')")
//    @Log(title = "财务管理-应付款-代发账单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody FmsPayableAgentShip fmsPayableAgentShip)
//    {
//        return toAjax(fmsPayableAgentShipService.updateFmsPayableAgentShip(fmsPayableAgentShip));
//    }
//
//    /**
//     * 删除财务管理-应付款-代发账单
//     */
//    @PreAuthorize("@ss.hasPermi('fms:agentShip:remove')")
//    @Log(title = "财务管理-应付款-代发账单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(fmsPayableAgentShipService.deleteFmsPayableAgentShipByIds(ids));
//    }
}
