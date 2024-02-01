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
import com.qihang.erp.api.domain.ScmPurchaseOrderItem;
import com.qihang.erp.api.service.IScmPurchaseOrderItemService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 采购订单明细Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/purchase/purchaseOrderItem")
public class ScmPurchaseOrderItemController extends BaseController
{
    @Autowired
    private IScmPurchaseOrderItemService scmPurchaseOrderItemService;

    /**
     * 查询采购订单明细列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmPurchaseOrderItem scmPurchaseOrderItem)
    {
        startPage();
        List<ScmPurchaseOrderItem> list = scmPurchaseOrderItemService.selectScmPurchaseOrderItemList(scmPurchaseOrderItem);
        return getDataTable(list);
    }


}
