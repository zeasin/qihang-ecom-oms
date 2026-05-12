import request from '@/utils/request'

export function getXhsOAuthUrl(query) {
  return request({
    url: '/api/oms-api/xhs/getOauthUrl',
    method: 'get',
    params: query
  })
}

export function getXhsToken(data) {
  return request({
    url: '/api/oms-api/xhs/getToken',
    method: 'post',
    data: data
  })
}
// 接口
export function pullLogisticsCompanyListXhs(data) {
  return request({
    url: '/api/oms-api/xhs/logistics/pull_logistics_company',
    method: 'post',
    data: data
  })
}
export function pullLogisticsTemplateListXhs(data) {
  return request({
    url: '/api/oms-api/wei/logistics/pullTemplateList',
    method: 'post',
    data: data
  })
}
