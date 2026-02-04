package cn.qihangerp.api.jd.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.entity.JdOrder;
import cn.qihangerp.module.service.JdOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/jd/order")
public class JdOrderFeignController {
    private final JdOrderService jdOrderService;
    @GetMapping(value = "/get_detail")
    public AjaxResult getInfo(Long orderId,Integer vc) {

        JdOrder order = jdOrderService.queryDetailByOrderId(orderId);
        if (order == null) return AjaxResult.error(404, "没有找到订单");
        else return AjaxResult.success(order);

    }
}
