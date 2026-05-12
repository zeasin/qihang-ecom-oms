import request from '@/utils/request'

// 查询供应商客户列表
export function listCustomer(query) {
  return request({
    url: 'api/erp-api/supplier/customer/list',
    method: 'get',
    params: query
  })
}

// 查询供应商客户详细
export function getCustomer(id) {
  return request({
    url: 'api/erp-api/supplier/customer/' + id,
    method: 'get'
  })
}

// 修改客户状态
export function updateCustomerStatus(data) {
  return request({
    url: 'api/erp-api/supplier/customer/status',
    method: 'put',
    data: data
  })
}
