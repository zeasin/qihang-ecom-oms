import request from '@/utils/request'

// 查询财务管理-应付款-代发账单列表
export function listShopAccounts(query) {
  return request({
    url: '/financial/shop_accounts/list',
    method: 'get',
    params: query
  })
}

// 查询
export function getAccounts(id) {
  return request({
    url: '/financial/shop_accounts/detail/' + id,
    method: 'get'
  })
}

// 新增
export function addAccounts(data) {
  return request({
    url: '/financial/shop_accounts/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateAccounts(data) {
  return request({
    url: '/financial/shop_accounts/edit',
    method: 'put',
    data: data
  })
}

// 删除
export function delAccounts(id) {
  return request({
    url: '/financial/shop_accounts/del/' + id,
    method: 'delete'
  })
}
