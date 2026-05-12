import request from '@/utils/request'

// 查询店铺平台列表
export function listRegion(query) {
  return request({
    url: '/api/oms-api/shop/region/list',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getRegion(id) {
  return request({
    url: '/api/oms-api/shop/region/' + id,
    method: 'get'
  })
}

// 修改店铺
export function updateRegion(data) {
  return request({
    url: '/api/oms-api/shop/region',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/api/oms-api/shop/region/changeStatus',
    method: 'put',
    data: data
  })
}

export function addRegion(data) {
  return request({
    url: '/api/oms-api/shop/region',
    method: 'post',
    data: data
  })
}
