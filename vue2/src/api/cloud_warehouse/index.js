import request from '@/utils/request'

export function getCloudWarehouseList(query) {
  return request({
    url: '/api/erp-api/warehouse/cloud_list',
    method: 'get',
    params: query
  })
}

//获取推送日志
export function getOrderPushList(query) {
  return request({
    url: '/api/erp-api/push/logs/order/push_list',
    method: 'get',
    params: query
  })
}



// 接口拉取云仓库存
export function pullCloudWarehouseInventory(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/stock/pull_goods_inventory',
    method: 'post',
    data: data
  })
}

// 查询云仓订单（包括：jdl出库单、吉客云销售单）
export function queryCloudWarehouseOrder(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/order/query',
    method: 'post',
    data: data
  })
}

// 添加jdl承运商
export function addWarehouseShipper(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/addShipper',
    method: 'post',
    data: data
  })
}

// 查询云仓店铺列表
export function listCloudWarehouseShop(query) {
  return request({
    url: '/api/erp-api/cloudWarehouse/shop_list',
    method: 'get',
    params: query
  })
}
// 查询云仓承运商list
export function listCloudWarehouseShipper(query) {
  return request({
    url: '/api/erp-api/cloudWarehouse/shipper_list',
    method: 'get',
    params: query
  })
}


// 推送采购入库单到三方云仓
export function pushCloudWarehousePurchase(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/purchase_stock_in_push',
    method: 'post',
    data: data
  })
}
