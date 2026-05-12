<template>
  <div class="shop-inventory-sharing">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>共享库存查询</h2>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-view" @click="refresh">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="SKU编码/名称">
        <el-input v-model="searchForm.keyword" placeholder="请输入SKU编码或名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="条码">
        <el-input v-model="searchForm.barcode" placeholder="请输入条码" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
        <el-button icon="el-icon-refresh-right" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 门店库存展示（卡片式分组） -->
    <div v-loading="loading">
      <el-collapse v-model="activeShops" accordion>
        <el-collapse-item v-for="shop in shopInventoryList" :key="shop.shopId" :name="shop.shopId">
          <template slot="title">
            <div class="shop-title">
              <span class="shop-name">{{ shop.shopName }}</span>
              <span class="visible-scope-tag">
                <el-tag size="small" :type="shop.visibleScope === 2 ? 'success' : 'info'">
                  {{ shop.visibleScope === 1 ? '仅汇总可见' : '批次明细可见' }}
                </el-tag>
              </span>
              <span class="inventory-summary" v-if="shop.visibleScope === 1">
                共 {{ shop.skuList.length }} 个SKU
              </span>
            </div>
          </template>

          <!-- 汇总模式（scope=1）显示SKU列表 -->
          <el-table v-if="shop.visibleScope === 1" :data="shop.skuList" border stripe size="small">
            <el-table-column prop="skuCode" label="SKU编码" width="150"></el-table-column>
            <el-table-column prop="skuName" label="商品名称" min-width="180"></el-table-column>
            <el-table-column prop="availableQuantity" label="可用库存" width="120" align="right">
              <template slot-scope="scope">
                <span class="quantity">{{ scope.row.availableQuantity }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="applyLock(shop.shopId, null, scope.row)">
                  申请锁库
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 明细模式（scope=2）显示批次明细 -->
          <el-table v-if="shop.visibleScope === 2" :data="shop.batchList" border stripe size="small">
            <el-table-column prop="barcode" label="条码" width="150"></el-table-column>
            <el-table-column prop="skuCode" label="SKU编码" width="120"></el-table-column>
            <el-table-column prop="skuName" label="商品名称" min-width="150"></el-table-column>
            <el-table-column prop="actualGoldWeight" label="金重(g)" width="100" align="right"></el-table-column>
            <el-table-column prop="actualSilverWeight" label="银重(g)" width="100" align="right"></el-table-column>
            <el-table-column prop="laborCost" label="工费(元)" width="100" align="right"></el-table-column>
            <el-table-column prop="availableQuantity" label="可用数量" width="100" align="right">
              <template slot-scope="scope">
                <span class="quantity">{{ scope.row.availableQuantity }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="applyLock(shop.shopId, scope.row.batchId, null, scope.row)">
                  申请锁库
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div v-if="!shop.skuList && !shop.batchList" class="empty-tip">暂无库存数据</div>
        </el-collapse-item>
      </el-collapse>

      <el-empty v-if="!loading && shopInventoryList.length === 0" description="暂无共享库存，请先完成授权"></el-empty>
    </div>
  </div>
</template>

<script>
import * as shareApi from '@/api/shop/share'

export default {
  name: 'ShopInventorySharing',
  data() {
    return {
      loading: false,
      searchForm: {
        keyword: '',
        barcode: ''
      },
      activeShops: [],
      shopInventoryList: []
    }
  },
  created() {
    this.loadInventory()
  },
  methods: {
    loadInventory() {
      this.loading = true
      const params = {
        page: 1,
        limit: 100,
        keyword: this.searchForm.keyword
      }
      shareApi.getSharedInventory(params).then(res => {
        if (res.code === 200 && res.rows) {
          // 按店铺分组整理数据
          const shopMap = {}
          res.rows.forEach(item => {
            if (!shopMap[item.shopId]) {
              shopMap[item.shopId] = {
                shopId: item.shopId,
                shopName: item.shopName || '门店' + item.shopId,
                visibleScope: 2, // 默认批次明细可见
                skuList: [],
                batchList: []
              }
            }
            // 将库存数据添加到对应店铺的skuList中
            shopMap[item.shopId].skuList.push({
              skuCode: item.skuCode,
              skuName: item.skuName,
              availableQuantity: item.quantity,
              goodsName: item.goodsName,
              goodsNum: item.goodsNum
            })
          })
          // 转换为数组
          this.shopInventoryList = Object.values(shopMap)
          if (this.shopInventoryList.length > 0) {
            this.activeShops = [this.shopInventoryList[0].shopId]
          }
        }
      }).finally(() => {
        this.loading = false
      })
    },
    search() {
      this.loadInventory()
    },
    resetSearch() {
      this.searchForm.keyword = ''
      this.searchForm.barcode = ''
      this.loadInventory()
    },
    refresh() {
      this.loadInventory()
    },
    // 申请锁库：携带参数跳转到锁库申请页面
    applyLock(shopId, batchId, skuInfo, batchDetail = null) {
      // 构造跳转参数
      let query = {
        targetShopId: shopId,
        sourceType: batchId ? 'batch' : 'sku'
      }
      if (batchId) {
        query.batchId = batchId
        if (batchDetail) {
          query.barcode = batchDetail.barcode
          query.skuName = batchDetail.skuName
        }
      } else if (skuInfo) {
        query.skuCode = skuInfo.skuCode
        query.skuName = skuInfo.skuName
        query.availableQuantity = skuInfo.availableQuantity
      }
      this.$router.push({
        path: '/shop/cross-shop-transfer',
        query: query
      })
    }
  }
}
</script>

<style scoped>
.shop-inventory-sharing {
  padding: 20px;
  background-color: #fff;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
  padding-bottom: 10px;
}
.page-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}
.search-form {
  margin-bottom: 20px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
}
.shop-title {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 15px;
}
.shop-name {
  font-weight: bold;
  font-size: 16px;
}
.visible-scope-tag {
  margin-left: 10px;
}
.inventory-summary {
  font-size: 12px;
  color: #909399;
}
.quantity {
  font-weight: bold;
  color: #409eff;
}
.empty-tip {
  padding: 20px;
  text-align: center;
  color: #909399;
}
</style>
