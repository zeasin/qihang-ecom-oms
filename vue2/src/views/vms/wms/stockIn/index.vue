<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="入库单号" prop="stockInNum">
        <el-input
          v-model="queryParams.stockInNum"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源单号" prop="sourceNo">
        <el-input
          v-model="queryParams.sourceNo"
          placeholder="请输入来源单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="入库来源" prop="sourceType">
        <el-select v-model="form.sourceType" filterable r placeholder="入库来源" >
          <el-option  label="自己入库" value="0"></el-option>
          <el-option  label="商户入库" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入库类型" prop="stockInType">
        <el-select v-model="form.stockInType" filterable r placeholder="入库类型" >
          <el-option  label="采购入库" value="1"></el-option>
          <el-option  label="退货入库" value="2"></el-option>
          <el-option  label="盘盈入库" value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable @change="handleQuery">
          <el-option label="总部" value="0"></el-option>
          <el-option
            v-for="item in merchantList"
            :key="item.merchantId"
            :label="item.name"
            :value="item.merchantId">
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="入库时间" prop="stockInTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.stockInTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择入库时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
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
      >新建入库单</el-button>
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
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="商户" align="left" prop="merchantId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.merchantId === 0">总部</el-tag>
          <el-tag v-else>{{ merchantList.find(x=>x.id === scope.row.merchantId) ? merchantList.find(x=>x.id === scope.row.merchantId).name : '' }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="主键ID" align="center" prop="id" />-->
      <el-table-column label="单号" align="center" prop="stockInNum" width="200"/>
      <el-table-column label="入库类型" align="center" prop="stockInType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.stockInType ===1 ">采购入库</el-tag>
          <el-tag size="small" v-if="scope.row.stockInType ===2 ">退货入库</el-tag>
          <el-tag size="small" v-if="scope.row.stockInType ===3 ">盘盈入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="入库来源" align="center" prop="stockInType" >
        <template slot-scope="scope">
        <el-tag size="small" v-if="scope.row.sourceType ===2 ">盘点入库</el-tag>
        <el-tag size="small" v-if="scope.row.sourceType ===1 ">商户入库</el-tag>
        <el-tag size="small" v-if="scope.row.sourceType ===0 ">自己入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="来源单号" align="center" prop="sourceNo" />
      <el-table-column label="商品" align="center" prop="goodsUnit" />
      <el-table-column label="商品SKU" align="center" prop="goodsSkuUnit" />
      <el-table-column label="总件数" align="center" prop="total" />

<!--      <el-table-column label="操作入库人id" align="center" prop="stockInOperatorId" />-->
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
         <el-tag size="small" v-if="scope.row.status === 0">待入库</el-tag>
         <el-tag size="small" v-if="scope.row.status === 1">入库中</el-tag>
         <el-tag size="small" v-if="scope.row.status === 2">已入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-left: 8px; padding-right: 8px;"
            v-if="scope.row.status === 0 || scope.row.status === 1"
            size="mini" plain
            type="primary"
            icon="el-icon-edit"
            @click="handleStockIn(scope.row,true)"
          >入库</el-button>
          <el-button
            v-if="scope.row.status ===2 "
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleStockIn(scope.row,false)"
          >入库详情</el-button>

          <el-button
            v-if="scope.row.status ===1 "
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleStockInComplete(scope.row)"
            v-hasPermi="['api:goodsInventory:remove']"
          >入库完成</el-button>
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
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
<!--        <el-form-item label="入库仓库" prop="warehouseId"  v-if="isEdit">-->
<!--          <el-select v-model="form.warehouseId" filterable r placeholder="入库类型" >-->
<!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="入库人" prop="stockInOperator" v-if="isEdit">-->
<!--          <el-input v-model="form.stockInOperator" placeholder="请输入操作入库人" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="入库时间" prop="stockInTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.stockInTime"-->
<!--                          type="datetime"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="请选择入库时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

        <el-divider content-position="center">入库明细</el-divider>

        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品图片" width="80">
            <template slot-scope="scope">
              <el-image style="width: 45px; height: 45px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格"  width="150" prop="skuName">
          </el-table-column>
          <el-table-column label="sku编码" prop="goodsNo"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <el-table-column label="已入库" prop="inQuantity"></el-table-column>
          <el-table-column label="入库数量" prop="intoQuantity" width="110"  v-if="isEdit">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.intoQuantity" placeholder="入库数量" v-if="scope.row.status === 0 "/>
            </template>
          </el-table-column>
          <el-table-column label="入库仓位编码" prop="positionNum" width="150"  v-if="!isEdit" >
            <template slot-scope="scope">
              {{scope.row.positionNum}}
            </template>
          </el-table-column>
          <el-table-column label="入库仓位" prop="positionId" width="150"  v-if="isEdit">
            <template slot-scope="scope">
              <el-select v-model="scope.row.positionId" filterable remote reserve-keyword placeholder="搜索仓位编码"  v-if="scope.row.status === 0 "
                         :remote-method="searchLocation" :loading="locationLoading" @change="locationChanage(scope.row)">
                <el-option v-for="item in locationList" :key="item.id"
                           :label="item.name"
                           :value="item.id">
                  <span style="float: left">{{ item.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.number }}</span>
                </el-option>
              </el-select>
<!--              <el-input v-model="scope.row.locationNum" placeholder="请输入入库仓位编码" />-->
            </template>
          </el-table-column>
          <el-table-column label="入库" prop=""  v-if="isEdit">
            <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0 " type="primary" @click="submitItemForm(scope.row)">确认入库</el-button>
            </template>
          </el-table-column>
<!--          <el-table-column label="总入库数量" prop="totalQuantity"></el-table-column>-->
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
import { listStockIn, getWmsStockInEntry, stockIn ,complete} from "@/api/vms/wms/stockIn";
import { listWarehouse,searchPosition } from "@/api/vms/wms/warehouse";
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
      itemList: [],
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
    handleAdd(){
      this.$router.push({path:"/stock_in/create"})
    },
    searchLocation(query){
        this.locationLoading = true;
        const qw = {
          number: query
        }
        searchPosition(qw).then(res => {
          this.locationList = res.rows;
          this.locationLoading = false;
        })
    },
    locationChanage(row){
      console.log(row)
      const selection = this.locationList.find(x => x.id === row.positionId);
      if (selection) {
        row.positionNum = selection.number
      }
    },
    // qtyChange(row) {
    //   console.log('======值变化=====', row)
    //   if(row.intoQuantity){
    //     row.totalQuantity = parseInt(row.inQuantity) + parseInt(row.intoQuantity)
    //   }else {
    //     row.totalQuantity = row.inQuantity
    //   }
    //
    // },
    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listStockIn(this.queryParams).then(response => {
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
    handleStockInComplete(row){
      this.$modal.confirm('确认完成之后就不能再入库了！您确定吗？').then(function() {
        return complete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("状态修改成功");
      }).catch(() => {});
    },
    /** 入库按钮操作 */
    handleStockIn(row,isEdit) {
      this.reset();
      const id = row.id || this.ids
      this.isEdit = isEdit
      getWmsStockInEntry(id).then(response => {
        // this.form = response.data;
        this.form.stockInId = response.data.id
        this.itemList = response.data.itemList;
        this.itemList.forEach(x=>{
          x.intoQuantity = x.quantity - x.inQuantity
        })
        this.open = true;
        this.title = "入库操作";
      });
    },
    submitItemForm(row) {
      console.log("======入库操作=====",row)
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.itemList = []
          this.form.itemList.push(row)
          // if(!row.positionId){
          //   this.$modal.msgError("请填写入库仓位");
          // }
          // else{
              stockIn(this.form).then(response => {
                this.$modal.msgSuccess("入库操作成功");
                this.open = false;
                this.getList();
              });
          // }

          // if(isValid){
          //   console.log('=======验证通过了========',this.form)
          //   stockIn(this.form).then(response => {
          //     this.$modal.msgSuccess("入库操作成功");
          //     this.open = false;
          //     this.getList();
          //   });
          // }else{
          //   this.$modal.msgError("请填写入库数量和仓位编码");
          // }
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
