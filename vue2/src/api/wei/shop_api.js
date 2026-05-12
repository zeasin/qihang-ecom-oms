import request from '@/utils/request'


// 接口拉取
export function pullWeiLogisticsCompanyList(data) {
  return request({
    url: '/api/oms-api/wei/logistics/pull_logistics_company',
    method: 'post',
    data: data
  })
}
export function pullLogisticsTemplateListWei(data) {
  return request({
    url: '/api/oms-api/wei/logistics/pullTemplateList',
    method: 'post',
    data: data
  })
}
