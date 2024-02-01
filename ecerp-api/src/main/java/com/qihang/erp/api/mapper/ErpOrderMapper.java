package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ErpOrder;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.domain.TaoOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-05
 */
@Mapper
public interface ErpOrderMapper 
{
    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    public ErpOrder selectErpOrderById(Long id);
    ErpOrder selectErpOrderByNum(String orderNum);

    /**
     * 查询订单列表
     * 
     * @param erpOrder 订单
     * @return 订单集合
     */
    public List<ErpOrder> selectErpOrderList(ErpOrder erpOrder);

    List<ErpOrderItem> selectOrderItemByOrderId(Long orderId);

    ErpOrderItem selectOrderItemByOrderItemNum(ErpOrderItem orderItem);

    /**
     * 新增订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    public int insertErpOrder(ErpOrder erpOrder);

    /**
     * 修改订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    public int updateErpOrder(ErpOrder erpOrder);

    /**
     * 删除订单
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteErpOrderById(Long id);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpOrderByIds(Long[] ids);

    /**
     * 批量删除订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增订单明细
     * 
     * @param erpOrderItemList 订单明细列表
     * @return 结果
     */
    public int batchErpOrderItem(List<ErpOrderItem> erpOrderItemList);
    public int insertErpOrderItem(ErpOrderItem erpOrderItem);


    /**
     * 通过订单主键删除订单明细信息
     * 
     * @param id 订单ID
     * @return 结果
     */
    public int deleteErpOrderItemByOrderId(Long id);
}
