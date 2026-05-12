import request from '@/utils/request'

// 查询仓库货架列表
export function listPosition(query) {
  return request({
    url: '/api/erp-api/warehouse/position/list',
    method: 'get',
    params: query
  })
}

// 查询仓库货架详细
export function getLocation(id) {
  return request({
    url: '/api/erp-api/warehouse/position/' + id,
    method: 'get'
  })
}

// 新增仓库货架
export function addLocation(data) {
  return request({
    url: '/api/erp-api/warehouse/position',
    method: 'post',
    data: data
  })
}

// 修改仓库货架
export function updateLocation(data) {
  return request({
    url: '/api/erp-api/warehouse/position',
    method: 'put',
    data: data
  })
}

// 删除仓库货架
export function delLocation(id) {
  return request({
    url: '/api/erp-api/warehouse/position/' + id,
    method: 'delete'
  })
}
