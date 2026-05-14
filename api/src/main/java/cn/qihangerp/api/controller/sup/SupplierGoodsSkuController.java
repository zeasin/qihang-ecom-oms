package cn.qihangerp.api.controller.sup;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.ErpSupplierProductItem;
import cn.qihangerp.security.common.BaseController;
import cn.qihangerp.service.ErpSupplierProductItemService;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商商品SKU管理Controller
 *
 * @author qihang
 * @date 2026-05-02
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/erp-api/supplier/goods_sku")
public class SupplierGoodsSkuController extends BaseController
{
    private final ErpSupplierProductItemService supplierProductItemService;


    /**
     * 查询供应商商品SKU列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ErpSupplierProductItem item, PageQuery pageQuery) {
        return getDataTable(supplierProductItemService.queryPageList(item, pageQuery));
    }

    /**
     * 获取供应商商品SKU详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(supplierProductItemService.getById(id));
    }

    /**
     * 修改供应商商品SKU
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody ErpSupplierProductItem item)
    {
        if(item.getId() == null) {
            return AjaxResult.error("SKU ID不能为空");
        }

        if(StringUtils.isEmpty(item.getStandard())) {
            return AjaxResult.error("规格不能为空");
        }

        item.setUpdateBy(getUsername());
        return toAjax(supplierProductItemService.updateById(item));
    }

    /**
     * 删除供应商商品SKU
     */
    @DeleteMapping("/del/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(supplierProductItemService.removeById(id));
    }

    /**
     * 绑定商品库SKU
     */
    @PutMapping("/bind")
    public AjaxResult bindGoodsSku(@RequestBody ErpSupplierProductItem item)
    {
        if(item.getId() == null) {
            return AjaxResult.error("SKU ID不能为空");
        }
        if(item.getErpGoodsSkuId() == null) {
            return AjaxResult.error("请选择商品库SKU");
        }

        ErpSupplierProductItem updateItem = new ErpSupplierProductItem();
        updateItem.setId(item.getId());
        updateItem.setErpGoodsSkuId(item.getErpGoodsSkuId());
        updateItem.setUpdateBy(getUsername());
        return toAjax(supplierProductItemService.updateById(updateItem));
    }
}
