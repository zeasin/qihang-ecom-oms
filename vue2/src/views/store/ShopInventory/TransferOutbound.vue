<template>
  <div class="transfer-outbound">
    <div class="page-header">
      <h2>调拨出库</h2>
      <el-button icon="el-icon-back" @click="goBack">返回</el-button>
    </div>

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="调拨单号">
        <el-input v-model="searchForm.applyNo" placeholder="请输入调拨单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 150px;">
          <el-option label="待出库" :value="2"></el-option>
          <el-option label="已出库" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
        <el-button icon="el-icon-refresh-right" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="transferList" v-loading="loading" border stripe>
      <el-table-column prop="applyNo" label="调拨单号" width="180"></el-table-column>
      <el-table-column prop="fromShopName" label="调出门店" width="150"></el-table-column>
      <el-table-column prop="toShopName" label="调入门店" width="150"></el-table-column>
      <el-table-column prop="skuName" label="商品名称" min-width="150"></el-table-column>
      <el-table-column prop="applyQuantity" label="调拨数量" width="100" align="right"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="expectedTime" label="期望时间" width="160"></el-table-column>
      <el-table-column prop="outboundTime" label="出库时间" width="160"></el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 2" type="primary" size="small" @click="confirmOutbound(scope.row)">确认出库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      @current-change="handlePageChange"
      :current-page="page"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      style="margin-top: 15px; text-align: right;">
    </el-pagination>

    <el-dialog title="确认出库" :visible.sync="outboundDialogVisible" width="400px">
      <div style="padding: 20px 0;">
        <p>确认对调拨单 <strong>{{ currentTransfer.applyNo }}</strong> 进行出库操作？</p>
        <p style="margin-top: 10px; color: #909399;">
          商品：{{ currentTransfer.skuName }}<br/>
          数量：{{ currentTransfer.applyQuantity }}
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="outboundDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitOutbound" :loading="outboundLoading">确认出库</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as transferApi from '@/api/shop/transfer'

export default {
  name: 'TransferOutbound',
  data() {
    return {
      loading: false,
      searchForm: {
        applyNo: '',
        status: null
      },
      transferList: [],
      total: 0,
      page: 1,
      pageSize: 10,
      outboundDialogVisible: false,
      outboundLoading: false,
      currentTransfer: {}
    }
  },
  created() {
    this.loadTransferList()
  },
  methods: {
    loadTransferList() {
      this.loading = true
      const params = {
        page: this.page,
        limit: this.pageSize,
        applyNo: this.searchForm.applyNo || null,
        status: this.searchForm.status
      }
      transferApi.list(params).then(res => {
        if (res.code === 200 && res.rows) {
          this.transferList = res.rows.filter(item => item.status === 2 || item.status === 4)
          this.total = res.total
        }
      }).finally(() => {
        this.loading = false
      })
    },
    search() {
      this.page = 1
      this.loadTransferList()
    },
    resetSearch() {
      this.searchForm = {
        applyNo: '',
        status: null
      }
      this.page = 1
      this.loadTransferList()
    },
    handlePageChange(page) {
      this.page = page
      this.loadTransferList()
    },
    confirmOutbound(row) {
      this.currentTransfer = row
      this.outboundDialogVisible = true
    },
    submitOutbound() {
      this.outboundLoading = true
      transferApi.outbound({ id: this.currentTransfer.id, operator: 'admin' }).then(res => {
        if (res.code === 200) {
          this.$message.success('出库成功')
          this.outboundDialogVisible = false
          this.loadTransferList()
        } else {
          this.$message.error(res.msg || '出库失败')
        }
      }).finally(() => {
        this.outboundLoading = false
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    getStatusType(status) {
      const map = { 1: 'warning', 2: 'primary', 3: 'danger', 4: 'success', 5: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 1: '待审批', 2: '已批准', 3: '已驳回', 4: '已完成', 5: '已取消' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.transfer-outbound {
  padding: 20px;
  background-color: #fff;
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
.search-form {
  margin-bottom: 20px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
}
</style>