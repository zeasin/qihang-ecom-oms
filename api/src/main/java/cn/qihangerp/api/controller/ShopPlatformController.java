package cn.qihangerp.api.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.service.OShopPlatformService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.security.common.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺Controller
 * 
 * @author qihang
 * @date 2023-12-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/oms-api/shop")
public class ShopPlatformController extends BaseController {
    private final OShopService shopService;
    private final OShopPlatformService shopPlatformService;

    @GetMapping("/platformList")
    public TableDataInfo platformList(OShopPlatform bo)
    {
        LambdaQueryWrapper<OShopPlatform> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.hasText(bo.getStatus()),OShopPlatform::getStatus,bo.getStatus());
        if(StringUtils.hasText(bo.getStatus())) {
            qw.last(" ORDER BY sort desc");
        }
        List<OShopPlatform> list = shopPlatformService.list(qw);
        return getDataTable(list);
    }



    @GetMapping(value = "/platform/{id}")
    public AjaxResult getPlatform(@PathVariable("id") Long id)
    {
        return success(shopPlatformService.getById(id));
    }



    /**
     * 修改平台
     * @param
     * @return
     */
    @PutMapping("/platform")
    public AjaxResult edit(@RequestBody OShopPlatform platform)
    {
        platform.setStatus(null);
        return toAjax(shopPlatformService.updateById(platform));
    }

    /**
     * 状态修改
     */
    @PutMapping("/platform/changeStatus")
    public AjaxResult changeStatus(@RequestBody OShopPlatform platform)
    {

        return toAjax(shopPlatformService.updateById(platform));
    }

}
