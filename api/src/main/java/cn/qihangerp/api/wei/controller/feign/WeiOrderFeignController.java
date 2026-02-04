package cn.qihangerp.api.wei.controller.feign;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.entity.OmsWeiOrder;
import cn.qihangerp.module.service.OmsWeiOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/open-api/wei/order")
public class WeiOrderFeignController  {
    private final OmsWeiOrderService orderService;
    @GetMapping(value = "/get_detail")
    public AjaxResult getInfo(String orderId)
    {
        OmsWeiOrder order = orderService.queryDetailByOrderId(orderId);
        if(order==null) return AjaxResult.error(404,"没有找到订单");
        else return AjaxResult.success(order);
    }
}
