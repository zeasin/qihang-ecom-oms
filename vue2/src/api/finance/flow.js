import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/api/erp-api/finance/flow/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/api/erp-api/finance/flow/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/api/erp-api/finance/flow',
    method: 'post',
    data: data
  })
}

export function update(data) {
  return request({
    url: '/api/erp-api/finance/flow',
    method: 'put',
    data: data
  })
}

export function remove(id) {
  return request({
    url: '/api/erp-api/finance/flow/' + id,
    method: 'delete'
  })
}

export function getByVoucherNo(voucherNo) {
  return request({
    url: '/api/erp-api/finance/flow/voucherNo/' + voucherNo,
    method: 'get'
  })
}