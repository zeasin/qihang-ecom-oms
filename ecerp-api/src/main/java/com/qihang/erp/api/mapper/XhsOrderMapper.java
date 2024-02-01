package com.qihang.erp.api.mapper;

import java.util.List;

import com.qihang.erp.api.domain.TaoOrderItem;
import com.qihang.erp.api.domain.XhsOrder;
import com.qihang.erp.api.domain.XhsOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 小红书订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Mapper
public interface XhsOrderMapper 
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
    List<XhsOrderItem> selectOrderItemByOrderId(Long orderId);

    /**
     * 新增小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    public int insertXhsOrder(XhsOrder xhsOrder);

    /**
     * 修改小红书订单
     * 
     * @param xhsOrder 小红书订单
     * @return 结果
     */
    public int updateXhsOrder(XhsOrder xhsOrder);

    /**
     * 删除小红书订单
     * 
     * @param id 小红书订单主键
     * @return 结果
     */
    public int deleteXhsOrderById(Long id);

    /**
     * 批量删除小红书订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXhsOrderByIds(Long[] ids);

    /**
     * 批量删除小红书订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXhsOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增小红书订单明细
     * 
     * @param xhsOrderItemList 小红书订单明细列表
     * @return 结果
     */
    public int batchXhsOrderItem(List<XhsOrderItem> xhsOrderItemList);
    

    /**
     * 通过小红书订单主键删除小红书订单明细信息
     * 
     * @param id 小红书订单ID
     * @return 结果
     */
    public int deleteXhsOrderItemByOrderId(Long id);
}
