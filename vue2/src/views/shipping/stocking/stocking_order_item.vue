<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">

      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品编码" prop="goodsNum">
        <el-input
          v-model="queryParams.goodsNum"
          placeholder="请输入商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="规格ID" prop="specId">-->
<!--        <el-input-->
<!--          v-model="queryParams.specId"-->
<!--          placeholder="请输入erp系统商品规格id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入商品SKU编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备货状态" prop="stockingStatus">
        <el-select v-model="queryParams.stockingStatus" placeholder="请选择" @change="handleQuery">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-document-copy"
          size="mini"
          :disabled="multiple"
          @click="handleSelection"
        >生成出库单</el-button>
      </el-col>
      <el-col :span="1.5">
      <el-button
        type="primary"
        plain
        icon="el-icon-data-analysis"
        size="mini"
        :disabled="multiple"
        @click="handleStatistics"
      >统计</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shippingList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" v-if="queryParams.status==='0'" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <el-table-column label="ItemId" align="center" prop="id" width="65"/>
      <el-table-column label="订单编号" align="center" prop="orderNum" width="200"/>

      <!-- <el-table-column label="子订单编号" align="center" prop="orderItemId" /> -->
<!--      <el-table-column label="订单日期" align="center" prop="orderDate" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品图片" >
        <template slot-scope="scope">
          <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
<!--              <el-image  style="width: 30px; height: 30px;" :src="scope.row.goodsImg"></el-image>-->
        </template>
      </el-table-column>
      <el-table-column label="商品标题" align="left" prop="goodsName" width="300"/>
      <el-table-column label="规格" align="center" prop="skuName" />
      <el-table-column label="sku编码" align="center" prop="skuCode" />
      <el-table-column label="商品id" align="center" prop="goodsId" />
      <el-table-column label="商品SkuId" align="center" prop="goodsSkuId" />

       <el-table-column label="数量" align="center" prop="quantity" >
         <template slot-scope="scope">
         <el-tag size="small">{{scope.row.quantity}}</el-tag>
         </template>
       </el-table-column>
<!--      <el-table-column label="仓库库存" align="center" prop="inventory" />-->
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.stockingStatus === 0">待备货</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 1">已备货</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 2">备货完成</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 3">已发货</el-tag>
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

    <!-- 添加或修改仓库订单发货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
<!--        <el-form-item label="单号" prop="stockOutNum" v-if="isGen">-->
<!--          <el-input v-model="form.stockOutNum" disabled placeholder="请输入单号" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="生成时间" prop="completeTime" v-if="isGen">-->
<!--          <el-date-picker clearable-->
<!--            v-model="form.completeTime"-->
<!--            type="datetime" disabled-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            placeholder="请选择时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
        <el-divider content-position="center" v-if="isGen">备货商品</el-divider>
        <el-table :data="skuList" :row-class-name="rowItemIndex" ref="skuItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="ItemId" align="center" prop="id" width="65"/>
<!--          <el-table-column label="商品图片" prop="goodsImg" >-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格" prop="skuName" ></el-table-column>
          <el-table-column label="sku编码" prop="skuCode" ></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
<!--          <el-table-column label="仓库库存" prop="inventory"></el-table-column>-->

        </el-table>
        <el-row style="padding-top: 20px"></el-row>
        <el-form-item label="仓库" prop="warehouseId" v-if="isGen">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" clearable @change="handleQuery">
            <el-option
              v-for="item in warehouseList"
              :key="item.id"
              :label="item.warehouseName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="isGen">
        <el-button type="primary" @click="submitForm">生成出库单</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listShipStockupItem,generateStockOutByItem} from "@/api/shipping/stocking";
import { listShop } from "@/api/shop/shop";
import { listWarehouse } from '@/api/wms/warehouse'
export default {
  name: "ShipStockupOrderItem",
  // computed: {
  //   supplier() {
  //     return supplier
  //   }
  // },
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
      // 仓库订单发货表格数据
      shippingList: [],
      warehouseList: [],
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      skuIdUpdateOpen: false,
      isGen:true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        orderNum: null,
        goodsNum: null,
        skuCode: null,
        stockingStatus: null
      },
      // 表单参数
      form: {
        ids:[],
        completeTime:null
      },
      form1: {
        orderItemIds:[]
      },
      form2: {
        orderItemId:null
      },

      skuList:[],

      statusList: [
        {
          value: '0',
          label: '待备货'
        }, {
          value: '1',
          label: '已备货'
        }
      ],
      // 表单校验
      rules: {
        stockOutNum: [{ required: true, message: "单号不能为空", trigger: "blur" }],
        warehouseId: [{ required: true, message: "请选择仓库", trigger: "blur" }],
        completeTime: [{ required: true, message: "生成时间不能为空", trigger: "blur" }],
        goodsId: [{ required: true, message: "erp系统商品id不能为空", trigger: "blur" }],
        specId: [{ required: true, message: "erp系统商品规格id不能为空", trigger: "blur" }],
        quantity: [{ required: true, message: "商品数量不能为空", trigger: "blur" }],
      },
      rules1: {
        supplierId: [{ required: true, message: "请选择供应商", trigger: "blur" }],
      },
      rules2: {
        erpGoodsSpecId: [{ required: true, message: "请选择填写ERP商品SkuId", trigger: "blur" }],
      }
    };
  },
  mounted() {

  },
  created() {
    if(this.$route.query.status){
      this.queryParams.status = this.$route.query.status
    }else {
      this.queryParams.status = '0'
    }
    // listShop().then(resp=>{
    //   this.shopList = resp.rows
    //   this.getList();
    // })
    listWarehouse({pageSize:100,warehouseType:'LOCAL'}).then(response => {
      this.warehouseList = response.rows;
    });
    this.getList();

  },
  methods: {
    rowItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 查询仓库订单发货列表 */
    getList() {
      this.loading = true;
      listShipStockupItem(this.queryParams).then(response => {
        this.shippingList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.skuList = []
      this.open = false;
      this.skuIdUpdateOpen = false;
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
    /** 提交按钮 */
    submitForm() {
      console.log("=============备货完成提交===",this.ids)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(!this.skuList || this.skuList.length === 0){
            this.$modal.msgError("请选择备货商品");
          }
          this.form.ids = this.ids;
          generateStockOutByItem(this.form).then(response => {
            this.$modal.msgSuccess("生成出库单成功");
            this.open = false;
            this.getList();
          });

        }
      });
    },
    handleStatistics(row){
      this.handleSelection(row,false)
    },
    /** 按钮操作 */
    handleSelection(row,isGen) {
      const ids = row.id || this.ids;
      console.log("=====生成出库单=====",ids)
      if(!ids && ids.length===0){
        this.$modal.msgError("请选选择要备货的商品");
        return
      }
      if(isGen===undefined) this.isGen = true
      else this.isGen = isGen
      if(this.isGen === false)this.title = "出库统计";
      else this.title = "生成出库单";

      // 创建一个包含年月日小时分钟秒的字符串作为基本编号
      var date = new Date();
      var year = date.getFullYear().toString(); // 四位数表示的年份
      var month = (date.getMonth() + 1).toString().padStart(2, '0'); // 两位数表示的月份（注意要加上补零）
      var day = date.getDate().toString().padStart(2, '0'); // 两位数表示的天数（同样需要补零）
      var hours = date.getHours().toString().padStart(2, '0'); // 两位数表示的小时数（同样需要补零）
      var minutes = date.getMinutes().toString().padStart(2, '0'); // 两位数表示的分钟数（同样需要补零）
      var seconds = date.getSeconds().toString().padStart(2, '0'); // 两位数表示的秒数（同样需要补零）
      // 生成随机数部分
      var randomNum = Math.floor((Math.random() * 9) + 1); // 生成1到9之间的随机整数
      // 将所有部分组合起来形成最终的编号
      var code = `${year}${month}${day}${hours}${minutes}${seconds}${randomNum}`;
      // console.log("生成的编号为：" + code);

      this.form.stockOutNum = code
      this.form.completeTime = new Date()
      this.skuList=[]

      ids.forEach(id=>{
        const obj = this.shippingList.find(y=>y.id === id)
        const has = this.skuList.find(y=>y.goodsSkuId === obj.goodsSkuId)
        if(has){
          // 增加数量即可
          has.quantity = has.quantity + obj.quantity
          has.ids.push(id)
        }else{
          // 新增数据
          const ids1 =[]
          ids1.push(id);
          this.skuList.push({
            ids:ids1,
            id:obj.id,
            goodsSkuId:obj.goodsSkuId,
            skuId:obj.skuId,
            goodsImg:obj.goodsImg,
            goodsNum:obj.goodsNum,
            goodsName:obj.goodsName,
            skuName:obj.skuName,
            skuCode:obj.skuCode,
            quantity:obj.quantity
          })
        }
      })
      this.open = true;
      // this.$modal.confirm('是否确认删除仓库订单发货编号为"' + ids + '"的数据项？').then(function() {
      //   return delShipping(ids);
      // }).then(() => {
      //   this.getList();
      //   this.$modal.msgSuccess("删除成功");
      // }).catch(() => {});

    },
  }
};
</script>
