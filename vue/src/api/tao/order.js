import request from '@/utils/request'

// 查询淘宝订单列表
export function listOrder(query) {
  return request({
    url: '/tao/order/list',
    method: 'get',
    params: query
  })
}

// 查询淘宝订单详细
export function getOrder(id) {
  return request({
    url: '/tao/order/' + id,
    method: 'get'
  })
}


// 接口拉取淘宝订单
export function pullOrder(data) {
  return request({
    url: '/tao/order/pull_order_tao',
    method: 'post',
    data: data
  })
}

export function pullOrderDetail(data) {
  return request({
    url: '/tao/order/pull_order_detail',
    method: 'post',
    data: data
  })
}

// 确认订单
export function confirmOrder(data) {
  return request({
    url: '/tao/order/confirmOrder',
    method: 'post',
    data: data
  })
}
