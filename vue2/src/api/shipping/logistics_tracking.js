import request from '@/utils/request'

// 查询物流轨迹
export function wuliuguiji(query) {
  return request({
    url: '/api/erp-api/ship/logistics_tracking/wuliuguiji',
    method: 'get',
    params: query
  })
}

