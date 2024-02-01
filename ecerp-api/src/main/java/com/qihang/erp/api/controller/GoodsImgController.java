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
import com.qihang.erp.api.domain.GoodsImg;
import com.qihang.erp.api.service.IGoodsImgService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品图片Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goodsImg")
public class GoodsImgController extends BaseController
{
    @Autowired
    private IGoodsImgService goodsImgService;

    /**
     * 查询商品图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsImg goodsImg)
    {
        startPage();
        List<GoodsImg> list = goodsImgService.selectGoodsImgList(goodsImg);
        return getDataTable(list);
    }

    /**
     * 导出商品图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:export')")
    @Log(title = "商品图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsImg goodsImg)
    {
        List<GoodsImg> list = goodsImgService.selectGoodsImgList(goodsImg);
        ExcelUtil<GoodsImg> util = new ExcelUtil<GoodsImg>(GoodsImg.class);
        util.exportExcel(response, list, "商品图片数据");
    }

    /**
     * 获取商品图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsImgService.selectGoodsImgById(id));
    }

    /**
     * 新增商品图片
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:add')")
    @Log(title = "商品图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsImg goodsImg)
    {
        return toAjax(goodsImgService.insertGoodsImg(goodsImg));
    }

    /**
     * 修改商品图片
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:edit')")
    @Log(title = "商品图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsImg goodsImg)
    {
        return toAjax(goodsImgService.updateGoodsImg(goodsImg));
    }

    /**
     * 删除商品图片
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsImg:remove')")
    @Log(title = "商品图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(goodsImgService.deleteGoodsImgByIds(ids));
    }
}
