import request from '@/utils/request'
// 查询总部商品列表
// export function listHeadGoods(query) {
//   return request({
//     url: '/api/erp-api/goods/list_head',
//     method: 'get',
//     params: query
//   })
// }
// 查询商品管理列表
export function listGoods(query) {
  return request({
    url: '/api/erp-api/goods/list',
    method: 'get',
    params: query
  })
}

// 查询商品规格列表
export function searchSku(query) {
  return request({
    url: '/api/erp-api/goods/searchSku',
    method: 'get',
    params: query
  })
}

// 查询商品管理详细
export function getGoods(id) {
  return request({
    url: '/api/erp-api/goods/' + id,
    method: 'get'
  })
}

// 新增商品管理
export function addGoods(data) {
  return request({
    url: '/api/erp-api/goods/add',
    method: 'post',
    data: data
  })
}

// 修改商品管理
export function updateGoods(data) {
  return request({
    url: '/api/erp-api/goods',
    method: 'put',
    data: data
  })
}

// 删除商品管理
export function delGoods(id) {
  return request({
    url: '/api/erp-api/goods/del/' + id,
    method: 'delete'
  })
}

export function updateGoodsStatus(data) {
  return request({
    url: '/api/erp-api/goods/updateGoodsStatus',
    method: 'post',
    data: data
  })
}
// 新增商品SKU
export function addGoodsSku(data) {
  return request({
    url: '/api/erp-api/goods/addSku',
    method: 'post',
    data: data
  })
}
// 修改商品SKU
export function updateGoodsSku(data) {
  return request({
    url: '/api/erp-api/goods/sku',
    method: 'put',
    data: data
  })
}

export function generateGoodsNumber(data) {
  return request({
    url: '/api/erp-api/goods/generateGoodsNumber',
    method: 'post',
    data: data
  })
}

