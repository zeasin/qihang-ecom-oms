<template>
  <div class="ai-history-detail-container">
    <el-card shadow="hover" class="history-card">
      <template #header>
        <div class="card-header">
          <span>分析报告</span>
          <el-button type="primary" @click="goBack">
            <i class="el-icon-back"></i> 返回历史列表
          </el-button>
        </div>
      </template>

      <div v-loading="loading" class="history-content">
        <div v-if="history.status === 1" class="report-content">
          <el-card shadow="hover" class="prompt-card">
            <template #header>
              <div class="card-header">
                <span>提示词内容</span>
              </div>
            </template>
            <div class="prompt-content">{{ history.promptContent }}</div>
          </el-card>
          <el-card shadow="hover" class="result-card" style="margin-top: 20px;">
            <template #header>
              <div class="card-header">
                <span>分析结果</span>
              </div>
            </template>
            <div class="result-content" v-html="formattedResult"></div>
          </el-card>
        </div>
        <div v-else-if="history.status === 2" class="error-content">
          <el-alert
            title="分析失败"
            type="error"
            :description="history.errorMessage"
            show-icon
          />
        </div>
        <div v-else class="loading-content">
          <el-alert
            title="分析中"
            type="info"
            description="分析正在进行中，请稍后查看"
            show-icon
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { marked } from 'marked'
import { getHistoryDetail } from '@/api/ai/analysis'

export default {
  name: 'AiHistoryDetail',
  data() {
    return {
      history: {
        id: '',
        analysisType: '',
        analysisContent: '',
        analysisResult: '',
        status: 0,
        errorMessage: '',
        createdTime: ''
      },
      loading: false
    }
  },
  computed: {
    formattedResult() {
      // 使用marked渲染markdown
      if (!this.history.analysisResult) {
        return ''
      }
      return marked.parse(this.history.analysisResult)
    }
  },
  mounted() {
    this.getHistoryDetail()
  },
  methods: {
    getHistoryDetail() {
      this.loading = true
      const id = this.$route.params.id
      getHistoryDetail(id).then(response => {
        if (response.code === 200) {
          this.history = response.data
        } else {
          this.$message.error(response.msg || '获取历史详情失败')
        }
      }).catch(error => {
        this.$message.error('获取历史详情失败：' + error.message)
      }).finally(() => {
        this.loading = false
      })
    },
    goBack() {
      this.$router.push('/ai/analysis')
    }
  }
}
</script>

<style scoped>
.ai-history-detail-container {
  padding: 20px;
}

.history-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-content {
  margin-top: 20px;
}

.detail-form {
  margin-bottom: 20px;
}

.result-card {
  margin-top: 10px;
}

.prompt-content {
  white-space: pre-wrap;
  line-height: 1.6;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 14px;
  color: #303133;
}

.result-content {
  white-space: pre-wrap;
  line-height: 1.6;
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
