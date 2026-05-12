import request from '@/utils/request'

export function listPrice(query) {
  return request({
    url: 'api/erp-api/supplier/price/list',
    method: 'get',
    params: query
  })
}

export function getPrice(id) {
  return request({
    url: 'api/erp-api/supplier/price/' + id,
    method: 'get'
  })
}

export function addPrice(data) {
  return request({
    url: 'api/erp-api/supplier/price/add',
    method: 'post',
    data: data
  })
}

export function updatePrice(data) {
  return request({
    url: 'api/erp-api/supplier/price/edit',
    method: 'put',
    data: data
  })
}

export function delPrice(id) {
  return request({
    url: 'api/erp-api/supplier/price/del/' + id,
    method: 'delete'
  })
}
