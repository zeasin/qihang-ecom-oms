<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="供货单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入供货单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="supplyList" @selection-change="handleSelectionChange">
      <el-table-column label="供货单号" align="left" prop="orderNum" width="180"/>
      <el-table-column label="下单日期" align="center" prop="orderDate" width="120"/>
      <el-table-column label="订单金额" align="center" prop="orderAmount" width="120">
        <template slot-scope="scope">
          <span style="color: #E6A23C; font-weight: bold;">¥{{ scope.row.orderAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品数量" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.itemList ? scope.row.itemList.length : 0 }} 种
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="warning" size="small">待确认</el-tag>
          <el-tag v-else-if="scope.row.status === 101" type="primary" size="small">已确认</el-tag>
          <el-tag v-else-if="scope.row.status === 102" type="info" size="small">已发货</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success" size="small">已收货</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="success" size="small">已入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            v-if="scope.row.status === 1"
            size="mini"
            type="primary"
            icon="el-icon-check"
            @click="handleConfirm(scope.row)"
          >确认供货</el-button>
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

    <el-dialog :title="'供货单详情 - ' + detailOrder.orderNum" :visible.sync="detailOpen" width="800px">
      <el-row v-if="detailOrder">
        <el-col :span="12">
          <div class="info-item">
            <span class="label">订单金额：</span>
            <span class="value">{{ detailOrder.orderAmount }}</span>
          </div>
          <div class="info-item">
            <span class="label">下单日期：</span>
            <span class="value">{{ detailOrder.orderDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ parseTime(detailOrder.createTime) }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <span class="label">状态：</span>
            <span class="value">
              <el-tag v-if="detailOrder.status === 1" type="warning">待确认</el-tag>
              <el-tag v-else-if="detailOrder.status === 101" type="primary">已确认</el-tag>
              <el-tag v-else-if="detailOrder.status === 102" type="info">已发货</el-tag>
              <el-tag v-else-if="detailOrder.status === 2" type="success">已收货</el-tag>
              <el-tag v-else-if="detailOrder.status === 3" type="success">已入库</el-tag>
            </span>
          </div>
          <div class="info-item">
            <span class="label">仓库：</span>
            <span class="value">{{ detailOrder.warehouseName }}</span>
          </div>
        </el-col>
      </el-row>

      <el-table :data="detailOrder.itemList" border style="margin-top: 20px;">
        <el-table-column label="商品名称" align="left" prop="goodsName"/>
        <el-table-column label="规格" align="center" prop="specNum"/>
        <el-table-column label="颜色" align="center" prop="colorValue"/>
        <el-table-column label="尺寸" align="center" prop="sizeValue"/>
        <el-table-column label="单价" align="center" prop="price"/>
        <el-table-column label="数量" align="center" prop="quantity"/>
        <el-table-column label="金额" align="center">
          <template slot-scope="scope">
            {{ scope.row.amount }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getSupplyList, getSupplyDetail, confirmSupply } from '@/api/vendor/supply'

export default {
  name: 'SupplierSupply',
  data() {
    return {
      loading: true,
      ids: [],
      showSearch: true,
      total: 0,
      supplyList: [],
      detailOpen: false,
      detailOrder: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getSupplyList(this.queryParams).then(response => {
        this.supplyList = response.records
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        orderNum: null
      }
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleDetail(row) {
      getSupplyDetail(row.id).then(response => {
        this.detailOrder = response.data
        this.detailOpen = true
      })
    },
    handleConfirm(row) {
      this.$confirm('确定要确认该供货单吗？确认后将生成发货订单。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        confirmSupply(row.id).then(response => {
          if (response.code === 200) {
            this.$message.success('确认成功')
            this.getList()
          } else {
            this.$message.error(response.msg)
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.info-item {
  margin-bottom: 10px;
}
.label {
  display: inline-block;
  width: 80px;
  color: #999;
}
.value {
  color: #333;
}
</style>
