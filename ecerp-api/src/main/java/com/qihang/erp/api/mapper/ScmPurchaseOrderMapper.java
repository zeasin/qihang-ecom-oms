package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.ScmPurchaseOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface ScmPurchaseOrderMapper 
{
    /**
     * 查询采购订单
     * 
     * @param id 采购订单主键
     * @return 采购订单
     */
    public ScmPurchaseOrder selectScmPurchaseOrderById(Long id);

    /**
     * 查询采购订单列表
     * 
     * @param scmPurchaseOrder 采购订单
     * @return 采购订单集合
     */
    public List<ScmPurchaseOrder> selectScmPurchaseOrderList(ScmPurchaseOrder scmPurchaseOrder);

    /**
     * 新增采购订单
     * 
     * @param scmPurchaseOrder 采购订单
     * @return 结果
     */
    public int insertScmPurchaseOrder(ScmPurchaseOrder scmPurchaseOrder);

    /**
     * 修改采购订单
     * 
     * @param scmPurchaseOrder 采购订单
     * @return 结果
     */
    public int updateScmPurchaseOrder(ScmPurchaseOrder scmPurchaseOrder);

    /**
     * 删除采购订单
     * 
     * @param id 采购订单主键
     * @return 结果
     */
    public int deleteScmPurchaseOrderById(Long id);

    /**
     * 批量删除采购订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScmPurchaseOrderByIds(Long[] ids);
}
