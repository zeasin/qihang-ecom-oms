import request from '@/utils/request'

// 查询店铺订单列表
export function listOrder(query) {
  return request({
    url: '/api/erp-api/order/list',
    method: 'get',
    params: query
  })
}


// 查询待分配发货订单列表
export function waitDistOrderList(query) {
  return request({
    url: '/api/erp-api/order/wait_dist_order_list',
    method: 'get',
    params: query
  })
}

// 查询待分配发货订单列表
export function waitDistOrderItemList(query) {
  return request({
    url: '/api/erp-api/order/wait_dist_order_item_list',
    method: 'get',
    params: query
  })
}

// 查询待自己发货的订单列表(已发货的)
export function selfShippedList(query) {
  return request({
    url: '/api/erp-api/order/selfShippedList',
    method: 'get',
    params: query
  })
}

// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/api/erp-api/order/' + id,
    method: 'get'
  })
}

// 按订单号查询订单详细
export function getOrderByOrderNum(orderNum) {
  return request({
    url: '/api/erp-api/order/detail',
    method: 'get',
    params: { orderNum }
  })
}

// 订单明细list
export function listOrderItem(query) {
  return request({
    url: '/api/erp-api/order/item_list',
    method: 'get',
    params: query
  })
}
// 修改订单item skuId
export function updateErpSkuId(data) {
  return request({
    url: '/api/erp-api/order/updateErpSkuId',
    method: 'post',
    data: data
  })
}

// 修改发货供应商
export function updateShipSupplierId(data) {
  return request({
    url: '/api/erp-api/order/updateShipSupplierId',
    method: 'post',
    data: data
  })
}


// 指定发货人
export function updateShipperId(data) {
  return request({
    url: '/api/erp-api/order/updateShipperId',
    method: 'post',
    data: data
  })
}

export function pushErp(id) {
  return request({
    url: '/api/erp-api/order/pushErp/' + id,
    method: 'post'
  })
}

// 待发货统计
export function waitOrderReport(query) {
  return request({
    url: '/api/erp-api/ship/report/wait_order_report',
    method: 'get',
    params: query
  })
}
