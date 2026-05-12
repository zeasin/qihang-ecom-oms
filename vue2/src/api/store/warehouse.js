import request from '@/utils/request'

// 查询仓库列表
// export function listLocalWarehouse(query) {
//   return request({
//     url: '/api/erp-api/warehouse/my_local_warehouse_list',
//     method: 'get',
//     params: query
//   })
// }
// 我的本地仓
export function listMyLocalWarehouse(query) {
  return request({
    url: '/api/erp-api/warehouse/merchant_local_warehouse_list',
    method: 'get',
    params: query
  })
}
// 查询商户 云仓list
export function listMerchantCloudWarehouse(query) {
  return request({
    url: '/api/erp-api/warehouse/merchant_cloud_warehouse_list',
    method: 'get',
    params: query
  })
}
// 我的所有仓库（云仓、本地仓）
export function listMyAllWarehouse(query) {
  return request({
    url: '/api/erp-api/warehouse/merchant_warehouse_list',
    method: 'get',
    params: query
  })
}
