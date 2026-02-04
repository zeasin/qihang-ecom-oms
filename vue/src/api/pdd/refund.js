import request from '@/utils/request'

// 查询退款列表
export function listRefund(query) {
  return request({
    url: '/pdd/refund/list',
    method: 'get',
    params: query
  })
}


export function pullRefund(data) {
  return request({
    url: '/pdd/refund/pull_list',
    method: 'post',
    data: data
  })
}
export function refundHandle(data) {
  return request({
    url: '/pdd/refund/handle',
    method: 'post',
    data:data
  })
}
