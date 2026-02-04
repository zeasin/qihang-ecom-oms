import request from '@/utils/request'

// 查询退款列表
export function listRefund(query) {
  return request({
    url: '/dou/refund/list',
    method: 'get',
    params: query
  })
}


export function pullRefund(data) {
  return request({
    url: '/dou/refund/pull_list',
    method: 'post',
    data: data
  })
}


