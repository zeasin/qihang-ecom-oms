<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" filterable  placeholder="搜索店铺" >
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">其他</span>
          </el-option>
          </el-select>
      </el-form-item>


      <el-form-item label="订单编号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>


      <el-form-item label="物流单号" prop="shipNo">
        <el-input
          v-model="queryParams.shipNo"
          placeholder="请输入物流单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="发货时间" prop="shipTime">
        <el-date-picker clearable
          v-model="queryParams.shipTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择发货时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-document-checked"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >批量结算确认</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['scm:agentShipping:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="agentShippingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="left" prop="orderNum" width="200"/>
      <!-- <el-table-column label="主键ID" align="center" prop="id" /> -->
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
          <span>{{ shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''  }}</span>
        </template>
      </el-table-column>
      <el-table-column label="包装费" align="center" prop="packageAmount" />
      <el-table-column label="物流费" align="center" prop="shipAmount" />
      <el-table-column label="总成本" align="center" prop="amount" />
      <el-table-column label="物流公司" align="center" prop="shipCompany" />
      <el-table-column label="物流单号" align="center" prop="shipNo" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="shipTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0">待结算</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1">已结算</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" >
        <template slot-scope="scope">

          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="text"
            icon="el-icon-document-checked"
            @click="handleDelete(scope.row)"
            v-hasPermi="['scm:agentShipping:remove']"
          >结算确认</el-button>
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
import {
  listWarehouseShipBill
} from "@/api/financial/shipment_bill";
import { listSupplier} from "@/api/goods/supplier";
import { listShop } from "@/api/shop/shop";
import {listLogistics} from "@/api/shipping/logistics";
export default {
  name: "AgentShipping",
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
      // 供应商代发货表格数据
      agentShippingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        shopType: null,
        supplierId: null,
        orderNum: null,
        orderItemId: null,
        orderDate: null,
        goodsId: null,
        specId: null,
        goodsTitle: null,
        goodsImg: null,
        goodsNum: null,
        goodsSpec: null,
        specNum: null,
        goodsPrice: null,
        quantity: null,
        shipCompany: null,
        shipNo: null,
        shipCost: null,
        shipTime: null,
        status: null,
        itemAmount: null,
      },
      shopList: [],
      logisticsList:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        goodsPrice: [{ required: true, message: '不能为空' }],
        shipCompany: [{ required: true, message: '不能为空' }],
        shipNo: [{ required: true, message: '不能为空' }],
        shipCost: [{ required: true, message: '不能为空' }],
        shipTime: [{ required: true, message: '不能为空' }],
      },
      supplierLoading:false,
      supplierList:[]
    };
  },
  created() {

    this.getList();
    listShop({}).then(response => {
        this.shopList = response.rows;
      });

    listLogistics({}).then(resp=>{
      this.logisticsList = resp.rows
    })
  },
  methods: {
    searchSupplier(query){
      this.supplierLoading = true;
      const qw = {
        name:query
      }
      listSupplier(qw).then(response => {
        this.supplierList = response.rows;
        this.supplierLoading = false;
      });
    },
    /** 查询供应商代发货列表 */
    getList() {
      this.loading = true;
      listWarehouseShipBill(this.queryParams).then(response => {
        this.agentShippingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null,
        shopType: null,
        supplierId: null,
        orderNum: null,
        orderItemId: null,
        orderDate: null,
        goodsId: null,
        specId: null,
        goodsTitle: null,
        goodsImg: null,
        goodsNum: null,
        goodsSpec: null,
        specNum: null,
        goodsPrice: null,
        quantity: null,
        remark: null,
        shipCompany: null,
        shipNo: null,
        shipCost: null,
        shipTime: null,
        status: null,
        itemAmount: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null
      };
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加供应商代发货";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAgentShipping(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改供应商代发货";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
            updateAgentShipping(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认编号为"' + ids + '"的订单批量付款呢？').then(function() {
        return delAgentShipping(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('scm/agentShipping/export', {
        ...this.queryParams
      }, `agentShipping_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
