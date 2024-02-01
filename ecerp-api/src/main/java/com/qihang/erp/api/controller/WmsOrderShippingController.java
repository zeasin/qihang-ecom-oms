package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qihang.erp.api.domain.bo.StockingAddVo;
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
import com.qihang.erp.api.domain.WmsOrderShipping;
import com.qihang.erp.api.service.IWmsOrderShippingService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 仓库订单发货Controller
 * 
 * @author qihang
 * @date 2024-01-07
 */
@RestController
@RequestMapping("/wms/shipping")
public class WmsOrderShippingController extends BaseController
{
    @Autowired
    private IWmsOrderShippingService wmsOrderShippingService;

    /**
     * 查询仓库订单发货列表
     */
    @PreAuthorize("@ss.hasPermi('wms:shipping:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsOrderShipping wmsOrderShipping)
    {
        startPage();
        List<WmsOrderShipping> list = wmsOrderShippingService.selectWmsOrderShippingList(wmsOrderShipping);
        return getDataTable(list);
    }

    /**
     * 导出仓库订单发货列表
     */
    @PreAuthorize("@ss.hasPermi('wms:shipping:export')")
    @Log(title = "仓库订单发货", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsOrderShipping wmsOrderShipping)
    {
        List<WmsOrderShipping> list = wmsOrderShippingService.selectWmsOrderShippingList(wmsOrderShipping);
        ExcelUtil<WmsOrderShipping> util = new ExcelUtil<WmsOrderShipping>(WmsOrderShipping.class);
        util.exportExcel(response, list, "仓库订单发货数据");
    }

    /**
     * 获取仓库订单发货详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:shipping:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsOrderShippingService.selectWmsOrderShippingById(id));
    }

    /**
     * 生成拣货单
     * @param addVo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wms:shipping:add')")
    @Log(title = "仓库订单发货", businessType = BusinessType.INSERT)
    @PostMapping("/stockingAdd")
    public AjaxResult stockingAdd(@RequestBody StockingAddVo addVo)
    {
        addVo.setCreateBy(getUsername());
        if(addVo.getIds() == null || addVo.getIds().length ==0) return new AjaxResult(502,"没有选择备货商品");
        int result = wmsOrderShippingService.stockingAdd(addVo);
        if(result == -1) return new AjaxResult(501,"状态不是待处理的，无法生成拣货单！");
        else if(result == -2) return new AjaxResult(503,"商品库存不足！");
        else if(result == -9) return new AjaxResult(509,"没有选择备货商品！");

        return toAjax(result);
    }

//    /**
//     * 新增仓库订单发货
//     */
//    @PreAuthorize("@ss.hasPermi('wms:shipping:add')")
//    @Log(title = "仓库订单发货", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody WmsOrderShipping wmsOrderShipping)
//    {
//        return toAjax(wmsOrderShippingService.insertWmsOrderShipping(wmsOrderShipping));
//    }
//
//    /**
//     * 修改仓库订单发货
//     */
//    @PreAuthorize("@ss.hasPermi('wms:shipping:edit')")
//    @Log(title = "仓库订单发货", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody WmsOrderShipping wmsOrderShipping)
//    {
//        return toAjax(wmsOrderShippingService.updateWmsOrderShipping(wmsOrderShipping));
//    }

//    /**
//     * 删除仓库订单发货
//     */
//    @PreAuthorize("@ss.hasPermi('wms:shipping:remove')")
//    @Log(title = "仓库订单发货", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(wmsOrderShippingService.deleteWmsOrderShippingByIds(ids));
//    }
}
