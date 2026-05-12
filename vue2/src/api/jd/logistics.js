import request from '@/utils/request'


export function pullLogisticsJd(data) {
  return request({
    url: '/api/oms-api/jd/logistics/pull_logistics_companies',
    method: 'post',
    data: data
  })
}

