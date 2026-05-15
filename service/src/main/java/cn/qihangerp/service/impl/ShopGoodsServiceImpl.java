package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.mapper.ShopGoodsMapper;
import cn.qihangerp.mapper.ShopGoodsSkuMapper;
import cn.qihangerp.mapper.goods.OGoodsMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.entity.*;
import cn.qihangerp.model.request.*;
import cn.qihangerp.service.OGoodsSkuService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.service.ShopGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【oms_shop_goods(其他渠道店铺商品)】的数据库操作Service实现
* @createDate 2025-07-15 08:29:21
*/
@Slf4j
@AllArgsConstructor
@Service
public class ShopGoodsServiceImpl extends ServiceImpl<ShopGoodsMapper, ShopGoods>
    implements ShopGoodsService {
    private final OGoodsSkuMapper goodsSkuMapper;
    private final OGoodsMapper goodsMapper;
    private final OGoodsSkuService goodsSkuService;
    private final ShopGoodsSkuMapper shopGoodsSkuMapper;
//    private final ShopGoodsSkuMappingService shopGoodsSkuMappingService;
    private final OShopService shopService;

    @Override
    public PageResult<ShopGoods> queryPageList(ShopGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShopGoods> queryWrapper = new LambdaQueryWrapper<ShopGoods>()
//                .eq(ShopGoods::getDeliverMethod,0)
                .eq(bo.getId()!=null,ShopGoods::getId,bo.getId())
                .eq(StringUtils.hasText(bo.getProductId()),ShopGoods::getProductId,bo.getProductId())
                .eq(StringUtils.hasText(bo.getSpuCode()),ShopGoods::getSpuCode,bo.getSpuCode())
                .eq(StringUtils.hasText(bo.getOuterProductId()),ShopGoods::getOuterProductId,bo.getOuterProductId())
                .like(StringUtils.hasText(bo.getTitle()),ShopGoods::getTitle,bo.getTitle())
                .eq(bo.getShopId()!=null,ShopGoods::getShopId,bo.getShopId())
                .eq(bo.getShopType()!=null,ShopGoods::getShopType,bo.getShopType())
                .eq(bo.getMerchantId()!=null,ShopGoods::getMerchantId,bo.getMerchantId())
                ;

        Page<ShopGoods> goodsPage = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        if(goodsPage.getTotal()>0){
            for(ShopGoods goods : goodsPage.getRecords()){
                goods.setSkuList(shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>().eq(ShopGoodsSku::getShopGoodsId,goods.getId())));
            }
        }
        return PageResult.build(goodsPage);
    }

    @Override
    public PageResult<ShopGoods> queryVirtualAndNoSkusPageList(ShopGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ShopGoods> queryWrapper = new LambdaQueryWrapper<ShopGoods>()
                .eq(ShopGoods::getDeliverMethod,1)
                .like(StringUtils.hasText(bo.getTitle()),ShopGoods::getTitle,bo.getTitle())
                .eq(StringUtils.hasText(bo.getProductId()),ShopGoods::getProductId,bo.getProductId())
                .eq(bo.getShopId()!=null,ShopGoods::getShopId,bo.getShopId())
                .eq(bo.getShopType()!=null,ShopGoods::getShopType,bo.getShopType())
                .eq(bo.getMerchantId()!=null,ShopGoods::getMerchantId,bo.getMerchantId())
                .eq(bo.getBindShipSku()!=null,ShopGoods::getBindShipSku,bo.getBindShipSku())
                ;
        pageQuery.setIsAsc("desc");
        pageQuery.setOrderByColumn("bind_ship_sku,id");
        Page<ShopGoods> goodsPage = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Integer> saveOrUpdateGoods(Long shopId,Long merchantId,Integer shopType, ShopGoods goods) {
//        OShop shop = shopService.getById(shopId);
//        if(shop==null) return ResultVo.error("店铺不存在");
//        goods.setMerchantId(shop.getMerchantId());
        Long shopGoodsId = 0L;
        goods.setMerchantId(merchantId);
        goods.setShopId(shopId);
        goods.setShopType(shopType);

        // 查询是否存在
        List<ShopGoods> shopGoods = this.baseMapper.selectList(new LambdaQueryWrapper<ShopGoods>()
                .eq(ShopGoods::getShopId, shopId)
                        .eq(ShopGoods::getShopType, shopType)
                .eq(ShopGoods::getMerchantId, merchantId)
                .eq(ShopGoods::getProductId, goods.getProductId())
        );
        if(shopGoods==null||shopGoods.size()==0){
            // 新增商品
            goods.setCreateOn(new Date());
            this.baseMapper.insert(goods);
            shopGoodsId = goods.getId();
            log.info("==========新增shopGoods==========");
        }else{
            // 修改商品
            goods.setId(shopGoods.get(0).getId());
            goods.setUpdateOn(new Date());
            this.baseMapper.updateById(goods);
            shopGoodsId =shopGoods.get(0).getId();
            log.info("==========修改shopGoods==========");
        }
        Long erpGoodsId = 0L;

        //处理skus
        if(goods.getSkuList()!=null&&goods.getSkuList().size()>0){
            for (ShopGoodsSku goodsSku : goods.getSkuList()) {
                goodsSku.setMerchantId(merchantId);
                goodsSku.setShopId(shopId);
                goodsSku.setShopType(shopType);
                Long erpGoodsSkuId = 0L;
                Long shopGoodsSkuId = 0L;
                //绑定skuId
                if(StringUtils.hasText(goodsSku.getOuterSkuId())){
                    OGoodsSku goodsSkuByCode = goodsSkuService.getGoodsSkuByCode(goodsSku.getOuterSkuId());
                    if(goodsSkuByCode!=null){
                        erpGoodsId = Long.parseLong(goodsSkuByCode.getGoodsId());
                        erpGoodsSkuId = Long.parseLong(goodsSkuByCode.getId());
//                        goodsSku.setErpGoodsId(Long.parseLong(goodsSkuByCode.getGoodsId()));
//                        goodsSku.setErpGoodsSkuId(Long.parseLong(goodsSkuByCode.getId()));
                    }
                }
                goodsSku.setErpGoodsId(erpGoodsId);
                goodsSku.setErpGoodsSkuId(erpGoodsSkuId);
                // 查询是否存在
                List<ShopGoodsSku> shopGoodsSkus = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>()
                        .eq(ShopGoodsSku::getShopGoodsId, goods.getId())
                        .eq(ShopGoodsSku::getSkuId, goodsSku.getSkuId())
                );
                if(shopGoodsSkus==null||shopGoodsSkus.size()==0){
                    //新增
                    goodsSku.setShopGoodsId(goods.getId());
                    goodsSku.setCreateOn(new Date());
                    shopGoodsSkuMapper.insert(goodsSku);
                    shopGoodsSkuId = goodsSku.getId();
                    log.info("==========新增shopGoodsSku==========");
                }else{
                    // update
                    if(shopGoodsSkus.get(0).getErpGoodsSkuId()!=null&&shopGoodsSkus.get(0).getErpGoodsSkuId()>0){
                        goodsSku.setErpGoodsId(null);
                        goodsSku.setErpGoodsSkuId(null);
                    }
                    goodsSku.setId(shopGoodsSkus.get(0).getId());
                    goodsSku.setUpdateOn(new Date());
                    shopGoodsSkuMapper.updateById(goodsSku);
                    shopGoodsSkuId = shopGoodsSkus.get(0).getId();
                    log.info("==========修改shopGoodsSku==========");
                }
//                if(erpGoodsSkuId.longValue()>0){
//                    // 添加绑定关联
//                    ShopGoodsSkuMapping shopGoodsSkuMapping = shopGoodsSkuMappingService.selectByPlatformSkuId(goodsSku.getSkuId(),goodsSku.getShopId());
//                    if (shopGoodsSkuMapping != null) {
//                        // 更新
////                        shopGoodsSkuMapping.setErpGoodsSkuId(erpGoodsSkuId);
////                        shopGoodsSkuMapping.setErpGoodsId(erpGoodsId);
////                        shopGoodsSkuMapping.setPlatformProductId(goodsSku.getProductId());
////                        shopGoodsSkuMapping.setPlatformSkuId(goodsSku.getSkuId());
////                        shopGoodsSkuMapping.setShopGoodsId(shopGoodsId);
////                        shopGoodsSkuMapping.setShopGoodsSkuId(shopGoodsSkuId);
////                        shopGoodsSkuMapping.setModifyOn(new Date());
////                        shopGoodsSkuMappingService.updateById(shopGoodsSkuMapping);
//                    } else {
//                        // 新增
//                        shopGoodsSkuMapping = new ShopGoodsSkuMapping();
//                        shopGoodsSkuMapping.setErpGoodsId(erpGoodsId);
//                        shopGoodsSkuMapping.setErpGoodsSkuId(erpGoodsSkuId);
//                        shopGoodsSkuMapping.setCreateOn(new Date());
//                        shopGoodsSkuMapping.setShopId(goodsSku.getShopId());
//                        shopGoodsSkuMapping.setShopType(goodsSku.getShopType());
//                        shopGoodsSkuMapping.setPlatformProductId(goodsSku.getProductId());
//                        shopGoodsSkuMapping.setPlatformSkuId(goodsSku.getSkuId());
//                        shopGoodsSkuMapping.setShopGoodsId(shopGoodsId);
//                        shopGoodsSkuMapping.setShopGoodsSkuId(shopGoodsSkuId);
//                        shopGoodsSkuMappingService.save(shopGoodsSkuMapping);
//                    }
//                }
            }
        }
//        else{
//            if(shopType== EnumShopType.TANG_LANG.getIndex()){
//                // 螳螂系统特殊处理
//                ShopGoodsSku sku = new ShopGoodsSku();
//            }
//        }
        if(erpGoodsId>0){
            ShopGoods updateGoods = new ShopGoods();
            updateGoods.setId(goods.getId());
            updateGoods.setErpGoodsId(erpGoodsId);
            this.baseMapper.updateById(updateGoods);
        }
//        log.info("=============添加店铺商品成功=============");
        return ResultVo.success();
    }

    /**
     * 手动添加店铺商品
     * @param goodsAddRequest
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> addGoods(ShopGoodsAddRequest goodsAddRequest, OShop shop) {
        if(goodsAddRequest.getSkuList()==null||goodsAddRequest.getSkuList().isEmpty()) return ResultVo.error("没有商品规格信息");
//        OShop shop = shopService.getById(goodsAddRequest.getShopId());
//        if(shop==null) return ResultVo.error("店铺不存在");

        if(StringUtils.hasText(goodsAddRequest.getProductId())&&!goodsAddRequest.getProductId().equals("0")){
            // 有值，判断是否存在
            List<ShopGoods> shopGoods = this.baseMapper.selectList(new LambdaQueryWrapper<ShopGoods>().eq(ShopGoods::getProductId, goodsAddRequest.getProductId()));
            if(!shopGoods.isEmpty()) return ResultVo.error("商品平台ID已存在");
        }
        Long c = System.currentTimeMillis()/1000;
        String erpGoodsId="0";
        // 组合sku
        List<ShopGoodsSku> skuList = new ArrayList<>();
        int total=0;
        for (ShopGoodsAddRequest.sku sku:goodsAddRequest.getSkuList()){
            ShopGoodsSku s = new ShopGoodsSku();
            s.setProductId(goodsAddRequest.getProductId());
            s.setProductTitle(goodsAddRequest.getGoodsName());
            s.setSkuId(sku.getSkuId());
            s.setOuterProductId(goodsAddRequest.getOuterProductId());
            s.setOuterSkuId(sku.getOuterSkuId());
            s.setImg(StringUtils.hasText(sku.getImg())?sku.getImg(): goodsAddRequest.getGoodsImg());
            s.setPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(sku.getPrice())).intValue());
            s.setStockNum(sku.getStockNum());
            s.setSkuCode(sku.getSkuCode());
            s.setSkuName(sku.getSkuName());
            s.setStatus(1);
            s.setAddTime(c);
            s.setStock(sku.getStockNum());
            s.setCreateOn(new Date());
            s.setBindShipSku(0);
            // 查询skucode
            List<OGoodsSku> oGoodsSkus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OGoodsSku>().eq(OGoodsSku::getSkuCode, sku.getSkuCode()));
            if(!oGoodsSkus.isEmpty()){
                erpGoodsId = oGoodsSkus.get(0).getGoodsId();
                s.setErpGoodsSkuId(Long.parseLong(oGoodsSkus.get(0).getId()));
            }
            s.setErpGoodsId(Long.parseLong(erpGoodsId));
            skuList.add(s);
            total+= s.getStockNum();
        }

        ShopGoods goods = new ShopGoods();
        goods.setShopId(shop.getId());
        goods.setShopType(shop.getType());
        goods.setMerchantId(shop.getMerchantId());
        goods.setProductId(goodsAddRequest.getProductId());
        goods.setOuterProductId(goodsAddRequest.getOuterProductId());
        goods.setTitle(goodsAddRequest.getGoodsName());
        goods.setImg(goodsAddRequest.getGoodsImg());
        goods.setStatus(1);
        goods.setMinPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(goodsAddRequest.getMinPrice())).intValue());
        goods.setSpuCode(goodsAddRequest.getGoodsNum());
        goods.setQuantity(total);
        goods.setErpGoodsId(Long.parseLong(erpGoodsId));
        goods.setAddTime(c);
        goods.setCreateOn(new Date());
        goods.setDeliverMethod(0);
        goods.setBindShipSku(0);
        this.baseMapper.insert(goods);
        //插入sku
        for(var sku:skuList){
            sku.setShopGoodsId(goods.getId());
            sku.setShopId(goods.getShopId());
            sku.setShopType(goods.getShopType());
            sku.setMerchantId(goods.getMerchantId());
            shopGoodsSkuMapper.insert(sku);
        }
        return ResultVo.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> addGoodsSku(ShopGoodsSkuAddRequest request) {
        OGoodsSku oGoodsSku = goodsSkuMapper.selectById(request.getGoodsSkuId());
        if(oGoodsSku==null){
            return ResultVo.error("没有找到商品库sku");
        }
        OShop shop = shopService.getById(request.getShopId());
        if(shop==null) return ResultVo.error("店铺不存在");

        Long shopGoodsId = null;
        Long shopGoodsSkuId = null;

            // 查询商品是否存在
            List<ShopGoods> shopGoods = this.baseMapper.selectList(new LambdaQueryWrapper<ShopGoods>().eq(ShopGoods::getShopId, request.getShopId()).eq(ShopGoods::getErpGoodsId, oGoodsSku.getGoodsId()));
            if (shopGoods.isEmpty()) {
                // 不存在，增加一条商品信息
                ShopGoods goods = new ShopGoods();
                goods.setShopId(shop.getId());
                goods.setShopType(shop.getType());
                goods.setMerchantId(shop.getMerchantId());
                goods.setProductId("");
                goods.setOuterProductId(oGoodsSku.getGoodsNum());
                goods.setTitle(oGoodsSku.getGoodsName());
                goods.setImg(oGoodsSku.getColorImage());
                goods.setStatus(1);
                goods.setMinPrice(BigDecimal.valueOf(100).multiply(oGoodsSku.getRetailPrice()).intValue());
                goods.setSpuCode(oGoodsSku.getGoodsNum());
                goods.setQuantity(0);
                goods.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
                goods.setAddTime(System.currentTimeMillis() / 100);
                goods.setCreateOn(new Date());
                goods.setDeliverMethod(0);
                goods.setBindShipSku(0);
                this.baseMapper.insert(goods);
                shopGoodsId = goods.getId();
            } else {
                // 存在，不动
                shopGoodsId = shopGoods.get(0).getId();
            }

        // 查询商品sku是否存在
        List<ShopGoodsSku> shopGoodsSkus = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>()
                .eq(ShopGoodsSku::getErpGoodsSkuId, request.getGoodsSkuId())
                .eq(StringUtils.hasText(request.getSkuId()), ShopGoodsSku::getSkuId, request.getSkuId())
                .eq(ShopGoodsSku::getShopId, request.getShopId())
        );
        if(shopGoodsSkus.isEmpty()){
            // 不存在 添加
            ShopGoodsSku s = new ShopGoodsSku();
            s.setShopGoodsId(shopGoodsId);
            s.setShopId(shop.getId());
            s.setShopType(shop.getType());
            s.setShopName(shop.getName());
            s.setMerchantId(shop.getMerchantId());
            s.setProductId("");
            s.setProductTitle(oGoodsSku.getGoodsName());
            s.setSkuId(request.getSkuId());
            s.setOuterProductId(oGoodsSku.getGoodsNum());
            s.setOuterSkuId(oGoodsSku.getSkuCode());
            s.setImg( oGoodsSku.getColorImage());
            s.setPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(request.getPrice())).intValue());
            s.setStockNum(0);
            s.setSkuCode(oGoodsSku.getSkuCode());
            s.setSkuName(oGoodsSku.getSkuName());
            s.setStatus(1);
            s.setAddTime(System.currentTimeMillis()/100);
            s.setStock(0);
            s.setCreateOn(new Date());
            s.setBindShipSku(0);
            s.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
            s.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
            shopGoodsSkuMapper.insert(s);
            shopGoodsSkuId = s.getId();
        }else{
            // 存在就修改
            shopGoodsSkuId = shopGoodsSkus.get(0).getId();
            ShopGoodsSku s = new ShopGoodsSku();
            s.setId(shopGoodsSkuId);
            s.setSkuId(request.getSkuId());
            s.setPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(request.getPrice())).intValue());
            s.setUpdateOn(new Date());
            s.setModifyTime(System.currentTimeMillis()/100);
            shopGoodsSkuMapper.updateById(s);
        }

        return ResultVo.success(shopGoodsSkuId);
    }

    @Override
    public ResultVo<Long> insertGoodsSku(ShopGoodsSkuInsertRequest request) {
        ShopGoods shopGoods = this.baseMapper.selectById(request.getShopGoodsId());
        if(shopGoods == null) return ResultVo.error("店铺商品不存在");

        OGoodsSku oGoodsSku = goodsSkuMapper.selectById(request.getErpGoodsSkuId());
        if(oGoodsSku==null){
            return ResultVo.error("没有找到商品库sku");
        }
        List<ShopGoodsSku> shopGoodsSkuList = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>().eq(ShopGoodsSku::getSkuId, request.getSkuId()));
        if(!shopGoodsSkuList.isEmpty()){
            return ResultVo.error("平台SkuId已经存在");
        }
        // 不存在 添加
        ShopGoodsSku s = new ShopGoodsSku();
        s.setShopGoodsId(request.getShopGoodsId());
        s.setShopId(shopGoods.getShopId());
        s.setShopType(shopGoods.getShopType());
        s.setShopName("");
        s.setMerchantId(shopGoods.getMerchantId());
        s.setProductId(shopGoods.getProductId());
        s.setProductTitle(oGoodsSku.getGoodsName());
        s.setSkuId(request.getSkuId());
        s.setOuterProductId(oGoodsSku.getGoodsNum());
        s.setOuterSkuId(oGoodsSku.getSkuCode());
        s.setImg(oGoodsSku.getColorImage());
        s.setPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(request.getPrice())).intValue());
        s.setStockNum(0);
        s.setSkuCode(oGoodsSku.getSkuCode());
        s.setSkuName(oGoodsSku.getSkuName());
        s.setStatus(1);
        s.setAddTime(System.currentTimeMillis()/100);
        s.setStock(0);
        s.setCreateOn(new Date());
        s.setBindShipSku(0);
        s.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        s.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        shopGoodsSkuMapper.insert(s);
        return ResultVo.success();
    }

    @Override
    public ResultVo<Long> updateGoodsSku(ShopGoodsSkuUpdateRequest request) {
        ShopGoodsSku shopGoodsSku = shopGoodsSkuMapper.selectById(request.getId());
        if(shopGoodsSku==null) return ResultVo.error("店铺商品Sku不存在");

        OGoodsSku oGoodsSku = goodsSkuMapper.selectById(request.getErpGoodsSkuId());
        if(oGoodsSku==null){
            return ResultVo.error("没有找到商品库sku");
        }

        if(!request.getSkuId().equals(shopGoodsSku.getSkuId())){
            List<ShopGoodsSku> shopGoodsSkuList = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>().eq(ShopGoodsSku::getSkuId, request.getSkuId()));
            if(!shopGoodsSkuList.isEmpty()){
                return ResultVo.error("平台SkuId已经存在");
            }
        }

        ShopGoodsSku s = new ShopGoodsSku();
        s.setId(request.getId());
        s.setProductTitle(request.getProductTitle());
        s.setSkuId(request.getSkuId());
        s.setOuterProductId(oGoodsSku.getGoodsNum());
        s.setOuterSkuId(oGoodsSku.getSkuCode());
        s.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        s.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        s.setImg(request.getImg());
        s.setPrice(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(request.getPrice())).intValue());
        s.setSkuCode(oGoodsSku.getSkuCode());
        s.setSkuName(request.getSkuName());
        s.setUpdateOn(new Date());
        s.setModifyTime(System.currentTimeMillis()/100);
        shopGoodsSkuMapper.updateById(s);

        return ResultVo.success();
    }

    @Override
    public ResultVo<Long> updateGoodsStock(ShopGoodsSkuStockUpdateRequest request) {
        ShopGoodsSku sku=new ShopGoodsSku();
        sku.setId(request.getId());
        sku.setStock(request.getStockNum());
        sku.setStockNum(request.getStockNum());
        sku.setUpdateOn(new Date());
        shopGoodsSkuMapper.updateById(sku);
        return ResultVo.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> savePddGoods(ShopGoods shopGoods, Long shopId) {
        if(shopGoods==null) return ResultVo.error("商品数据不能为空");
        if(shopGoods.getId()==null&&StringUtils.isEmpty(shopGoods.getProductId())) return ResultVo.error("商品ID或者平台商品ID不能为空");
//        if(shopGoods.getSkuList()==null||shopGoods.getSkuList().isEmpty()) return ResultVo.error("没有商品规格信息");

        OShop shop = shopService.getById(shopId);
        if(shop==null) return ResultVo.error("店铺不存在");

        ShopGoods goods = null;
        if(shopGoods.getId()!=null){
            goods = this.baseMapper.selectById(shopGoods.getId());
        }else {
            List<ShopGoods> shopGoodsTmp = this.baseMapper.selectList(
                    new LambdaQueryWrapper<ShopGoods>()
                            .eq(ShopGoods::getShopId, shopId)
                            .eq(ShopGoods::getProductId, shopGoods.getProductId()));
            if (!shopGoodsTmp.isEmpty()) {
                goods = shopGoodsTmp.get(0);
            }
        }
        Long erpGoodsId = 0L;
        // 查询商品外部编码
        if(StringUtils.hasText(shopGoods.getOuterProductId())) {
            List<OGoods> oGoods = goodsMapper.selectList(new LambdaQueryWrapper<OGoods>().eq(OGoods::getGoodsNum, shopGoods.getOuterProductId()));
            if (!oGoods.isEmpty()) {
                erpGoodsId = Long.parseLong(oGoods.get(0).getId());
            }
        }
        Long shopGoodsId = null;
        if(goods==null){
            // 新增
            // 不存在，增加一条商品信息
            ShopGoods goodsNew = new ShopGoods();
            goodsNew.setShopId(shop.getId());
            goodsNew.setShopType(shop.getType());
            goodsNew.setMerchantId(shop.getMerchantId());
            goodsNew.setProductId(shopGoods.getProductId());
            goodsNew.setOuterProductId(shopGoods.getOuterProductId());
            goodsNew.setTitle(shopGoods.getTitle());
            goodsNew.setImg(shopGoods.getImg());
            goodsNew.setStatus(shopGoods.getStatus());
            goodsNew.setMinPrice(shopGoods.getMinPrice());
            goodsNew.setMarketPrice(shopGoods.getMarketPrice());
            goodsNew.setSpuCode(shopGoods.getOuterProductId());
            goodsNew.setQuantity(shopGoods.getQuantity());
            goodsNew.setErpGoodsId(erpGoodsId);
            goodsNew.setAddTime(shopGoods.getAddTime());
            goodsNew.setCreateOn(new Date());
            goodsNew.setDeliverMethod(0);
            goodsNew.setBindShipSku(0);
            this.baseMapper.insert(goodsNew);
            shopGoodsId = goodsNew.getId();
        }else{
            // 修改
            shopGoodsId = goods.getId();
            shopGoods.setId(goods.getId());
            shopGoods.setUpdateOn(new Date());
            this.baseMapper.updateById(shopGoods);
        }

        // 添加sku
        for (ShopGoodsSku goodsSku : shopGoods.getSkuList()) {
            goodsSku.setMerchantId(shop.getMerchantId());
            goodsSku.setShopId(shopId);
            goodsSku.setShopType(shop.getType());
            Long erpGoodsSkuId = 0L;
            Long shopGoodsSkuId = 0L;
            //绑定skuId
            if(StringUtils.hasText(goodsSku.getOuterSkuId())){
                OGoodsSku goodsSkuByCode = goodsSkuService.getGoodsSkuByCode(goodsSku.getOuterSkuId());
                if(goodsSkuByCode!=null){
                    erpGoodsId = Long.parseLong(goodsSkuByCode.getGoodsId());
                    erpGoodsSkuId = Long.parseLong(goodsSkuByCode.getId());
                }
            }
            goodsSku.setErpGoodsId(erpGoodsId);
            goodsSku.setErpGoodsSkuId(erpGoodsSkuId);
            // 查询是否存在
            List<ShopGoodsSku> shopGoodsSkus = shopGoodsSkuMapper.selectList(new LambdaQueryWrapper<ShopGoodsSku>()
                    .eq(ShopGoodsSku::getShopGoodsId, shopGoodsId)
                    .eq(ShopGoodsSku::getSkuId, goodsSku.getSkuId())
            );
            if(shopGoodsSkus==null||shopGoodsSkus.size()==0){
                //新增
                goodsSku.setShopGoodsId(shopGoodsId);
                goodsSku.setCreateOn(new Date());
                shopGoodsSkuMapper.insert(goodsSku);
                shopGoodsSkuId = goodsSku.getId();
                log.info("==========新增shopGoodsSku==========");
            }else{
                // update
                if(shopGoodsSkus.get(0).getErpGoodsSkuId()!=null&&shopGoodsSkus.get(0).getErpGoodsSkuId()>0){
                    goodsSku.setErpGoodsId(null);
                    goodsSku.setErpGoodsSkuId(null);
                }
                goodsSku.setId(shopGoodsSkus.get(0).getId());
                goodsSku.setUpdateOn(new Date());
                shopGoodsSkuMapper.updateById(goodsSku);
                shopGoodsSkuId = shopGoodsSkus.get(0).getId();
                log.info("==========修改shopGoodsSku==========");
            }

        }
        return ResultVo.success();
    }

    @Override
    public boolean save(ShopGoods entity) {
        return super.save(entity);
    }
}




