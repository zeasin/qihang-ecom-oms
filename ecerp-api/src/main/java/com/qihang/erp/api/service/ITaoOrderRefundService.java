package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.TaoOrderRefund;

/**
 * 淘宝退款订单Service接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
public interface ITaoOrderRefundService 
{
    /**
     * 查询淘宝退款订单
     * 
     * @param id 淘宝退款订单主键
     * @return 淘宝退款订单
     */
    public TaoOrderRefund selectTaoOrderRefundById(Long id);

    /**
     * 查询淘宝退款订单列表
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 淘宝退款订单集合
     */
    public List<TaoOrderRefund> selectTaoOrderRefundList(TaoOrderRefund taoOrderRefund);

    /**
     * 新增淘宝退款订单
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 结果
     */
    public int insertTaoOrderRefund(TaoOrderRefund taoOrderRefund);

    /**
     * 修改淘宝退款订单
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 结果
     */
    public int confirmRefund(TaoOrderRefund taoOrderRefund);

    /**
     * 批量删除淘宝退款订单
     * 
     * @param ids 需要删除的淘宝退款订单主键集合
     * @return 结果
     */
    public int deleteTaoOrderRefundByIds(Long[] ids);

    /**
     * 删除淘宝退款订单信息
     * 
     * @param id 淘宝退款订单主键
     * @return 结果
     */
    public int deleteTaoOrderRefundById(Long id);

    Integer updOrderRefund(Long shopId,TaoOrderRefund refund);
}
