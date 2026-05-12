<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出库状态" prop="stockingStatus">
        <el-select v-model="queryParams.stockingStatus" placeholder="请选择出库状态" clearable @change="handleQuery">
          <el-option label="待出库" value="0" ></el-option>
          <el-option label="出库中" value="1" ></el-option>
          <el-option label="已出库" value="2"></el-option>
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
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdateSendStatus"-->
<!--        >生成出库单</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" >
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="订单ID" align="center" prop="id" />-->
      <el-table-column label="订单编号" align="left" prop="orderNum">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
          >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
        </template>
      </el-table-column>

      <el-table-column label="商品明细" align="center" width="900px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">商品标题</td>
              <td width="150" align="left">规格</td>
              <td width="200" align="left">Sku编码</td>
              <td width="50" align="left">数量</td>
            </th>
          </table>
        </template>
        <template slot-scope="scope" >
          <el-table :data="scope.row.itemList" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >
            <el-table-column label="商品图片" width="50px">
              <template slot-scope="scope">
                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
              </template>
            </el-table-column>
            <el-table-column label="商品名" align="left" width="250px" prop="goodsName" >
              <template slot-scope="scope">
                {{scope.row.goodsName}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="SKU名" align="left" prop="skuName" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.skuName)}}
              </template>
            </el-table-column>
            <el-table-column label="Sku编码" align="left" prop="skuCode" width="200"/>
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="订单备注" align="center" prop="remark" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          {{scope.row.buyerMemo}}<br />
          {{scope.row.sellerMemo}}
        </template>
      </el-table-column>


      <el-table-column label="发货信息" align="left" prop="shippingNumber" >
        <template slot-scope="scope">
          {{scope.row.shippingCompany}}<br />
          {{scope.row.shippingNumber}}&nbsp;
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.shippingTime) }}
        </template>
      </el-table-column>
      <el-table-column label="出库状态" align="center" prop="sendStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.stockingStatus === 0">待出库</el-tag>
          <el-tag v-if="scope.row.stockingStatus === 1">出库中</el-tag>
          <el-tag v-if="scope.row.stockingStatus === 2">已出库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">

            <el-button
              v-if="scope.row.stockingStatus === 0"
              size="mini"
              plain
              type="primary"
              icon="el-icon-share"
              @click="handleShip(scope.row)"
            >生成出库单</el-button>
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


  </div>
</template>

<script>
import {listOrder, getOrder, updateSendStatus} from "@/api/vms/order";
import {generateStockOutByOrder} from "@/api/vms/ship";
import {listPlatform} from "@/api/vms/shop";
import { listMerchant } from '@/api/vms/merchant'
import Clipboard from 'clipboard'

export default {
  name: "Order",
  data() {
    return {
      // 遮罩层
      loading: true,
      pushLoading: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
      merchantList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 店铺订单表格数据
      orderList: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      platformList:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      activeName: 'orderDetail',
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        sendStatus: 2,
        stockingStatus: '0',
      },
      // 表单参数
      form: {
        ids:[],
        stockingId:null
      },
      // 表单校验
      rules: {

      }
    };
  },
  created() {
        this.getList();
  },
  methods: {
    copyActiveCode(event,queryParams) {
      console.log(queryParams)
      const clipboard = new Clipboard(".tag-copy")
      clipboard.on('success', e => {
        this.$message({ type: 'success', message: '复制成功' })
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        // 不支持复制
        this.$message({ type: 'waning', message: '该浏览器不支持自动复制' })
        // 释放内存
        clipboard.destroy()
      })
    },
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value || item.value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询店铺订单列表 */
    getList() {
      console.log('=====搜索条件：=====',this.queryParams)
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.orderTime=null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    reset(){

    },

    handleShip(row){
      this.form.ids = []
      this.form.ids.push(row.id)
      this.form.stockingId=row.id

      generateStockOutByOrder(this.form).then(response => {
        this.$modal.msgSuccess("出货单生成成功")
      })
    },


  }
};
</script>

