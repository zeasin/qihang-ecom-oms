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
import com.qihang.erp.api.domain.WmsStockOutEntryItemDetail;
import com.qihang.erp.api.service.IWmsStockOutEntryItemDetailService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 出库明细详情Controller
 * 
 * @author qihang
 * @date 2024-01-10
 */
@RestController
@RequestMapping("/wms/stockOutEntryItemDetail")
public class WmsStockOutEntryItemDetailController extends BaseController
{
    @Autowired
    private IWmsStockOutEntryItemDetailService wmsStockOutEntryItemDetailService;

    /**
     * 查询出库明细详情列表
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        startPage();
        List<WmsStockOutEntryItemDetail> list = wmsStockOutEntryItemDetailService.selectWmsStockOutEntryItemDetailList(wmsStockOutEntryItemDetail);
        return getDataTable(list);
    }

    /**
     * 导出出库明细详情列表
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:export')")
    @Log(title = "出库明细详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        List<WmsStockOutEntryItemDetail> list = wmsStockOutEntryItemDetailService.selectWmsStockOutEntryItemDetailList(wmsStockOutEntryItemDetail);
        ExcelUtil<WmsStockOutEntryItemDetail> util = new ExcelUtil<WmsStockOutEntryItemDetail>(WmsStockOutEntryItemDetail.class);
        util.exportExcel(response, list, "出库明细详情数据");
    }

    /**
     * 获取出库明细详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsStockOutEntryItemDetailService.selectWmsStockOutEntryItemDetailById(id));
    }

    /**
     * 新增出库明细详情
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:add')")
    @Log(title = "出库明细详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        return toAjax(wmsStockOutEntryItemDetailService.insertWmsStockOutEntryItemDetail(wmsStockOutEntryItemDetail));
    }

    /**
     * 修改出库明细详情
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:edit')")
    @Log(title = "出库明细详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        return toAjax(wmsStockOutEntryItemDetailService.updateWmsStockOutEntryItemDetail(wmsStockOutEntryItemDetail));
    }

    /**
     * 删除出库明细详情
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntryItemDetail:remove')")
    @Log(title = "出库明细详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsStockOutEntryItemDetailService.deleteWmsStockOutEntryItemDetailByIds(ids));
    }
}
