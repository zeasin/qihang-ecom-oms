import request from '@/utils/request'

// 查询售后列表
export function listRefund(query) {
  return request({
    url: '/api/oms-api/jd/after/list',
    method: 'get',
    params: query
  })
}


export function pullRefund(data) {
  return request({
    url: '/api/oms-api/jd/refund/pull_list',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/api/oms-api/jd/after/push_oms',
    method: 'post',
    data: data
  })
}
