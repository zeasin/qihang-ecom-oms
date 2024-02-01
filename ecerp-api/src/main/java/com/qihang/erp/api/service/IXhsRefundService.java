package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.XhsRefund;

/**
 * 小红书订单退款Service接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
public interface IXhsRefundService 
{
    /**
     * 查询小红书订单退款
     * 
     * @param id 小红书订单退款主键
     * @return 小红书订单退款
     */
    public XhsRefund selectXhsRefundById(Long id);

    /**
     * 查询小红书订单退款列表
     * 
     * @param xhsRefund 小红书订单退款
     * @return 小红书订单退款集合
     */
    public List<XhsRefund> selectXhsRefundList(XhsRefund xhsRefund);

    /**
     * 新增小红书订单退款
     * 
     * @param xhsRefund 小红书订单退款
     * @return 结果
     */
    public int insertXhsRefund(XhsRefund xhsRefund);

    /**
     * 修改小红书订单退款
     * 
     * @param xhsRefund 小红书订单退款
     * @return 结果
     */
    public int updateXhsRefund(XhsRefund xhsRefund);

    /**
     * 批量删除小红书订单退款
     * 
     * @param ids 需要删除的小红书订单退款主键集合
     * @return 结果
     */
    public int deleteXhsRefundByIds(Long[] ids);

    /**
     * 删除小红书订单退款信息
     * 
     * @param id 小红书订单退款主键
     * @return 结果
     */
    public int deleteXhsRefundById(Long id);
}
