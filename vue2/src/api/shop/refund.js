import request from '@/utils/request'

// 查询店铺订单列表
export function listShopRefund(query) {
  return request({
    url: '/api/oms-api/shop/refund/list',
    method: 'get',
    params: query
  })
}

// 拉取退款
export function pullRefund(data) {
  return request({
    url: '/api/oms-api/shop/refund/pull_list',
    method: 'post',
    data: data
  })
}

// 拉取详情
export function pullOrderDetail(data) {
  return request({
    url: '/api/oms-api/shop/refund/pull_detail',
    method: 'post',
    data: data
  })
}

//推送到oms
export function pushOms(data) {
  return request({
    url: '/api/oms-api/shop/refund/push_oms',
    method: 'post',
    data: data
  })
}
// 添加店铺订单售后
export function addShopRefund(data) {
  return request({
    url: '/api/oms-api/shop/refund/addRefund',
    method: 'post',
    data:data
  })
}

// 批量确认订单
export function returnedConfirm(id) {
  return request({
    url: '/shop/refund/returnedConfirm/'+id,
    method: 'put',
  })
}

export function orderIntercept(id) {
  return request({
    url: '/shop/refund/orderIntercept/'+id,
    method: 'put',
  })
}

