<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="仓库" prop="warehouseId">
      <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" @change="handleQuery">
        <el-option
          v-for="item in warehouseList"
          :key="item.id"
          :label="item.warehouseName"
          :value="item.id">
          <span>{{item.warehouseName}}</span>
          <span style="float: right">
           <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='CLOUD'">系统云仓</span>
            <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC'">京东云仓</span>
<!--            <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC' && item.jdlApiType==0">京东云仓-仓配一体</span>-->
<!--            <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC' && item.jdlApiType==1">京东云仓-ERP</span>-->
          <el-tag type="info" v-else>未知仓库</el-tag>
            </span>
        </el-option>
      </el-select>
      </el-form-item>

<!--      <el-form-item label="商品编码" prop="goodsNo">-->
<!--        <el-input-->
<!--          v-model="queryParams.goodsNo"-->
<!--          placeholder="请输入商品编码"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="云仓商品编码" prop="goodsNo">
        <el-input
          v-model="queryParams.goodsNo"
          placeholder="请输入云仓商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="商家商品编码" prop="erpGoodsNo">
        <el-input
          v-model="queryParams.erpGoodsNo"
          placeholder="请输入商家商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="商品库SkuId" prop="erpGoodsSkuId">
        <el-input
          v-model="queryParams.erpGoodsSkuId"
          placeholder="请输入商品库SkuId"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品标题" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品标题"
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
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull()"
        >拉取JD云仓商品数据-ERP</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull1()"
        >拉取JD云仓商品数据-仓配一体</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="云仓商品编码" align="left" prop="goodsNo" width="180px"></el-table-column>
      <el-table-column label="商品标题" align="left" prop="goodsName" />
      <el-table-column label="规格" align="left" prop="standard" />
<!--      <el-table-column label="颜色" align="left" prop="color" />-->
<!--      <el-table-column label="尺寸" align="left" prop="size" />-->
      <el-table-column label="条码" align="left" prop="barCode" />
<!--      <el-table-column label="商品库SkuId" align="center" prop="erpGoodsSkuId" />-->
<!--      <el-table-column label="SkuId" align="center" prop="erpGoodsSkuId" />-->
      <el-table-column label="商家商品编码" align="left" prop="erpGoodsNo" width="180px"></el-table-column>
<!--      <el-table-column label="商品编码" align="left" prop="erpGoodsSign" width="180px"></el-table-column>-->


<!--      <el-table-column label="颜色" align="left" prop="color" />-->
<!--      <el-table-column label="尺寸" align="left" prop="size" />-->
<!--      <el-table-column label="规格" align="left" prop="standard" />-->

<!--      <el-table-column label="条码" align="left" prop="barCode" />-->
       <el-table-column label="商品库SkuId" align="center" prop="erpGoodsSkuId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--          >修改</el-button>-->
          <el-button
            size="mini" v-if="scope.row.warehouseType==='JDYC'"
            type="text"
            icon="el-icon-edit"
            @click="handleLink(scope.row)"
          >关联商品</el-button>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--          >修改关联</el-button>-->

<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="2" ref="popup"></PopupSkuList>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getToken} from "@/utils/auth";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import PopupSkuList from '@/views/goods/PopupSkuList.vue'
import {listCloudWarehouse, listWarehouse} from '@/api/wms/warehouse'
import { listWarehouseGoodsItems,linkErpGoodsSku } from '@/api/wms/goods'
import {  listMerchantCloudWarehouse } from '@/api/store/warehouse'
import { getUserProfile } from '@/api/system/user'

import {pullJdCloudWarehouseGoodsItems} from "@/api/cloud_warehouse/jdl_erp";
import {pullJdlGoodsItems} from "@/api/cloud_warehouse/jdl_isc";

export default {
  name: "CloudWarehouseGoodsList",
  components: { PopupSkuList, Treeselect },
  data() {
    return {
      importOpen:false,
      headers: { 'Authorization': 'Bearer ' + getToken() },
      // 遮罩层
      loading: true,
      pullLoading: false,
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
      // 商品管理表格数据
      goodsList: [],
      shopList: [],
      warehouseList: [],
      // 弹出层标题
      title: "",
      userType: null,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        warehouseId: null,
        goodsNo: null,
        erpGoodsNo: null,
        goodsName: null,
      },
      // 表单参数
      form: {
        id:null,
        erpGoodsSkuId:null
      },
      supplierList: [],
      categoryList: [],
      categoryTree: [],
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        goodsName: [{ required: true, message: "不能为空", trigger: "blur" }],
        erpGoodsNo: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    // getUserProfile().then(res=>{
    //   this.userType = res.data.userType;
    // })

    listMerchantCloudWarehouse({ }).then(response => {
      this.warehouseList = response.data;
      if (this.warehouseList&&this.warehouseList.length > 0) {
        this.queryParams.warehouseId = this.warehouseList[0].id
      }
      this.getList();
    });
  },
  methods: {
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listWarehouseGoodsItems(this.queryParams).then(response => {
        this.goodsList = response.rows;
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
        erpSkuId: null
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

    handleUpdate(row) {
      this.reset();
        this.form = row;
        this.open = true;
    },
    handlePull() {
      this.pullLoading = true
      pullJdCloudWarehouseGoodsItems({warehouseId:this.queryParams.warehouseId}).then(response => {
        console.log('拉取JD云仓商品接口返回=====',response)
        if(response.code === 200) {
          this.getList()
          this.$modal.msgSuccess(JSON.stringify(response));
        }else{
          this.$modal.msgError(response.msg)
        }
        this.pullLoading = false
      })
    },
    handlePull1() {
      this.pullLoading = true
      pullJdlGoodsItems({warehouseId:this.queryParams.warehouseId}).then(response => {
        console.log('拉取JD云仓-仓配一体商品接口返回=====',response)
        if(response.code === 200) {
          this.getList()
          this.$modal.msgSuccess(JSON.stringify(response));
        }else{
          this.$modal.msgError(response.msg)
        }
        this.pullLoading = false
      })
    },
    handleLink(row) {
      this.form.id = row.id;
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      this.form.erpGoodsSkuId = data[0].id
      linkErpGoodsSku(this.form).then(response => {
        this.$modal.msgSuccess("关联成功");
        this.open = false;
        this.getList();
      });

    },
  }
};
</script>
