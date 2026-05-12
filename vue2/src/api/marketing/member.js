import request from '@/utils/request'

// 查询列表
export function listShopMember(query) {
  return request({
    url: '/api/oms-api/shop/member/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getShopMember(id) {
  return request({
    url: '/api/oms-api/shop/member/' + id,
    method: 'get'
  })
}

// 搜索详细
export function searchShopMember(params) {
  return request({
    url: '/api/oms-api/shop/member/search',
    method: 'get',
    params: params
  })
}

// 修改
export function updateShopMember(data) {
  return request({
    url: '/api/oms-api/shop/member/edit',
    method: 'put',
    data: data
  })
}

// 新增
export function addShopMember(data) {
  return request({
    url: '/api/oms-api/shop/member/add',
    method: 'post',
    data: data
  })
}
