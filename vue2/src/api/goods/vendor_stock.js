import request from '@/utils/request'

// 云仓库库存list
export function listCloudStockInventory(query) {
  return request({
    url: '/api/erp-api/vendor_stock/inventory_list',
    method: 'get',
    params: query
  })
}
// 云仓在库商品sku列表
export function listStoredGoodsSku(query) {
  return request({
    url: '/api/erp-api/vendor_stock/stored_goods_sku_list',
    method: 'get',
    params: query
  })
}



// 云仓入库单list
export function listCloudStockIn(query) {
  return request({
    url: '/api/erp-api/vendor_stock/stock_in_list',
    method: 'get',
    params: query
  })
}

// 提交云仓入库申请
export function applyCloudStockIn(data) {
  return request({
    url: '/api/erp-api/vendor_stock/stock_in_apply',
    method: 'post',
    data: data
  })
}

// 查询详细
export function getCloudStockIn(id) {
  return request({
    url: '/api/erp-api/vendor_stock/stock_in_detail/' + id,
    method: 'get'
  })
}
