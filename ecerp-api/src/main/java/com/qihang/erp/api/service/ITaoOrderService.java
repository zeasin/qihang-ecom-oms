package com.qihang.erp.api.service;

import java.util.List;

import com.qihang.erp.api.common.ResultVo;
import com.qihang.erp.api.domain.TaoOrder;

/**
 * 淘宝订单Service接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
public interface ITaoOrderService 
{
    /**
     * 查询淘宝订单
     * 
     * @param id 淘宝订单主键
     * @return 淘宝订单
     */
    public TaoOrder selectTaoOrderById(String id);

    /**
     * 查询淘宝订单列表
     * 
     * @param taoOrder 淘宝订单
     * @return 淘宝订单集合
     */
    public List<TaoOrder> selectTaoOrderList(TaoOrder taoOrder);

    /**
     * 新增淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int insertTaoOrder(TaoOrder taoOrder);
    public int confirmOrder(TaoOrder taoOrder);


    /**
     * 批量删除淘宝订单
     * 
     * @param ids 需要删除的淘宝订单主键集合
     * @return 结果
     */
    public int deleteTaoOrderByIds(Long[] ids);

    /**
     * 删除淘宝订单信息
     * 
     * @param id 淘宝订单主键
     * @return 结果
     */
    public int deleteTaoOrderById(Long id);

    ResultVo<Integer> updateTmallOrderForOpenTaobao(Long shopId, TaoOrder order);
}
