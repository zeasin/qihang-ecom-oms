import request from '@/utils/request'

// 查询jdvc商品sku列表
export function listGoodsSku(query) {
  return request({
    url: '/api/oms-api/jdvc/goods/list',
    method: 'get',
    params: query
  })
}


export function getGoodsSku(id) {
  return request({
    url: '/api/oms-api/jdvc/goods/'+id,
    method: 'get',
  })
}


export function linkErpGoodsSkuId(data) {
  return request({
    url: '/api/oms-api/jdvc/goods/linkErpSku',
    method: 'post',
    data: data
  })
}

// 接口拉取淘宝商品
export function pullGoodsList(data) {
  return request({
    url: '/api/oms-api/jdvc/goods/pull_goods',
    method: 'post',
    data: data
  })
}
