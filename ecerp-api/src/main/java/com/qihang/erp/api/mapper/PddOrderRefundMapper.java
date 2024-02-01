package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.PddOrderRefund;
import org.apache.ibatis.annotations.Mapper;

/**
 * 拼多多订单退款Mapper接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Mapper
public interface PddOrderRefundMapper 
{
    /**
     * 查询拼多多订单退款
     * 
     * @param id 拼多多订单退款主键
     * @return 拼多多订单退款
     */
    public PddOrderRefund selectPddOrderRefundById(Long id);

    /**
     * 查询拼多多订单退款列表
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 拼多多订单退款集合
     */
    public List<PddOrderRefund> selectPddOrderRefundList(PddOrderRefund pddOrderRefund);

    /**
     * 新增拼多多订单退款
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 结果
     */
    public int insertPddOrderRefund(PddOrderRefund pddOrderRefund);

    /**
     * 修改拼多多订单退款
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 结果
     */
    public int updatePddOrderRefund(PddOrderRefund pddOrderRefund);

    /**
     * 删除拼多多订单退款
     * 
     * @param id 拼多多订单退款主键
     * @return 结果
     */
    public int deletePddOrderRefundById(Long id);

    /**
     * 批量删除拼多多订单退款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePddOrderRefundByIds(Long[] ids);
}
