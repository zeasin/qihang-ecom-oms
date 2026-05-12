import request from '@/utils/request'

// 接口拉取京东云仓商品-仓配一体
export function pullJdlGoodsItems(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/goods/pull_goods_items_new',
    method: 'post',
    data: data
  })
}

// 更新JD云仓基本信息
export function updateJdlBaseInfo(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/get_base_info',
    method: 'post',
    data: data
  })
}
