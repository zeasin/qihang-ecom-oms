package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.ErpBillShopOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_shop_bill(店铺账单)】的数据库操作Service
* @createDate 2025-05-26 11:05:47
*/
public interface ErpBillShopOrderService extends IService<ErpBillShopOrder> {
    PageResult<ErpBillShopOrder> queryPageList(ErpBillShopOrder bo, PageQuery pageQuery);
}
