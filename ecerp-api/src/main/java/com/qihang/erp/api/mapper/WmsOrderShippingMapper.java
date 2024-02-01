package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.WmsOrderShipping;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库订单发货Mapper接口
 * 
 * @author qihang
 * @date 2024-01-07
 */
@Mapper
public interface WmsOrderShippingMapper 
{
    /**
     * 查询仓库订单发货
     * 
     * @param id 仓库订单发货主键
     * @return 仓库订单发货
     */
    public WmsOrderShipping selectWmsOrderShippingById(Long id);

    /**
     * 查询仓库订单发货列表
     * 
     * @param wmsOrderShipping 仓库订单发货
     * @return 仓库订单发货集合
     */
    public List<WmsOrderShipping> selectWmsOrderShippingList(WmsOrderShipping wmsOrderShipping);
    public List<WmsOrderShipping> selectWmsOrderShippingVoByIds(Long[] ids);

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
    public int updateWmsOrderShippingStatusByErpOrderItemId(WmsOrderShipping wmsOrderShipping);

    /**
     * 删除仓库订单发货
     * 
     * @param id 仓库订单发货主键
     * @return 结果
     */
    public int deleteWmsOrderShippingById(Long id);

    /**
     * 批量删除仓库订单发货
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsOrderShippingByIds(Long[] ids);
}
