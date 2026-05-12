<template>
  <el-dialog :visible.sync="dialogVisible" title="选择优惠折扣">
<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="sendDataToParent"-->
<!--        >确认选中</el-button>-->
<!--      </el-col>-->

<!--      &lt;!&ndash;      <right-toolbar showSearch.sync="true" @queryTable="getList"></right-toolbar>&ndash;&gt;-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="discountList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center"/>-->
<!--      <el-table-column label="ID" align="center" prop="id" width="80"/>-->
      <el-table-column label="折扣名称" align="left" prop="ruleName" width="180" ></el-table-column>
      <el-table-column label="生效开始时间" align="left" prop="startTime" width="180" >
        <template slot-scope="scope">
          {{parseTime(scope.row.startTime)}}
        </template>
      </el-table-column>
      <el-table-column label="生效结束时间" align="left" prop="endTime" width="180" >
        <template slot-scope="scope">
          {{parseTime(scope.row.endTime)}}
        </template>
      </el-table-column>
      <el-table-column label="折扣类型" align="center" prop="discountType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.discountType===2">固定金额折扣</el-tag>
          <el-tag size="small" v-if="scope.row.discountType===1">百分比折扣</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="折扣值" align="center" prop="discountValue" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.discountType===2">{{scope.row.discountValue}}</el-tag>
          <el-tag size="small" v-if="scope.row.discountType===1">{{scope.row.discountValue}}%</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最少订单金额" align="center" prop="minOrderAmount" :formatter="amountFormatter"/>
      <el-table-column label="剩余可用" align="center" prop="totalQuota" >
        <template slot-scope="scope">
          {{scope.row.totalQuota - scope.row.usedQuota}}
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


<!--    <span>点击按钮向主页面传递数据</span>-->
<!--    <el-button @click="sendDataToParent">发送数据</el-button>-->
  </el-dialog>
</template>
<script>


import { amountFormatter } from '@/utils/zhijian'
import {listDiscount} from "@/api/marketing/discount";


export default {
  name: 'PopupDiscountList',
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
      discountList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // status: 1,
      },
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    amountFormatter,
    // 打开弹出框
    openDialog() {
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
      this.loading = true;
      listDiscount(this.queryParams).then(response => {
        this.discountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>
<style scoped lang="scss">

</style>
