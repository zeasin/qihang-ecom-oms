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
import com.qihang.erp.api.domain.GoodsAttributeConfig;
import com.qihang.erp.api.service.IGoodsAttributeConfigService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品属性配置Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goodsAttributeConfig")
public class GoodsAttributeConfigController extends BaseController
{
    @Autowired
    private IGoodsAttributeConfigService goodsAttributeConfigService;

    /**
     * 查询商品属性配置列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsAttributeConfig goodsAttributeConfig)
    {
        startPage();
        List<GoodsAttributeConfig> list = goodsAttributeConfigService.selectGoodsAttributeConfigList(goodsAttributeConfig);
        return getDataTable(list);
    }

    /**
     * 导出商品属性配置列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:export')")
    @Log(title = "商品属性配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsAttributeConfig goodsAttributeConfig)
    {
        List<GoodsAttributeConfig> list = goodsAttributeConfigService.selectGoodsAttributeConfigList(goodsAttributeConfig);
        ExcelUtil<GoodsAttributeConfig> util = new ExcelUtil<GoodsAttributeConfig>(GoodsAttributeConfig.class);
        util.exportExcel(response, list, "商品属性配置数据");
    }

    /**
     * 获取商品属性配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsAttributeConfigService.selectGoodsAttributeConfigById(id));
    }

    /**
     * 新增商品属性配置
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:add')")
    @Log(title = "商品属性配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsAttributeConfig goodsAttributeConfig)
    {
        return toAjax(goodsAttributeConfigService.insertGoodsAttributeConfig(goodsAttributeConfig));
    }

    /**
     * 修改商品属性配置
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:edit')")
    @Log(title = "商品属性配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsAttributeConfig goodsAttributeConfig)
    {
        return toAjax(goodsAttributeConfigService.updateGoodsAttributeConfig(goodsAttributeConfig));
    }

    /**
     * 删除商品属性配置
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsAttributeConfig:remove')")
    @Log(title = "商品属性配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(goodsAttributeConfigService.deleteGoodsAttributeConfigByIds(ids));
    }
}
