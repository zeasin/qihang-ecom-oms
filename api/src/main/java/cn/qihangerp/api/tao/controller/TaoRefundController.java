package cn.qihangerp.api.tao.controller;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.TaoRefund;
import cn.qihangerp.model.bo.TaoRefundBo;
import cn.qihangerp.service.TaoRefundService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/tao/refund")
public class TaoRefundController extends BaseController {
    private final TaoRefundService refundService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(TaoRefundBo bo, PageQuery pageQuery) {
        PageResult<TaoRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

}
