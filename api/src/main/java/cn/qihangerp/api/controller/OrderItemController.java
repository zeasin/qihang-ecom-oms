package cn.qihangerp.api.controller;

import cn.qihangerp.api.request.OrderItemSpecIdUpdateBo;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.bo.OrderItemListBo;
import cn.qihangerp.module.service.OOrderItemService;
import cn.qihangerp.module.service.OOrderService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺订单Controller
 *
 * @author qihang
 * @date 2023-12-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderItemController extends BaseController
{
    private final OOrderItemService itemService;
    private final OOrderService orderService;

    /**
     * 查询店铺订单列表
     */
    @GetMapping("/item_list")
    public TableDataInfo list(OrderItemListBo bo, PageQuery pageQuery)
    {
        var pageList = itemService.selectPageVo(pageQuery,bo);
        return getDataTable(pageList);
    }

    /**
     * 修改订单明细specId
     * @param bo
     * @return
     */
    @PostMapping("/order_item_sku_id_update")
    public AjaxResult orderItemSpecIdUpdate(@RequestBody OrderItemSpecIdUpdateBo bo)
    {
        if(bo.getOrderItemId()==null || bo.getOrderItemId() ==0) return AjaxResult.error("参数错误：orderItemId为空");
        if(bo.getErpGoodsSpecId()==null || bo.getErpGoodsSpecId() ==0) return AjaxResult.error("参数错误：ErpGoodsSpecId为空");

        var result = itemService.updateErpSkuId(bo.getOrderItemId(),bo.getErpGoodsSpecId());
        if (result.getCode() == 0) {
            return AjaxResult.success();
        } else return AjaxResult.error(result.getMsg());

    }
}
