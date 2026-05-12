import request from '@/utils/request'
import axios from 'axios';
// 查询拼多多订单列表
export function listOrder(query,header) {
  return request({
    url: '/api/oms-api/pdd/order/list',
    method: 'get',
    params: query,
    headers: {
      ...axios.defaults.headers.common,  // 合并全局默认的 headers
      ...header  // 合并传入的 header，后者会覆盖前者
    }
  })
}

// 查询拼多多订单详细
export function getOrder(id) {
  return request({
    url: '/api/oms-api/pdd/order/' + id,
    method: 'get'
  })
}



export function pushOms(data) {
  return request({
    url: '/api/oms-api/pdd/order/push_oms',
    method: 'post',
    data: data
  })
}

// 接口拉取拼多多订单
export function pullOrder(data) {
  return request({
    url: '/api/oms-api/pdd/order/pull_order',
    method: 'post',
    data: data
  })
}

export function pullOrderDetail(data) {
  return request({
    url: '/api/oms-api/pdd/order/pull_order_detail',
    method: 'post',
    data: data
  })
}
