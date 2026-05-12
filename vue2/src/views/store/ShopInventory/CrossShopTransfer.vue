<template>
  <div class="cross-shop-transfer-apply">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>跨店调拨申请</h2>
      <el-button icon="el-icon-back" @click="goBack">返回</el-button>
    </div>

    <el-row :gutter="20">
      <!-- 左侧：调拨申请表单 -->
      <el-col :span="12">
        <el-card class="apply-card" shadow="never">
          <div slot="header">
            <span>发起调拨申请</span>
          </div>
          <el-form :model="applyForm" :rules="applyRules" ref="applyForm" label-width="110px">
            <el-form-item label="目标门店" prop="targetShopId">
              <el-select v-model="applyForm.targetShopId" placeholder="请选择目标门店" filterable disabled style="width: 100%;">
                <el-option
                  v-for="shop in shopList"
                  :key="shop.id"
                  :label="shop.name"
                  :value="shop.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="调拨类型" prop="transferType" v-if="applyForm.sourceType === 'sku'">
              <el-radio-group v-model="applyForm.transferType">
                <el-radio label="batch">按批次（条码）调拨</el-radio>
                <el-radio label="sku">按SKU调拨（调任意库存）</el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 按SKU调拨模式 -->
            <template v-if="applyForm.transferType === 'sku'">
              <el-form-item label="SKU信息" prop="skuCode">
                <el-input v-model="applyForm.skuCode" disabled></el-input>
              </el-form-item>
              <el-form-item label="商品名称">
                <el-input v-model="applyForm.skuName" disabled></el-input>
              </el-form-item>
              <el-form-item label="调拨数量" prop="transferQuantity">
                <el-input-number v-model="applyForm.transferQuantity" :min="1" :max="applyForm.maxQuantity" controls-position="right"></el-input-number>
                <span class="form-tip">（最多可调拨 {{ applyForm.maxQuantity }} 件）</span>
              </el-form-item>
            </template>

            <!-- 按批次调拨模式（默认） -->
            <template v-else>
              <el-form-item label="条码" prop="barcode">
                <el-input v-model="applyForm.barcode" placeholder="请扫描或输入条码" clearable>
                  <el-button slot="append" icon="el-icon-search" @click="fetchBatchByBarcode">查询</el-button>
                </el-input>
              </el-form-item>
              <el-form-item label="批次详情" v-if="selectedBatch">
                <div class="batch-detail">
                  <div>SKU：{{ selectedBatch.skuCode }} - {{ selectedBatch.skuName }}</div>
                  <div>金重：{{ selectedBatch.actualGoldWeight }}g</div>
                  <div>银重：{{ selectedBatch.actualSilverWeight }}g</div>
                  <div>工费：{{ selectedBatch.laborCost }}元</div>
                  <div>可调拨数量：{{ selectedBatch.availableQuantity }}</div>
                </div>
              </el-form-item>
              <el-form-item label="调拨数量" prop="transferQuantity" v-if="selectedBatch">
                <el-input-number v-model="applyForm.transferQuantity" :min="1" :max="selectedBatch.availableQuantity" controls-position="right"></el-input-number>
                <span class="form-tip">（最多可调拨 {{ selectedBatch.availableQuantity }} 件）</span>
              </el-form-item>
            </template>

            <el-form-item label="期望完成时间" prop="expectedTime">
              <el-date-picker v-model="applyForm.expectedTime" type="datetime" placeholder="选择期望完成时间" :picker-options="pickerOptions" style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="申请理由" prop="reason">
              <el-input type="textarea" v-model="applyForm.reason" rows="3" placeholder="请输入调拨理由"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitApply" :loading="submitting">提交申请</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧：我的调拨申请记录 -->
      <el-col :span="12">
        <el-card class="record-card" shadow="never">
          <div slot="header">
            <span>我的调拨申请记录</span>
            <el-button type="text" style="float: right;" @click="loadMyTransfers">刷新</el-button>
          </div>
          <el-table :data="myTransferApplications" v-loading="loadingTransfers" border stripe size="small" max-height="500">
            <el-table-column prop="transferNo" label="调拨单号" width="150"></el-table-column>
            <el-table-column prop="targetShopName" label="目标门店" width="120"></el-table-column>
            <el-table-column prop="transferType" label="调拨类型" width="100">
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row.transferType === 'batch' ? 'success' : 'info'">
                  {{ scope.row.transferType === 'batch' ? '批次调拨' : 'SKU调拨' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="transferQuantity" label="数量" width="70" align="right"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getTransferStatusType(scope.row.status)" size="small">
                  {{ getTransferStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="expectedTime" label="期望完成" width="160"></el-table-column>
            <el-table-column label="操作" width="80" fixed="right">
              <template slot-scope="scope">
                <el-button v-if="scope.row.status === 1" type="text" size="small" @click="cancelTransfer(scope.row)">取消</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-if="transferTotal > 0"
            @current-change="handleTransferPageChange"
            :current-page="transferPage"
            :page-size="transferPageSize"
            layout="prev, pager, next"
            :total="transferTotal"
            style="margin-top: 15px; text-align: right;">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as transferApi from '@/api/shop/transfer'
import * as shopApi from '@/api/shop/shop'

export default {
  name: 'CrossShopTransferApply',
  data() {
    return {
      shopList: [],
      applyForm: {
        targetShopId: null,
        sourceType: 'batch', // 'batch' 或 'sku'
        transferType: 'batch',
        barcode: '',
        skuCode: '',
        skuName: '',
        transferQuantity: 1,
        expectedTime: null,
        reason: '',
        maxQuantity: 0
      },
      applyRules: {
        targetShopId: [{ required: true, message: '请选择目标门店', trigger: 'change' }],
        transferQuantity: [{ required: true, message: '请输入调拨数量', trigger: 'blur' }],
        expectedTime: [{ required: true, message: '请选择期望完成时间', trigger: 'change' }]
      },
      selectedBatch: null,
      submitting: false,
      myTransferApplications: [],
      loadingTransfers: false,
      transferPage: 1,
      transferPageSize: 10,
      transferTotal: 0,
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7 // 不能选择今天之前
        }
      }
    }
  },
  created() {
    this.initFromQuery()
    this.fetchShopList()
    this.loadMyTransfers()
  },
  methods: {
    initFromQuery() {
      const query = this.$route.query
      if (query.targetShopId) {
        this.applyForm.targetShopId = parseInt(query.targetShopId)
      }
      if (query.sourceType) {
        this.applyForm.sourceType = query.sourceType
        this.applyForm.transferType = query.sourceType === 'batch' ? 'batch' : 'sku'
      }
      if (query.batchId) {
        this.applyForm.barcode = query.barcode || ''
        this.selectedBatch = {
          batchId: query.batchId,
          barcode: query.barcode,
          skuCode: 'GOLD-RING-001',
          skuName: query.skuName || '足金戒指',
          actualGoldWeight: 3.25,
          actualSilverWeight: 0,
          laborCost: 180,
          availableQuantity: 1
        }
        this.applyForm.transferQuantity = 1
      } else if (query.skuCode) {
        this.applyForm.sourceType = 'sku'
        this.applyForm.transferType = 'sku'
        this.applyForm.skuCode = query.skuCode
        this.applyForm.skuName = query.skuName
        this.applyForm.maxQuantity = query.availableQuantity ? parseInt(query.availableQuantity) : 10
      }
    },
    fetchShopList() {
      shopApi.listShop().then(res => {
        if (res.code === 200) {
          this.shopList = res.data || []
        }
      })
    },
    fetchBatchByBarcode() {
      if (!this.applyForm.barcode) {
        this.$message.warning('请输入条码')
        return
      }
      setTimeout(() => {
        this.selectedBatch = {
          batchId: 10086,
          barcode: this.applyForm.barcode,
          skuCode: 'GOLD-RING-001',
          skuName: '足金戒指',
          actualGoldWeight: 3.26,
          actualSilverWeight: 0,
          laborCost: 180,
          availableQuantity: 1
        }
        this.applyForm.transferQuantity = 1
      }, 300)
    },
    submitApply() {
      this.$refs.applyForm.validate(valid => {
        if (!valid) return
        this.submitting = true
        const params = {
          toShopId: this.applyForm.targetShopId,
          transferType: this.applyForm.transferType,
          applyQuantity: this.applyForm.transferQuantity,
          expectedTime: this.applyForm.expectedTime,
          reason: this.applyForm.reason
        }
        if (this.applyForm.transferType === 'batch') {
          if (!this.selectedBatch && !this.applyForm.barcode) {
            this.$message.error('请先选择或查询批次')
            this.submitting = false
            return
          }
          params.batchId = this.selectedBatch.batchId
          params.barcode = this.selectedBatch.barcode
        } else {
          params.skuCode = this.applyForm.skuCode
          params.skuName = this.applyForm.skuName
        }
        transferApi.apply(params).then(res => {
          if (res.code === 200) {
            this.$message.success('调拨申请已提交，等待对方审批')
            this.resetForm()
            this.loadMyTransfers()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    loadMyTransfers() {
      this.loadingTransfers = true
      transferApi.list({ page: this.transferPage, limit: this.transferPageSize }).then(res => {
        if (res.code === 200 && res.rows) {
          this.myTransferApplications = res.rows
          this.transferTotal = res.total
        }
      }).finally(() => {
        this.loadingTransfers = false
      })
    },
    cancelTransfer(row) {
      this.$confirm('确认取消该调拨申请？', '提示', { type: 'warning' }).then(() => {
        transferApi.cancel(row.id).then(res => {
          if (res.code === 200) {
            this.$message.info('已取消')
            this.loadMyTransfers()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        })
      }).catch(() => {})
    },
    resetForm() {
      this.applyForm = {
        targetShopId: this.applyForm.targetShopId,
        sourceType: 'batch',
        transferType: 'batch',
        barcode: '',
        skuCode: '',
        skuName: '',
        transferQuantity: 1,
        expectedTime: null,
        reason: '',
        maxQuantity: 0
      }
      this.selectedBatch = null
      if (this.$refs.applyForm) this.$refs.applyForm.clearValidate()
    },
    goBack() {
      this.$router.go(-1)
    },
    handleTransferPageChange(page) {
      this.transferPage = page
      this.loadMyTransfers()
    },
    getTransferStatusType(status) {
      const map = { 1: 'warning', 2: 'success', 3: 'danger', 4: 'info' }
      return map[status] || 'info'
    },
    getTransferStatusText(status) {
      const map = { 1: '待审批', 2: '已批准', 3: '已驳回', 4: '已完成' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.cross-shop-transfer-apply {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 100px);
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px 20px;
  border-radius: 4px;
}
.page-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}
.apply-card, .record-card {
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
}
.batch-detail {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 13px;
  line-height: 1.8;
}
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}
</style>
