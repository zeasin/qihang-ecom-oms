package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.WmsStockLocation;

/**
 * 仓库货架Service接口
 * 
 * @author qihang
 * @date 2024-01-09
 */
public interface IWmsStockLocationService 
{
    /**
     * 查询仓库货架
     * 
     * @param id 仓库货架主键
     * @return 仓库货架
     */
    public WmsStockLocation selectWmsStockLocationById(Long id);

    /**
     * 查询仓库货架列表
     * 
     * @param wmsStockLocation 仓库货架
     * @return 仓库货架集合
     */
    public List<WmsStockLocation> selectWmsStockLocationList(WmsStockLocation wmsStockLocation);
    public List<WmsStockLocation> search(String number);

    /**
     * 新增仓库货架
     * 
     * @param wmsStockLocation 仓库货架
     * @return 结果
     */
    public int insertWmsStockLocation(WmsStockLocation wmsStockLocation);

    /**
     * 修改仓库货架
     * 
     * @param wmsStockLocation 仓库货架
     * @return 结果
     */
    public int updateWmsStockLocation(WmsStockLocation wmsStockLocation);

    /**
     * 批量删除仓库货架
     * 
     * @param ids 需要删除的仓库货架主键集合
     * @return 结果
     */
    public int deleteWmsStockLocationByIds(Long[] ids);

    /**
     * 删除仓库货架信息
     * 
     * @param id 仓库货架主键
     * @return 结果
     */
    public int deleteWmsStockLocationById(Long id);
}
