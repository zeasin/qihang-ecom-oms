<template>
  <el-dialog :visible.sync="dialogVisible" title="选择供应商商品" width="900px">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="供应商" prop="supplierId" v-if="!isSupplier">
        <el-select v-model="queryParams.supplierId" filterable clearable placeholder="请选择供应商" @change="handleQuery" style="width: 200px;">
          <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
          style="width: 200px;"
        />
      </el-form-item>
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入SKU编码"
          clearable
          @keyup.enter.native="handleQuery"
          style="width: 150px;"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-check"
          size="mini"
          :disabled="single"
          @click="sendDataToParent"
        >确认选择</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="skuList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="商品名称" align="left" prop="productName" width="200"/>
      <el-table-column label="规格" align="center" prop="standard" width="150"/>
      <el-table-column label="SKU编码" align="center" prop="skuCode" width="150"/>
      <el-table-column label="条码" align="center" prop="barCode" width="150"/>
      <el-table-column label="颜色" align="center" prop="colorValue" width="80"/>
      <el-table-column label="尺寸" align="center" prop="sizeValue" width="80"/>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success" size="small">销售中</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="warning" size="small">已下架</el-tag>
          <el-tag v-else size="small">待审核</el-tag>
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
  </el-dialog>
</template>

<script>
import { listSupplierGoodsSku } from "@/api/goods/supplier_goods_sku";
import { listSupplier } from "@/api/goods/supplier";
import { getUserProfile } from "@/api/system/user";

export default {
  name: 'PopupSupplierGoods',
  data() {
    return {
      dialogVisible: false,
      isSupplier: false,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      skuList: [],
      supplierList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        supplierId: null,
        productName: null,
        skuCode: null,
      },
    }
  },
  mounted() {
    getUserProfile().then(res => {
      if (res.data.userType == 30) {
        this.isSupplier = true;
        this.queryParams.supplierId = res.data.deptId;
      }
      this.loadSupplierList();
    });
  },
  methods: {
    loadSupplierList() {
      listSupplier({pageNum: 1, pageSize: 100}).then(resp => {
        this.supplierList = resp.rows || [];
        if (this.isSupplier && this.supplierList.length > 0) {
          this.queryParams.supplierId = this.supplierList[0].id;
        }
        this.getList();
      });
    },
    openDialog() {
      this.getList();
      this.dialogVisible = true;
    },
    sendDataToParent() {
      if (!this.ids || this.ids.length === 0) {
        this.$modal.msgError("请选择商品");
        return;
      }
      const filteredList = this.skuList.filter(item => this.ids.includes(item.id));
      this.$emit('data-from-select', filteredList);
      this.queryParams.pageNum = 1;
      this.queryParams.productName = null;
      this.queryParams.skuCode = null;
      this.dialogVisible = false;
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    getList() {
      this.loading = true;
      listSupplierGoodsSku(this.queryParams).then(response => {
        this.skuList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>
