package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.WmsStockLocationMapper;
import com.qihang.erp.api.domain.WmsStockLocation;
import com.qihang.erp.api.service.IWmsStockLocationService;

/**
 * 仓库货架Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-09
 */
@Service
public class WmsStockLocationServiceImpl implements IWmsStockLocationService 
{
    @Autowired
    private WmsStockLocationMapper wmsStockLocationMapper;

    /**
     * 查询仓库货架
     * 
     * @param id 仓库货架主键
     * @return 仓库货架
     */
    @Override
    public WmsStockLocation selectWmsStockLocationById(Long id)
    {
        return wmsStockLocationMapper.selectWmsStockLocationById(id);
    }

    /**
     * 查询仓库货架列表
     * 
     * @param wmsStockLocation 仓库货架
     * @return 仓库货架
     */
    @Override
    public List<WmsStockLocation> selectWmsStockLocationList(WmsStockLocation wmsStockLocation)
    {
        return wmsStockLocationMapper.selectWmsStockLocationList(wmsStockLocation);
    }

    @Override
    public List<WmsStockLocation> search(String number) {
        return wmsStockLocationMapper.search(number);
    }

    /**
     * 新增仓库货架
     * 
     * @param wmsStockLocation 仓库货架
     * @return 结果
     */
    @Override
    public int insertWmsStockLocation(WmsStockLocation wmsStockLocation)
    {
        wmsStockLocation.setCreateTime(DateUtils.getNowDate());
        return wmsStockLocationMapper.insertWmsStockLocation(wmsStockLocation);
    }

    /**
     * 修改仓库货架
     * 
     * @param wmsStockLocation 仓库货架
     * @return 结果
     */
    @Override
    public int updateWmsStockLocation(WmsStockLocation wmsStockLocation)
    {
        wmsStockLocation.setUpdateTime(DateUtils.getNowDate());
        return wmsStockLocationMapper.updateWmsStockLocation(wmsStockLocation);
    }

    /**
     * 批量删除仓库货架
     * 
     * @param ids 需要删除的仓库货架主键
     * @return 结果
     */
    @Override
    public int deleteWmsStockLocationByIds(Long[] ids)
    {
        return wmsStockLocationMapper.deleteWmsStockLocationByIds(ids);
    }

    /**
     * 删除仓库货架信息
     * 
     * @param id 仓库货架主键
     * @return 结果
     */
    @Override
    public int deleteWmsStockLocationById(Long id)
    {
        return wmsStockLocationMapper.deleteWmsStockLocationById(id);
    }
}
