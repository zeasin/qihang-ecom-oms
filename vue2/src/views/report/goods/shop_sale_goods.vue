<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="Sku编码" prop="skuCode">
      <el-input
        v-model="queryParams.skuCode"
        placeholder="请输入Sku编码"
        clearable
        @keyup.enter.native="handleQuery"
      />
      </el-form-item>
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
      <el-form-item label="平台" prop="platformId">
        <el-select v-model="queryParams.platformId" placeholder="请选择平台" clearable>
          <el-option
            v-for="item in platformList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >
              {{ platformList.find(x=>x.id === item.type) ? platformList.find(x=>x.id === item.type).name : '' }}
            </span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺小组" prop="shopGroupId">
        <el-select v-model="queryParams.shopGroupId" placeholder="请选择店铺小组" clearable>
          <el-option
            v-for="item in groupList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="负责人" prop="manageUserId">
        <el-select v-model="queryParams.manageUserId" placeholder="负责人" clearable>
          <el-option
            v-for="item in userList"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId">
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
      <el-table-column label="总金额" align="center" prop="orderAmount" :formatter="amountFormatter" :sort-method="sortOrderAmount"  :sortable="true" width="200px">
        <template slot-scope="scope">
          {{scope.row.orderAmount}}/ {{scope.row.exchangeRate}} =
          {{amountFormatter(null,null,scope.row.orderAmount / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
       <el-table-column label="刷单数" align="center" prop="falseOrderTotal" :sortable="true"/>
      <el-table-column label="刷单金额" align="center" prop="falseOrderAmount" :formatter="amountFormatter" :sortable="true"/>
      <el-table-column label="真实订单数" align="center" prop="trueOrderTotal" :sortable="true"/>
<!--      <el-table-column label="真实销售金额" align="center" prop="trueOrderAmount" :formatter="amountFormatter" />-->
      <el-table-column label="广告支出" align="center" prop="adFee" :sort-method="sortAdFee" :sortable="true">
        <template slot-scope="scope">
          <!--          {{scope.row.adFee}}/ {{scope.row.exchangeRate}} =-->
          {{amountFormatter(null,null,scope.row.adFee / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
<!--      <el-table-column label="广告点击次数" align="center" prop="adClick" />-->
<!--      <el-table-column label="广告点击成本" align="center" >-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.adFee / scope.row.adClick}}-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="ROI" align="center" :sort-method="sortRoi" :sortable="true">
        <template slot-scope="scope">
          {{scope.row.adFee >0 ? (scope.row.trueOrderAmount / scope.row.adFee).toFixed(2):'NaN'}}
        </template>
      </el-table-column>
      <el-table-column label="平均客单价" align="center" :sort-method="sortUnit" :sortable="true">
        <template slot-scope="scope">
          {{ (scope.row.trueOrderAmount / scope.row.trueOrderTotal).toFixed(2)}}
        </template>
      </el-table-column>
      <el-table-column label="广告占比" align="center" :sort-method="sortAdRate" :sortable="true">
        <template slot-scope="scope">
          {{ (scope.row.adFee / allAdFee*100).toFixed(2)}}%
        </template>
      </el-table-column>
      <el-table-column label="销售占比" align="center" :sort-method="sortSaleRate" :sortable="true">
        <template slot-scope="scope">
          {{(scope.row.trueOrderAmount/ allTotalAmount *100).toFixed(2) }}%
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="left" prop="shopId" >
        <template slot-scope="scope">
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="汇率" align="left" prop="exchangeRate" />
    </el-table>

  </div>
</template>

<script>
import { listShop,listPlatform} from "@/api/shop/shop";
import {listDaily} from "@/api/report/goods_sale";
import { listUser} from "@/api/system/user";
import {listRegion} from "@/api/shop/region";
import {listGroup} from "@/api/shop/group";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "ShopSaleGoodsDaily",
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
      listGroup({verifyUser:1}).then(response => {
        // this.deptList = this.handleTree(response.data, "deptId");
        this.groupList = response.rows;
        // this.categoryTree = this.buildTree(response.data, 0)
        // console.log('======tree======',this.categoryTree)
        listUser().then(response => {
          this.userList = response.rows.filter(item => item.userId >= 100);

        });
      })
    });
    listPlatform().then(res => {
      this.platformList = res.rows;
      listShop().then(response => {
          this.shopList = response.rows
          this.getList();
        });
    })

  },
  methods: {
    sortSaleRate(a,b){
      return (a.trueOrderAmount / this.allTotalAmount) - (b.trueOrderAmount / this.allTotalAmount)
    },sortAdRate(a,b){
      return (a.adFee / this.allAdFee) - (b.adFee / this.allAdFee)
    },sortUnit(a,b){
      return (a.trueOrderTotal ==0?0: a.trueOrderAmount / a.trueOrderTotal) - (b.trueOrderAmount==0?0:b.trueOrderAmount / b.trueOrderTotal)
    },sortRoi(a,b){
      return (a.trueOrderAmount / a.adFee) - (b.trueOrderAmount / b.adFee)
    },sortAdFee(a,b){
      return (a.adFee / a.exchangeRate) - (b.adFee / b.exchangeRate)
    },
    sortOrderAmount(a,b){
      return (a.orderAmount / a.exchangeRate) - (b.orderAmount / b.exchangeRate)
    },
    customSummaryMethod({columns, data}) {
      const totals = [];
      const totalAmount = data.reduce((sum, item) => sum + (item.orderAmount/item.exchangeRate), 0);

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
        }else if(index === 14) {
          totals[index] = this.amountFormatter(null, null, adFee / adClick)
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
      listDaily(this.queryParams).then(response => {
        this.allTotalAmount = response.rows.reduce((sum, item) => sum + item.trueOrderAmount, 0);
        this.allAdFee = response.rows.reduce((sum, item) => sum + item.adFee, 0);

        console.log("====total====",this.allTotalAmount)
        this.dailyList = response.rows;
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
