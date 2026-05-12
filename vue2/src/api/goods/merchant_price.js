import request from '@/utils/request'

// 商户商品列表
export function listMerchantGoodsPriceList(query) {
  return request({
    url: '/api/erp-api/merchant/goodsPrice/list',
    method: 'get',
    params: query
  })
}



// 删除商品报价
export function delGoodsPrice(id) {
  return request({
    url: '/api/erp-api/merchant/goodsPrice/del/' + id,
    method: 'delete'
  })
}

// 新增渠道商品价格
export function addGoodsPrice(data) {
  return request({
    url: '/api/erp-api/merchant/goodsPrice/addPrice',
    method: 'post',
    data: data
  })
}
