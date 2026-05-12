<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">

      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="生效中" value="1" ></el-option>
          <el-option label="待审核" value="0"></el-option>
          <el-option label="审核拒绝" value="2"></el-option>
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
        >添加折扣</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="center" prop="id" width="80"/>
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
      <el-table-column label="折扣范围" align="center" prop="applyScope" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.applyScope===1">全部</el-tag>
          <el-tag size="small" v-if="scope.row.applyScope===2">指定商户</el-tag>
          <el-tag size="small" v-if="scope.row.applyScope===3">指定门店</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="sourceType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.sourceType===1">总部创建</el-tag>
          <el-tag size="small" v-if="scope.row.sourceType===2">商户创建</el-tag>
          <el-tag size="small" v-if="scope.row.sourceType===3">门店创建</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最少订单金额" align="center" prop="minOrderAmount" :formatter="amountFormatter"/>
      <el-table-column label="发行数量" align="center" prop="totalQuota" />
      <el-table-column label="已使用" align="center" prop="usedQuota" />

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0">待审核</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1">生效中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2">审核拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="shipType" >
        <template slot-scope="scope">
          {{parseTime(scope.row.createdTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="isAdmin">
        <template slot-scope="scope">
            <el-button v-if="isAdmin"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          <el-button v-if="isAdmin && scope.row.status===0"
            size="mini"
            type="text"
            icon="el-icon-audit"
            @click="handleAudit(scope.row)"
          >审核</el-button>

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



    <!-- 添加或修改 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="折扣名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="折扣名称"  style="width: 460px;"/>
        </el-form-item>
        <el-form-item label="折扣类型" prop="discountType">
          <el-select v-model="form.discountType" style="width: 230px;"  placeholder="折扣类型">
            <el-option label="百分比折扣" :value="1"></el-option>
            <el-option label="固定金额折扣" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="折扣" prop="discountValue">
          <el-input type="number" v-model.number="form.discountValue" placeholder="折扣值，百分比时如10表示10%，固定金额时如50.00表示50元" @input="limitDecimalLength"  style="width: 460px;"/>
        </el-form-item>
        <el-form-item label="发行数量" prop="totalQuota">
          <el-input type="number" v-model="form.totalQuota" placeholder="发行数量" style="width: 230px;"/>
        </el-form-item>
        <el-form-item label="最小订单金额" prop="minOrderAmount">
          <el-input type="number" v-model="form.minOrderAmount" placeholder="最小订单金额，0不限" style="width: 230px;" @input="handleMinOrderAmountInput"/>
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" value-format="yyyy-MM-dd HH:mm:ss"
                          style="width: 230px;"   type="datetime" placeholder="生效开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" value-format="yyyy-MM-dd HH:mm:ss"
                          style="width: 230px;"  type="datetime" placeholder="生效结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="备注" style="width: 460px;"/>
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
import {getToken} from "@/utils/auth";
import {addDiscount, auditDiscount, delDiscount, listDiscount} from "@/api/marketing/discount";
import {getUserProfile} from "@/api/system/user";
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";

export default {
  name: "DiscountIndex",
  data() {
    return {
      headers: { 'Authorization': 'Bearer ' + getToken() },
      // 遮罩层
      loading: true,
      isAdmin: false,
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
      goodsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        status: null,
        number: null,
      },
      // 表单参数
      form: {
        id: null,
        ruleName: null,
        startTime:null,
        endTime:null,
        discountType:null,
        discountValue:null,
        totalQuota:null,
        minOrderAmount:null,
        remark:null,
      },
      // 表单校验
      rules: {
        ruleName: [{ required: true, message: "不能为空", trigger: "change" }],
        discountType: [{ required: true, message: "不能为空", trigger: "change" }],
        discountValue: [{ required: true, message: "不能为空", trigger: "blur" }],
        totalQuota: [{ required: true, message: "不能为空", trigger: "blur" }],
        minOrderAmount: [{ required: true, message: "不能为空", trigger: "blur" }],
        startTime: [{ required: true, message: "不能为空", trigger: "change" }],
        endTime: [{ required: true, message: "不能为空", trigger: "change" }],

      }
    };
  },
  mounted() {
    getUserProfile().then(res=> {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isAdmin = true;
      }
    })
  },
  created() {
    this.form.priceDate = new Date();
    this.getList();
  },
  methods: {
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listDiscount(this.queryParams).then(response => {
        this.goodsList = response.rows;
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
        ruleName: null,
        startTime:null,
        endTime:null,
        discountType:null,
        discountValue:null,
        totalQuota:null,
        minOrderAmount:null,
        remark:null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增折扣";
    },
    handleAudit(row){
      this.$modal.confirm('是否确认审核通过"' + row.id + '"的折扣吗？').then(function () {
        return auditDiscount(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审核成功");
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function () {
        return delDiscount(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将价格字段从字符串转换回数字类型
          this.form.discountValue = stringToNumber(this.form.discountValue);
          this.form.minOrderAmount = stringToNumber(this.form.minOrderAmount);
          addDiscount(this.form).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg)
            }
          });

        }
      });
    },
    limitDecimalLength() {
      this.form.discountValue = limitDecimalLength(this.form.discountValue);
    },
    handleMinOrderAmountInput() {
      this.form.minOrderAmount = limitDecimalLength(this.form.minOrderAmount);
    },

  }
};
</script>
