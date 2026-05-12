import request from '@/utils/request'

// 读取飞书table数据
export function queryFeiShuDocs(query) {
  return request({
    url: '/api/open/feishu/queryDocs',
    method: 'get',
    params: query
  })
}

// 保存数据到飞书
export function saveFeiShuDocs(data) {
  return request({
    url: '/api/open/feishu/saveDocs',
    method: 'post',
    data: data
  })
}

export function getFeiShuOAuthUrl(query) {
  return request({
    url: '/api/open/feishu/getOauthUrl',
    method: 'get',
    params: query
  })
}
export function getUserToken(query) {
  return request({
    url: '/api/open/feishu/getUserToken',
    method: 'get',
    params: query
  })
}
