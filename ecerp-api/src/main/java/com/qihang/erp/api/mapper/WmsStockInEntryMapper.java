package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockInEntry;
import com.qihang.erp.api.domain.WmsStockInEntryItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单Mapper接口
 * 
 * @author qihang
 * @date 2023-12-31
 */
@Mapper
public interface WmsStockInEntryMapper 
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
    public List<WmsStockInEntryItem> selectWmsStockInEntryItemByEntryId(Long entryId);


    /**
     * 新增入库单
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    public int insertWmsStockInEntry(WmsStockInEntry wmsStockInEntry);

    /**
     * 修改入库单
     * 
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    public int updateWmsStockInEntry(WmsStockInEntry wmsStockInEntry);
    public int updateWmsStockInEntryItem(WmsStockInEntryItem item);

    /**
     * 删除入库单
     * 
     * @param id 入库单主键
     * @return 结果
     */
    public int deleteWmsStockInEntryById(Long id);

    /**
     * 批量删除入库单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsStockInEntryByIds(Long[] ids);

    /**
     * 批量删除入库单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsStockInEntryItemByEntryIds(Long[] ids);
    
    /**
     * 批量新增入库单明细
     * 
     * @param wmsStockInEntryItemList 入库单明细列表
     * @return 结果
     */
    public int batchWmsStockInEntryItem(List<WmsStockInEntryItem> wmsStockInEntryItemList);
    

    /**
     * 通过入库单主键删除入库单明细信息
     * 
     * @param id 入库单ID
     * @return 结果
     */
    public int deleteWmsStockInEntryItemByEntryId(Long id);
}
