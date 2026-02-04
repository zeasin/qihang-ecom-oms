package cn.qihangerp.api.controller.bill;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.ErpBillShopAccounts;
import cn.qihangerp.module.service.ErpBillShopAccountsService;
import cn.qihangerp.module.service.OShopService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 店铺Controller
 * 
 * @author qihang
 * @date 2023-12-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/financial/shop_accounts")
public class ShopAccountsController extends BaseController {
    private final OShopService shopService;
    private final ErpBillShopAccountsService shopAccountsService;

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ErpBillShopAccounts bo, PageQuery pageQuery) {

        bo.setTenantId(getUserId());
        PageResult<ErpBillShopAccounts> pageResult = shopAccountsService.queryPageList(bo, pageQuery);

        return getDataTable(pageResult);
    }

    /**
     * 获取详细信息
     */

    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopAccountsService.getById(id));
    }

    /**
     * 新增
     */

    @PostMapping("/add")
    public AjaxResult add(@RequestBody ErpBillShopAccounts bo)
    {
//        bo.setDate(DateUtils.parseDateToStr("yyyy-MM-dd",bo.getTradeTime()));
        bo.setDate(bo.getTradeTime().substring(0,10));
        bo.setTenantId(getUserId());
        if(bo.getShopId()==null)bo.setShopId(0L);
        bo.setCreateTime(new Date());
        bo.setStatus(1);
        return toAjax(shopAccountsService.save(bo));
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody ErpBillShopAccounts bo)
    {
        var shopInfo = shopAccountsService.getById(bo.getId());
        if(shopInfo.getTenantId()!=getUserId()){
            return AjaxResult.error("不允许修改别人的数据");
        }else {
            bo.setTenantId(null);
            bo.setCreateTime(null);
//            bo.setTradeTime(null);
            bo.setDate(bo.getTradeTime().substring(0,10));
            bo.setUpdateTime(new Date());
            return toAjax(shopAccountsService.updateById(bo));
        }
    }


    /**
     * 删除
     */
	@DeleteMapping("/del/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        var shopInfo = shopAccountsService.getById(id);
        if(shopInfo.getTenantId()!=getUserId()){
            return AjaxResult.error("不允许修改别人的数据");
        }else {
            return toAjax(shopAccountsService.removeById(id));
        }
    }

}
