package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.XhsOrderReceiver;

/**
 * 订单收件人信息Service接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
public interface IXhsOrderReceiverService 
{
    /**
     * 查询订单收件人信息
     * 
     * @param id 订单收件人信息主键
     * @return 订单收件人信息
     */
    public XhsOrderReceiver selectXhsOrderReceiverById(Long id);

    /**
     * 查询订单收件人信息列表
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 订单收件人信息集合
     */
    public List<XhsOrderReceiver> selectXhsOrderReceiverList(XhsOrderReceiver xhsOrderReceiver);

    /**
     * 新增订单收件人信息
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 结果
     */
    public int insertXhsOrderReceiver(XhsOrderReceiver xhsOrderReceiver);

    /**
     * 修改订单收件人信息
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 结果
     */
    public int updateXhsOrderReceiver(XhsOrderReceiver xhsOrderReceiver);

    /**
     * 批量删除订单收件人信息
     * 
     * @param ids 需要删除的订单收件人信息主键集合
     * @return 结果
     */
    public int deleteXhsOrderReceiverByIds(Long[] ids);

    /**
     * 删除订单收件人信息信息
     * 
     * @param id 订单收件人信息主键
     * @return 结果
     */
    public int deleteXhsOrderReceiverById(Long id);
}
