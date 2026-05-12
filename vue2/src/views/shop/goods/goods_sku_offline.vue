<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
<!--      <el-form-item label="平台SkuId" prop="skuId">-->
<!--        <el-input-->
<!--          v-model="queryParams.skuId"-->
<!--          placeholder="请输入平台SkuId"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="商家sku编码-" prop="outerSkuId">
        <el-input
          v-model="queryParams.outerSkuId"
          placeholder="请输入商家sku编码"
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
      <el-form-item label="商户" prop="merchantId" v-if="!isMerchant">
        <el-select v-model="queryParams.merchantId"  placeholder="请选择商户"  @change="merchantChange">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="店铺" prop="shopId">-->
<!--        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable filterable @change="handleQuery">-->
<!--         <el-option-->
<!--            v-for="item in shopList"-->
<!--            :key="item.id"-->
<!--            :label="item.name"-->
<!--            :value="item.id">-->
<!--           <span style="float: left">{{ item.name }}</span>-->
<!--           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>-->
<!--           <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId"
                   remote reserve-keyword :remote-method="searchShop"  :loading="shopLoading"
                   placeholder="请选择店铺" clearable @change="handleQuery" filterable>
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加店铺商品</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="pullLoading"-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handlePull"-->
<!--        >API拉取商品数据</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
<!--      <el-table-column label="平台商品ID" align="left" prop="productId" width="180"/>-->
<!--      <el-table-column label="平台Id" align="left" prop="skuId" width="180"/>-->
      <el-table-column label="商品库SkuId" align="center" prop="erpGoodsSkuId"  width="180"/>
      <el-table-column label="商家Sku编码" align="center" prop="outerSkuId"  width="230"/>

      <el-table-column label="图片" align="center" prop="logo" width="70">
        <template slot-scope="scope">
          <image-preview :src="scope.row.img" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名" align="left" prop="productTitle" />
      <el-table-column label="规格" align="left" prop="skuName" />
      <el-table-column label="店铺" align="left" prop="shopId" >
        <template slot-scope="scope">
<!--          {{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}-->
          {{scope.row.shopName}}
          <br />
          <el-tag size="small">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
        </template>
      </el-table-column>
<!--       <el-table-column label="商家编码" align="center" prop="outProductId" />-->

      <el-table-column label="库存数量" align="center" prop="stockNum" />
<!--      <el-table-column label="状态" align="center" prop="status" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.status === 1">上架中</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 0">已下架</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 2">已售完</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 3">已删除</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 4">违规下架</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleLink(scope.row)"-->
<!--          >关联ERP</el-button>-->

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

    <!-- 添加或修改商品规格库存管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item  label="商户" prop="merchantId">
          <el-select v-model="form.merchantId"  placeholder="请选择商户"  @change="merchantChange" style="width: 440px">
            <el-option
              v-for="item in merchantList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item  label="店铺" prop="shopId">
          <el-select v-model="form.shopId"
                     remote reserve-keyword :remote-method="searchShop"  :loading="shopLoading"
                     placeholder="请选择店铺" clearable @change="handleQuery" filterable style="width: 440px">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品：" prop="goodsList">
          <el-col :span="24">
            <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <image-preview :src="form.image" :width="80" :height="80"/>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入商品名称" disabled  style="width: 440px;"/>
        </el-form-item>
        <el-form-item label="规格" prop="skuName">
          <el-input v-model="form.skuName" placeholder="请输入商品规格" disabled style="width: 360px;" />
        </el-form-item>

        <el-form-item label="售价" prop="price">
          <el-input type="number" v-model.number="form.price" placeholder="售价" />
        </el-form-item>
        <el-form-item label="第三方平台SkuId" prop="skuId">
          <el-input type="number" v-model.number="form.skuId" placeholder="请输入第三方平台SkuId" />
        </el-form-item>
        <!--        <el-form-item label="ERP商品SkuID" prop="outerErpSkuId">-->
        <!--          <el-input type="number" v-model.number="form.outerErpSkuId" placeholder="请输入ERP商品SkuID" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="2" ref="popup"></PopupSkuList>

  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

import {listShop} from "@/api/shop/shop";
import { listMerchant } from '@/api/shop/merchant'
import {listGoodsSku, linkErpGoodsSkuId, addShopGoodsSku} from "@/api/shop/goods";

import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import PopupSkuList from "@/views/goods/PopupSkuList.vue";

export default {
  name: "GoodsSkuOffline",
  components: {PopupSkuList},
  props: {

  },
  data() {
    return {
      // 遮罩层
      loading: true,
      isMerchant: false,
      // 选中数组
      ids: [],
      merchantList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      shopLoading: false,
      // 总条数
      total: 0,
      // 商品管理表格数据
      goodsList: [],
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        shopType: 999
      },
      // 表单参数
      form: {
        goodsName:null,
        skuName:null,
        goodsId:null,
        goodsSkuId:null,
        image:null,
        price:null,
        merchantId:null,
        shopId:null,
        skuId:null,
      },
      supplierList: [],
      categoryList: [],
      categoryTree: [],
      // 表单校验
      rules: {
        merchantId: [{ required: true, message: "不能为空", trigger: "change" }],
        shopId: [{ required: true, message: "不能为空", trigger: "blur" }],
        price: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    listMerchant({ }).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if(resp.rows.length === 1&&resp.rows[0].id>0) {
        this.isMerchant = true;
      }
      listShop({ merchantId: this.queryParams.merchantId,type:999 }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.getList()
      });

    })
    // listShop({type:this.shopType}).then(response => {
    //   this.shopList = response.rows;
    //   if (this.shopList && this.shopList.length > 0) {
    //     this.queryParams.shopId = this.shopList[0].id
    //   }
    //   this.getList();
    // });
    // // this.getList();
    // this.loading = false;
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:999 }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listGoodsSku(this.queryParams).then(response => {
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
        goodsName:null,
        skuName:null,
        goodsId:null,
        goodsSkuId:null,
        image:null,
        price:null,
        merchantId:null,
        shopId:null,
        skuId:null,
      };
      this.resetForm("form");
    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        this.form.shopId=null
        listShop({merchantId:this.form.merchantId,type:999,name:query}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        })
      }
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
    handleAdd(){
      this.reset()
      this.open = true
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data) {
        if (data[0]) {
          this.form.goodsId = data[0].goodsId
          this.form.goodsSkuId = data[0].id
          this.form.image = data[0].colorImage
          this.form.goodsName = data[0].goodsName
          this.form.skuName = data[0].skuName
          this.form.price = data[0].retailPrice
        }
      }
      console.log('====添加后=====', this.form)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addShopGoodsSku(this.form).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("店铺商品添加成功");
              this.open = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg);
            }

          });
        }
      });
    },

  }
};
</script>
