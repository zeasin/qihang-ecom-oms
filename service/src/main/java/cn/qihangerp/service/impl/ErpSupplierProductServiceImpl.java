package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.mapper.ErpSupplierProductItemMapper;
import cn.qihangerp.mapper.ErpSupplierProductMapper;
import cn.qihangerp.mapper.ErpWarehouseMapper;
import cn.qihangerp.model.bo.SupplierProductAddBo;
import cn.qihangerp.model.entity.ErpSupplierProduct;
import cn.qihangerp.model.entity.ErpSupplierProductItem;
import cn.qihangerp.model.entity.ErpWarehouse;
import cn.qihangerp.service.ErpSupplierProductService;
import cn.qihangerp.service.ErpSupplierService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author 1
* @description 针对表【erp_supplier_product(供应商商品表(SPU维度))】的数据库操作Service实现
* @createDate 2026-05-02 09:59:53
*/
@Service
@AllArgsConstructor
public class ErpSupplierProductServiceImpl extends ServiceImpl<ErpSupplierProductMapper, ErpSupplierProduct>
    implements ErpSupplierProductService{

    private final ErpSupplierProductItemMapper itemMapper;
    private final ErpWarehouseMapper warehouseMapper;
    private final ErpSupplierService supplierService;

    @Override
    public PageResult<ErpSupplierProduct> queryPageList(ErpSupplierProduct goods, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpSupplierProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(goods.getSupplierId() != null, ErpSupplierProduct::getSupplierId, goods.getSupplierId());
        queryWrapper.eq(goods.getStatus() != null, ErpSupplierProduct::getStatus, goods.getStatus());
        queryWrapper.like(StringUtils.hasText(goods.getProductName()), ErpSupplierProduct::getProductName, goods.getProductName());
        queryWrapper.like(StringUtils.hasText(goods.getProductNum()), ErpSupplierProduct::getProductNum, goods.getProductNum());
        queryWrapper.eq(goods.getCategoryId() != null, ErpSupplierProduct::getCategoryId, goods.getCategoryId());

        Page<ErpSupplierProduct> page = pageQuery.build();
        this.page(page, queryWrapper);

        // 为每个商品设置SKU数量
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            for (ErpSupplierProduct product : page.getRecords()) {
                Long skuCount = itemMapper.selectCount(new LambdaQueryWrapper<ErpSupplierProductItem>()
                        .eq(ErpSupplierProductItem::getSupplierProductId, product.getId()));
                product.setSkuCount(skuCount.intValue());
            }
        }

        return PageResult.build(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo<Long> addProduct(String username, SupplierProductAddBo bo) {
        // 1. 获取供应商信息
        cn.qihangerp.model.entity.ErpSupplier supplier = supplierService.getById(bo.getSupplierId());
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        
        // 3. 保存SPU信息
        ErpSupplierProduct product = new ErpSupplierProduct();
        product.setSupplierId(bo.getSupplierId());
        product.setProductName(bo.getProductName());
        product.setImageUrl(bo.getImageUrl());
        product.setProductNum(bo.getProductNum());
        product.setCategoryId(bo.getCategoryId());
        product.setBrandId(bo.getBrandId());
        product.setUnitName(bo.getUnitName());
        product.setLength(bo.getLength());
        product.setWidth(bo.getWidth());
        product.setHeight(bo.getHeight());
        product.setWeight(bo.getWeight());
        product.setStatus(1);
        product.setRemark(bo.getRemark());
        product.setCreateBy(username);
        this.save(product);

        // 4. 保存SKU列表并同步创建仓库商品记录
        if (bo.getItemList() != null && !bo.getItemList().isEmpty()) {
            for (SupplierProductAddBo.SupplierProductItemBo itemBo : bo.getItemList()) {
                ErpSupplierProductItem item = new ErpSupplierProductItem();
                item.setSupplierProductId(product.getId());
                item.setSupplierId(bo.getSupplierId());
                item.setSkuCode(itemBo.getSkuCode());
                item.setProductName(bo.getProductName());
                item.setBarCode(itemBo.getBarCode());
                item.setColorId(itemBo.getColorId());
                item.setColorValue(itemBo.getColorValue());
                item.setColorImage(itemBo.getColorImage());
                item.setSizeId(itemBo.getSizeId());
                item.setSizeValue(itemBo.getSizeValue());
                item.setStyleId(itemBo.getStyleId());
                item.setStyleValue(itemBo.getStyleValue());
                item.setStandard(itemBo.getStandard());
                item.setBrandNo(itemBo.getBrandNo());
                item.setBrandName(itemBo.getBrandName());
                item.setPrice(itemBo.getPrice());
                item.setStatus(1);
                item.setCreateBy(username);
                itemMapper.insert(item);
            }
        }

        return ResultVo.success(product.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateProduct(String username, SupplierProductAddBo bo) {
        if (bo.getId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        // 1. 更新SPU信息
        ErpSupplierProduct product = this.getById(bo.getId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        product.setProductName(bo.getProductName());
        product.setImageUrl(bo.getImageUrl());
        product.setProductNum(bo.getProductNum());
        product.setCategoryId(bo.getCategoryId());
        product.setBrandId(bo.getBrandId());
        product.setUnitName(bo.getUnitName());
        product.setLength(bo.getLength());
        product.setWidth(bo.getWidth());
        product.setHeight(bo.getHeight());
        product.setWeight(bo.getWeight());
        product.setRemark(bo.getRemark());
        product.setUpdateBy(username);
        this.updateById(product);

        // 2. 获取供应商信息，检查是否有仓库
        cn.qihangerp.model.entity.ErpSupplier supplier = supplierService.getById(product.getSupplierId());
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }

        // 3. 检查是否有仓库，没有则创建
        Long warehouseId = supplier.getWarehouseId();
        if (warehouseId == null) {
            // 自动创建供应商仓库
            ErpWarehouse warehouse = new ErpWarehouse();
            warehouse.setWarehouseType("SUPPLIER");
            warehouse.setWarehouseName(supplier.getName() + "仓库");
            warehouse.setWarehouseNo("SUPPLIER-"+supplier.getId());
            warehouse.setMerchantId(0L);
            warehouse.setShopId(0L);
            warehouse.setWarehouseSource(0);
            warehouse.setStatus("1");
            warehouse.setCreateBy(username);
            warehouse.setCreateTime(new Date());
            warehouseMapper.insert(warehouse);

            // 更新供应商的仓库ID
            cn.qihangerp.model.entity.ErpSupplier updateSupplier = new cn.qihangerp.model.entity.ErpSupplier();
            updateSupplier.setId(supplier.getId());
            updateSupplier.setWarehouseId(warehouse.getId());
            supplierService.updateById(updateSupplier);

            warehouseId = warehouse.getId();
        } else {
            // 验证仓库是否存在
            ErpWarehouse warehouse = warehouseMapper.selectById(warehouseId);
            if (warehouse == null) {
                // 仓库不存在，重新创建
                ErpWarehouse newWarehouse = new ErpWarehouse();
                newWarehouse.setWarehouseType("SUPPLIER");
                newWarehouse.setWarehouseName(supplier.getName() + "仓库");
                newWarehouse.setWarehouseNo("SUPPLIER-"+supplier.getId());
                newWarehouse.setMerchantId(0L);
                newWarehouse.setShopId(0L);
                newWarehouse.setWarehouseSource(0);
                newWarehouse.setStatus("1");
                newWarehouse.setCreateBy(username);
                newWarehouse.setCreateTime(new Date());
                warehouseMapper.insert(newWarehouse);

                // 更新供应商的仓库ID
                cn.qihangerp.model.entity.ErpSupplier updateSupplier = new cn.qihangerp.model.entity.ErpSupplier();
                updateSupplier.setId(supplier.getId());
                updateSupplier.setWarehouseId(newWarehouse.getId());
                supplierService.updateById(updateSupplier);

                warehouseId = newWarehouse.getId();
            }
        }

        // 4. 获取数据库中已有的SKU列表
        List<ErpSupplierProductItem> existingItems = queryItemListByProductId(bo.getId());
        java.util.Map<Long, ErpSupplierProductItem> existingMap = existingItems.stream()
                .collect(java.util.stream.Collectors.toMap(ErpSupplierProductItem::getId, item -> item));

        // 5. 遍历前端传来的SKU列表，更新或新增
        if (bo.getItemList() != null) {
            for (SupplierProductAddBo.SupplierProductItemBo itemBo : bo.getItemList()) {
                ErpSupplierProductItem item;
                if (itemBo.getId() != null && existingMap.containsKey(itemBo.getId())) {
                    // 已有的SKU，执行更新
                    item = existingMap.get(itemBo.getId());
                    item.setSkuCode(itemBo.getSkuCode());
                    item.setBarCode(itemBo.getBarCode());
                    item.setColorId(itemBo.getColorId());
                    item.setColorValue(itemBo.getColorValue());
                    item.setColorImage(itemBo.getColorImage());
                    item.setSizeId(itemBo.getSizeId());
                    item.setSizeValue(itemBo.getSizeValue());
                    item.setStyleId(itemBo.getStyleId());
                    item.setStyleValue(itemBo.getStyleValue());
                    item.setStandard(itemBo.getStandard());
                    item.setBrandNo(itemBo.getBrandNo());
                    item.setBrandName(itemBo.getBrandName());
                    item.setPrice(itemBo.getPrice());
                    item.setUpdateBy(username);
                    itemMapper.updateById(item);
                    // 从map中移除，表示已处理
                    existingMap.remove(itemBo.getId());
                } else {
                    // 新增的SKU
                    item = new ErpSupplierProductItem();
                    item.setSupplierProductId(product.getId());
                    item.setSupplierId(product.getSupplierId());
                    item.setSkuCode(itemBo.getSkuCode());
                    item.setProductName(bo.getProductName());
                    item.setBarCode(itemBo.getBarCode());
                    item.setColorId(itemBo.getColorId());
                    item.setColorValue(itemBo.getColorValue());
                    item.setColorImage(itemBo.getColorImage());
                    item.setSizeId(itemBo.getSizeId());
                    item.setSizeValue(itemBo.getSizeValue());
                    item.setStyleId(itemBo.getStyleId());
                    item.setStyleValue(itemBo.getStyleValue());
                    item.setStandard(itemBo.getStandard());
                    item.setBrandNo(itemBo.getBrandNo());
                    item.setBrandName(itemBo.getBrandName());
                    item.setPrice(itemBo.getPrice());
                    item.setStatus(1);
                    item.setCreateBy(username);
                    itemMapper.insert(item);
                }
            }
        }

        // 6. 删除前端没传的SKU（剩余在map中的就是被删除的）
        for (ErpSupplierProductItem deletedItem : existingMap.values()) {
            itemMapper.deleteById(deletedItem.getId());
        }
        return ResultVo.success(product.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        // 1. 删除SKU
        LambdaQueryWrapper<ErpSupplierProductItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(ErpSupplierProductItem::getSupplierProductId, id);
        itemMapper.delete(itemWrapper);

        // 2. 删除SPU
        this.removeById(id);
    }

    @Override
    public List<ErpSupplierProductItem> queryItemListByProductId(Long supplierProductId) {
        LambdaQueryWrapper<ErpSupplierProductItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ErpSupplierProductItem::getSupplierProductId, supplierProductId);
        return itemMapper.selectList(queryWrapper);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        ErpSupplierProduct product = this.getById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        product.setStatus(status);
        this.updateById(product);
    }
}
