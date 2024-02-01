package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.TaoOrderRefund;
import org.apache.ibatis.annotations.Mapper;

/**
 * 淘宝退款订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Mapper
public interface TaoOrderRefundMapper 
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
    public int updateTaoOrderRefund(TaoOrderRefund taoOrderRefund);

    /**
     * 删除淘宝退款订单
     * 
     * @param id 淘宝退款订单主键
     * @return 结果
     */
    public int deleteTaoOrderRefundById(Long id);

    /**
     * 批量删除淘宝退款订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderRefundByIds(Long[] ids);
}
