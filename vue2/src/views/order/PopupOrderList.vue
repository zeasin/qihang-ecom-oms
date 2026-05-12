<template>
  <el-dialog :visible.sync="dialogVisible" title="选择订单" width="1100px" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="订单号" align="left" prop="orderNum" width="220" />
      <el-table-column label="店铺" align="left" prop="shopId" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" align="right" prop="totalAmount" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.amount).toFixed(2) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="收货人" align="center" prop="consignee" width="100" />-->
      <el-table-column label="下单商品数" align="center" prop="consignee" width="100" >
        <template slot-scope="scope">
          {{ (scope.row.itemVoList.length) }}
        </template>
      </el-table-column>
      <el-table-column label="商户" align="left" prop="shopId" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" >{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>

            </span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 0;">新订单</el-tag>
          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 0;">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 0;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 0;">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 21" style="margin-bottom: 0;">待付款</el-tag>
          <el-tag v-if="scope.row.orderStatus === 22" style="margin-bottom: 0;">锁定</el-tag>
          <el-tag v-if="scope.row.orderStatus === 29" style="margin-bottom: 0;">删除</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 0;">已取消</el-tag>
          <el-tag v-if="scope.row.orderStatus === 13" style="margin-bottom: 0;">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template slot-scope="scope">
          <el-button type="text" @click="sendDataToParent(scope.row)">选择</el-button>
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
  </el-dialog>
</template>
<script>
import { listOrder } from "@/api/order/order";
import { listShop } from "@/api/shop/shop";
import { parseTime } from "@/utils/zhijian";
import {listMerchant} from "@/api/shop/merchant";

export default {
  name: 'PopupOrderList',
  props: {
    orderStatus: {
      type: Number,
      default: null
    },
    shareStatus: {
      type: Number,
      default: null
    },
    settlementStatus: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: true,
      total: 0,
      dataList: [],
      shopList: [],
      merchantList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        orderStatus: null,
        shareStatus: null,
        settlementStatus: null
      }
    };
  },
  created() {
    this.getShopList();
  },
  methods: {
    parseTime,
    getShopList() {
      listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
        this.merchantList = resp.rows
      })
      listShop({}).then(response => {
        this.shopList = response.rows;
      });
    },
    openDialog() {
      this.queryParams.orderNum = null;
      this.queryParams.pageNum = 1;
      this.queryParams.orderStatus = this.orderStatus;
      this.queryParams.shareStatus = this.shareStatus;
      this.queryParams.settlementStatus = this.settlementStatus;
      this.getList();
      this.dialogVisible = true;
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.queryParams.orderNum = null;
      this.queryParams.orderStatus = this.orderStatus;
      this.queryParams.shareStatus = this.shareStatus;
      this.queryParams.settlementStatus = this.settlementStatus;
      this.handleQuery();
    },
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    sendDataToParent(row) {
      this.$emit('data-from-select', row);
      this.dialogVisible = false;
    }
  }
};
</script>
<style scoped lang="scss">
</style>
