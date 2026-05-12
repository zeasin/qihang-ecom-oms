<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
      <el-form-item label="平台商品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入平台商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家编码" prop="outProductId">
        <el-input
          v-model="queryParams.outProductId"
          placeholder="请输入商家编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品库ID" prop="erpGoodsId">
        <el-input
          v-model="queryParams.erpGoodsId"
          placeholder="请输入商品库ID"
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

      <el-form-item label="店铺" prop="shopId" v-if="!isShop">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" :loading="shopLoading"
                   filterable clearable @change="handleQuery">
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >手动添加店铺商品</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取商品数据</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="diansanConfig"
          :loading="pullLoading"
          type="danger"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePullDownload(null)"
        >下载商品（DIANSAN）</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="diansanConfig && shopType===100"
          :loading="pullLoading"
          type="danger"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePullDownload(1)"
        >下载下架商品（DIANSAN）</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          :disabled="multiple"-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          @click="handlePushOms"-->
<!--        >推送到商品库</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="left" prop="id" width="60"/>
      <el-table-column label="平台商品ID" align="left" prop="productId"  width="180"/>
      <el-table-column label="图片" align="center" prop="logo" width="70">
        <template slot-scope="scope">
          <image-preview :src="scope.row.img" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名" align="left" prop="title" />
      <el-table-column label="商家编码" align="center" prop="spuCode" />

      <el-table-column label="SKU" align="center" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleViewSkuList(scope.row)"
          >{{scope.row.skuList.length +' 个SKU'}}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="center" prop="shopId" v-if="!isShop">
        <template slot-scope="scope">
          {{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}
          <el-tag size="small" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId).name}}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="商品库ID" align="center" prop="erpGoodsId" />
      <el-table-column label="状态" align="center" prop="isSkuOnsale" >
        <template slot-scope="scope">
          <!--是否在架上，0-下架中，1-架上-->
          <el-tag size="small" v-if="scope.row.status === 1">上架中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 0">已下架</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2">已售完</el-tag>
          <el-tag size="small" v-if="scope.row.status === 3">已删除</el-tag>
          <el-tag size="small" v-if="scope.row.status === 4">违规下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleAddSku(scope.row)"
          >新增SKU</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
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


    <el-dialog title="Sku List" :visible.sync="skuOpen" width="1200px" append-to-body>
      <el-table v-loading="loading" :data="skuList"  :row-class-name="rowDataItemIndex" >
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <el-table-column label="序号" align="center" prop="index" width="50"/>
        <el-table-column label="ID" align="left" prop="id" width="60"/>
        <el-table-column label="平台SkuId" align="left" prop="skuId" width="180"/>
        <el-table-column label="图片" align="center" prop="img" width="70">
          <template slot-scope="scope">
            <image-preview :src="scope.row.img" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <!--        <el-table-column label="商品名称" align="left" prop="goodsName" width="288px"/>-->
        <el-table-column label="Sku名称" align="left" prop="skuName" width="300"></el-table-column>
        <el-table-column label="商家Sku编码" align="left" prop="outerSkuId" />
        <el-table-column label="价格" align="center" prop="price" >
          <template slot-scope="scope">
            {{ amountFormatter(null,null,scope.row.price/100)}}
          </template>
        </el-table-column>
        <el-table-column label="库存" align="center" prop="stockNum" />
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
<!--        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-share"-->
<!--              @click="handleLink(scope.row)"-->
<!--            >关联ERP</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>
    </el-dialog>

    <!-- 添加或修改商品管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
        <el-form-item label="平台商品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入平台商品ID" style="width: 230px;" />
        </el-form-item>
        </el-row>
        <el-form-item label="商品编号" prop="spuCode">
          <el-input v-model="form.spuCode" placeholder="请输入商品编号" style="width: 230px;" />
        </el-form-item>
        <el-row>
          <el-form-item label="商品名称" prop="title">
            <el-input v-model="form.title" placeholder="请输入商品名称"  style="width: 590px;"/>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="图片" prop="img">
            <image-upload v-model="form.img" :limit="1"/>
            <el-input v-model="form.img" placeholder="请输入商品图片Url"  />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="商品库商品ID" prop="erpGoodsId">
            <el-input v-model="form.erpGoodsId" placeholder="商品库商品ID" style="width: 230px;" />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="价格" prop="price">
            <el-input type="number" v-model.number="form.price" placeholder="请输入商品价格" style="width: 230px;"/>
          </el-form-item>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status"   placeholder="状态">
            <el-option label="销售中" :value="1"></el-option>
            <el-option label="已下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加商品规格对话框 -->
    <el-dialog :title="title" :visible.sync="openSkuAdd" width="700px" append-to-body>
      <el-form ref="formSku" :model="formSku" :rules="rulesSku" label-width="140px">

        <el-form-item label="商品库商品：" prop="goodsList">
          <el-col :span="24">
            <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <image-preview :src="formSku.img" :width="80" :height="80"/>
        </el-form-item>
        <el-form-item label="商品名称" prop="title">
          <el-input v-model="formSku.title" placeholder="请输入商品名称"  style="width: 440px;"/>
        </el-form-item>
        <el-form-item label="规格" prop="skuName">
          <el-input v-model="formSku.skuName" placeholder="请输入商品规格" style="width: 360px;" />
        </el-form-item>

        <el-form-item label="售价" prop="price">
          <el-input type="number" v-model.number="formSku.price" placeholder="售价" />
        </el-form-item>
        <el-form-item label="第三方平台SkuId" prop="skuId">
          <el-input type="number" v-model.number="formSku.skuId" placeholder="请输入第三方平台SkuId" />
        </el-form-item>
        <!--        <el-form-item label="ERP商品SkuID" prop="outerErpSkuId">-->
        <!--          <el-input type="number" v-model.number="form.outerErpSkuId" placeholder="请输入ERP商品SkuID" />-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddSkuForm">确 定</el-button>
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

import {
  listGoods,
  pullGoodsList,
  pushToErp,
  pullDownloadGoods,
  delGoods,
  updateGoods,
  addShopGoodsSku,
  insertShopGoodsSku
} from "@/api/shop/goods";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import { listMerchant } from '@/api/shop/merchant'
import {amountFormatter, rowDataItemIndex} from '@/utils/zhijian'
import {getInternalSystemConfig} from "@/api/third_system";
import {getUserProfile} from "@/api/system/user";
import {listPlatform} from "@/api/vms/shop";
import PopupSkuList from "@/views/goods/PopupSkuList.vue";


export default {
  name: "ShopGoodsList",
  components: {PopupSkuList},
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: false,
    },
    refresh: {
      type: String,
      required: false,
      default: ''
    },
    targetShopId: {
      type: String,
      required: false,
      default: ''
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: false,
      isMerchant: false,
      isShop: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      diansanConfig: false,
      // 总条数
      total: 0,
      // 商品管理表格数据
      goodsList: [],
      shopList: [],
      platformList: [],
      skuList:[],
      skuOpen:false,
      openSkuAdd:false,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        merchantId: null,
        shopId: null,
        shopType:null,
        outProductId:null,
        productId:null,
        erpGoodsId:null,
      },
      // 表单参数
      form: {},
      formSku: {
        shopGoodsId: null,
        erpGoodsId: null,
        erpGoodsSkuId: null,
        skuId: null,
        title: null,
        img: null,
        skuName: null,
        price: null,
      },
      merchantList: [],
      supplierList: [],
      categoryList: [],
      categoryTree: [],
      // 表单校验
      rules: {
        id: [{ required: true, message: "不能为空", trigger: "change" }],
        productId: [{ required: true, message: "不能为空", trigger: "blur" }],
        title: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      rulesSku: {
        shopGoodsId: [{ required: true, message: "不能为空", trigger: "change" }],
        skuId: [{ required: true, message: "不能为空", trigger: "blur" }],
        title: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  mounted() {
    console.log('===店铺商品，店铺类型：'+this.shopType)
    console.log('===refresh:', this.refresh, 'targetShopId:', this.targetShopId)
    listPlatform({status:0}).then(res => {
      this.platformList = res.rows;
    })
    if (this.shopType!==0) {
      this.queryParams.shopType = this.shopType
    }
    // 检查是否需要刷新
    if (this.refresh === '1' && this.targetShopId) {
      this.refreshWithTargetShop();
      return;
    }
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
        this.shopList.push({id:res.data.deptId,name:res.data.nickName})
        this.getList()
      }
    })
  },
  created() {
    getInternalSystemConfig(400).then(response => {
      if (response.data) {
        this.diansanConfig = true;
      }
    })

    // listMerchant({ pageNum: 1, pageSize: 100 }).then(resp => {
    //   this.merchantList = resp.rows
    // })
    // this.shopLoading=true
    // listShop({type:this.shopType}).then(response => {
    //   this.shopList = response.rows;
    //   this.shopLoading=false
    //   // if (this.shopList && this.shopList.length > 0) {
    //   //   this.queryParams.shopId = this.shopList[0].id
    //   // }
    //   // this.getList();
    // });
    // this.getList();
    // this.loading = false;

  },
  watch: {
    $route: {
      handler(to, from) {
        // 检测到refresh参数时重新加载数据
        if (to.query.refresh === '1') {
          // 更新查询参数
          if (to.query.shopType) {
            this.queryParams.shopType = to.query.shopType
          }
          if (to.query.shopId) {
            this.queryParams.shopId = to.query.shopId
          }
          if (to.query.merchantId) {
            this.queryParams.merchantId = to.query.merchantId
          }
          this.getList();
        }
      },
      immediate: true
    }
  },

  methods: {
    amountFormatter,
    rowDataItemIndex,
    /** 根据目标店铺ID刷新列表 */
    refreshWithTargetShop() {
      this.loading = true;
      // 根据目标店铺ID获取店铺信息
      listShop({}).then(response => {
        this.shopList = response.rows;
        const targetShop = this.shopList.find(s => s.id == this.targetShopId);
        if (targetShop) {
          this.queryParams.shopId = targetShop.id;
          this.queryParams.merchantId = targetShop.merchantId;
          this.queryParams.shopType = targetShop.type;
          
          // 获取商户信息，确保商户下拉框显示正确的商户名称
          listMerchant({}).then(resp => {
            this.merchantList = resp.rows;
            this.shopLoading = false;
            this.getList();
          });
        } else {
          this.shopLoading = false;
          this.getList();
        }
      });
    },
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
    handleAdd() {
      this.$router.push('/goods/shop_goods_create?shopType=' + (this.shopType>0 ? this.shopType:'')+'&shopId='+this.queryParams.shopId);
    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({type:999,name:query}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        })
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
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

      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.skuOpen = false;
      this.open = false;
      this.openSkuAdd = false;
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
      this.pullLoading = false
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看SKU List*/
    handleViewSkuList(row){
      this.skuList = row.skuList
      this.skuOpen = true;

    },
    handleUpdate(row) {
      this.reset();
      this.form = row;
      if(row.price) this.form.price = row.price/100;
      this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateGoods(this.form).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.skuOpen = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg);
            }

          });
        }
      });
    },
    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullGoodsList({shopId:this.queryParams.shopId}).then(response => {
          console.log('拉取店铺商品接口返回=====',response)
          if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/list",query:{type:(this.shopType>0?this.shopType:null)}})
              // isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });
          }else if(response.code === 200) {
            this.getList()
            this.$modal.msgSuccess(JSON.stringify(response));
          }else{
            this.$modal.msgError(response.msg);
          }
          this.pullLoading = false
        })
      }else{
        this.$modal.msgError("请先选择店铺");
      }

      // this.$modal.msgSuccess("请先配置API");
    },
    handlePushOms(){
      this.$confirm('确认同步所有商品到商品库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        pushToErp( this.ids ).then(response => {
          this.$message.success('商品同步成功')
          this.getList()
        }).finally(() => {
          this.loading = false
        })
      })
    },
    handlePullDownload(downloadType) {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullDownloadGoods({shopId:this.queryParams.shopId,downloadType:downloadType}).then(resp=>{
          if(resp.code===200){
            this.$modal.msgSuccess("下载成功！请3-5分钟之后再去拉取商品资料！");
            this.pullLoading = false
            this.getList()
          }else{
            this.$modal.msgError(resp.msg)
          }
        })
        this.pullLoading = false
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除商品ID为"' + ids + '"的数据项？').then(function() {
        return delGoods(ids);
      }).then(response => {
        if(response.code === 200){
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }else
         this.$modal.msgError(response.msg);
      }).catch(() => {
      });
    },
    /**添加商品SKU**/
    handleAddSku(row){
      this.formSku={
        shopGoodsId: null,
        erpGoodsId: null,
        erpGoodsSkuId: null,
        title: null,
        img: null,
        skuName: null,
        price: null,
      }
      this.formSku.shopGoodsId = row.id;
      this.openSkuAdd = true
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data) {
        if (data[0]) {
          this.formSku.erpGoodsId = data[0].goodsId
          this.formSku.erpGoodsSkuId = data[0].id
          this.formSku.img = data[0].colorImage
          this.formSku.title = data[0].goodsName
          this.formSku.skuName = data[0].skuName
          this.formSku.price = data[0].retailPrice
        }
      }
      console.log('====添加后=====', this.formSku)
    },
    /** 提交按钮 */
    submitAddSkuForm() {
      this.$refs["formSku"].validate(valid => {
        if (valid) {
          insertShopGoodsSku(this.formSku).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess("添加SKU成功");
              this.open = false;
              this.skuOpen = false;
              this.openSkuAdd = false;
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
