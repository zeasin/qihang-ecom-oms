package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ScmPurchaseOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单明细Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface ScmPurchaseOrderItemMapper 
{
    /**
     * 查询采购订单明细
     * 
     * @param id 采购订单明细主键
     * @return 采购订单明细
     */
    public ScmPurchaseOrderItem selectScmPurchaseOrderItemById(Long id);

    /**
     * 查询采购订单明细列表
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 采购订单明细集合
     */
    public List<ScmPurchaseOrderItem> selectScmPurchaseOrderItemList(ScmPurchaseOrderItem scmPurchaseOrderItem);

    /**
     * 新增采购订单明细
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 结果
     */
    public int insertScmPurchaseOrderItem(ScmPurchaseOrderItem scmPurchaseOrderItem);

    /**
     * 修改采购订单明细
     * 
     * @param scmPurchaseOrderItem 采购订单明细
     * @return 结果
     */
    public int updateScmPurchaseOrderItem(ScmPurchaseOrderItem scmPurchaseOrderItem);

    /**
     * 删除采购订单明细
     * 
     * @param id 采购订单明细主键
     * @return 结果
     */
    public int deleteScmPurchaseOrderItemById(Long id);

    /**
     * 批量删除采购订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScmPurchaseOrderItemByIds(Long[] ids);
}
