import request from '@/utils/request'


// 拼多多授权回调
export function pddOauthLogin(data) {
  return request({
    url: '/api/sys-api/oauth/pdd_callback',
    method: 'post',
    data: data
  })
}

// 获取点三授权url
export function getDiansanAuthUrl(query) {
  return request({
    url: '/api/oms-api/oauth/diansan',
    method: 'get',
    params: query
  })
}
