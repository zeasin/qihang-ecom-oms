<template>
  <el-dialog :visible.sync="dialogVisible" title="选择子订单">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="120px">

      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" placeholder="请选择商户"  @change="merchantChange">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" @change="handleQuery">
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
<!--        <el-button round icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button round icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="btn===1"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          :disabled="multiple"
          @click="sendDataToParent"
        >确认添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="btn===2"
          type="primary"
          size="mini"
          :disabled="single"
          @click="sendDataToParent"
        >确认选中</el-button>
      </el-col>

      <!--      <right-toolbar showSearch.sync="true" @queryTable="getList"></right-toolbar>-->
    </el-row>

    <el-table v-loading="loading" :data="goodsSpecList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>

      <el-table-column label="订单号" align="left" prop="orderId" width="260"/>
      <el-table-column label="图片" align="center" prop="img" width="60">
        <template slot-scope="scope">
          <image-preview :src="scope.row.img" :width="35" :height="35"/>
        </template>
      </el-table-column>
      <el-table-column label="商品" align="center" prop="title" width="260"/>
      <el-table-column label="Sku规格" align="center" prop="skuName" ></el-table-column>
<!--      <el-table-column label="商品编码" align="center" prop="goodsNum" />-->
      <el-table-column label="平台SkuId" align="center" prop="skuId" />
      <!--      <el-table-column label="尺码" align="center" prop="sizeValue" />-->
      <!--      <el-table-column label="款式" align="center" prop="styleValue" />-->
<!--      <el-table-column label="采购价" align="center" prop="purPrice" />-->
      <el-table-column label="订单金额" align="center" prop="itemAmount" >
        <template slot-scope="scope">
          {{amountFormatter(null,null,scope.row.itemAmount/100)}}
        </template>

      </el-table-column>
<!--      <el-table-column label="零售价" align="center" prop="retailPrice" />-->

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


<!--    <span>点击按钮向主页面传递数据</span>-->
<!--    <el-button @click="sendDataToParent">发送数据</el-button>-->
  </el-dialog>
</template>
<script>

import { listMerchant } from '@/api/shop/merchant'
import { amountFormatter } from '../../../utils/zhijian'
import {listShopOrderItem} from "@/api/shop/order";
import {listShop} from "@/api/shop/shop";

export default {
  name: 'PopupShopGoodsSkuList',
  props:{
    btn:null,//1多选添加2单选
    shopId:null,
  },
  data() {
    return {
      dialogVisible: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],

      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 商品规格库存管理表格数据
      goodsSpecList: [],
      merchantList: [],
      shopList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        goodsId: null,
        id: null,
        goodsNum: null,
        skuCode: null,
        shopId: null,
      },
    }
  },
  mounted() {
    if(this.shopId){
      this.queryParams.shopId = this.shopId
    }
    listMerchant({ }).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if(resp.rows.length === 1&&resp.rows[0].id>0) {
        this.isMerchant = true;
      }
      listShop({ merchantId: this.queryParams.merchantId,type:this.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.getList()
      });

    })
  },
  methods: {
    amountFormatter,
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    // 打开弹出框
    openDialog() {
      if(this.shopId){
        this.queryParams.shopId = this.shopId
      }
      this.getList()
      this.dialogVisible = true;
    },
    // 发送数据到父组件
    sendDataToParent() {
      if(!this.ids) {
        this.$modal.msgError("请选择商品")
      }
      const filteredList = this.goodsSpecList.filter(item => this.ids.includes(item.id));
      console.log("======选中的商品=====",filteredList)
      const data = { message: 'Hello from Popup!' };
      this.$emit('data-from-select', filteredList); // 通过自定义事件传递数据
      this.dialogVisible = false; // 关闭弹出框
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询商品规格库存管理列表 */
    getList() {
      this.loading = true;
      listShopOrderItem(this.queryParams).then(response => {
        this.goodsSpecList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>
<style scoped lang="scss">

</style>
