package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.TaoRefund;
import cn.qihangerp.model.bo.TaoRefundBo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【tao_refund(淘宝退款订单表)】的数据库操作Service
* @createDate 2024-02-29 19:01:45
*/
public interface TaoRefundService extends IService<TaoRefund> {
    PageResult<TaoRefund> queryPageList(TaoRefundBo bo, PageQuery pageQuery);
    int saveAndUpdateRefund(Long shopId,TaoRefund refund);
}
