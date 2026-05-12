import request from '@/utils/request'

// 获取京东授权url
export function getJdOAuthUrl(query) {
  return request({
    url: '/api/oms-api/jd/oauth',
    method: 'get',
    params: query
  })
}

// 获取京东token
export function getJdToken(data) {
  return request({
    url: '/api/oms-api/jd/tokenCreate',
    method: 'post',
    data: data
  })
}

// export function pullLogisticsJd(data) {
//   return request({
//     url: '/api/oms-api/jd/shopApi/pull_logistics_companies',
//     method: 'post',
//     data: data
//   })
// }
//
// export function pullLogisticsTemplateListJd(data) {
//   return request({
//     url: '/api/oms-api/jd/shopApi/pullTemplateList',
//     method: 'post',
//     data: data
//   })
// }
