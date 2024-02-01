package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qihang.erp.api.domain.ErpOrder;
import com.qihang.erp.api.mapper.ErpOrderMapper;
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
import com.qihang.erp.api.domain.TaoOrder;
import com.qihang.erp.api.service.ITaoOrderService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 淘宝订单Controller
 * 
 * @author qihang
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/tao/order")
public class TaoOrderController extends BaseController
{
    @Autowired
    private ITaoOrderService taoOrderService;


    /**
     * 查询淘宝订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoOrder taoOrder)
    {
        startPage();
        List<TaoOrder> list = taoOrderService.selectTaoOrderList(taoOrder);

        return getDataTable(list);
    }

    /**
     * 导出淘宝订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:order:export')")
    @Log(title = "淘宝订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaoOrder taoOrder)
    {
        List<TaoOrder> list = taoOrderService.selectTaoOrderList(taoOrder);
        ExcelUtil<TaoOrder> util = new ExcelUtil<TaoOrder>(TaoOrder.class);
        util.exportExcel(response, list, "淘宝订单数据");
    }

    /**
     * 获取淘宝订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(taoOrderService.selectTaoOrderById(id+""));
    }

    /**
     * 新增淘宝订单
     */
    @PreAuthorize("@ss.hasPermi('tao:order:add')")
    @Log(title = "淘宝订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaoOrder taoOrder)
    {
        taoOrder.setCreateBy(getUsername());
        int result = taoOrderService.insertTaoOrder(taoOrder);
        if(result == -1) return new AjaxResult(505,"订单号已存在");
        else if(result == -2) return new AjaxResult(506,"请添加商品");
        else if(result == -3) return new AjaxResult(507,"商品数据错误");
        return toAjax(result);
    }
    @Log(title = "淘宝订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody TaoOrder taoOrder)
    {
        taoOrder.setUpdateBy(getUsername());
        int result = taoOrderService.confirmOrder(taoOrder);
        if(result == -1) return new AjaxResult(501,"已确认过了！请勿重复确认！");
        else if(result == -2) return new AjaxResult(502,"订单已存在！请勿重复确认！");
        else if(result == -3) return new AjaxResult(503,"请指定发货方式！");
        else if(result == -4) return new AjaxResult(504,"发货方式不支持！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");


        return toAjax(1);
    }


//    /**
//     * 修改淘宝订单
//     */
//    @PreAuthorize("@ss.hasPermi('tao:order:edit')")
//    @Log(title = "淘宝订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TaoOrder taoOrder)
//    {
//        return toAjax(taoOrderService.updateTaoOrder(taoOrder));
//    }
//
//    /**
//     * 删除淘宝订单
//     */
//    @PreAuthorize("@ss.hasPermi('tao:order:remove')")
//    @Log(title = "淘宝订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(taoOrderService.deleteTaoOrderByIds(ids));
//    }
}
