package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.DouOrderRefund;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抖店订单退款Mapper接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Mapper
public interface DouOrderRefundMapper 
{
    /**
     * 查询抖店订单退款
     * 
     * @param id 抖店订单退款主键
     * @return 抖店订单退款
     */
    public DouOrderRefund selectDouOrderRefundById(Long id);

    /**
     * 查询抖店订单退款列表
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 抖店订单退款集合
     */
    public List<DouOrderRefund> selectDouOrderRefundList(DouOrderRefund douOrderRefund);

    /**
     * 新增抖店订单退款
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 结果
     */
    public int insertDouOrderRefund(DouOrderRefund douOrderRefund);

    /**
     * 修改抖店订单退款
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 结果
     */
    public int updateDouOrderRefund(DouOrderRefund douOrderRefund);

    /**
     * 删除抖店订单退款
     * 
     * @param id 抖店订单退款主键
     * @return 结果
     */
    public int deleteDouOrderRefundById(Long id);

    /**
     * 批量删除抖店订单退款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDouOrderRefundByIds(Long[] ids);
}
