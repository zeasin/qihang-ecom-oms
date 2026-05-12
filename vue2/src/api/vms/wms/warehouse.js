import request from '@/utils/request'

// 查询仓库货架列表
export function listWarehouse(query) {
  return request({
    url: '/api/wms-api/warehouse/list',
    method: 'get',
    params: query
  })
}
export function searchPosition(query) {
  return request({
    url: '/api/wms-api/warehouse/position/search',
    method: 'get',
    params: query
  })
}


// 查询仓库货架详细
export function getLocation(id) {
  return request({
    url: '/api/wms-api/warehouse/' + id,
    method: 'get'
  })
}

// 新增仓库货架
export function addLocation(data) {
  return request({
    url: '/api/wms-api/warehouse',
    method: 'post',
    data: data
  })
}

// 修改仓库货架
export function updateLocation(data) {
  return request({
    url: '/api/wms-api/warehouse',
    method: 'put',
    data: data
  })
}

// 删除仓库货架
export function delLocation(id) {
  return request({
    url: '/api/wms-api/warehouse/' + id,
    method: 'delete'
  })
}
