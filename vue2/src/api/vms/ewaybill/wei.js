import request from '@/utils/request'

// 列表
export function getWaybillAccountList(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/get_waybill_account_list',
    method: 'post',
    data: data
  })
}



// 取号
export function getWaybillCode(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/get_waybill_code',
    method: 'post',
    data: data
  })
}

export function getWaybillCodeAndSend(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/get_waybill_code_and_send',
    method: 'post',
    data: data
  })
}

export function cancelWaybillCode(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/cancel_waybill_code',
    method: 'post',
    data: data
  })
}

// 获取打印的数据
export function getWaybillPrintData(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/get_print_data',
    method: 'post',
    data: data
  })
}

// 打印成功
export function pushWaybillPrintSuccess(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/push_print_success',
    method: 'post',
    data: data
  })
}


export function pushShipSend(data) {
  return request({
    url: '/api/wms-api/wei/ewaybill/push_ship_send',
    method: 'post',
    data: data
  })
}
