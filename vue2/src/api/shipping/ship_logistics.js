import request from '@/utils/request'

// 获取平台列表
export function getPlatformList() {
  return request({
    url: 'api/erp-api/ship/logistics/platform_list',
    method: 'get'
  })
}

// 获取常用快递公司列表
export function getFavoriteList() {
  return request({
    url: 'api/erp-api/ship/logistics/favorite_list',
    method: 'get'
  })
}

// 获取可选择的快递公司列表（支持按平台筛选和模糊搜索）
export function getAvailableList(query) {
  return request({
    url: 'api/erp-api/ship/logistics/available_list',
    method: 'get',
    params: query
  })
}

// 添加常用快递公司
export function addFavorite(data) {
  return request({
    url: 'api/erp-api/ship/logistics/favorite_add',
    method: 'post',
    data
  })
}

// 删除常用快递公司
export function deleteFavorite(id) {
  return request({
    url: `api/erp-api/ship/logistics/favorite_delete/${id}`,
    method: 'delete'
  })
}

// 设置默认快递公司
export function setDefault(id) {
  return request({
    url: `api/erp-api/ship/logistics/favorite_set_default/${id}`,
    method: 'put'
  })
}