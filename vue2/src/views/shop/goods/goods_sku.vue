<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
      <el-form-item label="平台SkuId" prop="skuId">
        <el-input
          v-model="queryParams.skuId"
          placeholder="请输入平台SkuId"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家sku编码" prop="outerId">
        <el-input
          v-model="queryParams.outerId"
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
      <el-form-item label="平台" prop="shopType" v-if="!isShop && (shopType==null || shopType==0)">
      <el-select v-model="queryParams.shopType"  placeholder="请选择平台"  @change="platformChange">
        <el-option
          v-for="item in platformList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
      <el-form-item label="店铺" prop="shopId" v-if="!isShop ">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
         <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
           <span style="float: left">{{ item.name }}</span>
           <el-tag> {{ platformList.find(x=>x.id === item.type)?platformList.find(x=>x.id === item.type).name:'未知平台' }}</el-tag>
<!--           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>-->
<!--           <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>-->
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          @click="handleLinkOms"-->
<!--        >一键关联商品库SKU</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="店铺商品SkuId" align="center" prop="id" />
      <el-table-column label="平台商品ID" align="left" prop="productId" width="180"/>
      <el-table-column label="平台SkuId" align="left" prop="skuId" width="180"/>
      <el-table-column label="图片" align="center" prop="logo" width="70">
        <template slot-scope="scope">
          <image-preview :src="scope.row.img" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名" align="left" prop="productTitle" />
      <el-table-column label="规格" align="left" prop="skuName" />
      <el-table-column label="店铺" align="center" prop="shopId" v-if="!isShop">
        <template slot-scope="scope">
          {{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}
          <el-tag size="small" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
        </template>
      </el-table-column>
<!--       <el-table-column label="商家编码" align="center" prop="outProductId" />-->
       <el-table-column label="商家Sku编码" align="center" prop="outerSkuId" />
      <el-table-column label="商品库SkuId" align="center" prop="erpGoodsSkuId" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 1">上架中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 0">已下架</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2">已售完</el-tag>
          <el-tag size="small" v-if="scope.row.status === 3">已删除</el-tag>
          <el-tag size="small" v-if="scope.row.status === 4">违规下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleLink(scope.row)"-->
<!--          >关联ERP</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleLink(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['goods:goods:remove']"
          >删除</el-button>
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


    <!-- 添加或修改商品管理对话框 -->
<!--    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>-->
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="120px">-->
<!--        <el-form-item label="ERP商品SkuId" prop="erpGoodsSkuId">-->
<!--          <el-input v-model.number="form.erpGoodsSkuId" placeholder="请输入ERP商品SkuId" />-->
<!--        </el-form-item>-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">

        <el-form-item label="商品库商品：" prop="goodsList">
          <el-col :span="24">
            <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <image-preview :src="form.img" :width="80" :height="80"/>
        </el-form-item>
        <el-form-item label="商品名称" prop="productTitle">
          <el-input v-model="form.productTitle" placeholder="请输入商品名称"  style="width: 440px;"/>
        </el-form-item>
        <el-form-item label="规格" prop="skuName">
          <el-input v-model="form.skuName" placeholder="请输入商品规格" style="width: 360px;" />
        </el-form-item>

        <el-form-item label="售价" prop="price">
          <el-input type="number" v-model.number="form.price" placeholder="售价" />
        </el-form-item>
        <el-form-item label="第三方平台SkuId" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入第三方平台SkuId" />
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
import {listGoodsSku, linkErpGoodsSkuId, delGoodsSku, updateGoodsSku} from "@/api/shop/goods";

import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import {getUserProfile} from "@/api/system/user";
import {listPlatform} from "@/api/vms/shop";
import PopupSkuList from "@/views/goods/PopupSkuList.vue";

export default {
  name: "ShopGoodsSku",
  components: {PopupSkuList},
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: false,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      isMerchant: false,
      isShop: false,
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
      // 总条数
      total: 0,
      // 商品管理表格数据
      goodsList: [],
      shopList: [],
      platformList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        shopType:null
      },
      // 表单参数
      form: {
        id: null,
        productTitle: null,
        img: null,
        erpGoodsId: null,
        erpGoodsSkuId: null,
        skuId: null,
        skuName: null,
        price: null,
      },
      supplierList: [],
      categoryList: [],
      categoryTree: [],
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        erpGoodsSkuId: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },mounted() {
    listPlatform({status:0}).then(res => {
      this.platformList = res.rows;
    })
    if (this.shopType!==0) {
      this.queryParams.shopType = this.shopType
    }
  },
  created() {
    getUserProfile().then(res => {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isMerchant = false
        this.isShop = false
        listMerchant({}).then(resp => {
          this.merchantList = resp.rows
          if (this.merchantList.length > 0) {
            this.queryParams.merchantId = this.merchantList[0].id
          }
          listShop({merchantId: this.queryParams.merchantId, type: this.queryParams.shopType}).then(response => {
            this.shopList = response.rows;
            if (this.shopList.length > 0) {
              this.queryParams.shopId = this.shopList[0].id
            }
            this.shopLoading = false
            this.getList()
          });
        })
      } else if (res.data.userType == 20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId;
        this.merchantList = []
        this.merchantList.push({id:res.data.deptId,name:res.data.nickName})
        this.shopList = []
        listShop({merchantId: this.queryParams.merchantId, type: this.queryParams.shopType}).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.getList()
        });
      } else if (res.data.userType == 40) {
        this.isMerchant = true;
        this.isShop = true
        this.queryParams.shopId = res.data.deptId;
        this.merchantList = []
        this.shopList = []
        this.shopList.push({id: res.data.deptId, name: res.data.nickName})
        this.getList()
      }
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
    platformChange(nv){
      console.log('========ShopType======',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({type:this.queryParams.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.queryParams.shopType }).then(response => {
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
      if(this.shopType>0){
        this.queryParams.shopType = this.shopType
      }else {
        if(!this.queryParams.shopType){
          this.queryParams.shopType = null
        }
      }
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
    handleLink(row) {
      this.reset();
      this.form = {
        id: null,
        img: null,
        productTitle: null,
        erpGoodsId: null,
        erpGoodsSkuId: null,
        skuId: null,
        skuName: null,
        price: null,
      }
      this.form.id = row.id;
      this.form.productTitle = row.productTitle;
      this.form.img = row.img;
      this.form.erpGoodsId = row.erpGoodsId;
      this.form.erpGoodsSkuId = row.erpGoodsSkuId;
      this.form.skuId = row.skuId;
      this.form.skuName = row.skuName;
      this.form.price = row.price;
      this.open = true;
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data) {
        if (data[0]) {
          this.form.erpGoodsId = data[0].goodsId
          this.form.erpGoodsSkuId = data[0].id
          if(!this.form.img){
            this.form.img = data[0].colorImage
          }
          if(!this.form.productTitle){
            this.form.productTitle = data[0].goodsName
          }
          if(!this.form.skuName){
            this.form.skuName = data[0].skuName
          }
          // this.form.productTitle = data[0].goodsName
          // this.form.skuName = data[0].skuName
        }
      }
      console.log('====添加后=====', this.form)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateGoodsSku(this.form).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg)
            }

          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除SkuId为"' + row.id + '"的数据项？').then(function() {
        return delGoodsSku(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
};
</script>
