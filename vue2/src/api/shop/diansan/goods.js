import request from '@/utils/request'


// 拉取店铺商品lsit
export function pullDiansanShopGoodsList(query) {
  return request({
    url: '/api/oms-api/diansan/goods/pull_goods_list',
    method: 'get',
    params: query
  })
}


export function pullDiansanShopOrderList(query) {
  return request({
    url: '/api/oms-api/diansan/order/pull_order_list',
    method: 'get',
    params: query
  })
}


export function pullDiansanShopRefundList(query) {
  return request({
    url: '/api/oms-api/diansan/refund/pull_refund_list',
    method: 'get',
    params: query
  })
}
