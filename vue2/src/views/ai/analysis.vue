<template>
  <div class="ai-analysis-container">
    <el-card shadow="hover" class="analysis-card">
      <template #header>
        <div class="card-header">
          <span>AI智能分析</span>
          <div class="header-buttons">
            <el-button type="info" @click="goToConfig">
              <i class="el-icon-setting"></i> AI配置
            </el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab" type="card">
        <!-- 智能分析标签页 -->
        <el-tab-pane label="智能分析" name="analysis">
          <div class="tab-content">
            <el-dropdown>
              <el-button type="primary" style="margin-bottom: 20px;">
              分析类型 <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="setAnalysisType('sales')">销售分析</el-dropdown-item>
                  <el-dropdown-item @click="setAnalysisType('inventory')">库存优化</el-dropdown-item>
                  <el-dropdown-item @click="setAnalysisType('customer')">客户洞察</el-dropdown-item>
                  <el-dropdown-item @click="setAnalysisType('operation')">运营效率</el-dropdown-item>
                  <el-dropdown-item @click="setAnalysisType('custom')">自定义分析</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

            <!-- 销售分析 -->
            <div v-if="analysisType === 'sales'" class="analysis-form">
              <el-form :model="form" label-width="120px">
                <el-form-item label="分析说明">
                  <el-alert
                    title="系统将自动从数据库获取最近7天的销售数据进行分析"
                    type="info"
                    show-icon
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="analyze" :loading="loading">开始分析</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 库存优化 -->
            <div v-if="analysisType === 'inventory'" class="analysis-form">
              <el-form :model="form" label-width="120px">
                <el-form-item label="库存数据">
                  <el-input
                    type="textarea"
                    v-model="form.inventoryData"
                    placeholder="请输入库存数据（JSON格式）"
                    :rows="6"
                  />
                </el-form-item>
                <el-form-item label="销售数据">
                  <el-input
                    type="textarea"
                    v-model="form.salesData"
                    placeholder="请输入销售数据（JSON格式）"
                    :rows="6"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="analyze" :loading="loading">开始分析</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 客户洞察 -->
            <div v-if="analysisType === 'customer'" class="analysis-form">
              <el-form :model="form" label-width="120px">
                <el-form-item label="客户数据">
                  <el-input
                    type="textarea"
                    v-model="form.customerData"
                    placeholder="请输入客户数据（JSON格式）"
                    :rows="6"
                  />
                </el-form-item>
                <el-form-item label="订单数据">
                  <el-input
                    type="textarea"
                    v-model="form.orderData"
                    placeholder="请输入订单数据（JSON格式）"
                    :rows="6"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="analyze" :loading="loading">开始分析</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 运营效率 -->
            <div v-if="analysisType === 'operation'" class="analysis-form">
              <el-form :model="form" label-width="120px">
                <el-form-item label="运营数据">
                  <el-input
                    type="textarea"
                    v-model="form.operationData"
                    placeholder="请输入运营数据（JSON格式）"
                    :rows="8"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="analyze" :loading="loading">开始分析</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 自定义分析 -->
            <div v-if="analysisType === 'custom'" class="analysis-form">
              <el-form :model="form" label-width="120px">
                <el-form-item label="分析提示词">
                  <el-input
                    type="textarea"
                    v-model="form.prompt"
                    placeholder="请输入分析提示词"
                    :rows="8"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="analyze" :loading="loading">开始分析</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 分析结果 -->
            <div v-if="result" class="analysis-result">
              <el-card shadow="hover" class="result-card">
                <template #header>
                  <div class="result-header">
                    <span>分析结果</span>
                  </div>
                </template>
                <div class="result-content" v-html="result"></div>
              </el-card>
            </div>
          </div>
        </el-tab-pane>

        <!-- 历史记录标签页 -->
        <el-tab-pane label="历史记录" name="history">
          <div class="tab-content">
            <el-table
              v-loading="historyLoading"
              :data="historyList"
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="analysisType" label="分析类型" width="120">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.analysisType === 'sales'" size="small" type="primary">销售分析</el-tag>
                  <el-tag v-else-if="scope.row.analysisType === 'inventory'" size="small" type="success">库存优化</el-tag>
                  <el-tag v-else-if="scope.row.analysisType === 'customer'" size="small" type="warning">客户洞察</el-tag>
                  <el-tag v-else-if="scope.row.analysisType === 'operation'" size="small" type="info">运营效率</el-tag>
                  <el-tag v-else size="small" type="danger">自定义分析</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="analysisContent" label="分析内容" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.status === 0" size="small">分析中</el-tag>
                  <el-tag v-else-if="scope.row.status === 1" size="small" type="success">已完成</el-tag>
                  <el-tag v-else size="small" type="danger">失败</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createdTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="viewHistoryDetail(scope.row.id)">查看报告</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container" v-if="total > 0">
              <el-pagination
                background
                layout="prev, pager, next, jumper"
                :total="total"
                :current-page="currentPage"
                :page-size="pageSize"
                @current-change="handleCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { analyzeSales, optimizeInventory, analyzeCustomerInsights, analyzeOperationEfficiency, customAnalysis, getHistoryList } from '@/api/ai/analysis'
import marked from 'marked'

export default {
  name: 'AiAnalysis',
  data() {
    return {
      analysisType: 'sales',
      activeTab: 'analysis', // analysis 或 history
      form: {
        salesData: '',
        inventoryData: '',
        customerData: '',
        orderData: '',
        operationData: '',
        prompt: ''
      },
      result: '',
      loading: false,
      historyList: [],
      historyLoading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  mounted() {
    this.getHistoryList()
  },
  methods: {
    setAnalysisType(type) {
      this.analysisType = type
      this.result = ''
    },
    async analyze() {
      this.loading = true
      try {
        let response
        switch (this.analysisType) {
          case 'sales':
            response = await analyzeSales()
            break
          case 'inventory':
            response = await optimizeInventory({
              inventoryData: this.form.inventoryData,
              salesData: this.form.salesData
            })
            break
          case 'customer':
            response = await analyzeCustomerInsights({
              customerData: this.form.customerData,
              orderData: this.form.orderData
            })
            break
          case 'operation':
            response = await analyzeOperationEfficiency({ operationData: this.form.operationData })
            break
          case 'custom':
            response = await customAnalysis({ prompt: this.form.prompt })
            break
        }
        if (response.code === 200) {
          this.result = this.formatResult(response.data)
          // 重新获取历史列表
          this.getHistoryList()
        } else {
          this.$message.error(response.msg || '分析失败')
        }
      } catch (error) {
        this.$message.error('分析失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    formatResult(result) {
      // 使用marked渲染markdown
      if (!result) {
        return ''
      }
      return marked(result)
    },
    goToConfig() {
      this.$router.push('/ai/config')
    },
    getHistoryList() {
      this.historyLoading = true
      getHistoryList({
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(response => {
        if (response.code === 200) {
          this.historyList = response.rows || []
          this.total = response.total || 0
        } else {
          this.$message.error(response.msg || '获取历史记录失败')
        }
      }).catch(error => {
        this.$message.error('获取历史记录失败：' + error.message)
      }).finally(() => {
        this.historyLoading = false
      })
    },
    viewHistoryDetail(id) {
      // 跳转到历史详情页面
      this.$router.push(`/ai/history/${id}`)
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.getHistoryList()
    }
  }
}
</script>

<style scoped>
.ai-analysis-container {
  padding: 20px;
}

.analysis-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.analysis-content {
  margin-top: 20px;
}

.analysis-form {
  margin-bottom: 20px;
}

.result-card {
  margin-top: 20px;
}

.result-content {
  white-space: pre-wrap;
  line-height: 1.6;
}

.tab-content {
  padding: 20px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* Markdown样式 */
.result-content h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 20px 0 10px 0;
  color: #303133;
}

.result-content h2 {
  font-size: 20px;
  font-weight: bold;
  margin: 15px 0 10px 0;
  color: #303133;
}

.result-content p {
  margin: 10px 0;
  line-height: 1.6;
  color: #606266;
}

.result-content ul {
  margin: 10px 0;
  padding-left: 20px;
}

.result-content li {
  margin: 5px 0;
  line-height: 1.6;
  color: #606266;
}

.result-content code {
  background-color: #f5f7fa;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 14px;
}
</style>
