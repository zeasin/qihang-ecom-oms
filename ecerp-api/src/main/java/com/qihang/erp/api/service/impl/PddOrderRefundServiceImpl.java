package com.qihang.erp.api.service.impl;

import java.util.List;

import com.qihang.erp.api.domain.PddOrder;
import com.qihang.erp.api.domain.PddOrderItem;
import com.qihang.erp.api.mapper.PddOrderMapper;
import com.zhijian.core.web.domain.server.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.PddOrderRefundMapper;
import com.qihang.erp.api.domain.PddOrderRefund;
import com.qihang.erp.api.service.IPddOrderRefundService;

/**
 * 拼多多订单退款Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Service
public class PddOrderRefundServiceImpl implements IPddOrderRefundService 
{
    @Autowired
    private PddOrderRefundMapper pddOrderRefundMapper;
    @Autowired
    private PddOrderMapper pddOrderMapper;

    /**
     * 查询拼多多订单退款
     * 
     * @param id 拼多多订单退款主键
     * @return 拼多多订单退款
     */
    @Override
    public PddOrderRefund selectPddOrderRefundById(Long id)
    {
        return pddOrderRefundMapper.selectPddOrderRefundById(id);
    }

    /**
     * 查询拼多多订单退款列表
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 拼多多订单退款
     */
    @Override
    public List<PddOrderRefund> selectPddOrderRefundList(PddOrderRefund pddOrderRefund)
    {
        return pddOrderRefundMapper.selectPddOrderRefundList(pddOrderRefund);
    }

    /**
     * 新增拼多多订单退款
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 结果
     */
    @Override
    public int insertPddOrderRefund(PddOrderRefund pddOrderRefund)
    {
        PddOrderItem orderItem = pddOrderMapper.selectOrderItemByOrderItemId(pddOrderRefund.getOrderItemId());
        if(orderItem == null)  return -1;
        else if(orderItem.getRefundStatus()!=1) return -2;
        PddOrder pddOrder = pddOrderMapper.selectPddOrderById(orderItem.getOrderId());
        PddOrderRefund insert = new PddOrderRefund();
        insert.setId(pddOrderRefund.getId());
        insert.setAfterSalesType(pddOrderRefund.getAfterSalesType());
        insert.setOrderSn(pddOrder.getOrderSn());
        insert.setShopId(pddOrder.getShopId());
        if(pddOrderRefund.getAfterSalesType() == 2){
            insert.setAfterSalesStatus(4L);
        }else if(pddOrderRefund.getAfterSalesType() == 3){
            insert.setAfterSalesStatus(7L);
        }else if(pddOrderRefund.getAfterSalesType() == 4){
            insert.setAfterSalesStatus(15L);
        }else if(pddOrderRefund.getAfterSalesType() == 5){
            insert.setAfterSalesStatus(32L);
        }else if(pddOrderRefund.getAfterSalesType() == 9){
            insert.setAfterSalesStatus(31L);
        }
        insert.setAfterSaleReason("");
        insert.setConfirmTime(0L);
        insert.setCreatedTime(System.currentTimeMillis() / 1000);
        insert.setDiscountAmount(pddOrder.getDiscountAmount());
        insert.setOrderAmount(pddOrder.getPayAmount());
        insert.setRefundAmount(pddOrderRefund.getRefundAmount());
        insert.setGoodsId(orderItem.getGoodId());
        insert.setSkuId(orderItem.getSkuId());
        insert.setGoodsImage(orderItem.getGoodsImage());
        insert.setGoodsName(orderItem.getGoodsName());
        insert.setGoodsNumber(orderItem.getGoodsNum());
        insert.setSpecNumber(orderItem.getSpecNum());
        insert.setGoodsSpec(orderItem.getGoodsSpec());
        insert.setQuantity(pddOrderRefund.getRefundQty());
        insert.setGoodsPrice(orderItem.getGoodsPrice());
        insert.setAuditStatus(0L);
        pddOrderRefundMapper.insertPddOrderRefund(insert);

        // 更新 order_item 状态
        PddOrderItem up = new PddOrderItem();
        up.setId(orderItem.getId());
        up.setRefundStatus(2L);
        up.setRefundCount(pddOrderRefund.getRefundQty().longValue());
        pddOrderMapper.updatePddOrderItem(up);
        return 1;
    }

    /**
     * 修改拼多多订单退款
     * 
     * @param pddOrderRefund 拼多多订单退款
     * @return 结果
     */
    @Override
    public int updatePddOrderRefund(PddOrderRefund pddOrderRefund)
    {
        return pddOrderRefundMapper.updatePddOrderRefund(pddOrderRefund);
    }

    /**
     * 批量删除拼多多订单退款
     * 
     * @param ids 需要删除的拼多多订单退款主键
     * @return 结果
     */
    @Override
    public int deletePddOrderRefundByIds(Long[] ids)
    {
        return pddOrderRefundMapper.deletePddOrderRefundByIds(ids);
    }

    /**
     * 删除拼多多订单退款信息
     * 
     * @param id 拼多多订单退款主键
     * @return 结果
     */
    @Override
    public int deletePddOrderRefundById(Long id)
    {
        return pddOrderRefundMapper.deletePddOrderRefundById(id);
    }
}
