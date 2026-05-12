<template>
  <el-dialog :visible.sync="dialogVisible" title="选择回收抵扣">
    <el-table v-loading="loading" :data="dataList" >
      <el-table-column label="回收日期" align="center" prop="recoveryDate" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recoveryDate,'{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="回收单号" align="left" prop="recoveryNo"></el-table-column>
      <el-table-column label="客户姓名" align="left" prop="customerName"/>
      <el-table-column label="回收总额" align="center" prop="totalAmount" :formatter="amountFormatter"/>
      <el-table-column label="剩余金额" align="center" prop="remainingAmount" :formatter="amountFormatter"/>

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


<!--    <span>点击按钮向主页面传递数据</span>-->
<!--    <el-button @click="sendDataToParent">发送数据</el-button>-->
  </el-dialog>
</template>
<script>


import { amountFormatter } from '@/utils/zhijian'
import {listDiscount} from "@/api/marketing/discount";
import {listRecovery, listUserDeduction} from "../../api/afterSale/recovery";


export default {
  name: 'PopupRecycleList',
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
      // 数据
      dataList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: null,
      },
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    amountFormatter,
    // 打开弹出框
    openDialog(customerId) {
      this.queryParams.customerId = customerId;
      this.getList()
      this.dialogVisible = true;
    },
    // 发送数据到父组件
    sendDataToParent(row) {
      if(row.id){
        this.$emit('data-from-select', row);
      }
      this.dialogVisible = false; // 关闭弹出框
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询商品规格库存管理列表 */
    getList() {
      this.loading = true;
      listUserDeduction(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>
<style scoped lang="scss">

</style>
