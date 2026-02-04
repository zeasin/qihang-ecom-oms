package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.bo.OrderItemListBo;
import cn.qihangerp.model.entity.OOrderItem;
import cn.qihangerp.model.vo.OrderItemListVo;
import cn.qihangerp.model.vo.SalesTopSkuVo;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
* @author TW
* @description 针对表【o_order_item(订单明细表)】的数据库操作Service
* @createDate 2024-03-11 14:24:49
*/
public interface OOrderItemService extends IService<OOrderItem> {
    PageResult<OrderItemListVo> selectPageVo(PageQuery pageQuery, OrderItemListBo bo);
    PageResult<OOrderItem> queryPageList(OOrderItem bo, PageQuery pageQuery);

    ResultVo<Integer>  updateErpSkuId(Long orderItemId,Long erpSkuId);

    List<OOrderItem> getOrderItemListByOrderId(Long orderId);
    List<SalesTopSkuVo> selectTopSku(String startDate, String endDate);
}
