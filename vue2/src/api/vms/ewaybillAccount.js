import request from '@/utils/request'

// 列表
export function getShareMeWaybillAccountList(params) {
  return request({
    url: '/api/wms-api/ewaybill/getShareMeWaybillAccountList',
    method: 'get',
    params: params
  })
}
//获取店铺电子面单list
export function getShopWaybillAccountList(params) {
  return request({
    url: '/api/wms-api/ewaybill/get_shop_waybill_account_list',
    method: 'get',
    params: params
  })
}


// 快递模块列表
export function getWaybillTemplateList(params) {
  return request({
    url: '/api/wms-api/ewaybill/get_waybill_template_list',
    method: 'get',
    params: params
  })
}

// 更新电子面单账户
export function pullWaybillAccount(data) {
  return request({
    url: '/api/wms-api/ewaybill/pull_waybill_account',
    method: 'post',
    data: data
  })
}





export function updateAccount(data) {
  return request({
    url: '/api/wms-api/ewaybill/updateAccount',
    method: 'post',
    data: data
  })
}
