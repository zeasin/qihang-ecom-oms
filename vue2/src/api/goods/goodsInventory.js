import request from '@/utils/request'

// 查询商品库存列表
export function listGoodsInventory(query) {
  return request({
    url: '/api/erp-api/goodsInventory/list',
    method: 'get',
    params: query
  })
}

// 查询商品库存详细
export function getGoodsInventory(id) {
  return request({
    url: '/api/erp-api/goodsInventory/' + id,
    method: 'get'
  })
}
// 搜索可以出库的批次
export function searchSkuInventoryBatch(query) {
  return request({
    url: '/api/erp-api/stockOut/searchSkuInventoryBatch',
    method: 'get',
    params: query
  })
}

// 手动更新库存
export function updateInventory(data) {
  return request({
    url: '/api/erp-api/goodsInventory/updateInventory',
    method: 'post',
    data: data
  })
}
export function batchUpdateInventory(data) {
  return request({
    url: '/api/erp-api/goodsInventory/batchUpdateInventory',
    method: 'post',
    data: data
  })
}
