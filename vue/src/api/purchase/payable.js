import request from '@/utils/request'

// 查询财务管理-应付款-采购货款列表
export function listPayablePurchase(query) {
  return request({
    url: '/fms/payablePurchase/list',
    method: 'get',
    params: query
  })
}

// 查询财务管理-应付款-采购货款详细
export function getPayablePurchase(id) {
  return request({
    url: '/fms/payablePurchase/' + id,
    method: 'get'
  })
}



// 修改财务管理-应付款-采购货款
export function updatePayablePurchase(data) {
  return request({
    url: '/fms/payablePurchase',
    method: 'put',
    data: data
  })
}

