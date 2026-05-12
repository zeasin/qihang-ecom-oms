import request from '@/utils/request'


// 接口拉取淘宝
export function pullLogisticsTao(data) {
  return request({
    url: '/api/oms-api/tao/shopApi/pull_logistics_companies',
    method: 'post',
    data: data
  })
}

export function pullLogisticsTemplateListTao(data) {
  return request({
    url: '/api/oms-api/tao/shopApi/pullTemplateList',
    method: 'post',
    data: data
  })
}


export function getTaoOAuthUrl(query) {
  return request({
    url: '/api/oms-api/tao/oauth',
    method: 'get',
    params: query
  })
}
export function saveSessionKey(data) {
  return request({
    url: '/api/oms-api/tao/saveSessionKey',
    method: 'post',
    data: data
  })
}
