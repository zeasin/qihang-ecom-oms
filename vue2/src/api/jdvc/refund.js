import request from '@/utils/request'

// 查询淘宝退款订单列表
export function listRefund(query) {
  return request({
    url: '/api/oms-api/jdvc/refund/list',
    method: 'get',
    params: query
  })
}


export function pullRefund(data) {
  return request({
    url: '/api/oms-api/jdvc/refund/pull_list',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/api/oms-api/jdvc/refund/push_oms',
    method: 'post',
    data: data
  })
}
