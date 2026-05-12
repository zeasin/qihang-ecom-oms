import request from '@/utils/request'

// 查询店铺订单列表
export function listShopRefund(query) {
  return request({
    url: '/api/oms-api/wei/refund/list',
    method: 'get',
    params: query
  })
}

// 接口拉取订单
export function pullRefund(data) {
  return request({
    url: '/api/oms-api/wei/refund/pull_list',
    method: 'post',
    data: data
  })
}

// 拉取详情
export function pullOrderDetail(data) {
  return request({
    url: '/api/oms-api/wei/refund/pull_detail',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/api/oms-api/wei/refund/push_oms',
    method: 'post',
    data: data
  })
}
