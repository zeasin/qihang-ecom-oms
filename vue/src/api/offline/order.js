import request from '@/utils/request'

// 查询店铺订单列表
export function listOrder(query) {
  return request({
    url: '/offline_order/list',
    method: 'get',
    params: query
  })
}

// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/offline_order/' + id,
    method: 'get'
  })
}

// 新增店铺订单
export function addOrder(data) {
  return request({
    url: '/offline_order/create',
    method: 'post',
    data: data
  })
}

// 确认抖店订单
export function confirmOrder(data) {
  return request({
    url: '/offline_order/confirmOrder',
    method: 'post',
    data: data
  })
}

