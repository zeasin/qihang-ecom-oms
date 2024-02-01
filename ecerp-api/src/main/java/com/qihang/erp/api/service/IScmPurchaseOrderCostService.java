package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ScmPurchaseOrderCost;

/**
 * 采购订单费用确认Service接口
 * 
 * @author qihang
 * @date 2023-12-30
 */
public interface IScmPurchaseOrderCostService 
{
    /**
     * 查询采购订单费用确认
     * 
     * @param id 采购订单费用确认主键
     * @return 采购订单费用确认
     */
    public ScmPurchaseOrderCost selectScmPurchaseOrderCostById(Long id);

    /**
     * 查询采购订单费用确认列表
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 采购订单费用确认集合
     */
    public List<ScmPurchaseOrderCost> selectScmPurchaseOrderCostList(ScmPurchaseOrderCost scmPurchaseOrderCost);

    /**
     * 新增采购订单费用确认
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 结果
     */
    public int insertScmPurchaseOrderCost(ScmPurchaseOrderCost scmPurchaseOrderCost);

    /**
     * 修改采购订单费用确认
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 结果
     */
    public int updateScmPurchaseOrderCost(ScmPurchaseOrderCost scmPurchaseOrderCost);

    /**
     * 批量删除采购订单费用确认
     * 
     * @param ids 需要删除的采购订单费用确认主键集合
     * @return 结果
     */
    public int deleteScmPurchaseOrderCostByIds(Long[] ids);

    /**
     * 删除采购订单费用确认信息
     * 
     * @param id 采购订单费用确认主键
     * @return 结果
     */
    public int deleteScmPurchaseOrderCostById(Long id);
}
