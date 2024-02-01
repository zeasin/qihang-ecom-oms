package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.FmsPayablePurchase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 财务管理-应付款-采购货款Mapper接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Mapper
public interface FmsPayablePurchaseMapper 
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
     * 删除财务管理-应付款-采购货款
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 结果
     */
    public int deleteFmsPayablePurchaseById(Long id);

    /**
     * 批量删除财务管理-应付款-采购货款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsPayablePurchaseByIds(Long[] ids);
}
