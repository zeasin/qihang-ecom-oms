import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/api/erp-api/shop/share/list',
    method: 'get',
    params: params
  })
}

export function add(data) {
  return request({
    url: '/api/erp-api/shop/share/add',
    method: 'post',
    data: data
  })
}

export function update(data) {
  return request({
    url: '/api/erp-api/shop/share/update',
    method: 'post',
    data: data
  })
}

export function deleteShare(id) {
  return request({
    url: '/api/erp-api/shop/share/delete/' + id,
    method: 'post'
  })
}

export function getMyApplications(params) {
  return request({
    url: '/api/erp-api/shop/share/my-applications',
    method: 'get',
    params: params
  })
}

export function getPendingApprovals(params) {
  return request({
    url: '/api/erp-api/shop/share/pending-approvals',
    method: 'get',
    params: params
  })
}

export function getShareHistory(params) {
  return request({
    url: '/api/erp-api/shop/share/share-history',
    method: 'get',
    params: params
  })
}

export function approve(id, data) {
  return request({
    url: '/api/erp-api/shop/share/approve/' + id,
    method: 'post',
    data: data
  })
}

export function reject(id, data) {
  return request({
    url: '/api/erp-api/shop/share/reject/' + id,
    method: 'post',
    data: data
  })
}

export function cancel(id) {
  return request({
    url: '/api/erp-api/shop/share/cancel/' + id,
    method: 'post'
  })
}

export function getAvailableShops() {
  return request({
    url: '/api/erp-api/shop/share/available-shops',
    method: 'get'
  })
}

export function getAuthorizedShops() {
  return request({
    url: '/api/erp-api/shop/share/authorized-shops',
    method: 'get'
  })
}

export function getSharedInventory(params) {
  return request({
    url: '/api/erp-api/shop/inventory/shared-list',
    method: 'get',
    params: params
  })
}