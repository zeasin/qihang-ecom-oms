import request from '@/utils/request'

// 查询出库单列表
export function listStockOut(query) {
  return request({
    url: '/api/erp-api/stockOut/list',
    method: 'get',
    params: query
  })
}

// 出库报表
export function listStockReport(query) {
  return request({
    url: '/api/erp-api/stockOut/report',
    method: 'get',
    params: query
  })
}

// 查询出库单详细
export function getStockOutEntry(id) {
  return request({
    url: '/api/erp-api/stockOut/' + id,
    method: 'get'
  })
}

// export function getStockOutEntryItem(id) {
//   return request({
//     url: '/wms/stockOutEntry/item/' + id,
//     method: 'get'
//   })
// }

// 创建出库单
export function stockOutCreate(data) {
  return request({
    url: '/api/erp-api/stockOut/create',
    method: 'post',
    data: data
  })
}
// 出库
export function stockOut(data) {
  return request({
    url: '/api/erp-api/stockOut/out',
    method: 'post',
    data: data
  })
}

