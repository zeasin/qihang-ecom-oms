import request from '@/utils/request'

// 获取配置
export function getTangLangConfig() {
  return request({
    url: '/api/oms-api/shop/tanglang/systemConfig',
    method: 'get',
  })
}


export function saveTangLangConfig(data) {
  return request({
    url: '/api/oms-api/shop/tanglang/systemConfig/save',
    method: 'post',
    data: data
  })
}


// // 拉取商品列表
// export function pullTanglangGoodsItems(data) {
//   return request({
//     url: '/api/erp-api/third/tanglang/goods/pull_goods',
//     method: 'post',
//     data: data
//   })
// }

// 查询商品列表
export function listTanglangGoodsItems(query) {
  return request({
    url: '/api/oms-api/shop/tanglang/goods/item_list',
    method: 'get',
    params: query
  })
}
// 查询绑定发货清单
export function listTanglangGoodsShipItems(id) {
  return request({
    url: '/api/oms-api/shop/tanglang/goods/ship_item/'+id,
    method: 'get'
  })
}


// 绑定发货商品SKU
export function bindTanglangGoodsSku(data) {
  return request({
    url: '/api/oms-api/shop/tanglang/goods/bind_sku_list',
    method: 'post',
    data: data
  })
}

// // 拉取订单(课程)
// export function pullTanglangOrderList(query) {
//   return request({
//     url: '/api/erp-api/third/tanglang/order/pull_order_kc',
//     method: 'get',
//     params: query
//   })
// }

// 拉取订单(课程)
export function pullTanglangSwOrderList(data) {
  return request({
    url: '/api/oms-api/shop/order/pull_tl_order_sw',
    method: 'post',
    data: data
  })
}

// 订单list
export function listTanglangOrderList(query) {
  return request({
    url: '/api/oms-api/shop/tanglang/order/list',
    method: 'get',
    params: query
  })
}


// 确认虚拟单并生成新单
export function virtualOrderConfirmAndNew(data) {
  return request({
    url: '/api/oms-api/shop/tanglang/order/virtualOrderConfirmAndNew',
    method: 'post',
    data: data
  })
}
// 手动推送订单到OMS
export function pushOms(data) {
  return request({
    url: '/api/oms-api/shop/tanglang/order/push_oms',
    method: 'post',
    data: data
  })
}
