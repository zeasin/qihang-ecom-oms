package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.DouPullRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.module.service.OShopPlatformService;
import cn.qihangerp.module.service.OShopService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.dou.DouTokenApiHelper;
import cn.qihangerp.open.dou.model.Token;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/dou/oauth")
public class DouOAuthController {
    private final OShopService shopService;
    private final OShopPlatformService platformService;

    @PostMapping("/update_token")
    @ResponseBody
    public AjaxResult getToken(@RequestBody DouPullRequest req)   {
        if(req.getShopId()==null) return AjaxResult.error("缺少参数：shopId");

        var shop = shopService.getById(req.getShopId());
        if (shop == null) {
            return AjaxResult.error("没有找到店铺");
        }
        if (shop.getType() != EnumShopType.DOU.getIndex()) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "店铺不是抖店店铺");
        }
        if (shop.getSellerId() == null || shop.getSellerId()<=0) {
            return AjaxResult.error("请设置抖店平台店铺ID（shopId）");
        }
        String appKey = shop.getAppKey();
        String appSecret = shop.getAppSecret();
        if(StringUtils.isEmpty(appKey) || StringUtils.isEmpty(appSecret)) {
            OShopPlatform platform = platformService.getById(EnumShopType.DOU.getIndex());
            appKey = platform.getAppKey();
            appSecret = platform.getAppSecret();
        }


        if (!StringUtils.hasText(appKey)) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "店铺配置错误，没有找到AppKey");
        }
        if (!StringUtils.hasText(appSecret)) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "店铺配置错误，没有找到AppSecret");
        }
        ApiResultVo<Token> token = DouTokenApiHelper.getToken(appKey, appSecret, shop.getSellerId());
        if(token.getCode()!=0) {
            return AjaxResult.error(ResultVoEnum.API_FAIL.getIndex(), token.getMsg());
        }else{
            shopService.updateSessionKey(req.getShopId(), token.getData().getAccessToken());
        }
        return AjaxResult.success();
    }
}
