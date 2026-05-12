import request from '@/utils/request'


// 接口拉取淘宝
export function pullLogisticsCompanyList(data) {
  return request({
    url: '/api/oms-api/dou/logistics/pull_logistics_company',
    method: 'post',
    data: data
  })
}
export function pullLogisticsTemplateListDou(data) {
  return request({
    url: '/api/oms-api/dou/logistics/pullTemplateList',
    method: 'post',
    data: data
  })
}
