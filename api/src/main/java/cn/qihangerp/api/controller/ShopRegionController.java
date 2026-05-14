package cn.qihangerp.api.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.model.entity.OShopRegion;
import cn.qihangerp.security.common.BaseController;
import cn.qihangerp.service.OShopPlatformService;
import cn.qihangerp.service.OShopRegionService;
import cn.qihangerp.service.OShopService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
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
public class ShopRegionController extends BaseController {
    private final OShopRegionService shopRegionService;

    @GetMapping("/region/list")
    public TableDataInfo platformList(OShopPlatform bo)
    {
        LambdaQueryWrapper<OShopRegion> qw = new LambdaQueryWrapper<>();
        qw.eq(bo.getStatus()!=null,OShopRegion::getStatus,bo.getStatus());
        if(bo.getStatus()!=null) {
            qw.last(" ORDER BY sort desc");
        }
        List<OShopRegion> list = shopRegionService.list(qw);
        return getDataTable(list);
    }



    @GetMapping(value = "/region/{id}")
    public AjaxResult getPlatform(@PathVariable("id") Long id)
    {
        return success(shopRegionService.getById(id));
    }

    @PostMapping("/region")
    public AjaxResult add(@RequestBody OShopRegion platform)
    {
        platform.setStatus("0");
        platform.setCreateBy(getUsername());
        platform.setCreateTime(new Date());
        return toAjax(shopRegionService.save(platform));
    }

    /**
     * 修改平台
     * @param
     * @return
     */
    @PutMapping("/region")
    public AjaxResult edit(@RequestBody OShopRegion platform)
    {
        platform.setStatus(null);
        platform.setUpdateBy(getUsername());
        platform.setUpdateTime(new Date());
        return toAjax(shopRegionService.updateById(platform));
    }

    /**
     * 状态修改
     */
    @PutMapping("/region/changeStatus")
    public AjaxResult changeStatus(@RequestBody OShopRegion platform)
    {
        return toAjax(shopRegionService.updateById(platform));
    }

}
