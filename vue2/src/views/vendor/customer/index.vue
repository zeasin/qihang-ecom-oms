<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item label="店铺名称" prop="shopName">
        <el-input
          v-model="queryParams.shopName"
          placeholder="请输入店铺名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属商户" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入所属商户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="customerList">
      <el-table-column label="店铺名称" align="left" prop="shopName" width="180" />
      <el-table-column label="所属商户" align="left" prop="merchantName" width="180" />
      <el-table-column label="累计订单数" align="center" prop="totalOrders" width="120" />
      <el-table-column label="累计金额" align="center" prop="totalAmount" width="120">
        <template slot-scope="scope">
          <span>¥{{ scope.row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="首次下单时间" align="center" prop="firstOrderTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstOrderTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最近下单时间" align="center" prop="lastOrderTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastOrderTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            :type="scope.row.status === 1 ? 'danger' : 'primary'"
            @click="handleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
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
  </div>
</template>

<script>
import { listCustomer, updateCustomerStatus } from "@/api/vendor/customer";

export default {
  name: "SupplierCustomer",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      customerList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopName: null,
        merchantName: null,
        status: null
      },
      form: {},
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCustomer(this.queryParams).then(response => {
        this.customerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleStatus(row) {
      const newStatus = row.status === 1 ? 0 : 1;
      this.$modal.confirm('确认' + (newStatus === 1 ? '启用' : '禁用') + '该客户吗？').then(() => {
        return updateCustomerStatus({ id: row.id, status: newStatus });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => {});
    }
  }
};
</script>
