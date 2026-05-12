import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/api/erp-api/afterSale/list',
    method: 'get',
    params: query
  })
}

// 退货确认收货并入库
export function returnedConfirmAndStockIn(data) {
  return request({
    url: '/api/erp-api/afterSale/returnedConfirmAndStockIn',
    method: 'post',
    data: data
  })
}


// 补发确认
export function shipAgainConfirm(data) {
  return request({
    url: '/api/erp-api/afterSale/shipAgainConfirm',
    method: 'post',
    data: data
  })
}


// 换货确认
export function exchangeConfirm(data) {
  return request({
    url: '/api/erp-api/afterSale/exchangeConfirm',
    method: 'post',
    data: data
  })
}



