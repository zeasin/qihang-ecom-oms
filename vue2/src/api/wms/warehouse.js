import request from '@/utils/request'

// 查询仓库货架列表
export function listWarehouse(query) {
  return request({
    url: '/api/erp-api/warehouse/list',
    method: 'get',
    params: query
  })
}

// 所有可用（仅总部查询）
export function myAvailableList(query) {
  return request({
    url: '/api/erp-api/warehouse/my_available_list',
    method: 'get',
    params: query
  })
}

// 所有云仓（仅总部查询）
export function listCloudWarehouse(query) {
  return request({
    url: '/api/erp-api/warehouse/cloud_list',
    method: 'get',
    params: query
  })
}

export function searchPosition(query) {
  return request({
    url: '/api/erp-api/warehouse/position/search',
    method: 'get',
    params: query
  })
}


// 查询仓库货架详细
export function getLocation(id) {
  return request({
    url: '/api/erp-api/warehouse/' + id,
    method: 'get'
  })
}

// 新增仓库货架
export function addLocation(data) {
  return request({
    url: '/api/erp-api/warehouse',
    method: 'post',
    data: data
  })
}

// 修改仓库货架
export function updateLocation(data) {
  return request({
    url: '/api/erp-api/warehouse',
    method: 'put',
    data: data
  })
}

// 删除仓库货架
export function delLocation(id) {
  return request({
    url: '/api/erp-api/warehouse/' + id,
    method: 'delete'
  })
}

// 仓库分配供应商
export function shareMerchant(data) {
  return request({
    url: '/api/erp-api/warehouse/shareMerchant',
    method: 'post',
    data: data
  })
}
export function setWarehouseLoginName(data) {
  return request({
    url: '/api/erp-api/warehouse/setLoginName',
    method: 'post',
    data: data
  })
}
// 获取登录账号
export function getLoginAccount(id) {
  return request({
    url: '/api/erp-api/warehouse/getLoginAccount/' + id,
    method: 'get'
  })
}
