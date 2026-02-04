import request from '@/utils/request'

// 查询售后列表
export function listRefund(query) {
  return request({
    url: '/jd/after/list',
    method: 'get',
    params: query
  })
}


export function pullRefund(data) {
  return request({
    url: '/jd/refund/pull_list',
    method: 'post',
    data: data
  })
}

