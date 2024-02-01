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
import com.qihang.erp.api.domain.ErpGoodsInventory;
import com.qihang.erp.api.service.IErpGoodsInventoryService;
import com.zhijian.common.utils.poi.ExcelUtil;
import com.zhijian.common.core.page.TableDataInfo;

/**
 * 商品库存Controller
 * 
 * @author qihang
 * @date 2024-01-09
 */
@RestController
@RequestMapping("/api/goodsInventory")
public class ErpGoodsInventoryController extends BaseController
{
    @Autowired
    private IErpGoodsInventoryService erpGoodsInventoryService;

    /**
     * 查询商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('api:goodsInventory:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsInventory erpGoodsInventory)
    {
        startPage();
        List<ErpGoodsInventory> list = erpGoodsInventoryService.selectErpGoodsInventoryList(erpGoodsInventory);
        return getDataTable(list);
    }

    /**
     * 导出商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('api:goodsInventory:export')")
    @Log(title = "商品库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpGoodsInventory erpGoodsInventory)
    {
        List<ErpGoodsInventory> list = erpGoodsInventoryService.selectErpGoodsInventoryList(erpGoodsInventory);
        ExcelUtil<ErpGoodsInventory> util = new ExcelUtil<ErpGoodsInventory>(ErpGoodsInventory.class);
        util.exportExcel(response, list, "商品库存数据");
    }

    /**
     * 获取商品库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:goodsInventory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpGoodsInventoryService.selectErpGoodsInventoryById(id));
    }

//    /**
//     * 新增商品库存
//     */
//    @PreAuthorize("@ss.hasPermi('api:goodsInventory:add')")
//    @Log(title = "商品库存", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ErpGoodsInventory erpGoodsInventory)
//    {
//        return toAjax(erpGoodsInventoryService.insertErpGoodsInventory(erpGoodsInventory));
//    }
//
//    /**
//     * 修改商品库存
//     */
//    @PreAuthorize("@ss.hasPermi('api:goodsInventory:edit')")
//    @Log(title = "商品库存", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody ErpGoodsInventory erpGoodsInventory)
//    {
//        return toAjax(erpGoodsInventoryService.updateErpGoodsInventory(erpGoodsInventory));
//    }

//    /**
//     * 删除商品库存
//     */
//    @PreAuthorize("@ss.hasPermi('api:goodsInventory:remove')")
//    @Log(title = "商品库存", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(erpGoodsInventoryService.deleteErpGoodsInventoryByIds(ids));
//    }
}
