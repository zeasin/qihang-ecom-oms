package cn.qihangerp.api.controller.sup;

import cn.qihangerp.common.*;
import cn.qihangerp.model.bo.SupplierProductAddBo;
import cn.qihangerp.model.entity.ErpSupplierProduct;
import cn.qihangerp.model.entity.ErpSupplierProductItem;
import cn.qihangerp.security.common.BaseController;
import cn.qihangerp.service.ErpSupplierProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品管理Controller
 *
 * @author qihang
 * @date 2026-05-02
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/erp-api/supplier/goods")
public class SupplierGoodsController extends BaseController
{
    private final ErpSupplierProductService erpSupplierProductService;

    /**
     * 查询供应商商品列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ErpSupplierProduct goods, PageQuery pageQuery, HttpServletRequest request) {
        PageResult<ErpSupplierProduct> pageList = erpSupplierProductService.queryPageList(goods, pageQuery);
        return getDataTable(pageList);
    }

    /**
     * 获取供应商商品详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {

        ErpSupplierProduct product = erpSupplierProductService.getById(id);
        if(product == null){
            return AjaxResult.error("商品不存在");
        }
        
        // 查询SKU列表
        List<ErpSupplierProductItem> itemList = erpSupplierProductService.queryItemListByProductId(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("product", product);
        result.put("itemList", itemList);
        return success(result);
    }

    /**
     * 添加供应商商品（含SKU）
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SupplierProductAddBo bo) {
        if(StringUtils.isEmpty(bo.getProductName())) {
            return AjaxResult.error("商品名称不能为空");
        }

        if(bo.getItemList() == null || bo.getItemList().isEmpty()) {
            return AjaxResult.error("请至少添加一个SKU");
        }

        // 验证SKU规格不能为空
        for(int i=0; i<bo.getItemList().size(); i++) {
            if(StringUtils.isEmpty(bo.getItemList().get(i).getStandard())) {
                return AjaxResult.error("第" + (i+1) + "个SKU的规格不能为空");
            }
        }

        try {
            ResultVo<Long> resultVo = erpSupplierProductService.addProduct(getUsername(), bo);
            if(resultVo.getCode()==0) return AjaxResult.success(resultVo.getData());
            else return  AjaxResult.error(resultVo.getMsg());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改供应商商品（含SKU）
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody SupplierProductAddBo bo) {
        if(bo.getId() == null) {
            return AjaxResult.error("商品ID不能为空");
        }

        if(StringUtils.isEmpty(bo.getProductName())) {  return AjaxResult.error("商品名称不能为空");  }

        if(bo.getItemList() != null && !bo.getItemList().isEmpty()) {
            // 验证SKU规格不能为空
            for(int i=0; i<bo.getItemList().size(); i++) {
                if(StringUtils.isEmpty(bo.getItemList().get(i).getStandard())) {
                    return AjaxResult.error("第" + (i+1) + "个SKU的规格不能为空");
                }
            }
        }

        try {
            ResultVo<Long> resultVo= erpSupplierProductService.updateProduct(getUsername(), bo);
            if(resultVo.getCode()==0) return AjaxResult.success(resultVo.getData());
            else return  AjaxResult.error(resultVo.getMsg());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除供应商商品
     */
    @DeleteMapping("/del/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        try {
            erpSupplierProductService.deleteProduct(id);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改供应商商品状态（上架/下架）
     */
    @PutMapping("/status")
    public AjaxResult updateStatus(@RequestBody ErpSupplierProduct product) {
        try {
            erpSupplierProductService.updateStatus(product.getId(), product.getStatus());
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
