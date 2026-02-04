package cn.qihangerp.api.tao.controller;

import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.model.entity.TaoRefund;
import cn.qihangerp.module.service.TaoRefundService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tao/refund")
public class TaoRefundFeignController {
    private final TaoRefundService refundService;
    @GetMapping(value = "/get_detail")
    public AjaxResult getInfo(Long refundId)
    {
        List<TaoRefund> refundList = refundService.list(new LambdaQueryWrapper<TaoRefund>().eq(TaoRefund::getRefundId,refundId));
        if(refundList==null) return AjaxResult.error(404,"没有找到退款单");
        else return AjaxResult.success(refundList.get(0));
    }
}
