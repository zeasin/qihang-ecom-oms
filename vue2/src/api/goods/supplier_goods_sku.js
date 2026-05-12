import request from '@/utils/request'

// 查询供应商商品SKU列表
export function listSupplierGoodsSku(query) {
  return request({
    url: '/api/erp-api/supplier/goods_sku/list',
    method: 'get',
    params: query
  })
}

// 查询供应商商品SKU详情
export function getSupplierGoodsSku(id) {
  return request({
    url: '/api/erp-api/supplier/goods_sku/' + id,
    method: 'get'
  })
}

// 修改供应商商品SKU
export function updateSupplierGoodsSku(data) {
  return request({
    url: '/api/erp-api/supplier/goods_sku/edit',
    method: 'put',
    data: data
  })
}

// 删除供应商商品SKU
export function delSupplierGoodsSku(id) {
  return request({
    url: '/api/erp-api/supplier/goods_sku/del/' + id,
    method: 'delete'
  })
}

// 绑定商品库SKU
export function bindGoodsSku(data) {
  return request({
    url: '/api/erp-api/supplier/goods_sku/bind',
    method: 'put',
    data: data
  })
}
