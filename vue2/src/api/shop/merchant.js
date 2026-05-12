import request from '@/utils/request'

// 查询供应商管理列表
export function listMerchant(query) {
  return request({
    url: '/api/oms-api/merchant/list',
    method: 'get',
    params: query
  })
}
export function pageListMerchant() {
  return request({
    url: '/api/oms-api/merchant/pageList',
    method: 'get'
  })
}
export function listAllMerchant() {
  return request({
    url: '/api/oms-api/merchant/list_all',
    method: 'get'
  })
}

// 查询供应商管理详细
export function getMerchant(id) {
  return request({
    url: '/api/oms-api/merchant/' + id,
    method: 'get'
  })
}

// 新增供应商管理
export function addMerchant(data) {
  return request({
    url: '/api/oms-api/merchant/add',
    method: 'post',
    data: data
  })
}

// 修改供应商管理
export function updateMerchant(data) {
  return request({
    url: '/api/oms-api/merchant/edit',
    method: 'put',
    data: data
  })
}

// 删除供应商管理
export function delMerchant(id) {
  return request({
    url: '/api/oms-api/merchant/del/' + id,
    method: 'delete'
  })
}

export function setMerchantLoginName(data) {
  return request({
    url: '/api/oms-api/merchant/setLoginName',
    method: 'post',
    data: data
  })
}


export function shareCloudWarehouse(data) {
  return request({
    url: '/api/oms-api/merchant/shareCloudWarehouse',
    method: 'post',
    data: data
  })
}

// 分配给供应商发货
export function pushGoodsToMerchant(data) {
  return request({
    url: '/api/erp-api/goods/push_goods_to_merchant',
    method: 'post',
    data: data
  })
}



// 获取登录账号
export function getLoginAccount(id) {
  return request({
    url: '/api/oms-api/merchant/getLoginAccount/' + id,
    method: 'get'
  })
}
