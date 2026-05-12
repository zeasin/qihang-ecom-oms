import request from '@/utils/request'

// 查询采购订单列表
export function listPurchaseOrder(query) {
  return request({
    url: '/api/erp-api/erp/purchase/list',
    method: 'get',
    params: query
  })
}

export function listPurchaseOrderItem(query) {
  return request({
    url: '/api/erp-api/erp/purchase/item_list',
    method: 'get',
    params: query
  })
}

// 查询采购订单详细
export function getPurchaseOrder(id) {
  return request({
    url: '/api/erp-api/erp/purchase/detail/' + id,
    method: 'get'
  })
}

// 新增采购订单
export function addPurchaseOrder(data) {
  return request({
    url: '/api/erp-api/erp/purchase/create',
    method: 'post',
    data: data
  })
}

// 修改采购订单
export function updatePurchaseOrder(data) {
  return request({
    url: '/api/erp-api/erp/purchase/updateStatus',
    method: 'put',
    data: data
  })
}


