//package cn.qihangerp.api.controller;
//
//import cn.qihangerp.api.wei.PullRequest;
//import cn.qihangerp.common.AjaxResult;
//import cn.qihangerp.common.ResultVo;
//import cn.qihangerp.common.ResultVoEnum;
//import cn.qihangerp.common.enums.EnumShopType;
//import cn.qihangerp.model.entity.*;
//import cn.qihangerp.open.wei.WeiGoodsApiService;
//import cn.qihangerp.open.wei.WeiTokenApiHelper;
//import cn.qihangerp.open.wei.model.Product;
//import cn.qihangerp.open.xhs.response.GoodsItemInfo;
//import cn.qihangerp.open.xhs.xhsGoodsApiService;
//import cn.qihangerp.security.common.BaseController;
//import cn.qihangerp.service.*;
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//
//@RestController
//@RequestMapping("/shop/goods")
//public class ShopGoodsApiController extends BaseController {
//
//    @Autowired
//    private OShopService shopService;
//    @Autowired
//    private ShopGoodsService shopGoodsService;
//    @Autowired
//    private OShopPlatformService platformService;
//    @Autowired
//    private TaoGoodsService taoGoodsService;
//    @Autowired
//    private PddGoodsService pddGoodsService;
//    @Autowired
//    private DouGoodsService douGoodsService;
//    @Autowired
//    private WeiGoodsApiService weiGoodsApiService;
//    @Autowired
//    private WeiGoodsService weiGoodsService;
//    @Autowired
//    private KsGoodsService ksGoodsService;
//    @Autowired
//    private xhsGoodsApiService xhsGoodsApiService;
//    @Autowired
//    private XhsGoodsService xhsGoodsService;
//    @Autowired
//    private GoodsHelper goodsHelper;
//    @Autowired
//    @Qualifier("diansanPullExecutor")
//    private ThreadPoolTaskExecutor diansanExecutorService;
//
//    /**
//     * 拉取商品
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/pull_list", method = RequestMethod.POST)
//    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
//        if (params.getShopId() == null || params.getShopId() <= 0) {
//            return AjaxResult.error("参数错误，没有店铺Id");
//        }
//        OShop shop = shopService.getById(params.getShopId());
//        if (shop == null) return AjaxResult.error("店铺不存在");
//
//
//        log.info("==========pt接口调用,shopType:{}", shop.getType());
//
//        String appSecret = "";
//        String appKey = "";
//        String kwaiSignSecret = "";
//        if (shop.getApiStatus().intValue() == EnumShopApiStatus.SHOP.getIndex()) {
//            appKey = shop.getAppKey();
//            appSecret = shop.getAppSecret();
//            kwaiSignSecret = shop.getApiRequestUrl();
//        } else if (shop.getApiStatus().intValue() == EnumShopApiStatus.OPEN.getIndex()) {
//            OShopPlatform oShopPlatform = platformService.selectById(shop.getType());
//            appKey = oShopPlatform.getAppKey();
//            appSecret = oShopPlatform.getAppSecret();
//            kwaiSignSecret = oShopPlatform.getServerUrl();
//        }
//        String accessToken = shop.getAccessToken();
//        String sellerId = shop.getSellerId();
//
//        if (!StringUtils.hasText(appKey)) {
//            return AjaxResult.error("未配置AppKey");
//        }
//        if (!StringUtils.hasText(appSecret)) {
//            return AjaxResult.error("未配置AppSecret");
//        }
//
//        if (shop.getType() == EnumShopType.PDD.getIndex()) {
//            log.info("============PDD===============");
//            if (!StringUtils.hasText(accessToken)) {
//                return AjaxResult.error("没有找到授权AccessToken");
//            }
//            int successTotal = 0;
//            // 原始接口拉取
//            ApiResultVo<PddGoodsResponse> resultVo = GoodsApiHelper.pullGoodsList(appKey, appSecret, accessToken);
//            if (resultVo.getCode() == 0) {
////                    for (var goods : resultVo.getList()) {
////                        ShopGoods shopGoodsPdd = PddGoodsTransformer.transformShopGoods(goods);
////                        ResultVo<Long> longResultVo = shopGoodsService.savePddGoods(shopGoodsPdd, shop.getId());
////                        log.info("===========拉取pdd店铺商品:{}", JSON.toJSONString(longResultVo));
////                        successTotal++;
////                    }
//                for (var goods : resultVo.getList()) {
//                    PddGoods pddGoods = new PddGoods();
//                    BeanUtils.copyProperties(goods, pddGoods);
//                    List<PddGoodsSku> skuList = new ArrayList<>();
//                    if (goods.getSkuList() != null && goods.getSkuList().size() > 0) {
//                        for (var sku : goods.getSkuList()) {
//                            PddGoodsSku skuSku = new PddGoodsSku();
//                            BeanUtils.copyProperties(sku, skuSku);
//                            skuSku.setGoodsId(pddGoods.getGoodsId());
//                            skuList.add(skuSku);
//                        }
//                    }
//                    pddGoods.setSkuList(skuList);
//                    var result = pddGoodsService.saveGoods(params.getShopId(), pddGoods);
//                    log.info("===========拉取pdd店铺商品:{}", JSONObject.toJSONString(result));
//                    successTotal++;
//                }
//                log.info("=====pdd商品添加完成，总计：{}", successTotal);
//                return AjaxResult.success();
//            } else return AjaxResult.error(resultVo.getMsg());
//        } else if (shop.getType() == EnumShopType.TAO.getIndex()) {
//            log.info("============TAO===============");
//            if (!StringUtils.hasText(accessToken)) {
//                return AjaxResult.error("没有找到授权AccessToken");
//            }
//            int successTotal = 0;
//            // 平台api拉取
//            ApiResultVo<TaoGoodsResponse> apiResult = cn.qihangerp.sdk.tao.GoodsApiHelper.pullGoods(appKey, appSecret, accessToken, null, null);
//            if (apiResult.getCode() == 0) {
////                    for (var goods : apiResult.getList()) {
////                        ShopGoods shopGoodsPdd = TaoGoodsTransformer.transformShopGoods(goods);
////                        ResultVo<Long> longResultVo = shopGoodsService.savePddGoods(shopGoodsPdd, shop.getId());
////                        log.info("===========拉取tao店铺商品:{}", JSON.toJSONString(longResultVo));
////                        successTotal++;
////                    }
//                for (var goods : apiResult.getList()) {
//                    TaoGoods taoGoods = new TaoGoods();
//                    BeanUtils.copyProperties(goods, taoGoods);
//                    taoGoods.setShopId(shop.getId());
//                    taoGoods.setStatus(goods.getApproveStatus());
//                    List<TaoGoodsSku> skuList = new ArrayList<>();
//                    if (goods.getSkus() != null && goods.getSkus().size() > 0) {
//                        for (var sku : goods.getSkus()) {
//                            TaoGoodsSku taoGoodsSku = new TaoGoodsSku();
//                            BeanUtils.copyProperties(sku, taoGoodsSku);
//                            taoGoodsSku.setPropertiesName(taoGoodsSku.getPropertiesName());
//                            taoGoodsSku.setShopId(shop.getId());
//                            taoGoodsSku.setTitle(goods.getTitle());
//                            taoGoodsSku.setPicUrl(goods.getPicUrl());
//                            skuList.add(taoGoodsSku);
//                        }
//                    }
//                    taoGoods.setSkuList(skuList);
//                    var result = taoGoodsService.saveAndUpdateGoods(shop.getId(), taoGoods);
//                    log.info("==================保存tao商品，结果：{}", JSONObject.toJSONString(result));
//                }
//                log.info("====tao商品添加完成，总计：{}", successTotal);
//                return AjaxResult.success();
//            } else return AjaxResult.error(apiResult.getMsg());
//        } else if (shop.getType() == EnumShopType.DOU.getIndex()) {
//            log.info("============DOU===============");
//            if (!StringUtils.hasText(sellerId)) {
//                return AjaxResult.error("抖店参数配置错误：请设置sellerId");
//            }
//            int successTotal = 0;
//            Long pageIndex = 1L;
//            Long pageSize = 20L;
//            // 平台api拉取
//            ApiResultVo<DouApiResponse> resultVo = cn.qihangerp.sdk.dou.GoodsApiHelper.pullGoodsList(appKey, appSecret, sellerId, null, null, pageIndex, pageSize);
//
//            if (resultVo.getCode() == 0) {
////                    for (var goods: resultVo.getData().getGoodsList()){
////                        ShopGoods shopGoodsPdd = DouGoodsTransformer.transformShopGoods(goods);
////                        ResultVo<Long> longResultVo = shopGoodsService.savePddGoods(shopGoodsPdd, shop.getId());
////                        log.info("===========拉取dou店铺商品:{}", JSON.toJSONString(longResultVo));
////                        successTotal++;
////                    }
//                for (var goods : resultVo.getData().getGoodsList()) {
//                    DouGoods douGoods = new DouGoods();
//                    BeanUtils.copyProperties(goods, douGoods);
//                    douGoods.setProductId(goods.getProductId().toString());
//                    douGoods.setProductType(goods.getProductType().intValue());
//                    douGoods.setCheckStatus(goods.getCheckStatus().intValue());
//                    douGoods.setCreateTime(goods.getCreateTime().intValue());
//                    douGoods.setUpdateTime(goods.getUpdateTime().intValue());
//                    douGoods.setStatus(goods.getStatus().intValue());
//                    douGoods.setIsPackageProduct(goods.getIsPackageProduct().toString());
//                    List<DouGoodsSku> skuList = new ArrayList<>();
//                    if (goods.getSkuList() != null && goods.getSkuList().size() > 0) {
//                        for (var s : goods.getSkuList()) {
//                            DouGoodsSku sku = new DouGoodsSku();
//                            BeanUtils.copyProperties(s, sku);
//                            sku.setProductId(goods.getProductId().toString());
//                            sku.setId(s.getId().toString());
//                            sku.setSkuType(s.getSkuType().intValue());
//                            sku.setSkuStatus(s.getSkuStatus().toString());
//                            sku.setPrice(s.getPrice().intValue());
//                            sku.setCreateTime(s.getCreateTime().intValue());
//                            sku.setStockNum(s.getStockNum().intValue());
//                            skuList.add(sku);
//                        }
//                    }
//                    douGoods.setSkuList(skuList);
//                    ResultVo<Integer> integerResultVo = douGoodsService.saveGoods(params.getShopId(), douGoods);
//                    log.info("========DOU商品拉取结果：{}", integerResultVo.getData());
//                    successTotal++;
//                }
//                log.info("====dou商品添加完成，总计：{}", successTotal);
//                return AjaxResult.success();
//            } else return AjaxResult.error(resultVo.getMsg());
//        } else if (shop.getType() == EnumShopType.WEI.getIndex()) {
//            log.info("============WEI===============");
//            ApiResultVo<WeiTokenResponse> token1 = WeiTokenApiHelper.getToken(appKey, appSecret);
//            if (token1.getCode() != 0) {
//                return AjaxResult.error(token1.getMsg());
//            }
//            accessToken = token1.getData().getAccess_token();
//            ApiResultVo<Product> productApiResultVo = weiGoodsApiService.pullGoodsList(accessToken);
//            int successTotal = 0;
//            if (productApiResultVo.getCode() == 0) {
//                // 成功
//                for (var product : productApiResultVo.getList()) {
//                    successTotal++;
//                    WeiGoods goods = new WeiGoods();
//                    goods.setProductId(product.getProduct_id());
//                    goods.setOutProductId(product.getOut_product_id());
//                    goods.setTitle(product.getTitle());
//                    goods.setSubTitle(product.getSub_title());
//                    goods.setHeadImg(product.getHead_imgs().getString(0));
//                    goods.setHeadImgs(JSONObject.toJSONString(product.getHead_imgs()));
//                    goods.setDescInfo(JSONObject.toJSONString(product.getDesc_info()));
//                    goods.setAttrs(JSONObject.toJSONString(product.getAttrs()));
//                    goods.setStatus(product.getStatus());
//                    goods.setEditStatus(product.getEdit_status());
//                    goods.setMinPrice(product.getMin_price());
//                    goods.setSpuCode(product.getSpu_code());
//                    goods.setProductType(product.getProduct_type());
//                    goods.setEditTime(product.getEdit_time());
//                    List<cn.qihangerp.model.entity.WeiGoodsSku> skuList = new ArrayList<>();
//                    for (var sku : product.getSkus()) {
//                        WeiGoodsSku goodsSku = new WeiGoodsSku();
//
//                        goodsSku.setSkuId(sku.getSku_id());
//                        goodsSku.setProductId(product.getProduct_id());
//                        goodsSku.setOutSkuId(sku.getOut_sku_id());
//                        goodsSku.setThumbImg(sku.getThumb_img());
//                        if (org.apache.commons.lang3.StringUtils.isBlank(goodsSku.getThumbImg())) {
//                            goodsSku.setThumbImg(goods.getHeadImg());
//                        }
//                        goodsSku.setSkuCode(sku.getSku_code());
//                        goodsSku.setSkuAttrs(JSONObject.toJSONString(sku.getSku_attrs()));
//                        goodsSku.setSalePrice(sku.getSale_price());
//                        goodsSku.setStockNum(sku.getStock_num());
//                        goodsSku.setStatus(sku.getStatus());
//                        goodsSku.setSkuDeliverInfo(JSONObject.toJSONString(sku.getSku_deliver_info()));
//                        skuList.add(goodsSku);
//                    }
//                    goods.setSkuList(skuList);
//                    ResultVo resultVo = weiGoodsService.saveAndUpdateGoods(params.getShopId(), goods);
//                    log.info("=======WEI商品添加：{}", JSON.toJSONString(resultVo));
//                }
//                log.info("=========wei商品添加总数：{}", successTotal);
//                return AjaxResult.success(successTotal);
//            } else return AjaxResult.error(productApiResultVo.getMsg());
//        } else if (shop.getType() == EnumShopType.KWAI.getIndex()) {
//            log.info("============KWAI===============");
//            if (!StringUtils.hasText(kwaiSignSecret)) {
//                return AjaxResult.error("没有找到SignSecret");
//            }
//            int successTotal = 0;
//            ApiResultVo<ShelfItemInfoResponseParam> productApiResultVo = KwaiGoodsApiHelper.getGoodsList(appKey, kwaiSignSecret, accessToken);
//            if (productApiResultVo.getCode() != 0) return AjaxResult.error(productApiResultVo.getMsg());
//            if (productApiResultVo.getCode() == 0) {
//                // 成功
//                for (var item : productApiResultVo.getList()) {
//                    KsGoods ksGoods = new KsGoods();
//                    BeanUtils.copyProperties(item, ksGoods);
//                    ksGoods.setPrice(item.getPrice().longValue());
//                    ksGoods.setMultipleStock(item.getMultipleStock() ? 1 : 0);
//                    ksGoods.setServiceRule(JSONObject.toJSONString(item.getServiceRule()));
//                    ksGoods.setImageUrls(JSONObject.toJSONString(item.getImageUrls()));
//                    ksGoods.setShopId(shop.getId());
//                    ksGoods.setMerchantId(shop.getMerchantId());
//                    List<KsGoodsSku> skuList = new ArrayList<>();
//                    if (item.getSkuList() != null) {
//                        for (var sku : item.getSkuList()) {
//                            KsGoodsSku skuInfo = new KsGoodsSku();
//                            BeanUtils.copyProperties(sku, skuInfo);
//                            skuInfo.setSkuProp(JSONObject.toJSONString(sku.getSkuProp()));
//                            skuInfo.setMealDetail(JSONObject.toJSONString(sku.getMealDetail()));
//                            skuInfo.setGoodsCode(JSONObject.toJSONString(sku.getGoodsCode()));
//                            skuInfo.setShopId(shop.getId());
//                            skuInfo.setMerchantId(shop.getMerchantId());
//                            skuList.add(skuInfo);
//                        }
//                        ksGoods.setSkuList(skuList);
//                    }
//                    ResultVo<Long> longResultVo = ksGoodsService.saveGoods(ksGoods, params.getShopId());
//                    successTotal++;
//                    log.info("==========保存KS商品:{}", JSONObject.toJSONString(longResultVo));
//                }
//
//            }
//            return AjaxResult.success(successTotal);
//        } else if (shop.getType() == EnumShopType.XHS.getIndex()) {
//            log.info("============XHS===============");
//            if (!StringUtils.hasText(accessToken)) {
//                return AjaxResult.error("没有找到授权AccessToken");
//            }
//            int successTotal = 0;
//            ApiResultVo<GoodsItemInfo> productApiResultVo = xhsGoodsApiService.pullGoodsItemList(appKey, appSecret, accessToken);
//            if (productApiResultVo.getCode() != 0) {
//                return AjaxResult.error(productApiResultVo.getMsg());
//            }
//
//            // 成功
//            for (var product : productApiResultVo.getList()) {
//                successTotal++;
//                XhsGoods goods = new XhsGoods();
//                BeanUtils.copyProperties(product, goods);
//                goods.setItemId(product.getId());
//                goods.setShopId(shop.getId());
//                goods.setMerchantId(shop.getMerchantId());
//                goods.setErpGoodsNum(product.getArticleNo());
//                if (product.getImages() != null && product.getImages().length > 0) {
//                    goods.setImageUrl(product.getImages()[0]);
//                    goods.setImages(JSONObject.toJSONString(product.getImages()));
//                }
//                if (product.getAttributes() != null && product.getAttributes().length > 0) {
//                    goods.setAttributes(JSONObject.toJSONString(product.getAttributes()));
//                }
//                if (product.getVariantIds() != null && product.getVariantIds().length > 0) {
//                    goods.setVariantIds(JSONObject.toJSONString(product.getVariantIds()));
//                }
//                if (product.getImageDescriptions() != null && product.getImageDescriptions().length > 0) {
//                    goods.setImageDescriptions(JSONObject.toJSONString(product.getImageDescriptions()));
//                }
//                if (product.getFaq() != null && product.getFaq().length > 0) {
//                    goods.setFaq(JSONObject.toJSONString(product.getFaq()));
//                }
//                if (product.getIsChannel() != null) {
//                    goods.setIsChannel(product.getIsChannel().toString());
//                }
//                if (product.getFreeReturn() != null) {
//                    goods.setFreeReturn(product.getFreeReturn() ? 1 : 0);
//                }
//                if (product.getEnableStepPresale() != null) {
//                    goods.setEnableStepPresale(product.getEnableStepPresale().toString());
//                }
//                List<XhsGoodsSku> skuList = new ArrayList<>();
//                for (var sku : product.getSkus()) {
//                    XhsGoodsSku goodsSku = new XhsGoodsSku();
//                    BeanUtils.copyProperties(sku, goodsSku);
//                    StringBuilder skuName = new StringBuilder();
//                    if (sku.getVariants() != null && !sku.getVariants().isEmpty()) {
//                        for (var skuVariant : sku.getVariants()) {
//                            skuName.append(skuVariant.getValue()).append(" ");
//                        }
//                        goodsSku.setVariants(JSONObject.toJSONString(sku.getVariants()));
//                    }
//                    goodsSku.setSkuName(skuName.toString());
//                    goodsSku.setSkuId(sku.getId());
//                    if (sku.getDeliveryTime() != null) {
//                        goodsSku.setDeliveryTime(sku.getDeliveryTime().getTime());
//                        goodsSku.setDeliveryType(sku.getDeliveryTime().getType());
//                    }
//                    if (sku.getBuyable() != null) {
//                        goodsSku.setBuyable(sku.getBuyable().toString());
//                    }
//                    if (sku.getIsGift() != null) {
//                        goodsSku.setIsGift(sku.getIsGift().toString());
//                    }
//                    if (sku.getUnionItemDetails() != null && !sku.getUnionItemDetails().isEmpty()) {
//                        goodsSku.setUnionitemDetails(JSONObject.toJSONString(sku.getUnionItemDetails()));
//                    }
//                    skuList.add(goodsSku);
//                }
//                goods.setSkuList(skuList);
//                ResultVo<Long> resultVo = xhsGoodsService.saveAndUpdateGoods(params.getShopId(), goods);
//                log.info("=====添加xhs商品：{}，结果：{}", goods.getItemId(), JSONObject.toJSONString(resultVo));
//            }
//            log.info("======xhs商品添加完成=======");
//            return AjaxResult.success(successTotal);
//        } else return AjaxResult.error("暂不支持！请切换到平台原生！");
//    }
//
//
//}
