<template>
  <div class="quote-submit-container">
    <!-- 页面标题和状态 -->
    <div class="page-header">
      <h2>供应商报价提交</h2>
      <el-alert v-if="hasPendingQuotes" title="您有正在审核中的报价，请耐心等待" type="info" show-icon></el-alert>
    </div>

    <!-- 报价表单 -->
    <el-form ref="quoteForm" :model="form" :rules="rules" label-width="120px" class="quote-form">
      <!-- 商品选择 -->
      <el-form-item label="报价商品" prop="productId" required>
        <el-select
          v-model="form.productId"
          placeholder="请选择商品"
          filterable
          clearable
          style="width: 380px"
          @change="handleProductChange"
        >
          <el-option
            v-for="product in productList"
            :key="product.id"
            :label="`${product.productName} (SKU: ${product.standard})`"
            :value="product.id"
          >
            <div class="product-option">
              <span>{{ product.productName }}</span>
              <span class="sku-tag">{{ product.standard }}</span>
              <span class="category-tag">{{ product.skuCode }}</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 商品详情展示 -->
      <el-card v-if="selectedProduct" class="product-info-card">
        <div slot="header">
          <span>商品详情</span>
        </div>
        <div class="product-details">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="detail-item">
                <label>规格型号：</label>
                <span>{{ selectedProduct.specification }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <label>单位：</label>
                <span>{{ selectedProduct.unit }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="detail-item">
                <label>上次报价：</label>
                <span v-if="selectedProduct.lastQuote">
                  ¥{{ selectedProduct.lastQuote.price }}
                  ({{ selectedProduct.lastQuote.date }})
                </span>
                <span v-else>暂无记录</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 价格信息 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="单价" prop="unitPrice" required>
            <el-input v-model="form.unitPrice" placeholder="请输入单价">
              <template slot="prepend">¥</template>
            </el-input>
            <div class="form-tip">不含税价格</div>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="含税单价" prop="taxIncludedPrice">
            <el-input v-model="form.taxIncludedPrice" placeholder="自动计算" readonly>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="税率" prop="taxRate">
            <el-select v-model="form.taxRate" placeholder="选择税率" style="width: 100%">
              <el-option label="13% - 增值税普通" value="0.13"></el-option>
              <el-option label="9% - 增值税特殊" value="0.09"></el-option>
              <el-option label="6% - 服务类" value="0.06"></el-option>
              <el-option label="0% - 免税" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 阶梯价格 -->
      <el-form-item label="阶梯价格">
        <el-table :data="form.tierPrices" border style="width: 100%">
          <el-table-column prop="minQuantity" label="最小数量" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.minQuantity"
                :min="1"
                size="small"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column prop="maxQuantity" label="最大数量" width="120">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.maxQuantity"
                :min="scope.row.minQuantity"
                size="small"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column prop="tierPrice" label="阶梯单价" width="150">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.tierPrice"
                placeholder="¥"
                size="small"
              >
                <template slot="prepend">¥</template>
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                size="mini"
                @click="removeTierPrice(scope.$index)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" icon="el-icon-plus" size="small" @click="addTierPrice">
          添加阶梯价格
        </el-button>
      </el-form-item>

      <!-- 其他信息 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最小起订量" prop="moq">
            <el-input-number
              v-model="form.moq"
              :min="1"
              placeholder="最小采购数量"
              style="width: 100%"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交货周期" prop="deliveryDays">
            <el-input-number
              v-model="form.deliveryDays"
              :min="1"
              :max="365"
              placeholder="天数"
              style="width: 100%"
            >
              <template slot="append">天</template>
            </el-input-number>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报价有效期" prop="validityPeriod">
            <el-date-picker
              v-model="form.validityPeriod"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="包装方式" prop="packaging">
            <el-input v-model="form.packaging" placeholder="如：纸箱装，每箱20个"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 文件上传 -->
      <el-form-item label="报价单文件" prop="attachments">
        <el-upload
          class="upload-demo"
          action="/api/upload/quote"
        :on-success="handleUploadSuccess"
        :on-remove="handleUploadRemove"
        :file-list="form.attachments"
        :limit="5"
        :on-exceed="handleExceed"
        multiple
        >
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">
          支持上传报价单、产品规格书等文件，单文件不超过10MB
        </div>
        </el-upload>
      </el-form-item>

      <!-- 备注 -->
      <el-form-item label="备注说明" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="可填写报价的特殊说明、优惠条件等"
          maxlength="500"
          show-word-limit
        ></el-input>
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item class="form-actions">
        <el-button type="primary" :loading="submitting" @click="submitForm('正式提交')">
          {{ submitting ? '提交中...' : '正式提交报价' }}
        </el-button>
        <el-button @click="saveAsDraft">保存为草稿</el-button>
        <el-button @click="resetForm">重置表单</el-button>
        <el-button type="text" @click="showHistory = true">查看历史报价</el-button>
      </el-form-item>
    </el-form>

    <!-- 历史报价对话框 -->
    <el-dialog title="历史报价记录" :visible.sync="showHistory" width="80%">
      <quote-history :product-id="form.productId" />
    </el-dialog>
  </div>
</template>

<script>
import QuoteHistory from './QuoteHistory.vue'
import {listGoods} from "@/api/goods/supplier_goods";

export default {
  name: 'SupplierQuoteSubmit',
  components: {
    QuoteHistory
  },
  data() {
    // 单价验证
    const validatePrice = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入单价'))
      } else if (isNaN(value) || value <= 0) {
        callback(new Error('请输入有效的正数'))
      } else {
        callback()
      }
    }

    return {
      // 表单数据
      form: {
        productId: '',
        unitPrice: '',
        taxRate: '0.13',
        taxIncludedPrice: '',
        moq: 1,
        deliveryDays: 7,
        validityPeriod: [],
        packaging: '',
        tierPrices: [
          { minQuantity: 1, maxQuantity: 100, tierPrice: '' },
          { minQuantity: 101, maxQuantity: 500, tierPrice: '' },
          { minQuantity: 501, maxQuantity: 999999, tierPrice: '' }
        ],
        attachments: [],
        remarks: ''
      },

      // 验证规则
      rules: {
        productId: [
          { required: true, message: '请选择报价商品', trigger: 'change' }
        ],
        unitPrice: [
          { required: true, validator: validatePrice, trigger: 'blur' }
        ],
        moq: [
          { required: true, message: '请输入最小起订量', trigger: 'blur' }
        ],
        deliveryDays: [
          { required: true, message: '请输入交货周期', trigger: 'blur' }
        ]
      },

      // 页面状态
      productList: [],
      selectedProduct: null,
      submitting: false,
      showHistory: false,
      hasPendingQuotes: false
    }
  },

  watch: {
    // 监听单价和税率变化，计算含税单价
    'form.unitPrice': {
      handler(newVal) {
        this.calculateTaxIncludedPrice()
      },
      immediate: true
    },
    'form.taxRate': {
      handler() {
        this.calculateTaxIncludedPrice()
      }
    }
  },

  mounted() {
    this.loadProducts()
    this.checkPendingQuotes()
  },

  methods: {
    // 加载商品列表
    async loadProducts() {
      listGoods(this.queryParams).then(response => {
        this.productList = response.rows;
      });
      // try {
      //   const response = await this.$http.get('/api/supplier/products')
      //   this.productList = response.data
      // } catch (error) {
      //   this.$message.error('加载商品列表失败')
      // }
    },

    // 商品选择变化
    handleProductChange(productId) {
      this.selectedProduct = this.productList.find(p => p.id === productId)
      if (this.selectedProduct?.lastQuote) {
        this.form.unitPrice = this.selectedProduct.lastQuote.price
      }
    },

    // 计算含税单价
    calculateTaxIncludedPrice() {
      if (this.form.unitPrice && this.form.taxRate) {
        const price = parseFloat(this.form.unitPrice)
        const taxRate = parseFloat(this.form.taxRate)
        this.form.taxIncludedPrice = (price * (1 + taxRate)).toFixed(2)
      } else {
        this.form.taxIncludedPrice = ''
      }
    },

    // 阶梯价格操作
    addTierPrice() {
      this.form.tierPrices.push({
        minQuantity: 1,
        maxQuantity: 100,
        tierPrice: ''
      })
    },

    removeTierPrice(index) {
      if (this.form.tierPrices.length > 1) {
        this.form.tierPrices.splice(index, 1)
      } else {
        this.$message.warning('至少保留一个价格阶梯')
      }
    },

    // 文件上传处理
    handleUploadSuccess(response, file) {
      this.form.attachments.push({
        name: file.name,
        url: response.data.url,
        uid: file.uid
      })
    },

    handleUploadRemove(file, fileList) {
      this.form.attachments = fileList
    },

    handleExceed() {
      this.$message.warning('最多只能上传5个文件')
    },

    // 检查待审核报价
    async checkPendingQuotes() {
      try {
        const response = await this.$http.get('/api/quotes/pending-count')
        this.hasPendingQuotes = response.data.count > 0
      } catch (error) {
        console.error('检查待审核报价失败:', error)
      }
    },

    // 表单提交
    submitForm(action) {
      this.$refs.quoteForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const quoteData = {
              ...this.form,
              status: 'pending',
              submittedAt: new Date().toISOString(),
              submitType: action
            }

            const response = await this.$http.post('/api/quotes/submit', quoteData)

            this.$message.success('报价提交成功！审核结果将通过系统通知您')
            this.resetForm()

            // 记录操作日志
            this.logAction('submit_quote', {
              productId: this.form.productId,
              price: this.form.unitPrice
            })

          } catch (error) {
            this.$message.error('提交失败：' + (error.response?.data?.message || error.message))
          } finally {
            this.submitting = false
          }
        } else {
          this.$message.error('请填写完整的必填信息')
          return false
        }
      })
    },

    // 保存草稿
    saveAsDraft() {
      const draftData = {
        ...this.form,
        status: 'draft',
        savedAt: new Date().toISOString()
      }

      localStorage.setItem('quote_draft', JSON.stringify(draftData))
      this.$message.success('已保存为草稿')

      this.logAction('save_draft', {
        productId: this.form.productId
      })
    },

    // 重置表单
    resetForm() {
      this.$refs.quoteForm.resetFields()
      this.selectedProduct = null
      this.form.tierPrices = [
        { minQuantity: 1, maxQuantity: 100, tierPrice: '' },
        { minQuantity: 101, maxQuantity: 500, tierPrice: '' },
        { minQuantity: 501, maxQuantity: 999999, tierPrice: '' }
      ]
      this.form.attachments = []
    },

    // 记录操作日志
    logAction(action, data) {
      // 这里可以调用日志API
      console.log('操作日志:', { action, data, timestamp: new Date() })
    }
  }
}
</script>

<style scoped>
.quote-submit-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.page-header {
  margin-bottom: 20px;
}

.quote-form {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.product-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sku-tag {
  font-size: 12px;
  color: #909399;
  background: #f4f4f5;
  padding: 2px 6px;
  border-radius: 3px;
}

.category-tag {
  font-size: 12px;
  color: #67c23a;
  background: #f0f9eb;
  padding: 2px 6px;
  border-radius: 3px;
}

.product-info-card {
  margin-bottom: 20px;
}

.product-details {
  display: flex;
  flex-wrap: wrap;
}

.detail-item {
  margin-bottom: 8px;
}

.detail-item label {
  color: #909399;
  font-weight: 500;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.upload-demo {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .quote-form {
    padding: 15px;
  }

  .el-col {
    margin-bottom: 15px;
  }
}
</style>
