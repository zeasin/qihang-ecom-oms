<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="stockInNum">
        <el-input
          v-model="queryParams.stockInNum"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="入库来源" prop="sourceType">-->
<!--        <el-select v-model="form.sourceType" filterable r placeholder="入库来源" >-->
<!--          <el-option  label="自己入库" value="0"></el-option>-->
<!--          <el-option  label="商户入库" value="1"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable @change="handleQuery">
          <el-option  label="总部" value="0"></el-option>
          <el-option
            v-for="item in merchantList"
            :key="item.merchantId"
            :label="item.name"
            :value="item.merchantId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" filterable r placeholder="状态" @change="handleQuery">
          <el-option  label="待审核" value="0"></el-option>
          <el-option  label="已审核" value="1"></el-option>
          <el-option  label="已入库" value="2"></el-option>
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

    <el-table v-loading="loading" :data="WmsStockInEntryList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="商户" align="left" prop="merchantId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.merchantId==0" type="primary" size="small">总部</el-tag>
          <el-tag v-else>{{ merchantList.find(x=>x.id === scope.row.merchantId) ? merchantList.find(x=>x.id === scope.row.merchantId).name : '' }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="主键ID" align="center" prop="id" />-->
      <el-table-column label="单号" align="center" prop="stockInNum" width="200"/>
      <el-table-column label="入库类型" align="center" prop="stockInType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.stockInType ===1 ">采购入库</el-tag>
          <el-tag size="small" v-if="scope.row.stockInType ===2 ">退货入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品数" align="center" prop="sourceGoodsUnit" />
      <el-table-column label="商品规格数" align="center" prop="sourceSpecUnit" />
      <el-table-column label="总件数" align="center" prop="sourceSpecUnitTotal" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作入库人" align="center" prop="stockInOperator" />
      <el-table-column label="最后入库时间" align="center" prop="stockInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stockInTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
         <el-tag size="small" v-if="scope.row.status === 0">待审核</el-tag>
         <el-tag size="small" v-if="scope.row.status === 1">已审核</el-tag>
         <el-tag size="small" v-if="scope.row.status === 2">已入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleStockIn(scope.row,true)"
          >审核入库</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-divider content-position="center">入库商品</el-divider>

        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
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
        </el-table>

<!--        <el-form-item label="操作入库人id" prop="stockInOperatorId">-->
<!--          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />-->
<!--        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMerchantStockIn, getMerchantStockInEntry, merchantStockInAudit } from '@/api/vms/wms/stockIn'
import {listMerchant} from "@/api/vms/merchant";

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
      merchantList: [],
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
        warehouseId: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        stockInOperator: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
      this.getList();
    })
  },
  methods: {

    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listMerchantStockIn(this.queryParams).then(response => {
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

    /** 入库按钮操作 */
    handleStockIn(row,isEdit) {
      this.reset();
      const id = row.id || this.ids
      this.isEdit = isEdit

      getMerchantStockInEntry(id).then(response => {
        this.form.stockInId = response.data.id
        this.itemList = response.data.itemList;
        this.open = true;
        this.title = "商户入库审核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.itemList = this.itemList;
            console.log('=======验证通过了========',this.form)
          merchantStockInAudit(this.form).then(response => {
              this.$modal.msgSuccess("商户入库审核成功");
              this.open = false;
              this.getList();
            });

        }
      });
    },
	/** 入库单明细序号 */
    rowWmsStockInEntryItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
  }
};
</script>
