<template>
  <div class="quote-history-container">
    <!-- 组件标题和摘要信息 -->
    <div class="history-header">
      <h3>{{ productName }} - 历史报价记录</h3>
      <div class="summary-cards" v-if="historyData.length > 0">
        <el-card class="summary-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-trend-chart"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ formatCurrency(currentPrice) }}</div>
              <div class="card-label">当前报价</div>
            </div>
          </div>
        </el-card>

        <el-card class="summary-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-sort"></i>
            </div>
            <div class="card-info">
              <div class="card-value" :class="getTrendClass(priceTrend)">
                {{ priceTrend >= 0 ? '+' : '' }}{{ priceTrend.toFixed(2) }}%
              </div>
              <div class="card-label">价格趋势</div>
            </div>
          </div>
        </el-card>

        <el-card class="summary-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ historyData.length }}</div>
              <div class="card-label">历史记录</div>
            </div>
          </div>
        </el-card>

        <el-card class="summary-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ approvalRate }}%</div>
              <div class="card-label">通过率</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 时间范围筛选 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-row">
        <div class="filter-group">
          <span class="filter-label">时间范围：</span>
          <el-date-picker
            v-model="timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions"
            @change="handleTimeRangeChange"
          ></el-date-picker>
        </div>

        <div class="filter-group">
          <span class="filter-label">报价状态：</span>
          <el-select
            v-model="selectedStatus"
            placeholder="全部状态"
            clearable
            @change="handleStatusChange"
          >
            <el-option label="全部状态" value="all"></el-option>
            <el-option label="审核通过" value="approved"></el-option>
            <el-option label="审核拒绝" value="rejected"></el-option>
            <el-option label="待审核" value="pending"></el-option>
            <el-option label="已过期" value="expired"></el-option>
            <el-option label="已撤回" value="withdrawn"></el-option>
          </el-select>
        </div>

        <div class="filter-group">
          <el-button
            type="primary"
            icon="el-icon-download"
            @click="exportHistoryData"
            :loading="exporting"
          >
            导出数据
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 价格趋势图表 -->
    <el-card class="chart-card" shadow="never" v-if="historyData.length > 0">
      <div slot="header">
        <span>价格趋势分析</span>
        <el-button
          type="text"
          @click="toggleChart"
          style="float: right; padding: 3px 0"
        >
          {{ showChart ? '隐藏图表' : '显示图表' }}
        </el-button>
      </div>

      <el-collapse-transition>
        <div v-show="showChart" class="chart-container">
          <div class="chart-wrapper">
            <!-- 价格趋势折线图 -->
            <div ref="priceChart" style="width: 100%; height: 300px;"></div>
          </div>

          <div class="chart-statistics">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="stat-item">
                  <div class="stat-label">最高报价</div>
                  <div class="stat-value text-success">
                    {{ formatCurrency(priceStats.max) }}
                  </div>
                  <div class="stat-date">
                    {{ priceStats.maxDate }}
                  </div>
                </div>
              </el-col>

              <el-col :span="6">
                <div class="stat-item">
                  <div class="stat-label">最低报价</div>
                  <div class="stat-value text-danger">
                    {{ formatCurrency(priceStats.min) }}
                  </div>
                  <div class="stat-date">
                    {{ priceStats.minDate }}
                  </div>
                </div>
              </el-col>

              <el-col :span="6">
                <div class="stat-item">
                  <div class="stat-label">平均报价</div>
                  <div class="stat-value">
                    {{ formatCurrency(priceStats.average) }}
                  </div>
                  <div class="stat-desc">
                    标准差: {{ formatCurrency(priceStats.stdDev) }}
                  </div>
                </div>
              </el-col>

              <el-col :span="6">
                <div class="stat-item">
                  <div class="stat-label">波动率</div>
                  <div class="stat-value">
                    {{ priceStats.volatility.toFixed(2) }}%
                  </div>
                  <div
                    class="stat-desc"
                    :class="priceStats.volatility > 10 ? 'text-danger' : 'text-success'"
                  >
                    {{ priceStats.volatility > 10 ? '波动较大' : '波动稳定' }}
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-collapse-transition>
    </el-card>

    <!-- 历史报价表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="filteredHistoryData"
        v-loading="loading"
        element-loading-text="加载历史报价..."
        element-loading-spinner="el-icon-loading"
        style="width: 100%"
        @sort-change="handleSortChange"
        :default-sort="{ prop: 'submittedAt', order: 'descending' }"
      >
        <!-- 报价单号 -->
        <el-table-column prop="quoteNumber" label="报价单号" width="180" sortable>
          <template slot-scope="{ row }">
            <div class="quote-number-cell">
              <el-tag
                size="mini"
                :type="getStatusTagType(row.status)"
                class="status-tag"
              >
                {{ formatStatus(row.status) }}
              </el-tag>
              <span class="quote-number">{{ row.quoteNumber }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- 报价时间 -->
        <el-table-column prop="submittedAt" label="报价时间" width="160" sortable>
          <template slot-scope="{ row }">
            <div class="time-cell">
              <div>{{ formatDate(row.submittedAt) }}</div>
              <div class="time-detail">{{ formatTime(row.submittedAt) }}</div>
            </div>
          </template>
        </el-table-column>

        <!-- 价格信息 -->
        <el-table-column label="价格信息" width="200">
          <template slot-scope="{ row }">
            <div class="price-cell">
              <div class="price-main">
                <span class="price-label">单价：</span>
                <span class="price-value">{{ formatCurrency(row.unitPrice) }}</span>
              </div>
              <div class="price-sub">
                <span class="price-label">含税：</span>
                <span class="price-value">{{ formatCurrency(row.taxIncludedPrice) }}</span>
                <span class="tax-rate">({{ (row.taxRate * 100) }}%)</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 阶梯价格 -->
        <el-table-column label="阶梯价格" width="180">
          <template slot-scope="{ row }">
            <div class="tier-price-cell">
              <div v-if="row.tierPrices && row.tierPrices.length > 0">
                <el-tooltip placement="top">
                  <div slot="content">
                    <div v-for="(tier, index) in row.tierPrices" :key="index">
                      {{ tier.minQuantity }}-{{ tier.maxQuantity }}: {{ formatCurrency(tier.tierPrice) }}
                    </div>
                  </div>
                  <el-tag size="mini" type="info">
                    {{ row.tierPrices.length }} 个阶梯
                  </el-tag>
                </el-tooltip>
              </div>
              <div v-else>
                <el-tag size="mini">无阶梯</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 采购条件 -->
        <el-table-column label="采购条件" width="180">
          <template slot-scope="{ row }">
            <div class="terms-cell">
              <div class="term-item">
                <i class="el-icon-sold-out"></i>
                <span>MOQ: {{ row.moq }}</span>
              </div>
              <div class="term-item">
                <i class="el-icon-timer"></i>
                <span>交期: {{ row.deliveryDays }}天</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 有效期 -->
        <el-table-column prop="validityEnd" label="有效期" width="140" sortable>
          <template slot-scope="{ row }">
            <div class="validity-cell">
              <div>{{ formatDate(row.validityEnd) }}</div>
              <div v-if="isExpired(row.validityEnd)" class="expired-badge">
                <el-tag size="mini" type="info">已过期</el-tag>
              </div>
              <div v-else-if="isExpiring(row.validityEnd)" class="expiring-badge">
                <el-tag size="mini" type="warning">即将过期</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- 审核信息 -->
        <el-table-column label="审核信息" width="200">
          <template slot-scope="{ row }">
            <div v-if="row.status !== 'pending'" class="review-info">
              <div class="reviewer">
                <i class="el-icon-user"></i>
                <span>{{ row.reviewer || '系统' }}</span>
              </div>
              <div class="review-time">
                <i class="el-icon-time"></i>
                <span>{{ formatDate(row.reviewedAt) }}</span>
              </div>
              <div
                v-if="row.status === 'rejected'"
                class="reject-reason"
                :title="row.rejectReason"
              >
                <i class="el-icon-warning"></i>
                <span class="reason-text">{{ row.rejectReason }}</span>
              </div>
            </div>
            <div v-else class="pending-info">
              <el-tag type="warning">等待审核</el-tag>
            </div>
          </template>
        </el-table-column>

        <!-- 附件 -->
        <el-table-column label="附件" width="100">
          <template slot-scope="{ row }">
            <div class="attachments-cell">
              <el-tooltip
                v-if="row.attachments && row.attachments.length > 0"
                placement="top"
              >
                <div slot="content">
                  <div v-for="(file, index) in row.attachments" :key="index">
                    {{ file.name }}
                  </div>
                </div>
                <el-button
                  type="text"
                  icon="el-icon-document"
                  @click="viewAttachments(row)"
                >
                  {{ row.attachments.length }}个
                </el-button>
              </el-tooltip>
              <span v-else class="no-attachments">无附件</span>
            </div>
          </template>
        </el-table-column>

        <!-- 操作 -->
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{ row }">
            <div class="action-buttons">
              <el-button
                type="primary"
                icon="el-icon-view"
                size="mini"
                circle
                @click="viewQuoteDetail(row)"
                title="查看详情"
              ></el-button>

              <el-button
                v-if="canDuplicate(row)"
                type="success"
                icon="el-icon-copy-document"
                size="mini"
                circle
                @click="duplicateQuote(row)"
                title="复制报价"
              ></el-button>

              <el-dropdown
                v-if="row.status === 'pending'"
                @command="handlePendingAction(row, $event)"
                trigger="click"
              >
                <el-button
                  type="warning"
                  icon="el-icon-more"
                  size="mini"
                  circle
                ></el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="withdraw">撤回报价</el-dropdown-item>
                  <el-dropdown-item command="modify">修改报价</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container" v-if="pagination.total > 0">
        <el-pagination
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && filteredHistoryData.length === 0">
        <el-empty description="暂无历史报价记录">
          <el-button
            type="primary"
            @click="refreshData"
            icon="el-icon-refresh"
          >
            刷新数据
          </el-button>
        </el-empty>
      </div>
    </el-card>

    <!-- 报价详情对话框 -->
    <el-dialog
      title="报价详情"
      :visible.sync="detailDialogVisible"
      width="70%"
      top="5vh"
    >
      <quote-detail-view
        v-if="detailDialogVisible"
        :quote-id="selectedQuoteId"
        @close="detailDialogVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import QuoteDetailView from './QuoteDetailView.vue'
import echarts from 'echarts'


export default {
  name: 'QuoteHistory',
  props: {
    productId: {
      type: [String, Number],
      required: true
    }
  },
  components: {
    QuoteDetailView
  },
  data() {
    // 计算最近一年的日期范围
    const endDate = new Date()
    const startDate = new Date()
    startDate.setFullYear(startDate.getFullYear() - 1)

    return {
      // 数据
      historyData: [],
      filteredHistoryData: [],
      productName: '',
      currentPrice: 0,
      priceTrend: 0,
      approvalRate: 0,

      // 筛选条件
      timeRange: [
        this.formatDateForPicker(startDate),
        this.formatDateForPicker(endDate)
      ],
      selectedStatus: 'all',

      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },

      // 状态
      loading: false,
      exporting: false,
      showChart: true,
      detailDialogVisible: false,
      selectedQuoteId: null,

      // 图表实例
      priceChart: null,

      // 价格统计
      priceStats: {
        max: 0,
        min: 0,
        average: 0,
        stdDev: 0,
        volatility: 0,
        maxDate: '',
        minDate: ''
      },

      // 日期选择器选项
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一年',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setFullYear(start.getFullYear() - 1)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      }
    }
  },

  watch: {
    productId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadHistoryData()
        }
      }
    }
  },

  mounted() {
    // 监听窗口变化，重绘图表
    window.addEventListener('resize', this.handleResize)
  },

  beforeDestroy() {
    // 销毁图表实例
    if (this.priceChart) {
      this.priceChart.dispose()
    }
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize)
  },

  methods: {
    // 加载历史数据
    async loadHistoryData() {
      this.loading = true
      try {
        const params = {
          productId: this.productId,
          startDate: this.timeRange[0],
          endDate: this.timeRange[1],
          page: this.pagination.current,
          size: this.pagination.size
        }

        const response = await this.$http.get('/api/quotes/history', { params })
        const data = response.data

        this.historyData = data.records || []
        this.filteredHistoryData = [...this.historyData]
        this.pagination.total = data.total || 0

        // 更新产品信息
        if (data.productInfo) {
          this.productName = data.productInfo.name
          this.currentPrice = data.productInfo.currentPrice || 0
        }

        // 计算统计数据
        this.calculateStatistics()

        // 如果有数据，初始化图表
        if (this.historyData.length > 0 && this.showChart) {
          this.$nextTick(() => {
            this.initPriceChart()
          })
        }

      } catch (error) {
        this.$message.error('加载历史报价失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    // 计算统计数据
    calculateStatistics() {
      if (this.historyData.length === 0) return

      // 价格数组
      const prices = this.historyData.map(item => item.unitPrice).filter(price => price)

      // 基础统计
      this.priceStats.max = Math.max(...prices)
      this.priceStats.min = Math.min(...prices)
      this.priceStats.average = prices.reduce((a, b) => a + b, 0) / prices.length

      // 标准差
      const squareDiffs = prices.map(price => Math.pow(price - this.priceStats.average, 2))
      this.priceStats.stdDev = Math.sqrt(squareDiffs.reduce((a, b) => a + b, 0) / prices.length)

      // 波动率（标准差/平均值）
      this.priceStats.volatility = (this.priceStats.stdDev / this.priceStats.average) * 100

      // 最高价和最低价日期
      const maxQuote = this.historyData.find(item => item.unitPrice === this.priceStats.max)
      const minQuote = this.historyData.find(item => item.unitPrice === this.priceStats.min)

      this.priceStats.maxDate = maxQuote ? this.formatDate(maxQuote.submittedAt) : ''
      this.priceStats.minDate = minQuote ? this.formatDate(minQuote.submittedAt) : ''

      // 计算价格趋势（最新价相对于平均价的变化）
      const latestPrice = this.historyData[0]?.unitPrice || 0
      if (latestPrice > 0 && this.priceStats.average > 0) {
        this.priceTrend = ((latestPrice - this.priceStats.average) / this.priceStats.average) * 100
      }

      // 计算通过率
      const approvedCount = this.historyData.filter(item => item.status === 'approved').length
      const reviewedCount = this.historyData.filter(item =>
        item.status === 'approved' || item.status === 'rejected'
      ).length

      this.approvalRate = reviewedCount > 0
        ? Math.round((approvedCount / reviewedCount) * 100)
        : 0
    },

    // 初始化价格趋势图表
    initPriceChart() {
      if (!this.$refs.priceChart) return

      // 销毁旧的图表实例
      if (this.priceChart) {
        this.priceChart.dispose()
      }

      // 创建新图表
      this.priceChart = echarts.init(this.$refs.priceChart)

      // 准备图表数据
      const chartData = this.historyData
        .filter(item => item.unitPrice)
        .sort((a, b) => new Date(a.submittedAt) - new Date(b.submittedAt))
        .map(item => ({
          date: this.formatDate(item.submittedAt, 'MM-DD'),
          price: item.unitPrice,
          status: item.status
        }))

      const dates = chartData.map(item => item.date)
      const prices = chartData.map(item => item.price)

      // 图表配置
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: (params) => {
            const data = params[0]
            const originalData = chartData[data.dataIndex]
            return `
              <div style="margin: 0">${data.name}</div>
              <div>价格: <b>${this.formatCurrency(data.value)}</b></div>
              <div>状态: ${this.formatStatus(originalData.status)}</div>
            `
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '报价价格',
            type: 'line',
            data: prices,
            smooth: true,
            itemStyle: {
              color: '#1890ff'
            },
            lineStyle: {
              width: 3
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(24, 144, 255, 0.6)' },
                { offset: 1, color: 'rgba(24, 144, 255, 0.1)' }
              ])
            },
            markLine: {
              data: [
                {
                  type: 'average',
                  name: '平均值'
                }
              ],
              label: {
                formatter: '平均: {c}'
              }
            }
          }
        ]
      }

      // 设置图表选项
      this.priceChart.setOption(option)
    },

    // 时间范围变化
    handleTimeRangeChange() {
      this.pagination.current = 1
      this.loadHistoryData()
    },

    // 状态筛选变化
    handleStatusChange(status) {
      if (status === 'all') {
        this.filteredHistoryData = [...this.historyData]
      } else {
        this.filteredHistoryData = this.historyData.filter(item => item.status === status)
      }
    },

    // 表格排序
    handleSortChange({ column, prop, order }) {
      if (!prop || !order) return

      this.filteredHistoryData.sort((a, b) => {
        const aVal = a[prop]
        const bVal = b[prop]

        if (order === 'ascending') {
          return aVal > bVal ? 1 : -1
        } else {
          return aVal < bVal ? 1 : -1
        }
      })
    },

    // 分页大小变化
    handlePageSizeChange(size) {
      this.pagination.size = size
      this.loadHistoryData()
    },

    // 页码变化
    handlePageChange(page) {
      this.pagination.current = page
      this.loadHistoryData()
    },

    // 导出历史数据
    async exportHistoryData() {
      this.exporting = true
      try {
        const exportData = this.filteredHistoryData.map(item => ({
          报价单号: item.quoteNumber,
          报价时间: this.formatDateTime(item.submittedAt),
          商品名称: item.productName || this.productName,
          单价: item.unitPrice,
          含税单价: item.taxIncludedPrice,
          税率: `${(item.taxRate * 100)}%`,
          最小起订量: item.moq,
          交货周期: `${item.deliveryDays}天`,
          有效期至: this.formatDate(item.validityEnd),
          报价状态: this.formatStatus(item.status),
          审核人: item.reviewer || '',
          审核时间: item.reviewedAt ? this.formatDateTime(item.reviewedAt) : '',
          拒绝原因: item.rejectReason || '',
          备注: item.remarks || ''
        }))

        const fileName = `${this.productName}_历史报价_${new Date().getTime()}`
        exportJsonToExcel(exportData, fileName)

        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      } finally {
        this.exporting = false
      }
    },

    // 查看报价详情
    viewQuoteDetail(row) {
      this.selectedQuoteId = row.id
      this.detailDialogVisible = true
    },

    // 查看附件
    viewAttachments(row) {
      if (row.attachments && row.attachments.length > 0) {
        // 打开附件预览对话框
        this.$emit('view-attachments', row.attachments)
      }
    },

    // 复制报价
    duplicateQuote(row) {
      this.$emit('duplicate-quote', row)
    },

    // 处理待处理报价的操作
    handlePendingAction(row, command) {
      switch (command) {
        case 'withdraw':
          this.withdrawQuote(row)
          break
        case 'modify':
          this.modifyQuote(row)
          break
      }
    },

    // 撤回报价
    async withdrawQuote(row) {
      try {
        await this.$confirm('确定要撤回此报价吗？', '提示', {
          type: 'warning'
        })

        await this.$http.post(`/api/quotes/${row.id}/withdraw`)
        this.$message.success('报价已撤回')
        this.loadHistoryData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('撤回失败：' + error.message)
        }
      }
    },

    // 修改报价
    modifyQuote(row) {
      this.$emit('modify-quote', row)
    },

    // 切换图表显示
    toggleChart() {
      this.showChart = !this.showChart
      if (this.showChart && this.historyData.length > 0) {
        this.$nextTick(() => {
          this.initPriceChart()
        })
      }
    },

    // 刷新数据
    refreshData() {
      this.loadHistoryData()
    },

    // 窗口大小变化处理
    handleResize() {
      if (this.priceChart) {
        this.priceChart.resize()
      }
    },

    // 工具方法
    formatDate(dateString) {
      if (!dateString) return ''
      return this.$moment(dateString).format('YYYY-MM-DD')
    },

    formatDateForPicker(date) {
      return this.$moment(date).format('YYYY-MM-DD')
    },

    formatTime(dateString) {
      if (!dateString) return ''
      return this.$moment(dateString).format('HH:mm:ss')
    },

    formatDateTime(dateString) {
      if (!dateString) return ''
      return this.$moment(dateString).format('YYYY-MM-DD HH:mm:ss')
    },

    formatCurrency(value) {
      if (value === undefined || value === null) return '¥0.00'
      return '¥' + parseFloat(value).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    },

    formatStatus(status) {
      const statusMap = {
        pending: '待审核',
        approved: '审核通过',
        rejected: '审核拒绝',
        expired: '已过期',
        withdrawn: '已撤回',
        draft: '草稿'
      }
      return statusMap[status] || status
    },

    getStatusTagType(status) {
      const typeMap = {
        pending: 'warning',
        approved: 'success',
        rejected: 'danger',
        expired: 'info',
        withdrawn: 'info',
        draft: ''
      }
      return typeMap[status] || ''
    },

    getTrendClass(trend) {
      if (trend > 5) return 'text-danger'
      if (trend > 0) return 'text-warning'
      if (trend < -5) return 'text-success'
      if (trend < 0) return 'text-info'
      return ''
    },

    isExpired(validityEnd) {
      if (!validityEnd) return false
      return new Date(validityEnd) < new Date()
    },

    isExpiring(validityEnd) {
      if (!validityEnd || this.isExpired(validityEnd)) return false
      const daysDiff = this.$moment(validityEnd).diff(this.$moment(), 'days')
      return daysDiff >= 0 && daysDiff <= 3
    },

    canDuplicate(row) {
      return ['approved', 'rejected', 'expired', 'withdrawn'].includes(row.status)
    }
  }
}
</script>

<style scoped>
.quote-history-container {
  padding: 0;
}

.history-header {
  margin-bottom: 20px;
}

.history-header h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-weight: 500;
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

.summary-card {
  height: 100%;
}

.card-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.card-icon {
  font-size: 32px;
  color: #409EFF;
  margin-right: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 8px;
}

.card-info {
  flex: 1;
}

.card-value {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 4px;
}

.card-label {
  font-size: 14px;
  color: #909399;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 20px;
}

.filter-group {
  display: flex;
  align-items: center;
}

.filter-label {
  margin-right: 10px;
  color: #606266;
  font-size: 14px;
  white-space: nowrap;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-wrapper {
  width: 100%;
  height: 300px;
}

.chart-statistics {
  margin-top: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.stat-item {
  text-align: center;
  padding: 10px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
}

.stat-date {
  font-size: 12px;
  color: #909399;
}

.stat-desc {
  font-size: 12px;
  color: #909399;
}

.table-card {
  margin-bottom: 20px;
}

.quote-number-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-tag {
  flex-shrink: 0;
}

.quote-number {
  font-family: 'Courier New', monospace;
  font-weight: 500;
  color: #303133;
}

.time-cell {
  line-height: 1.4;
}

.time-detail {
  font-size: 12px;
  color: #909399;
}

.price-cell {
  line-height: 1.5;
}

.price-main {
  display: flex;
  align-items: center;
  margin-bottom: 2px;
}

.price-sub {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.price-label {
  color: #909399;
  margin-right: 4px;
}

.price-value {
  font-weight: 500;
  color: #f56c6c;
}

.tax-rate {
  margin-left: 4px;
}

.tier-price-cell {
  display: flex;
  justify-content: center;
}

.terms-cell {
  line-height: 1.5;
}

.term-item {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
  font-size: 13px;
}

.term-item i {
  margin-right: 6px;
  color: #909399;
}

.validity-cell {
  line-height: 1.4;
}

.expired-badge,
.expiring-badge {
  margin-top: 4px;
}

.review-info {
  line-height: 1.5;
}

.reviewer,
.review-time,
.reject-reason {
  display: flex;
  align-items: center;
  font-size: 13px;
  margin-bottom: 2px;
}

.reviewer i,
.review-time i,
.reject-reason i {
  margin-right: 6px;
  color: #909399;
}

.reject-reason {
  color: #f56c6c;
}

.reason-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 150px;
}

.pending-info {
  display: flex;
  justify-content: center;
}

.attachments-cell {
  display: flex;
  justify-content: center;
}

.no-attachments {
  color: #909399;
  font-size: 13px;
}

.action-buttons {
  display: flex;
  gap: 5px;
  justify-content: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.empty-state {
  padding: 40px 0;
}

/* 颜色类 */
.text-success {
  color: #67c23a !important;
}

.text-danger {
  color: #f56c6c !important;
}

.text-warning {
  color: #e6a23c !important;
}

.text-info {
  color: #909399 !important;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .summary-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .summary-cards {
    grid-template-columns: 1fr;
  }

  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-group {
    margin-bottom: 10px;
  }

  .filter-label {
    width: 80px;
    text-align: right;
  }
}
</style>
