<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="回收单号" prop="recoveryNo">
        <el-input
          v-model="queryParams.recoveryNo"
          placeholder="请输入回收单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户姓名" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户手机号" prop="customerPhone">
        <el-input
          v-model="queryParams.customerPhone"
          placeholder="请输入客户手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结算方式" prop="settlementType">
        <el-select v-model="queryParams.settlementType" placeholder="请选择结算方式" clearable @change="handleQuery">
          <el-option label="仅抵扣" value="1" ></el-option>
          <el-option label="仅现金" value="2" ></el-option>
          <el-option label="混合" value="3" ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否结清" prop="settlementStatus">
        <el-select v-model="queryParams.settlementStatus" placeholder="请选择结清状态" clearable @change="handleQuery">
          <el-option label="未结清" value="1" ></el-option>
          <el-option label="已结清" value="2" ></el-option>
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
        >新增回收</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="回收日期" align="center" prop="recoveryDate" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recoveryDate,'{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="回收单号" align="left" prop="refundNum">
        <template slot-scope="scope">
          {{scope.row.recoveryNo}} <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.recoveryNo" @click="copyActiveCode($event,scope.row.recoveryNo)" ></i>
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="left" prop="orderNum" >
        <template slot-scope="scope">
          {{scope.row.originalOrderNo}} <i class="el-icon-copy-document tag-copy" v-if="scope.row.originalOrderNo" :data-clipboard-text="scope.row.originalOrderNo" @click="copyActiveCode($event,scope.row.originalOrderNo)" ></i>
        </template>
      </el-table-column>
      <el-table-column label="商户" align="left" prop="merchantName" />
      <el-table-column label="店铺" align="left" prop="shopName" />
      <el-table-column label="客户姓名" align="left" prop="customerName"/>
      <el-table-column label="客户手机号" align="left" prop="customerPhone"/>
      <el-table-column label="结算方式" align="center" prop="settlementType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.settlementType === 0">未结算</el-tag>
          <el-tag size="small" v-if="scope.row.settlementType === 1">仅抵扣</el-tag>
          <el-tag size="small" v-if="scope.row.settlementType === 2">仅现金</el-tag>
          <el-tag size="small" v-if="scope.row.settlementType === 3">混合</el-tag>
        </template>
      </el-table-column>
       <el-table-column label="金重(g)" align="left" prop="goldWeight"  />
       <el-table-column label="回收金价" align="center" prop="goldPrice" :formatter="amountFormatter"/><
      <el-table-column label="银重(g)" align="left" prop="silverWeight"  />
       <el-table-column label="回收银价" align="center" prop="silverPrice" :formatter="amountFormatter"/>
       <el-table-column label="回收总额" align="center" prop="totalAmount" :formatter="amountFormatter"/>
       <el-table-column label="现金回收" align="center" prop="cashAmount" :formatter="amountFormatter"/>
       <el-table-column label="抵扣金额" align="center" prop="deductedAmount" :formatter="amountFormatter"/>
       <el-table-column label="剩余金额" align="center" prop="remainingAmount" :formatter="amountFormatter"/>

       <el-table-column label="是否结算" align="center" prop="settlementStatus" >
         <template slot-scope="scope">
           <el-tag size="small" v-if="scope.row.settlementStatus === 1"> 未结算</el-tag>
           <el-tag size="small" v-if="scope.row.settlementStatus === 2"> 已结算</el-tag>

         </template>
       </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" />

      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
            <el-button
              v-if="scope.row.settlementStatus === 1"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >结算</el-button>
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

    <!-- 添加 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="购买订单" prop="originalOrderNo" >
          <el-input
            v-model="form.originalOrderNo" style="width: 460px"
            placeholder="请输入购买订单号"
            size="large">
            <el-button slot="append" icon="el-icon-search" @click="searchMember">选择购买订单</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="客户手机号" prop="customerPhone">
          <el-input
            v-model="form.customerPhone" style="width: 460px"
            placeholder="请输入客户手机号（搜索会员或者录入新会员）"
            size="large" @blur="searchMember"
            @keyup.enter.native="searchMember"
            clearable
          >
            <el-button slot="append" icon="el-icon-search" @click="searchMember">查询</el-button>
          </el-input>
<!--          <el-input v-model="form.customerPhone" placeholder="请输入客户手机号"  style="width: 320px" @keyup.enter.native="searchMember" @blur="searchMember"/>-->
        </el-form-item>
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名"  style="width: 460px"/>
        </el-form-item>
        <el-form-item label="联系地址" prop="customerAddress">
          <el-input v-model="form.customerAddress" placeholder="请输入联系地址" style="width: 460px" />
        </el-form-item>
        <el-form-item label="银重(g)" prop="goldWeight">
          <el-input type="number" v-model.number="form.goldWeight" placeholder="请输入银重(g)" style="width: 460px" />
        </el-form-item>
        <el-form-item label="回收金价" prop="goldPrice">
          <el-input type="number" v-model.number="form.goldPrice" placeholder="请输入回收金价" style="width: 460px" />
        </el-form-item>
        <el-form-item label="银重(g)" prop="silverWeight">
          <el-input type="number" v-model.number="form.silverWeight" placeholder="请输入银重(g)" style="width: 460px"/>
        </el-form-item>
        <el-form-item label="回收银价" prop="silverPrice">
          <el-input type="number" v-model.number="form.silverPrice" placeholder="请输入回收银价" style="width: 460px"/>
        </el-form-item>
<!--        <el-form-item label="结算方式" prop="settlementType">-->
<!--          <el-select v-model="form.settlementType" placeholder="请选择结算方式" >-->
<!--            <el-option label="仅抵扣" value="1" ></el-option>-->
<!--            <el-option label="仅现金" value="2" ></el-option>-->
<!--            <el-option label="混合" value="3" ></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" style="width: 460px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 详情对话框 -->

  </div>
</template>

<script>

import { amountFormatter } from '../../utils/zhijian'
import {copyActiveCode, parseTime} from '@/utils/zhijian'
import {addRecovery, listRecovery} from "@/api/afterSale/recovery";
import {searchShopMember} from "@/api/marketing/member";
export default {
  name: "AfterSale",
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
      // 表格数据
      dataList: [],
      shopList: [],
      warehouseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        recoveryNo: null,
        merchantId: null,
        shopId: null,
        customerName: null,
        customerPhone: null,
        settlementType: null,
        settlementStatus: null,
        status: null,
      },
      // 表单参数
      form: {
        originalOrderId:0,
        originalOrderNo:null,
        customerId:0,
        customerName:null,
        customerPhone:null,
        customerAddress:null,
        goldWeight:null,
        goldPrice:null,
        silverWeight:null,
        silverPrice:null,
        // settlementType:null,
      },
      // 表单校验
      rules: {
        customerName: [{ required: true, message: "请选择类型", trigger: "change" }],
        customerPhone: [{ required: true, message: "不能为空", trigger: "blur" }],
        goldWeight: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        goldPrice: [{ required: true, message: "不能为空", trigger: "blur" }],
        silverWeight: [{ required: true, message: "不能为空", trigger: "change" }],
        silverPrice: [{ required: true, message: "不能为空", trigger: "change" }],
        // settlementType: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    this.getList();
  },
  async mounted() {
  },
  methods: {
    parseTime,
    copyActiveCode,
    amountFormatter,
    /** 查询退换货列表 */
    getList() {
      this.loading = true;
      listRecovery(this.queryParams).then(response => {
        this.dataList = response.rows;
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
        originalOrderNo:null,
        customerId:0,
        customerName:null,
        customerPhone:null,
        customerAddress:null,
        goldWeight:null,
        goldPrice:null,
        silverWeight:null,
        silverPrice:null,
        settlementType:null,
      };

      this.title =''
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
    searchMember(){
      searchShopMember({mobile:this.form.customerPhone}).then(response => {
        if (response.code === 200) {
          if (response.data){
            this.form.customerId=response.data.id;
            this.form.customerName=response.data.name;
            this.form.customerAddress=response.data.province+response.data.city+response.data.county+response.data.address;
          }else {
            this.form.customerId=0;
            this.form.customerName='';
            this.form.customerAddress='';
            this.$modal.msgError('没有找到会员信息')
          }
        }else{
          this.$modal.msgError(response.msg)
        }
      })

    },
    handleAdd(){
      this.reset()
      this.open=true
      this.title="添加回收记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.open=true
      this.title="手动添加补发信息"
      // shipAgainComplete(id).then(response => {
      //   this.$modal.msgSuccess("确认完成");
      //   this.getList()
      // });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addRecovery(this.form).then(res => {
            if (res.code === 200) {
                this.$modal.msgSuccess("添加成功");
                this.open = false;
                this.getList()
            }else{
              this.$modal.msgError(res.msg);
            }
          })
        }
      })
    }
  }
};
</script>
