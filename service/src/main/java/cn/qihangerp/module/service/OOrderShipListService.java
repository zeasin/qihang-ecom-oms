package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.bo.ShipStockUpBo;
import cn.qihangerp.model.entity.OOrderShipList;

import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【o_order_ship_list(发货-备货表（取号发货加入备货清单、分配供应商发货加入备货清单）)】的数据库操作Service
* @createDate 2025-06-01 23:07:24
*/
public interface OOrderShipListService extends IService<OOrderShipList> {
    PageResult<OOrderShipList> querySupplierPageList(ShipStockUpBo bo, PageQuery pageQuery);
    PageResult<OOrderShipList> queryWarehousePageList(ShipStockUpBo bo, PageQuery pageQuery);
}
