import request from '@/utils/request'

// 获取第三方云仓商品批次列表
export function getThirdPartyCloudWarehouseBatches(params) {
  return request({
    url: 'api/erp-api/warehouse/third-party-cloud/batches',
    method: 'get',
    params
  })
}

// 第三方云仓手动发货
export function manualShipment(data) {
  return request({
    url: 'api/erp-api/warehouse/third-party-cloud/manual-shipment',
    method: 'post',
    data
  })
}

// 第三方云仓手动确认入库
export function manualConfirmStockIn(data) {
  return request({
    url: 'api/erp-api/cloudWarehouse/third-party-cloud/manual-confirm-stock-in',
    method: 'post',
    data
  })
}
