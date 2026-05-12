import request from '@/utils/request'

// 备货清单
export function listShipStockup(query) {
  return request({
    url: '/api/erp-api/ship/stocking/stock_up_list',
    method: 'get',
    params: query
  })
}

// 备货商品清单
export function listShipStockupItem(query) {
  return request({
    url: '/api/erp-api/ship/stocking/stock_up_item_list',
    method: 'get',
    params: query
  })
}

// 生成出库单
export function generateStockOutByItem(data) {
  return request({
    url: '/api/erp-api/ship/stocking/generateStockOutByItem',
    method: 'post',
    data: data
  })
}
// 备货完成by order
export function shipStockupCompleteByOrder(data) {
  return request({
    url: '/api/erp-api/ship/stocking/generateStockOutByOrder',
    method: 'post',
    data: data
  })
}
