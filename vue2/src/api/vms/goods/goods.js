import request from '@/utils/request'

// // 查询商品管理列表
// export function listGoods(query) {
//   return request({
//     url: '/api/vms-api/goods/list',
//     method: 'get',
//     params: query
//   })
// }
//
//
//
// // 查询商品管理详细
// export function getGoodsItem(id) {
//   return request({
//     url: '/api/vms-api/goods/' + id,
//     method: 'get'
//   })
// }
//
// // 新增商品Item
// export function addGoodsItem(data) {
//   return request({
//     url: '/api/vms-api/goods/add',
//     method: 'post',
//     data: data
//   })
// }
//
// // 修改商品Item
// export function updateGoodsItem(data) {
//   return request({
//     url: '/api/vms-api/goods/edit',
//     method: 'put',
//     data: data
//   })
// }
//
// // 删除商品Item
// export function delGoodsSpec(id) {
//   return request({
//     url: '/api/vms-api/goods/del/' + id,
//     method: 'delete'
//   })
// }


// 查询商品规格列表
export function searchSkuAndStock(query) {
  return request({
    url: '/api/wms-api/warehouse/goods_sku_stock_search',
    method: 'get',
    params: query
  })
}
