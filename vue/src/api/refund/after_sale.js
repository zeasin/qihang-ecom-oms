import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/afterSale/list',
    method: 'get',
    params: query
  })
}

// 新增补发
export function addHandle (data) {
  return request({
    url: '/afterSale/addHandle',
    method: 'post',
    data: data
  })
}
