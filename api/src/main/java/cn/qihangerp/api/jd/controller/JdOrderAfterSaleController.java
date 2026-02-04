package cn.qihangerp.api.jd.controller;


import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.JdRefund;
import cn.qihangerp.model.bo.JdAfterBo;
import cn.qihangerp.model.bo.JdOrderPushBo;
import cn.qihangerp.module.service.JdOrderService;
import cn.qihangerp.module.service.JdRefundService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/jd/after")
public class JdOrderAfterSaleController extends BaseController {
    private final JdOrderService orderService;
//    private final JdRefundService refundService;
//    private final JdOrderAfterService afterService;
    private final JdRefundService afterSaleService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(JdAfterBo bo, PageQuery pageQuery) {
        PageResult<JdRefund> result = afterSaleService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }


}
