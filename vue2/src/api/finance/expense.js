import request from '@/utils/request'

// 查询支出报销列表
export function list(params) {
  return request({
    url: '/api/erp-api/finance/expense/list',
    method: 'get',
    params: params
  })
}

// 添加支出报销
export function add(data) {
  return request({
    url: '/api/erp-api/finance/expense/add',
    method: 'post',
    data: data
  })
}

// 审核支出报销
export function approve(data) {
  return request({
    url: '/api/erp-api/finance/expense/approve',
    method: 'post',
    data: data
  })
}

// 支付支出报销
export function pay(data) {
  return request({
    url: '/api/erp-api/finance/expense/pay',
    method: 'post',
    data: data
  })
}

// 关闭支出报销
export function close(data) {
  return request({
    url: '/api/erp-api/finance/expense/close',
    method: 'post',
    data: data
  })
}
