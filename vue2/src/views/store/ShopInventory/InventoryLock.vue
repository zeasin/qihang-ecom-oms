<template>
  <div class="inventory-lock">
    <div class="page-header">
      <h2>库存锁定管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog">创建锁库</el-button>
    </div>

    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="锁定单号">
        <el-input v-model="searchForm.lockNo" placeholder="请输入锁定单号" clearable></el-input>
      </el-form-item>
      <el-form-item label="来源类型">
        <el-select v-model="searchForm.sourceType" placeholder="请选择" clearable style="width: 150px;">
          <el-option label="调拨申请" :value="1"></el-option>
          <el-option label="临时锁库" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 150px;">
          <el-option label="锁定中" :value="1"></el-option>
          <el-option label="已释放" :value="2"></el-option>
          <el-option label="已取消" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
        <el-button icon="el-icon-refresh-right" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="lockList" v-loading="loading" border stripe>
      <el-table-column prop="lockNo" label="锁定单号" width="200"></el-table-column>
      <el-table-column label="来源类型" width="120">
        <template slot-scope="scope">
          <el-tag :type="scope.row.sourceType === 1 ? 'success' : 'warning'" size="small">
            {{ scope.row.sourceType === 1 ? '调拨申请' : '临时锁库' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sourceId" label="关联ID" width="100"></el-table-column>
      <el-table-column prop="batchId" label="批次ID" width="100"></el-table-column>
      <el-table-column prop="lockQuantity" label="锁定数量" width="100" align="right"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="expireTime" label="过期时间" width="160"></el-table-column>
      <el-table-column prop="createdTime" label="创建时间" width="160"></el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === 1" type="danger" size="small" @click="releaseLock(scope.row)">释放</el-button>
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

    <el-dialog title="创建锁库" :visible.sync="addDialogVisible" width="500px" @close="resetAddForm">
      <el-form :model="addForm" :rules="addRules" ref="addForm" label-width="100px">
        <el-form-item label="批次ID" prop="batchId">
          <el-input-number v-model="addForm.batchId" :min="1" controls-position="right" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="锁定数量" prop="lockQuantity">
          <el-input-number v-model="addForm.lockQuantity" :min="1" controls-position="right" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker v-model="addForm.expireTime" type="datetime" placeholder="选择过期时间" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="addForm.remark" rows="3" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as inventoryLockApi from '@/api/shop/inventoryLock'

export default {
  name: 'InventoryLock',
  data() {
    return {
      loading: false,
      searchForm: {
        lockNo: '',
        sourceType: null,
        status: null
      },
      lockList: [],
      total: 0,
      page: 1,
      pageSize: 10,
      addDialogVisible: false,
      addLoading: false,
      addForm: {
        batchId: null,
        lockQuantity: 1,
        expireTime: null,
        remark: '',
        sourceType: 2,
        sourceId: 0
      },
      addRules: {
        batchId: [{ required: true, message: '请输入批次ID', trigger: 'blur' }],
        lockQuantity: [{ required: true, message: '请输入锁定数量', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadLockList()
  },
  methods: {
    loadLockList() {
      this.loading = true
      const params = {
        page: this.page,
        limit: this.pageSize,
        lockNo: this.searchForm.lockNo || null,
        sourceType: this.searchForm.sourceType,
        status: this.searchForm.status
      }
      inventoryLockApi.list(params).then(res => {
        if (res.code === 200 && res.rows) {
          this.lockList = res.rows
          this.total = res.total
        }
      }).finally(() => {
        this.loading = false
      })
    },
    search() {
      this.page = 1
      this.loadLockList()
    },
    resetSearch() {
      this.searchForm = {
        lockNo: '',
        sourceType: null,
        status: null
      }
      this.page = 1
      this.loadLockList()
    },
    handlePageChange(page) {
      this.page = page
      this.loadLockList()
    },
    openAddDialog() {
      this.addDialogVisible = true
    },
    resetAddForm() {
      this.addForm = {
        batchId: null,
        lockQuantity: 1,
        expireTime: null,
        remark: '',
        sourceType: 2,
        sourceId: 0
      }
      if (this.$refs.addForm) {
        this.$refs.addForm.clearValidate()
      }
    },
    submitAdd() {
      this.$refs.addForm.validate(valid => {
        if (!valid) return
        this.addLoading = true
        const params = {
          batchId: this.addForm.batchId,
          lockQuantity: this.addForm.lockQuantity,
          expireTime: this.addForm.expireTime,
          sourceType: this.addForm.sourceType,
          sourceId: this.addForm.sourceId
        }
        inventoryLockApi.add(params).then(res => {
          if (res.code === 200) {
            this.$message.success('锁库创建成功')
            this.addDialogVisible = false
            this.loadLockList()
          } else {
            this.$message.error(res.msg || '创建失败')
          }
        }).finally(() => {
          this.addLoading = false
        })
      })
    },
    releaseLock(row) {
      this.$confirm('确认释放该库存锁定？', '提示', { type: 'warning' }).then(() => {
        inventoryLockApi.release(row.lockNo).then(res => {
          if (res.code === 200) {
            this.$message.success('已释放')
            this.loadLockList()
          } else {
            this.$message.error(res.msg || '释放失败')
          }
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      const map = { 1: 'warning', 2: 'success', 3: 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 1: '锁定中', 2: '已释放', 3: '已取消' }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.inventory-lock {
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