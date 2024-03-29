package com.qihang.tao.controller;

import com.qihang.common.common.AjaxResult;
import com.qihang.common.common.PageQuery;
import com.qihang.common.common.PageResult;
import com.qihang.common.common.TableDataInfo;
import com.qihang.common.enums.EnumShopType;
import com.qihang.common.mq.MqMessage;
import com.qihang.common.mq.MqType;
import com.qihang.common.mq.MqUtils;
import com.qihang.security.common.BaseController;
import com.qihang.tao.common.TaoRequest;
import com.qihang.tao.domain.TaoGoods;
import com.qihang.tao.domain.TaoOrder;
import com.qihang.tao.domain.bo.TaoGoodsBo;
import com.qihang.tao.domain.bo.TaoOrderBo;
import com.qihang.tao.domain.bo.TaoOrderPushBo;
import com.qihang.tao.service.TaoOrderService;
import com.taobao.api.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderContoller extends BaseController {
    private final TaoOrderService orderService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(TaoOrderBo bo, PageQuery pageQuery) {
        PageResult<TaoOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @PostMapping("/push_oms")
    @ResponseBody
    public AjaxResult pushOms(@RequestBody TaoOrderPushBo bo) {
        // TODO:需要优化消息格式
        if(bo!=null && bo.getIds()!=null) {
            for(String id: bo.getIds()) {
                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.TAO, MqType.ORDER_MESSAGE, id));
            }
        }
        return success();
    }
}
