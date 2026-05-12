import request from '@/utils/request'

export function getKsOAuthUrl(query) {
  return request({
    url: '/api/oms-api/ks/getOauthUrl',
    method: 'get',
    params: query
  })
}

export function getKsToken(data) {
  return request({
    url: '/api/oms-api/ks/getToken',
    method: 'post',
    data: data
  })
}
