import request from '@/utils/request'

export function listOrderSettlement(query) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/list',
    method: 'get',
    params: query
  })
}

export function autoSettlement(data) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/autoSettlement',
    method: 'post',
    data: data
  })
}

export function manualSettlement(data) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/manualSettlement',
    method: 'post',
    data: data
  })
}

export function cancelSettlement(id) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/cancel/' + id,
    method: 'put'
  })
}

export function getUnsettledOrders(params) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/unsettled',
    method: 'get',
    params: params
  })
}

export function getSettledOrders(params) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/settled',
    method: 'get',
    params: params
  })
}

export function getSettlementDetail(id) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/detail/' + id,
    method: 'get'
  })
}

export function calculateSettlement(orderId) {
  return request({
    url: '/api/erp-api/finance/orderSettlement/calculate/' + orderId,
    method: 'get'
  })
}
