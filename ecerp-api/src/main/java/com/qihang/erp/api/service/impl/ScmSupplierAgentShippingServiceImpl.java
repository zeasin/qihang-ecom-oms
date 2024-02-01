package com.qihang.erp.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.qihang.erp.api.domain.FmsPayableAgentShip;
import com.qihang.erp.api.domain.ScmSupplier;
import com.qihang.erp.api.mapper.FmsPayableAgentShipMapper;
import com.qihang.erp.api.mapper.ScmSupplierMapper;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ScmSupplierAgentShippingMapper;
import com.qihang.erp.api.domain.ScmSupplierAgentShipping;
import com.qihang.erp.api.service.IScmSupplierAgentShippingService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商代发货Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-07
 */
@Service
public class ScmSupplierAgentShippingServiceImpl implements IScmSupplierAgentShippingService 
{
    @Autowired
    private ScmSupplierAgentShippingMapper scmSupplierAgentShippingMapper;
    @Autowired
    private FmsPayableAgentShipMapper fmsPayableAgentShipMapper;
    @Autowired
    private ScmSupplierMapper supplierMapper;

    /**
     * 查询供应商代发货
     * 
     * @param id 供应商代发货主键
     * @return 供应商代发货
     */
    @Override
    public ScmSupplierAgentShipping selectScmSupplierAgentShippingById(Long id)
    {
        return scmSupplierAgentShippingMapper.selectScmSupplierAgentShippingById(id);
    }

    /**
     * 查询供应商代发货列表
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 供应商代发货
     */
    @Override
    public List<ScmSupplierAgentShipping> selectScmSupplierAgentShippingList(ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        return scmSupplierAgentShippingMapper.selectScmSupplierAgentShippingList(scmSupplierAgentShipping);
    }

    /**
     * 新增供应商代发货
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 结果
     */
    @Override
    public int insertScmSupplierAgentShipping(ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        scmSupplierAgentShipping.setCreateTime(DateUtils.getNowDate());
        return scmSupplierAgentShippingMapper.insertScmSupplierAgentShipping(scmSupplierAgentShipping);
    }

    /**
     * 修改供应商代发货
     * 
     * @param scmSupplierAgentShipping 供应商代发货
     * @return 结果
     */
    @Transactional
    @Override
    public int updateScmSupplierAgentShipping(ScmSupplierAgentShipping scmSupplierAgentShipping)
    {
        ScmSupplierAgentShipping ship = scmSupplierAgentShippingMapper.selectScmSupplierAgentShippingById(scmSupplierAgentShipping.getId());
        if(ship== null) return -1;
        else if(ship.getStatus().intValue()!=0) return -2;

        ScmSupplier scmSupplier = supplierMapper.selectScmSupplierById(ship.getSupplierId());
        //更新自己
        ScmSupplierAgentShipping up = new ScmSupplierAgentShipping();
        up.setId(scmSupplierAgentShipping.getId());
        up.setGoodsPrice(scmSupplierAgentShipping.getGoodsPrice());
        up.setShipCompany(scmSupplierAgentShipping.getShipCompany());
        up.setShipNo(scmSupplierAgentShipping.getShipNo());
        up.setShipCost(scmSupplierAgentShipping.getShipCost());
        up.setShipTime(scmSupplierAgentShipping.getShipTime());
        up.setRemark(scmSupplierAgentShipping.getRemark());
        up.setUpdateBy(scmSupplierAgentShipping.getUpdateBy());
        up.setUpdateTime(DateUtils.getNowDate());
        up.setStatus(1L);
        scmSupplierAgentShippingMapper.updateScmSupplierAgentShipping(up);

        // 插入财务应付款项-代发账单
        FmsPayableAgentShip fmsPayableAgentShip = new FmsPayableAgentShip();
        fmsPayableAgentShip.setOrderNum(ship.getOrderNum());
        fmsPayableAgentShip.setShopId(ship.getShopId());
        fmsPayableAgentShip.setSupplierId(ship.getSupplierId());
        fmsPayableAgentShip.setSupplierName(scmSupplier.getName());
        fmsPayableAgentShip.setDate(new Date());
        fmsPayableAgentShip.setShipCompany(scmSupplierAgentShipping.getShipCompany());
        fmsPayableAgentShip.setShipNo(scmSupplierAgentShipping.getShipNo());
        fmsPayableAgentShip.setShipAmount(scmSupplierAgentShipping.getShipCost());
        fmsPayableAgentShip.setGoodsAmount(scmSupplierAgentShipping.getGoodsPrice().subtract(BigDecimal.valueOf(ship.getQuantity())));
        fmsPayableAgentShip.setAmount(fmsPayableAgentShip.getGoodsAmount().add(BigDecimal.valueOf(fmsPayableAgentShip.getShipAmount())));
        fmsPayableAgentShip.setStatus(0L);
        fmsPayableAgentShip.setCreateTime(new Date());
        fmsPayableAgentShip.setCreateBy(scmSupplierAgentShipping.getUpdateBy());
        fmsPayableAgentShipMapper.insertFmsPayableAgentShip(fmsPayableAgentShip);
        return 1;
    }

    /**
     * 批量删除供应商代发货
     * 
     * @param ids 需要删除的供应商代发货主键
     * @return 结果
     */
    @Override
    public int deleteScmSupplierAgentShippingByIds(Long[] ids)
    {
        return scmSupplierAgentShippingMapper.deleteScmSupplierAgentShippingByIds(ids);
    }

    /**
     * 删除供应商代发货信息
     * 
     * @param id 供应商代发货主键
     * @return 结果
     */
    @Override
    public int deleteScmSupplierAgentShippingById(Long id)
    {
        return scmSupplierAgentShippingMapper.deleteScmSupplierAgentShippingById(id);
    }
}
