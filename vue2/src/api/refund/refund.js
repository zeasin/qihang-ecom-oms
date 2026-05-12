import request from '@/utils/request'

// 查询退换货列表
export function listReturned(query) {
  return request({
    url: '/api/erp-api/refund/list',
    method: 'get',
    params: query
  })
}

// 查询退换货详细
export function getReturned(id) {
  return request({
    url: '/api/erp-api/refund/' + id,
    method: 'get'
  })
}



export function refundProcessing(data) {
  return request({
    url: '/api/erp-api/refund/processing',
    method: 'post',
    data:data
  })
}

// 添加销售订单售后
export function addSaleOrderAfter(data) {
  return request({
    url: '/api/erp-api/refund/addSaleOrderAfter',
    method: 'post',
    data:data
  })
}
