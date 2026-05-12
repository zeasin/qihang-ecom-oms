import request from '@/utils/request'

// 查询供应商管理列表
export function listSalesPeople(query) {
  return request({
    url: '/api/erp-api/sales/people/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getSalesPeople(id) {
  return request({
    url: '/api/erp-api/sales/people/' + id,
    method: 'get'
  })
}
// 新增
export function addSalesPeople(data) {
  return request({
    url: '/api/erp-api/sales/people/add',
    method: 'post',
    data: data
  })
}
// 修改
export function updateSalesPeople(data) {
  return request({
    url: '/api/erp-api/sales/people/update',
    method: 'post',
    data: data
  })
}

// 删除
export function delSalesPeople(id) {
  return request({
    url: '/api/erp-api/sales/people/del/' + id,
    method: 'delete'
  })
}
