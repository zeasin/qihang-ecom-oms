package cn.qihangerp.api.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.OLogisticsCompany;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.module.service.OLogisticsCompanyService;
import cn.qihangerp.module.service.OShopService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/shop")
public class ShopController extends BaseController {
    private final OLogisticsCompanyService logisticsCompanyService;
    private final OShopService shopService;


    /**
     * 查询店铺列表logistics
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:list')")
    @GetMapping("/list")
    public TableDataInfo list(OShop shop)
    {
        List<OShop> list = shopService.selectShopList(shop);
        return getDataTable(list);
    }


    /**
     * 获取店铺详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:query')")
    @GetMapping(value = "/shop/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopService.selectShopById(id));
    }

    /**
     * 新增店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:add')")
    @PostMapping("/shop")
    public AjaxResult add(@RequestBody OShop shop)
    {
        shop.setModifyOn(System.currentTimeMillis()/1000);
        return toAjax(shopService.insertShop(shop));
    }

    /**
     * 修改店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:edit')")
    @PutMapping("/shop")
    public AjaxResult edit(@RequestBody OShop shop)
    {
        if(shop.getId()==null) return AjaxResult.error("缺少参数：id");
        if(StringUtils.hasText(shop.getAccessToken())){
            shop.setApiStatus(1);
        }
        shop.setModifyOn(System.currentTimeMillis() /1000);
        shopService.updateShopById(shop);
//        try{
//            erpPushHelper.shopSave(shopService.getById(shop.getId()));
//        }catch (Exception x){
//        }

        return toAjax(1);
    }

    /**
     * 删除店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:remove')")
    @DeleteMapping("/shop/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shopService.deleteShopByIds(ids));
    }


//    /**
//     * 查询店铺列表logistics
//     */
//    @GetMapping("/logistics")
//    public TableDataInfo logisticsList(Integer type, Integer shopId, PageQuery pageQuery)
//    {
//        PageResult<OLogisticsCompany> result = logisticsCompanyService.queryPageList(type, shopId, pageQuery);
//        return getDataTable(result);
//    }
//    @GetMapping("/logistics_status")
//    public TableDataInfo logisticsStatusList(Integer status, Integer shopType, Integer shopId)
//    {
//        return getDataTable(logisticsCompanyService.queryListByStatus(status,shopType, shopId));
//    }
//    @PutMapping("/logistics/updateStatus")
//    public AjaxResult logisticsUpdateStatus(@RequestBody OLogisticsCompany company)
//    {
//        Integer newStatus = null;
//        if(company.getStatus()==null || company.getStatus().intValue() ==0){
//            newStatus = 1;
//        }else{
//            newStatus = 0;
//        }
//        return toAjax(logisticsCompanyService.updateStatus(company.getId(),newStatus));
//    }
//    @PostMapping("/logistics/add")
//    public AjaxResult add(@RequestBody OLogisticsCompany company)
//    {
//        return toAjax(logisticsCompanyService.save(company));
//    }

}
