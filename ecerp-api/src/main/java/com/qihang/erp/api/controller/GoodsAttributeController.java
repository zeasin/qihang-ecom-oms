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
import com.qihang.erp.api.domain.GoodsAttribute;
import com.qihang.erp.api.service.IGoodsAttributeService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品属性Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goodsAttribute")
public class GoodsAttributeController extends BaseController
{
    @Autowired
    private IGoodsAttributeService goodsAttributeService;

    /**
     * 查询商品属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsAttribute goodsAttribute)
    {
        startPage();
        List<GoodsAttribute> list = goodsAttributeService.selectGoodsAttributeList(goodsAttribute);
        return getDataTable(list);
    }

    /**
     * 导出商品属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:export')")
    @Log(title = "商品属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsAttribute goodsAttribute)
    {
        List<GoodsAttribute> list = goodsAttributeService.selectGoodsAttributeList(goodsAttribute);
        ExcelUtil<GoodsAttribute> util = new ExcelUtil<GoodsAttribute>(GoodsAttribute.class);
        util.exportExcel(response, list, "商品属性数据");
    }

    /**
     * 获取商品属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsAttributeService.selectGoodsAttributeById(id));
    }

    /**
     * 新增商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:add')")
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsAttribute goodsAttribute)
    {
        return toAjax(goodsAttributeService.insertGoodsAttribute(goodsAttribute));
    }

    /**
     * 修改商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:edit')")
    @Log(title = "商品属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsAttribute goodsAttribute)
    {
        return toAjax(goodsAttributeService.updateGoodsAttribute(goodsAttribute));
    }

    /**
     * 删除商品属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttribute:remove')")
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(goodsAttributeService.deleteGoodsAttributeByIds(ids));
    }
}
