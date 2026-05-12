import request from '@/utils/request'

// 查询商品库存列表
export function listGoodsStock(query) {
  return request({
    url: '/api/wms-api/goodsStock/list',
    method: 'get',
    params: query
  })
}

// 查询商品库存详细
export function getGoodsStock(id) {
  return request({
    url: '/api/wms-api/goodsStock/' + id,
    method: 'get'
  })
}

export function searchGoodsStockBatch(query) {
  return request({
    url: '/api/wms-api/goodsStock/searchSkuInventoryBatch',
    method: 'get',
    params: query
  })
}


export function getGoodsStockQty(query) {
  return request({
    url: '/api/wms-api/goodsStock/getGoodsStockQty',
    method: 'get',
    params: query
  })
}
