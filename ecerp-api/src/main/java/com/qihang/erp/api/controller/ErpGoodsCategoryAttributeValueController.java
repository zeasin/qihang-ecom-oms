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
import com.qihang.erp.api.domain.ErpGoodsCategoryAttributeValue;
import com.qihang.erp.api.service.IErpGoodsCategoryAttributeValueService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品分类属性值Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/categoryAttributeValue")
public class ErpGoodsCategoryAttributeValueController extends BaseController
{
    @Autowired
    private IErpGoodsCategoryAttributeValueService erpGoodsCategoryAttributeValueService;

    /**
     * 查询商品分类属性值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        startPage();
        List<ErpGoodsCategoryAttributeValue> list = erpGoodsCategoryAttributeValueService.selectErpGoodsCategoryAttributeValueList(erpGoodsCategoryAttributeValue);
        return getDataTable(list);
    }

    /**
     * 导出商品分类属性值列表
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:export')")
    @Log(title = "商品分类属性值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        List<ErpGoodsCategoryAttributeValue> list = erpGoodsCategoryAttributeValueService.selectErpGoodsCategoryAttributeValueList(erpGoodsCategoryAttributeValue);
        ExcelUtil<ErpGoodsCategoryAttributeValue> util = new ExcelUtil<ErpGoodsCategoryAttributeValue>(ErpGoodsCategoryAttributeValue.class);
        util.exportExcel(response, list, "商品分类属性值数据");
    }

    /**
     * 获取商品分类属性值详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpGoodsCategoryAttributeValueService.selectErpGoodsCategoryAttributeValueById(id));
    }

    /**
     * 新增商品分类属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:add')")
    @Log(title = "商品分类属性值", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        return toAjax(erpGoodsCategoryAttributeValueService.insertErpGoodsCategoryAttributeValue(erpGoodsCategoryAttributeValue));
    }

    /**
     * 修改商品分类属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:edit')")
    @Log(title = "商品分类属性值", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpGoodsCategoryAttributeValue erpGoodsCategoryAttributeValue)
    {
        return toAjax(erpGoodsCategoryAttributeValueService.updateErpGoodsCategoryAttributeValue(erpGoodsCategoryAttributeValue));
    }

    /**
     * 删除商品分类属性值
     */
    @PreAuthorize("@ss.hasPermi('goods:categoryAttributeValue:remove')")
    @Log(title = "商品分类属性值", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpGoodsCategoryAttributeValueService.deleteErpGoodsCategoryAttributeValueByIds(ids));
    }
}
