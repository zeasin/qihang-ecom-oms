import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/api/erp-api/goods/package/list',
    method: 'get',
    params: params
  })
}

export function getInfo(id) {
  return request({
    url: '/api/erp-api/goods/package/' + id,
    method: 'get'
  })
}

export function getItems(id) {
  return request({
    url: '/api/erp-api/goods/package/' + id + '/items',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/api/erp-api/goods/package',
    method: 'post',
    data: data
  })
}

export function update(data) {
  return request({
    url: '/api/erp-api/goods/package',
    method: 'put',
    data: data
  })
}

export function updateStatus(data) {
  return request({
    url: '/api/erp-api/goods/package/status',
    method: 'put',
    data: data
  })
}

export function remove(id) {
  return request({
    url: '/api/erp-api/goods/package/' + id,
    method: 'delete'
  })
}

export function saveItems(id, items) {
  return request({
    url: '/api/erp-api/goods/package/' + id + '/items',
    method: 'put',
    data: items
  })
}