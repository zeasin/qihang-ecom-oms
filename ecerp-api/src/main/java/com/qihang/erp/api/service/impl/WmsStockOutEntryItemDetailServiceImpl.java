package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.WmsStockOutEntryItemDetailMapper;
import com.qihang.erp.api.domain.WmsStockOutEntryItemDetail;
import com.qihang.erp.api.service.IWmsStockOutEntryItemDetailService;

/**
 * 出库明细详情Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-10
 */
@Service
public class WmsStockOutEntryItemDetailServiceImpl implements IWmsStockOutEntryItemDetailService 
{
    @Autowired
    private WmsStockOutEntryItemDetailMapper wmsStockOutEntryItemDetailMapper;

    /**
     * 查询出库明细详情
     * 
     * @param id 出库明细详情主键
     * @return 出库明细详情
     */
    @Override
    public WmsStockOutEntryItemDetail selectWmsStockOutEntryItemDetailById(Long id)
    {
        return wmsStockOutEntryItemDetailMapper.selectWmsStockOutEntryItemDetailById(id);
    }

    /**
     * 查询出库明细详情列表
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 出库明细详情
     */
    @Override
    public List<WmsStockOutEntryItemDetail> selectWmsStockOutEntryItemDetailList(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        return wmsStockOutEntryItemDetailMapper.selectWmsStockOutEntryItemDetailList(wmsStockOutEntryItemDetail);
    }

    /**
     * 新增出库明细详情
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 结果
     */
    @Override
    public int insertWmsStockOutEntryItemDetail(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        return wmsStockOutEntryItemDetailMapper.insertWmsStockOutEntryItemDetail(wmsStockOutEntryItemDetail);
    }

    /**
     * 修改出库明细详情
     * 
     * @param wmsStockOutEntryItemDetail 出库明细详情
     * @return 结果
     */
    @Override
    public int updateWmsStockOutEntryItemDetail(WmsStockOutEntryItemDetail wmsStockOutEntryItemDetail)
    {
        return wmsStockOutEntryItemDetailMapper.updateWmsStockOutEntryItemDetail(wmsStockOutEntryItemDetail);
    }

    /**
     * 批量删除出库明细详情
     * 
     * @param ids 需要删除的出库明细详情主键
     * @return 结果
     */
    @Override
    public int deleteWmsStockOutEntryItemDetailByIds(Long[] ids)
    {
        return wmsStockOutEntryItemDetailMapper.deleteWmsStockOutEntryItemDetailByIds(ids);
    }

    /**
     * 删除出库明细详情信息
     * 
     * @param id 出库明细详情主键
     * @return 结果
     */
    @Override
    public int deleteWmsStockOutEntryItemDetailById(Long id)
    {
        return wmsStockOutEntryItemDetailMapper.deleteWmsStockOutEntryItemDetailById(id);
    }
}
