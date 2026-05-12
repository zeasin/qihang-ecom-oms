import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/api/erp-api/shop/inventory-lock/list',
    method: 'get',
    params: params
  })
}

export function add(data) {
  return request({
    url: '/api/erp-api/shop/inventory-lock/add',
    method: 'post',
    data: data
  })
}

export function release(lockNo) {
  return request({
    url: '/api/erp-api/shop/inventory-lock/release',
    method: 'post',
    data: { lockNo: lockNo }
  })
}

export function getLocksByTransfer(transferId) {
  return request({
    url: '/api/erp-api/shop/inventory-lock/transfer/' + transferId,
    method: 'get'
  })
}