import request from '@/utils/request'

// 查询
export function listDaily(query) {
  return request({
    url: '/api/erp-api/goodsSale/report',
    method: 'get',
    params: query
  })
}


export function listRegionDaily(query) {
  return request({
    url: '/api/erp-api/goodsSale/regionReport',
    method: 'get',
    params: query
  })
}
