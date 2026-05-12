import request from '@/utils/request'

export function listStockOut(query) {
  return request({
    url: 'api/wms-api/stockOut/list',
    method: 'get',
    params: query
  })
}

export function getStockOutEntry(id) {
  return request({
    url: 'api/wms-api/stockOut/' + id,
    method: 'get'
  })
}

export function stockOut(data) {
  return request({
    url: 'api/wms-api/stockOut/out',
    method: 'post',
    data: data
  })
}

export function searchSkuInventoryBatch(query) {
  return request({
    url: 'api/wms-api/goodsStock/searchSkuInventoryBatch',
    method: 'get',
    params: query
  })
}