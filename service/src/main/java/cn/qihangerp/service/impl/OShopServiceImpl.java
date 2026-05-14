package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.mapper.OShopMapper;
import cn.qihangerp.model.entity.OShop;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.service.OShopService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【sys_shop(数据中心-店铺)】的数据库操作Service实现
* @createDate 2024-03-17 15:17:34
*/
@AllArgsConstructor
@Service
public class OShopServiceImpl extends ServiceImpl<OShopMapper, OShop>
    implements OShopService {
    private final OShopMapper mapper;

    @Override
    public PageResult<OShop> queryPageList(OShop bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OShop> queryWrapper = new LambdaQueryWrapper<OShop>()
                .like(StringUtils.hasText(bo.getName()), OShop::getName, bo.getName())
                .eq(StringUtils.hasText(bo.getSellerId()), OShop::getSellerId, bo.getSellerId())
                .eq(bo.getMerchantId() != null, OShop::getMerchantId, bo.getMerchantId())
                .eq(bo.getStatus()!=null, OShop::getStatus, bo.getStatus())
                .eq(bo.getType()!=null, OShop::getType, bo.getType())
                .eq(bo.getRegionId() != null, OShop::getRegionId, bo.getRegionId());

        Page<OShop> pages = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }
    @Override
    public List<OShop> selectShopList(OShop shop) {
        LambdaQueryWrapper<OShop> qw = new LambdaQueryWrapper<OShop>()
                .eq(shop.getType()!=null, OShop::getType,shop.getType());
        return mapper.selectList(qw);
    }

    @Override
    public OShop selectShopById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public int updateShopById(OShop shop) {
        return mapper.updateById(shop);
    }

    @Override
    public int insertShop(OShop shop) {
        shop.setCreateTime(new Date());
        return mapper.insert(shop);
    }

    @Override
    public int deleteShopByIds(Long[] ids) {
        return mapper.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public List<OShop> selectShopByShopType(EnumShopType shopType) {
        LambdaQueryWrapper<OShop> qw = new LambdaQueryWrapper<OShop>()
                .eq(OShop::getType,shopType.getIndex())
                .eq(OShop::getStatus,1);
        return mapper.selectList(qw);
    }

    @Override
    public void updateSessionKey(Long shopId, String sessionKey) {
        OShop shop = new OShop();
        shop.setId(shopId);
        shop.setAccessToken(sessionKey);
        mapper.updateById(shop);
    }
//    @Override
//    public List<SysPlatform> selectShopPlatformList() {
//        return platformMapper.selectList(new LambdaQueryWrapper<>());
//    }
//
//    @Override
//    public SysPlatform selectShopPlatformById(Long id) {
//        return platformMapper.selectById(id);
//    }
//
//    @Override
//    public int updateShopPlatformById(SysPlatform platform) {
//        return platformMapper.updateById(platform);
//    }
}




