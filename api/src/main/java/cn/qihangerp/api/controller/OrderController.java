package cn.qihangerp.api.controller;

import cn.qihangerp.api.request.OrderCancelRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.bo.OrderAllocateShipRequest;
import cn.qihangerp.model.bo.OrderShipRequest;
import cn.qihangerp.module.service.OOrderItemService;
import cn.qihangerp.module.service.OOrderService;
import cn.qihangerp.request.OrderSearchRequest;
import cn.qihangerp.security.common.BaseController;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺订单Controller
 *
 * @author qihang
 * @date 2023-12-31
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController
{

    private final OOrderService orderService;
    private final OOrderItemService orderItemService;

    /**
     * 查询店铺订单列表
     */
    @PreAuthorize("@ss.hasPermi('shop:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderSearchRequest bo, PageQuery pageQuery)
    {
        var pageList = orderService.queryPageList(bo,pageQuery);
        return getDataTable(pageList);
    }
    /**
     * 待发货列表（去除处理过的）
     * @param order
     * @param pageQuery
     * @return
     */
    @GetMapping("/waitShipmentList")
    public TableDataInfo waitShipmentList(OrderSearchRequest order, PageQuery pageQuery)
    {
        var pageList = orderService.queryWaitShipmentPageList(order,pageQuery);
        return getDataTable(pageList);
    }




    /**
     * 订单发货(手动发货)
     * @param shipBo
     * @return
     */
    @PostMapping("/manualShipment")
    public AjaxResult manualShipment(@RequestBody OrderShipRequest shipBo)
    {
        if(StringUtils.isEmpty(shipBo.getShippingCompany())) return AjaxResult.error("请选择发货快递公司");
        if(StringUtils.isEmpty(shipBo.getShippingNumber())) return AjaxResult.error("请选择发货快递单号");
        var result = orderService.manualShipmentOrder(shipBo,getUsername());
        if(result.getCode() == 0) return AjaxResult.success();
        else return AjaxResult.error(result.getMsg());
    }

    /**
     * 分配供应商发货
     * @param shipBo
     * @return
     */
    @PostMapping("/allocateShipmentOrder")
    public AjaxResult allocateShipmentOrder(@RequestBody OrderAllocateShipRequest shipBo)
    {
        log.info("======分配供应商发货：{}",JSONObject.toJSONString(shipBo));
        if(shipBo.getSupplierId()==null||shipBo.getSupplierId()==0) return AjaxResult.error("请选择供应商");
        var result = orderService.allocateShipmentOrder(shipBo,getUsername());
        if(result.getCode() == 0) return AjaxResult.success();
        else return AjaxResult.error(result.getMsg());
//        return AjaxResult.success();
    }

    /**
     * 获取店铺订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.queryDetailById(id));
    }
    /**
     * 取消订单（如果有itemId就是取消子订单）
     * @param request
     * @return
     */
    @PostMapping("/cancelOrder")
    public AjaxResult cancelOrder(@RequestBody OrderCancelRequest request) {
        if (request.getId() == null) return AjaxResult.error("确实参数：Id");
        if (StringUtils.isEmpty(request.getCancelReason())) return AjaxResult.error("请填写取消原因");
        if (request.getOrderItemId() != null && request.getOrderItemId() > 0) {
            log.info("=====取消子订单====={}", JSONObject.toJSONString(request));
            var result = orderService.cancelOrderItem(request.getOrderItemId(), request.getCancelReason(), getUsername());
            if (result.getCode() == 0) {
                log.info("==============子订单取消成功=========");
                return AjaxResult.success();
            } else return AjaxResult.error(result.getMsg());
        } else {
            log.info("======取消订单======{}", JSONObject.toJSONString(request));
            var result = orderService.cancelOrder(request.getId(), request.getCancelReason(), getUsername());
            if (result.getCode() == 0) {
                log.info("==============订单取消成功=========");
                return AjaxResult.success();
            } else return AjaxResult.error(result.getMsg());
        }
    }



//    @PostMapping
//    public AjaxResult add(@RequestBody OrderCreateBo order)
//    {
//        if(order.getGoodsAmount()==null)return new AjaxResult(1503,"请填写商品价格！");
//
//        int result = orderService.insertErpOrder(order,getUsername());
//        if(result == -1) return new AjaxResult(501,"订单号已存在！");
//        if(result == -2) return new AjaxResult(502,"请添加订单商品！");
//        if(result == -3) return new AjaxResult(503,"请完善订单商品明细！");
//        if(result == -4) return new AjaxResult(504,"请选择店铺！");
//        return toAjax(result);
//    }

}
