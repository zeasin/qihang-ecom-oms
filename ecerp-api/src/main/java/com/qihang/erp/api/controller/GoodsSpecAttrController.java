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
import com.qihang.erp.api.domain.GoodsSpecAttr;
import com.qihang.erp.api.service.IGoodsSpecAttrService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品规格属性Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goodsSpecAttr")
public class GoodsSpecAttrController extends BaseController
{
    @Autowired
    private IGoodsSpecAttrService goodsSpecAttrService;

    /**
     * 查询商品规格属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsSpecAttr goodsSpecAttr)
    {
        startPage();
        List<GoodsSpecAttr> list = goodsSpecAttrService.selectGoodsSpecAttrList(goodsSpecAttr);
        return getDataTable(list);
    }

    /**
     * 导出商品规格属性列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:export')")
    @Log(title = "商品规格属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsSpecAttr goodsSpecAttr)
    {
        List<GoodsSpecAttr> list = goodsSpecAttrService.selectGoodsSpecAttrList(goodsSpecAttr);
        ExcelUtil<GoodsSpecAttr> util = new ExcelUtil<GoodsSpecAttr>(GoodsSpecAttr.class);
        util.exportExcel(response, list, "商品规格属性数据");
    }

    /**
     * 获取商品规格属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsSpecAttrService.selectGoodsSpecAttrById(id));
    }

    /**
     * 新增商品规格属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:add')")
    @Log(title = "商品规格属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsSpecAttr goodsSpecAttr)
    {
        return toAjax(goodsSpecAttrService.insertGoodsSpecAttr(goodsSpecAttr));
    }

    /**
     * 修改商品规格属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:edit')")
    @Log(title = "商品规格属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsSpecAttr goodsSpecAttr)
    {
        return toAjax(goodsSpecAttrService.updateGoodsSpecAttr(goodsSpecAttr));
    }

    /**
     * 删除商品规格属性
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpecAttr:remove')")
    @Log(title = "商品规格属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(goodsSpecAttrService.deleteGoodsSpecAttrByIds(ids));
    }
}
