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
import com.qihang.erp.api.domain.ErpGoodsCategoryAttribute;
import com.qihang.erp.api.service.IErpGoodsCategoryAttributeService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品分类属性Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/categoryAttribute")
public class ErpGoodsCategoryAttributeController extends BaseController
{
    @Autowired
    private IErpGoodsCategoryAttributeService erpGoodsCategoryAttributeService;

    /**
     * 查询商品分类属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        startPage();
        List<ErpGoodsCategoryAttribute> list = erpGoodsCategoryAttributeService.selectErpGoodsCategoryAttributeList(erpGoodsCategoryAttribute);
        return getDataTable(list);
    }

    /**
     * 导出商品分类属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:export')")
    @Log(title = "商品分类属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        List<ErpGoodsCategoryAttribute> list = erpGoodsCategoryAttributeService.selectErpGoodsCategoryAttributeList(erpGoodsCategoryAttribute);
        ExcelUtil<ErpGoodsCategoryAttribute> util = new ExcelUtil<ErpGoodsCategoryAttribute>(ErpGoodsCategoryAttribute.class);
        util.exportExcel(response, list, "商品分类属性数据");
    }

    /**
     * 获取商品分类属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpGoodsCategoryAttributeService.selectErpGoodsCategoryAttributeById(id));
    }

    /**
     * 新增商品分类属性
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:add')")
    @Log(title = "商品分类属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        return toAjax(erpGoodsCategoryAttributeService.insertErpGoodsCategoryAttribute(erpGoodsCategoryAttribute));
    }

    /**
     * 修改商品分类属性
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:edit')")
    @Log(title = "商品分类属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpGoodsCategoryAttribute erpGoodsCategoryAttribute)
    {
        return toAjax(erpGoodsCategoryAttributeService.updateErpGoodsCategoryAttribute(erpGoodsCategoryAttribute));
    }

    /**
     * 删除商品分类属性
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttribute:remove')")
    @Log(title = "商品分类属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpGoodsCategoryAttributeService.deleteErpGoodsCategoryAttributeByIds(ids));
    }
}
