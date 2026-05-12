import request from '@/utils/request'

// 查询店铺订单列表
export function listXhsRefund(query) {
  return request({
    url: '/api/oms-api/xhs/refund/list',
    method: 'get',
    params: query
  })
}

// 接口拉取订单
export function pullXhsRefund(data) {
  return request({
    url: '/api/oms-api/xhs/refund/pull_list',
    method: 'post',
    data: data
  })
}

// 拉取详情
export function pullXhsRefundDetail(data) {
  return request({
    url: '/api/oms-api/xhs/refund/pull_detail',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/api/oms-api/xhs/refund/push_oms',
    method: 'post',
    data: data
  })
}
