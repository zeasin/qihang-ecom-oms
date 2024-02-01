package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ShopSettingMapper;
import com.qihang.erp.api.domain.ShopSetting;
import com.qihang.erp.api.service.IShopSettingService;

/**
 * 第三方平台设置Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-18
 */
@Service
public class ShopSettingServiceImpl implements IShopSettingService 
{
    @Autowired
    private ShopSettingMapper shopSettingMapper;

    /**
     * 查询第三方平台设置
     * 
     * @param id 第三方平台设置主键
     * @return 第三方平台设置
     */
    @Override
    public ShopSetting selectShopSettingById(Long id)
    {
        return shopSettingMapper.selectShopSettingById(id);
    }

    /**
     * 查询第三方平台设置列表
     * 
     * @param shopSetting 第三方平台设置
     * @return 第三方平台设置
     */
    @Override
    public List<ShopSetting> selectShopSettingList(ShopSetting shopSetting)
    {
        return shopSettingMapper.selectShopSettingList(shopSetting);
    }

    /**
     * 新增第三方平台设置
     * 
     * @param shopSetting 第三方平台设置
     * @return 结果
     */
    @Override
    public int insertShopSetting(ShopSetting shopSetting)
    {
        return shopSettingMapper.insertShopSetting(shopSetting);
    }

    /**
     * 修改第三方平台设置
     * 
     * @param shopSetting 第三方平台设置
     * @return 结果
     */
    @Override
    public int updateShopSetting(ShopSetting shopSetting)
    {
        return shopSettingMapper.updateShopSetting(shopSetting);
    }

    /**
     * 批量删除第三方平台设置
     * 
     * @param ids 需要删除的第三方平台设置主键
     * @return 结果
     */
    @Override
    public int deleteShopSettingByIds(Long[] ids)
    {
        return shopSettingMapper.deleteShopSettingByIds(ids);
    }

    /**
     * 删除第三方平台设置信息
     * 
     * @param id 第三方平台设置主键
     * @return 结果
     */
    @Override
    public int deleteShopSettingById(Long id)
    {
        return shopSettingMapper.deleteShopSettingById(id);
    }
}
