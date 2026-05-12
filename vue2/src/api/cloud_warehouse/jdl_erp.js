import request from '@/utils/request'

// 接口拉取京东云仓商品-ERP
export function pullJdCloudWarehouseGoodsItems(data) {
  return request({
    url: '/api/erp-api/cloudWarehouse/jd/goods/pull_goods_items',
    method: 'post',
    data: data
  })
}

