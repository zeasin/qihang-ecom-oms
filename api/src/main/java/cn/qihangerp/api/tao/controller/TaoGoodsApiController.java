package cn.qihangerp.api.tao.controller;

import cn.qihangerp.api.tao.TaoApiCommon;
import cn.qihangerp.api.tao.TaoRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.api.ShopApiParams;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.TaoGoods;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.module.service.TaoGoodsService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.tao.TaoGoodsApiHelper;
import cn.qihangerp.open.tao.response.TaoGoodsResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping("/tao/goods")
@RestController
@AllArgsConstructor
public class TaoGoodsApiController  {
    private final TaoApiCommon taoApiCommon;
    private final TaoGoodsService goodsService;
    private final OShopPullLogsService pullLogsService;
    private final OShopPullLasttimeService pullLasttimeService;
/**
        * @api {post} /api/v1/pull_goods 更新店铺商品列表
     * @apiVersion 1.0.0
            * @apiName pullGoods
     * @apiGroup taoGood
     * @apiParam {String}  startTime 开始时间
     * @apiParam {String}  endTime 结束时间
     * @apiParam {Number}  shopId 店铺id(东方符号：7)
     * @apiSuccessExample {json} Success-Response:
            * HTTP/1.1 200 OK{
        "code": "0成功其他失败",
                "msg": "成功或失败信息"
    }
     */
    @RequestMapping(value = "/pull_goods", method = RequestMethod.POST)
    public AjaxResult pullGoodsList(@RequestBody TaoRequest req) throws Exception {
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
//            return ApiResult.build(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        Date currDateTime = new Date();
        long startTimeMillis = System.currentTimeMillis();
        var checkResult = taoApiCommon.checkBefore(req.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg());
        }
        ShopApiParams shopApiParams = checkResult.getData();
        String sessionKey = shopApiParams.getAccessToken();
        String url = shopApiParams.getServerUrl();
        String appKey = shopApiParams.getAppKey();
        String appSecret = shopApiParams.getAppSecret();

        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(req.getShopId(), "GOODS");
        if(req.getPullType()!=null && req.getPullType()==1) {
            if (lasttime != null) {
                // 按更新时间来
                startTime = lasttime.getLasttime().minusHours(7*24);//取上次结束一个小时前
                endTime = LocalDateTime.now();
            }
        }

        Long pageIndex = 1L;
        Long pageSize = 100L;

//        ApiResult<TaoGoods> listApiResult = GoodsApiHelper.pullGoods(pageIndex, pageSize, url, appKey, appSecret, sessionKey);
        try {
            ApiResultVo<TaoGoodsResponse> listApiResult = TaoGoodsApiHelper.pullGoodsList(appKey, appSecret, sessionKey);
//            ApiResultVo<TaoGoodsResponse> listApiResult = GoodsApiHelper.pullGoods(pageIndex, pageSize, url, appKey, appSecret, sessionKey, startTime, endTime);

            int insertSuccess = 0;//新增成功的订单
            int totalError = 0;
            int hasExistOrder = 0;//已存在的订单数

            for (var g : listApiResult.getList()) {
                TaoGoods taoGoods = new TaoGoods();
                BeanUtils.copyProperties(g, taoGoods);
                // TODO:转换goods
                taoGoods.setNumIid(g.getNum_iid());
                taoGoods.setTitle(g.getTitle());
                taoGoods.setType(g.getType());
                taoGoods.setCid(g.getCid());
                taoGoods.setPicUrl(g.getPic_url());
                taoGoods.setNum(g.getNum());
                taoGoods.setValidThru(g.getValid_thru());
                taoGoods.setHasDiscount(g.isHas_discount() + "");
                taoGoods.setHasInvoice(g.isHas_invoice() + "");
                taoGoods.setHasWarranty(g.isHas_warranty() + "");
                taoGoods.setHasShowcase(g.isHas_showcase() + "");
                taoGoods.setModified(DateUtils.stringtoDate(g.getModified()));
                taoGoods.setDelistTime(StringUtils.isEmpty(g.getDelist_time()) ? null : DateUtils.stringtoDate(g.getDelist_time()));
                taoGoods.setPostageId(g.getPostage_id());
                taoGoods.setOuterId(g.getOuter_id());
                taoGoods.setListTime(StringUtils.isEmpty(g.getList_time()) ? null : DateUtils.stringtoDate(g.getList_time()));
                taoGoods.setPrice(g.getPrice());
                taoGoods.setSoldQuantity(g.getSold_quantity());
                taoGoods.setShopId(req.getShopId());
                List<TaoGoodsSku> skuList = new ArrayList<>();
                if (g.getSkuList() != null && g.getSkuList().size() > 0) {
                    for (var s : g.getSkuList()) {
                        TaoGoodsSku taoGoodsSku = new TaoGoodsSku();
                        BeanUtils.copyProperties(s, taoGoodsSku);
//                        taoGoodsSku.setShopId(req.getShopId());
                        taoGoodsSku.setNumIid(s.getNum_iid());
                        taoGoodsSku.setIid(s.getIid());
                        taoGoodsSku.setSkuId(s.getSku_id());
                        taoGoodsSku.setProperties(s.getProperties());
                        taoGoodsSku.setPropertiesName(s.getProperties_name());
                        taoGoodsSku.setQuantity(s.getQuantity());
                        taoGoodsSku.setSkuSpecId(s.getSku_spec_id() + "");
                        taoGoodsSku.setPrice(StringUtils.isEmpty(s.getPrice()) ? null : Double.parseDouble(s.getPrice()));
                        taoGoodsSku.setOuterId(s.getOuter_id());
                        taoGoodsSku.setCreated(StringUtils.isEmpty(s.getCreated()) ? null : s.getCreated());
                        taoGoodsSku.setModified(StringUtils.isEmpty(s.getModified()) ? null : s.getModified());
                        taoGoodsSku.setStatus(s.getStatus());
                        taoGoodsSku.setCreateTime(new Date());
                        skuList.add(taoGoodsSku);
                    }
                }
                taoGoods.setSkus(skuList);
                int result = goodsService.saveAndUpdateGoods(req.getShopId(), taoGoods);
                if (result == ResultVoEnum.DataExist.getIndex()) {
                    //已经存在
                    hasExistOrder++;
                } else if (result == ResultVoEnum.SUCCESS.getIndex()) {
                    insertSuccess++;
                } else {
                    totalError++;
                }
            }

            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(req.getShopId());
            logs.setShopType(EnumShopType.TAO.getIndex());
            logs.setPullType("GOODS");
            logs.setPullWay("主动拉取");
            logs.setPullParams("{PageNo:1,PageSize:100}");
            logs.setPullResult("{successTotal:" + listApiResult.getTotalRecords() + "}");
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - startTimeMillis);
            pullLogsService.save(logs);
            if (totalError == 0) {
                if (lasttime == null) {
                    // 新增
                    OShopPullLasttime insertLasttime = new OShopPullLasttime();
                    insertLasttime.setShopId(req.getShopId());
                    insertLasttime.setCreateTime(new Date());
                    insertLasttime.setLasttime(endTime == null ? LocalDateTime.now() : endTime);
                    insertLasttime.setPullType("GOODS");
                    pullLasttimeService.save(insertLasttime);

                } else {
                    // 修改
                    OShopPullLasttime updateLasttime = new OShopPullLasttime();
                    updateLasttime.setId(lasttime.getId());
                    updateLasttime.setUpdateTime(new Date());
                    updateLasttime.setLasttime(endTime == null ? LocalDateTime.now() : endTime);
                    pullLasttimeService.updateById(updateLasttime);
                }
            }

            String msg = "成功，总共找到：" + listApiResult.getTotalRecords() + "条商品数据，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
            log.info(msg);
//        return new ApiResult<>(EnumResultVo.SUCCESS.getIndex(), msg);
            return AjaxResult.success(msg);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("调用接口异常");
        }
    }

}
