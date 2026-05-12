<template>
  <div class="sales-order-page">
    <van-nav-bar
      title="内销订单"
      left-arrow
      @click-left="onBack"
    />

    <div class="content-wrapper">
      <van-form @submit="submitOrder" class="order-form">
      <div class="section">
        <div class="section-title">
          <van-icon name="shop-o" class="title-icon" />
          <span>选择商户</span>
        </div>

        <van-cell-group inset class="cell-group">
          <van-field
            v-model="merchantText"
            placeholder="请选择商户"
            readonly
            clickable
            @click="showMerchantPicker = true"
            left-icon="store-o"
          />
          <van-field
            v-model="shopText"
            placeholder="请选择店铺"
            readonly
            clickable
            @click="showShopPicker = true"
            left-icon="building-o"
          />
        </van-cell-group>
      </div>
        <div class="section">
          <div class="section-title">
            <van-icon name="map-o" class="title-icon" />
            <span>收货信息</span>
          </div>

          <van-cell-group inset class="cell-group">
            <van-field
              v-model="form.receiverName"
              placeholder="请输入收件人姓名"
              left-icon="user-o"
              required
            />
            <van-field
              v-model="form.receiverPhone"
              placeholder="请输入手机号"
              left-icon="phone"
              type="number"
              maxlength="11"
              required
            />
            <van-field
              v-model="form.address"
              placeholder="请输入详细地址"
              left-icon="location-o"
              type="textarea"
              rows="2"
              required
            />
          </van-cell-group>
        </div>
      <div class="section">
        <div class="section-title">
          <van-icon name="gift-o" class="title-icon" />
          <span>选择套餐</span>
          <span class="subtitle">（可多选）</span>
        </div>

        <div class="package-list">
          <van-checkbox-group v-model="selectedPackages">
            <div
              v-for="pkg in packageList"
              :key="pkg.id"
              class="package-item"
            >
              <van-checkbox :name="pkg.id" shape="circle" />
              <div class="package-info">
                <div class="package-name">{{ pkg.packageName }}</div>
                <div class="package-desc">{{ pkg.remark || '暂无描述' }}</div>
              </div>
            </div>
          </van-checkbox-group>
        </div>
      </div>

      <div class="section">
        <div class="section-title">
          <van-icon name="goods" class="title-icon" />
          <span>商品明细</span>
          <van-button
            size="mini"
            type="primary"
            icon="plus"
            @click.stop="openGoodsPicker"
            native-type="button"
            class="add-btn"
          >添加</van-button>
        </div>

        <div v-if="goodsList.length === 0" class="empty-state">
          <van-icon name="shopping-cart-o" size="48" color="#ccc" />
          <p>暂无商品</p>
        </div>

        <div
          v-for="(item, index) in goodsList"
          :key="index"
          class="goods-card"
        >
          <div class="goods-header">
            <img :src="item.colorImage" alt="" class="goods-img" />
            <div class="goods-info">
              <div class="goods-name">{{ item.goodsName }}</div>
              <div class="goods-sku">{{ item.skuName }}</div>
            </div>
            <van-button size="mini" type="danger" @click="removeGoods(index)">删除</van-button>
          </div>
          <div class="goods-footer">
            <div class="goods-price">¥{{ item.retailPrice.toFixed(2) }}</div>
            <van-stepper
              v-model="item.quantity"
              min="1"
              @change="onQuantityChange"
            />
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-title">
          <van-icon name="comment-o" class="title-icon" />
          <span>备注信息</span>
        </div>

        <van-cell-group inset class="cell-group">
          <van-field
            v-model="form.remark"
            placeholder="选填，如有特殊要求请备注"
            type="textarea"
            rows="2"
          />
        </van-cell-group>
      </div>
    </van-form>

    <div class="submit-bar">
      <div class="total-info">
        <span class="total-label">订单总额</span>
        <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <van-button
        type="primary"
        size="large"
        @click="submitOrder"
        :loading="submitting"
        :disabled="!canSubmit"
        class="submit-btn"
      >
        {{ submitting ? '提交中...' : '提交订单' }}
      </van-button>
    </div>

    <van-action-sheet v-model="showMerchantPicker" title="选择商户">
      <van-picker :columns="merchantColumns" @change="onMerchantChange" />
    </van-action-sheet>

    <van-action-sheet v-model="showShopPicker" title="选择店铺">
      <van-picker :columns="shopColumns" @change="onShopChange" />
    </van-action-sheet>

    <GoodsSelector :visible.sync="showGoodsSelector" @confirm="handleGoodsSelect" />
    </div>
  </div>
</template>

<script>
import { h5Create } from '@/api/h5/sales_order'
import { listAllMerchant } from '@/api/shop/merchant'
import { listShop } from '@/api/shop/shop'
import { list as listPackage, getItems } from '@/api/order/sales_goods_package'
import GoodsSelector from './GoodsSelector.vue'

export default {
  name: 'H5SalesOrderCreate',
  components: { GoodsSelector },
  data() {
    return {
      submitting: false,
      showMerchantPicker: false,
      showShopPicker: false,
      showGoodsSelector: false,
      merchantList: [],
      shopList: [],
      packageList: [],
      selectedPackages: [],
      goodsList: [],
      form: {
        receiverName: '',
        receiverPhone: '',
        province: '',
        city: '',
        district: '',
        address: '',
        merchantId: null,
        merchantName: '',
        shopId: null,
        shopName: '',
        remark: ''
      }
    }
  },
  computed: {
    merchantColumns() {
      return this.merchantList.map(m => ({ text: m.name, value: m.id }))
    },
    shopColumns() {
      return this.shopList.map(s => ({ text: s.name, value: s.id }))
    },
    merchantText() {
      return this.form.merchantName || '请选择商户'
    },
    shopText() {
      return this.form.shopName || '请选择店铺'
    },
    totalAmount() {
      return this.goodsList.reduce((sum, item) => {
        return sum + (item.retailPrice || 0) * (item.quantity || 0)
      }, 0)
    },
    canSubmit() {
      return (
        this.form.receiverName &&
        this.form.receiverPhone &&
        this.form.address &&
        this.form.merchantId &&
        this.form.shopId &&
        this.goodsList.length > 0
      )
    }
  },
  created() {
    this.loadPackages()
    this.loadMerchants()
  },
  watch: {
    selectedPackages: {
      handler() {
        this.loadPackageGoods()
      },
      deep: true
    }
  },
  methods: {
    onBack() {
      this.$router.replace('/h5/sales/list')
    },
    loadPackages() {
      listPackage({ status: 1 }).then(res => {
        this.packageList = res.rows || []
      })
    },
    loadMerchants() {
      listAllMerchant().then(res => {
        this.merchantList = (res.data || []).filter(m => m.id !== 0)
        if (this.merchantList.length > 0) {
          this.form.merchantId = this.merchantList[0].id
          this.form.merchantName = this.merchantList[0].name
          this.loadShops()
        }
      })
    },
    loadShops() {
      if (!this.form.merchantId) return
      listShop({ merchantId: this.form.merchantId }).then(res => {
        this.shopList = res.rows || []
        if (this.shopList.length > 0) {
          this.form.shopId = this.shopList[0].id
          this.form.shopName = this.shopList[0].name
          this.onShopChange(null,this.shopList[0].id)
        }
      })
    },
    onMerchantChange(picker, value) {
      const merchant = this.merchantList.find(m => m.id === value.value)
      if (merchant) {
        this.form.merchantId = merchant.id
        this.form.merchantName = merchant.name
        this.form.shopId = null
        this.form.shopName = ''
        this.loadShops()
      }
      this.showMerchantPicker = false
    },
    onShopChange(picker, value) {
      console.log('====店铺变化，',value)
      const shop = this.shopList.find(s => s.id === value)
      if (shop) {
        this.form.shopId = shop.id
        this.form.shopName = shop.name
        this.form.province = shop.province
        this.form.city = shop.city
        this.form.district = shop.district
        this.form.address = (this.form.province ? this.form.province:'') + (this.form.city ? this.form.city:'')+ (this.form.district ? this.form.district:'')+shop.address
        this.form.receiverName = shop.contact
        this.form.receiverPhone = shop.phone
      }
      this.showShopPicker = false
    },
    openGoodsPicker() {
      this.showGoodsSelector = true
    },
    handleGoodsSelect(goods) {
      console.log('接收到的商品数据:', goods)
      const newGoods = []
      let hasDuplicate = false
      goods.forEach(item => {
        const exists = this.goodsList.some(g => g.goodsSkuId == item.id)
        if (!exists) {
          newGoods.push({
            goodsId: item.goodsId || item.goods_id,
            goodsSkuId: item.id,
            goodsName: item.goodsName || item.productName || item.name || '未知商品',
            skuName: item.skuName || item.specName || item.spec || '未知规格',
            skuCode: item.skuCode || item.sku_code || '',
            colorImage: item.colorImage || item.imageUrl || item.imgUrl || '',
            quantity: 1,
            retailPrice: item.retailPrice || item.price || 0
          })
        } else {
          hasDuplicate = true
        }
      })
      if (hasDuplicate && newGoods.length === 0) {
        this.$toast('不能添加相同的商品')
      } else if (newGoods.length > 0) {
        if (hasDuplicate) {
          this.$toast('部分商品已存在，未重复添加')
        }
        this.goodsList = [...this.goodsList, ...newGoods]
      }
      console.log('当前商品列表:', this.goodsList)
    },
    loadPackageGoods() {
      const packageIds = [...this.selectedPackages]
      if (packageIds.length === 0) {
        this.goodsList = this.goodsList.filter(g => !g.fromPackage)
        return
      }

      const promises = packageIds.map(packageId => getItems(packageId))
      Promise.all(promises).then(results => {
        const newPackageGoods = []
        results.forEach(res => {
          const items = res.data || []
          items.forEach(item => {
            const exists = this.goodsList.some(g => g.goodsSkuId === item.goodsSkuId)
            if (!exists) {
              newPackageGoods.push({
                goodsId: item.goodsId,
                goodsSkuId: item.goodsSkuId,
                goodsName: item.goodsName,
                skuName: item.skuName,
                skuCode: item.skuCode,
                colorImage: item.skuImage || '', // 套餐商品使用 skuImage 字段
                quantity: item.quantity || 1,
                retailPrice: item.retailPrice || 0,
                fromPackage: true
              })
            }
          })
        })
        this.goodsList = [...this.goodsList.filter(g => !g.fromPackage), ...newPackageGoods]
        console.log('套餐商品已添加:', this.goodsList)
      })
    },
    removeGoods(index) {
      this.goodsList.splice(index, 1)
    },
    onQuantityChange() {
    },
    submitOrder() {
      if (!this.canSubmit) {
        if (!this.form.receiverName || !this.form.receiverPhone || !this.form.address) {
          this.$toast('请填写完整收货信息')
        } else if (!this.form.merchantId || !this.form.shopId) {
          this.$toast('请选择商户和店铺')
        } else if (this.goodsList.length === 0) {
          this.$toast('请添加商品')
        }
        return
      }

      this.submitting = true

      const data = {
        orderType: 0,
        customerType: 20,
        merchantId: this.form.merchantId,
        merchantName: this.form.merchantName,
        shopId: this.form.shopId,
        shopName: this.form.shopName,
        receiverName: this.form.receiverName,
        receiverPhone: this.form.receiverPhone,
        address: this.form.address,
        remark: this.form.remark,
        itemList: this.goodsList.map(item => ({
          goodsId: item.goodsId,
          goodsSkuId: item.goodsSkuId,
          goodsName: item.goodsName,
          skuName: item.skuName,
          skuCode: item.skuCode,
          colorImage: item.colorImage,
          quantity: item.quantity,
          retailPrice: item.retailPrice
        }))
      }

      h5Create(data).then(res => {
        if (res.code === 200) {
          this.$toast.success('提交成功')
          setTimeout(() => {
            this.$router.push('/h5/sales/list')
          }, 1500)
        } else {
          this.$toast(res.msg || '提交失败')
        }
      }).finally(() => {
        this.submitting = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.sales-order-page {
  height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.order-form {
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
  justify-content: space-between;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 600;
  color: #333;

  .title-icon {
    margin-right: 8px;
    color: #4e6cf7;
  }

  .subtitle {
    font-size: 12px;
    font-weight: 400;
    color: #999;
    margin-left: 4px;
  }

  .add-btn {
    border-radius: 16px;
    background: linear-gradient(135deg, #4e6cf7 0%, #6b8cff 100%);
  }
}

.cell-group {
  background: #f8f9fa;
  border-radius: 8px;
}

.package-list {
  background: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
}

.package-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  &:last-child {
    border-bottom: none;
  }

  .van-checkbox {
    flex-shrink: 0;
  }

  .package-info {
    flex: 1;
    margin-left: 12px;
    text-align: left;
  }

  .package-name {
    font-size: 15px;
    font-weight: 500;
    color: #333;
    margin-bottom: 2px;
  }

  .package-desc {
    font-size: 12px;
    color: #999;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  background: #f8f9fa;
  border-radius: 8px;

  p {
    margin: 12px 0 16px;
    color: #999;
    font-size: 14px;
  }
}

.goods-card {
  margin-bottom: 12px;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid #eee;
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
}

.goods-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background: #fafafa;

  .goods-price {
    font-size: 16px;
    font-weight: 600;
    color: #ee0a24;
  }
}

.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.08);
}

.total-info {
  display: flex;
  flex-direction: column;

  .total-label {
    font-size: 12px;
    color: #999;
  }

  .total-price {
    font-size: 22px;
    font-weight: 700;
    color: #ee0a24;
  }
}

.submit-btn {
  width: 140px;
  border-radius: 25px;
  background: linear-gradient(135deg, #4e6cf7 0%, #6b8cff 100%);
  border: none;

  &:disabled {
    background: #ccc;
  }
}
</style>
