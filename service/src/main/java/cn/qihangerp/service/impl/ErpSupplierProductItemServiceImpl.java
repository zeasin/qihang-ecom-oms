package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ErpSupplierProductItemMapper;

import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.entity.ErpSupplierProductItem;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.service.ErpSupplierProductItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 1
 * @description 针对表【erp_supplier_product_item(供应商商品表(SKU维度))】的数据库操作Service实现
 * @createDate 2026-05-02 09:59:53
 */
@Service
@AllArgsConstructor
public class ErpSupplierProductItemServiceImpl extends ServiceImpl<ErpSupplierProductItemMapper, ErpSupplierProductItem> implements ErpSupplierProductItemService {

    private final OGoodsSkuMapper oGoodsSkuMapper;

    @Override
    public PageResult<ErpSupplierProductItem> queryPageList(ErpSupplierProductItem item, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpSupplierProductItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(item.getSupplierId() != null, ErpSupplierProductItem::getSupplierId, item.getSupplierId());
        queryWrapper.eq(item.getSupplierProductId() != null, ErpSupplierProductItem::getSupplierProductId, item.getSupplierProductId());
        queryWrapper.eq(item.getStatus() != null, ErpSupplierProductItem::getStatus, item.getStatus());
        queryWrapper.eq(StringUtils.hasText(item.getSkuCode()), ErpSupplierProductItem::getSkuCode, item.getSkuCode());
        queryWrapper.like(StringUtils.hasText(item.getStandard()), ErpSupplierProductItem::getStandard, item.getStandard());
        queryWrapper.orderByDesc(ErpSupplierProductItem::getCreateTime);

        Page<ErpSupplierProductItem> page = this.page(pageQuery.build(), queryWrapper);

        // 填充商品库SKU信息
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            List<Long> erpGoodsSkuIds = new ArrayList<>();
            for (ErpSupplierProductItem supplierProductItem : page.getRecords()) {
                if (supplierProductItem.getErpGoodsSkuId() != null) {
                    erpGoodsSkuIds.add(supplierProductItem.getErpGoodsSkuId());
                }
            }

            if (!erpGoodsSkuIds.isEmpty()) {
                List<OGoodsSku> oGoodsSkuList = oGoodsSkuMapper.selectBatchIds(erpGoodsSkuIds);
                Map<Long, OGoodsSku> skuMap = oGoodsSkuList.stream()
                        .collect(Collectors.toMap(
                                sku -> Long.valueOf(sku.getId()),
                                sku -> sku
                        ));

                for (ErpSupplierProductItem supplierProductItem : page.getRecords()) {
                    if (supplierProductItem.getErpGoodsSkuId() != null) {
                        supplierProductItem.setErpGoodsSku(skuMap.get(supplierProductItem.getErpGoodsSkuId()));
                    }
                }
            }
        }

        return PageResult.build(page);
    }
}
