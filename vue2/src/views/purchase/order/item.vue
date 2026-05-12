<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="采购单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入采购单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Sku编码" prop="specNum">
        <el-input
          v-model="queryParams.specNum"
          placeholder="请输入Sku编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="SkuId" prop="specId">
        <el-input
          v-model="queryParams.specId"
          placeholder="请输入商品SkuId"
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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['purchase:purchaseOrder:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="dataList" style="width: 100%">

      <el-table-column label="ID" align="center" prop="id" width="60px"/>
      <el-table-column label="采购单号" align="center" prop="orderNum" />
      <el-table-column prop="colorImage" label="商品图片">
        <template slot-scope="scope">
          <el-image style="width: 40px; height: 40px" :src="scope.row.colorImage"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称" width="300px"></el-table-column>
      <el-table-column prop="specId" label="SkuId"></el-table-column>
      <el-table-column prop="specNum" label="Sku编码"></el-table-column>
      <el-table-column prop="colorValue" label="颜色">
      </el-table-column>
      <el-table-column prop="sizeValue" label="尺码">
      </el-table-column>
      <el-table-column prop="styleValue" label="款式">
      </el-table-column>

      <el-table-column prop="price" label="采购价" :formatter="amountFormatter">
      </el-table-column>
      <el-table-column prop="quantity" label="采购数量">
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
import {
  listPurchaseOrder, getPurchaseOrder, delPurchaseOrder, addPurchaseOrder, updatePurchaseOrder,
  listPurchaseOrderItem
} from "@/api/purchase/purchaseOrder";
import {listAllSupplier, listSupplier} from "@/api/goods/supplier";
import { listLogistics } from "@/api/purchase/logistics";
import userInfo from '@/views/system/user/profile/userInfo.vue'
import Clipboard from "clipboard";
import {getUserProfile, listUser} from '@/api/system/user'
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";
import {amountFormatter, copyActiveCode} from "@/utils/zhijian";


export default {
  name: "PurchaseOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 采购订单表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        goodsName: null,
        specNum: null,
        specId: null
      },
      // 表单参数
      form: {
      },

      // 表单校验
      rules: {
      },
      supplierLoading:false,
      supplierList:[],
      userList:[],
      logisticsList:[],
    };
  },
  created() {
  },
  methods: {
    amountFormatter,
    copyActiveCode,
    dateToString(timespan){
      var date = new Date(timespan * 1000);
      return date.toLocaleString();
    },
    // searchSupplier(query){
    //   this.supplierLoading = true;
    //   const qw = {
    //     name:query
    //   }
    //   listSupplier(qw).then(response => {
    //     this.supplierList = response.rows;
    //     this.supplierLoading = false;
    //   });
    // },
    /** 查询采购订单列表 */
    getList() {
      this.loading = true;
      listPurchaseOrderItem(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 详情
    handleDetail(row){
      // this.$router.push('/scm/purchase/order/detail',{id:row.id});
      this.$router.push({path :"purchase_order_detail", query: { id: row.id }});
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/purchaseOrder/export', {
        ...this.queryParams
      }, `purchaseOrder_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
