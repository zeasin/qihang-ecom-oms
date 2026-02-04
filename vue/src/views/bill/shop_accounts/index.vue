<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" filterable  placeholder="搜索店铺" >
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 1">淘宝天猫</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 2">京东</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 4">抖店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">其他</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" filterable  placeholder="" >
          <el-option label="支出" value="1"></el-option>
          <el-option label="收入" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用途" prop="usageScenario">
        <el-select v-model="queryParams.usageScenario" filterable  placeholder="" >
          <el-option v-for="item in usageScenarioList" :key="item.value" :label="item.label" :value="item.value">
          <span style="float: left">{{ item.label }}</span>
        </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="交易日期" prop="date">
        <el-date-picker clearable
                        v-model="queryParams.date"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择交易日期">
        </el-date-picker>
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
          v-hasPermi="['shop:shop:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['shop:shop:edit']"
        >修改</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange" show-summary :summary-method="customSummaryMethod">
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="店铺ID" align="center" prop="id" />-->
      <el-table-column label="日期" align="center" prop="date" />


      <el-table-column label="交易类型" align="center" prop="type" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type===1">支出</el-tag>
          <el-tag v-if="scope.row.type===2">收入</el-tag>
        </template>
      </el-table-column>

       <el-table-column label="金额" align="center" prop="amount" :formatter="amountFormatter"/>
       <el-table-column label="用途" align="left" prop="usageScenario" />
      <el-table-column label="店铺" align="left" prop="shopId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shopId>0">{{ shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''  }}</el-tag>
          <el-tag v-else>公共</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="left" prop="remark" />
      <el-table-column label="交易时间" align="center" prop="tradeTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-row>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['shop:shop:remove']"
          >删除</el-button>
          </el-row>
          <el-button
            v-if="scope.row.type===3"
            type="success"
            plain
            icon="el-icon-refresh"
            size="mini"
            @click="handleUpdateToken(scope.row)"
          >更新AccessToken</el-button>
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

    <!-- 添加或修改店铺对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="店铺" prop="shopId">
          <el-select v-model="form.shopId" filterable  placeholder="搜索店铺" >
            <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">其他</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易时间" prop="tradeTime">
          <el-date-picker clearable
                          v-model="form.tradeTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择交易时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="交易类型" prop="type">
          <el-select v-model="form.type" placeholder="交易类型">
            <el-option label="支出" value="1"></el-option>
            <el-option label="收入" value="2"></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="金额" prop="amount">
          <el-input type="number" v-model="form.amount" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="用途" prop="usageScenario">
<!--          <el-input v-model="form.usageScenario" placeholder="请输入用途" />-->
          <el-select v-model="form.usageScenario" placeholder="交易类型">
            <el-option v-for="item in usageScenarioList" :key="item.value" :label="item.label" :value="item.value">
              <span style="float: left">{{ item.label }}</span>
            </el-option>
<!--            <el-option label="代发货款" value="代发货款"></el-option>-->
<!--            <el-option label="采购货款" value="采购货款"></el-option>-->
<!--            <el-option label="快递费用" value="快递费用"></el-option>-->
<!--            <el-option label="推广费" value="推广费"></el-option>-->
<!--            <el-option label="消费者补偿" value="消费者补偿"></el-option>-->
<!--            <el-option label="包装耗材" value="包装耗材"></el-option>-->
<!--            <el-option label="提现收入" value="提现收入"></el-option>-->

<!--            <el-option label="店铺保证金" value="店铺保证金"></el-option>-->

<!--            <el-option label="店铺扣款" value="店铺扣款"></el-option>-->
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入描述" />
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
import { listShop } from "@/api/shop/shop";
import {getUserProfile} from "@/api/system/user";

import {addAccounts, delAccounts, getAccounts, listShopAccounts, updateAccounts} from "@/api/financial/shop_accounts";
import {amountFormatter} from "@/utils/zhijian";

export default {
  name: "ShopAccounts",
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
      userId: 0,
      // 店铺表格数据
      shopList: [],
      usageScenarioList: [{
        label:'代发货款',
        value:'代发货款'
      },{
        label:'采购货款',
        value:'采购货款'
      },{
        label:'快递费用',
        value:'快递费用'
      },{
        label:'推广费',
        value:'推广费'
      },{
        label:'消费者补偿',
        value:'消费者补偿'
      },{
        label:'包装耗材',
        value:'包装耗材'
      },{
        label:'提现收入',
        value:'提现收入'
      },{
        label:'店铺扣款',
        value:'店铺扣款'
      },{
        label:'店铺保证金',
        value:'店铺保证金'
      }
      ],
      dataList: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      apiOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null
      },
      // 表单参数
      form: {
        type:'1',
        tradeTime:null
      },
      tokenForm:{

      },
      // 表单校验
      rules: {
        // shopId: [{ required: true, message: "店铺不能为空", trigger: "blur" }],
        tradeTime: [{ required: true, message: "不能为空", trigger: "blur" }],
        type: [{ required: true, message: "不能为空", trigger: "blur" }],
        amount: [{ required: true, message: "不能为空", trigger: "change" }],
        usageScenario: [{ required: true, message: "不能为空", trigger: "change" }],
        remark: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    listShop({}).then(response => {
      this.shopList = response.rows;
      this.getList();
    });

  },
  methods: {
    amountFormatter,
    customSummaryMethod({columns, data}) {
      const totals = [];
      // 遍历每一列
      columns.forEach((column, index) => {
        if (index === 0) {
          totals[index] = '汇总'; // 第一列显示为 "Total"
        }  else if (index === 2) {
          // 对数量和价格列进行求和
          const sum = data.reduce((prev, current) => {
            return prev + current[column.property];
          }, 0);
          totals[index] = sum;
        }
      });
      return totals;
    },
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      listShopAccounts(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.apiOpen = false;
      this.authOpen = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null,
        type: null,
        tradeTime: null,
        amount: null,
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加店铺账目";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccounts(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改店铺账目";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccounts(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.apiOpen = false
              this.getList();
            });
          } else {
            addAccounts(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除店铺编号为"' + ids + '"的数据项？').then(function() {
        return delAccounts(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },


  }
};
</script>
