import request from '@/utils/request'

// 查询店铺平台列表
export function listGroup(query) {
  return request({
    url: '/api/oms-api/shop/group/list',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getGroup(id) {
  return request({
    url: '/api/oms-api/shop/group/' + id,
    method: 'get'
  })
}

// 修改店铺
export function updateGroup(data) {
  return request({
    url: '/api/oms-api/shop/group',
    method: 'put',
    data: data
  })
}



export function addGroup(data) {
  return request({
    url: '/api/oms-api/shop/group',
    method: 'post',
    data: data
  })
}
