import request from '@/utils/request'
// 获取电子面单账户list
export function getWaybillAccountList(query) {
  return request({
    url: '/api/wms-api/dou/ewaybill/get_waybill_account_list',
    method: 'get',
    params: query
  })
}


// 取号
export function getWaybillCode(data) {
  return request({
    url: '/api/wms-api/dou/ewaybill/get_waybill_code',
    method: 'post',
    data: data
  })
}


// 取号
export function getWaybillCodeAndSend(data) {
  return request({
    url: '/api/wms-api/dou/ewaybill/get_waybill_code_and_send',
    method: 'post',
    data: data
  })
}

// 获取打印的数据
export function getWaybillPrintData(data) {
  return request({
    url: '/api/wms-api/dou/ewaybill/get_print_data',
    method: 'post',
    data: data
  })
}

// 打印成功
export function pushWaybillPrintSuccess(data) {
  return request({
    url: '/api/wms-api/dou/ewaybill/push_print_success',
    method: 'post',
    data: data
  })
}
export function pushShipSend(data) {
  return request({
    url: '/api/wms-api/dou/ewaybill/push_ship_send',
    method: 'post',
    data: data
  })
}
