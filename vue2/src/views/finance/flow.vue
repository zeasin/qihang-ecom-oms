<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="凭证号" prop="voucherNo">
        <el-input v-model="queryParams.voucherNo" placeholder="请输入凭证号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="会计科目" prop="accountType">
        <el-select v-model="queryParams.accountType" placeholder="请选择会计科目" clearable @change="handleQuery">
          <el-option label="销售收入" value="销售收入" />
          <el-option label="采购成本" value="采购成本" />
          <el-option label="日常支出" value="日常支出" />
          <el-option label="差旅报销" value="差旅报销" />
          <el-option label="平台退款" value="平台退款" />
          <el-option label="其他收入" value="其他收入" />
          <el-option label="其他支出" value="其他支出" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源类型" prop="sourceType">
        <el-select v-model="queryParams.sourceType" placeholder="请选择来源类型" clearable @change="handleQuery">
          <el-option label="订单" value="order" />
          <el-option label="采购" value="purchase" />
          <el-option label="费用" value="expense" />
          <el-option label="回收" value="recovery" />
          <el-option label="手动" value="manual" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="日期范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          @change="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="凭证号" align="center" prop="voucherNo" width="180" />
      <el-table-column label="会计科目" align="center" prop="accountType" width="120">
        <template slot-scope="scope">
          <el-tag size="small" :type="getAccountTypeTag(scope.row.accountType)">{{ scope.row.accountType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="收入金额" align="right" prop="incomeAmount" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.incomeAmount && scope.row.incomeAmount > 0" class="income-text">{{ scope.row.incomeAmount.toFixed(2) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="支出金额" align="right" prop="expenseAmount" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.expenseAmount && scope.row.expenseAmount > 0" class="expense-text">{{ scope.row.expenseAmount.toFixed(2) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="来源类型" align="center" prop="sourceType" width="100">
        <template slot-scope="scope">
          <span>{{ getSourceTypeName(scope.row.sourceType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="来源单据号" align="center" prop="sourceNo" width="180" />
      <el-table-column label="订单号" align="center" prop="orderNo" width="180" />
      <el-table-column label="费用日期" align="center" prop="expenseDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expenseDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createdTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="会计科目" prop="accountType">
              <el-select v-model="form.accountType" placeholder="请选择会计科目">
                <el-option label="销售收入" value="销售收入" />
                <el-option label="采购成本" value="采购成本" />
                <el-option label="日常支出" value="日常支出" />
                <el-option label="差旅报销" value="差旅报销" />
                <el-option label="平台退款" value="平台退款" />
                <el-option label="其他收入" value="其他收入" />
                <el-option label="其他支出" value="其他支出" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用日期" prop="expenseDate">
              <el-date-picker v-model="form.expenseDate" type="date" placeholder="选择费用日期" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收入金额" prop="incomeAmount">
              <el-input-number v-model="form.incomeAmount" :min="0" :precision="2" placeholder="请输入收入金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支出金额" prop="expenseAmount">
              <el-input-number v-model="form.expenseAmount" :min="0" :precision="2" placeholder="请输入支出金额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源类型" prop="sourceType">
              <el-select v-model="form.sourceType" placeholder="请选择来源类型">
                <el-option label="订单" value="order" />
                <el-option label="采购" value="purchase" />
                <el-option label="费用" value="expense" />
                <el-option label="回收" value="recovery" />
                <el-option label="手动" value="manual" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源单据号" prop="sourceNo">
              <el-input v-model="form.sourceNo" placeholder="请输入来源单据号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入订单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, getInfo, add, update, remove } from '@/api/finance/flow'
import { parseTime } from '@/utils/zhijian'

export default {
  name: 'FinanceFlow',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      dataList: [],
      title: '',
      open: false,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        voucherNo: undefined,
        accountType: undefined,
        sourceType: undefined,
        orderNo: undefined
      },
      form: {},
      rules: {
        accountType: [{ required: true, message: '请选择会计科目', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }
      list(params)
        .then(res => {
          this.dataList = res.rows || []
          this.total = res.total
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.handleQuery()
    },
    handleAdd() {
      this.form = {
        incomeAmount: 0,
        expenseAmount: 0
      }
      this.open = true
      this.title = '新增财务流水'
    },
    handleView(row) {
      this.form = { ...row }
      this.open = true
      this.title = '查看财务流水'
    },
    submitForm() {
      if (!this.form.id) {
        add(this.form).then(() => {
          this.$message.success('新增成功')
          this.open = false
          this.getList()
        })
      } else {
        update(this.form).then(() => {
          this.$message.success('修改成功')
          this.open = false
          this.getList()
        })
      }
    },
    cancel() {
      this.open = false
    },
    handleExport() {
      this.download('/api/erp-api/finance/flow/export', { ...this.queryParams }, `财务流水_${parseTime(new Date())}.xlsx`)
    },
    getAccountTypeTag(type) {
      const map = {
        '销售收入': 'success',
        '采购成本': 'danger',
        '日常支出': 'warning',
        '差旅报销': 'warning',
        '平台退款': 'danger',
        '其他收入': 'success',
        '其他支出': 'warning'
      }
      return map[type] || 'info'
    },
    getSourceTypeName(type) {
      const map = {
        'order': '订单',
        'purchase': '采购',
        'expense': '费用',
        'recovery': '回收',
        'manual': '手动'
      }
      return map[type] || type
    }
  }
}
</script>

<style scoped>
.income-text {
  color: #67c23a;
  font-weight: bold;
}
.expense-text {
  color: #f56c6c;
  font-weight: bold;
}
</style>