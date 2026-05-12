import request from '@/utils/request'
//TODO:准备作废
// 查询店铺订单列表
export function listOrder(query) {
  return request({
    url: '/api/erp-api/ship/order/list',
    method: 'get',
    params: query
  })
}

// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/api/erp-api/ship/order/' + id,
    method: 'get'
  })
}

// 订单明细list
export function listOrderItem(query) {
  return request({
    url: '/api/erp-api/ship/order/item_list',
    method: 'get',
    params: query
  })
}

// 查询今日取号list
export function todayWaybillList(query) {
  return request({
    url: '/api/erp-api//ship/order/today_waybill_list',
    method: 'get',
    params: query
  })
}

