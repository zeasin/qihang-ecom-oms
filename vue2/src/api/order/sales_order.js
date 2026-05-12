import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/api/erp-api/sales/order/list',
    method: 'get',
    params: query
  })
}
// 查询订单列表
export function searchSaleOrder(query) {
  return request({
    url: '/api/erp-api/sales/order/search',
    method: 'get',
    params: query
  })
}
// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/api/erp-api/sales/order/' + id,
    method: 'get'
  })
}

// 新增店铺订单
export function addOrder(data) {
  return request({
    url: '/api/erp-api/sales/order/create',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/api/erp-api/sales/order/push_oms',
    method: 'post',
    data: data
  })
}

// 取消订单
export function cancelOrder(data) {
  return request({
    url: '/api/erp-api/sales/order/cancelOrder',
    method: 'post',
    data: data
  })
}
