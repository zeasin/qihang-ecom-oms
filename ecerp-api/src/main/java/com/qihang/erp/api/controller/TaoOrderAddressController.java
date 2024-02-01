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
import com.qihang.erp.api.domain.TaoOrderAddress;
import com.qihang.erp.api.service.ITaoOrderAddressService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 淘宝订单地址Controller
 * 
 * @author qihang
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/tao/orderAddress")
public class TaoOrderAddressController extends BaseController
{
    @Autowired
    private ITaoOrderAddressService taoOrderAddressService;

    /**
     * 查询淘宝订单地址列表
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoOrderAddress taoOrderAddress)
    {
        startPage();
        List<TaoOrderAddress> list = taoOrderAddressService.selectTaoOrderAddressList(taoOrderAddress);
        return getDataTable(list);
    }

    /**
     * 导出淘宝订单地址列表
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:export')")
    @Log(title = "淘宝订单地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaoOrderAddress taoOrderAddress)
    {
        List<TaoOrderAddress> list = taoOrderAddressService.selectTaoOrderAddressList(taoOrderAddress);
        ExcelUtil<TaoOrderAddress> util = new ExcelUtil<TaoOrderAddress>(TaoOrderAddress.class);
        util.exportExcel(response, list, "淘宝订单地址数据");
    }

    /**
     * 获取淘宝订单地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(taoOrderAddressService.selectTaoOrderAddressById(id));
    }

    /**
     * 新增淘宝订单地址
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:add')")
    @Log(title = "淘宝订单地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaoOrderAddress taoOrderAddress)
    {
        return toAjax(taoOrderAddressService.insertTaoOrderAddress(taoOrderAddress));
    }

    /**
     * 修改淘宝订单地址
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:edit')")
    @Log(title = "淘宝订单地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaoOrderAddress taoOrderAddress)
    {
        return toAjax(taoOrderAddressService.updateTaoOrderAddress(taoOrderAddress));
    }

    /**
     * 删除淘宝订单地址
     */
    @PreAuthorize("@ss.hasPermi('tao:orderAddress:remove')")
    @Log(title = "淘宝订单地址", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taoOrderAddressService.deleteTaoOrderAddressByIds(ids));
    }
}
