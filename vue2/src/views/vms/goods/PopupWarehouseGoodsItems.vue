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
      <el-form-item label="仓库商品ID" prop="id">
              <el-input
                v-model="queryParams.id"
                placeholder="请输入仓库商品ID"
                clearable
                @keyup.enter.native="handleQuery"
              />
      </el-form-item>
      <el-form-item label="ERP商品ID" prop="erpGoodsSkuId">
        <el-input
          v-model="queryParams.erpGoodsSkuId"
          placeholder="请输入ERP商品ID"
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

      <el-form-item label="商户" prop="merchantId" style="display: none">
        <el-select v-model="queryParams.merchantId" clearable placeholder="请选择商户"  @change="handleQuery">
          <el-option label="总部" value="0"></el-option>
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

      <el-table-column label="仓库商品ID" align="center" prop="id" width="120"/>
      <el-table-column label="Erp商品ID" align="center" prop="erpGoodsSkuId" width="120"/>
      <el-table-column label="图片" align="center" prop="imageUrl" width="60">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="35" :height="35"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="left" prop="goodsName" />
      <el-table-column label="规格" align="center" prop="standard" ></el-table-column>
      <el-table-column label="库存" align="center" prop="stock" ></el-table-column>
      <el-table-column label="商家商品编码" align="center" prop="erpGoodsNo" />
      <el-table-column label="商户" align="left" prop="merchantId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.merchantId==0">总部</el-tag>
          <el-tag v-else>
            {{ merchantList.find(x=>x.id === scope.row.merchantId) ? merchantList.find(x=>x.id === scope.row.merchantId).name : '' }}
          </el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column label="尺码" align="center" prop="sizeValue" />-->
      <!--      <el-table-column label="款式" align="center" prop="styleValue" />-->
<!--      <el-table-column label="采购价" align="center" prop="purPrice" />-->
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
import { listStoredGoodsSkuAndStock } from '@/api/vms/goods/stored_goods_sku'
import {listMerchant} from "@/api/vms/merchant";

export default {
  name: 'PopupWarehouseGoodsItems',
  props:{
    btn:null,//1多选添加2单选
    merchantId:null,
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
        erpGoodsSkuId: null,
        goodsNum: null,
        skuCode: null,
        merchantId: null,
      },
    }
  },
  mounted() {
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
      this.getList();
    })
  },
  methods: {
    // 打开弹出框
    openDialog() {
      listMerchant({pageNum:1,pageSize:100}).then(resp=>{
        this.merchantList = resp.rows
        if(this.merchantId){
          this.queryParams.merchantId = this.merchantId
        }
        if(this.queryParams.merchantId===0){
          this.queryParams.merchantId = '0'
        }
        // this.getList()
      })
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
      this.queryParams.merchantId = this.merchantId
      this.goodsSpecList = []
      listStoredGoodsSkuAndStock(this.queryParams).then(response => {
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
