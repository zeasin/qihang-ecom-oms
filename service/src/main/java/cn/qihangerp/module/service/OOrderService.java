package cn.qihangerp.module.service;

import cn.qihangerp.model.bo.OrderAllocateShipRequest;
import cn.qihangerp.model.bo.OrderShipRequest;
import cn.qihangerp.model.entity.OOrder;
import cn.qihangerp.model.vo.SalesDailyVo;
import cn.qihangerp.request.OrderSearchRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;



import java.util.List;

/**
* @author qilip
* @description 针对表【o_order(订单表)】的数据库操作Service
* @createDate 2024-03-09 13:15:57
*/
public interface OOrderService extends IService<OOrder> {

    PageResult<OOrder> queryPageList(OrderSearchRequest bo, PageQuery pageQuery);
    /**
     * 获取待发货list（去除处理过的）
     * @param bo
     * @param pageQuery
     * @return
     */
    PageResult<OOrder> queryWaitShipmentPageList(OrderSearchRequest bo, PageQuery pageQuery);



    OOrder queryDetailById(Long id);

    /**
     * 手动添加订单
     * @param
     * @return
     */
//    int insertErpOrder(OrderCreateBo bo,String createBy);

    List<SalesDailyVo> salesDaily();
    SalesDailyVo getTodaySalesDaily();
    Integer getWaitShipOrderAllCount();
    /**
     * 手动发货
     * @param shipBo
     * @return
     */
    ResultVo<Integer> manualShipmentOrder(OrderShipRequest shipBo, String createBy);

    /**
     * 分配给供应商发货
     * @param shipBo
     * @param createBy
     * @return
     */
    ResultVo<Integer> allocateShipmentOrder(OrderAllocateShipRequest shipBo, String createBy);

    /**
     * 取消订单
     * @param id 店铺订单id
     * @param cancelReason 取消原因
     * @param man 操作人
     * @return
     */
    ResultVo cancelOrder(Long id, String cancelReason, String man);

    /**
     * 取消子订单
     * @param orderItemId
     * @param cancelReason
     * @param man
     * @return
     */
    ResultVo cancelOrderItem(Long orderItemId, String cancelReason, String man);
}
