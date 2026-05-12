import request from '@/utils/request'

// 列表
export function listStockTake(query) {
  return request({
    url: '/api/wms-api/stockTake/list',
    method: 'get',
    params: query
  })
}

// 详细
export function getStockTakeEntry(id) {
  return request({
    url: '/api/wms-api/stockTake/' + id,
    method: 'get'
  })
}

// 新增
export function stockTakeCreate(data) {
  return request({
    url: '/api/wms-api/stockTake/create',
    method: 'post',
    data: data
  })
}

// 保存盘点商品
export function saveTakeItemGoods(data) {
  return request({
    url: '/api/wms-api/stockTake/addTakeGoods',
    method: 'post',
    data: data
  })
}
// 删除盘点商品
export function delTakeItemGoods(data) {
  return request({
    url: '/api/wms-api/stockTake/delTakeGoods',
    method: 'post',
    data: data
  })
}
// 保存盘点结果
export function saveTake(data) {
  return request({
    url: '/api/wms-api/stockTake/saveTake',
    method: 'post',
    data: data
  })
}

export function stockTakeItem(data) {
  return request({
    url: '/api/wms-api/stockTake/addItem',
    method: 'post',
    data: data
  })
}

export function stockTakeComplete(id) {
  return request({
    url: '/api/wms-api/stockTake/complete/' + id,
    method: 'post'
  })
}
