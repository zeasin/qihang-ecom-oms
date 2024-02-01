package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ScmPurchaseOrderItemMapper;
import com.qihang.erp.api.domain.ScmPurchaseOrderItem;
import com.qihang.erp.api.service.IScmPurchaseOrderItemService;

/**
 * 采购订单明细Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ScmPurchaseOrderItemServiceImpl implements IScmPurchaseOrderItemService 
{
    @Autowired
    private ScmPurchaseOrderItemMapper scmPurchaseOrderItemMapper;

    /**
     * 查询采购订单明细
     * 
     * @param id 采购订单明细主键
     * @return 采购订单明细
     */
    @Override
    public ScmPurchaseOrderItem selectScmPurchaseOrderItemById(Long id)
    {
        return scmPurchaseOrderItemMapper.selectScmPurchaseOrderItemById(id);
    }

    /**
     * 查询采购订单明细列表
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 采购订单明细
     */
    @Override
    public List<ScmPurchaseOrderItem> selectScmPurchaseOrderItemList(ScmPurchaseOrderItem scmPurchaseOrderItem)
    {
        return scmPurchaseOrderItemMapper.selectScmPurchaseOrderItemList(scmPurchaseOrderItem);
    }

    /**
     * 新增采购订单明细
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 结果
     */
    @Override
    public int insertScmPurchaseOrderItem(ScmPurchaseOrderItem scmPurchaseOrderItem)
    {
        return scmPurchaseOrderItemMapper.insertScmPurchaseOrderItem(scmPurchaseOrderItem);
    }

    /**
     * 修改采购订单明细
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 结果
     */
    @Override
    public int updateScmPurchaseOrderItem(ScmPurchaseOrderItem scmPurchaseOrderItem)
    {
        return scmPurchaseOrderItemMapper.updateScmPurchaseOrderItem(scmPurchaseOrderItem);
    }

    /**
     * 批量删除采购订单明细
     * 
     * @param ids 需要删除的采购订单明细主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderItemByIds(Long[] ids)
    {
        return scmPurchaseOrderItemMapper.deleteScmPurchaseOrderItemByIds(ids);
    }

    /**
     * 删除采购订单明细信息
     * 
     * @param id 采购订单明细主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderItemById(Long id)
    {
        return scmPurchaseOrderItemMapper.deleteScmPurchaseOrderItemById(id);
    }
}
