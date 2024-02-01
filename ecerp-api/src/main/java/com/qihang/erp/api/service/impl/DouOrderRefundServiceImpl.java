package com.qihang.erp.api.service.impl;

import java.util.List;

import com.qihang.erp.api.domain.DouOrder;
import com.qihang.erp.api.domain.DouOrderItem;
import com.qihang.erp.api.mapper.DouOrderMapper;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.DouOrderRefundMapper;
import com.qihang.erp.api.domain.DouOrderRefund;
import com.qihang.erp.api.service.IDouOrderRefundService;

/**
 * 抖店订单退款Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Service
public class DouOrderRefundServiceImpl implements IDouOrderRefundService 
{
    @Autowired
    private DouOrderRefundMapper douOrderRefundMapper;
    @Autowired
    private DouOrderMapper douOrderMapper;

    /**
     * 查询抖店订单退款
     * 
     * @param id 抖店订单退款主键
     * @return 抖店订单退款
     */
    @Override
    public DouOrderRefund selectDouOrderRefundById(Long id)
    {
        return douOrderRefundMapper.selectDouOrderRefundById(id);
    }

    /**
     * 查询抖店订单退款列表
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 抖店订单退款
     */
    @Override
    public List<DouOrderRefund> selectDouOrderRefundList(DouOrderRefund douOrderRefund)
    {
        return douOrderRefundMapper.selectDouOrderRefundList(douOrderRefund);
    }

    /**
     * 新增抖店订单退款
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 结果
     */
    @Override
    public int insertDouOrderRefund(DouOrderRefund douOrderRefund)
    {
        DouOrderItem orderItem = douOrderMapper.selectDouOrderItemBySubOrderId(douOrderRefund.getSubOrderId());
        if(orderItem == null)  return -1;
        else if(orderItem.getItemStatus()!=0) return -2;
        DouOrder order = douOrderMapper.selectDouOrderById(orderItem.getDouyinOrderId());

        DouOrderRefund insert = new DouOrderRefund();
        insert.setAftersaleId(douOrderRefund.getAftersaleId());
        insert.setAftersaleType(douOrderRefund.getAftersaleType());
        insert.setOrderId(orderItem.getOrderId());
        insert.setSubOrderId(orderItem.getSubOrderId());
        insert.setShopId(order.getShopId());
        insert.setProductPic(orderItem.getProductPic());
        insert.setProductId(orderItem.getProductId());
        insert.setProductName(orderItem.getProductName());
        insert.setGoodsNum(orderItem.getGoodsNum());
        insert.setComboId(orderItem.getComboId());
        insert.setGoodsSpec(orderItem.getGoodsSpec());
        insert.setComboNum(douOrderRefund.getComboNum());
        insert.setOrderAmount(orderItem.getTotalAmount().doubleValue());
        insert.setComboAmount(douOrderRefund.getComboAmount());
        insert.setAuditStatus(0L);
        if(douOrderRefund.getAftersaleType() == 0){
            insert.setRefundStatus(7L);
        }else if(douOrderRefund.getAftersaleType() == 1){
            insert.setRefundStatus(12L);
        }else if(douOrderRefund.getAftersaleType() == 2){
            insert.setRefundStatus(12L);
        }else if(douOrderRefund.getAftersaleType() == 3){
            insert.setRefundStatus(7L);
        }else if(douOrderRefund.getAftersaleType() == 9){
            insert.setRefundStatus(12L);
        }
        insert.setCreateTime(DateUtils.getNowDate());
        douOrderRefundMapper.insertDouOrderRefund(insert);
        // 更新 order_item 状态
        DouOrderItem up=new DouOrderItem();
        up.setId(orderItem.getId());
        up.setItemStatus(2L);
        douOrderMapper.updateDouOrderItem(up);
        return 1;
    }

    /**
     * 修改抖店订单退款
     * 
     * @param douOrderRefund 抖店订单退款
     * @return 结果
     */
    @Override
    public int updateDouOrderRefund(DouOrderRefund douOrderRefund)
    {
        douOrderRefund.setUpdateTime(DateUtils.getNowDate());
        return douOrderRefundMapper.updateDouOrderRefund(douOrderRefund);
    }

    /**
     * 批量删除抖店订单退款
     * 
     * @param ids 需要删除的抖店订单退款主键
     * @return 结果
     */
    @Override
    public int deleteDouOrderRefundByIds(Long[] ids)
    {
        return douOrderRefundMapper.deleteDouOrderRefundByIds(ids);
    }

    /**
     * 删除抖店订单退款信息
     * 
     * @param id 抖店订单退款主键
     * @return 结果
     */
    @Override
    public int deleteDouOrderRefundById(Long id)
    {
        return douOrderRefundMapper.deleteDouOrderRefundById(id);
    }
}
