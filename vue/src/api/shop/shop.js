import request from '@/utils/request'

// 查询店铺列表
export function listShop(query) {
  return request({
    url: '/shop/list',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getShop(id) {
  return request({
    url: '/shop/shop/' + id,
    method: 'get'
  })
}

// 新增店铺
export function addShop(data) {
  return request({
    url: '/shop/shop',
    method: 'post',
    data: data
  })
}

// 修改店铺
export function updateShop(data) {
  return request({
    url: '/shop/shop',
    method: 'put',
    data: data
  })
}

// 删除店铺
export function delShop(id) {
  return request({
    url: '/shop/shop/' + id,
    method: 'delete'
  })
}

// 查询店铺平台列表
export function listPlatform(query) {
  return request({
    url: '/shop/platformList',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getPlatform(id) {
  return request({
    url: '/shop/platform/' + id,
    method: 'get'
  })
}

// 修改店铺
export function updatePlatform(data) {
  return request({
    url: '/shop/platform',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changePlatformStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/shop/platform/changeStatus',
    method: 'put',
    data: data
  })
}


// export function listLogistics(query) {
//   return request({
//     url: '/shop/logistics',
//     method: 'get',
//     params: query
//   })
// }
// export function listLogisticsStatus(query) {
//   return request({
//     url: '/shop/logistics_status',
//     method: 'get',
//     params: query
//   })
// }
//
// // 新增物流公司
// export function addLogistics(data) {
//   return request({
//     url: '/shop/logistics/add',
//     method: 'post',
//     data: data
//   })
// }
//
// // 查询物流公司详细
// export function getLogistics(id) {
//   return request({
//     url: '/logistics/' + id,
//     method: 'get'
//   })
// }
//
//
// // 修改物流公司
// export function updateLogistics(data) {
//   return request({
//     url: '/logistics',
//     method: 'put',
//     data: data
//   })
// }
//
// // 删除物流公司
// export function delLogistics(id) {
//   return request({
//     url: '/logistics/' + id,
//     method: 'delete'
//   })
// }
//
//
export function listShopPullLogs(query) {
  return request({
    url: '/shop/pull_logs_list',
    method: 'get',
    params: query
  })
}
// // 修改物流状态
// export function updateStatus(data) {
//   return request({
//     url: '/shop/logistics/updateStatus',
//     method: 'put',
//     data: data
//   })
// }
