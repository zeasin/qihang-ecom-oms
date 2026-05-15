package cn.qihangerp.api.controller;

import cn.qihangerp.api.request.OrderCancelRequest;
import cn.qihangerp.common.*;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.enums.EnumShopType;
import cn.qihangerp.enums.EnumUserType;
import cn.qihangerp.model.bo.PosOrderCreateBo;
import cn.qihangerp.model.bo.ShopOrderCreateBo;
import cn.qihangerp.model.bo.ShopOrderPushBo;
import cn.qihangerp.model.entity.ErpMerchant;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.model.entity.ShopOrder;
import cn.qihangerp.model.entity.ShopOrderItem;
import cn.qihangerp.model.query.ShopOrderQueryBo;
import cn.qihangerp.security.common.BaseController;
import cn.qihangerp.service.ErpMerchantService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.service.ShopOrderItemService;
import cn.qihangerp.service.ShopOrderService;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/oms-api/shop/order")
public class ShopOrderController extends BaseController {
    private final ShopOrderService shopOrderService;
    private final ShopOrderItemService shopOrderItemService;
    private final ErpMerchantService merchantService;
    private final MqUtils mqUtils;
    private final OShopService oShopService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(ShopOrderQueryBo bo, PageQuery pageQuery) {
        PageResult<ShopOrder> result = shopOrderService.queryOrderPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @RequestMapping(value = "/item_list", method = RequestMethod.GET)
    public TableDataInfo orderItemList(ShopOrderQueryBo bo, PageQuery pageQuery) {
        PageResult<ShopOrderItem> result = shopOrderItemService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        ShopOrder shopOrder = shopOrderService.queryDetailById(id);

        return success(shopOrder);
    }

    @PostMapping("/push_oms")
    @ResponseBody
    public AjaxResult pushOms(@RequestBody ShopOrderPushBo bo) {
        if(bo!=null && bo.getIds()!=null) {
            for(Long shopOrderId: bo.getIds()) {
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.SHOP_ORDER,MqType.ORDER_MESSAGE,shopOrderId.toString()));
            }
        }
        return success();
    }

    @PostMapping("/create")
    public AjaxResult shopOrderCreate(@RequestBody ShopOrderCreateBo order) {
        log.info("=======手动创建店铺订单====={}", JSONObject.toJSONString(order));
        if(StringUtils.isEmpty(order.getOrderNum())) return AjaxResult.error("请填写订单号");
        if(order.getShopId()==null||order.getShopId()<=0) return AjaxResult.error("请选择店铺");
        if(StringUtils.isEmpty(order.getReceiverName())) return AjaxResult.error("请填写收件人姓名");
        if(StringUtils.isEmpty(order.getReceiverPhone())) return AjaxResult.error("请填写收件人手机号");
        if(StringUtils.isEmpty(order.getProvince())||StringUtils.isEmpty(order.getCity())||StringUtils.isEmpty(order.getTown())) return AjaxResult.error("请选择省市区");
        if(StringUtils.isEmpty(order.getAddress())) return AjaxResult.error("请填写详细地址");
        if(order.getGoodsAmount()==null)return new AjaxResult(1503,"请填写商品价格！");
        if(order.getItemList()==null||order.getItemList().isEmpty()) return AjaxResult.error("请添加商品明细");
        OShop shop = oShopService.getById(order.getShopId());
        if(shop==null) return AjaxResult.error("店铺不存在");


        ResultVo<Long> result = shopOrderService.createOrder(order,getUsername());
        if(result.getCode()!=0) {
            return AjaxResult.error(result.getMsg());
        }

        log.info("==============手工订单添加成功==========");
        mqUtils.sendApiMessage(MqMessage.build(EnumShopType.SHOP_ORDER, MqType.ORDER_MESSAGE, result.getData().toString()));
        return AjaxResult.success(result.getData());
    }

    @PostMapping("/cancelOrder")
    public AjaxResult cancelOrder(@RequestBody OrderCancelRequest request) {
        if(request.getId()==null) return AjaxResult.error("确实参数：Id");
        if(StringUtils.isEmpty(request.getCancelReason())) return AjaxResult.error("请填写取消原因");


        var result = shopOrderService.cancelOrder(request.getId(),request.getCancelReason(),getUsername());
        if(result.getCode() == 0){
            log.info("==============店铺订单取消成功=========开始通知");
            mqUtils.sendApiMessage(MqMessage.build(EnumShopType.SHOP_ORDER,MqType.ORDER_CANCEL_MESSAGE,request.getId().toString()));
            return AjaxResult.success();
        }
        else return AjaxResult.error(result.getMsg());
    }

}
