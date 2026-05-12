import request from '@/utils/request'

// 查询列表
export function listRecovery(query) {
  return request({
    url: '/api/erp-api/recovery/list',
    method: 'get',
    params: query
  })
}

// 查询列表(会员可用抵扣)
export function listUserDeduction(query) {
  return request({
    url: '/api/erp-api/recovery/userDeductionList',
    method: 'get',
    params: query
  })
}

// 新增
export function addRecovery(data) {
  return request({
    url: '/api/erp-api/recovery/add',
    method: 'post',
    data: data
  })
}
