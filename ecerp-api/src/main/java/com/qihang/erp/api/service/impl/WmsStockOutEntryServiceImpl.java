package com.qihang.erp.api.service.impl;

import java.util.Date;
import java.util.List;

import com.qihang.erp.api.domain.*;
import com.qihang.erp.api.domain.bo.StockOutBo;
import com.qihang.erp.api.mapper.*;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.service.IWmsStockOutEntryService;

/**
 * 出库单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-10
 */
@Service
public class WmsStockOutEntryServiceImpl implements IWmsStockOutEntryService 
{
    @Autowired
    private WmsStockOutEntryMapper wmsStockOutEntryMapper;
    @Autowired
    private ErpGoodsInventoryMapper erpGoodsInventoryMapper;
    @Autowired
    private WmsStockOutEntryItemDetailMapper stockOutEntryItemDetailMapper;
    @Autowired
    private WmsOrderShippingMapper orderShippingMapper;
    @Autowired
    private ErpOrderMapper erpOrderMapper;
    /**
     * 查询出库单
     * 
     * @param id 出库单主键
     * @return 出库单
     */
    @Override
    public WmsStockOutEntry selectWmsStockOutEntryById(Long id)
    {
        WmsStockOutEntry entry = wmsStockOutEntryMapper.selectWmsStockOutEntryById(id);
        if(entry.getWmsStockOutEntryItemList() !=null && !entry.getWmsStockOutEntryItemList().isEmpty()){
            for (WmsStockOutEntryItem item:entry.getWmsStockOutEntryItemList()) {
                item.setInventoryDetails(erpGoodsInventoryMapper.selectErpGoodsInventoryDetailBySpecId(item.getSpecId()));

            }
        }
        return entry;
    }

    /**
     * 查询出库单列表
     * 
     * @param wmsStockOutEntry 出库单
     * @return 出库单
     */
    @Override
    public List<WmsStockOutEntry> selectWmsStockOutEntryList(WmsStockOutEntry wmsStockOutEntry)
    {
        return wmsStockOutEntryMapper.selectWmsStockOutEntryList(wmsStockOutEntry);
    }

    /**
     * 新增出库单
     * 
     * @param wmsStockOutEntry 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertWmsStockOutEntry(WmsStockOutEntry wmsStockOutEntry)
    {
        wmsStockOutEntry.setCreateTime(DateUtils.getNowDate());
        int rows = wmsStockOutEntryMapper.insertWmsStockOutEntry(wmsStockOutEntry);
        insertWmsStockOutEntryItem(wmsStockOutEntry);
        return rows;
    }

    /**
     * 出库
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int stockOut(StockOutBo bo) {
        WmsStockOutEntryItem item = wmsStockOutEntryMapper.selectWmsStockOutEntryItemById(bo.getEntryItemId());
        if(item==null) return -1;
        else if(item.getStatus().intValue() ==2) return -2;
        else if(item.getOriginalQuantity() == item.getOutQuantity()) return -3;
        else if(item.getOriginalQuantity() - item.getOutQuantity() < bo.getOutQty()) return -4;
        // 库存判断

        ErpGoodsInventoryDetail detail = erpGoodsInventoryMapper.selectErpGoodsInventoryDetailById(bo.getInventoryDetailId());
        if(detail == null) return -11;
        else if(detail.getCurrentQty() < bo.getOutQty()) return -12;
        ErpGoodsInventory inventory = erpGoodsInventoryMapper.selectErpGoodsInventoryById(detail.getInventoryId());

        // 开始出库
        // 第一步：新增 wms_stock_out_entry_item_detail
        WmsStockOutEntryItemDetail itemDetail = new WmsStockOutEntryItemDetail();
        itemDetail.setEntryId(item.getEntryId());
        itemDetail.setEntryItemId(item.getId());
        itemDetail.setGoodsInventoryId(detail.getInventoryId());
        itemDetail.setGoodsInventoryDetailId(detail.getId());
        itemDetail.setQuantity(bo.getOutQty());
        itemDetail.setLocationId(detail.getInLocation());
        itemDetail.setStockOutOperatorId(bo.getOperatorId());
        itemDetail.setStockOutOperatorName(bo.getOperatorName());
        itemDetail.setStockOutTime(new Date());
        stockOutEntryItemDetailMapper.insertWmsStockOutEntryItemDetail(itemDetail);
        // 第二步：更新 wms_stock_out_entry_item
        WmsStockOutEntryItem entryItem = new WmsStockOutEntryItem();
        entryItem.setId(item.getId());
        entryItem.setOutQuantity(item.getOutQuantity()+bo.getOutQty());
        if(item.getOutQuantity()==0){
            // 证明是刚开始出库
            entryItem.setPickedTime(new Date());
        }
        // 更新状态
        if(item.getOutQuantity()+ bo.getOutQty() == item.getOriginalQuantity()){
            // 出库完成了
            entryItem.setCompleteTime(new Date());
            entryItem.setStatus(2L);
        }else{
            entryItem.setStatus(1L);
        }
        wmsStockOutEntryMapper.updateWmsStockOutEntryItem(entryItem);

        // 第三步：减库存
        ErpGoodsInventoryDetail inventoryDetailUpdate = new ErpGoodsInventoryDetail();
        inventoryDetailUpdate.setId(detail.getId());
        inventoryDetailUpdate.setCurrentQty(detail.getCurrentQty() - bo.getOutQty());
        erpGoodsInventoryMapper.updateErpGoodsInventoryDetail(inventoryDetailUpdate);

        ErpGoodsInventory inventoryUpdate = new ErpGoodsInventory();
        inventoryUpdate.setId(detail.getInventoryId());
        inventoryUpdate.setCurrentQty(inventory.getCurrentQty() - bo.getOutQty());
        inventoryUpdate.setUpdateBy(bo.getOperatorName());
        inventoryUpdate.setUpdateTime(new Date());
        erpGoodsInventoryMapper.updateErpGoodsInventory(inventoryUpdate);

        // 第四步：更新wms_order_shipping状态
        if(entryItem.getStatus()!=null && entryItem.getStatus() ==2) {
            WmsOrderShipping shippingUpdate = new WmsOrderShipping();
            shippingUpdate.setErpOrderItemId(item.getSourceOrderItemId());
            shippingUpdate.setStatus(2L);
            shippingUpdate.setOutOperator(bo.getOperatorName());
            shippingUpdate.setOutPosition(detail.getInLocation().toString());
            shippingUpdate.setOutTime(new Date());
            shippingUpdate.setUpdateBy(bo.getOperatorName());
            shippingUpdate.setUpdateTime(new Date());

            orderShippingMapper.updateWmsOrderShippingStatusByErpOrderItemId(shippingUpdate);
            WmsOrderShipping select = new WmsOrderShipping();
            select.setErpOrderId(item.getSourceOrderId());
            // 查询 同orderId所有数据状态
            List<WmsOrderShipping> shipList = orderShippingMapper.selectWmsOrderShippingList(select);
            // 循环
            if(shipList!=null){
                int allTotal = shipList.size();
                int outTotal = 0;
                for (WmsOrderShipping s:shipList) {
                    if(s.getStatus().intValue() == 2){
                        outTotal +=1;
                    }
                }
                //是否全部出库
                if(allTotal == outTotal){
                    // 订单下面的所有子订单已出库 更新订单状态
                    ErpOrder erpOrder = new ErpOrder();
                    erpOrder.setId(item.getSourceOrderId());
                    erpOrder.setOrderStatus(2);
                    erpOrder.setUpdateBy(bo.getOperatorName());
                    erpOrder.setUpdateTime(new Date());
                    erpOrderMapper.updateErpOrder(erpOrder);
                }
            }
        }
        return 1;
    }

    /**
     * 修改出库单
     * 
     * @param wmsStockOutEntry 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateWmsStockOutEntry(WmsStockOutEntry wmsStockOutEntry)
    {
        wmsStockOutEntry.setUpdateTime(DateUtils.getNowDate());
        wmsStockOutEntryMapper.deleteWmsStockOutEntryItemByEntryId(wmsStockOutEntry.getId());
        insertWmsStockOutEntryItem(wmsStockOutEntry);
        return wmsStockOutEntryMapper.updateWmsStockOutEntry(wmsStockOutEntry);
    }

    /**
     * 批量删除出库单
     * 
     * @param ids 需要删除的出库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsStockOutEntryByIds(Long[] ids)
    {
        wmsStockOutEntryMapper.deleteWmsStockOutEntryItemByEntryIds(ids);
        return wmsStockOutEntryMapper.deleteWmsStockOutEntryByIds(ids);
    }

    /**
     * 删除出库单信息
     * 
     * @param id 出库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsStockOutEntryById(Long id)
    {
        wmsStockOutEntryMapper.deleteWmsStockOutEntryItemByEntryId(id);
        return wmsStockOutEntryMapper.deleteWmsStockOutEntryById(id);
    }

    /**
     * 新增出库单明细信息
     * 
     * @param wmsStockOutEntry 出库单对象
     */
    public void insertWmsStockOutEntryItem(WmsStockOutEntry wmsStockOutEntry)
    {
        List<WmsStockOutEntryItem> wmsStockOutEntryItemList = wmsStockOutEntry.getWmsStockOutEntryItemList();
        Long id = wmsStockOutEntry.getId();
        if (StringUtils.isNotNull(wmsStockOutEntryItemList))
        {
            List<WmsStockOutEntryItem> list = new ArrayList<WmsStockOutEntryItem>();
            for (WmsStockOutEntryItem wmsStockOutEntryItem : wmsStockOutEntryItemList)
            {
                wmsStockOutEntryItem.setEntryId(id);
                list.add(wmsStockOutEntryItem);
            }
            if (list.size() > 0)
            {
                wmsStockOutEntryMapper.batchWmsStockOutEntryItem(list);
            }
        }
    }
}
