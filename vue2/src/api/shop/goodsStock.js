import request from '@/utils/request'


// 查询店铺商品sku和库存
export function queryShopGoodsSkuAndStock(query) {
  return request({
    url: '/api/oms-api/shop/goods/searchSkuAndStock',
    method: 'get',
    params: query
  })
}

