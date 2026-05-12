import request from '@/utils/request'

// 查询退换货列表
export function listSalesAfter(query) {
  return request({
    url: '/api/wms-api/salesAfter/list',
    method: 'get',
    params: query
  })
}

// 查询退换货详细
export function getSalesAfter(id) {
  return request({
    url: '/api/wms-api/salesAfter/' + id,
    method: 'get'
  })
}


export function salesAfterProcessing(data) {
  return request({
    url: '/api/wms-api/salesAfter/processing',
    method: 'post',
    data:data
  })
}
