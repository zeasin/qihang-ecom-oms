package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.DouApiCommon;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.module.service.DouOrderService;
import cn.qihangerp.open.dou.DouRequest;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@Log
@AllArgsConstructor
@RestController
@RequestMapping("/dou/ewaybill")
public class DouWaybillController extends BaseController {
    private final DouApiCommon apiCommon;
    private final DouOrderService orderService;

    @GetMapping(value = "/get_waybill_account_list")
    public AjaxResult getWaybillAccountList(Long shopId) throws Exception {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }


    /**
     * 拉取电子面单账号
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_waybill_account", method = RequestMethod.POST)
    public AjaxResult pullWaybillAccount(@RequestBody DouRequest params) throws Exception {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }

    @PostMapping("/get_waybill_code")
    @ResponseBody
    public AjaxResult getWaybillCode() {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }

    @PostMapping("/get_print_data")
    @ResponseBody
    public AjaxResult getPrintData() {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }

    @PostMapping("/push_print_success")
    @ResponseBody
    public AjaxResult pushPrintSuccess() {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }

    /**
     * 发货
     *
     * @param
     * @return
     */
    @PostMapping("/push_ship_send")
    @ResponseBody
    public AjaxResult pushShipSend() {
        return AjaxResult.error("开源版本不支持电子面单功能");
    }
}
