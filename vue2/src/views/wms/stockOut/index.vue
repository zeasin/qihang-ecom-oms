<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="仓库" prop="warehouseId" >
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable @change="handleQuery">
          <el-option
            v-for="item in warehouseList"
            :key="item.id"
            :label="item.warehouseName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="出库类型" prop="type">
        <el-select v-model="queryParams.type" filterable r placeholder="出库类型"  @change="handleQuery">
          <el-option  label="订单发货出库" value="1"></el-option>
          <el-option  label="采购退货出库" value="2"></el-option>
          <el-option  label="盘亏出库" value="3"></el-option>
          <el-option  label="报损出库" value="4"></el-option>
        </el-select>
      </el-form-item>

<!--      <el-form-item label="源单号" prop="sourceNo">-->
<!--        <el-input-->
<!--          v-model="queryParams.sourceNo"-->
<!--          placeholder="请输入来源单据号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="源单Id" prop="sourceId">-->
<!--        <el-input-->
<!--          v-model="queryParams.sourceId"-->
<!--          placeholder="请输入来源单据Id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->

      <el-form-item label="创建日期" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建日期">
        </el-date-picker>
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
<!--          v-hasPermi="['wms:stockOutEntry:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新建商品出库单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockOutEntryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="出库单号" align="center" prop="outNum" />
      <el-table-column label="源单号" align="center" prop="sourceNum" />
<!--      <el-table-column label="源单Id" align="center" prop="sourceId" />-->
      <el-table-column label="出库类型" align="center" prop="type" width="150">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.type === 1">订单发货出库</el-tag>
          <el-tag size="small" v-if="scope.row.type === 2">采购退货出库</el-tag>
          <el-tag size="small" v-if="scope.row.type === 3">盘亏出库</el-tag>
          <el-tag size="small" v-if="scope.row.type === 4">报损出库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="出库仓库" align="center" prop="warehouseName" width="200">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.warehouseName">{{ scope.row.warehouseName }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0">待出库</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1">部分出库</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2">全部出库</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否打印" align="center" prop="printStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.printStatus === 0">未打印</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.printStatus === 1">已打印</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="打印时间" align="center" prop="printTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.printTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="创建日期" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime)}}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="创建人" align="center" prop="createBy" />-->
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="更新人" align="center" prop="updateBy" />-->
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="出库操作人userid" align="center" prop="stockOutOperatorId" />-->
      <el-table-column label="操作人" align="center" prop="operatorName" />
<!--      <el-table-column label="出库时间" align="center" prop="stockOutTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.stockOutTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="是否删除0未删除1已删除" align="center" prop="isDelete" />-->
      <el-table-column label="商品数" align="center" prop="goodsUnit" />
      <el-table-column label="商品规格数" align="center" prop="specUnit" />
      <el-table-column label="总件数" align="center" prop="specUnitTotal" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status !== 2"
            size="mini"
            type="primary"
            plain
            icon="el-icon-d-arrow-right"
            @click="handleStockOut(scope.row)"
            style="padding-left: 10px;padding-right: 10px"
          >出库</el-button>
          <el-button
            v-if="scope.row.status === 2"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleStockOut(scope.row)"
          >出库详情</el-button>
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

    <!-- 添加或修改出库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-descriptions title="出库单详情">
          <el-descriptions-item label="单号">{{form.stockOutNum}}</el-descriptions-item>
          <el-descriptions-item label="来源">
            <el-tag size="small" v-if="form.type === 1">订单发货出库</el-tag>
            <el-tag size="small" v-if="form.type === 2">采购退货出库</el-tag>
            <el-tag size="small" v-if="form.type === 3">盘点出库</el-tag>
            <el-tag size="small" v-if="form.type === 4">报损出库</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注">{{form.remark}}</el-descriptions-item>
          <el-descriptions-item label="商品数">{{form.goodsUnit}}</el-descriptions-item>
          <el-descriptions-item label="规格数">{{form.specUnit}}</el-descriptions-item>
          <el-descriptions-item label="总件数">{{form.specUnitTotal}}</el-descriptions-item>
<!--          <el-descriptions-item label="店铺">-->
<!--            <span v-if="form.shopId==6">梦小妮牛仔裤</span>-->
<!--          </el-descriptions-item>-->
        </el-descriptions>


        <el-divider content-position="center">出库商品明细</el-divider>
        <el-table :data="form.itemList" :row-class-name="rowWmsStockOutEntryItemIndex" ref="wmsStockOutEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
<!--          <el-table-column label="商品图片" prop="goodsImage" >-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImage"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="商品名" prop="goodsName"></el-table-column>
          <el-table-column label="规格编码" prop="skuCode"></el-table-column>
          <el-table-column label="规格"  prop="skuName"></el-table-column>
          <el-table-column label="数量" prop="originalQuantity" width="80"></el-table-column>
          <el-table-column label="已出库" prop="outQuantity" width="100"></el-table-column>
<!--          <el-table-column label="出库仓位" prop="positionNum"></el-table-column>-->

<!--          <el-table-column label="出库仓位" prop="inventoryId" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-select v-model="scope.row.inventoryDetailId" placeholder="请选择出库仓位" v-if="scope.row.status < 2">-->
<!--                <el-option v-for="item in scope.row.inventoryDetails" :key="item.id" :label="item.locationNum" :value="item.id">-->
<!--                  <span style="float: left">{{ item.locationNum }}</span>-->
<!--                  <span style="float: right; color: #8492a6; font-size: 13px"  >剩余库存：{{ item.currentQty }}</span>-->

<!--                </el-option>-->

<!--              </el-select>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="出库数量" prop="outQty" width="110">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.originalQuantity - scope.row.outQuantity" disabled placeholder="出库数量"  v-if="scope.row.status < 2" />
            </template>
          </el-table-column>
          <el-table-column label="出库操作" prop="outQuantity" width="100"  >
          <template slot-scope="scope">
          <el-button style="padding-left: 10px;padding-right: 10px"
            v-if="scope.row.status < 2"
            size="mini"
            plain
            type="danger"
            @click="stockOutSubmit(scope.row)"
            icon="el-icon-d-arrow-right"
          >出库</el-button>
        </template>
          </el-table-column>
        </el-table>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
import { listStockOut, getStockOutEntry, stockOut } from "@/api/wms/stockOut";
import {listShop} from "@/api/shop/shop";
import {listGroup} from "@/api/shop/group";
import { listWarehouse } from '@/api/wms/warehouse'
import {getUserProfile} from "@/api/system/user";
import {listMyLocalWarehouse} from "@/api/store/warehouse";
export default {
  name: "StockOutEntry",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedWmsStockOutEntryItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 出库单表格数据
      stockOutEntryList: [],
      shopList: [],
      shopGroupList: [],
      warehouseList: [],
      // 出库单明细表格数据
      wmsStockOutEntryItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outNum: null,
        sourceNum: null,
        sourceId: null,
        warehouseId: null,
        type: null,
        status: null,

      },
      // 表单参数
      form: {
      },
      // 表单校验
      rules: {
        inventoryId:[{ required: true, message: '请填写收货信息' }],
      }
    };
  },
  created() {

    getUserProfile().then(res=> {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        listWarehouse({pageSize: 50, warehouseType: 'LOCAL'}).then(response => {
          this.warehouseList = response.rows;
          if (this.warehouseList && this.warehouseList.length > 0) {
            this.queryParams.warehouseId = this.warehouseList[0].id;
          }
          this.getList()
        });
      } else {
        listMyLocalWarehouse().then(response => {
          this.warehouseList = response.data;
          if (this.warehouseList && this.warehouseList.length > 0) {
            this.queryParams.warehouseId = this.warehouseList[0].id;
          }
          this.getList()
        })
      }

    })
    // listWarehouse({pageSize:100,warehouseType:'LOCAL'}).then(response => {
    //   this.warehouseList = response.rows;
    //   this.getList();
    // });
  },
  methods: {
    handleAdd(){
      this.$router.push({path:"stock_out/create"})
    },
    /** 查询出库单列表 */
    getList() {
      this.loading = true;
      listStockOut(this.queryParams).then(response => {
        this.stockOutEntryList = response.rows;
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
        stockOutNum: null,
        sourceNo: null,
        sourceId: null,
        stockOutType: null,
        status: null,
        printStatus: null,
        printTime: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        completeTime: null,
        stockOutOperatorId: null,
        stockOutOperatorName: null,
        stockOutTime: null,
        remark: null,
        isDelete: null,
        goodsUnit: null,
        specUnit: null,
        specUnitTotal: null
      };
      this.wmsStockOutEntryItemList = [];
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
    /** 修改按钮操作 */
    handleStockOut(row) {
      // this.$router.push({path:"stock_out",query:{id: row.id}})
      this.reset();
      const id = row.id || this.ids
      getStockOutEntry(id).then(response => {
        this.form = response.data;
        this.wmsStockOutEntryItemList = response.data.wmsStockOutEntryItemList;
        // this.wmsStockOutEntryItemList.forEach(x=>{
        //   x.inventoryId = null;
        //   x.outQty = null
        // })
        this.open = true;
        this.title = "出库操作";
      });
    },
    /** 提交按钮 */
    stockOutSubmit(row) {
      console.log("=====提交出库操作====",row)
      if(!row.outQty && row.outQty <= 0){
        this.$modal.msgError("请填写要出库的库存");
        return
      }
      // if(!row.inventoryDetailId){
      //   this.$modal.msgError("请选择库存仓位");
      //   return
      // }else{
      //   // 判断填写的数量是否小于等于当前仓位库存
      //   // if(row.inventoryId < row.outQty){
      //   //   this.$modal.msgError("仓位库存不足！");
      //   //   return
      //   // }
      //   // 判断输入的数量要小于等于需要出库的数量
      //   if(row.outQty > (row.originalQuantity - row.outQuantity)){
      //     this.$modal.msgError("出库数量不对！");
      //     return
      //   }
      // }
      this.$refs["form"].validate(valid => {
        if (valid) {
          const subForm ={
            entryItemId:row.id,
            entryId:row.entryId,
            skuId:row.skuId,
            positionId:row.positionId,
            originalQuantity:row.originalQuantity,
            outQuantity:row.outQuantity,
            outQty:row.originalQuantity - row.outQuantity
          }
          stockOut(subForm).then(response => {
              this.$modal.msgSuccess("出库成功");
              // this.open = false;
            this.getList()
              getStockOutEntry(row.id).then(response => {
                this.form = response.data;
                this.wmsStockOutEntryItemList = response.data.wmsStockOutEntryItemList;
              });
            });

        }
      });
    },
	/** 出库单明细序号 */
    rowWmsStockOutEntryItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/stockOutEntry/export', {
        ...this.queryParams
      }, `stockOutEntry_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
