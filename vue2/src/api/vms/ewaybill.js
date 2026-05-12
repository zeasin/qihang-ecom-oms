import request from '@/utils/request'


// 取号
export function getWaybillCodeDiansan(data) {
  return request({
    url: '/api/wms-api/ewaybill/get_waybill_code_diansan',
    method: 'post',
    data: data
  })
}

// 获取打印的数据
export function getWaybillPrintData(data) {
  return request({
    url: '/api/wms-api/ewaybill/get_print_data',
    method: 'post',
    data: data
  })
}
// 获取打印的数据
export function getWaybillPrintDataDiansan(data) {
  return request({
    url: '/api/wms-api/ewaybill/get_print_data_diansan',
    method: 'post',
    data: data
  })
}

// 取消取号
export function cancelWaybillCodeDiansan(data) {
  return request({
    url: '/api/wms-api/ewaybill/cancel_waybill_code_diansan',
    method: 'post',
    data: data
  })
}
// 推送发货
export function pushShipSendDiansan(data) {
  return request({
    url: '/api/wms-api/ewaybill/push_ship_send_diansan',
    method: 'post',
    data: data
  })
}
