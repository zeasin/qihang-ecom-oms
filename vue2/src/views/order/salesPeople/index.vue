<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">

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
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId"  placeholder="请选择商户"  @change="merchantChange">
          <el-option
            v-for="item in merchantList"
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
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd"
              >添加销售员</el-button>
            </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="memberList" >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="姓名" align="left" prop="name" width="180px"></el-table-column>
      <el-table-column label="手机号" align="left" prop="mobile" />
      <el-table-column label="工号" align="left" prop="employeeNo" />
      <el-table-column label="商户" align="left" prop="merchantId" width="150px">
        <template slot-scope="scope">
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" >{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="left" prop="merchantId" width="150px">
        <template slot-scope="scope">
<!--          <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>-->
          <el-tag size="small">{{scope.row.shopName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0">禁用</el-tag>
          <el-tag v-if="scope.row.status === 1">启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="orderCreateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleLink(scope.row)"
          >修改</el-button>
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


    <!-- 添加或修改商品管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="工号" prop="employeeNo">
          <el-input v-model="form.employeeNo" placeholder="请输入工号" />
        </el-form-item>

        <el-form-item label="商户" prop="merchantId">
          <el-select v-model="form.merchantId"  placeholder="请选择商户"  @change="merchantChange">
            <el-option
              v-for="item in merchantList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="店铺" prop="shopId" >
          <el-select v-model="form.shopId" filterable placeholder="搜索店铺" >
            <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getToken} from "@/utils/auth";


import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";

import {addSalesPeople, listSalesPeople, updateSalesPeople} from "@/api/order/salespeople";
import {listShop} from "@/api/shop/shop";
import {listMerchant} from "@/api/shop/merchant";
export default {
  name: "SalesPeople",
  components: { Treeselect },
  data() {
    return {
      pcaTextArr,
      importOpen:false,
      headers: { 'Authorization': 'Bearer ' + getToken() },
      // 遮罩层
      loading: true,
      pullLoading: false,
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
      // 商品管理表格数据
      memberList: [],
      merchantList: [],
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        merchantId: null,
        shopId: null
      },
      // 表单参数
      form: {
        id: null,
        name: null,
        mobile: null,
        employeeNo: null,
        merchantId: null,
        shopId: null,
        remark: null,
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "change" }],
        mobile: [{ required: true, message: "不能为空", trigger: "change" }],
        employeeNo: [{ required: true, message: "不能为空", trigger: "change" }],
        merchantId: [{ required: true, message: "不能为空", trigger: "change" }],
        shopId: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    listMerchant({}).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if(resp.rows.length === 1&&resp.rows[0].id>0) {
        this.isMerchant = true;
      }
      listShop({ merchantId: this.queryParams.merchantId }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.getList()
      });

    })
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listSalesPeople(this.queryParams).then(response => {
        this.memberList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        mobile: null,
        employeeNo: null,
        merchantId: null,
        shopId: null,
        remark: null,
      };
      this.resetForm("form");
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
    handleAdd(){
      this.reset();
      this.open = true;
    },
    handleLink(row) {
      this.reset();
      const id = row.id || this.ids
      this.form = row;
      this.form.provinces= []
      if(this.form.province){
        this.form.provinces.push(this.form.province)
      }
      if(this.form.city){
        this.form.provinces.push(this.form.city)
      }
      if(this.form.county){
        this.form.provinces.push(this.form.county)
      }
      this.open = true;
    },
    /** 提交按钮 */
    submitForm() {
      console.log('=====',this.form.provinces)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.id){
            updateSalesPeople(this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          }else {
            addSalesPeople(this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msgSuccess("添加成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          }
        }
      });
    },

  }
};
</script>
