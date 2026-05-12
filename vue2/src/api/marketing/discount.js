import request from '@/utils/request'

// 查询
export function listDiscount(query) {
  return request({
    url: '/api/oms-api/marketing/discount/list',
    method: 'get',
    params: query
  })
}


// 查询
export function getDiscount(id) {
  return request({
    url: '/api/oms-api/marketing/discount/' + id,
    method: 'get'
  })
}

// 新增
export function addDiscount(data) {
  return request({
    url: '/api/oms-api/marketing/discount/add',
    method: 'post',
    data: data
  })
}

// 删除
export function delDiscount(id) {
  return request({
    url: '/api/oms-api/marketing/discount/del/' + id,
    method: 'delete'
  })
}

// 审核
export function auditDiscount(id) {
  return request({
    url: '/api/oms-api/marketing/discount/audit/' + id,
    method: 'delete'
  })
}

