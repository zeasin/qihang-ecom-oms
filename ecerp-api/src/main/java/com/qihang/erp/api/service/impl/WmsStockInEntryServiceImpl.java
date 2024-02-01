package com.qihang.erp.api.service.impl;

import java.util.Date;
import java.util.List;

import com.qihang.erp.api.domain.ErpGoodsInventory;
import com.qihang.erp.api.domain.ErpGoodsInventoryDetail;
import com.qihang.erp.api.mapper.ErpGoodsInventoryMapper;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.domain.WmsStockInEntryItem;
import com.qihang.erp.api.mapper.WmsStockInEntryMapper;
import com.qihang.erp.api.domain.WmsStockInEntry;
import com.qihang.erp.api.service.IWmsStockInEntryService;

/**
 * 入库单Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-31
 */
@Service
public class WmsStockInEntryServiceImpl implements IWmsStockInEntryService 
{
    @Autowired
    private WmsStockInEntryMapper wmsStockInEntryMapper;

    @Autowired
    private ErpGoodsInventoryMapper goodsInventoryMapper;


    /**
     * 查询入库单
     * 
     * @param id 入库单主键
     * @return 入库单
     */
    @Override
    public WmsStockInEntry selectWmsStockInEntryById(Long id)
    {
        return wmsStockInEntryMapper.selectWmsStockInEntryById(id);
    }

    /**
     * 查询入库单列表
     * 
     * @param wmsStockInEntry 入库单
     * @return 入库单
     */
    @Override
    public List<WmsStockInEntry> selectWmsStockInEntryList(WmsStockInEntry wmsStockInEntry)
    {
        return wmsStockInEntryMapper.selectWmsStockInEntryList(wmsStockInEntry);
    }



    /**
     * 新增入库单
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertWmsStockInEntry(WmsStockInEntry wmsStockInEntry)
    {
        wmsStockInEntry.setCreateTime(DateUtils.getNowDate());
        int rows = wmsStockInEntryMapper.insertWmsStockInEntry(wmsStockInEntry);
        insertWmsStockInEntryItem(wmsStockInEntry);
        return rows;
    }

    /**
     * 修改入库单
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    @Transactional
    @Override
    public int stockIn(WmsStockInEntry wmsStockInEntry)
    {
        // 查询入库单
        WmsStockInEntry origin = wmsStockInEntryMapper.selectWmsStockInEntryById(wmsStockInEntry.getId());
        if(origin == null) return -1;
        else if(origin.getStatus() == 2) return -9;
        // 待入库数据
        List<WmsStockInEntryItem> waitList = new ArrayList<>();
        for (var item:wmsStockInEntry.getWmsStockInEntryItemList()) {
            if(StringUtils.isNotNull(item.getQuantity()) && StringUtils.isNotNull(item.getLocationId())){
                waitList.add(item);
            }
        }
        if(waitList.size() == 0) return -2;

        // 查询
        for (var item:waitList) {
            // 查询 商品库存
            ErpGoodsInventory gi = goodsInventoryMapper.selectErpGoodsInventoryBySpecId(item.getSpecId());
            if(gi == null){
                // 添加
                ErpGoodsInventory add = new ErpGoodsInventory();
                add.setGoodsId(item.getGoodsId());
                add.setGoodsNumber(item.getGoodsNum());
                add.setSpecId(item.getSpecId());
                add.setSpecNumber(item.getSpecNum());
                add.setIsDelete(0);
                add.setCreateBy(wmsStockInEntry.getUpdateBy());
                add.setCreateTime(new Date());
                add.setLockedQty(0L);
                add.setCurrentQty(item.getQuantity());
                goodsInventoryMapper.insertErpGoodsInventory(add);

                // 添加detail
                ErpGoodsInventoryDetail detail = new ErpGoodsInventoryDetail();
                detail.setInventoryId(add.getId());
                detail.setInQty(item.getQuantity());
                detail.setOriginQty(0L);
                detail.setCurrentQty(item.getQuantity());
                detail.setGoodsId(item.getGoodsId());
                detail.setSpecId(item.getSpecId());
                detail.setInLocation(item.getLocationId());
                detail.setEntryId(origin.getId());
                detail.setEntryItemId(item.getId());
                detail.setCreateBy(wmsStockInEntry.getUpdateBy());
                detail.setCreateTime(new Date());
                List<ErpGoodsInventoryDetail> ds = new ArrayList<>();
                ds.add(detail);
                goodsInventoryMapper.batchErpGoodsInventoryDetail(ds);

                // update item
                WmsStockInEntryItem upItem = new WmsStockInEntryItem();
                upItem.setId(item.getId());
                upItem.setInQuantity(item.getInQuantity() + item.getQuantity());
                upItem.setStatus(1);
                upItem.setUpdateTime(new Date());
                upItem.setUpdateBy(wmsStockInEntry.getUpdateBy());
                wmsStockInEntryMapper.updateWmsStockInEntryItem(upItem);

            }else{
                // 修改
                ErpGoodsInventory update = new ErpGoodsInventory();
                update.setId(gi.getId());
                update.setCurrentQty(gi.getCurrentQty() + item.getQuantity());
                update.setUpdateBy(wmsStockInEntry.getUpdateBy());
                update.setUpdateTime(new Date());
                goodsInventoryMapper.updateErpGoodsInventory(update);

                // 添加detail
                ErpGoodsInventoryDetail detail = new ErpGoodsInventoryDetail();
                detail.setInventoryId(update.getId());
                detail.setInQty(item.getQuantity());
                detail.setOriginQty(gi.getCurrentQty());
                detail.setCurrentQty(update.getCurrentQty());
                detail.setGoodsId(item.getGoodsId());
                detail.setSpecId(item.getSpecId());
                detail.setInLocation(item.getLocationId());
                detail.setEntryId(origin.getId());
                detail.setEntryItemId(item.getId());
                detail.setCreateBy(wmsStockInEntry.getUpdateBy());
                detail.setCreateTime(new Date());
                List<ErpGoodsInventoryDetail> ds = new ArrayList<>();
                ds.add(detail);
                goodsInventoryMapper.batchErpGoodsInventoryDetail(ds);

                // update item
                WmsStockInEntryItem upItem = new WmsStockInEntryItem();
                upItem.setId(item.getId());
                upItem.setInQuantity(item.getInQuantity() + item.getQuantity());
                upItem.setStatus(1);
                upItem.setUpdateTime(new Date());
                upItem.setUpdateBy(wmsStockInEntry.getUpdateBy());
                wmsStockInEntryMapper.updateWmsStockInEntryItem(upItem);
            }

        }

        // 更新自己
        WmsStockInEntry new1 = new WmsStockInEntry();
        new1.setId(wmsStockInEntry.getId());
        new1.setStockInOperatorId(wmsStockInEntry.getStockInOperatorId());
        new1.setStockInOperator(wmsStockInEntry.getUpdateBy());
        new1.setStockInTime(wmsStockInEntry.getStockInTime());
        new1.setUpdateBy(wmsStockInEntry.getUpdateBy());
        new1.setUpdateTime(new Date());
        new1.setStatus(1);
        wmsStockInEntryMapper.updateWmsStockInEntry(new1);
//        wmsStockInEntry.setUpdateTime(DateUtils.getNowDate());
//        wmsStockInEntryMapper.deleteWmsStockInEntryItemByEntryId(wmsStockInEntry.getId());
//        insertWmsStockInEntryItem(wmsStockInEntry);
//        return wmsStockInEntryMapper.updateWmsStockInEntry(wmsStockInEntry);
        return 1;
    }

    @Transactional
    @Override
    public int complete(Long id,String updateBy) {
        // 更新自己
        WmsStockInEntry new1 = new WmsStockInEntry();
        new1.setId(id);
        new1.setUpdateBy(updateBy);
        new1.setUpdateTime(new Date());
        new1.setStatus(2);
        wmsStockInEntryMapper.updateWmsStockInEntry(new1);

        List<WmsStockInEntryItem> items = wmsStockInEntryMapper.selectWmsStockInEntryItemByEntryId(id);
        if(!items.isEmpty()){
            for (var item:items) {
                // update item
                WmsStockInEntryItem upItem = new WmsStockInEntryItem();
                upItem.setId(item.getId());
                upItem.setStatus(2);
                upItem.setUpdateTime(new Date());
                upItem.setUpdateBy(updateBy);
                wmsStockInEntryMapper.updateWmsStockInEntryItem(upItem);
            }
        }

        return 1;
    }

    /**
     * 批量删除入库单
     * 
     * @param ids 需要删除的入库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsStockInEntryByIds(Long[] ids)
    {
        wmsStockInEntryMapper.deleteWmsStockInEntryItemByEntryIds(ids);
        return wmsStockInEntryMapper.deleteWmsStockInEntryByIds(ids);
    }

    /**
     * 删除入库单信息
     * 
     * @param id 入库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsStockInEntryById(Long id)
    {
        wmsStockInEntryMapper.deleteWmsStockInEntryItemByEntryId(id);
        return wmsStockInEntryMapper.deleteWmsStockInEntryById(id);
    }

    /**
     * 新增入库单明细信息
     * 
     * @param wmsStockInEntry 入库单对象
     */
    public void insertWmsStockInEntryItem(WmsStockInEntry wmsStockInEntry)
    {
        List<WmsStockInEntryItem> wmsStockInEntryItemList = wmsStockInEntry.getWmsStockInEntryItemList();
        Long id = wmsStockInEntry.getId();
        if (StringUtils.isNotNull(wmsStockInEntryItemList))
        {
            List<WmsStockInEntryItem> list = new ArrayList<WmsStockInEntryItem>();
            for (WmsStockInEntryItem wmsStockInEntryItem : wmsStockInEntryItemList)
            {
                wmsStockInEntryItem.setEntryId(id);
                list.add(wmsStockInEntryItem);
            }
            if (list.size() > 0)
            {
                wmsStockInEntryMapper.batchWmsStockInEntryItem(list);
            }
        }
    }
}
