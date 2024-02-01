package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.XhsOrderReceiver;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单收件人信息Mapper接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Mapper
public interface XhsOrderReceiverMapper 
{
    /**
     * 查询订单收件人信息
     * 
     * @param id 订单收件人信息主键
     * @return 订单收件人信息
     */
    public XhsOrderReceiver selectXhsOrderReceiverById(Long id);
    public XhsOrderReceiver selectXhsOrderReceiverByOrderId(Long orderId);

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
     * 删除订单收件人信息
     * 
     * @param id 订单收件人信息主键
     * @return 结果
     */
    public int deleteXhsOrderReceiverById(Long id);

    /**
     * 批量删除订单收件人信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXhsOrderReceiverByIds(Long[] ids);
}
