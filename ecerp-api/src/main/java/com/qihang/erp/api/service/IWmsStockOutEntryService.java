package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockOutEntry;
import com.qihang.erp.api.domain.bo.StockOutBo;

/**
 * 出库单Service接口
 * 
 * @author qihang
 * @date 2024-01-10
 */
public interface IWmsStockOutEntryService 
{
    /**
     * 查询出库单
     * 
     * @param id 出库单主键
     * @return 出库单
     */
    public WmsStockOutEntry selectWmsStockOutEntryById(Long id);

    /**
     * 查询出库单列表
     * 
     * @param wmsStockOutEntry 出库单
     * @return 出库单集合
     */
    public List<WmsStockOutEntry> selectWmsStockOutEntryList(WmsStockOutEntry wmsStockOutEntry);

    /**
     * 新增出库单
     * 
     * @param wmsStockOutEntry 出库单
     * @return 结果
     */
    public int insertWmsStockOutEntry(WmsStockOutEntry wmsStockOutEntry);
    public int stockOut(StockOutBo bo);

    /**
     * 修改出库单
     * 
     * @param wmsStockOutEntry 出库单
     * @return 结果
     */
    public int updateWmsStockOutEntry(WmsStockOutEntry wmsStockOutEntry);

    /**
     * 批量删除出库单
     * 
     * @param ids 需要删除的出库单主键集合
     * @return 结果
     */
    public int deleteWmsStockOutEntryByIds(Long[] ids);

    /**
     * 删除出库单信息
     * 
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteWmsStockOutEntryById(Long id);
}
