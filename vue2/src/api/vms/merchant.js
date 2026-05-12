import request from '@/utils/request'

// 查询供应商管理列表
export function listMerchant(query) {
  return request({
    url: '/api/wms-api/merchant/list',
    method: 'get',
    params: query
  })
}

// 查询供应商管理详细
export function getMerchant(id) {
  return request({
    url: '/api/wms-api/merchant/' + id,
    method: 'get'
  })
}
// 查询平台所有店铺
export function listMerchantShop(id) {
  return request({
    url: '/api/wms-api/merchant/shopList',
    method: 'get'
  })
}
