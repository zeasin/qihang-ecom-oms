import request from '@/utils/request'

export function listShareParty(query) {
  return request({
    url: '/api/erp-api/sales/share/party/list',
    method: 'get',
    params: query
  })
}

export function getShareParty(id) {
  return request({
    url: '/api/erp-api/sales/share/party/' + id,
    method: 'get'
  })
}

export function addShareParty(data) {
  return request({
    url: '/api/erp-api/sales/share/party',
    method: 'post',
    data: data
  })
}

export function updateShareParty(data) {
  return request({
    url: '/api/erp-api/sales/share/party',
    method: 'put',
    data: data
  })
}

export function delShareParty(id) {
  return request({
    url: '/api/erp-api/sales/share/party/' + id,
    method: 'delete'
  })
}

export function listShareRule(query) {
  return request({
    url: '/api/erp-api/sales/share/rule/list',
    method: 'get',
    params: query
  })
}

export function getShareRule(id) {
  return request({
    url: '/api/erp-api/sales/share/rule/' + id,
    method: 'get'
  })
}

export function addShareRule(data) {
  return request({
    url: '/api/erp-api/sales/share/rule',
    method: 'post',
    data: data
  })
}

export function updateShareRule(data) {
  return request({
    url: '/api/erp-api/sales/share/rule',
    method: 'put',
    data: data
  })
}

export function delShareRule(id) {
  return request({
    url: '/api/erp-api/sales/share/rule/' + id,
    method: 'delete'
  })
}

export function listShareRecord(query) {
  return request({
    url: '/api/erp-api/sales/share/record/list',
    method: 'get',
    params: query
  })
}

export function manualShare(orderId) {
  return request({
    url: '/api/erp-api/sales/share/manual/' + orderId,
    method: 'post'
  })
}