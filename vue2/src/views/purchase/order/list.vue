<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="供应商" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          filterable
          @change="handleQuery"
          placeholder="请选择供应商">
          <el-option
            v-for="item in supplierList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <!-- <el-input
          v-model="queryParams.contactId"
          placeholder="请输入供应商id"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
      </el-form-item>
      <el-form-item label="采购单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入采购单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下单日期" prop="orderDate">
        <el-date-picker
            clearable
          v-model="queryParams.orderDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择下单日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="下单人" prop="createBy">
        <el-select
          v-model="queryParams.createBy"
          filterable
          @change="handleQuery"
          placeholder="请选择下单人">
          <el-option
            v-for="item in userList"
            :key="item.nickName"
            :label="item.nickName"
            :value="item.nickName">
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

        >创建采购订单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['purchase:purchaseOrder:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purchaseOrderList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="center" prop="id" width="60px"/>
      <el-table-column label="供应商" align="center" prop="supplierId" >
        <template slot-scope="scope">
          <span>{{ supplierList.find(x=>x.id == scope.row.supplierId)?supplierList.find(x=>x.id == scope.row.supplierId).name :'' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单号" align="center" prop="orderNum" />
      <el-table-column label="下单日期" align="center" prop="orderDate" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.orderDate }}</span>
        </template>
      </el-table-column>

      <el-table-column label="总金额" align="center" prop="orderAmount" />
      <el-table-column label="下单人" align="center" prop="createBy" />
      <el-table-column label="下单时间" align="center" prop="auditTime" >
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="审核人" align="center" prop="auditUser" />-->
<!--      <el-table-column label="审核时间" align="center" prop="auditTime" >-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.auditTime ==0 ? '' : dateToString(scope.row.auditTime) }}-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.status === 0">待审核</el-tag>
          <el-tag type="success" v-if="scope.row.status === 1">已审核</el-tag>
          <el-tag type="warning" v-if="scope.row.status === 101">已确认待供应商发货</el-tag>
          <el-tag v-if="scope.row.status === 102">供应商已发货</el-tag>
          <el-tag type="primary" v-if="scope.row.status === 2">已收货</el-tag>
          <el-tag type="primary" v-if="scope.row.status === 3">已入库</el-tag>
          <el-tag type="warning" v-if="scope.row.status === 99">已取消</el-tag>
        </template>
      </el-table-column>


<!--      <el-table-column label="供应商发货时间" align="center" prop="supplierDeliveryTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.supplierDeliveryTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="收货时间" align="center" prop="receivedTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.receivedTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="入库时间" align="center" prop="stockInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stockInTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入库仓库" align="center" prop="warehouseName" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <i class="el-icon-copy-document tag-copy" v-if="scope.row.remark" :data-clipboard-text="scope.row.remark" @click="copyActiveCode($event,scope.row.remark)" ></i>
          <span>{{scope.row.remark}}</span>

        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-col :span="24">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row)"
            >详情</el-button>
          </el-col>
          <el-col :span="24">
            <el-button
              v-if="scope.row.status === 0"
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleUpdateStatus(scope.row,'audit')"

            >审核</el-button>
          </el-col>
<!--          <el-col :span="24">-->
<!--            <el-button-->
<!--            v-if="scope.row.status === 1"-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-finished"-->
<!--              @click="handleUpdateStatus(scope.row,'confirm')"-->
<!--              v-hasPermi="['purchase:purchaseOrder:edit']"-->
<!--            >采购确认</el-button>-->
<!--          </el-col>-->
          <el-col :span="24">
            <el-button
              v-if="scope.row.status === 1 || scope.row.status === 0"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleUpdateStatus(scope.row,'cancel')"
            >取消</el-button>
          </el-col>
        <el-col :span="24">
          <el-button
          v-if="scope.row.status === 1"
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="handleUpdateStatus(scope.row,'SupplierShip')"

          >供应商发货</el-button>
        </el-col>
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

    <!-- 添加或修改采购订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="138px">

        <el-form-item label="采购单号" prop="orderNum">
          <el-input v-model="form.orderNum" disabled placeholder="请输入单号" />
        </el-form-item>
        <el-form-item label="下单日期" prop="orderDate">
          <el-date-picker clearable
            v-model="form.orderDate"
            type="date"
            disabled
            value-format="yyyy-MM-dd"
            placeholder="请选择订单日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建时间" prop="orderTime">
          <el-input v-model="form.orderTime" disabled placeholder="请输入订单创建时间" />
        </el-form-item>
        <el-form-item label="采购金额" prop="orderAmount">
          <el-input v-model="form.orderAmount" disabled placeholder="请输入订单总金额" />
        </el-form-item>

        <el-form-item label="审核人" prop="auditUser" >
          <el-input v-model="form.auditUser" placeholder="请输入采购单审核人" :disabled=" form.optionType !== 'audit' " />
        </el-form-item>
        <el-form-item label="审核时间" prop="auditTime" v-if="form.optionType !== 'audit'">
          <el-input v-model="form.auditTime" placeholder="请输入审核时间" :disabled=" form.optionType !== 'audit' " />
        </el-form-item>

        <el-form-item label="供应商发货时间" prop="supplierDeliveryTime" v-if="form.optionType === 'SupplierShip'">
          <el-date-picker clearable
            v-model="form.supplierDeliveryTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择供应商发货时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="物流公司"  v-if="form.optionType === 'SupplierShip'">
<!--          <el-input v-model="form.shipCompany" placeholder="请输入物流公司" />-->
          <el-select v-model="form.shipCompany" filterable r placeholder="选择快递公司" >
            <el-option v-for="item in logisticsList" :key="item.id" :label="item.name" :value="item.name">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px" >{{item.number}}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物流单号" v-if="form.optionType === 'SupplierShip'">
          <el-input v-model="form.shipNo" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流费用" v-if="form.optionType === 'SupplierShip'">
          <el-input v-model="form.shipCost" placeholder="请输入物流费用" @input="handleShipCostInput" />
        </el-form-item>
        <el-form-item label="采购金额(不含运费)" v-if="form.optionType === 'confirm'">
          <el-input v-model="form.totalAmount" placeholder="请输入采购金额(不含运费)" @input="handleTotalAmountInput" />
        </el-form-item>
        <el-form-item label="采购单确认人" v-if="form.optionType === 'confirm'">
          <el-input v-model="form.confirmUser" placeholder="请输入采购单确认人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注"  />
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
import { listPurchaseOrder, getPurchaseOrder, delPurchaseOrder, addPurchaseOrder, updatePurchaseOrder } from "@/api/purchase/purchaseOrder";
import {listAllSupplier, listSupplier} from "@/api/goods/supplier";
import { listLogistics } from "@/api/purchase/logistics";
import userInfo from '@/views/system/user/profile/userInfo.vue'
import Clipboard from "clipboard";
import {getUserProfile, listUser} from '@/api/system/user'
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";
import {copyActiveCode} from "@/utils/zhijian";


export default {
  name: "PurchaseOrder",
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
      // 采购订单表格数据
      purchaseOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contactId: null,
        orderNo: null,
        orderDate: null,
        orderTime: null,
        orderAmount: null,
        status: null,
        createBy: null,
        auditUser: null,
        auditTime: null
      },
      // 表单参数
      form: {
        id:null,
        orderNo:null,
        optionType:null,
        auditUser:null,
        remark:null,
        orderAmount:null,
        shipCost:0.0,
        supplierDeliveryTime:null,
        totalAmount:null
      },

      // 表单校验
      rules: {
      },
      supplierLoading:false,
      supplierList:[],
      userList:[],
      logisticsList:[],
    };
  },
  created() {
    listAllSupplier({}).then(response => {
      this.supplierList = response.rows;
      // this.supplierLoading = false;
      this.getList();
    });
    listUser({pageIndex:1,pageSize:1000,userType:'00'}).then(response => {
        this.userList = response.rows;
      }
    );
    getUserProfile().then(resp=>{
      this.form.auditUser = resp.data.nickName
    })

  },
  methods: {
    copyActiveCode,
    dateToString(timespan){
      var date = new Date(timespan * 1000);
      return date.toLocaleString();
    },
    // searchSupplier(query){
    //   this.supplierLoading = true;
    //   const qw = {
    //     name:query
    //   }
    //   listSupplier(qw).then(response => {
    //     this.supplierList = response.rows;
    //     this.supplierLoading = false;
    //   });
    // },
    /** 查询采购订单列表 */
    getList() {
      this.loading = true;
      listPurchaseOrder(this.queryParams).then(response => {
        this.purchaseOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 详情
    handleDetail(row){
      // this.$router.push('/scm/purchase/order/detail',{id:row.id});
      this.$router.push({path :"purchase_order_detail", query: { id: row.id }});
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
        contactId: null,
        orderNo: null,
        orderDate: null,
        orderTime: null,
        orderAmount: null,
        remark: null,
        status: null,
        auditUser: null,
        auditTime: null,
        supplierConfirmTime: null,
        supplierDeliveryTime: null,
        receivedTime: null,
        stockInTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.$router.push('purchase_order_create');
    },
    handleUpdateStatus(row,optionType){
      this.form.id = row.id
      this.form.orderNum = row.orderNum
      this.form.orderDate = row.orderDate
      var date = new Date(row.orderTime * 1000);
      this.form.orderTime = date.toLocaleString();
      this.form.orderAmount = row.orderAmount
      this.form.remark = row.remark

      this.form.auditUser = row.auditUser
      this.form.auditTime = this.dateToString(row.auditTime)



      if ('audit' === optionType){
        this.form.optionType = 'audit'
        this.title = "审核采购订单";
        this.open = true;
      }else if (optionType === 'SupplierShip'){
        this.form.optionType = 'SupplierShip'
        listLogistics({}).then(resp=>{
          this.logisticsList = resp.rows
          this.title = "供应商发货";
          this.form.supplierDeliveryTime = new Date()
          this.form.shipCost = '0'
          this.open = true;
        })

      }
      else if (optionType === 'confirm'){
        this.form.optionType = 'confirm'
        this.form.totalAmount = row.orderAmount != null ? row.orderAmount.toString() : null
        this.title = "采购确认";
        this.open = true;
      }
      else if (optionType === 'cancel'){
        this.form.optionType = 'cancel'
        this.form.totalAmount = row.orderAmount
        this.title = "取消采购";
        this.open = true;
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将价格字段从字符串转换回数字类型
          this.form.shipCost = stringToNumber(this.form.shipCost);
          this.form.totalAmount = stringToNumber(this.form.totalAmount);
          if(this.form.optionType === 'audit'){
            if(!this.form.auditUser){
              this.$message.error("请填写审核人")
              return
            }
          }else if(this.form.optionType === 'SupplierShip'){
            if(!this.form.supplierDeliveryTime){
              this.$message.error("请填写供应商发货日期")
              return
            }
            if(!this.form.shipCompany){
              this.$message.error("请填写供应商发货物流公司")
              return
            }
            if(!this.form.shipNo){
              this.$message.error("请填写供应商发货物流单号")
              return
            }
            if(this.form.shipCost === null || this.form.shipCost === undefined){
              this.$message.error("请填写供应商发货物流费用")
              return
            }

          }else if(this.form.optionType === 'confirm'){
            if(!this.form.totalAmount){
              this.$message.error("请填写采购实际金额")
              return
            }
            if(!this.form.confirmUser){
              this.$message.error("请填写采购单确认人")
              return
            }

          }else if(this.form.optionType === 'cancel'){
            if(!this.form.remark){
              this.$message.error("请填写备注")
              return
            }
          }
          updatePurchaseOrder(this.form).then(response => {
            if(this.form.optionType === 'audit'){
              if(response.code===0){
                this.$message.error("审核失败！"+ response.msg)
              }
              else if(response.code === 200) {
                this.$modal.msgSuccess("审核成功");
                this.open = false;
                this.getList();
              }else{
                this.$message.error("失败！"+ response.msg)
              }
            }else if(this.form.optionType === 'SupplierShip'){
              console.log('=====发货结果======',response)
              if(response.code === 200) {
                this.$modal.msgSuccess("发货成功");
                this.open = false;
                this.getList();
              }else{
                this.$message.error("发货失败！"+ response.msg)
              }
            }
            else if(this.form.optionType === 'confirm'){
              console.log('=====确认结果======',response)
              if(response.code === 200) {
                this.$modal.msgSuccess("确认成功");
                this.open = false;
                this.getList();
              }else{
                this.$message.error("确认失败！"+ response.msg)
              }
            }else if(this.form.optionType === 'cancel'){
              console.log('=====取消结果======',response)
              if(response.code === 200) {
                this.$modal.msgSuccess("取消成功");
                this.open = false;
                this.getList();
              }else{
                this.$message.error("取消成功！"+ response.msg)
              }
            }

          });
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/purchaseOrder/export', {
        ...this.queryParams
      }, `purchaseOrder_${new Date().getTime()}.xlsx`)
    },
    handleShipCostInput() {
      this.form.shipCost = limitDecimalLength(this.form.shipCost);
    },
    handleTotalAmountInput() {
      this.form.totalAmount = limitDecimalLength(this.form.totalAmount);
    }
  }
};
</script>
