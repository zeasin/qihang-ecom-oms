package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.bo.PosOrderCreateBo;
import cn.qihangerp.model.bo.ShopOrderCreateBo;
import cn.qihangerp.model.entity.ShopOrder;
import cn.qihangerp.model.entity.ShopOrderItem;
import cn.qihangerp.model.query.ShopOrderQueryBo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【oms_shop_order】的数据库操作Service
* @createDate 2025-07-15 11:36:42
*/
public interface ShopOrderService extends IService<ShopOrder> {

    PageResult<ShopOrder> queryOrderPageList(ShopOrderQueryBo bo, PageQuery pageQuery);
    ShopOrder queryDetailById(Long orderId);
    ShopOrder queryDetailByOrderNo(String orderNo);
    List<ShopOrderItem> queryOrderItemList(Long shopOrderId);

    /**
     * 更新店铺物流信息
     * @param shopOrderNo
     * @param tradeNo
     * @param logisticCompany
     * @param logisticCode
     * @param consignTime
     * @return
     */
    ResultVo<Long> updateShopOrderLogistic(String shopOrderNo, String tradeNo, String logisticCompany, String logisticCode, Date consignTime);
    /**
     * 保存店铺订单
     * @param shopId
     * @param order
     * @return
     */
    ResultVo<Long> saveOrder(Long shopId,Long merchantId,Integer shopType, ShopOrder order);


    /**
     * 取消订单
     * @param id 店铺订单id
     * @param cancelReason 取消原因
     * @param man 操作人
     * @return
     */
    ResultVo cancelOrder(Long id,String cancelReason,String man);

    /**
     * 创建订单
     * @param orderCreateBo
     * @param man
     * @return
     */
    ResultVo<Long> createOrder(ShopOrderCreateBo orderCreateBo, String man);


    /**
     * 快递单号查询发货记录
     * @param logisticsNo
     * @return
     */
    ShopOrder  queryByLogisticsNo(String logisticsNo);

}