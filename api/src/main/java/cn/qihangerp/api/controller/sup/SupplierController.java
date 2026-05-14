package cn.qihangerp.api.controller.sup;

import cn.qihangerp.common.*;
import cn.qihangerp.model.entity.ErpSupplier;
import cn.qihangerp.security.common.BaseController;
import cn.qihangerp.service.ErpSupplierService;
import cn.qihangerp.service.OShopService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@AllArgsConstructor
@RestController
@RequestMapping("/api/erp-api/supplier")
public class SupplierController extends BaseController {
    private final ErpSupplierService supplierService;

    private final OShopService shopService;

    /**
     * 所有供应商list
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/list_all")
    public TableDataInfo list_all(ErpSupplier bo, PageQuery pageQuery) {

        var pageList = supplierService.list(new LambdaQueryWrapper<ErpSupplier>()
                .eq(ErpSupplier::getIsDelete, 0)
                .eq(bo.getIsShipper() != null, ErpSupplier::getIsShipper, bo.getIsShipper())
                .eq(bo.getMerchantId() != null, ErpSupplier::getMerchantId, bo.getMerchantId())
                .eq(bo.getShopId() != null, ErpSupplier::getShopId, bo.getShopId())
        );
        return getDataTable(pageList);
    }

    @GetMapping("/list")
    public TableDataInfo list(ErpSupplier bo, PageQuery pageQuery) {
        var pageList = supplierService.queryPageList(bo, pageQuery);
        return getDataTable(pageList);
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(supplierService.getById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PostMapping
    public AjaxResult add(@RequestBody ErpSupplier scmSupplier) {
        if (scmSupplier.getMerchantId() == null) return AjaxResult.error("请选择商户");
        scmSupplier.setCreateTime(new Date());
        scmSupplier.setIsDelete(0);
        return toAjax(supplierService.save(scmSupplier));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ErpSupplier scmSupplier) {
        if (scmSupplier.getId() == null) return AjaxResult.error("缺少参数：id");
        ErpSupplier byId = supplierService.getById(scmSupplier.getId());
        if (byId == null) return AjaxResult.error("供应商数据不存在");
        scmSupplier.setUpdateTime(new Date());
        return toAjax(supplierService.updateById(scmSupplier));
    }

    /**
     * 删除【请填写功能名称】
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        if (ids == null || ids.length == 0) return AjaxResult.error("缺少参数id");

        for (Long id : ids) {
            ErpSupplier byId = supplierService.getById(id);
            if (byId == null) return AjaxResult.error("供应商数据不存在");
            supplierService.removeById(id);
        }
        return AjaxResult.success();
    }


}
