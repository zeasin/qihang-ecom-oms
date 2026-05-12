<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">

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
      <el-form-item label="创建日期" prop="stockTakeDate">
        <el-date-picker clearable
                        v-model="queryParams.stockTakeDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建日期">
        </el-date-picker>
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
        >新建盘点</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--      <el-button-->
<!--        type="primary"-->
<!--        plain-->
<!--        icon="el-icon-plus"-->
<!--        size="mini"-->
<!--        @click="handleAdd"-->
<!--      >新建入库单</el-button>-->
<!--      </el-col>-->
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
<!--      <el-table-column label="日期" align="center" prop="stockTakeDate" width="100"/>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品SKU" align="center" prop="skuUnit" />
      <el-table-column label="总件数" align="center" prop="totalQuantity" />
      <el-table-column label="结果总数" align="center" prop="resultQuantity" />
      <el-table-column label="首次盘点时间" align="center" prop="firstTakeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstTakeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作入库人id" align="center" prop="stockInOperatorId" />-->
      <el-table-column label="创建人" align="center" prop="createBy" />

      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
         <el-tag size="small" v-if="scope.row.status === 0">已创建</el-tag>
         <el-tag size="small" v-if="scope.row.status === 1">盘点中</el-tag>
         <el-tag size="small" v-if="scope.row.status === 2">盘点完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-right: 6px;padding-left: 6px;"
            v-if="scope.row.status === 0 || scope.row.status === 1"
            size="mini" plain
            type="primary"
            icon="el-icon-edit"
            @click="handleStockTake(scope.row)"
          >盘点</el-button>
          <el-button style="padding-right: 6px;padding-left: 6px;"
            v-if="scope.row.status ===1 "
            size="mini" plain
            type="success"
            icon="el-icon-ok"
            @click="handleStockTakeComplete(scope.row)"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row,false)"
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

    <!-- 添加或修改店铺对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="盘点商户" prop="merchantId">
          <el-select v-model="form.merchantId" placeholder="请选择商户">
            <el-option label="总部" value="0"></el-option>
            <el-option
              v-for="item in merchantList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 盘点 -->
    <el-dialog :title="title" :visible.sync="takeOpen" width="1300px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-form-item label="">
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAddSShopOrderItem">添加</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-table style="margin-bottom: 20px;" :data="form.itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="sShopOrderItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <!-- <el-table-column label="erp系统商品id" prop="goodsId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.goodsId" placeholder="请输入erp系统商品id" />
            </template>
          </el-table-column>
          <el-table-column label="erp系统商品规格id" prop="specId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.specId" placeholder="请输入erp系统商品规格id" />
            </template>
          </el-table-column> -->
          <el-table-column label="图片" prop="goodsImg" width="80">
            <template slot-scope="scope">
              <el-image style="width: 60px; height: 60px" :src="scope.row.imageUrl"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品" prop="goodsName" width="350"></el-table-column>
          <el-table-column label="规格" prop="skuName" width="150"></el-table-column>
          <el-table-column label="编码" prop="goodsNo" width="180"></el-table-column>
          <el-table-column label="库存数量" prop="quantity" width="100"></el-table-column>
          <el-table-column label="盘点数量" prop="takeQuantity" width="150">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.takeQuantity" placeholder="请输入盘点数量" />
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="" width="80">
            <template slot-scope="scope">
<!--              <el-button size="mini" plain type="danger" @click="handleDeleteSku(scope.$index, scope.row)" v-if="!scope.row.isOld">删除</el-button>-->
              <el-button size="mini" plain type="danger" @click="handleDeleteSku(scope.$index, scope.row)" >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!--        <el-form-item label="操作入库人id" prop="stockInOperatorId">-->
        <!--          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />-->
        <!--        </el-form-item>-->

      </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitStockTakeForm">保存盘点结果</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
    </el-dialog>

    <!-- 详情 -->
    <el-dialog :title="title" :visible.sync="detailOpen" width="1400px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="图片" width="80">
            <template slot-scope="scope">
              <el-image style="width: 45px; height: 45px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格"  width="150" prop="skuName">
          </el-table-column>
          <el-table-column label="编码" prop="goodsNo"></el-table-column>
          <el-table-column label="盘点前数量" prop="quantity"></el-table-column>
          <el-table-column label="盘点后数量" prop="takeQuantity"></el-table-column>
          <el-table-column label="盘点时间" prop="createTime">
            <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
          </el-table-column>
          <el-table-column label="更新时间" prop="updateTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>
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

    <PopupWarehouseGoodsItems @data-from-select="handleDataFromPopup" :btn="2" ref="popup"></PopupWarehouseGoodsItems>

  </div>
</template>

<script>

import {listMerchant} from "@/api/vms/merchant";

import {
  getStockTakeEntry,
  listStockTake,
  stockTakeCreate,
  stockTakeComplete,
  stockTakeItem,
  saveTakeItemGoods, delTakeItemGoods, saveTake
} from "@/api/vms/wms/stockTake";
import {searchSkuAndStock} from "@/api/vms/goods/goods";
import PopupWarehouseGoodsItems from "@/views/vms/goods/PopupWarehouseGoodsItems.vue";
import {getGoodsStockQty} from "@/api/vms/wms/goods_inventory";

export default {
  name: "WmsStockInEntry",
  components: {PopupWarehouseGoodsItems},
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
      skuList: [],
      // 入库单明细表格数据
      itemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      takeOpen: false,
      skuListLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockInNum: null,
        stockInType: null,
        sourceNo: null,
        sourceId: null,
        stockTakeDate: null,
      },
      // 表单参数
      form: {
        merchantId:null,
        remark:null,
        itemList:[]
      },
      // 仓库列表
      warehouseList:[],
      // 仓位列表
      locationList:[],
      locationLoading:false,
      // 表单校验
      rules: {
        merchantId: [{ required: true, message: "不能为空", trigger: "change" }],
        remark: [{ required: true, message: "不能为空", trigger: "change" }],
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
      this.open = true
    },
    // handleAdd(){
    //   this.$router.push({path:"/stock_in/create"})
    // },
    handleAddSShopOrderItem() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if(data) {
        data.forEach((item) => {
          const find = this.form.itemList.find(x => x.id === item.id)
          if (!find) {
            // 添加盘点
            saveTakeItemGoods({goodsId:item.id,id:this.form.id}).then(resp=>{
              if(resp.code===200){
                this.form.itemList.push(resp.data)
                this.getList()
              }else{
                this.$modal.msgError(resp.msg)
              }
            })
          }else{
            this.$modal.msgError("已经添加过了")
          }
        })
      }
      console.log('====添加后=====',this.form.itemList)
    },
    handleDeleteSku(index, row) {
      console.log("==========删除商品：",row)
      delTakeItemGoods({id:row.id}).then(resp=>{
        if(resp.code===200){
          this.form.itemList.splice(index, 1);
          this.getList()
        }else{
          this.$modal.msgError(resp.msg)
        }
      })

    },

    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listStockTake(this.queryParams).then(response => {
        this.WmsStockInEntryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detailOpen = false;
      this.takeOpen = false;
      this.reset();
      this.getList()
    },
    // 表单重置
    reset() {
      this.form = {
        merchantId:null,
        remark:null,
        itemList:[]
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
    handleStockTakeComplete(row){
      this.$modal.confirm('确认完成之后就不能再盘点了！并且会自动生成盘盈盘亏出入库！您确定吗？').then(function() {
        return stockTakeComplete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("盘点完成");
      }).catch(() => {});
    },
    /** 详情按钮操作 */
    handleDetail(row,isEdit) {
      this.reset();
      const id = row.id || this.ids
      this.isEdit = isEdit
      getStockTakeEntry(id).then(response => {
        // this.form = response.data;
        this.form.stockInId = response.data.id
        this.itemList = response.data.itemList;
        this.detailOpen = true;
        this.title = "盘点详情";
      });
    },
    handleStockTake(row) {
      this.reset();
      // this.takeOpen = true;
      this.form.merchantId = row.merchantId
      this.form.id = row.id
      this.form.itemList = []
      getStockTakeEntry(this.form.id).then(response => {
        if(response.data.itemList && response.data.itemList.length>0){
          // this.itemList = response.data.itemList
          this.form.itemList = response.data.itemList
          // response.data.itemList.forEach(spec => {
          //   if (spec) {
          //     console.log('=======111112222222222222==', spec)
          //     const  sku ={
          //       quantity : spec.takeQuantity,
          //       id : spec.goodsId,
          //       goodsName : spec.goodsName,
          //       standard : spec.skuName,
          //       isOld:true,
          //       goodsNo : spec.goodsNo,
          //       erpGoodsNo : spec.erpGoodsNo,
          //       imageUrl : spec.goodsImage,
          //       stock : spec.quantity
          //     }
          //
          //     this.form.itemList.push(sku)
          //   }
          // })

        }
        // this.form.itemList = response.data.itemList;
        this.takeOpen = true;
        this.title = "录入盘点结果";
      });
      // this.title = "录入盘点结果";
    },
    submitStockTakeForm() {
      console.log("======盘点数据保存=====", this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.form.itemList || this.form.itemList.length === 0) {
            this.$modal.msgError("请录入数据")
            return
          }
          // 使用 some() 方法判断是否有 quantity 为 null 的元素
          const hasNullQuantity = this.form.itemList.some(item => item.quantity === null);

          console.log(hasNullQuantity); // 如果有 quantity 为 null 的元素，会输出 true，否则 false
          if (hasNullQuantity) {
            this.$modal.msgError("请录入完整的数据")
            return
          }

          saveTake(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("保存成功");
              this.takeOpen = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg)
            }
          });
        }
      });
    },
	/** 入库单明细序号 */
    rowWmsStockInEntryItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          stockTakeCreate(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.reset()
            this.getList();
          });
        }
      });
    },
  }
};
</script>
