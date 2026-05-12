import request from '@/utils/request'

// 接口拉取吉客云云仓商品
export function pullJkyGoodsItems(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jky/goods/pull_goods_items',
    method: 'post',
    data: data
  })
}
// 推送到吉客云
export function pullJkyShops(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jky/shop/pull_shops',
    method: 'post',
    data: data
  })
}

// 推送到吉客云
export function pushJkyGoodsItems(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jky/goods/push_goods_items',
    method: 'post',
    data: data
  })
}

