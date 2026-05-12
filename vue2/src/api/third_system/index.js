import request from '@/utils/request'


export function getInternalSystemConfig(systemId) {
  return request({
    url: '/api/erp-api/third/systemConfig?systemId=' + systemId,
    method: 'get',
  })
}


export function saveInternalSystemConfig(data) {
  return request({
    url: '/api/erp-api/third/systemConfig/save',
    method: 'post',
    data: data
  })
}
