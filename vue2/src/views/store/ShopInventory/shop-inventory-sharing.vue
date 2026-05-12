<template>
  <div class="authorization-management">
    <div class="page-header">
      <h2>合作授权管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openApplyDialog">发起授权申请</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="我的申请" name="myApplications">
        <el-table :data="myApplications" v-loading="loading.myApplications" border stripe>
          <el-table-column prop="applyNo" label="申请单号" width="180"></el-table-column>
          <el-table-column prop="targetShopName" label="目标门店" width="150"></el-table-column>
          <el-table-column label="授权类型" width="120">
            <template slot-scope="scope">
              <el-tag v-for="type in scope.row.authTypes" :key="type" size="small" style="margin-right: 5px;">
                {{ type === 1 ? '查看库存' : '调拨申请' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visibleScope" label="可见范围" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.visibleScope === 1">仅汇总</span>
              <span v-else-if="scope.row.visibleScope === 2">批次明细</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="申请理由" show-overflow-tooltip></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" width="160">
            <template slot-scope="scope">
              {{ parseTime(scope.row.createdTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 1" type="text" size="small" @click="cancelApplication(scope.row)">取消</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="myApplicationsTotal > 0"
          @current-change="handleMyAppsPageChange"
          :current-page="myAppsPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="myApplicationsTotal"
          style="margin-top: 15px; text-align: right;">
        </el-pagination>
      </el-tab-pane>

      <el-tab-pane label="待我审批" name="pendingApproval">
        <el-table :data="pendingApprovals" v-loading="loading.pendingApprovals" border stripe>
          <el-table-column prop="applyNo" label="申请单号" width="180"></el-table-column>
          <el-table-column prop="fromShopName" label="申请门店" width="150"></el-table-column>
          <el-table-column label="授权类型" width="120">
            <template slot-scope="scope">
              <el-tag v-for="type in scope.row.authTypes" :key="type" size="small" style="margin-right: 5px;">
                {{ type === 1 ? '查看库存' : '调拨申请' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visibleScope" label="可见范围" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.visibleScope === 1">仅汇总</span>
              <span v-else-if="scope.row.visibleScope === 2">批次明细</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="申请理由" show-overflow-tooltip></el-table-column>
          <el-table-column label="申请时间" width="160">
            <template slot-scope="scope">
              {{ parseTime(scope.row.createdTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template slot-scope="scope">
              <el-button type="success" size="small" @click="approve(scope.row)">通过</el-button>
              <el-button type="danger" size="small" @click="reject(scope.row)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="pendingApprovalsTotal > 0"
          @current-change="handlePendingPageChange"
          :current-page="pendingPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="pendingApprovalsTotal"
          style="margin-top: 15px; text-align: right;">
        </el-pagination>
      </el-tab-pane>

      <el-tab-pane label="授权记录" name="authHistory">
        <el-table :data="authHistory" v-loading="loading.authHistory" border stripe>
          <el-table-column prop="applyNo" label="申请单号" width="180"></el-table-column>
          <el-table-column prop="fromShopName" label="申请门店" width="150"></el-table-column>
          <el-table-column prop="targetShopName" label="目标门店" width="150"></el-table-column>
          <el-table-column label="授权类型" width="120">
            <template slot-scope="scope">
              <el-tag v-for="type in scope.row.authTypes" :key="type" size="small" style="margin-right: 5px;">
                {{ type === 1 ? '查看库存' : '调拨申请' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="visibleScope" label="可见范围" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.visibleScope === 1">仅汇总</span>
              <span v-else-if="scope.row.visibleScope === 2">批次明细</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="处理时间" width="160">
            <template slot-scope="scope">
              {{ parseTime(scope.row.approvedTime) }}
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="authHistoryTotal > 0"
          @current-change="handleHistoryPageChange"
          :current-page="historyPage"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="authHistoryTotal"
          style="margin-top: 15px; text-align: right;">
        </el-pagination>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="发起共享申请" :visible.sync="applyDialogVisible" width="500px" @close="resetApplyForm">
      <el-form :model="applyForm" :rules="applyRules" ref="applyForm" label-width="120px">
        <el-form-item label="目标门店编码" prop="targetShopCode" required>
          <el-input v-model="applyForm.targetShopCode" placeholder="请输入目标门店编码" clearable style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="申请理由" prop="reason">
          <el-input type="textarea" v-model="applyForm.reason" rows="3" placeholder="请输入申请理由（选填）"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="applyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitApplication" :loading="applyLoading">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as shareApi from '@/api/shop/share'

export default {
  name: 'AuthorizationManagement',
  data() {
    return {
      activeTab: 'myApplications',
      pageSize: 10,
      myApplications: [],
      myApplicationsTotal: 0,
      myAppsPage: 1,
      pendingApprovals: [],
      pendingApprovalsTotal: 0,
      pendingPage: 1,
      authHistory: [],
      authHistoryTotal: 0,
      historyPage: 1,
      loading: {
        myApplications: false,
        pendingApprovals: false,
        authHistory: false
      },
      applyDialogVisible: false,
      applyLoading: false,
      applyForm: {
        targetShopCode: '',
        reason: ''
      },
      applyRules: {
        targetShopCode: [{ required: true, message: '请输入目标门店编码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadMyApplications()
    this.loadPendingApprovals()
    this.loadAuthHistory()
  },
  methods: {
    loadMyApplications() {
              this.loading.myApplications = true
              shareApi.getMyApplications({ page: this.myAppsPage, limit: this.pageSize }).then(res => {
                if (res.code === 200 && res.rows) {
                  this.myApplications = res.rows
                  this.myApplicationsTotal = res.total
                }
              }).finally(() => {
                this.loading.myApplications = false
              })
            },
    loadPendingApprovals() {
      this.loading.pendingApprovals = true
      shareApi.getPendingApprovals({ page: this.pendingPage, limit: this.pageSize }).then(res => {
        if (res.code === 200 && res.rows) {
          this.pendingApprovals = res.rows
          this.pendingApprovalsTotal = res.total
        }
      }).finally(() => {
        this.loading.pendingApprovals = false
      })
    },
    loadAuthHistory() {
      this.loading.authHistory = true
      shareApi.getShareHistory({ page: this.historyPage, limit: this.pageSize }).then(res => {
        if (res.code === 200 && res.rows) {
          this.authHistory = res.rows
          this.authHistoryTotal = res.total
        }
      }).finally(() => {
        this.loading.authHistory = false
      })
    },
    openApplyDialog() {
      this.applyDialogVisible = true
    },
    submitApplication() {
      this.$refs.applyForm.validate(valid => {
        if (!valid) return
        this.applyLoading = true
        const params = {
          targetShopCode: this.applyForm.targetShopCode,
          reason: this.applyForm.reason
        }
        shareApi.add(params).then(res => {
          if (res.code === 200) {
            this.$message.success('申请已提交，等待对方审批')
            this.applyDialogVisible = false
            this.loadMyApplications()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        }).finally(() => {
          this.applyLoading = false
        })
      })
    },
    resetApplyForm() {
      this.applyForm = {
        targetShopCode: '',
        reason: ''
      }
      this.$nextTick(() => {
        if (this.$refs.applyForm) this.$refs.applyForm.clearValidate()
      })
    },
    approve(row) {
      this.$confirm('确认通过该共享申请？', '提示', { type: 'success' }).then(() => {
        shareApi.approve(row.id, {}).then(res => {
          if (res.code === 200) {
            this.$message.success('已通过')
            this.loadPendingApprovals()
            this.loadAuthHistory()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        })
      }).catch(() => {})
    },
    reject(row) {
      this.$prompt('请输入驳回理由', '驳回申请', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(({ value }) => {
        shareApi.reject(row.id, { remark: value }).then(res => {
          if (res.code === 200) {
            this.$message.warning('已驳回')
            this.loadPendingApprovals()
            this.loadAuthHistory()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        })
      }).catch(() => {})
    },
    cancelApplication(row) {
      this.$confirm('确认取消该申请？', '提示', { type: 'warning' }).then(() => {
        shareApi.cancel(row.id).then(res => {
          if (res.code === 200) {
            this.$message.info('已取消')
            this.loadMyApplications()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        })
      }).catch(() => {})
    },
    handleMyAppsPageChange(page) {
      this.myAppsPage = page
      this.loadMyApplications()
    },
    handlePendingPageChange(page) {
      this.pendingPage = page
      this.loadPendingApprovals()
    },
    handleHistoryPageChange(page) {
      this.historyPage = page
      this.loadAuthHistory()
    },
    handleTabClick() {
      if (this.activeTab === 'myApplications') {
        this.loadMyApplications()
      } else if (this.activeTab === 'pendingApproval') {
        this.loadPendingApprovals()
      } else if (this.activeTab === 'authHistory') {
        this.loadAuthHistory()
      }
    },
    getStatusType(status) {
      const map = { 1: 'warning', 2: 'success', 3: 'danger', 4: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 1: '待审批', 2: '已通过', 3: '已驳回', 4: '已取消' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.authorization-management {
  padding: 20px;
  background-color: #fff;
  min-height: calc(100vh - 100px);
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
  padding-bottom: 10px;
}
.page-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}
</style>
