package cn.qihangerp.api.controller;

import cn.qihangerp.api.request.ShipOrderItemSkuIdUpdateBo;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.bo.ShipStockUpBo;
import cn.qihangerp.module.service.OOrderShipListItemService;
import cn.qihangerp.module.service.OOrderShipListService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/ship")
public class ShipStockupController  extends BaseController {
    private final OOrderShipListItemService orderShipListItemService;
    private final OOrderShipListService shipStockUpService;
    /**
     * 备货列表
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/stock_up_list")
    public TableDataInfo stock_up_list(ShipStockUpBo bo, PageQuery pageQuery)
    {
        var pageList = shipStockUpService.queryWarehousePageList(bo,pageQuery);
        return getDataTable(pageList);
    }

    /**
     * 备货列表(仓库发货)
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/stock_up_list_item_by_warehouse")
    public TableDataInfo stock_up_list_item_by_warehouse(ShipStockUpBo bo, PageQuery pageQuery)
    {
        var pageList = orderShipListItemService.queryWarehousePageList(bo,pageQuery);
        return getDataTable(pageList);
    }
    /**
     * 备货中-供应商代发
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/stock_up_list_by_supplier")
    public TableDataInfo supplierShipList(ShipStockUpBo bo, PageQuery pageQuery)
    {
//        bo.setShipper(1);
//        bo.setStockStatus(0);
//        bo.setTenantId(getUserId());
//        PageResult<ErpShipmentItem> list = shippingItemService.queryPageList(bo, pageQuery);
//        return getDataTable(list);
//        bo.setShipper(1);
//        bo.setTenantId(getUserId());
//        PageResult<ErpShipment> erpShipmentPageResult = shippingService.queryPageList(bo, pageQuery);
//        return getDataTable(erpShipmentPageResult);
        var pageList = shipStockUpService.querySupplierPageList(bo,pageQuery);
        return getDataTable(pageList);
    }


//    @PostMapping("/stock_up_complete")
//    public AjaxResult stock_up_complete(@RequestBody ShipStockUpCompleteBo bo)
//    {
//        int result = orderShipListItemService.stockUpComplete(bo);
//        if(result == -1) return AjaxResult.error("参数错误：orderItemIds为空");
//        if(result == -2) return AjaxResult.error("参数错误：没有要添加的");
//        else if(result == -1001) return AjaxResult.error("存在错误的orderItemId：状态不对不能生成出库单");
//        else if(result == -1002) return AjaxResult.error("存在错误的订单数据：名单明细中没有skuId请修改！");
//        //wmsStockOutEntryService.insertWmsStockOutEntry(wmsStockOutEntry)
//        return toAjax(1);
//    }

    @PostMapping("/stock_up_complete_by_order")
    public AjaxResult stock_up_completeByOrder(@RequestBody cn.qihangerp.model.bo.ShipStockOutByOrder bo)
    {
        if(bo.getShipOrderId()==null || bo.getShipOrderId()==0) return AjaxResult.error("缺少参数：shipOrderId");

        var result = orderShipListItemService.stockUpCompleteByOrder(bo.getShipOrderId());
        if(result.getCode()==0) return AjaxResult.success();
        else return AjaxResult.error(result.getMsg());
    }

    /**
     * 修改订单明细skuId
     * @param bo
     * @return
     */
    @PostMapping("/order_item_sku_id_update")
    public AjaxResult orderItemSpecIdUpdate(@RequestBody ShipOrderItemSkuIdUpdateBo bo)
    {
        if(bo.getId()==null || bo.getId() ==0) return AjaxResult.error("参数错误：id为空");
        if(bo.getErpGoodsSkuId()==null || bo.getErpGoodsSkuId() ==0) return AjaxResult.error("参数错误：ErpGoodsSkuId为空");

        var result = orderShipListItemService.updateErpSkuId(bo.getId(),bo.getErpGoodsSkuId());
        if (result.getCode() == 0) {
            return AjaxResult.success();
        } else return AjaxResult.error(result.getMsg());

    }
}
