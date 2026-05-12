<template>
  <div class="h5-page">
    <van-nav-bar title="我的订单">
      <template #right>
        <van-icon name="plus" size="18" @click="onCreate" />
      </template>
    </van-nav-bar>
    
    <van-tabs v-model="active" @change="onTabChange">
      <van-tab title="全部" name="" />
      <van-tab title="待审核" name="0" />
      <van-tab title="待发货" name="1" />
      <van-tab title="已发货" name="2" />
      <van-tab title="已完成" name="3" />
    </van-tabs>

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <van-cell-group>
          <van-cell v-for="order in orderList" :key="order.id" @click="onDetail(order)">
            <div class="order-item">
              <div class="order-header">
                <span class="order-num">{{ order.orderNum }}</span>
                <span class="order-status" :class="'status-' + order.orderStatus">{{ getStatusText(order.orderStatus) }}</span>
              </div>
              <div class="order-info">
                <div>{{ order.merchantName }}</div>
                <div>{{ order.shopName }}</div>
              </div>
              <div class="order-amount">
                <span>¥{{ order.amount }}</span>
              </div>
            </div>
          </van-cell>
        </van-cell-group>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script>
import { h5List } from '@/api/h5/sales_order'

export default {
  name: 'H5SalesOrderList',
  data() {
    return {
      active: '',
      refreshing: false,
      loading: false,
      finished: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      orderList: []
    }
  },
  methods: {
    onTabChange() {
      this.queryParams.pageNum = 1
      this.orderList = []
      this.finished = false
      this.loadData()
    },
    onRefresh() {
      this.queryParams.pageNum = 1
      this.orderList = []
      this.finished = false
      this.loadData()
    },
    onLoad() {
      this.loadData()
    },
    loadData() {
      const params = {
        ...this.queryParams,
        orderStatus: this.active || undefined
      }
      h5List(params).then(res => {
        const rows = res.rows || []
        if (this.queryParams.pageNum === 1) {
          this.orderList = rows
        } else {
          this.orderList.push(...rows)
        }
        this.loading = false
        this.refreshing = false
        if (rows.length < this.queryParams.pageSize) {
          this.finished = true
        } else {
          this.queryParams.pageNum++
        }
      }).catch(() => {
        this.loading = false
        this.refreshing = false
      })
    },
    onDetail(order) {
      this.$router.push(`/h5/sales/detail/${order.id}`)
    },
    onCreate() {
      this.$router.push('/h5/sales/create')
    },
    getStatusText(status) {
      const map = {
        0: '新订单',
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

<style scoped>
.order-item {
  width: 100%;
}
.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.order-num {
  font-size: 14px;
  color: #333;
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
.order-info {
  font-size: 12px;
  color: #666;
}
.order-amount {
  text-align: right;
  font-size: 16px;
  font-weight: bold;
  color: #f56c6c;
  margin-top: 8px;
}
</style>