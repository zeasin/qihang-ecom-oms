import request from '@/utils/request'

// 查询物流公司列表
export function listLogistics(query) {
  return request({
    url: '/api/erp-api/erp/logistics/list',
    method: 'get',
    params: query
  })
}

// 查询物流公司详细
export function getLogistics(id) {
  return request({
    url: '/api/erp-api/erp/logistics/' + id,
    method: 'get'
  })
}

// 新增物流公司
export function addLogistics(data) {
  return request({
    url: '/api/erp-api/erp/logistics/add',
    method: 'post',
    data: data
  })
}

// 修改物流公司
export function updateLogistics(data) {
  return request({
    url: '/api/erp-api/erp/logistics/update',
    method: 'put',
    data: data
  })
}

// 修改物流状态
export function updateStatus(data) {
  return request({
    url: '/api/erp-api/erp/logistics/updateStatus',
    method: 'put',
    data: data
  })
}

// 删除物流公司
export function delLogistics(id) {
  return request({
    url: '/api/erp-api/erp/logistics/del/' + id,
    method: 'delete'
  })
}
