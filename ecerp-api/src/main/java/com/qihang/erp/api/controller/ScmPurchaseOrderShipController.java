package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qihang.erp.api.domain.bo.PurchaseOrderStockInBo;
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
import com.qihang.erp.api.domain.ScmPurchaseOrderShip;
import com.qihang.erp.api.service.IScmPurchaseOrderShipService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 采购订单物流Controller
 * 
 * @author qihang
 * @date 2023-12-30
 */
@RestController
@RequestMapping("/purchase/PurchaseOrderShip")
public class ScmPurchaseOrderShipController extends BaseController
{
    @Autowired
    private IScmPurchaseOrderShipService scmPurchaseOrderShipService;

    /**
     * 查询采购订单物流列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:PurchaseOrderShip:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        startPage();
        List<ScmPurchaseOrderShip> list = scmPurchaseOrderShipService.selectScmPurchaseOrderShipList(scmPurchaseOrderShip);
        return getDataTable(list);
    }

    /**
     * 导出采购订单物流列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:PurchaseOrderShip:export')")
    @Log(title = "采购订单物流", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        List<ScmPurchaseOrderShip> list = scmPurchaseOrderShipService.selectScmPurchaseOrderShipList(scmPurchaseOrderShip);
        ExcelUtil<ScmPurchaseOrderShip> util = new ExcelUtil<ScmPurchaseOrderShip>(ScmPurchaseOrderShip.class);
        util.exportExcel(response, list, "采购订单物流数据");
    }

    /**
     * 获取采购订单物流详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:PurchaseOrderShip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scmPurchaseOrderShipService.selectScmPurchaseOrderShipById(id));
    }

    /**
     * 确认收货
     */
    @PreAuthorize("@ss.hasPermi('purchase:PurchaseOrderShip:edit')")
    @Log(title = "采购订单物流", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult confirmReceipt(@RequestBody ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        scmPurchaseOrderShip.setUpdateBy(getUsername());
        return toAjax(scmPurchaseOrderShipService.updateScmPurchaseOrderShip(scmPurchaseOrderShip));
    }

    @Log(title = "采购订单物流", businessType = BusinessType.UPDATE)
    @PostMapping("/createStockInEntry")
    public AjaxResult createStockInEntry(@RequestBody PurchaseOrderStockInBo bo)
    {
        bo.setCreateBy(getUsername());
        int result = scmPurchaseOrderShipService.createStockInEntry(bo);
        if(result == -1) return new AjaxResult(404,"采购物流不存在");
        else if (result == -2) return new AjaxResult(501,"未确认收货不允许操作");
        else if (result == -3) {
            return new AjaxResult(502,"已处理过了请勿重复操作");
        } else if (result == -4) {
            return new AjaxResult(503,"状态不正确不能操作");
        } else if (result == 1) {
            return toAjax(1);
        }else return toAjax(result);
    }


}
