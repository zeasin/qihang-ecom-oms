package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.ScmPurchaseOrderShip;
import com.qihang.erp.api.domain.bo.PurchaseOrderStockInBo;

/**
 * 采购订单物流Service接口
 * 
 * @author qihang
 * @date 2023-12-30
 */
public interface IScmPurchaseOrderShipService 
{
    /**
     * 查询采购订单物流
     * 
     * @param id 采购订单物流主键
     * @return 采购订单物流
     */
    public ScmPurchaseOrderShip selectScmPurchaseOrderShipById(Long id);

    /**
     * 查询采购订单物流列表
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 采购订单物流集合
     */
    public List<ScmPurchaseOrderShip> selectScmPurchaseOrderShipList(ScmPurchaseOrderShip scmPurchaseOrderShip);

    /**
     * 新增采购订单物流
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 结果
     */
    public int insertScmPurchaseOrderShip(ScmPurchaseOrderShip scmPurchaseOrderShip);

    /**
     * 修改采购订单物流
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 结果
     */
    public int updateScmPurchaseOrderShip(ScmPurchaseOrderShip scmPurchaseOrderShip);
    public int createStockInEntry(PurchaseOrderStockInBo bo);

    /**
     * 批量删除采购订单物流
     * 
     * @param ids 需要删除的采购订单物流主键集合
     * @return 结果
     */
    public int deleteScmPurchaseOrderShipByIds(Long[] ids);

    /**
     * 删除采购订单物流信息
     * 
     * @param id 采购订单物流主键
     * @return 结果
     */
    public int deleteScmPurchaseOrderShipById(Long id);
}
