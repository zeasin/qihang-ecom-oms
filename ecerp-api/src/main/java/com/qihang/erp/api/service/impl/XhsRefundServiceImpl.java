package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.domain.XhsRefundItem;
import com.qihang.erp.api.mapper.XhsRefundMapper;
import com.qihang.erp.api.domain.XhsRefund;
import com.qihang.erp.api.service.IXhsRefundService;

/**
 * 小红书订单退款Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Service
public class XhsRefundServiceImpl implements IXhsRefundService 
{
    @Autowired
    private XhsRefundMapper xhsRefundMapper;

    /**
     * 查询小红书订单退款
     * 
     * @param id 小红书订单退款主键
     * @return 小红书订单退款
     */
    @Override
    public XhsRefund selectXhsRefundById(Long id)
    {
        return xhsRefundMapper.selectXhsRefundById(id);
    }

    /**
     * 查询小红书订单退款列表
     * 
     * @param xhsRefund 小红书订单退款
     * @return 小红书订单退款
     */
    @Override
    public List<XhsRefund> selectXhsRefundList(XhsRefund xhsRefund)
    {
        return xhsRefundMapper.selectXhsRefundList(xhsRefund);
    }

    /**
     * 新增小红书订单退款
     * 
     * @param xhsRefund 小红书订单退款
     * @return 结果
     */
    @Transactional
    @Override
    public int insertXhsRefund(XhsRefund xhsRefund)
    {
        int rows = xhsRefundMapper.insertXhsRefund(xhsRefund);
        insertXhsRefundItem(xhsRefund);
        return rows;
    }

    /**
     * 修改小红书订单退款
     * 
     * @param xhsRefund 小红书订单退款
     * @return 结果
     */
    @Transactional
    @Override
    public int updateXhsRefund(XhsRefund xhsRefund)
    {
        xhsRefund.setUpdateTime(DateUtils.getNowDate());
        xhsRefundMapper.deleteXhsRefundItemByRefundId(xhsRefund.getId());
        insertXhsRefundItem(xhsRefund);
        return xhsRefundMapper.updateXhsRefund(xhsRefund);
    }

    /**
     * 批量删除小红书订单退款
     * 
     * @param ids 需要删除的小红书订单退款主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteXhsRefundByIds(Long[] ids)
    {
        xhsRefundMapper.deleteXhsRefundItemByRefundIds(ids);
        return xhsRefundMapper.deleteXhsRefundByIds(ids);
    }

    /**
     * 删除小红书订单退款信息
     * 
     * @param id 小红书订单退款主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteXhsRefundById(Long id)
    {
        xhsRefundMapper.deleteXhsRefundItemByRefundId(id);
        return xhsRefundMapper.deleteXhsRefundById(id);
    }

    /**
     * 新增小红书订单退款明细信息
     * 
     * @param xhsRefund 小红书订单退款对象
     */
    public void insertXhsRefundItem(XhsRefund xhsRefund)
    {
        List<XhsRefundItem> xhsRefundItemList = xhsRefund.getXhsRefundItemList();
        Long id = xhsRefund.getId();
        if (StringUtils.isNotNull(xhsRefundItemList))
        {
            List<XhsRefundItem> list = new ArrayList<XhsRefundItem>();
            for (XhsRefundItem xhsRefundItem : xhsRefundItemList)
            {
                xhsRefundItem.setRefundId(id);
                list.add(xhsRefundItem);
            }
            if (list.size() > 0)
            {
                xhsRefundMapper.batchXhsRefundItem(list);
            }
        }
    }
}
