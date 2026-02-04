package cn.qihangerp.api.jd.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.model.bo.JdTokenCreateBo;
import cn.qihangerp.module.service.OShopPlatformService;
import cn.qihangerp.module.service.OShopService;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequestMapping("/jd")
@AllArgsConstructor
@RestController
public class JdOAuthController {
    private final OShopPlatformService platformService;
    private final OShopService shopService;
    private static Logger log = LoggerFactory.getLogger(JdOAuthController.class);

    @GetMapping("/oauth")
    public AjaxResult OAuth(@RequestParam Long shopId) throws IOException, InterruptedException {
        //查询店铺信息
        var shop = shopService.selectShopById(shopId);
        OShopPlatform platform = platformService.selectById(shop.getType());

        String url = "https://open-oauth.jd.com/oauth2/to_login?app_key=" + platform.getAppKey()+
                "&response_type=code&redirect_uri=" + URLEncoder.encode(platform.getRedirectUri(), "UTF-8")+
                "&state="+shopId+"&scope=snsapi_base";
       return AjaxResult.success("SUCCESS",url);
    }

    @PostMapping("/tokenCreate")
    public AjaxResult callback(@RequestBody JdTokenCreateBo bo) throws IOException, InterruptedException {
        log.info("京东授权返回code");
        if (!StringUtils.hasText(bo.getCode())) return AjaxResult.error("code不能为空");
        OShopPlatform platform = platformService.getById(bo.getShopType());
//        String code = request.getParameter("code");//gwide6
//        JdClient client=new DefaultJdClient(serverUrl,accessToken,appKey,appSecret);

        String url = "https://open-oauth.jd.com/oauth2/access_token?app_key=" + platform.getAppKey() + "&app_secret=" + platform.getAppSecret() +
                "&grant_type=authorization_code&code=" + bo.getCode();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).header("Content-Type", "application/json").GET()
                .build();
        HttpResponse<String> result = client.send(request, HttpResponse.BodyHandlers.ofString());
        String s = result.body();
        JSONObject resultJson = JSONObject.parse(result.body());
        if(resultJson.getInteger("code")==0){
            //保存accessToken
            String token = resultJson.getString("access_token");
            shopService.updateSessionKey(bo.getShopId(),token);
            return AjaxResult.success("SUCCESS");
        }else {
            return AjaxResult.error(resultJson.getString("msg"));
        }

    }
}
