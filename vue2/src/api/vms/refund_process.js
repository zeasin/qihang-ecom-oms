import request from '@/utils/request'

// 查询列表
export function list(query) {
  return request({
    url: '/api/vms-api/salesAfter/processRecordList',
    method: 'get',
    params: query
  })
}



