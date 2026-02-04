package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.entity.TaoOrder;
import cn.qihangerp.model.bo.TaoOrderBo;
import cn.qihangerp.model.bo.TaoOrderConfirmBo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【tao_order(淘宝订单表)】的数据库操作Service
* @createDate 2024-02-29 19:01:11
*/
public interface TaoOrderService extends IService<TaoOrder> {

    PageResult<TaoOrder> queryPageList(TaoOrderBo bo, PageQuery pageQuery);

    TaoOrder queryDetailById(Long id);
    TaoOrder queryDetailByTid(String tid);
    /**
     * 保存店铺订单
     * @param shopId
     * @param order
     * @return
     */
    ResultVo<Long> saveOrder(Long shopId, TaoOrder order);
    ResultVo<Integer> updateOrder(TaoOrder order);

    ResultVo<Long> confirmOrder(TaoOrderConfirmBo confirmBo);

    /**
     * 手动发货
     * @param shipBo
     * @return
     */
//    ResultVo<Integer> manualShipmentOrder(ShopOrderShipBo shipBo, String createBy);
}
