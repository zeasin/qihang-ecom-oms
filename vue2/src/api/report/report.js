import request from '@/utils/request'

// 首页今日销售统计
export function todayDaily(query) {
  return request({
    url: '/api/erp-api/report/todayDaily',
    method: 'get',
    params: query
  })
}

// 销售统计
export function salesDaily(query) {
  return request({
    url: '/api/erp-api/report/salesDaily',
    method: 'get',
    params: query
  })
}



export function listRegionDaily(query) {
  return request({
    url: '/api/erp-api/shop/daily/regionReport',
    method: 'get',
    params: query
  })
}
