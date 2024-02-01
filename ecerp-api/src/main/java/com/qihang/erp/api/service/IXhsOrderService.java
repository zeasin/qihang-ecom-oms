package com.qihang.erp.api.service;

import java.util.List;

import com.qihang.erp.api.domain.DouOrder;
import com.qihang.erp.api.domain.XhsOrder;

/**
 * 小红书订单Service接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
public interface IXhsOrderService 
{
    /**
     * 查询小红书订单
     * 
     * @param id 小红书订单主键
     * @return 小红书订单
     */
    public XhsOrder selectXhsOrderById(Long id);

    /**
     * 查询小红书订单列表
     * 
     * @param xhsOrder 小红书订单
     * @return 小红书订单集合
     */
    public List<XhsOrder> selectXhsOrderList(XhsOrder xhsOrder);

    /**
     * 新增小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    public int insertXhsOrder(XhsOrder xhsOrder);
    public int confirmOrder(XhsOrder bo);
    /**
     * 修改小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    public int updateXhsOrder(XhsOrder xhsOrder);

    /**
     * 批量删除小红书订单
     * 
     * @param ids 需要删除的小红书订单主键集合
     * @return 结果
     */
    public int deleteXhsOrderByIds(Long[] ids);

    /**
     * 删除小红书订单信息
     * 
     * @param id 小红书订单主键
     * @return 结果
     */
    public int deleteXhsOrderById(Long id);
}
