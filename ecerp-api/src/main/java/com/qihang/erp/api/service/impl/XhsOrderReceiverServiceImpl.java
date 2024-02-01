package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.XhsOrderReceiverMapper;
import com.qihang.erp.api.domain.XhsOrderReceiver;
import com.qihang.erp.api.service.IXhsOrderReceiverService;

/**
 * 订单收件人信息Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Service
public class XhsOrderReceiverServiceImpl implements IXhsOrderReceiverService 
{
    @Autowired
    private XhsOrderReceiverMapper xhsOrderReceiverMapper;

    /**
     * 查询订单收件人信息
     * 
     * @param id 订单收件人信息主键
     * @return 订单收件人信息
     */
    @Override
    public XhsOrderReceiver selectXhsOrderReceiverById(Long id)
    {
        return xhsOrderReceiverMapper.selectXhsOrderReceiverById(id);
    }

    /**
     * 查询订单收件人信息列表
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 订单收件人信息
     */
    @Override
    public List<XhsOrderReceiver> selectXhsOrderReceiverList(XhsOrderReceiver xhsOrderReceiver)
    {
        return xhsOrderReceiverMapper.selectXhsOrderReceiverList(xhsOrderReceiver);
    }

    /**
     * 新增订单收件人信息
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 结果
     */
    @Override
    public int insertXhsOrderReceiver(XhsOrderReceiver xhsOrderReceiver)
    {
        return xhsOrderReceiverMapper.insertXhsOrderReceiver(xhsOrderReceiver);
    }

    /**
     * 修改订单收件人信息
     * 
     * @param xhsOrderReceiver 订单收件人信息
     * @return 结果
     */
    @Override
    public int updateXhsOrderReceiver(XhsOrderReceiver xhsOrderReceiver)
    {
        return xhsOrderReceiverMapper.updateXhsOrderReceiver(xhsOrderReceiver);
    }

    /**
     * 批量删除订单收件人信息
     * 
     * @param ids 需要删除的订单收件人信息主键
     * @return 结果
     */
    @Override
    public int deleteXhsOrderReceiverByIds(Long[] ids)
    {
        return xhsOrderReceiverMapper.deleteXhsOrderReceiverByIds(ids);
    }

    /**
     * 删除订单收件人信息信息
     * 
     * @param id 订单收件人信息主键
     * @return 结果
     */
    @Override
    public int deleteXhsOrderReceiverById(Long id)
    {
        return xhsOrderReceiverMapper.deleteXhsOrderReceiverById(id);
    }
}
