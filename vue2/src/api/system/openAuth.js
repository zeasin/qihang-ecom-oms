import request from '@/utils/request'

// 查询列表
export function listOpenAuth(query) {
  return request({
    url: '/api/sys-api/openAuth/list',
    method: 'get',
    params: query
  })
}


// 查询详细
export function getDetail(id) {
  return request({
    url: '/api/sys-api/openAuth/detail/' + id,
    method: 'get'
  })
}

// 新增
export function addOpenAuth(data) {
  return request({
    url: '/api/sys-api/openAuth/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateOpenAuth(data) {
  return request({
    url: '/api/sys-api/openAuth/edit',
    method: 'put',
    data: data
  })
}

// 删除店铺
export function delShop(id) {
  return request({
    url: '/api/sys-api/openAuth/del/' + id,
    method: 'delete'
  })
}
