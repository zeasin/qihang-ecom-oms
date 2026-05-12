<template>
  <van-popup v-model="show" position="bottom" :style="{ height: '85%' }" round>
    <div class="goods-selector">
      <div class="selector-header">
        <van-nav-bar title="选择商品" left-arrow @click-left="close" class="nav-bar" />
        
        <van-search 
          v-model="searchText" 
          placeholder="搜索商品名称或编码" 
          @search="onSearch"
          show-action
        />
      </div>

      <div class="selector-body">
        <van-tabs v-model="activeTab" @change="onTabChange" class="tabs">
          <van-tab title="全部商品" name="all">
            <div class="tab-content">
              <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
                <van-list 
                  v-model="loading" 
                  :finished="finished" 
                  finished-text="没有更多了"
                  @load="loadMore"
                >
                <van-checkbox-group v-model="selectedGoods">
                  <div 
                    v-for="item in goodsList" 
                    :key="item.id" 
                    class="goods-item"
                  >
                    <van-checkbox :name="item.id" shape="circle" class="checkbox" />
                    <img :src="item.colorImage" alt="" class="goods-img" />
                    <div class="goods-info">
                      <div class="goods-name">{{ item.goodsName }}</div>
                      <div class="goods-sku">{{ item.skuName }}</div>
                      <div class="goods-price">¥{{ item.retailPrice.toFixed(2) }}</div>
                    </div>
                  </div>
                </van-checkbox-group>
                
                <div v-if="goodsList.length === 0 && !loading" class="empty-content">
                  <van-icon name="search" size="48" color="#ccc" />
                  <p>暂无商品</p>
                </div>
              </van-list>
            </van-pull-refresh>
            </div>
          </van-tab>
          
          <van-tab title="已选择" name="selected">
            <div v-if="selectedList.length === 0" class="empty-content">
              <van-icon name="shopping-cart-o" size="48" color="#ccc" />
              <p>还没有选择商品</p>
            </div>
            
            <van-cell-group v-else>
              <van-cell 
                v-for="(item, index) in selectedList" 
                :key="item.id" 
                class="selected-item"
              >
                <template #default>
                  <img :src="item.colorImage" alt="" class="goods-img" />
                  <div class="goods-info">
                    <div class="goods-name">{{ item.goodsName }}</div>
                    <div class="goods-sku">{{ item.skuName }}</div>
                    <div class="goods-price">¥{{ item.retailPrice.toFixed(2) }}</div>
                  </div>
                  <van-button size="mini" type="danger" @click="removeSelected(index)">删除</van-button>
                </template>
              </van-cell>
            </van-cell-group>
          </van-tab>
        </van-tabs>
      </div>

      <div class="selector-footer">
        <div class="select-info">
          <span>已选 {{ selectedGoods.length }} 件商品</span>
        </div>
        <van-button type="primary" size="large" @click="confirmSelect" :disabled="selectedGoods.length === 0">
          确认添加
        </van-button>
      </div>
    </div>
  </van-popup>
</template>

<script>
import { listGoodsSpec } from '@/api/goods/goodsSpec'

export default {
  name: 'GoodsSelector',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      show: false,
      activeTab: 'all',
      searchText: '',
      goodsList: [],
      selectedGoods: [],
      pageNum: 1,
      pageSize: 10,
      loading: false,
      finished: false,
      refreshing: false
    }
  },
  computed: {
    selectedList() {
      return this.goodsList.filter(item => this.selectedGoods.includes(item.id))
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.show = val
        if (val) {
          this.initData()
        }
      }
    }
  },
  methods: {
    close() {
      this.show = false
      this.$emit('update:visible', false)
    },
    initData() {
      this.goodsList = []
      this.selectedGoods = []
      this.pageNum = 1
      this.finished = false
      this.loadGoods()
    },
    loadGoods() {
      this.loading = true
      return listGoodsSpec({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        goodsName: this.searchText || undefined,
        skuCode: this.searchText || undefined
      }).then(res => {
        if (res.rows && res.rows.length > 0) {
          console.log('API返回的数据结构:', res.rows[0])
          console.log('字段列表:', Object.keys(res.rows[0]))
          this.goodsList = [...this.goodsList, ...res.rows]
          if (res.rows.length < this.pageSize) {
            this.finished = true
          }
        } else {
          this.finished = true
        }
        this.loading = false
        return res
      }).catch(() => {
        this.loading = false
      })
    },
    loadMore() {
      if (!this.finished && !this.loading) {
        this.pageNum++
        this.loadGoods()
      }
    },
    onRefresh() {
      this.pageNum = 1
      this.finished = false
      this.goodsList = []
      this.loadGoods().then(() => {
        this.refreshing = false
      }).catch(() => {
        this.refreshing = false
      })
    },
    onSearch() {
      this.initData()
    },
    onTabChange() {
    },
    removeSelected(index) {
      const item = this.selectedList[index]
      const idx = this.selectedGoods.indexOf(item.id)
      if (idx > -1) {
        this.selectedGoods.splice(idx, 1)
      }
    },
    confirmSelect() {
      const selectedItems = this.goodsList.filter(item => this.selectedGoods.includes(item.id))
      console.log('要传递的商品数据:', selectedItems)
      this.$emit('confirm', selectedItems)
      this.close()
    }
  }
}
</script>

<style lang="scss" scoped>
.goods-selector {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.nav-bar {
  background: #fff;
  border-bottom: 1px solid #eee;
  
  .van-nav-bar__title {
    color: #333;
  }
}

.selector-header {
  background: #fff;
  padding-bottom: 8px;
}

.selector-body {
  flex: 1;
  overflow: hidden;
  background: #f5f7fa;
}

.tabs {
  height: 100%;
}

.tab-content {
  height: calc(100vh - 200px);
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.goods-item {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  
  .checkbox {
    flex-shrink: 0;
    margin-right: 12px;
  }
  
  .goods-img {
    flex-shrink: 0;
    width: 60px;
    height: 60px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 12px;
  }
  
  .goods-info {
    flex: 1;
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
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .goods-price {
    font-size: 15px;
    font-weight: 600;
    color: #ee0a24;
  }
}

.selected-item {
  display: flex;
  align-items: center;
  background: #fff;
  
  .goods-img {
    width: 50px;
    height: 50px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 12px;
  }
  
  .goods-info {
    flex: 1;
    overflow: hidden;
  }
  
  .goods-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin-bottom: 3px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .goods-sku {
    font-size: 12px;
    color: #999;
    margin-bottom: 3px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .goods-price {
    font-size: 14px;
    font-weight: 600;
    color: #ee0a24;
  }
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
  
  p {
    margin-top: 12px;
    color: #999;
    font-size: 14px;
  }
}

.selector-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #eee;
  
  .select-info {
    font-size: 14px;
    color: #666;
  }
  
  .van-button {
    width: 140px;
    border-radius: 25px;
  }
}
</style>