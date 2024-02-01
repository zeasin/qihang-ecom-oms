package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ErpOrderReturnedMapper;
import com.qihang.erp.api.domain.ErpOrderReturned;
import com.qihang.erp.api.service.IErpOrderReturnedService;

/**
 * 退换货Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Service
public class ErpOrderReturnedServiceImpl implements IErpOrderReturnedService 
{
    @Autowired
    private ErpOrderReturnedMapper erpOrderReturnedMapper;

    /**
     * 查询退换货
     * 
     * @param id 退换货主键
     * @return 退换货
     */
    @Override
    public ErpOrderReturned selectErpOrderReturnedById(Long id)
    {
        return erpOrderReturnedMapper.selectErpOrderReturnedById(id);
    }

    /**
     * 查询退换货列表
     * 
     * @param erpOrderReturned 退换货
     * @return 退换货
     */
    @Override
    public List<ErpOrderReturned> selectErpOrderReturnedList(ErpOrderReturned erpOrderReturned)
    {
        return erpOrderReturnedMapper.selectErpOrderReturnedList(erpOrderReturned);
    }

    /**
     * 新增退换货
     * 
     * @param erpOrderReturned 退换货
     * @return 结果
     */
    @Override
    public int insertErpOrderReturned(ErpOrderReturned erpOrderReturned)
    {
        erpOrderReturned.setCreateTime(DateUtils.getNowDate());
        return erpOrderReturnedMapper.insertErpOrderReturned(erpOrderReturned);
    }

    /**
     * 修改退换货
     * 
     * @param erpOrderReturned 退换货
     * @return 结果
     */
    @Override
    public int updateErpOrderReturned(ErpOrderReturned erpOrderReturned)
    {
        erpOrderReturned.setUpdateTime(DateUtils.getNowDate());
        return erpOrderReturnedMapper.updateErpOrderReturned(erpOrderReturned);
    }

    /**
     * 批量删除退换货
     * 
     * @param ids 需要删除的退换货主键
     * @return 结果
     */
    @Override
    public int deleteErpOrderReturnedByIds(Long[] ids)
    {
        return erpOrderReturnedMapper.deleteErpOrderReturnedByIds(ids);
    }

    /**
     * 删除退换货信息
     * 
     * @param id 退换货主键
     * @return 结果
     */
    @Override
    public int deleteErpOrderReturnedById(Long id)
    {
        return erpOrderReturnedMapper.deleteErpOrderReturnedById(id);
    }
}
