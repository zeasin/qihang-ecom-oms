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
<!--      <el-form-item label="店铺" prop="shopId">-->
<!--        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">-->
<!--         <el-option-->
<!--            v-for="item in shopList"-->
<!--            :key="item.id"-->
<!--            :label="item.name"-->
<!--            :value="item.id">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
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
        >添加会员</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="memberList" >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="姓名" align="left" prop="name" width="180px"></el-table-column>
      <el-table-column label="手机号" align="left" prop="phone" />
      <el-table-column label="平台id" align="left" prop="platformUserId" />
      <el-table-column label="平台账号" align="left" prop="platformAccount" />
      <el-table-column label="省" align="left" prop="province" />
      <el-table-column label="市" align="left" prop="city" />
      <el-table-column label="区" align="left" prop="county" />
      <el-table-column label="街道" align="center" prop="town" />
      <el-table-column label="详细地址" align="center" prop="address" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
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
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="省市区" prop="provinces">
          <el-cascader style="width:250px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="平台UserId" prop="platformUserId">
          <el-input v-model="form.platformUserId" placeholder="请输入平台UserId" />
        </el-form-item>
        <el-form-item label="平台Account" prop="platformAccount">
          <el-input v-model="form.platformAccount" placeholder="请输入平台Account" />
        </el-form-item>
        <el-form-item label="平台Openid" prop="platformOpenid">
          <el-input v-model="form.platformOpenid" placeholder="请输入平台Openid" />
        </el-form-item>
        <el-form-item label="订单备注" prop="remark">
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
import {addShopMember, listShopMember} from "@/api/marketing/member";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import {updateShopMember} from "@/api/marketing/member";
export default {
  name: "GoodsList",
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
        saleState: '1'
      },
      // 表单参数
      form: {
        id: null,
        name: null,
        phone: null,
        platformUserId: null,
        platformAccount: null,
        platformOpenid: null,
        address: null,
        status: null,
        remark: null,
        provinces: []
      },

      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "change" }],
        phone: [{ required: true, message: "不能为空", trigger: "change" }],
        provinces: [{ required: true, message: "不能为空", trigger: "change" }],
        address: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
      this.getList();
  },
  methods: {

    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listShopMember(this.queryParams).then(response => {
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
        phone: null,
        platformUserId: null,
        platformAccount: null,
        platformOpenid: null,
        address: null,
        status: null,
        remark: null,
        provinces: []
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
    handleAdd() {
      this.reset();
      this.open = true;
    },
    handleUpdate(row) {
      this.reset();
      this.form = row;
      console.log(this.form);
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
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.county = this.form.provinces[2]
          if (this.form.id) {
            updateShopMember(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("会员编辑成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }

            });
          } else {
            addShopMember(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("会员添加成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            })
          }
        }
      });
    },

  }
};
</script>
