import request from '@/utils/request'

// 查询店铺商品列表
export function listGoods(query) {
  return request({
    url: '/api/oms-api/shop/goods/list',
    method: 'get',
    params: query
  })
}

// 查询店铺商品sku列表
export function listGoodsSku(query) {
  return request({
    url: '/api/oms-api/shop/goods/skuList',
    method: 'get',
    params: query
  })
}

// 拉取店铺商品lsit
export function pullGoodsList(data) {
  return request({
    url: '/api/oms-api/shop/goods/pull_list',
    method: 'post',
    data: data
  })
}

// 关联
export function linkErpGoodsSkuId(data) {
  return request({
    url: '/api/oms-api/shop/goods/sku/linkErp',
    method: 'post',
    data: data
  })
}
// 修改店铺商品Sku
export function updateGoodsSku(data) {
  return request({
    url: '/api/oms-api/shop/goods/sku/update',
    method: 'post',
    data: data
  })
}

// 新增店铺商品
export function addShopGoods(data) {
  return request({
    url: '/api/oms-api/shop/goods/add',
    method: 'post',
    data: data
  })
}

// 新增店铺商品Sku
export function addShopGoodsSku(data) {
  return request({
    url: '/api/oms-api/shop/goods/sku/add',
    method: 'post',
    data: data
  })
}

// 0店铺商品增加Sku
export function insertShopGoodsSku(data) {
  return request({
    url: '/api/oms-api/shop/goods/sku/insert',
    method: 'post',
    data: data
  })
}


// 修改店铺商品
export function updateGoods(data) {
  return request({
    url: '/api/oms-api/shop/goods/update',
    method: 'put',
    data: data
  })
}

// 删除店铺商品
export function delGoods(id) {
  return request({
    url: '/api/oms-api/shop/goods/del/' + id,
    method: 'delete'
  })
}
// 删除店铺商品Sku
export function delGoodsSku(id) {
  return request({
    url: '/api/oms-api/shop/goods/sku/del/' + id,
    method: 'delete'
  })
}
// 接口下载商品
export function pullDownloadGoods(data) {
  return request({
    url: '/api/oms-api/shop/goods/download_goods',
    method: 'post',
    data: data
  })
}

