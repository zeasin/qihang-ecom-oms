package cn.qihangerp.api.wei.controller;

import cn.qihangerp.api.wei.WeiApiCommon;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.model.entity.OLogisticsCompany;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.module.service.OLogisticsCompanyService;
import cn.qihangerp.module.service.OShopService;
import cn.qihangerp.open.wei.WeiLogisticsApiHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/wei/logistics")
@RestController
@AllArgsConstructor
public class WeiLogisticsApiController {
    private final WeiApiCommon apiCommon;
    private final OLogisticsCompanyService companyService;
    private final OShopService shopService;
    @RequestMapping(value = "/pull_logistics", method = RequestMethod.POST)
    public AjaxResult pullList() throws Exception {
        log.info("=========拉取wei快递公司==============");

        List<OShop> shopList = shopService.selectShopByShopType(EnumShopType.WEI);
        if(shopList == null || shopList.isEmpty()) {
            return AjaxResult.error("没有找到Wei店铺");
        }

        var checkResult = apiCommon.checkBefore(shopList.get(0).getId());
        if (checkResult.getCode() != ResultVoEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }

        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        String accessToken = checkResult.getData().getAccessToken();

        var resultVo = WeiLogisticsApiHelper.getLogisticsCompanyList(accessToken);
        if(resultVo.getCode() == 0){
            // 成功
            for (var item:resultVo.getList()){
                OLogisticsCompany oLogisticsCompany = new OLogisticsCompany();
                oLogisticsCompany.setPlatformId(EnumShopType.WEI.getIndex());
                oLogisticsCompany.setShopId(0L);
                oLogisticsCompany.setLogisticsId(item.getDelivery_id());
                oLogisticsCompany.setCode(item.getDelivery_id());
                oLogisticsCompany.setName(item.getDelivery_name());
                oLogisticsCompany.setStatus(0);
                companyService.save(oLogisticsCompany);
                log.info("=========添加物流快递公司===========");
            }
            log.info("=========Wei物流快递公司添加完成========");
            return AjaxResult.success();
        }
        else return AjaxResult.error(resultVo.getMsg());
    }
}
