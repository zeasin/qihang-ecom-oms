package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.entity.PddOrder;
import cn.qihangerp.model.bo.PddOrderBo;
import cn.qihangerp.model.bo.PddOrderConfirmBo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【pdd_order(拼多多订单表)】的数据库操作Service
* @createDate 2024-06-05 10:58:43
*/
public interface PddOrderService extends IService<PddOrder> {
    PageResult<PddOrder> queryPageList(PddOrderBo bo, PageQuery pageQuery);

    PddOrder queryDetailById(Long id);
    PddOrder queryDetailBySn(String orderSn);
    /**
     * 保存店铺订单
     * @param shopId
     * @param order
     * @return
     */
    ResultVo<Integer> saveOrder(Long shopId, PddOrder order);

    ResultVo<Long> confirmOrder(PddOrderConfirmBo confirmBo);
}
