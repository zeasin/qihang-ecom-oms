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
import com.qihang.erp.api.domain.BLogisticsCompany;
import com.qihang.erp.api.service.IBLogisticsCompanyService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 物流公司Controller
 * 
 * @author qihang
 * @date 2024-01-12
 */
@RestController
@RequestMapping("/api/logistics")
public class BLogisticsCompanyController extends BaseController
{
    @Autowired
    private IBLogisticsCompanyService bLogisticsCompanyService;

    /**
     * 查询物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(BLogisticsCompany bLogisticsCompany)
    {
        startPage();
        List<BLogisticsCompany> list = bLogisticsCompanyService.selectBLogisticsCompanyList(bLogisticsCompany);
        return getDataTable(list);
    }

    /**
     * 导出物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:export')")
    @Log(title = "物流公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BLogisticsCompany bLogisticsCompany)
    {
        List<BLogisticsCompany> list = bLogisticsCompanyService.selectBLogisticsCompanyList(bLogisticsCompany);
        ExcelUtil<BLogisticsCompany> util = new ExcelUtil<BLogisticsCompany>(BLogisticsCompany.class);
        util.exportExcel(response, list, "物流公司数据");
    }

    /**
     * 获取物流公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bLogisticsCompanyService.selectBLogisticsCompanyById(id));
    }

    /**
     * 新增物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:add')")
    @Log(title = "物流公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BLogisticsCompany bLogisticsCompany)
    {
        return toAjax(bLogisticsCompanyService.insertBLogisticsCompany(bLogisticsCompany));
    }

    /**
     * 修改物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:edit')")
    @Log(title = "物流公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BLogisticsCompany bLogisticsCompany)
    {
        return toAjax(bLogisticsCompanyService.updateBLogisticsCompany(bLogisticsCompany));
    }

    /**
     * 删除物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:remove')")
    @Log(title = "物流公司", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bLogisticsCompanyService.deleteBLogisticsCompanyByIds(ids));
    }
}
