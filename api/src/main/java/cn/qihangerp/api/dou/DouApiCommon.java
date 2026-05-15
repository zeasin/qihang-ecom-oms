package cn.qihangerp.api.dou;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.api.ShopApiParams;
import cn.qihangerp.enums.EnumShopType;
import cn.qihangerp.enums.HttpStatus;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.service.OShopPlatformService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.dou.DouTokenApiHelper;
import cn.qihangerp.open.dou.model.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Component
public class DouApiCommon {
    private final OShopService shopService;
    private final OShopPlatformService platformService;

    /**
     * 更新前的检查
     *
     * @param shopId
     * @return
     * @throws
     */
    public ResultVo<ShopApiParams> checkBefore(Long shopId) {
        var shop = shopService.getById(shopId);
        if (shop == null) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "参数错误，没有找到店铺");
        }
        if (shop.getType() != EnumShopType.DOU.getIndex()) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "参数错误，店铺不是抖店店铺");
        }
        if (StringUtils.isEmpty(shop.getSellerId()) || shop.getSellerId().equals("0")) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "参数错误，请设置抖店平台店铺ID（shopId）");
        }
        String appKey = shop.getAppKey();
        String appSecret = shop.getAppSecret();
        if(StringUtils.isEmpty(appKey) || StringUtils.isEmpty(appSecret)) {
            OShopPlatform platform = platformService.getById(EnumShopType.DOU.getIndex());
            appKey = platform.getAppKey();
            appSecret = platform.getAppSecret();
        }


        if (!StringUtils.hasText(appKey)) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "店铺配置错误，没有找到AppKey");
        }
        if (!StringUtils.hasText(appSecret)) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "店铺配置错误，没有找到AppSecret");
        }
//        if (!StringUtils.hasText(platform.getRedirectUri())) {
//            return ResultVo.error(HttpStatus.PARAMS_ERROR, "第三方平台配置错误，没有找到RedirectUri");
//        }
//        if (!StringUtils.hasText(platform.getServerUrl())) {
//            return ResultVo.error(HttpStatus.PARAMS_ERROR, "第三方平台配置错误，没有找到ServerUrl");
//        }

//        if(shop.getSellerId() == null || shop.getSellerId() <= 0) {
//            return cn.qihangerp.tao.common.ApiResult.build(HttpStatus.PARAMS_ERROR,  "第三方平台配置错误，没有找到SellerUserId");
//        }

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(appKey);
        params.setAppSecret(appSecret);
//        params.setRedirectUri(shop.get());
        params.setServerUrl(shop.getApiRequestUrl());
        params.setSellerId(shop.getSellerId());
        String accessToken = shop.getAccessToken();

        if (!StringUtils.hasText(shop.getAccessToken())) {
            ApiResultVo<Token> token = DouTokenApiHelper.getToken(appKey, appSecret, Long.parseLong(shop.getSellerId()));
            if(token.getCode()!=0) {
                return ResultVo.error(ResultVoEnum.API_FAIL.getIndex(), token.getMsg(), params);
            }else{
                shopService.updateSessionKey(shopId,token.getData().getAccessToken());
                params.setAccessToken(token.getData().getAccessToken());
                accessToken  = token.getData().getAccessToken();
            }
        }else{
            if(StringUtils.hasText(shop.getRefreshToken())) {
                ApiResultVo<Token> token1 = DouTokenApiHelper.refreshToken(appKey, appSecret, accessToken, shop.getRefreshToken());
                if (token1.getCode() == 0) {
                    shopService.updateSessionKey(shopId, token1.getData().getAccessToken());
                    params.setAccessToken(token1.getData().getAccessToken());
                    accessToken = token1.getData().getAccessToken();
                }
            }
        }
        params.setAccessToken(accessToken);
        return ResultVo.success(HttpStatus.SUCCESS, params);
    }

}
