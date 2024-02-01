package com.qihang.erp.api.mapper;

import java.util.List;

import com.qihang.erp.api.domain.DouOrderItem;
import com.qihang.erp.api.domain.TaoOrder;
import com.qihang.erp.api.domain.TaoOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 淘宝订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Mapper
public interface TaoOrderMapper 
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
    List<TaoOrderItem> selectOrderItemByOrderId(String orderId);
    TaoOrderItem selectOrderItemBySubItemIdId(Long subItemId);
    /**
     * 新增淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int insertTaoOrder(TaoOrder taoOrder);

    /**
     * 修改淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int updateTaoOrder(TaoOrder taoOrder);
    public int updateTaoOrderItem(TaoOrderItem taoOrderItem);

    /**
     * 删除淘宝订单
     * 
     * @param id 淘宝订单主键
     * @return 结果
     */
    public int deleteTaoOrderById(Long id);

    /**
     * 批量删除淘宝订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderByIds(Long[] ids);

    /**
     * 批量删除淘宝订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增淘宝订单明细
     * 
     * @param taoOrderItemList 淘宝订单明细列表
     * @return 结果
     */
    public int batchTaoOrderItem(List<TaoOrderItem> taoOrderItemList);
    

    /**
     * 通过淘宝订单主键删除淘宝订单明细信息
     * 
     * @param id 淘宝订单ID
     * @return 结果
     */
    public int deleteTaoOrderItemByOrderId(Long id);
}
