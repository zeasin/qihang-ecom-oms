package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.mapper.ShopGoodsMapper;
import cn.qihangerp.mapper.ShopGoodsSkuMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.bo.BindErpGoodsSkuBo;
import cn.qihangerp.model.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.model.entity.ShopGoods;
import cn.qihangerp.model.entity.ShopGoodsSku;
import cn.qihangerp.model.query.ShopGoodsSkuQuery;
import cn.qihangerp.service.ShopGoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【oms_shop_goods_sku(其他渠道店铺商品SKU)】的数据库操作Service实现
* @createDate 2025-07-15 08:29:21
*/
@Slf4j
@AllArgsConstructor
@Service
public class ShopGoodsSkuServiceImpl extends ServiceImpl<ShopGoodsSkuMapper, ShopGoodsSku>
    implements ShopGoodsSkuService {
    private final ShopGoodsMapper shopGoodsMapper;
    private final OGoodsSkuMapper oGoodsSkuMapper;

    @Override
    public PageResult<ShopGoodsSku> queryPageList(ShopGoodsSkuQuery bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShopGoodsSku> queryWrapper = new LambdaQueryWrapper<ShopGoodsSku>()
                .eq(StringUtils.hasText(bo.getProductId()),ShopGoodsSku::getProductId,bo.getProductId())
                .eq(StringUtils.hasText(bo.getOuterProductId()),ShopGoodsSku::getOuterProductId,bo.getOuterProductId())
                .eq(StringUtils.hasText(bo.getOuterSkuId()),ShopGoodsSku::getOuterSkuId,bo.getOuterSkuId())
                .eq(StringUtils.hasText(bo.getSkuId()),ShopGoodsSku::getSkuId,bo.getSkuId())
                .eq(StringUtils.hasText(bo.getSkuCode()),ShopGoodsSku::getSkuCode,bo.getSkuCode())
                .eq(bo.getShopId()!=null,ShopGoodsSku::getShopId,bo.getShopId())
                .eq(bo.getShopType()!=null,ShopGoodsSku::getShopType,bo.getShopType())
                .eq(bo.getMerchantId()!=null,ShopGoodsSku::getMerchantId,bo.getMerchantId())
                .eq(bo.getGoodsId()!=null,ShopGoodsSku::getShopGoodsId,bo.getGoodsId())
                .eq(bo.getHasLink()!=null&&bo.getHasLink()==0,ShopGoodsSku::getErpGoodsSkuId,0)
                .gt(bo.getHasLink()!=null&&bo.getHasLink()==1,ShopGoodsSku::getErpGoodsSkuId,0)
                ;

        Page<ShopGoodsSku> goodsPage = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(goodsPage);
    }



    @Override
    public List<ShopGoodsSku> querySkuList(Long shopGoodsId) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>().eq(ShopGoodsSku::getShopGoodsId,shopGoodsId));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo) {
        ShopGoodsSku shopGoodsSku = this.baseMapper.selectById(bo.getId());
        if(shopGoodsSku == null) {
            return ResultVo.error("店铺商品sku数据不存在");
        }
        ShopGoods shopGoods = shopGoodsMapper.selectById(shopGoodsSku.getShopGoodsId());
        if(shopGoods==null){
            return ResultVo.error("店铺商品数据不存在");
        }


        if(StringUtils.isEmpty(bo.getErpGoodsSkuId())||bo.getErpGoodsSkuId().equals("0")){
            // 取消原来的关联
            ShopGoodsSku sku = new ShopGoodsSku();
            sku.setId(Long.parseLong(bo.getId()));
            sku.setErpGoodsId(0l);
            sku.setErpGoodsSkuId(0l);
            this.baseMapper.updateById(sku);

            ShopGoods goodsUp=new ShopGoods();
            goodsUp.setId(shopGoods.getId());
            goodsUp.setErpGoodsId(0L);
            shopGoodsMapper.updateById(goodsUp);
            // 删除原来的绑定关系
//            shopGoodsSkuMappingService.deleteByPlatformSkuId(shopGoodsSku.getSkuId().toString(),shopGoodsSku.getShopId());

            return ResultVo.success();
        }

        OGoodsSku oGoodsSku = oGoodsSkuMapper.selectById(bo.getErpGoodsSkuId());
        if(oGoodsSku == null) return ResultVo.error("未找到系统商品sku");

        Long erpGoodsId =Long.parseLong(oGoodsSku.getGoodsId());
        Long erpGoodsSkuId = Long.parseLong(oGoodsSku.getId());

        ShopGoodsSku sku = new ShopGoodsSku();
        sku.setId(Long.parseLong(bo.getId()));
        sku.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        sku.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        this.baseMapper.updateById(sku);

        ShopGoods goodsUp=new ShopGoods();
        goodsUp.setId(shopGoods.getId());
        goodsUp.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        shopGoodsMapper.updateById(goodsUp);

        // 添加绑定关联
//        ShopGoodsSkuMapping shopGoodsSkuMapping = shopGoodsSkuMappingService.selectByPlatformSkuId(shopGoodsSku.getSkuId().toString(),shopGoodsSku.getShopId());
//        if (shopGoodsSkuMapping != null) {
//            // 更新
//            shopGoodsSkuMapping.setErpGoodsSkuId(erpGoodsSkuId);
//            shopGoodsSkuMapping.setErpGoodsId(erpGoodsId);
//            shopGoodsSkuMapping.setPlatformProductId(shopGoodsSku.getProductId());
//            shopGoodsSkuMapping.setPlatformSkuId(shopGoodsSku.getSkuId());
//            shopGoodsSkuMapping.setShopGoodsId(shopGoods.getId());
//            shopGoodsSkuMapping.setShopGoodsSkuId(shopGoodsSku.getId());
//            shopGoodsSkuMapping.setModifyOn(new Date());
//            shopGoodsSkuMappingService.updateById(shopGoodsSkuMapping);
//        } else {
//            // 新增
//            shopGoodsSkuMapping = new ShopGoodsSkuMapping();
//            shopGoodsSkuMapping.setErpGoodsId(erpGoodsId);
//            shopGoodsSkuMapping.setErpGoodsSkuId(erpGoodsSkuId);
//            shopGoodsSkuMapping.setCreateOn(new Date());
//            shopGoodsSkuMapping.setShopId(shopGoodsSku.getShopId());
//            shopGoodsSkuMapping.setShopType(shopGoodsSku.getShopType());
//            shopGoodsSkuMapping.setPlatformProductId(shopGoodsSku.getProductId());
//            shopGoodsSkuMapping.setPlatformSkuId(shopGoodsSku.getSkuId());
//            shopGoodsSkuMapping.setShopGoodsId(shopGoods.getId());
//            shopGoodsSkuMapping.setShopGoodsSkuId(shopGoodsSku.getId());
//            shopGoodsSkuMappingService.save(shopGoodsSkuMapping);
//        }

        return ResultVo.success();

    }

    @Override
    public ShopGoodsSku selectByPlatformSkuId(String platformSkuId,Long shopId) {
        ShopGoodsSku shopGoodsSkuMapping = this.baseMapper.selectOne(new LambdaQueryWrapper<ShopGoodsSku>()
                .eq(ShopGoodsSku::getSkuId, platformSkuId)
                .eq(ShopGoodsSku::getShopId, shopId));

        return shopGoodsSkuMapping;
    }

}




