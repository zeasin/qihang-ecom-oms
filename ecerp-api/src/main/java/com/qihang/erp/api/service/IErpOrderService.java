package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ErpOrder;

/**
 * 订单Service接口
 * 
 * @author qihang
 * @date 2024-01-04
 */
public interface IErpOrderService 
{
    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    public ErpOrder selectErpOrderById(Long id);

    /**
     * 查询订单列表
     * 
     * @param erpOrder 订单
     * @return 订单集合
     */
    public List<ErpOrder> selectErpOrderList(ErpOrder erpOrder);

    /**
     * 新增订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    public int insertErpOrder(ErpOrder erpOrder);
    public int shipErpOrder(ErpOrder erpOrder);


    /**
     * 修改订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    public int updateErpOrder(ErpOrder erpOrder);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteErpOrderByIds(Long[] ids);

    /**
     * 删除订单信息
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteErpOrderById(Long id);
}
