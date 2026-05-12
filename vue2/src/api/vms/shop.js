import request from '@/utils/request'


// 查询店铺平台列表
export function listPlatform(query) {
  return request({
    url: '/api/oms-api/shop/platformList',
    method: 'get',
    params: query
  })
}

// 查询店铺列表
export function listShop(query) {
  return request({
    url: '/api/wms-api/shop/listAll',
    method: 'get',
    params: query
  })
}

export function listShopPage(query) {
  return request({
    url: '/api/wms-api/shop/pageList',
    method: 'get',
    params: query
  })
}

// 新增店铺
export function addShop(data) {
  return request({
    url: '/api/wms-api/shop/add',
    method: 'post',
    data: data
  })
}

// 查询店铺详细
export function getShop(id) {
  return request({
    url: '/api/wms-api/shop/detail/' + id,
    method: 'get'
  })
}

// 修改店铺
export function updateShop(data) {
  return request({
    url: '/api/wms-api/shop/update',
    method: 'put',
    data: data
  })
}

// 删除店铺
export function delShop(id) {
  return request({
    url: '/api/wms-api/shop/del/' + id,
    method: 'delete'
  })
}
