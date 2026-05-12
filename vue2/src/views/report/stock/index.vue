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
      <el-table-column label="商品图片" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.goodsImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150"/>
       <el-table-column label="商品" align="left" prop="goodsName" width="400" />
       <el-table-column label="规格" align="center" prop="aaa" >
         <template slot-scope="scope">
           {{scope.row.skuName}}
         </template>
       </el-table-column>
       <el-table-column label="出库数量" align="center" prop="outQuantity" />
       <el-table-column label="单价" align="center" prop="purPrice" :formatter="amountFormatter"/>

      <el-table-column label="出库总金额" align="center" >
        <template slot-scope="scope">
          {{amountFormatter(null,null,scope.row.outQuantity * scope.row.purPrice)}}
        </template>
      </el-table-column>
      <el-table-column label="出库小组" align="left" prop="shopGroupId" >
        <template slot-scope="scope">
          <el-tag type="info">{{ groupList.find(x=>x.id === scope.row.shopGroupId) ? groupList.find(x=>x.id === scope.row.shopGroupId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="出库小组" align="left" prop="shopGroupId" >
        <template slot-scope="scope">
          <el-tag type="info">{{ groupList.find(x=>x.id === scope.row.shopGroupId) ? groupList.find(x=>x.id === scope.row.shopGroupId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="出库时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import { listShop,listPlatform} from "@/api/shop/shop";
import {listStockReport} from "@/api/wms/stockOut";
import {listGroup} from "@/api/shop/group";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "ShopDaily",
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

    listGroup({}).then(response => {
      this.groupList = response.rows;
    })

    listPlatform().then(res => {
      this.platformList = res.rows;
      listShop().then(response => {
        this.shopList = response.rows
        this.getList();
      });
    })

  },
  methods: {
    customSummaryMethod({columns, data}) {
      const totals = [];
      const totalAmount = data.reduce((sum, item) => sum + (item.purPrice*item.outQuantity), 0);
      const orderTotal = data.reduce((sum, item) => sum + item.orderTotal, 0);
      const adFee = data.reduce((sum, item) => sum + item.adFee, 0);
      const adClick = data.reduce((sum, item) => sum + item.adClick, 0);
      // 遍历每一列
      columns.forEach((column, index) => {
        if (index === 0) {
          totals[index] = '汇总'; // 第一列显示为 "Total"
        } else if(index === 3|| index === 5 ){
          totals[index] == ''
        }else if (index === 4 ) {
          // 对数量和价格列进行求和
          const sum = data.reduce((prev, current) => {
            return prev + current[column.property];
          }, 0);
          totals[index] = sum;
        }else if (index === 6 ) {
          totals[index] = totalAmount.toFixed(2)
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
      listStockReport(this.queryParams).then(response => {
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
