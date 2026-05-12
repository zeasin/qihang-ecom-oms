import request from '@/utils/request'

// 查询商品管理列表
export function listStoredGoodsSku(query) {
  return request({
    url: '/api/wms-api/warehouse/goods_sku_list',
    method: 'get',
    params: query
  })
}

export function listStoredGoodsSkuAndStock(query) {
  return request({
    url: '/api/wms-api/warehouse/goods_sku_stock_list',
    method: 'get',
    params: query
  })
}

// 新增仓库商品资料
export function addWarehouseGoodsItem(data) {
  return request({
    url: '/api/wms-api/warehouse/goodsSkuAdd',
    method: 'post',
    data: data
  })
}


export function updateWarehouseGoodsItem(data) {
  return request({
    url: '/api/wms-api/warehouse/goodsSkuUpdate',
    method: 'post',
    data: data
  })
}

// 删除商品管理
export function delWarehouseGoods(id) {
  return request({
    url: '/api/wms-api/warehouse/goodsSku/del/' + id,
    method: 'delete'
  })
}
