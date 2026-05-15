package cn.qihangerp.api.tao.controller;

import cn.qihangerp.api.tao.TaoApiCommon;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.enums.EnumShopType;
import cn.qihangerp.enums.HttpStatus;
import cn.qihangerp.model.entity.OLogisticsCompany;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.service.OLogisticsCompanyService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.tao.TaoLogisticsApiHelper;
import cn.qihangerp.open.tao.model.TaoLogisticsCompany;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

/**
 *
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/tao/logistics")
public class TaoLogisticsApiController {
    private final OShopService shopService;
    private final TaoApiCommon taoApiCommon;
    private final OLogisticsCompanyService companyService;
    /**
     *
     * @param
     * @
     * @throws
     */
    @PostMapping("/pull_logistics")
    @ResponseBody
    public AjaxResult pull_logistics() throws IOException {
        log.info("=========拉取tao快递公司==============");

        List<OShop> shopList = shopService.selectShopByShopType(EnumShopType.TAO);
        if(shopList == null || shopList.isEmpty()) {
            return AjaxResult.error("没有找到Tao店铺");
        }
        var checkResult = taoApiCommon.checkBefore(shopList.get(0).getId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }

        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        String accessToken = checkResult.getData().getAccessToken();

        ApiResultVo<TaoLogisticsCompany> resultVo = TaoLogisticsApiHelper.getLogisticsCompany(appKey,appSecret);

        if (resultVo.getCode() != 0) return AjaxResult.error(resultVo.getCode(), resultVo.getMsg());
        for (var item:resultVo.getList()){
            OLogisticsCompany oLogisticsCompany = new OLogisticsCompany();
            oLogisticsCompany.setPlatformId(EnumShopType.TAO.getIndex());
            oLogisticsCompany.setShopId(0L);
            oLogisticsCompany.setLogisticsId(item.getId().toString());
            oLogisticsCompany.setCode(item.getCode());
            oLogisticsCompany.setName(item.getName());
            oLogisticsCompany.setStatus(0);
            companyService.save(oLogisticsCompany);
            log.info("=========添加物流快递公司===========");
        }
        log.info("=========TAO物流快递公司添加完成========");
        return AjaxResult.success();
    }



}
