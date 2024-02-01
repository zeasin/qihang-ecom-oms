package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockInEntry;

/**
 * 入库单Service接口
 * 
 * @author qihang
 * @date 2023-12-31
 */
public interface IWmsStockInEntryService 
{
    /**
     * 查询入库单
     * 
     * @param id 入库单主键
     * @return 入库单
     */
    public WmsStockInEntry selectWmsStockInEntryById(Long id);

    /**
     * 查询入库单列表
     * 
     * @param wmsStockInEntry 入库单
     * @return 入库单集合
     */
    public List<WmsStockInEntry> selectWmsStockInEntryList(WmsStockInEntry wmsStockInEntry);

    /**
     * 新增入库单
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    public int insertWmsStockInEntry(WmsStockInEntry wmsStockInEntry);

    /**
     * 入库操作
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    public int stockIn(WmsStockInEntry wmsStockInEntry);
    public int complete(Long id,String updateBy);

    /**
     * 批量删除入库单
     * 
     * @param ids 需要删除的入库单主键集合
     * @return 结果
     */
    public int deleteWmsStockInEntryByIds(Long[] ids);

    /**
     * 删除入库单信息
     * 
     * @param id 入库单主键
     * @return 结果
     */
    public int deleteWmsStockInEntryById(Long id);
}
