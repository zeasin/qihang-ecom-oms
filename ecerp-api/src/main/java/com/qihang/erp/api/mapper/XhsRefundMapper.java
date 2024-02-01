package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.XhsRefund;
import com.qihang.erp.api.domain.XhsRefundItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 小红书订单退款Mapper接口
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Mapper
public interface XhsRefundMapper 
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
     * 删除小红书订单退款
     * 
     * @param id 小红书订单退款主键
     * @return 结果
     */
    public int deleteXhsRefundById(Long id);

    /**
     * 批量删除小红书订单退款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXhsRefundByIds(Long[] ids);

    /**
     * 批量删除小红书订单退款明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXhsRefundItemByRefundIds(Long[] ids);
    
    /**
     * 批量新增小红书订单退款明细
     * 
     * @param xhsRefundItemList 小红书订单退款明细列表
     * @return 结果
     */
    public int batchXhsRefundItem(List<XhsRefundItem> xhsRefundItemList);
    

    /**
     * 通过小红书订单退款主键删除小红书订单退款明细信息
     * 
     * @param id 小红书订单退款ID
     * @return 结果
     */
    public int deleteXhsRefundItemByRefundId(Long id);
}
