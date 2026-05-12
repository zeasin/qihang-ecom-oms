<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="商品名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="商品名称搜索"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台商品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入平台商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
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
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull(1)"
        >API拉取螳螂商品数据</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="pullLoading"-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handlePull(0)"-->
<!--        >API全量拉取商品数据</el-button>-->
<!--      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="平台商品ID" align="left" prop="productId" width="180px"></el-table-column>
      <el-table-column label="商品编码" align="left" prop="outerProductId" width="180px"></el-table-column>
      <el-table-column label="商品标题" align="left" prop="title" />
<!--      <el-table-column label="商品库商品编码" align="left" prop="erpGoodsNo" />-->
<!--      <el-table-column label="颜色" align="left" prop="color" />-->
<!--      <el-table-column label="尺寸" align="left" prop="size" />-->
<!--      <el-table-column label="规格" align="left" prop="standard" />-->

<!--      <el-table-column label="条码" align="left" prop="barCode" />-->
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
          {{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}
          <el-tag size="small">{{merchantList.find(x=>x.id == scope.row.merchantId)?merchantList.find(x=>x.id == scope.row.merchantId).name:''}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否绑定发货实物SKU" align="center" prop="erpGoodsSkuId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.bindShipSku===0">未绑定</el-tag>
          <el-tag v-if="scope.row.bindShipSku===1">已绑定</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.deliverMethod===1"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleSetSw(scope.row)"
          >配置发货实物</el-button>
          <el-button
            v-if="scope.row.deliverMethod===0"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleLink(scope.row)"
          >关联商品库商品</el-button>
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

    <!-- 虚拟商品发货实物配置 -->
    <el-dialog :title="title" :visible.sync="openSw" width="1000px" append-to-body>
      <el-descriptions title="">
        <el-descriptions-item label="ID">{{swForm.id}}</el-descriptions-item>
        <el-descriptions-item label="平台商品ID">{{swForm.productId}}</el-descriptions-item>
        <el-descriptions-item label="">
        </el-descriptions-item>
        <el-descriptions-item label="商品名">
          {{swForm.title}}
        </el-descriptions-item>
      </el-descriptions>

      <el-form ref="swForm" :model="swForm" :rules="rules" label-width="80px" >
        <el-form-item label="实物商品">
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5">
              <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSShopOrderItem">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-table style="pardding-left: 108px;" :data="goodsSkuBindList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="SkuID" prop="id" width="100"></el-table-column>
          <el-table-column label="" prop="goodsImg" width="60">
            <template slot-scope="scope">
              <image-preview :src="scope.row.colorImage" :width="35" :height="35"/>
            </template>
          </el-table-column>
          <el-table-column label="商品名" prop="goodsName" width="200">
          </el-table-column>

          <el-table-column label="规格" prop="skuName">
<!--            <template slot-scope="scope">-->
<!--              <el-tag size="small" v-if="scope.row.colorValue">{{ scope.row.colorValue }}</el-tag>-->
<!--              <el-tag size="small" v-if="scope.row.sizeValue">{{ scope.row.sizeValue }}</el-tag>-->
<!--              <el-tag size="small" v-if="scope.row.styleValue">{{ scope.row.styleValue }}</el-tag>-->
<!--            </template>-->
          </el-table-column>
          <el-table-column label="Sku编码" prop="skuCode" ></el-table-column>
          <el-table-column label="数量" prop="quantity" >
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.quantity" placeholder="请输入商品数量" style="width: 100px"/>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveGoodsSubmitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 关联商品库 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="SKU编码" prop="skuCode">
          <el-input v-model="form.skuCode" placeholder="请输入SKU编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="1" :merchantId="5" ref="popup"></PopupSkuList>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getToken} from "@/utils/auth";
// import {listJdCloudWarehouseGoodsItems,getGoodsSku,linkErpGoodsSkuId,pullJdCloudWarehouseGoodsItems} from "@/api/cloud_warehouse/jd";
import { bindTanglangGoodsSku, listTanglangGoodsItems,listTanglangGoodsShipItems } from '@/api/shop/tanglang'
import { pullGoodsList } from '@/api/shop/goods'
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import PopupSkuList from '@/views/goods/PopupSkuList.vue'
import { listShop } from '@/api/shop/shop'
import { listMerchant } from '@/api/shop/merchant'

export default {
  name: "GoodsList",
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openSw: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        title: null,
        productId: null,
        saleState: '1'
      },
      // 表单参数
      form: {},
      goodsSkuBindList:[],
      swForm: {
        id:null,
        skuList:[]
      },
      merchantList: [],
      supplierList: [],
      categoryList: [],
      categoryTree: [],
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        skuCode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    listMerchant({ pageNum: 1, pageSize: 100 }).then(resp => {
      this.merchantList = resp.rows
    })
    listShop({type:911}).then(response => {
      this.shopList = response.rows;
      // if (this.shopList && this.shopList.length > 0) {
      //   this.queryParams.shopId = this.shopList[0].id
      // }
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
      listTanglangGoodsItems(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openSw = false;
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
    // 虚拟商品配置发货实物
    handleSetSw(row){
      this.title='配送发货实物商品'
      this.swForm.id = row.id;
      this.swForm.title = row.title;
      this.swForm.productId = row.productId;
      this.swForm.skuList=[]
      listTanglangGoodsShipItems(row.id).then(response => {
        this.goodsSkuBindList = response.data;
      })
      this.openSw = true;
    },
    handleLink(row) {
      this.reset();
      const id = row.id || this.ids
        this.form = row;
        this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          linkErpGoodsSkuId(this.form).then(response => {
            this.$modal.msgSuccess("关联成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    handlePull(pull_type) {
      if(!this.queryParams.shopId){
        this.$modal.msgError('请选择店铺');
        return;
      }
        this.pullLoading = true
      pullGoodsList({shopId:this.queryParams.shopId,pullType:pull_type}).then(response => {
          console.log('拉取JD自营商品接口返回=====',response)
          if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/shop_list",query:{type:5}})
              // isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else{
            this.getList()
            this.$modal.msgSuccess(JSON.stringify(response));
          }
          this.pullLoading = false

        })
      // this.$modal.msgSuccess("请先配置API");
    },
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    handleSShopOrderItemSelectionChange(selection) {
      this.checkedSShopOrderItem = selection.map(item => item.index)
    },
    handleDeleteSShopOrderItem() {
      if (this.checkedSShopOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的商品数据");
      } else {
        const sShopOrderItemList = this.goodsSkuBindList;
        const checkedSShopOrderItem = this.checkedSShopOrderItem;
        this.goodsSkuBindList = sShopOrderItemList.filter(function(item) {
          return checkedSShopOrderItem.indexOf(item.index) == -1
        });
      }
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);

      this.goodsSkuBindList.push(...data)

      console.log('====添加后=====',this.swForm)

    },
    // 保存发货实物商品配置
    saveGoodsSubmitForm(){
      this.swForm.skuList = this.goodsSkuBindList
      console.log('====保存验证前=====',this.swForm)
      bindTanglangGoodsSku(this.swForm).then(response => {
        this.$modal.msgSuccess("绑定成功")
        this.openSw = false;
        this.getList()
      })
    },
  }
};
</script>
