import request from '@/utils/request'

// 查询店铺订单列表
export function listShopOrder(query) {
  return request({
    url: '/api/oms-api/shop/order/list',
    method: 'get',
    params: query
  })
}

// 查询店铺订单明细列表
export function listShopOrderItem(query) {
  return request({
    url: '/api/oms-api/shop/order/item_list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getShopOrder(id) {
  return request({
    url: '/api/oms-api/shop/order/' + id,
    method: 'get'
  })
}

// 接口拉取订单
export function pullOrder(data) {
  return request({
    url: '/api/oms-api/shop/order/pull_list',
    method: 'post',
    data: data
  })
}

// 接口拉取订单
export function pullKcOrder(data) {
  return request({
    url: '/api/oms-api/shop/order/pull_list',
    method: 'post',
    data: data
  })
}

export function pullScrmOrder(data) {
  return request({
    url: '/api/oms-api/shop/order/pull_tl_order_scrm',
    method: 'post',
    data: data
  })
}

// 拉取订单详情
export function pullOrderDetail(data) {
  return request({
    url: '/api/oms-api/shop/order/pull_detail',
    method: 'post',
    data: data
  })
}
// 新增店铺订单
export function createShopOrder(data) {
  return request({
    url: '/api/oms-api/shop/order/create',
    method: 'post',
    data: data
  })
}
// 批量确认订单
export function orderPushOms(data) {
  return request({
    url: '/api/oms-api/shop/order/push_oms',
    method: 'post',
    data: data
  })
}

// 取消订单
export function cancelOrder(data) {
  return request({
    url: '/api/oms-api/shop/order/cancelOrder',
    method: 'post',
    data: data
  })
}

// POS手工创建订单
export function offlineOrderCreate(data) {
  return request({
    url: '/api/oms-api/shop/order/offlineOrderCreate',
    method: 'post',
    data: data
  })
}