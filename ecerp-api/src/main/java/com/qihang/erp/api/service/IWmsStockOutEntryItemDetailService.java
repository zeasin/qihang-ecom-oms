package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockOutEntryItemDetail;

/**
 * 出库明细详情Service接口
 * 
 * @author qihang
 * @date 2024-01-10
 */
public interface IWmsStockOutEntryItemDetailService 
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
     * 批量删除出库明细详情
     * 
     * @param ids 需要删除的出库明细详情主键集合
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemDetailByIds(Long[] ids);

    /**
     * 删除出库明细详情信息
     * 
     * @param id 出库明细详情主键
     * @return 结果
     */
    public int deleteWmsStockOutEntryItemDetailById(Long id);
}
