<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="出库单号" prop="outNum">
        <el-input
          v-model="queryParams.outNum"
          placeholder="请输入出库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源单号" prop="sourceNum">
        <el-input
          v-model="queryParams.sourceNum"
          placeholder="请输入源单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出库状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待出库" :value="0" />
          <el-option label="部分出库" :value="1" />
          <el-option label="全部出库" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockOutList" @selection-change="handleSelectionChange">
      <el-table-column label="出库单号" align="center" prop="outNum" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleDetail(scope.row)"
          >{{ scope.row.outNum }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="源单号" align="center" prop="sourceNum" width="180">
        <template slot-scope="scope">
          {{ scope.row.sourceNum || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="出库类型" align="center" prop="type" width="120">
        <template slot-scope="scope">
          <el-tag size="small" type="info">订单出库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0" type="warning">待出库</el-tag>
          <el-tag size="small" v-else-if="scope.row.status === 1" type="primary">部分出库</el-tag>
          <el-tag size="small" v-else-if="scope.row.status === 2" type="success">全部出库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品数" align="center" prop="goodsUnit" width="80" />
      <el-table-column label="规格数" align="center" prop="specUnit" width="80" />
      <el-table-column label="总件数" align="center" prop="specUnitTotal" width="80" />
      <el-table-column label="已出库" align="center" prop="outTotal" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="operatorName" width="100" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status !== 2"
            size="mini"
            type="primary"
            plain
            icon="el-icon-d-arrow-right"
            @click="handleStockOut(scope.row)"
          >出库</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
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

    <!-- 出库单详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form :model="form" label-width="100px" inline>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="出库单号">{{ form.outNum }}</el-descriptions-item>
          <el-descriptions-item label="源单号">{{ form.sourceNum || '-' }}</el-descriptions-item>
          <el-descriptions-item label="出库类型">
            <el-tag size="small" type="info">订单出库</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag size="small" v-if="form.status === 0" type="warning">待出库</el-tag>
            <el-tag size="small" v-else-if="form.status === 1" type="primary">部分出库</el-tag>
            <el-tag size="small" v-else-if="form.status === 2" type="success">全部出库</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="商品数">{{ form.goodsUnit }}</el-descriptions-item>
          <el-descriptions-item label="规格数">{{ form.specUnit }}</el-descriptions-item>
          <el-descriptions-item label="总件数">{{ form.specUnitTotal }}</el-descriptions-item>
          <el-descriptions-item label="已出库">{{ form.outTotal }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ parseTime(form.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ parseTime(form.completeTime) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ form.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="center">出库商品明细</el-divider>

        <el-table :data="form.itemList" border size="small">
          <el-table-column label="序号" align="center" type="index" width="50" />
          <el-table-column label="商品名称" align="left" prop="goodsName" min-width="150" :show-overflow-tooltip="true" />
          <el-table-column label="规格编码" align="left" prop="goodsNum" width="120" />
          <el-table-column label="规格" align="left" prop="skuName" width="150" :show-overflow-tooltip="true" />
          <el-table-column label="待出库数量" align="center" prop="originalQuantity" width="100">
            <template slot-scope="scope">
              <el-tag size="small" type="danger">{{ scope.row.originalQuantity }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="已出库数量" align="center" prop="outQuantity" width="100">
            <template slot-scope="scope">
              <el-tag size="small">{{ scope.row.outQuantity }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="100" fixed="right">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.status < 2"
                size="mini"
                type="danger"
                plain
                icon="el-icon-d-arrow-right"
                @click="handleItemStockOut(scope.row)"
              >出库</el-button>
              <span v-else class="text-success">已完成</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择批次出库对话框 -->
    <el-dialog title="选择批次出库" :visible.sync="batchOpen" width="600px" append-to-body>
      <el-form :model="batchForm" label-width="100px">
        <el-form-item label="商品">
          <span>{{ batchForm.goodsName }}</span>
        </el-form-item>
        <el-form-item label="规格">
          <span>{{ batchForm.skuName }}</span>
        </el-form-item>
        <el-form-item label="待出库数量">
          <el-tag type="danger">{{ batchForm.pendingQty }}</el-tag>
        </el-form-item>
        <el-divider content-position="center">批次库存</el-divider>
        <el-table :data="batchList" border size="small" max-height="300">
          <el-table-column label="批次号" align="center" prop="batchNum" width="150" />
          <el-table-column label="可用库存" align="center" prop="currentQty" width="100" />
          <el-table-column label="选择出库数量" align="center" width="150">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.selectedQty"
                :min="1"
                :max="scope.row.currentQty"
                size="small"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitBatchStockOut">确认出库</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockOut, getStockOutEntry, stockOut, searchSkuInventoryBatch } from '@/api/vendor/stockup'

export default {
  name: 'VendorStockUp',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      stockOutList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outNum: null,
        sourceNum: null,
        type: 1,
        status: null
      },
      title: '',
      open: false,
      form: {},
      batchOpen: false,
      batchForm: {},
      batchList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listStockOut(this.queryParams).then(response => {
        this.stockOutList = response.rows
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
      this.queryParams.type = 1
      this.queryParams.status = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleDetail(row) {
      const id = row.id
      getStockOutEntry(id).then(response => {
        this.form = response.data
        this.title = '出库单详情'
        this.open = true
      })
    },
    handleStockOut(row) {
      const id = row.id
      getStockOutEntry(id).then(response => {
        this.form = response.data
        this.title = '出库操作'
        this.open = true
      })
    },
    handleItemStockOut(row) {
      this.batchForm = {
        entryItemId: row.id,
        entryId: this.form.id,
        goodsId: row.goodsId,
        goodsName: row.goodsName,
        skuName: row.skuName,
        pendingQty: row.originalQuantity - row.outQuantity
      }
      this.batchList = []
      this.batchOpen = true
      this.loadBatchList(row.goodsId)
    },
    loadBatchList(goodsId) {
      searchSkuInventoryBatch({ goodsId: goodsId }).then(response => {
        this.batchList = (response.rows || []).map(item => {
          const maxQty = Math.min(item.currentQty, this.batchForm.pendingQty)
          return {
            ...item,
            selectedQty: maxQty > 0 ? maxQty : 0
          }
        })
      })
    },
    submitBatchStockOut() {
      const selectedBatches = this.batchList.filter(item => item.selectedQty > 0)
      if (selectedBatches.length === 0) {
        this.$message.warning('请选择要出库的批次')
        return
      }

      Promise.all(
        selectedBatches.map(batch => {
          const data = {
            entryItemId: this.batchForm.entryItemId,
            batchId: batch.id,
            outQty: batch.selectedQty
          }
          return stockOut(data)
        })
      ).then(() => {
        this.$message.success('出库成功')
        this.batchOpen = false
        this.handleStockOut({ id: this.form.id })
        this.getList()
      }).catch(error => {
        this.$message.error('出库失败: ' + (error.msg || error.message))
      })
    },
    cancel() {
      this.open = false
    }
  }
}
</script>

<style scoped>
.text-success {
  color: #67c23a;
  font-size: 12px;
}
</style>