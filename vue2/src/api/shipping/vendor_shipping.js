import request from '@/utils/request'

// 供应商发货列表查询
export function listVendorShipOrder(query) {
  return request({
    url: '/api/erp-api/ship/vendor/assigned_list',
    method: 'get',
    params: query
  })
}


// 获取共享给供应商电子面单
export function listWaybillAccountShareVendor(query) {
  return request({
    url: '/api/erp-api/ship/vendor/waybill_accounts_share_list',
    method: 'get',
    params: query
  })
}
// 供应商发货确认
export function supplierShipConfirm(data) {
  return request({
    url: '/api/erp-api/ship/vendor/supplier_ship_confirm',
    method: 'post',
    data: data
  })
}
