package cn.qihangerp.api.tao.controller;


import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.bo.TaoOrderShipBo;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@Log
@AllArgsConstructor
@RestController
@RequestMapping("/tao/ship")
public class TaoShipController extends BaseController {

    @PostMapping("/order_ship")
    @ResponseBody
    public AjaxResult orderShip(@RequestBody TaoOrderShipBo bo) {
        log.info("接收到oms发货通知：开始推送到tao平台");
        String tid= bo.getTid();
        return success();
    }
}
