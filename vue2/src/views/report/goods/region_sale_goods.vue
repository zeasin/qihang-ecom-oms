<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="国家/地区" prop="regionId">
        <el-select v-model="queryParams.regionId" placeholder="请选择国家/地区" clearable>
          <el-option
            v-for="item in regionList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="日期" prop="dates">
        <el-date-picker
                        v-model="queryParams.dates" value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="dailyList" @selection-change="handleSelectionChange" show-summary :summary-method="customSummaryMethod">
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <!-- 汇总行 -->

      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150"/>
       <el-table-column label="商品" align="left" prop="goodsName" width="330" />
       <el-table-column label="规格" align="center" prop="skuName" />
      <el-table-column label="总金额" align="center" prop="orderAmount" :formatter="amountFormatter" width="200px">
        <template slot-scope="scope">
          {{(scope.row.orderAmount / scope.row.exchangeRate ).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="订单数" align="center" prop="orderTotal" :sortable="true"/>

      <el-table-column label="刷单数" align="center" prop="falseOrderTotal" :sortable="true"/>
      <el-table-column label="刷单金额" align="center" prop="falseOrderAmount" :formatter="amountFormatter" width="200px" :sortable="true">

      </el-table-column>
      <el-table-column label="广告费" align="center" prop="adFee" :sortable="true">
        <template slot-scope="scope">
          {{(scope.row.adFee / scope.row.exchangeRate ).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="ROI" align="center" :sort-method="sortRoi"  :sortable="true">
        <template slot-scope="scope">
          {{scope.row.adFee==0?'NaN':(scope.row.orderAmount/ scope.row.adFee ).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="平均客单价" align="center" :sort-method="sortUnit" :sortable="true" >
        <template slot-scope="scope">
          {{(scope.row.orderAmount/ scope.row.orderTotal / scope.row.exchangeRate ).toFixed(2) }}
        </template>
      </el-table-column>


      <el-table-column label="广告占比" align="center" :sort-method="sortAdRate" :sortable="true">
        <template slot-scope="scope">
          {{(scope.row.adFee/ allAdFee *100).toFixed(2) }}%
        </template>
      </el-table-column>
      <el-table-column label="销售占比" align="center" :sort-method="sortSaleRate" :sortable="true">
        <template slot-scope="scope">
          {{(scope.row.orderAmount/ allTotalAmount *100).toFixed(2) }}%
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import { listShop,listPlatform} from "@/api/shop/shop";
import {listRegionDaily} from "@/api/report/goods_sale";

import {listRegion} from "@/api/shop/region";

import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "RegionSaleGoodsDaily",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 店铺表格数据
      dailyList: [],
      regionList: [],
      platformList: [],
      userList: [],
      groupList: [],
      shopList: [],
      categoryTree: [],
      allTotalAmount: 0,
      allAdFee: 0,
      // 查询参数
      queryParams: {
        dates:[]
      },
      detailOpen:false,
      // 表单参数
      form: {
        itemList:[]
      },
    };
  },
  created() {
    this.queryParams.dates[0] = this.getYesterday()
    this.queryParams.dates[1] = this.getYesterday()
    listRegion().then(response => {
      this.regionList = response.rows;
      this.queryParams.regionId = response.rows[0].id
      this.getList();
    });
  },
  methods: {
    sortSaleRate(a,b){
      return (a.orderAmount / this.allTotalAmount) - (b.orderAmount / this.allTotalAmount)
    },sortAdRate(a,b){
      return (a.adFee / this.allAdFee) - (b.adFee / this.allAdFee)
    },sortUnit(a,b){
      return (a.orderAmount ==0?0: a.orderAmount / a.orderTotal) - (b.orderAmount==0?0:b.orderAmount / b.orderTotal)
    },
    sortRoi(a,b){
      return (a.adFee==0?0:a.orderAmount / a.adFee) - (b.adFee==0?0:b.orderAmount / b.adFee)
    },
    customSummaryMethod({columns, data}) {
      const totals = [];

      const totalAmount = data.reduce((sum, item) => sum + (item.orderAmount/((item.exchangeRate)?item.exchangeRate:1)), 0);

      const orderTotal = data.reduce((sum, item) => sum + item.orderTotal, 0);
      const adFee = data.reduce((sum, item) => sum + (item.adFee/item.exchangeRate), 0);
      const adClick = data.reduce((sum, item) => sum + item.adClick, 0);
      // 遍历每一列
      columns.forEach((column, index) => {
        if (index === 0) {
          totals[index] = '汇总'; // 第一列显示为 "Total"
        } if (index === 3) {
          totals[index] = totalAmount.toFixed(2)
        } else if (index === 4 || index === 5|| index === 6) {
          // 对数量和价格列进行求和
          const sum = data.reduce((prev, current) => {
            return prev + current[column.property];
          }, 0);
          totals[index] = sum;
        }if (index === 7) {
          totals[index] = adFee.toFixed(2)
        } else if (index === 8 ) {
          totals[index] = (totalAmount / adFee).toFixed(2)
        }else if(index === 9){
          totals[index] = this.amountFormatter(null,null,totalAmount / orderTotal)
        }
      });
      return totals;
    },
    getYesterday() {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      return this.formatDate(yesterday);
    },
    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    amountFormatter(row, column, cellValue, index) {
      return parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listRegionDaily(this.queryParams).then(response => {
        this.allTotalAmount = response.rows.reduce((sum, item) => sum + item.orderAmount, 0);
        this.allAdFee = response.rows.reduce((sum, item) => sum + item.adFee, 0);


        console.log("====total====",this.allTotalAmount)
        this.dailyList = response.rows.filter(x=>x.orderTotal > 0);
        this.loading = false;
      });
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
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

  }
};
</script>
