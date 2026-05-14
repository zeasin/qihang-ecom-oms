package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.ErpSupplierProductItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 1
 * @description 针对表【erp_supplier_product_item(供应商商品表(SKU维度))】的数据库操作Service
 * @createDate 2026-05-02 09:59:53
 */
public interface ErpSupplierProductItemService extends IService<ErpSupplierProductItem> {

    PageResult<ErpSupplierProductItem> queryPageList(ErpSupplierProductItem item, PageQuery pageQuery);
}
