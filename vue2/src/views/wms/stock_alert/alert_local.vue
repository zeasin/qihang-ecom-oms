<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="stockInNum">
        <el-input
          v-model="queryParams.stockInNum"
          placeholder="请输入单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="来源单号" prop="sourceNo">-->
<!--        <el-input-->
<!--          v-model="queryParams.sourceNo"-->
<!--          placeholder="请输入来源单号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item :label="$t('system.vendor.title')" prop="vendorId">
        <el-select v-model="queryParams.vendorId" :placeholder="$t('system.vendor.select')" clearable @change="handleQuery">
          <el-option
            v-for="item in VendorList"
            :key="item.id"
            :label="item.name"
            :value="item.originVendorId">

            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >{{item.loginName}}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" prop="stockInTime">
        <el-date-picker clearable
          v-model="queryParams.stockInTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择申请时间">
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
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
      >新建云仓商品入库申请</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['wms:WmsStockInEntry:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="WmsStockInEntryList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="单号" align="center" prop="stockInNum" width="200"/>
      <el-table-column :label="$t('system.vendor.title')" align="center" prop="vendorId" >
        <template slot-scope="scope">
        {{ VendorList.find(x=>x.originVendorId == scope.row.vendorId) ? VendorList.find(x=>x.originVendorId == scope.row.vendorId).name : '' }}
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="applyMan" />
      <el-table-column label="申请人电话" align="center" prop="applyMobile" />

      <el-table-column label="商品数" align="center" prop="goodsUnit" />
      <el-table-column label="商品规格数" align="center" prop="goodsSkuUnit" />
      <el-table-column label="总件数" align="center" prop="total" />
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="操作入库人id" align="center" prop="stockInOperatorId" />-->
      <el-table-column label="操作入库人" align="center" prop="stockInOperator" />
      <el-table-column label="最后入库时间" align="center" prop="stockInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stockInTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
         <el-tag size="small" v-if="scope.row.status === 0">待审核</el-tag>
         <el-tag size="small" v-if="scope.row.status === 1">待入库</el-tag>
         <el-tag size="small" v-if="scope.row.status === 2">已入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row,false)"
          >查看详情</el-button>

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

    <!-- 添加或修改入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
        <el-form-item label="申请单号" prop="stockInNum"  >
          <el-input v-model="form.stockInNum" disabled placeholder="请输入操作入库人" />
        </el-form-item>
        <el-form-item label="申请人" prop="stockInOperator" >
          <el-input v-model="form.applyMan" disabled placeholder="请输入操作入库人" />
        </el-form-item>
        <el-form-item label="申请人电话" prop="stockInOperator" >
          <el-input v-model="form.applyMobile" disabled placeholder="请输入操作入库人" />
        </el-form-item>
<!--        <el-form-item label="入库时间" prop="stockInTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.stockInTime"-->
<!--                          type="datetime"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="请选择入库时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

        <el-divider content-position="center">商品明细</el-divider>

        <el-table style="margin-bottom: 10px;" :data="form.itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品图片" width="80">
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格"  width="150" prop="skuName">
          </el-table-column>
          <el-table-column label="sku编码" prop="skuCode"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <el-table-column label="已入库" prop="inQuantity"></el-table-column>

        </el-table>

<!--        <el-form-item label="操作入库人id" prop="stockInOperatorId">-->
<!--          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />-->
<!--        </el-form-item>-->

      </el-form>
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
    </el-dialog>
  </div>
</template>

<script>
import { listCloudStockIn,getCloudStockIn} from "@/api/goods/vendor_stock";
import {listSupplier} from "@/api/goods/supplier";

export default {
  name: "WmsStockInEntry",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedWmsStockInEntryItem: [],
      VendorList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      isEdit: true,
      // 总条数
      total: 0,
      // 入库单表格数据
      WmsStockInEntryList: [],
      // 入库单明细表格数据
      itemList: [
      ],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockInNum: null,
        stockInType: null,
        sourceNo: null,
        sourceId: null,
        sourceType: 1,
        stockInTime: null,
      },
      // 表单参数
      form: {
        stockInId:null,
        warehouseId:null
      },
      // 仓库列表
      warehouseList:[],
      // 仓位列表
      locationList:[],
      locationLoading:false,
      // 表单校验
      rules: {

      }
    };
  },
  created() {
    listSupplier().then(resp=>{
      this.VendorList = resp.data
      this.getList();
    })

  },
  methods: {
    handleAdd(){
      this.$router.push({path:"/stock/cloud_stock_in_create"})
    },

    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listCloudStockIn(this.queryParams).then(response => {
        this.WmsStockInEntryList = response.rows;
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
        stockInOperator: null,
        stockInTime: null,
      };
      this.itemList = [];
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
    handleDetail(row){
      getCloudStockIn(row.id).then(resp=>{
        this.form = resp.data
        this.open=true
      })

    }
  }
};
</script>
