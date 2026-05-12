import request from '@/utils/request'

export function listStockAlert(query) {
  return request({
    url: 'api/wms-api/stockAlert/list',
    method: 'get',
    params: query
  })
}

export function alertList() {
  return request({
    url: 'api/wms-api/stockAlert/alertList',
    method: 'get'
  })
}

export function addStockAlert(data) {
  return request({
    url: 'api/wms-api/stockAlert',
    method: 'post',
    data: data
  })
}

export function updateStockAlert(id, data) {
  return request({
    url: 'api/wms-api/stockAlert/' + id,
    method: 'put',
    data: data
  })
}

export function deleteStockAlert(id) {
  return request({
    url: 'api/wms-api/stockAlert/' + id,
    method: 'delete'
  })
}

export function setAlertStatus(id, status) {
  return request({
    url: 'api/wms-api/stockAlert/status/' + id,
    method: 'put',
    params: { status: status }
  })
}

export function listWarehouseGoods(query) {
  return request({
    url: 'api/wms-api/warehouse/goods_sku_stock_list',
    method: 'get',
    params: query
  })
}
