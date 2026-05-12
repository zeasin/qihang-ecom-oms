import request from '@/utils/request'

// 首页今日销售统计
export function todayDaily(query) {
  return request({
    url: '/api/wms-api/report/todayDaily',
    method: 'get',
    params: query
  })
}




