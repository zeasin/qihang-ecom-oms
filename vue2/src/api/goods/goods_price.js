import request from '@/utils/request'

// 查询
export function listGoldPrice(query) {
  return request({
    url: '/api/erp-api/goods-price/list',
    method: 'get',
    params: query
  })
}


// 查询
export function getGoldPrice(id) {
  return request({
    url: '/api/erp-api/goods-price/' + id,
    method: 'get'
  })
}

// 新增
export function addGoldPrice(data) {
  return request({
    url: '/api/erp-api/goods-price/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateGoldPrice(data) {
  return request({
    url: '/api/erp-api/goods-price/update',
    method: 'post',
    data: data
  })
}

// 删除
export function delGoldPrice(id) {
  return request({
    url: '/api/erp-api/goods-price/del/' + id,
    method: 'delete'
  })
}
