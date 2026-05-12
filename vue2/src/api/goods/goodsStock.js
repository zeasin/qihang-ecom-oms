import request from '@/utils/request'


// 获取仓库商品库存
export function getWarehouseGoodsStockList(query) {
  return request({
    url: '/api/erp-api/goods/stock/list',
    method: 'get',
    params: query
  })
}

// 查询商品库存详细
export function getGoodsStockBatch(id) {
  return request({
    url: '/api/erp-api/goods/stock/batch/' + id,
    method: 'get'
  })
}



