package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.zhijian.common.utils.StringUtils;
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
import com.qihang.erp.api.domain.PddOrder;
import com.qihang.erp.api.service.IPddOrderService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 拼多多订单Controller
 * 
 * @author qihang
 * @date 2024-01-02
 */
@RestController
@RequestMapping("/pdd/order")
public class PddOrderController extends BaseController
{
    @Autowired
    private IPddOrderService pddOrderService;

    /**
     * 查询拼多多订单列表
     */
    @PreAuthorize("@ss.hasPermi('pdd:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(PddOrder pddOrder)
    {
        startPage();
        List<PddOrder> list = pddOrderService.selectPddOrderList(pddOrder);
        return getDataTable(list);
    }

    /**
     * 导出拼多多订单列表
     */
    @PreAuthorize("@ss.hasPermi('pdd:order:export')")
    @Log(title = "拼多多订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PddOrder pddOrder)
    {
        List<PddOrder> list = pddOrderService.selectPddOrderList(pddOrder);
        ExcelUtil<PddOrder> util = new ExcelUtil<PddOrder>(PddOrder.class);
        util.exportExcel(response, list, "拼多多订单数据");
    }

    /**
     * 获取拼多多订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('pdd:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pddOrderService.selectPddOrderById(id));
    }

    /**
     * 新增拼多多订单
     */
    @PreAuthorize("@ss.hasPermi('pdd:order:add')")
    @Log(title = "拼多多订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PddOrder pddOrder)
    {
        Integer result = pddOrderService.insertPddOrder(pddOrder);
        if(result == -1) return new AjaxResult(505,"订单号已存在");
        else if(result == -2) return new AjaxResult(506,"请添加商品");
        else if(result == -3) return new AjaxResult(507,"商品数据错误");
        return toAjax(result);
    }

    @Log(title = "拼多多订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm")
    public AjaxResult confirmOrder(@RequestBody PddOrder pddOrder)
    {
        pddOrder.setUpdateBy(getUsername());
        Integer result = pddOrderService.confirmOrder(pddOrder);
        if(result == -1) return new AjaxResult(505,"订单不存在");
        else if(result == -2) return new AjaxResult(506,"订单已确认过了");
        else if(result == -3) return new AjaxResult(507,"订单售后中！无法操作！");
        else if(result == -4) return new AjaxResult(508,"订单号确认过了！请检查订单号是否正确！");
        else if(result == -5) return new AjaxResult(509,"不支持的发货方式！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");
        return toAjax(result);
    }

//    /**
//     * 修改拼多多订单
//     */
//    @PreAuthorize("@ss.hasPermi('pdd:order:edit')")
//    @Log(title = "拼多多订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody PddOrder pddOrder)
//    {
//        return toAjax(pddOrderService.updatePddOrder(pddOrder));
//    }
//
//    /**
//     * 删除拼多多订单
//     */
//    @PreAuthorize("@ss.hasPermi('pdd:order:remove')")
//    @Log(title = "拼多多订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(pddOrderService.deletePddOrderByIds(ids));
//    }
}
