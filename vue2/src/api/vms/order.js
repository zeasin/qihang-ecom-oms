import request from '@/utils/request'

// 查询店铺订单列表
export function listOrder(query) {
  return request({
    url: '/api/wms-api/ship/order/list',
    method: 'get',
    params: query
  })
}
//补打单list

export function budadanListOrder(query) {
  return request({
    url: '/api/wms-api/ship/order/budadanList',
    method: 'get',
    params: query
  })
}
// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/api/wms-api/ship/order/' + id,
    method: 'get'
  })
}

// 订单明细list
export function listOrderItem(query) {
  return request({
    url: '/api/wms-api/ship/order/item_list',
    method: 'get',
    params: query
  })
}


export function updateSendStatus(ids) {
  return request({
    url: '/api/wms-api/ship/order/updateSendStatus',
    method: 'post',
    data:ids
  })
}
