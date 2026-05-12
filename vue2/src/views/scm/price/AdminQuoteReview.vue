<template>
  <div class="review-container">
    <!-- 页面标题和统计 -->
    <div class="page-header">
      <div class="header-content">
        <h2>报价审核管理</h2>
        <div class="stats">
          <el-tag type="warning">待审核: {{ stats.pending }}</el-tag>
          <el-tag type="success">今日通过: {{ stats.approvedToday }}</el-tag>
          <el-tag type="danger">今日拒绝: {{ stats.rejectedToday }}</el-tag>
          <el-tag>总计: {{ stats.total }}</el-tag>
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
        <el-button @click="showAutoReviewRules">
          <i class="el-icon-setting"></i> 自动审核规则
        </el-button>
        <el-button @click="exportData" :loading="exporting">
          <i class="el-icon-download"></i> 导出数据
        </el-button>
      </div>
    </div>

    <!-- 筛选工具栏 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="供应商">
          <el-select
            v-model="filters.supplierId"
            placeholder="选择供应商"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="supplier in suppliers"
              :key="supplier.id"
              :label="supplier.name"
              :value="supplier.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="商品类目">
          <el-cascader
            v-model="filters.category"
            :options="categories"
            placeholder="选择类目"
            clearable
            style="width: 200px"
          ></el-cascader>
        </el-form-item>

        <el-form-item label="报价状态">
          <el-select v-model="filters.status" placeholder="选择状态" clearable>
            <el-option label="待审核" value="pending"></el-option>
            <el-option label="已通过" value="approved"></el-option>
            <el-option label="已拒绝" value="rejected"></el-option>
            <el-option label="已过期" value="expired"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="提交时间">
          <el-date-picker
            v-model="filters.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="关键词">
          <el-input
            v-model="filters.keyword"
            placeholder="商品名/SKU/供应商"
            prefix-icon="el-icon-search"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilters">重置</el-button>
          <el-button type="text" @click="showAdvanced = !showAdvanced">
            {{ showAdvanced ? '收起高级筛选' : '展开高级筛选' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 高级筛选 -->
      <el-collapse-transition>
        <div v-show="showAdvanced" class="advanced-filters">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="单价范围">
                <el-input-number
                  v-model="filters.minPrice"
                  placeholder="最低价"
                  :precision="2"
                  :controls="false"
                  style="width: 100px"
                ></el-input-number>
                <span style="margin: 0 10px">-</span>
                <el-input-number
                  v-model="filters.maxPrice"
                  placeholder="最高价"
                  :precision="2"
                  :controls="false"
                  style="width: 100px"
                ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="交货周期">
                <el-input-number
                  v-model="filters.maxDeliveryDays"
                  placeholder="最大天数"
                  :min="1"
                  style="width: 120px"
                >
                  <template slot="append">天</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="紧急程度">
                <el-select v-model="filters.priority" placeholder="选择紧急度" clearable>
                  <el-option label="高" value="high"></el-option>
                  <el-option label="中" value="medium"></el-option>
                  <el-option label="低" value="low"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-collapse-transition>
    </el-card>

    <!-- 批量操作栏 -->
    <div class="batch-actions" v-show="selectedQuotes.length > 0">
      <el-alert
        :title="`已选择 ${selectedQuotes.length} 条报价`"
        type="info"
        :closable="false"
        show-icon
      >
        <div slot="action" class="batch-buttons">
          <el-button-group>
            <el-button type="success" size="small" @click="batchApprove">
              <i class="el-icon-check"></i> 批量通过
            </el-button>
            <el-button type="danger" size="small" @click="batchReject">
              <i class="el-icon-close"></i> 批量拒绝
            </el-button>
            <el-button type="warning" size="small" @click="batchAssign">
              <i class="el-icon-user"></i> 批量分配
            </el-button>
          </el-button-group>
          <el-button size="small" @click="clearSelection">取消选择</el-button>
        </div>
      </el-alert>
    </div>

    <!-- 报价列表表格 -->
    <el-card class="table-card">
      <el-table
        ref="quoteTable"
        :data="quotes"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column prop="quoteNumber" label="报价单号" width="180" sortable>
          <template slot-scope="{ row }">
            <div class="quote-number">
              <el-tag size="mini" :type="getStatusTagType(row.status)">
                {{ formatStatus(row.status) }}
              </el-tag>
              <span class="number">{{ row.quoteNumber }}</span>
            </div>
            <div class="sub-info" v-if="row.priority === 'high'">
              <el-tag size="mini" type="danger">紧急</el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="productName" label="商品信息" min-width="200">
          <template slot-scope="{ row }">
            <div class="product-info">
              <div class="product-name">{{ row.productName }}</div>
              <div class="sku-info">
                <span class="sku">SKU: {{ row.sku }}</span>
                <span class="category">类目: {{ row.category }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="supplierName" label="供应商" width="150">
          <template slot-scope="{ row }">
            <div class="supplier-info">
              <div>{{ row.supplierName }}</div>
              <div class="supplier-rating">
                <el-rate
                  v-model="row.supplierRating"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
                ></el-rate>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="unitPrice" label="价格信息" width="150" sortable>
          <template slot-scope="{ row }">
            <div class="price-info">
              <div class="main-price">¥{{ row.unitPrice }}</div>
              <div class="tax-info">含税: ¥{{ row.taxIncludedPrice }}</div>
              <div class="moq" v-if="row.moq > 1">MOQ: {{ row.moq }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="deliveryDays" label="交货周期" width="120" sortable>
          <template slot-scope="{ row }">
            <div class="delivery-info">
              <span>{{ row.deliveryDays }} 天</span>
              <el-progress
                v-if="row.deliveryDays <= 7"
                :percentage="getDeliveryProgress(row.deliveryDays)"
                :show-text="false"
                :stroke-width="6"
                status="success"
                style="margin-top: 5px"
              ></el-progress>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="validity" label="有效期" width="180">
          <template slot-scope="{ row }">
            <div class="validity-info">
              <div>{{ formatDate(row.validityStart) }} 至</div>
              <div>{{ formatDate(row.validityEnd) }}</div>
              <div v-if="isExpiring(row.validityEnd)" class="expire-warning">
                <i class="el-icon-warning"></i> 即将过期
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="submittedAt" label="提交时间" width="160" sortable>
          <template slot-scope="{ row }">
            <div class="time-info">
              <div>{{ formatDateTime(row.submittedAt) }}</div>
              <div class="reviewer" v-if="row.reviewer">
                审核人: {{ row.reviewer }}
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="220">
          <template slot-scope="{ row }">
            <el-button-group>
              <!-- 查看详情 -->
              <el-button
                type="primary"
                icon="el-icon-view"
                size="mini"
                @click="viewQuoteDetail(row)"
                title="查看详情"
              ></el-button>

              <!-- 审核通过 -->
              <el-button
                v-if="row.status === 'pending'"
                type="success"
                icon="el-icon-check"
                size="mini"
                @click="approveQuote(row)"
                title="审核通过"
              ></el-button>

              <!-- 拒绝 -->
              <el-button
                v-if="row.status === 'pending'"
                type="danger"
                icon="el-icon-close"
                size="mini"
                @click="rejectQuote(row)"
                title="拒绝报价"
              ></el-button>

              <!-- 沟通 -->
              <el-button
                type="info"
                icon="el-icon-chat-line-round"
                size="mini"
                @click="contactSupplier(row)"
                title="联系供应商"
              ></el-button>

              <!-- 更多操作 -->
              <el-dropdown @command="handleMoreCommand(row, $event)">
                <el-button type="default" size="mini">
                  <i class="el-icon-more"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="history">历史记录</el-dropdown-item>
                  <el-dropdown-item command="compare">比价分析</el-dropdown-item>
                  <el-dropdown-item command="duplicate">复制为新报价</el-dropdown-item>
                  <el-dropdown-item
                    v-if="row.status !== 'pending'"
                    command="reopen"
                    divided
                  >
                    重新打开
                  </el-dropdown-item>
                  <el-dropdown-item command="export" divided>导出详情</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 报价详情对话框 -->
    <quote-detail-dialog
      :visible.sync="detailDialogVisible"
      :quote-id="selectedQuoteId"
      @approve="handleDialogApprove"
      @reject="handleDialogReject"
    />

    <!-- 审核操作对话框 -->
    <review-action-dialog
      :visible.sync="actionDialogVisible"
      :action-type="actionType"
      :quote="selectedQuote"
      @confirm="handleActionConfirm"
    />

    <!-- 自动审核规则对话框 -->
    <auto-review-rules-dialog
      :visible.sync="rulesDialogVisible"
    />
  </div>
</template>

<script>
import QuoteDetailDialog from './QuoteDetailDialog.vue'
import ReviewActionDialog from './ReviewActionDialog.vue'
import AutoReviewRulesDialog from './AutoReviewRulesDialog.vue'

export default {
  name: 'AdminQuoteReview',
  components: {
    QuoteDetailDialog,
    ReviewActionDialog,
    AutoReviewRulesDialog
  },
  data() {
    return {
      // 筛选条件
      filters: {
        supplierId: '',
        category: [],
        status: '',
        dateRange: [],
        keyword: '',
        minPrice: null,
        maxPrice: null,
        maxDeliveryDays: null,
        priority: ''
      },

      // 表格数据
      quotes: [],
      selectedQuotes: [],
      loading: false,

      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },

      // 统计信息
      stats: {
        pending: 0,
        approvedToday: 0,
        rejectedToday: 0,
        total: 0
      },

      // 下拉选项
      suppliers: [],
      categories: [],

      // 对话框控制
      showAdvanced: false,
      detailDialogVisible: false,
      actionDialogVisible: false,
      rulesDialogVisible: false,
      exporting: false,

      // 当前操作
      selectedQuoteId: null,
      selectedQuote: null,
      actionType: 'approve'
    }
  },

  mounted() {
    this.initPage()
  },

  methods: {
    // 初始化页面
    async initPage() {
      await Promise.all([
        this.loadSuppliers(),
        this.loadCategories(),
        this.loadQuotes(),
        this.loadStats()
      ])
    },

    // 加载供应商列表
    async loadSuppliers() {
      try {
        const response = await this.$http.get('/api/suppliers/options')
        this.suppliers = response.data
      } catch (error) {
        this.$message.error('加载供应商列表失败')
      }
    },

    // 加载类目列表
    async loadCategories() {
      try {
        const response = await this.$http.get('/api/categories/tree')
        this.categories = response.data
      } catch (error) {
        this.$message.error('加载类目列表失败')
      }
    },

    // 加载报价列表
    async loadQuotes() {
      this.loading = true
      try {
        const params = {
          ...this.buildQueryParams(),
          page: this.pagination.current,
          size: this.pagination.size
        }

        const response = await this.$http.get('/api/quotes/admin', { params })
        this.quotes = response.data.records
        this.pagination.total = response.data.total
      } catch (error) {
        this.$message.error('加载报价列表失败')
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStats() {
      try {
        const response = await this.$http.get('/api/quotes/stats')
        this.stats = response.data
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 构建查询参数
    buildQueryParams() {
      const params = { ...this.filters }

      // 处理日期范围
      if (params.dateRange && params.dateRange.length === 2) {
        params.startDate = params.dateRange[0]
        params.endDate = params.dateRange[1]
        delete params.dateRange
      }

      // 处理类目（取最后一个值）
      if (params.category && params.category.length > 0) {
        params.categoryId = params.category[params.category.length - 1]
        delete params.category
      }

      // 移除空值
      Object.keys(params).forEach(key => {
        if (params[key] === '' || params[key] === null || params[key] === undefined) {
          delete params[key]
        }
      })

      return params
    },

    // 搜索处理
    handleSearch() {
      this.pagination.current = 1
      this.loadQuotes()
    },

    // 重置筛选
    resetFilters() {
      this.filters = {
        supplierId: '',
        category: [],
        status: '',
        dateRange: [],
        keyword: '',
        minPrice: null,
        maxPrice: null,
        maxDeliveryDays: null,
        priority: ''
      }
      this.handleSearch()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedQuotes = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      console.log('排序:', prop, order)
      // 这里可以添加排序逻辑
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadQuotes()
    },

    // 页码变化
    handleCurrentChange(page) {
      this.pagination.current = page
      this.loadQuotes()
    },

    // 查看报价详情
    viewQuoteDetail(row) {
      this.selectedQuoteId = row.id
      this.detailDialogVisible = true
    },

    // 审核通过报价
    approveQuote(row) {
      this.selectedQuote = row
      this.actionType = 'approve'
      this.actionDialogVisible = true
    },

    // 拒绝报价
    rejectQuote(row) {
      this.selectedQuote = row
      this.actionType = 'reject'
      this.actionDialogVisible = true
    },

    // 联系供应商
    contactSupplier(row) {
      this.$prompt('请输入沟通内容', '联系供应商', {
        confirmButtonText: '发送',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请描述需要沟通的事项...',
        inputValidator: (value) => {
          if (!value) {
            return '沟通内容不能为空'
          }
        }
      }).then(async ({ value }) => {
        try {
          await this.$http.post('/api/quotes/contact', {
            quoteId: row.id,
            message: value
          })
          this.$message.success('消息已发送')
        } catch (error) {
          this.$message.error('发送失败')
        }
      })
    },

    // 更多操作
    handleMoreCommand(row, command) {
      switch (command) {
        case 'history':
          this.viewQuoteHistory(row)
          break
        case 'compare':
          this.comparePrices(row)
          break
        case 'duplicate':
          this.duplicateQuote(row)
          break
        case 'reopen':
          this.reopenQuote(row)
          break
        case 'export':
          this.exportQuoteDetail(row)
          break
      }
    },

    // 批量通过
    async batchApprove() {
      if (this.selectedQuotes.length === 0) return

      try {
        const quoteIds = this.selectedQuotes.map(q => q.id)
        await this.$http.post('/api/quotes/batch-approve', { quoteIds })

        this.$message.success(`成功通过 ${quoteIds.length} 条报价`)
        this.clearSelection()
        this.refreshData()
      } catch (error) {
        this.$message.error('批量操作失败')
      }
    },

    // 批量拒绝
    async batchReject() {
      this.$prompt('请输入拒绝原因', '批量拒绝', {
        confirmButtonText: '确认拒绝',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请说明拒绝原因...',
        inputValidator: (value) => {
          if (!value) {
            return '拒绝原因不能为空'
          }
        }
      }).then(async ({ value }) => {
        try {
          const quoteIds = this.selectedQuotes.map(q => q.id)
          await this.$http.post('/api/quotes/batch-reject', {
            quoteIds,
            reason: value
          })

          this.$message.success(`成功拒绝 ${quoteIds.length} 条报价`)
          this.clearSelection()
          this.refreshData()
        } catch (error) {
          this.$message.error('批量操作失败')
        }
      })
    },

    // 批量分配
    batchAssign() {
      this.$message.info('批量分配功能开发中')
    },

    // 清空选择
    clearSelection() {
      this.$refs.quoteTable.clearSelection()
      this.selectedQuotes = []
    },

    // 刷新数据
    refreshData() {
      this.loadQuotes()
      this.loadStats()
    },

    // 导出数据
    async exportData() {
      this.exporting = true
      try {
        const params = this.buildQueryParams()
        const response = await this.$http.get('/api/quotes/export', {
          params,
          responseType: 'blob'
        })

        const url = window.URL.createObjectURL(new Blob([response.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `报价审核数据_${new Date().getTime()}.xlsx`)
        document.body.appendChild(link)
        link.click()

        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      } finally {
        this.exporting = false
      }
    },

    // 显示自动审核规则
    showAutoReviewRules() {
      this.rulesDialogVisible = true
    },

    // 对话框审核通过
    handleDialogApprove(quoteId) {
      console.log('审核通过:', quoteId)
      this.refreshData()
    },

    // 对话框拒绝
    handleDialogReject(quoteId) {
      console.log('拒绝:', quoteId)
      this.refreshData()
    },

    // 确认审核操作
    async handleActionConfirm(data) {
      try {
        const endpoint = this.actionType === 'approve'
          ? '/api/quotes/approve'
          : '/api/quotes/reject'

        await this.$http.post(endpoint, {
          quoteId: this.selectedQuote.id,
          ...data
        })

        this.$message.success(
          `报价已${this.actionType === 'approve' ? '通过' : '拒绝'}`
        )
        this.actionDialogVisible = false
        this.refreshData()
      } catch (error) {
        this.$message.error('操作失败')
      }
    },

    // 工具方法
    getStatusTagType(status) {
      const map = {
        pending: 'warning',
        approved: 'success',
        rejected: 'danger',
        expired: 'info',
        draft: ''
      }
      return map[status] || ''
    },

    formatStatus(status) {
      const map = {
        pending: '待审核',
        approved: '已通过',
        rejected: '已拒绝',
        expired: '已过期',
        draft: '草稿'
      }
      return map[status] || status
    },

    formatDate(date) {
      if (!date) return ''
      return this.$moment(date).format('YYYY-MM-DD')
    },

    formatDateTime(datetime) {
      if (!datetime) return ''
      return this.$moment(datetime).format('MM-DD HH:mm')
    },

    getDeliveryProgress(days) {
      return Math.max(0, Math.min(100, (14 - days) * 100 / 14))
    },

    isExpiring(endDate) {
      if (!endDate) return false
      const daysDiff = this.$moment(endDate).diff(this.$moment(), 'days')
      return daysDiff >= 0 && daysDiff <= 3
    },

    // 其他方法占位
    viewQuoteHistory() {
      this.$message.info('查看历史功能开发中')
    },

    comparePrices() {
      this.$message.info('比价分析功能开发中')
    },

    duplicateQuote() {
      this.$message.info('复制报价功能开发中')
    },

    reopenQuote() {
      this.$message.info('重新打开功能开发中')
    },

    exportQuoteDetail() {
      this.$message.info('导出详情功能开发中')
    }
  }
}
</script>

<style scoped>
.review-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.page-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.stats {
  display: flex;
  gap: 10px;
}

.quick-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  margin-bottom: 0;
}

.advanced-filters {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.batch-actions {
  margin-bottom: 20px;
}

.batch-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.table-card {
  margin-top: 20px;
}

.quote-number {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quote-number .number {
  font-family: 'Courier New', monospace;
  font-weight: bold;
}

.sub-info {
  margin-top: 4px;
}

.product-info {
  line-height: 1.4;
}

.product-name {
  font-weight: 500;
  margin-bottom: 4px;
}

.sku-info {
  font-size: 12px;
  color: #909399;
}

.sku-info .sku {
  margin-right: 10px;
}

.supplier-info {
  line-height: 1.4;
}

.supplier-rating {
  margin-top: 4px;
}

.price-info {
  line-height: 1.4;
}

.main-price {
  font-weight: bold;
  color: #f56c6c;
  font-size: 16px;
}

.tax-info {
  font-size: 12px;
  color: #909399;
}

.moq {
  font-size: 12px;
  color: #e6a23c;
  margin-top: 2px;
}

.delivery-info {
  text-align: center;
}

.validity-info {
  line-height: 1.4;
}

.expire-warning {
  color: #e6a23c;
  font-size: 12px;
  margin-top: 2px;
}

.time-info {
  line-height: 1.4;
}

.reviewer {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .filter-form .el-form-item {
    margin-bottom: 10px;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .stats {
    flex-wrap: wrap;
    margin-top: 10px;
  }

  .quick-actions {
    justify-content: flex-start;
    margin-top: 15px;
  }
}
</style>
