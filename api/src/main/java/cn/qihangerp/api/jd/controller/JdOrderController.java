package cn.qihangerp.api.jd.controller;


import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.common.mq.MqUtils;
import cn.qihangerp.model.entity.JdOrder;
import cn.qihangerp.model.bo.JdOrderBo;
import cn.qihangerp.service.JdOrderService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/jd/order")
public class JdOrderController extends BaseController {
    private final JdOrderService orderService;
    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(JdOrderBo bo, PageQuery pageQuery) {
        PageResult<JdOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

}
