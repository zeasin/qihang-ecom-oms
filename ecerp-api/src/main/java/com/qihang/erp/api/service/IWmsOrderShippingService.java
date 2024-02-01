package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.WmsOrderShipping;
import com.qihang.erp.api.domain.bo.StockingAddVo;

/**
 * 仓库订单发货Service接口
 * 
 * @author qihang
 * @date 2024-01-07
 */
public interface IWmsOrderShippingService 
{
    /**
     * 查询仓库订单发货
     * 
     * @param id 仓库订单发货主键
     * @return 仓库订单发货
     */
    public WmsOrderShipping selectWmsOrderShippingById(Long id);

    /**
     * 生成拣货单
     * @param addVo
     * @return
     */
    int stockingAdd(StockingAddVo addVo);

    /**
     * 查询仓库订单发货列表
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 仓库订单发货集合
     */
    public List<WmsOrderShipping> selectWmsOrderShippingList(WmsOrderShipping wmsOrderShipping);

    /**
     * 新增仓库订单发货
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 结果
     */
    public int insertWmsOrderShipping(WmsOrderShipping wmsOrderShipping);

    /**
     * 修改仓库订单发货
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 结果
     */
    public int updateWmsOrderShipping(WmsOrderShipping wmsOrderShipping);

    /**
     * 批量删除仓库订单发货
     * 
     * @param ids 需要删除的仓库订单发货主键集合
     * @return 结果
     */
    public int deleteWmsOrderShippingByIds(Long[] ids);

    /**
     * 删除仓库订单发货信息
     * 
     * @param id 仓库订单发货主键
     * @return 结果
     */
    public int deleteWmsOrderShippingById(Long id);
}
