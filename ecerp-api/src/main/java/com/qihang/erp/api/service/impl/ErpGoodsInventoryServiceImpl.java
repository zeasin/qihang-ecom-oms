package com.qihang.erp.api.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.zhijian.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.qihang.erp.api.domain.ErpGoodsInventoryDetail;
import com.qihang.erp.api.mapper.ErpGoodsInventoryMapper;
import com.qihang.erp.api.domain.ErpGoodsInventory;
import com.qihang.erp.api.service.IErpGoodsInventoryService;

/**
 * 商品库存Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-09
 */
@Service
public class ErpGoodsInventoryServiceImpl implements IErpGoodsInventoryService 
{
    @Autowired
    private ErpGoodsInventoryMapper erpGoodsInventoryMapper;

    /**
     * 查询商品库存
     * 
     * @param id 商品库存主键
     * @return 商品库存
     */
    @Override
    public ErpGoodsInventory selectErpGoodsInventoryById(Long id)
    {
        return erpGoodsInventoryMapper.selectErpGoodsInventoryById(id);
    }

    /**
     * 查询商品库存列表
     * 
     * @param erpGoodsInventory 商品库存
     * @return 商品库存
     */
    @Override
    public List<ErpGoodsInventory> selectErpGoodsInventoryList(ErpGoodsInventory erpGoodsInventory)
    {
        return erpGoodsInventoryMapper.selectErpGoodsInventoryList(erpGoodsInventory);
    }

    /**
     * 新增商品库存
     * 
     * @param erpGoodsInventory 商品库存
     * @return 结果
     */
    @Transactional
    @Override
    public int insertErpGoodsInventory(ErpGoodsInventory erpGoodsInventory)
    {
        erpGoodsInventory.setCreateTime(DateUtils.getNowDate());
        int rows = erpGoodsInventoryMapper.insertErpGoodsInventory(erpGoodsInventory);
        insertErpGoodsInventoryDetail(erpGoodsInventory);
        return rows;
    }

    /**
     * 修改商品库存
     * 
     * @param erpGoodsInventory 商品库存
     * @return 结果
     */
    @Transactional
    @Override
    public int updateErpGoodsInventory(ErpGoodsInventory erpGoodsInventory)
    {
        erpGoodsInventory.setUpdateTime(DateUtils.getNowDate());
        erpGoodsInventoryMapper.deleteErpGoodsInventoryDetailByInventoryId(erpGoodsInventory.getId());
        insertErpGoodsInventoryDetail(erpGoodsInventory);
        return erpGoodsInventoryMapper.updateErpGoodsInventory(erpGoodsInventory);
    }

    /**
     * 批量删除商品库存
     * 
     * @param ids 需要删除的商品库存主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpGoodsInventoryByIds(Long[] ids)
    {
        erpGoodsInventoryMapper.deleteErpGoodsInventoryDetailByInventoryIds(ids);
        return erpGoodsInventoryMapper.deleteErpGoodsInventoryByIds(ids);
    }

    /**
     * 删除商品库存信息
     * 
     * @param id 商品库存主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpGoodsInventoryById(Long id)
    {
        erpGoodsInventoryMapper.deleteErpGoodsInventoryDetailByInventoryId(id);
        return erpGoodsInventoryMapper.deleteErpGoodsInventoryById(id);
    }

    /**
     * 新增商品库存明细信息
     * 
     * @param erpGoodsInventory 商品库存对象
     */
    public void insertErpGoodsInventoryDetail(ErpGoodsInventory erpGoodsInventory)
    {
        List<ErpGoodsInventoryDetail> erpGoodsInventoryDetailList = erpGoodsInventory.getErpGoodsInventoryDetailList();
        Long id = erpGoodsInventory.getId();
        if (StringUtils.isNotNull(erpGoodsInventoryDetailList))
        {
            List<ErpGoodsInventoryDetail> list = new ArrayList<ErpGoodsInventoryDetail>();
            for (ErpGoodsInventoryDetail erpGoodsInventoryDetail : erpGoodsInventoryDetailList)
            {
                erpGoodsInventoryDetail.setInventoryId(id);
                list.add(erpGoodsInventoryDetail);
            }
            if (list.size() > 0)
            {
                erpGoodsInventoryMapper.batchErpGoodsInventoryDetail(list);
            }
        }
    }
}
