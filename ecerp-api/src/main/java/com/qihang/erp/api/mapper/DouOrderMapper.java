package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.DouOrder;
import com.qihang.erp.api.domain.DouOrderItem;
import com.qihang.erp.api.domain.PddOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抖店订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-02
 */
@Mapper
public interface DouOrderMapper 
{
    /**
     * 查询抖店订单
     * 
     * @param id 抖店订单主键
     * @return 抖店订单
     */
    public DouOrder selectDouOrderById(Long id);
    public DouOrderItem selectDouOrderItemBySubOrderId(String subOrderId);

    /**
     * 查询抖店订单列表
     * 
     * @param douOrder 抖店订单
     * @return 抖店订单集合
     */
    public List<DouOrder> selectDouOrderList(DouOrder douOrder);

    List<DouOrderItem> selectOrderItemByOrderId(Long orderId);
    /**
     * 新增抖店订单
     * 
     * @param douOrder 抖店订单
     * @return 结果
     */
    public int insertDouOrder(DouOrder douOrder);

    /**
     * 修改抖店订单
     * 
     * @param douOrder 抖店订单
     * @return 结果
     */
    public int updateDouOrder(DouOrder douOrder);
    public int updateDouOrderItem(DouOrderItem douOrderItem);

    /**
     * 删除抖店订单
     * 
     * @param id 抖店订单主键
     * @return 结果
     */
    public int deleteDouOrderById(Long id);

    /**
     * 批量删除抖店订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDouOrderByIds(Long[] ids);

    /**
     * 批量删除抖店订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDouOrderItemByDouyinOrderIds(Long[] ids);
    
    /**
     * 批量新增抖店订单明细
     * 
     * @param douOrderItemList 抖店订单明细列表
     * @return 结果
     */
    public int batchDouOrderItem(List<DouOrderItem> douOrderItemList);
    

    /**
     * 通过抖店订单主键删除抖店订单明细信息
     * 
     * @param id 抖店订单ID
     * @return 结果
     */
    public int deleteDouOrderItemByDouyinOrderId(Long id);
}
