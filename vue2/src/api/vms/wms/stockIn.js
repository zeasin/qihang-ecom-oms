import request from '@/utils/request'

// 查询入库单列表
export function listStockIn(query) {
  return request({
    url: '/api/wms-api/stockIn/list',
    method: 'get',
    params: query
  })
}

// 查询入库单详细
export function getWmsStockInEntry(id) {
  return request({
    url: '/api/wms-api/stockIn/' + id,
    method: 'get'
  })
}

// 新增入库单
export function stockInCreate(data) {
  return request({
    url: '/api/wms-api/stockIn/create',
    method: 'post',
    data: data
  })
}
export function stockIn(data) {
  return request({
    url: '/api/wms-api/stockIn/in',
    method: 'post',
    data: data
  })
}
// export function complete(id) {
//   return request({
//     url: '/wms/WmsStockInEntry/complete/' + id,
//     method: 'get'
//   })
// }

// 查询商户入库单列表
export function listMerchantStockIn(query) {
  return request({
    url: '/api/wms-api/merchant/stock_in_list',
    method: 'get',
    params: query
  })
}
// 查询入库单详细
export function getMerchantStockInEntry(id) {
  return request({
    url: '/api/wms-api/merchant/stock_in/' + id,
    method: 'get'
  })
}
export function merchantStockInAudit(data) {
  return request({
    url: '/api/wms-api/merchant/stock_in_audit',
    method: 'post',
    data: data
  })
}
