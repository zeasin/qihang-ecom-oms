<template>
  <el-dialog :visible.sync="dialogVisible" title="选择会员">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="120px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button round icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center"/>-->
<!--      <el-table-column label="ID" align="center" prop="id" width="66"/>-->
      <el-table-column label="姓名" align="left" prop="name" width="180px"></el-table-column>
      <el-table-column label="手机号" align="left" prop="mobile" />
      <el-table-column label="工号" align="left" prop="employeeNo" />
      <el-table-column label="提成比例" align="left" prop="commissionRate" />
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

import {listShop} from "@/api/shop/shop";
import {listSalesPeople} from "@/api/order/salespeople";


export default {
  name: 'PopupSalesPeopleList',
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
      // 数据
      memberList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        phone: null,

      },
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
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
    sendDataToParent(row) {
      if(row && row.id) {
        // 有传数据过啦
        this.$emit('data-from-select', row); // 通过自定义事件传递数据
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
      listSalesPeople(this.queryParams).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
  }
}
</script>
<style scoped lang="scss">

</style>
