import request from '@/utils/request'

// 查询退换货列表
export function listReturned(query) {
  return request({
    url: '/api/vms-api/salesAfter/list',
    method: 'get',
    params: query
  })
}

// 查询退换货详细
export function getReturned(id) {
  return request({
    url: '/api/vms-api/salesAfter/' + id,
    method: 'get'
  })
}


export function refundProcessing(data) {
  return request({
    url: '/api/vms-api/salesAfter/processing',
    method: 'post',
    data:data
  })
}
