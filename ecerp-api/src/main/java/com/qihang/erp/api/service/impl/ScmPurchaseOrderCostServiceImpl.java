package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ScmPurchaseOrderCostMapper;
import com.qihang.erp.api.domain.ScmPurchaseOrderCost;
import com.qihang.erp.api.service.IScmPurchaseOrderCostService;

/**
 * 采购订单费用确认Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-30
 */
@Service
public class ScmPurchaseOrderCostServiceImpl implements IScmPurchaseOrderCostService 
{
    @Autowired
    private ScmPurchaseOrderCostMapper scmPurchaseOrderCostMapper;

    /**
     * 查询采购订单费用确认
     * 
     * @param id 采购订单费用确认主键
     * @return 采购订单费用确认
     */
    @Override
    public ScmPurchaseOrderCost selectScmPurchaseOrderCostById(Long id)
    {
        return scmPurchaseOrderCostMapper.selectScmPurchaseOrderCostById(id);
    }

    /**
     * 查询采购订单费用确认列表
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 采购订单费用确认
     */
    @Override
    public List<ScmPurchaseOrderCost> selectScmPurchaseOrderCostList(ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        return scmPurchaseOrderCostMapper.selectScmPurchaseOrderCostList(scmPurchaseOrderCost);
    }

    /**
     * 新增采购订单费用确认
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 结果
     */
    @Override
    public int insertScmPurchaseOrderCost(ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        return scmPurchaseOrderCostMapper.insertScmPurchaseOrderCost(scmPurchaseOrderCost);
    }

    /**
     * 修改采购订单费用确认
     * 
     * @param scmPurchaseOrderCost 采购订单费用确认
     * @return 结果
     */
    @Override
    public int updateScmPurchaseOrderCost(ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        scmPurchaseOrderCost.setUpdateBy(scmPurchaseOrderCost.getUpdateBy());
        scmPurchaseOrderCost.setUpdateTime(DateUtils.getNowDate());

        return scmPurchaseOrderCostMapper.updateScmPurchaseOrderCost(scmPurchaseOrderCost);
    }

    /**
     * 批量删除采购订单费用确认
     * 
     * @param ids 需要删除的采购订单费用确认主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderCostByIds(Long[] ids)
    {
        return scmPurchaseOrderCostMapper.deleteScmPurchaseOrderCostByIds(ids);
    }

    /**
     * 删除采购订单费用确认信息
     * 
     * @param id 采购订单费用确认主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderCostById(Long id)
    {
        return scmPurchaseOrderCostMapper.deleteScmPurchaseOrderCostById(id);
    }
}
