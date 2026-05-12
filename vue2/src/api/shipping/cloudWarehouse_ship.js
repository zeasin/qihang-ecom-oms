import request from '@/utils/request'


export function listCloudWarehouseShipOrder(query) {
  return request({
    url: '/api/erp-api/ship/cloudWarehouse/push_list',
    method: 'get',
    params: query
  })
}

// 重新推送到云仓发货
// export function pushAgainToCloudWarehouse(data) {
//   return request({
//     url: '/api/erp-api/ship/cloudWarehouse/pushAgainToCloudWarehouse',
//     method: 'post',
//     data: data
//   })
// }
