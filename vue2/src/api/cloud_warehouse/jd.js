import request from '@/utils/request'

// 京东云仓仓库list
export function getJdCloudWarehouseList(query) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/list',
    method: 'get',
    params: query
  })
}


export function getJdCloudWarehouseConfig() {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/systemConfig',
    method: 'get',
  })
}


export function saveJdCloudWarehouseConfig(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/systemConfig/save',
    method: 'post',
    data: data
  })
}




// 查询京东云仓商品列表
// export function listJdCloudWarehouseGoodsItems(query) {
//   return request({
//     url: '/api/erp-api/cloudWarehouse/jd/goods/item_list',
//     method: 'get',
//     params: query
//   })
// }
//

// 推送采购入库单到京东云仓
export function pushJdCloudWarehousePurchase(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/purchase_push',
    method: 'post',
    data: data
  })
}




// 获取JD云仓库存
export function getJdlCloudWarehouseStockList(query) {
  return request({
    url: '/api/erp-api/cloudWarehouse/stock/jdl_list',
    method: 'get',
    params: query
  })
}
