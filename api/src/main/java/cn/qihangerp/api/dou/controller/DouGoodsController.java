package cn.qihangerp.api.dou.controller;

import cn.qihangerp.common.*;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.entity.DouGoods;
import cn.qihangerp.model.entity.DouGoodsSku;
import cn.qihangerp.model.bo.DouGoodsBo;
import cn.qihangerp.module.service.DouGoodsService;
import cn.qihangerp.module.service.DouGoodsSkuService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dou/goods")
@RestController
@AllArgsConstructor
public class DouGoodsController extends BaseController {
    private final DouGoodsService goodsService;
    private final DouGoodsSkuService skuService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(DouGoodsBo bo, PageQuery pageQuery) {
        PageResult<DouGoods> result = goodsService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @RequestMapping(value = "/skuList", method = RequestMethod.GET)
    public TableDataInfo skuList(DouGoodsBo bo, PageQuery pageQuery) {
        PageResult<DouGoodsSku> result = skuService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    /**
     * 获取店铺订单详细信息
     */
    @GetMapping(value = "/sku/{id}")
    public AjaxResult getSkuInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(skuService.getById(id));
    }
    @PostMapping(value = "/sku/linkErp")
    public AjaxResult linkErp(@RequestBody LinkErpGoodsSkuBo bo)
    {
        if(StringUtils.isBlank(bo.getId())){
            return AjaxResult.error(500,"缺少参数Id");
        }
        if(StringUtils.isBlank(bo.getErpGoodsSkuId())){
            return AjaxResult.error(500,"缺少参数oGoodsSkuId");
        }
        ResultVo resultVo = skuService.linkErpGoodsSku(bo);
        if(resultVo.getCode()==0)
            return success();
        else return AjaxResult.error(resultVo.getMsg());
    }

}
