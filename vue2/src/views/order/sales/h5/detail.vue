<template>
  <div class="h5-page">
    <van-nav-bar
      title="订单详情"
      left-arrow
      @click-left="onBack"
    />

    <div class="content-wrapper" v-if="order">
      <div class="section">
        <div class="section-title">
          <van-icon name="map-o" class="title-icon" />
          <span>收货信息</span>
        </div>
        <van-cell-group inset class="cell-group">
          <van-cell title="收件人" :value="order.receiverName" />
          <van-cell title="手机号" :value="order.receiverMobile" />
          <van-cell title="收货地址" :value="fullAddress" />
        </van-cell-group>
      </div>

      <div class="section">
        <div class="section-title">
          <van-icon name="shop-o" class="title-icon" />
          <span>订单信息</span>
        </div>
        <van-cell-group inset class="cell-group">
          <van-cell title="订单编号" :value="order.orderNum" />
          <van-cell title="订单状态">
            <template #value>
              <span class="order-status" :class="'status-' + order.orderStatus">{{ getStatusText(order.orderStatus) }}</span>
            </template>
          </van-cell>
          <van-cell title="下单时间" :value="formatTime(order.createTime)" />
          <van-cell title="销售员" :value="order.salesmanName || '-'" />
        </van-cell-group>
      </div>

      <div class="section">
        <div class="section-title">
          <van-icon name="goods" class="title-icon" />
          <span>商品明细</span>
        </div>
        <div class="goods-list">
          <div
            v-for="item in order.itemList"
            :key="item.id"
            class="goods-card"
          >
            <div class="goods-header">
              <img :src="item.goodsImg" alt="" class="goods-img" />
              <div class="goods-info">
                <div class="goods-name">{{ item.goodsTitle }}</div>
                <div class="goods-sku">{{ item.goodsSpec }}</div>
              </div>
              <div class="goods-price">¥{{ item.goodsPrice.toFixed(2) }}</div>
            </div>
            <div class="goods-footer">
              <span class="goods-num">x{{ item.quantity }}</span>
              <span class="goods-amount">小计：¥{{ item.itemAmount.toFixed(2) }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="section" v-if="order.remark">
        <div class="section-title">
          <van-icon name="comment-o" class="title-icon" />
          <span>备注信息</span>
        </div>
        <van-cell-group inset class="cell-group">
          <van-cell>
            <div class="remark-text">{{ order.remark }}</div>
          </van-cell>
        </van-cell-group>
      </div>

      <div class="section">
        <div class="section-title">
          <van-icon name="paid" class="title-icon" />
          <span>费用信息</span>
        </div>
        <van-cell-group inset class="cell-group">
          <van-cell title="商品总额">
            <template #value>
              <span class="amount">¥{{ order.goodsAmount.toFixed(2) }}</span>
            </template>
          </van-cell>
          <van-cell title="运费">
            <template #value>
              <span class="amount">¥{{ order.postFee || '0.00' }}</span>
            </template>
          </van-cell>
          <van-cell title="优惠">
            <template #value>
              <span class="amount">-¥{{ order.sellerDiscount || '0.00' }}</span>
            </template>
          </van-cell>
          <van-cell title="实付金额">
            <template #value>
              <span class="amount-total">¥{{ order.payment.toFixed(2) }}</span>
            </template>
          </van-cell>
        </van-cell-group>
      </div>
    </div>

    <van-loading v-if="loading" class="loading-center" type="spinner" />
  </div>
</template>

<script>
import { h5Detail } from '@/api/h5/sales_order'

export default {
  name: 'H5SalesOrderDetail',
  data() {
    return {
      loading: false,
      order: null
    }
  },
  computed: {
    fullAddress() {
      if (!this.order) return ''
      return [this.order.province, this.order.city, this.order.town, this.order.address].filter(Boolean).join('')
    }
  },
  created() {
    this.loadDetail()
  },
  methods: {
    onBack() {
      this.$router.go(-1)
    },
    loadDetail() {
      const id = this.$route.params.id
      if (!id) {
        this.$toast('参数错误')
        this.$router.go(-1)
        return
      }
      this.loading = true
      h5Detail(id).then(res => {
        if (res.code === 200) {
          this.order = res.data
        } else {
          this.$toast(res.msg || '获取详情失败')
        }
      }).finally(() => {
        this.loading = false
      })
    },
    formatTime(time) {
      if (!time) return '-'
      const date = new Date(time)
      return date.toLocaleString()
    },
    getStatusText(status) {
      const map = {
        0: '待审核',
        1: '待发货',
        2: '已发货',
        3: '已完成',
        11: '已取消'
      }
      return map[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.h5-page {
  height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.section {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #333;

  .title-icon {
    margin-right: 8px;
    color: #4e6cf7;
  }
}

.cell-group {
  background: #f8f9fa;
  border-radius: 8px;
}

.order-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
}

.status-0 { background: #e6a23c; color: #fff; }
.status-1 { background: #409eff; color: #fff; }
.status-2 { background: #67c23a; color: #fff; }
.status-3 { background: #909399; color: #fff; }
.status-11 { background: #f56c6c; color: #fff; }

.goods-list {
  background: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
}

.goods-card {
  margin-bottom: 12px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  &:last-child {
    margin-bottom: 0;
  }
}

.goods-header {
  display: flex;
  align-items: center;
  padding: 12px;

  .goods-img {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    object-fit: cover;
  }

  .goods-info {
    flex: 1;
    margin-left: 12px;
    overflow: hidden;
  }

  .goods-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .goods-sku {
    font-size: 12px;
    color: #999;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .goods-price {
    font-size: 14px;
    font-weight: 600;
    color: #333;
  }
}

.goods-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: #fafafa;
  border-top: 1px solid #eee;

  .goods-num {
    font-size: 12px;
    color: #999;
  }

  .goods-amount {
    font-size: 14px;
    font-weight: 600;
    color: #ee0a24;
  }
}

.remark-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.amount {
  color: #333;
}

.amount-total {
  font-size: 18px;
  font-weight: 700;
  color: #ee0a24;
}

.loading-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
