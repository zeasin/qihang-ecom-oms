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
import com.qihang.erp.api.domain.ErpGoodsCategory;
import com.qihang.erp.api.service.IErpGoodsCategoryService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品分类Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/category")
public class ErpGoodsCategoryController extends BaseController
{
    @Autowired
    private IErpGoodsCategoryService erpGoodsCategoryService;

    /**
     * 查询商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('goods:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsCategory erpGoodsCategory)
    {
//        startPage();
        List<ErpGoodsCategory> list = erpGoodsCategoryService.selectErpGoodsCategoryList(erpGoodsCategory);
        return getDataTable(list);
    }

    /**
     * 导出商品分类列表
     */
    @PreAuthorize("@ss.hasPermi('goods:category:export')")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpGoodsCategory erpGoodsCategory)
    {
        List<ErpGoodsCategory> list = erpGoodsCategoryService.selectErpGoodsCategoryList(erpGoodsCategory);
        ExcelUtil<ErpGoodsCategory> util = new ExcelUtil<ErpGoodsCategory>(ErpGoodsCategory.class);
        util.exportExcel(response, list, "商品分类数据");
    }

    /**
     * 获取商品分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpGoodsCategoryService.selectErpGoodsCategoryById(id));
    }

    /**
     * 新增商品分类
     */
    @PreAuthorize("@ss.hasPermi('goods:category:add')")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpGoodsCategory erpGoodsCategory)
    {
        return toAjax(erpGoodsCategoryService.insertErpGoodsCategory(erpGoodsCategory));
    }

    /**
     * 修改商品分类
     */
    @PreAuthorize("@ss.hasPermi('goods:category:edit')")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpGoodsCategory erpGoodsCategory)
    {
        return toAjax(erpGoodsCategoryService.updateErpGoodsCategory(erpGoodsCategory));
    }

    /**
     * 删除商品分类
     */
    @PreAuthorize("@ss.hasPermi('goods:category:remove')")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpGoodsCategoryService.deleteErpGoodsCategoryByIds(ids));
    }
}
