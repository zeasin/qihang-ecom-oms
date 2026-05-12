import request from '@/utils/request'

// 列表
// export function getWaybillAccountList(data) {
//   return request({
//     url: '/api/oms-api/wei/ewaybill/get_waybill_account_list',
//     method: 'post',
//     data: data
//   })
// }

// 更新电子面单账户
export function pullWaybillAccountWei(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/pull_waybill_account',
    method: 'post',
    data: data
  })
}

// 取号
export function getWaybillCode(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/get_waybill_code',
    method: 'post',
    data: data
  })
}

// 获取打印的数据
export function getWaybillPrintData(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/get_print_data',
    method: 'post',
    data: data
  })
}

// 打印成功
export function pushWaybillPrintSuccess(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/push_print_success',
    method: 'post',
    data: data
  })
}


export function pushShipSend(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/push_ship_send',
    method: 'post',
    data: data
  })
}


export function shareSupplier(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/shareSupplier',
    method: 'post',
    data: data
  })
}

export function updateAccount(data) {
  return request({
    url: '/api/oms-api/wei/ewaybill/updateAccount',
    method: 'post',
    data: data
  })
}

// 快递模版列表
export function getWaybillTemplateList(params) {
  return request({
    url: '/api/oms-api/wei/ewaybill/get_waybill_template_list',
    method: 'get',
    params: params
  })
}

// 快递包裹类型列表
export function getWaybillPackageTypeList(params) {
  return request({
    url: '/api/oms-api/wei/ewaybill/get_waybill_packageType_list',
    method: 'get',
    params: params
  })
}
