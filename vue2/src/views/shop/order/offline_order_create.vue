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
              v-model="barcode"
              placeholder="请扫描或输入商品条码（本店库存）"
              size="large"
              @keyup.enter.native="handleBarcodeEnter"
              clearable
            >
              <el-button slot="append" icon="el-icon-search" @click="handleBarcodeEnter">查询</el-button>
            </el-input>
            <div class="scan-tip">支持扫描商品条码或输入SKU编码</div>
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
            <div v-if="currentProduct.localStock > 0" class="add-to-cart">
              <el-input-number v-model="addQuantity" :min="1" :max="currentProduct.localStock" controls-position="right" size="small"></el-input-number>
              <el-button type="primary" size="small" @click="addToCart">加入订单</el-button>
            </div>
            <div v-else class="out-of-stock">
              <el-alert title="本店无现货" type="warning" :closable="false" />
              <el-button type="info" size="small" disabled>暂无可售库存</el-button>
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
            <el-table-column prop="goodsName" label="商品名称" min-width="150"></el-table-column>
            <el-table-column prop="skuName" label="规格" min-width="100"></el-table-column>
            <el-table-column prop="barcode" label="条码" width="100"></el-table-column>
            <el-table-column prop="goldWeight" label="金重(g)" width="100"></el-table-column>
            <el-table-column prop="silverWeight" label="银重(g)" width="100"></el-table-column>
            <el-table-column prop="laborCost" label="工费" width="100"></el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" align="center"></el-table-column>
            <el-table-column prop="unitPrice" label="单价(元)" width="100" align="right"></el-table-column>
            <el-table-column prop="subtotal" label="小计(元)" width="100" align="right"></el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="removeFromCart(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧：订单信息（会员、销售员、折扣、回收等） -->
      <el-col :span="10">
        <!-- 会员信息 -->
        <el-card class="info-card" shadow="never">
          <div slot="header">
            <span>会员信息</span>
            <el-button type="text" size="small" style="float: right;" @click="openMemberSearch">选择会员</el-button>
          </div>
          <div v-if="selectedMember.shopMemberId" class="member-info">
            <div>姓名：{{ selectedMember.receiverName }}</div>
            <div>手机号：{{ selectedMember.receiverPhone }}</div>
            <div>会员等级：{{ selectedMember.levelName || '普通会员' }}</div>
            <div>积分：{{ selectedMember.points || 0 }}</div>
          </div>
          <div v-else class="empty-info">选择会员（可享受积分/折扣）</div>
        </el-card>

        <!-- 销售员信息 -->
        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>销售员</span>
            <el-button type="text" size="small" style="float: right;" @click="openSalespersonSelect">选择</el-button>
          </div>
          <div v-if="selectedSalesperson.id" class="salesperson-info">
            <div>姓名：{{ selectedSalesperson.name }}</div>
            <div>工号：{{ selectedSalesperson.employeeNo }}</div>
            <div>提成比例：{{ selectedSalesperson.commissionRate }}%</div>
          </div>
          <div v-else class="empty-info">未选择销售员</div>
        </el-card>

        <!-- 折扣优惠信息 -->
        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>折扣优惠</span>
            <el-button type="text" size="small" style="float: right;" @click="openDiscountDialog">选择折扣</el-button>
          </div>
          <div v-if="discountInfo.id" class="discount-info">
            <div>折扣类型：{{ discountInfo.type === 1 ? '百分比折扣' : '固定金额' }}</div>
            <div>折扣值：{{ discountInfo.type === 1 ? discountInfo.value + '%' : discountInfo.value + '元' }}</div>
            <div>折扣金额：{{ discountInfo.amount }} 元</div>
          </div>
          <div v-else class="empty-info">未使用折扣</div>
        </el-card>

        <!-- 回收抵扣信息 -->
        <el-card class="info-card" shadow="never" style="margin-top: 15px;">
          <div slot="header">
            <span>回收抵扣</span>
            <el-button type="text" size="small" style="float: right;" @click="openRecoverySelect">选择回收单</el-button>
          </div>
          <div v-if="selectedRecovery.id" class="recovery-info">
            <div>回收单号：{{ selectedRecovery.recoveryNo }}</div>
            <div>回收金额：{{ selectedRecovery.totalAmount }} 元</div>
            <div>剩余可抵扣：{{ selectedRecovery.remainingAmount }} 元</div>
            <div>本次抵扣：<el-input-number v-model="recoveryDeductionAmount" :min="0" :max="maxRecoveryDeduction" size="small" controls-position="right" @input="handleRecoveryDeductionInput"></el-input-number> 元</div>

          </div>
          <div v-else class="empty-info">未使用回收抵扣</div>
        </el-card>

        <!-- 订单合计与结算 -->
        <el-card class="total-card" shadow="never" style="margin-top: 15px;">
          <div class="total-line">
            <span>商品总额：</span>
            <span class="total-value">{{ cartTotal }} 元</span>
          </div>
          <div class="total-line" >
            <span>折扣优惠：</span>
            <span class="total-value">- {{ discountAmount }} 元</span>
          </div>
          <div class="total-line" >
            <span>回收抵扣：</span>
            <span class="total-value">- {{ recoveryDeductionAmount }} 元</span>
          </div>
          <div class="total-line grand-total">
            <span>应付金额：</span>
            <span class="total-value">{{ finalPayAmount }} 元</span>
          </div>
          <div class="total-line">
            <span>支付方式：</span>
            <el-select v-model="paymentMethod" size="small" style="width: 120px;">
              <el-option
                v-for="item in paymentOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
          <el-button type="success" size="large" style="width: 100%; margin-top: 15px;" @click="submitOrder" :disabled="cartItems.length === 0">结算</el-button>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品选择弹窗（多条结果时使用） -->
    <el-dialog
      title="请选择商品"
      :visible.sync="selectProductDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-table :data="selectableProducts" border stripe>
        <el-table-column prop="skuCode" label="SKU编码" width="140"></el-table-column>
        <el-table-column prop="skuName" label="商品名称" min-width="180"></el-table-column>
        <el-table-column prop="barcode" label="条码/批次号" width="160"></el-table-column>
        <el-table-column prop="goldWeight" label="金重(g)" width="80" align="right"></el-table-column>
        <el-table-column prop="silverWeight" label="银重(g)" width="80" align="right"></el-table-column>
        <el-table-column prop="laborCost" label="工费(元)" width="80" align="right"></el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="selectAndAddToCart(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer">
        <el-button @click="selectProductDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>



    <!-- 选择会员 -->
    <PopupShopMemberList @data-from-select="handleDataFromPopupMember" ref="popupMember"></PopupShopMemberList>
    <!-- 选择优惠 -->
    <PopupDiscountList @data-from-select="handleDataFromPopupDiscount" ref="popupDiscount"></PopupDiscountList>
    <!-- 选择销售员 -->
    <PopupSalesPeopleList @data-from-select="handleDataFromPopupSalesperson" ref="popupSalesPeople"></PopupSalesPeopleList>
    <!-- 回收单选择弹窗 -->
    <PopupRecycleList @data-from-select="handleDataFromPopupRecycle" ref="popupRecycle"></PopupRecycleList>
  </div>
</template>

<script>
import { queryShopGoodsSkuAndStock } from "@/api/shop/goodsStock";
import { offlineOrderCreate } from "@/api/shop/order";
import PopupDiscountList from "@/views/marketing/discount/PopupDiscountList.vue";
import PopupShopMemberList from "@/views/member/PopupShopMemberList.vue";
import PopupSalesPeopleList from "@/views/order/salesPeople/PopupSalesPeopleList.vue";
import PopupRecycleList from "@/views/afterSale/PopupRecycleList.vue";
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";

export default {
  name: 'PosOrderCreation',
  components: {PopupShopMemberList, PopupDiscountList,PopupSalesPeopleList,PopupRecycleList},
  data() {
    return {
      // 扫码相关
      barcode: '',
      currentProduct: null,
      addQuantity: 1,
      // 购物车
      cartItems: [],
      // 会员相关
      selectedMember: {
        shopMemberId: null,
        receiverName: null,
      },
      memberDialogVisible: false,
      memberSearchForm: { mobile: '', name: '' },
      memberList: [],
      memberLoading: false,
      memberPage: 1,
      memberPageSize: 10,
      memberTotal: 0,
      // 销售员相关
      selectedSalesperson: {
        id:null,
        name: null,
        employeeNo: null,
        commissionRate: null,
      },
      salespersonDialogVisible: false,
      salespersonKeyword: '',
      salespersonList: [],
      salespersonLoading: false,
      // 折扣相关
      discountInfo: {
        id: null,
        type: null,
        value: null,
        amount: 0.0,
      },
      discountDialogVisible: false,
      discountForm: { type: 'percentage', value: 0, reason: '' },
      discountRules: {
        value: [{ required: true, message: '请输入折扣值', trigger: 'blur' }],
        reason: [{ required: true, message: '请输入申请理由', trigger: 'blur' }]
      },
      discountSubmitting: false,
      // 回收抵扣相关
      selectedRecovery: {
        id: null,
      },
      recoveryDeductionAmount: 0,
      recoveryDialogVisible: false,
      recoveryKeyword: '',
      recoveryList: [],
      recoveryLoading: false,
      recoveryPage: 1,
      recoveryPageSize: 10,
      recoveryTotal: 0,
      // 商品选择弹窗（多条结果时）
      selectProductDialogVisible: false,
      selectableProducts: [],
      paymentMethod: 'cash', // 默认现金，可选值：cash, wechat, alipay, bankcard 等
      paymentOptions: [
        { label: '现金', value: 'cash' },
        { label: '微信支付', value: 'wechat' },
        { label: '支付宝', value: 'alipay' },
        { label: '银行卡', value: 'bankcard' }
      ]
    }
  },
  computed: {
    cartTotal() {
      return this.cartItems.reduce((sum, item) => sum + item.subtotal, 0).toFixed(2)
    },
    discountAmount() {
      if (!this.discountInfo) return 0
      if (!this.discountInfo.id) return 0
      const total = parseFloat(this.cartTotal)
      if (this.discountInfo.type === 1) {
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
  },
  methods: {
    // ========== 商品扫码与购物车 ==========
    handleBarcodeEnter() {
      if (!this.barcode) {
        this.$message.warning('请输入条码或SKU编码')
        return
      }
      queryShopGoodsSkuAndStock({ code: this.barcode }).then((res) => {
        if (res.code === 200) {
          const rows = res.rows || []
          // 情况1：只有一条SKU且只有一个批次 -> 直接加入购物车
          if (rows.length === 1 && rows[0].batchList && rows[0].batchList.length === 1) {
            console.log("=====查到了商品1条，直接添加")
            const sku = rows[0]
            const batch = sku.batchList[0]
            this.addItemToCart({
              skuId: sku.skuId,
              skuCode: sku.skuCode,
              goodsName: sku.goodsName,
              skuName: sku.skuName,
              barcode: batch.barCode,
              goldWeight: batch.goldWeight,
              goldPrice: batch.goldPrice,
              silverWeight: batch.silverWeight,
              silverPrice: batch.silverPrice,
              laborCost: batch.laborCost,
              laborPrice: batch.laborPrice,
              price: sku.inventoryMode===1?batch.price:sku.price,
              quantity: 1,
              inventoryMode: sku.inventoryMode
            })
            this.barcode = ''
            return
          }
          // 情况2：多条结果 -> 弹出选择列表
          const productList = []
          for (const sku of rows) {
            if (sku.batchList && sku.batchList.length > 0) {
              for (const batch of sku.batchList) {
                productList.push({
                  skuId: sku.skuId,
                  skuCode: sku.skuCode,
                  goodsName: sku.goodsName,
                  skuName: sku.skuName,
                  barcode: batch.barCode,
                  goldWeight: batch.goldWeight,
                  goldPrice: batch.goldPrice,
                  silverWeight: batch.silverWeight,
                  silverPrice: batch.silverPrice,
                  laborCost: batch.laborCost,
                  laborPrice: batch.laborPrice,
                  inventoryMode: sku.inventoryMode,
                  price: sku.price,
                  quantity: 1
                })
              }
            } else {
              productList.push({
                skuId: sku.skuId,
                skuCode: sku.skuCode,
                goodsName: sku.goodsName,
                skuName: sku.skuName,
                barcode: null,
                goldWeight: sku.goldWeight || 0,
                silverWeight: sku.silverWeight || 0,
                laborCost: sku.laborCost || 0,
                inventoryMode: sku.inventoryMode,
                price: sku.price,
                quantity: 1
              })
            }
          }
          if (productList.length === 0) {
            this.$message.warning('未找到可售商品')
            return
          }
          this.selectableProducts = productList
          this.selectProductDialogVisible = true
          this.barcode = ''
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      }).catch(err => {
        console.error(err)
        this.$message.error('查询失败，请重试')
      })
    },
    addItemToCart(item) {
      console.log('添加到购物车',item)
      // 一物一码：校验条码是否已存在
      if (item.inventoryMode === 1) {
        const exists = this.cartItems.find(cartItem =>
          cartItem.inventoryMode === 1 && cartItem.barcode === item.barcode
        );
        if (exists) {
          this.$message.warning(`条码 ${item.barcode} 已添加，不可重复`);
          return;
        }
      }
      // 普通商品：校验同一SKU是否已存在，若存在则合并数量
      else if (item.inventoryMode === 0) {
        const exists = this.cartItems.find(cartItem =>
          cartItem.inventoryMode === 0 && cartItem.skuCode === item.skuCode
        );
        if (exists) {
          // 合并数量
          const newQuantity = exists.quantity + item.quantity;
          exists.quantity = newQuantity;
          exists.subtotal = exists.unitPrice * newQuantity;
          this.$message.success(`已增加 ${item.skuName} 数量至 ${newQuantity}`);
          return;
        }
      }
      console.log('计算价格',item);
      // 获取单价：优先使用后端返回的 price，否则降级使用前端计算（兼容旧数据）
      let unitPrice = item.price;
      if (item.inventoryMode === 1) {
        // 一物一码：价格
        unitPrice = item.price
      }
      if (!unitPrice && unitPrice !== 0) {
        unitPrice = this.calculatePriceByItem(item);
      }

      const cartItem = {
        skuId: item.skuId,
        skuCode: item.skuCode,
        goodsName: item.goodsName,
        skuName: item.skuName,
        barcode: item.barcode,
        quantity: item.quantity,
        goldWeight: item.goldWeight,
        goldPrice: item.goldPrice,
        silverWeight: item.silverWeight,
        silverPrice: item.silverPrice,
        laborCost: item.laborCost,
        laborPrice: item.laborPrice,
        unitPrice: unitPrice,
        subtotal: unitPrice * item.quantity,
        inventoryMode: item.inventoryMode
      };
      this.cartItems.push(cartItem);
      this.$message.success(`已添加 ${item.skuName} ${item.barcode ? `条码:${item.barcode}` : `x${item.quantity}`}`);
    },
    selectAndAddToCart(row) {
      this.addItemToCart(row)
      this.selectProductDialogVisible = false
    },
    addToCart() {
      if (!this.currentProduct) return
      this.addItemToCart({
        skuId: this.currentProduct.skuId,
        skuCode: this.currentProduct.skuCode,
        skuName: this.currentProduct.skuName,
        barcode: this.currentProduct.barcode,
        goldWeight: this.currentProduct.goldWeight,
        silverWeight: this.currentProduct.silverWeight,
        laborCost: this.currentProduct.laborCost,
        quantity: this.addQuantity,
        inventoryMode: this.currentProduct.inventoryMode === 1 ? 1 : 0
      })
      this.currentProduct = null
      this.barcode = ''
    },

    removeFromCart(index) {
      this.cartItems.splice(index, 1)
    },
    clearCart() {
      this.cartItems = []
    },

    // ========== 会员管理 ==========
    openMemberSearch() {
      this.$refs.popupMember.openDialog();
    },
    //接收会员信息
    handleDataFromPopupMember(data){
      console.log('========选择的会员：',data);
      if(data){
        console.log('=====接收到会员')
        this.selectedMember.shopMemberId=data.id;
        this.selectedMember.receiverName=data.name;
        this.selectedMember.receiverPhone=data.phone;
        this.selectedMember.address=data.address;
        this.selectedMember.province=data.province;
        this.selectedMember.city=data.city;
        this.selectedMember.town=data.county;
      }
    },

    // ========== 销售员管理 ==========
    openSalespersonSelect() {
      this.$refs.popupSalesPeople.openDialog();
    },
    //接收会员信息
    handleDataFromPopupSalesperson(data){
      console.log('========选择的销售员：',data);
      if(data){
        this.selectedSalesperson.id=data.id;
        this.selectedSalesperson.name=data.name;
        this.selectedSalesperson.employeeNo=data.employeeNo;
        this.selectedSalesperson.commissionRate=data.commissionRate;
      }
    },

    // ========== 折扣申请 ==========
    openDiscountDialog() {
      if (this.cartItems.length === 0) {
        this.$message.warning('请先添加商品')
        return
      }
      this.$refs.popupDiscount.openDialog();
    },
    // 接收优惠折扣
    handleDataFromPopupDiscount(data){
      console.log('========选择的折扣：',data);
      if(data) {
        // 判断订单金额是否满足
        if(data.minOrderAmount>0){
          if( this.cartTotal < data.minOrderAmount){
            this.$modal.msgError('订单金额不满足折扣要求')
            return
          }
        }
        this.discountInfo.amount = 0.0
        this.discountInfo.type = data.discountType
        this.discountInfo.id = data.id
        this.discountInfo.value = data.discountValue
        // 计算折扣后的金额
        if(data.discountType==1){
          // 百分比
          this.discountInfo.amount = this.cartTotal * data.discountValue /100;
        }else{
          // 固定金额
          this.discountInfo.amount = data.discountValue;
        }
      }
    },
    // ========== 回收抵扣 ==========
    openRecoverySelect() {
      if (!this.selectedMember.shopMemberId) {
        this.$message.warning('请先选择会员')
        return
      }
      this.$refs.popupRecycle.openDialog(this.selectedMember.shopMemberId);
    },
    handleDataFromPopupRecycle(data){
      console.log('========选择的回收抵扣：',data);
      if(data) {
        this.selectedRecovery = data
        this.recoveryDeductionAmount = 0
        this.recoveryDialogVisible = false
        this.$message.success(`已选择回收单：${data.recoveryNo}`)
      }
    },

    // ========== 提交订单 ==========
    submitOrder() {
      if (this.cartItems.length === 0) return
      // 将价格字段从字符串转换回数字类型
      this.recoveryDeductionAmount = stringToNumber(this.recoveryDeductionAmount);
      const orderData = {
        memberId: this.selectedMember ? this.selectedMember.shopMemberId : null,
        salespersonId: this.selectedSalesperson ? this.selectedSalesperson.id : null,
        paymentMethod: this.paymentMethod,
        recoveryId: this.selectedRecovery ? this.selectedRecovery.id : null,
        recoveryDeductionAmount: parseFloat(this.recoveryDeductionAmount) || 0,
        discountInfo: this.discountInfo.id ? {
          id: this.discountInfo.id,
          type: this.discountInfo.type,
          value: this.discountInfo.value,
          amount: parseFloat(this.discountAmount)
        } : null,
        items: this.cartItems.map(item => ({
          skuId: item.skuId,
          skuCode: item.skuCode,
          goodsName: item.goodsName,
          skuName: item.skuName,
          barcode: item.barcode,
          quantity: item.quantity,
          unitPrice: parseFloat(item.unitPrice),
          subtotal: parseFloat(item.subtotal),
          goldWeight: item.goldWeight || 0,
          silverWeight: item.silverWeight || 0,
          laborCost: item.laborCost || 0
        })),
        totalAmount: parseFloat(this.cartTotal),
        discountAmount: parseFloat(this.discountAmount),
        finalAmount: parseFloat(this.finalPayAmount)
      }
      console.log('提交订单数据：', orderData)
      this.$confirm(`订单总金额：${this.finalPayAmount} 元，确认结算？`, '提示', { type: 'success' }).then(() => {
        offlineOrderCreate(orderData).then(res => {
          if (res.code === 200) {
            this.$message.success('订单创建成功')
            this.cartItems = []
            this.selectedMember = { shopMemberId: null, receiverName: null }
            this.selectedSalesperson = { id: null, name: null, employeeNo: null, commissionRate: null }
            this.discountInfo = { id: null, type: null, value: null, amount: 0.0 }
            this.selectedRecovery = { id: null }
            this.recoveryDeductionAmount = 0
            this.paymentMethod = 'cash'
            this.barcode = ''
            this.currentProduct = null
          } else {
            this.$message.error(res.msg || '订单创建失败')
          }
        }).catch(err => {
          console.error(err)
          this.$message.error('网络错误，请重试')
        })
      }).catch(() => {})
    },
    handleRecoveryDeductionInput() {
      this.recoveryDeductionAmount = limitDecimalLength(this.recoveryDeductionAmount);
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
  margin-bottom: 15px;
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
.add-to-cart, .out-of-stock {
  margin-top: 10px;
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
.member-search-form {
  margin-bottom: 15px;
}
</style>