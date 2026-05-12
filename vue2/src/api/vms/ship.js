import request from '@/utils/request'

// 填写物流单号
export function logisticsManualSend(data) {
  return request({
    url: '/api/wms-api/ship/order/manualShipment',
    method: 'post',
    data: data
  })
}

// 批量发货
export function batchOrderSend(data) {
  return request({
    url: '/api/supplier-api/ship/order_batch_send',
    method: 'post',
    data: data
  })
}

// 生成出库单（按订单）
export function generateStockOutByOrder(data) {
  return request({
    url: '/api/wms-api/ship/stocking/generateStockOutByOrder',
    method: 'post',
    data: data
  })
}
