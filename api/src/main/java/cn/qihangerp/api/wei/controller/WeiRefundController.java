package cn.qihangerp.api.wei.controller;



import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.mq.MqMessage;
import cn.qihangerp.common.mq.MqType;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.bo.WeiRefundBo;
import cn.qihangerp.model.entity.OmsWeiRefund;
import cn.qihangerp.model.bo.WeiOrderPushBo;
import cn.qihangerp.module.service.OmsWeiRefundService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wei/refund")
public class WeiRefundController extends BaseController {
    private final OmsWeiRefundService refundService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(WeiRefundBo bo, PageQuery pageQuery) {
        PageResult<OmsWeiRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }



}
