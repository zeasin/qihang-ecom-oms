<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="商品名称" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="已启用" :value="1" />
          <el-option label="已禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加预警</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="alertList" @selection-change="handleSelectionChange">
      <el-table-column label="商品名称" align="left" prop="goodsName" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150" />
      <el-table-column label="规格" align="left" prop="skuName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="预警数量" align="center" prop="alertQty" width="100">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.alertQty }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="当前库存" align="center" prop="currentQty" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStockStatusType(scope.row)">
            {{ scope.row.currentQty || 0 }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="isLowStock(scope.row)" type="danger">库存不足</el-tag>
          <el-tag v-else type="success">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改库存预警对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择商品" prop="goodsId">
          <el-select
            v-model="form.goodsId"
            filterable
            remote
            placeholder="输入商品名称搜索"
            :remote-method="searchGoods"
            :loading="goodsLoading"
            @change="handleGoodsChange"
            style="width: 100%"
          >
            <el-option
              v-for="item in goodsOptions"
              :key="item.id"
              :label="item.goodsName"
              :value="item.id">
              <span style="float: left">{{ item.goodsName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px;">{{ item.standard }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="SKU编码">
          <span>{{ form.skuCode || '-' }}</span>
        </el-form-item>
        <el-form-item label="规格">
          <span>{{ form.skuName || '-' }}</span>
        </el-form-item>
        <el-form-item label="预警数量" prop="alertQty">
          <el-input-number v-model="form.alertQty" :min="0" :max="999999" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockAlert, addStockAlert, updateStockAlert, deleteStockAlert, setAlertStatus, listWarehouseGoods } from '@/api/vendor/stockAlert'

export default {
  name: 'StockAlert',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      alertList: [],
      goodsOptions: [],
      goodsLoading: false,
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        goodsName: null,
        status: null
      },
      form: {
        id: null,
        goodsId: null,
        goodsName: null,
        skuCode: null,
        skuName: null,
        alertQty: 0,
        remark: null
      },
      rules: {
        goodsId: [{ required: true, message: '请选择商品', trigger: 'change' }],
        alertQty: [{ required: true, message: '请输入预警数量', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listStockAlert(this.queryParams).then(response => {
        this.alertList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.status = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleAdd() {
      this.reset()
      this.title = '添加库存预警'
      this.open = true
      this.goodsOptions = []
    },
    handleUpdate(row) {
      this.reset()
      this.form = {
        id: row.id,
        goodsId: row.goodsId,
        goodsName: row.goodsName,
        skuCode: row.skuCode,
        skuName: row.skuName,
        alertQty: row.alertQty,
        remark: row.remark
      }
      this.title = '修改库存预警'
      this.open = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该库存预警设置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteStockAlert(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    handleStatusChange(row) {
      const action = row.status === 1 ? '启用' : '禁用'
      setAlertStatus(row.id, row.status).then(() => {
        this.$message.success(action + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    searchGoods(keyword) {
      if (!keyword || keyword.trim() === '') {
        this.goodsOptions = []
        return
      }
      this.goodsLoading = true
      listWarehouseGoods({ keyword: keyword, pageSize: 50 }).then(response => {
        this.goodsOptions = response.rows || []
        this.goodsLoading = false
      }).catch(() => {
        this.goodsOptions = []
        this.goodsLoading = false
      })
    },
    handleGoodsChange(goodsId) {
      const selected = this.goodsOptions.find(item => item.id === goodsId)
      if (selected) {
        this.form.skuCode = selected.goodsNo
        this.form.skuName = selected.standard
      }
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id) {
            updateStockAlert(this.form.id, { alertQty: this.form.alertQty, remark: this.form.remark }).then(() => {
              this.$message.success('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addStockAlert(this.form).then(() => {
              this.$message.success('添加成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
    },
    reset() {
      this.form = {
        id: null,
        goodsId: null,
        goodsName: null,
        skuCode: null,
        skuName: null,
        alertQty: 0,
        remark: null
      }
      this.resetForm('form')
    },
    getStockStatusType(row) {
      if (row.currentQty === null || row.currentQty === undefined) {
        return 'info'
      }
      if (row.currentQty < row.alertQty) {
        return 'danger'
      }
      return 'success'
    },
    isLowStock(row) {
      if (row.currentQty === null || row.currentQty === undefined) {
        return false
      }
      return row.currentQty < row.alertQty
    }
  }
}
</script>
