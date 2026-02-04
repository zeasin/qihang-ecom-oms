package cn.qihangerp.api.tao.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.model.bo.TaoTokenSaveBo;
import cn.qihangerp.module.service.OShopPlatformService;
import cn.qihangerp.module.service.OShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 淘宝回调地址
 */
@RequestMapping("/tao")
@AllArgsConstructor
@RestController
public class TaoOAuthController {

    private final OShopPlatformService platformService;
    private final OShopService shopService;
    private static Logger log = LoggerFactory.getLogger(TaoOAuthController.class);
    /**
     * 淘宝授权url
     *
     * @param shopId
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/oauth")
    public AjaxResult OAuth(@RequestParam Long shopId) throws IOException, InterruptedException {
        //查询店铺信息
        var shop = shopService.selectShopById(shopId);
        OShopPlatform platform = platformService.selectById(shop.getType());


//        var entity = thirdSettingService.getEntity(shop.getType());
        String url = "http://container.open.taobao.com/container?appkey=" + platform.getAppKey() + "&state=" + shopId;
        //https://oauth.taobao.com/authorize?response_type=token&force_auth=true&from_site=fuwu&client_id=28181872
        return AjaxResult.success("SUCCESS",url);
    }

    /**
     * 淘宝授权回调
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/code_callback")
    public String callback(Model model, HttpServletRequest request) {
        log.info("淘系店铺授权回调开始");

        String sessionKey = request.getParameter("top_session");
        String state = request.getParameter("state");
        if(StringUtils.isEmpty(state) ){
            return "错误：没有state参数，state参数就是店铺ID";
        }
        if(StringUtils.isEmpty(sessionKey) ){
            return "错误：没有top_session参数，top_session参数就是access_token";
        }
        try {
            Long shopId = Long.parseLong(state);
            var shop = shopService.selectShopById(shopId);
            if(shop == null) {
                return "错误：没有找到店铺"+shopId;
            }
            shopService.updateSessionKey(shopId, sessionKey);
            return "成功：请关闭此页面";
        } catch (Exception e) {
            return "异常："+e.getMessage();
        }
    }
    @PostMapping("/saveSessionKey")
    public AjaxResult saveSessionKey(@RequestBody TaoTokenSaveBo bo){
        log.info("保存淘宝开放平台SessionKey");
        if (!org.springframework.util.StringUtils.hasText(bo.getCode())) return AjaxResult.error("code不能为空");
        if (bo.getShopId()==null) return AjaxResult.error("shopId不能为空");
        var shop = shopService.selectShopById(bo.getShopId());
        if(shop == null) {
            return AjaxResult.error("没有找到店铺");
        }
        shopService.updateSessionKey(bo.getShopId(), bo.getCode());
        return AjaxResult.success();
    }

}
