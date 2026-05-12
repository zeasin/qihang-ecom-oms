import request from '@/utils/request'

export function h5Create(data) {
  return request({
    url: '/api/erp-api/sales/order/h5/create',
    method: 'post',
    data: data
  })
}

export function h5List(query) {
  return request({
    url: '/api/erp-api/sales/order/h5/list',
    method: 'get',
    params: query
  })
}

export function h5Detail(id) {
  return request({
    url: `/api/erp-api/sales/order/${id}`,
    method: 'get'
  })
}

export function listPackage(params) {
  return request({
    url: '/api/erp-api/goods/package/list',
    method: 'get',
    params: params
  })
}