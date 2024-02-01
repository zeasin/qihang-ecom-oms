package com.qihang.erp.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.qihang.erp.api.domain.WmsStockOutEntry;
import com.qihang.erp.api.domain.WmsStockOutEntryItem;
import com.qihang.erp.api.domain.bo.StockingAddVo;
import com.qihang.erp.api.mapper.WmsStockOutEntryMapper;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.WmsOrderShippingMapper;
import com.qihang.erp.api.domain.WmsOrderShipping;
import com.qihang.erp.api.service.IWmsOrderShippingService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 仓库订单发货Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-07
 */
@Service
public class WmsOrderShippingServiceImpl implements IWmsOrderShippingService 
{
    @Autowired
    private WmsOrderShippingMapper wmsOrderShippingMapper;
    @Autowired
    private WmsStockOutEntryMapper stockOutEntryMapper;


    /**
     * 查询仓库订单发货
     * 
     * @param id 仓库订单发货主键
     * @return 仓库订单发货
     */
    @Override
    public WmsOrderShipping selectWmsOrderShippingById(Long id)
    {
        return wmsOrderShippingMapper.selectWmsOrderShippingById(id);
    }

    @Transactional
    @Override
    public int stockingAdd(StockingAddVo addVo) {
        List<WmsOrderShipping> shipList = wmsOrderShippingMapper.selectWmsOrderShippingVoByIds(addVo.getIds());
        if(shipList == null) return -9;
        for (var ship:shipList) {
            // 判断状态
            if(ship.getStatus().intValue() != 0) return -1;//状态不对不能生成拣货单
        }

        // 判断库存
        Map<Long, Long> specQty = shipList.stream().collect(Collectors.groupingBy(WmsOrderShipping::getSpecId, Collectors.summingLong(WmsOrderShipping::getQuantity)));
        for (Map.Entry<Long, Long> entry : specQty.entrySet()) {
            WmsOrderShipping specShip = shipList.stream().filter(x -> x.getSpecId() == entry.getKey()).findFirst().get();
            if(specShip.getInventory() < entry.getValue()){
                return -2;
            }
        }

        Map<Long, List<WmsOrderShipping>> goodsGroup = shipList.stream().collect(Collectors.groupingBy(WmsOrderShipping::getGoodsId));
        Long sum = shipList.stream().mapToLong(WmsOrderShipping::getQuantity).sum();
        // 插入数据 出库单主表
        WmsStockOutEntry entry = new WmsStockOutEntry();
        entry.setStockOutNum(addVo.getStockOutNum());
        entry.setCreateTime(new Date());
        entry.setStockOutType(1L);
        entry.setStatus(0L);
        entry.setPrintStatus(0L);
        entry.setCreateBy(addVo.getCreateBy());
        entry.setIsDelete(0L);
        entry.setSpecUnit(specQty.size());
        entry.setGoodsUnit(goodsGroup.size());
        entry.setSpecUnitTotal(sum.intValue());
        stockOutEntryMapper.insertWmsStockOutEntry(entry);
        // 插入数据 出库单item
        List<WmsStockOutEntryItem> entryItems = new ArrayList<>();
        for (var ship:shipList) {
            WmsStockOutEntryItem item = new WmsStockOutEntryItem();
            item.setEntryId(entry.getId());
            item.setSourceOrderId(ship.getErpOrderId());
            item.setSourceOrderNo(ship.getOrderNum());
            item.setSourceOrderItemId(ship.getErpOrderItemId());
            item.setGoodsId(ship.getGoodsId());
            item.setSpecId(ship.getSpecId());
            item.setSpecNum(ship.getSpecNum());
            item.setOriginalQuantity(ship.getQuantity());
            item.setOutQuantity(0L);
            item.setStatus(0L);
            entryItems.add(item);
            // 更新自己
            WmsOrderShipping update = new WmsOrderShipping();
            update.setId(ship.getId());
            update.setStatus(1L);
            update.setUpdateTime(new Date());
            update.setUpdateBy(addVo.getCreateBy());
            wmsOrderShippingMapper.updateWmsOrderShipping(update);
        }
        stockOutEntryMapper.batchWmsStockOutEntryItem(entryItems);


        return 1;
    }

    /**
     * 查询仓库订单发货列表
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 仓库订单发货
     */
    @Override
    public List<WmsOrderShipping> selectWmsOrderShippingList(WmsOrderShipping wmsOrderShipping)
    {
        return wmsOrderShippingMapper.selectWmsOrderShippingList(wmsOrderShipping);
    }

    /**
     * 新增仓库订单发货
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 结果
     */
    @Override
    public int insertWmsOrderShipping(WmsOrderShipping wmsOrderShipping)
    {
        wmsOrderShipping.setCreateTime(DateUtils.getNowDate());
        return wmsOrderShippingMapper.insertWmsOrderShipping(wmsOrderShipping);
    }

    /**
     * 修改仓库订单发货
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 结果
     */
    @Override
    public int updateWmsOrderShipping(WmsOrderShipping wmsOrderShipping)
    {
        wmsOrderShipping.setUpdateTime(DateUtils.getNowDate());
        return wmsOrderShippingMapper.updateWmsOrderShipping(wmsOrderShipping);
    }

    /**
     * 批量删除仓库订单发货
     * 
     * @param ids 需要删除的仓库订单发货主键
     * @return 结果
     */
    @Override
    public int deleteWmsOrderShippingByIds(Long[] ids)
    {
        return wmsOrderShippingMapper.deleteWmsOrderShippingByIds(ids);
    }

    /**
     * 删除仓库订单发货信息
     * 
     * @param id 仓库订单发货主键
     * @return 结果
     */
    @Override
    public int deleteWmsOrderShippingById(Long id)
    {
        return wmsOrderShippingMapper.deleteWmsOrderShippingById(id);
    }
}
