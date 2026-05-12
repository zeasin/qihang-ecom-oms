<template>
  <div class="pos-order-creation">
    <el-row :gutter="20">
      <!-- 左侧：商品扫码与购物车 -->
      <el-col :span="14">
        <el-card class="scan-card" shadow="never">
          <div slot="header">
            <span>商品扫码添加</span>
          </div>
          <div class="scan-input">
            <el-input
              v-model="scanCode"
              placeholder="请扫描或输入商品条码/SKU编码"
              size="large"
              @keyup.enter.native="handleScan"
              clearable
              ref="scanInput"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleScan">查询</el-button>
            </el-input>
            <div class="scan-tip">支持普通商品条码/SKU，一物一码商品条码</div>
          </div>
          <div v-if="currentProduct" class="product-info">
            <el-alert
              :title="`商品：${currentProduct.skuName} (${currentProduct.skuCode})`"
              type="info"
              :closable="false"
            />
            <div class="product-detail">
              <div>金重：{{ currentProduct.goldWeight || '-' }}g</div>
              <div>银重：{{ currentProduct.silverWeight || '-' }}g</div>
              <div>工费：{{ currentProduct.laborCost || '-' }}元</div>
              <div>本店库存：{{ currentProduct.localStock || 0 }}件</div>
            </div>
            <div v-if="currentProduct.inventoryMode === 0" class="add-to-cart">
              <el-input-number v-model="addQuantity" :min="1" :max="currentProduct.localStock" controls-position="right" size="small"></el-input-number>
              <el-button type="primary" size="small" @click="addToCart">加入订单</el-button>
            </div>
            <div v-else-if="currentProduct.inventoryMode === 1" class="one-code-one-product">
              <div class="barcode-select-area">
                <span>选择条码：</span>
                <el-select v-model="selectedBarcode" placeholder="请选择具体条码" filterable clearable size="small" style="width: 200px;">
                  <el-option
                    v-for="item in currentProduct.availableBarcodes"
                    :key="item.barcode"
                    :label="`${item.barcode} (金重:${item.goldWeight}g 工费:${item.laborCost}元)`"
                    :value="item.barcode"
                  ></el-option>
                </el-select>
                <el-button type="primary" size="small" @click="addOneCodeToCart" :disabled="!selectedBarcode">加入订单</el-button>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 购物车列表 -->
        <el-card class="cart-card" shadow="never" style="margin-top: 20px;">
          <div slot="header">
            <span>当前订单</span>
            <el-button type="danger" size="small" style="float: right;" @click="clearCart">清空</el-button>
          </div>
          <el-table :data="cartItems" border stripe>
            <el-table-column prop="skuName" label="商品名称" min-width="150"></el-table-column>
            <el-table-column prop="barcode" label="条码" width="150"></el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" align="center"></el-table-column>
            <el-table-column prop="unitPrice" label="单价(元)" width="100" align="right"></el-table-column>
            <el-table-column prop="subtotal" label="小计(元)" width="100" align="right"></el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="removeFromCart(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="cart-actions">
            <el-button type="primary" plain icon="el-icon-search" @click="openCrossShopQuery">跨店查询</el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：订单信息 -->
      <el-col :span="10">
        <el-card class="info-card" shadow="never">
          <div slot="header">
            <span>会员信息</span>
            <el-button type="text" size="small" style="float: right;" @click="openMemberSearch">选择会员</el-button>
          </div>
          <div v-if="selectedMember" class="member-info">
            <div>姓名：{{ selectedMember.name }}</div>
            <div>手机号：{{ selectedMember.mobile }}</div>
            <div>会员等级：{{ selectedMember.levelName || '普通会员' }}</div>
            <div>积分：{{ selectedMember.points || 0 }}</div>
          </div>
          <div v-else class="empty-info">未选择会员（可享受积分/折扣）</div>
        </el-card>

        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>销售员</span>
            <el-button type="text" size="small" style="float: right;" @click="openSalespersonSelect">选择</el-button>
          </div>
          <div v-if="selectedSalesperson" class="salesperson-info">
            <div>姓名：{{ selectedSalesperson.name }}</div>
            <div>工号：{{ selectedSalesperson.employeeNo }}</div>
            <div>提成比例：{{ selectedSalesperson.commissionRate }}%</div>
          </div>
          <div v-else class="empty-info">未选择销售员</div>
        </el-card>

        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>折扣优惠</span>
            <el-button type="text" size="small" style="float: right;" @click="openDiscountDialog">申请折扣</el-button>
          </div>
          <div v-if="discountInfo" class="discount-info">
            <div>折扣类型：{{ discountInfo.type === 'percentage' ? '百分比折扣' : '固定金额' }}</div>
            <div>折扣值：{{ discountInfo.type === 'percentage' ? discountInfo.value + '%' : discountInfo.value + '元' }}</div>
            <div>折后金额：{{ discountedAmount }} 元</div>
            <div>审批状态：<el-tag size="mini" :type="discountApprovalStatus.type">{{ discountApprovalStatus.text }}</el-tag></div>
          </div>
          <div v-else class="empty-info">未使用折扣</div>
        </el-card>

        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>回收抵扣</span>
            <el-button type="text" size="small" style="float: right;" @click="openRecoverySelect">选择回收单</el-button>
          </div>
          <div v-if="selectedRecovery" class="recovery-info">
            <div>回收单号：{{ selectedRecovery.recoveryNo }}</div>
            <div>回收金额：{{ selectedRecovery.totalAmount }} 元</div>
            <div>已抵扣：{{ selectedRecovery.deductedAmount }} 元</div>
            <div>本次抵扣：<el-input-number v-model="recoveryDeductionAmount" :min="0" :max="maxRecoveryDeduction" size="small" controls-position="right"></el-input-number> 元</div>
            <div>剩余可抵扣：{{ selectedRecovery.remainingAmount }} 元</div>
          </div>
          <div v-else class="empty-info">未使用回收抵扣</div>
        </el-card>

        <el-card class="total-card" shadow="never" style="margin-top: 15px;">
          <div class="total-line">
            <span>商品总额：</span>
            <span class="total-value">{{ cartTotal }} 元</span>
          </div>
          <div class="total-line" v-if="discountInfo">
            <span>折扣优惠：</span>
            <span class="total-value">- {{ discountAmount }} 元</span>
          </div>
          <div class="total-line" v-if="selectedRecovery && recoveryDeductionAmount > 0">
            <span>回收抵扣：</span>
            <span class="total-value">- {{ recoveryDeductionAmount }} 元</span>
          </div>
          <div class="total-line grand-total">
            <span>应付金额：</span>
            <span class="total-value">{{ finalPayAmount }} 元</span>
          </div>
          <el-button type="success" size="large" style="width: 100%; margin-top: 15px;" @click="submitOrder" :disabled="cartItems.length === 0">结算</el-button>
        </el-card>
      </el-col>
    </el-row>

    <!-- 会员选择弹窗（略，可复用之前的） -->
    <el-dialog title="选择会员" :visible.sync="memberDialogVisible" width="600px">
      <!-- 内容略，参考之前代码 -->
      <span>会员选择组件（实际开发时完善）</span>
    </el-dialog>

    <!-- 销售员选择弹窗（略） -->
    <el-dialog title="选择销售员" :visible.sync="salespersonDialogVisible" width="500px">
      <span>销售员选择组件</span>
    </el-dialog>

    <!-- 折扣申请弹窗（略） -->
    <el-dialog title="申请折扣" :visible.sync="discountDialogVisible" width="500px">
      <span>折扣申请组件</span>
    </el-dialog>

    <!-- 回收单选择弹窗（略） -->
    <el-dialog title="选择回收单" :visible.sync="recoveryDialogVisible" width="700px">
      <span>回收单选择组件</span>
    </el-dialog>

    <!-- 跨店查询弹窗（略，可复用之前设计的） -->
    <el-dialog title="跨店库存查询" :visible.sync="crossShopDialogVisible" width="800px">
      <span>跨店查询组件</span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PosOrderCreation',
  data() {
    return {
      scanCode: '',
      currentProduct: null,
      addQuantity: 1,
      selectedBarcode: null,
      cartItems: [],
      // 会员
      selectedMember: null,
      memberDialogVisible: false,
      // 销售员
      selectedSalesperson: null,
      salespersonDialogVisible: false,
      // 折扣
      discountInfo: null,
      discountDialogVisible: false,
      // 回收
      selectedRecovery: null,
      recoveryDeductionAmount: 0,
      recoveryDialogVisible: false,
      // 跨店查询
      crossShopDialogVisible: false
    }
  },
  computed: {
    cartTotal() {
      return this.cartItems.reduce((sum, item) => sum + item.subtotal, 0).toFixed(2)
    },
    discountAmount() {
      if (!this.discountInfo) return 0
      const total = parseFloat(this.cartTotal)
      if (this.discountInfo.type === 'percentage') {
        return total * (this.discountInfo.value / 100)
      } else {
        return Math.min(this.discountInfo.value, total)
      }
    },
    discountedAmount() {
      return (parseFloat(this.cartTotal) - this.discountAmount).toFixed(2)
    },
    maxRecoveryDeduction() {
      if (!this.selectedRecovery) return 0
      return Math.min(this.selectedRecovery.remainingAmount, parseFloat(this.discountedAmount))
    },
    finalPayAmount() {
      let amount = parseFloat(this.discountedAmount)
      if (this.selectedRecovery && this.recoveryDeductionAmount > 0) {
        amount -= this.recoveryDeductionAmount
      }
      return amount.toFixed(2)
    },
    discountApprovalStatus() {
      if (!this.discountInfo) return { type: 'info', text: '无' }
      const statusMap = {
        1: { type: 'warning', text: '待审批' },
        2: { type: 'success', text: '已批准' },
        3: { type: 'danger', text: '已驳回' }
      }
      return statusMap[this.discountInfo.status] || { type: 'info', text: '未知' }
    }
  },
  mounted() {
    this.$refs.scanInput.focus()
  },
  methods: {
    // 处理扫码/输入
    async handleScan() {
      if (!this.scanCode) return
      // 调用后端接口：根据输入值（条码或SKU编码）查询商品信息
      // 后端返回数据结构：
      // {
      //   inventoryMode: 0 或 1,
      //   skuInfo: { skuId, skuCode, skuName, goldWeight, silverWeight, laborCost },
      //   localStock: 总库存（普通模式）,
      //   availableBarcodes: [ { barcode, goldWeight, silverWeight, laborCost } ]  // 仅一物一码模式
      // }
      try {
        const res = await this.fetchProductInfo(this.scanCode)
        if (!res) {
          this.$message.error('未找到商品')
          this.currentProduct = null
          this.scanCode = ''
          return
        }
        this.currentProduct = {
          skuId: res.skuInfo.skuId,
          skuCode: res.skuInfo.skuCode,
          skuName: res.skuInfo.skuName,
          goldWeight: res.skuInfo.goldWeight,
          silverWeight: res.skuInfo.silverWeight,
          laborCost: res.skuInfo.laborCost,
          inventoryMode: res.inventoryMode,
          localStock: res.localStock || 0,
          availableBarcodes: res.availableBarcodes || []
        }
        this.addQuantity = 1
        this.selectedBarcode = null
        // 如果是一物一码且有可用条码，默认选中第一个
        if (this.currentProduct.inventoryMode === 1 && this.currentProduct.availableBarcodes.length > 0) {
          this.selectedBarcode = this.currentProduct.availableBarcodes[0].barcode
        }
        this.scanCode = ''
      } catch (error) {
        this.$message.error('查询失败')
        this.currentProduct = null
        this.scanCode = ''
      }
    },
    // 模拟接口：根据输入值查询商品
    fetchProductInfo(input) {
      return new Promise(resolve => {
        setTimeout(() => {
          // 模拟数据：输入123456为一物一码足金戒指
          if (input === '123456') {
            resolve({
              inventoryMode: 1,
              skuInfo: {
                skuId: 1001,
                skuCode: 'GOLD-RING-001',
                skuName: '足金戒指',
                goldWeight: 3.26,
                silverWeight: 0,
                laborCost: 180
              },
              localStock: 1,  // 总库存件数
              availableBarcodes: [
                { barcode: '123456', goldWeight: 3.26, silverWeight: 0, laborCost: 180 }
              ]
            })
          } else if (input === '654321') {
            // 普通商品银手镯
            resolve({
              inventoryMode: 0,
              skuInfo: {
                skuId: 2001,
                skuCode: 'SILVER-BRACELET-001',
                skuName: '银手镯',
                goldWeight: 0,
                silverWeight: 25.5,
                laborCost: 80
              },
              localStock: 5
            })
          } else {
            // 其他输入按SKU编码查询，假设是普通商品
            resolve({
              inventoryMode: 0,
              skuInfo: {
                skuId: 3001,
                skuCode: input,
                skuName: '测试商品',
                goldWeight: 0,
                silverWeight: 0,
                laborCost: 0
              },
              localStock: 0
            })
          }
        }, 300)
      })
    },
    // 普通商品加入购物车
    addToCart() {
      if (this.addQuantity <= 0) return
      const item = {
        skuId: this.currentProduct.skuId,
        skuCode: this.currentProduct.skuCode,
        skuName: this.currentProduct.skuName,
        barcode: null,
        quantity: this.addQuantity,
        unitPrice: this.calculatePrice(this.currentProduct),
        subtotal: 0,
        inventoryMode: 0
      }
      item.subtotal = item.unitPrice * item.quantity
      this.cartItems.push(item)
      this.$message.success('已加入订单')
      this.currentProduct = null
      this.$refs.scanInput.focus()
    },
    // 一物一码商品加入购物车（已选择具体条码）
    addOneCodeToCart() {
      const selected = this.currentProduct.availableBarcodes.find(b => b.barcode === this.selectedBarcode)
      if (!selected) return
      const item = {
        skuId: this.currentProduct.skuId,
        skuCode: this.currentProduct.skuCode,
        skuName: this.currentProduct.skuName,
        barcode: selected.barcode,
        quantity: 1,
        unitPrice: this.calculateOneCodePrice(selected),
        subtotal: 0,
        inventoryMode: 1
      }
      item.subtotal = item.unitPrice * item.quantity
      this.cartItems.push(item)
      this.$message.success(`已添加条码 ${selected.barcode}`)
      this.currentProduct = null
      this.selectedBarcode = null
      this.$refs.scanInput.focus()
    },
    calculatePrice(product) {
      const goldPrice = 450
      const silverPrice = 5
      return (product.goldWeight * goldPrice + product.silverWeight * silverPrice + product.laborCost)
    },
    calculateOneCodePrice(barcodeInfo) {
      const goldPrice = 450
      const silverPrice = 5
      return (barcodeInfo.goldWeight * goldPrice + barcodeInfo.silverWeight * silverPrice + barcodeInfo.laborCost)
    },
    removeFromCart(index) {
      this.cartItems.splice(index, 1)
    },
    clearCart() {
      this.cartItems = []
    },
    // 跨店查询（略）
    openCrossShopQuery() {
      this.crossShopDialogVisible = true
    },
    // 其他弹窗方法（会员、销售员、折扣、回收）可参考之前代码实现
    openMemberSearch() { this.memberDialogVisible = true },
    openSalespersonSelect() { this.salespersonDialogVisible = true },
    openDiscountDialog() { this.discountDialogVisible = true },
    openRecoverySelect() { this.recoveryDialogVisible = true },
    submitOrder() {
      // 结算逻辑
      this.$confirm(`订单总金额：${this.finalPayAmount} 元，确认结算？`, '提示', { type: 'success' }).then(() => {
        this.$message.success('订单创建成功')
        // 清空购物车等
        this.cartItems = []
        this.selectedMember = null
        this.selectedSalesperson = null
        this.discountInfo = null
        this.selectedRecovery = null
        this.recoveryDeductionAmount = 0
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.pos-order-creation {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 100px);
}
.scan-card, .cart-card, .info-card, .total-card {
  border-radius: 8px;
}
.scan-input {
  margin-bottom: 10px;
}
.scan-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
.product-info {
  margin-top: 10px;
}
.product-detail {
  display: flex;
  gap: 20px;
  margin: 10px 0;
  font-size: 14px;
}
.add-to-cart, .one-code-one-product {
  margin-top: 10px;
}
.barcode-select-area {
  display: flex;
  align-items: center;
  gap: 10px;
}
.cart-actions {
  margin-top: 15px;
  text-align: left;
}
.member-info, .salesperson-info, .discount-info, .recovery-info {
  font-size: 14px;
  line-height: 1.8;
}
.empty-info {
  color: #909399;
  font-size: 14px;
}

.total-line {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}
.grand-total {
  font-size: 18px;
  font-weight: bold;
  color: #e6a23c;
}
.total-value {
  font-weight: bold;
}
</style>
