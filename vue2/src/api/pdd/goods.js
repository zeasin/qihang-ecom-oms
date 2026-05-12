import request from '@/utils/request'

// 查询列表
export function listGoodsSku(query) {
  return request({
    url: '/api/oms-api/pdd/goods/skuList',
    method: 'get',
    params: query
  })
}
// 查询列表
export function listGoods(query) {
  return request({
    url: '/api/oms-api/pdd/goods/list',
    method: 'get',
    params: query
  })
}


export function getGoodsSku(id) {
  return request({
    url: '/api/oms-api/pdd/goods/sku/'+id,
    method: 'get',
  })
}

// 删除商品
export function delGoods(id) {
  return request({
    url: '/api/oms-api/pdd/goods/del/' + id,
    method: 'delete'
  })
}

export function linkErpGoodsSkuId(data) {
  return request({
    url: '/api/oms-api/pdd/goods/sku/linkErp',
    method: 'post',
    data: data
  })
}

// 接口拉取商品
export function pullGoodsList(data) {
  return request({
    url: '/api/oms-api/pdd/goods/pull_goods',
    method: 'post',
    data: data
  })
}
// 拉取商品详情
export function pullPddGoodsDetail(data) {
  return request({
    url: '/api/oms-api/pdd/goods/pull_goods_detail',
    method: 'post',
    data: data
  })
}
