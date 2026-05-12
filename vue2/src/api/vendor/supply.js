import request from '@/utils/request'

export function getSupplyList(params) {
  return request({
    url: 'api/erp-api/vendor/supply/list',
    method: 'get',
    params
  })
}

export function getSupplyDetail(id) {
  return request({
    url: `api/erp-api/vendor/supply/detail/${id}`,
    method: 'get'
  })
}

export function confirmSupply(id) {
  return request({
    url: `api/erp-api/vendor/supply/confirm/${id}`,
    method: 'put'
  })
}