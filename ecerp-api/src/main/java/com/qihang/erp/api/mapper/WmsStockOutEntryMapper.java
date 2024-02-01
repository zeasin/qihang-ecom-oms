package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockOutEntry;
import com.qihang.erp.api.domain.WmsStockOutEntryItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-10
 */
@Mapper
public interface WmsStockOutEntryMapper 
{
    /**
     * 查询出库单
     * 
     * @param id 出库单主键
     * @return 出库单
     */
    public WmsStockOutEntry selectWmsStockOutEntryById(Long id);
    public WmsStockOutEntryItem selectWmsStockOutEntryItemById(Long id);

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

    /**
     * 修改出库单
     * 
     * @param wmsStockOutEntry 出库单
     * @return 结果
     */
    public int updateWmsStockOutEntry(WmsStockOutEntry wmsStockOutEntry);
    public int updateWmsStockOutEntryItem(WmsStockOutEntryItem entryItem);

    /**
     * 删除出库单
     * 
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteWmsStockOutEntryById(Long id);

    /**
     * 批量删除出库单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsStockOutEntryByIds(Long[] ids);

    /**
     * 批量删除出库单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemByEntryIds(Long[] ids);
    
    /**
     * 批量新增出库单明细
     * 
     * @param wmsStockOutEntryItemList 出库单明细列表
     * @return 结果
     */
    public int batchWmsStockOutEntryItem(List<WmsStockOutEntryItem> wmsStockOutEntryItemList);
    

    /**
     * 通过出库单主键删除出库单明细信息
     * 
     * @param id 出库单ID
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemByEntryId(Long id);
}
