import request from '@/utils/request'

// 查询店铺平台列表
export function listDaily(query) {
  return request({
    url: '/api/erp-api/shop/daily/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getDaily(id) {
  return request({
    url: '/api/erp-api/shop/dailyDetail/' + id,
    method: 'get'
  })
}

// 修改
export function updateDaily(data) {
  return request({
    url: '/api/erp-api/shop/daily',
    method: 'put',
    data: data
  })
}

export function delDaily(id) {
  return request({
    url: '/api/erp-api/shop/dailyDel/' + id,
    method: 'delete'
  })
}

// 新增司
export function addDaily(data) {
  return request({
    url: '/api/erp-api/shop/daily',
    method: 'post',
    data: data
  })
}

