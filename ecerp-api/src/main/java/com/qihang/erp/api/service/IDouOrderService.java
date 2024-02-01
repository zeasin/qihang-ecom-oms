package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.DouOrder;

/**
 * 抖店订单Service接口
 * 
 * @author qihang
 * @date 2024-01-02
 */
public interface IDouOrderService 
{
    /**
     * 查询抖店订单
     * 
     * @param id 抖店订单主键
     * @return 抖店订单
     */
    public DouOrder selectDouOrderById(Long id);

    /**
     * 查询抖店订单列表
     * 
     * @param douOrder 抖店订单
     * @return 抖店订单集合
     */
    public List<DouOrder> selectDouOrderList(DouOrder douOrder);

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
    public int confirmOrder(DouOrder douOrder);

    /**
     * 批量删除抖店订单
     * 
     * @param ids 需要删除的抖店订单主键集合
     * @return 结果
     */
    public int deleteDouOrderByIds(Long[] ids);

    /**
     * 删除抖店订单信息
     * 
     * @param id 抖店订单主键
     * @return 结果
     */
    public int deleteDouOrderById(Long id);
}
