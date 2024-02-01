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
import com.qihang.erp.api.domain.ScmSupplierAgentShipping;
import com.qihang.erp.api.service.IScmSupplierAgentShippingService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 供应商代发货Controller
 * 
 * @author qihang
 * @date 2024-01-07
 */
@RestController
@RequestMapping("/scm/agentShipping")
public class ScmSupplierAgentShippingController extends BaseController
{
    @Autowired
    private IScmSupplierAgentShippingService scmSupplierAgentShippingService;

    /**
     * 查询供应商代发货列表
     */
    @PreAuthorize("@ss.hasPermi('scm:agentShipping:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        startPage();
        List<ScmSupplierAgentShipping> list = scmSupplierAgentShippingService.selectScmSupplierAgentShippingList(scmSupplierAgentShipping);
        return getDataTable(list);
    }

    /**
     * 导出供应商代发货列表
     */
    @PreAuthorize("@ss.hasPermi('scm:agentShipping:export')")
    @Log(title = "供应商代发货", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        List<ScmSupplierAgentShipping> list = scmSupplierAgentShippingService.selectScmSupplierAgentShippingList(scmSupplierAgentShipping);
        ExcelUtil<ScmSupplierAgentShipping> util = new ExcelUtil<ScmSupplierAgentShipping>(ScmSupplierAgentShipping.class);
        util.exportExcel(response, list, "供应商代发货数据");
    }

    /**
     * 获取供应商代发货详细信息
     */
    @PreAuthorize("@ss.hasPermi('scm:agentShipping:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scmSupplierAgentShippingService.selectScmSupplierAgentShippingById(id));
    }

//    /**
//     * 新增供应商代发货
//     */
//    @PreAuthorize("@ss.hasPermi('scm:agentShipping:add')")
//    @Log(title = "供应商代发货", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ScmSupplierAgentShipping scmSupplierAgentShipping)
//    {
//        return toAjax(scmSupplierAgentShippingService.insertScmSupplierAgentShipping(scmSupplierAgentShipping));
//    }

    /**
     * 发货
     */
    @PreAuthorize("@ss.hasPermi('scm:agentShipping:edit')")
    @Log(title = "供应商代发货", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        scmSupplierAgentShipping.setUpdateBy(getUsername());
        Integer result = scmSupplierAgentShippingService.updateScmSupplierAgentShipping(scmSupplierAgentShipping);
        if(result == -1) return new AjaxResult(505,"订单号已存在");
        else if(result == -2) return new AjaxResult(506,"该订单无需发货");
        else if(result == -3) return new AjaxResult(507,"商品数据错误");

        return toAjax(result);
    }

//    /**
//     * 删除供应商代发货
//     */
//    @PreAuthorize("@ss.hasPermi('scm:agentShipping:remove')")
//    @Log(title = "供应商代发货", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(scmSupplierAgentShippingService.deleteScmSupplierAgentShippingByIds(ids));
//    }
}
