package com.qihang.erp.api.service;

import java.util.List;
import com.qihang.erp.api.domain.FmsPayablePurchase;

/**
 * 财务管理-应付款-采购货款Service接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
public interface IFmsPayablePurchaseService 
{
    /**
     * 查询财务管理-应付款-采购货款
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 财务管理-应付款-采购货款
     */
    public FmsPayablePurchase selectFmsPayablePurchaseById(Long id);

    /**
     * 查询财务管理-应付款-采购货款列表
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 财务管理-应付款-采购货款集合
     */
    public List<FmsPayablePurchase> selectFmsPayablePurchaseList(FmsPayablePurchase fmsPayablePurchase);

    /**
     * 新增财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    public int insertFmsPayablePurchase(FmsPayablePurchase fmsPayablePurchase);

    /**
     * 修改财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    public int updateFmsPayablePurchase(FmsPayablePurchase fmsPayablePurchase);

    /**
     * 批量删除财务管理-应付款-采购货款
     * 
     * @param ids 需要删除的财务管理-应付款-采购货款主键集合
     * @return 结果
     */
    public int deleteFmsPayablePurchaseByIds(Long[] ids);

    /**
     * 删除财务管理-应付款-采购货款信息
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 结果
     */
    public int deleteFmsPayablePurchaseById(Long id);
}
