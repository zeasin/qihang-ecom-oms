package cn.qihangerp.api.pdd.controller;


import cn.qihangerp.common.*;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.bo.RefundProcessingBo;
import cn.qihangerp.model.entity.PddRefund;
import cn.qihangerp.model.bo.PddOrderPushBo;
import cn.qihangerp.model.bo.PddRefundBo;
import cn.qihangerp.module.service.PddRefundService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/pdd/refund")
public class PddRefundController extends BaseController {
    private final PddRefundService refundService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(PddRefundBo bo, PageQuery pageQuery) {
        PageResult<PddRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }
    /**
     *
     * @param bo
     * @return
     */
    @PostMapping("/handle")
    public AjaxResult handle(@RequestBody RefundProcessingBo bo ) {
        if (bo.getRefundId() == null) return AjaxResult.error(500, "缺少参数refundId");
        ResultVo<Long> resultVo = refundService.refundHandle(bo,getUsername());
        if(resultVo.getCode() == 0) return success();
        else return AjaxResult.error(resultVo.getMsg());
    }
}
