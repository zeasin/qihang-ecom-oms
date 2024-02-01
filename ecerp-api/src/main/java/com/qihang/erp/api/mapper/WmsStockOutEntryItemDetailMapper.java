package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockOutEntryItemDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库明细详情Mapper接口
 * 
 * @author qihang
 * @date 2024-01-10
 */
@Mapper
public interface WmsStockOutEntryItemDetailMapper 
{
    /**
     * 查询出库明细详情
     * 
     * @param id 出库明细详情主键
     * @return 出库明细详情
     */
    public WmsStockOutEntryItemDetail selectWmsStockOutEntryItemDetailById(Long id);

    /**
     * 查询出库明细详情列表
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 出库明细详情集合
     */
    public List<WmsStockOutEntryItemDetail> selectWmsStockOutEntryItemDetailList(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail);

    /**
     * 新增出库明细详情
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 结果
     */
    public int insertWmsStockOutEntryItemDetail(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail);

    /**
     * 修改出库明细详情
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 结果
     */
    public int updateWmsStockOutEntryItemDetail(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail);

    /**
     * 删除出库明细详情
     * 
     * @param id 出库明细详情主键
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemDetailById(Long id);

    /**
     * 批量删除出库明细详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemDetailByIds(Long[] ids);
}
