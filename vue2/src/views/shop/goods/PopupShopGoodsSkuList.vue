<template>
  <el-dialog :visible.sync="dialogVisible" title="选择商品">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="120px">

      <el-form-item label="商品名称" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称"
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
            <el-form-item label="SKUID" prop="id">
              <el-input
                v-model="queryParams.id"
                placeholder="请输入SKUID"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入SKU编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户" prop="merchantId" style="display: none">
        <el-select v-model="queryParams.merchantId" clearable placeholder="请选择商户" @change="handleQuery">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button round icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

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

      <el-table-column label="规格ID" align="center" prop="id" width="60"/>
      <el-table-column label="商品ID" align="center" prop="shopGoodsId" width="60"/>
      <el-table-column label="图片" align="center" prop="img" width="60">
        <template slot-scope="scope">
          <image-preview :src="scope.row.img" :width="35" :height="35"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="left" prop="productTitle" />
      <el-table-column label="Sku规格" align="center" prop="skuName" >
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.colorValue">{{ scope.row.colorValue }}</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.sizeValue">{{ scope.row.sizeValue }}</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.styleValue">{{ scope.row.styleValue }}</el-tag>-->
<!--        </template>-->
      </el-table-column>
<!--      <el-table-column label="商品编码" align="center" prop="goodsNum" />-->
      <el-table-column label="Sku编码" align="center" prop="skuCode" />
      <!--      <el-table-column label="尺码" align="center" prop="sizeValue" />-->
      <!--      <el-table-column label="款式" align="center" prop="styleValue" />-->
<!--      <el-table-column label="采购价" align="center" prop="purPrice" />-->
      <el-table-column label="销售价" align="center" prop="price" >
        <template slot-scope="scope">
          {{amountFormatter(null,null,scope.row.price/100)}}
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
import {listGoodsSku} from "@/api/shop/goods";
import { listMerchant } from '@/api/shop/merchant'
import { amountFormatter } from '../../../utils/zhijian'

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
    this.getList()
  },
  methods: {
    amountFormatter,
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
      listGoodsSku(this.queryParams).then(response => {
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
