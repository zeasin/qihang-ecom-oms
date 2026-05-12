import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/api/erp-api/shop/transfer/list',
    method: 'get',
    params: params
  })
}

export function apply(data) {
  return request({
    url: '/api/erp-api/shop/transfer/apply',
    method: 'post',
    data: data
  })
}

export function approve(data) {
  return request({
    url: '/api/erp-api/shop/transfer/approve',
    method: 'post',
    data: data
  })
}

export function outbound(data) {
  return request({
    url: '/api/erp-api/shop/transfer/outbound',
    method: 'post',
    data: data
  })
}

export function inbound(data) {
  return request({
    url: '/api/erp-api/shop/transfer/inbound',
    method: 'post',
    data: data
  })
}

export function cancel(id) {
  return request({
    url: '/api/erp-api/shop/transfer/cancel/' + id,
    method: 'post'
  })
}
