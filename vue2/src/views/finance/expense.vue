<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请单号" prop="expenseNo">
        <el-input
          v-model="queryParams.expenseNo"
          placeholder="请输入申请单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支出类型" prop="expenseType">
        <el-select v-model="queryParams.expenseType" placeholder="请选择支出类型" clearable @change="handleQuery">
          <el-option label="日常支出" value="1" />
          <el-option label="差旅报销" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="草稿" value="1" />
          <el-option label="待审批" value="2" />
          <el-option label="已审批" value="3" />
          <el-option label="已驳回" value="4" />
          <el-option label="已支付" value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="发生日期" prop="expenseDate">
        <el-date-picker
          v-model="queryParams.expenseDate"
          type="date"
          placeholder="选择费用发生日期"
          @change="handleQuery"
        />
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
        >添加</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请单号" align="left" prop="expenseNo" width="200px" />
      <el-table-column label="支出类型" align="center" prop="expenseType">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.expenseType === 1">日常支出</el-tag>
          <el-tag size="small" v-if="scope.row.expenseType === 2">差旅报销</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请金额" align="center" prop="amount" />
      <el-table-column label="费用发生日期" align="center" prop="expenseDate" width="150px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expenseDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="applicant" />
      <el-table-column label="收款方" align="center" prop="payee" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 1">草稿</el-tag>
          <el-tag size="small" type="info" v-if="scope.row.status === 2">待审批</el-tag>
          <el-tag size="small" type="success" v-if="scope.row.status === 3">已审批</el-tag>
          <el-tag size="small" type="danger" v-if="scope.row.status === 4">已驳回</el-tag>
          <el-tag size="small" type="warning" v-if="scope.row.status === 5">已支付</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="实际支付时间" align="center" prop="paidTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.paidTime ? parseTime(scope.row.paidTime) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 2"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApprove(scope.row)"
          >审核</el-button>
          <el-button
            v-if="scope.row.status === 3"
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handlePay(scope.row)"
          >支付</el-button>
          <el-button
            v-if="scope.row.status === 1 || scope.row.status === 2"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleClose(scope.row)"
          >关闭</el-button>
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
    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="支出类型" prop="expenseType">
          <el-select v-model="form.expenseType" placeholder="请选择支出类型" style="width: 300px">
            <el-option label="日常支出" value="1" />
            <el-option label="差旅报销" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入申请金额" style="width: 300px" />
        </el-form-item>
        <el-form-item label="费用发生日期" prop="expenseDate">
          <el-date-picker
            v-model="form.expenseDate"
            type="date"
            placeholder="选择费用发生日期"
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-input v-model="form.applicant" placeholder="请输入申请人" style="width: 300px" />
        </el-form-item>
        <el-form-item label="收款方" prop="payee">
          <el-input v-model="form.payee" placeholder="请输入收款方" style="width: 300px" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" style="width: 300px" />
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
import { list, add, approve, pay, close } from '@/api/finance/expense'
import { parseTime } from '@/utils/zhijian'

export default {
  name: "Expense",
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        expenseNo: null,
        expenseType: null,
        status: null,
        expenseDate: null,
      },
      // 表单参数
      form: {
        merchantId: null,
        shopId: null,
        expenseType: null,
        amount: null,
        expenseDate: null,
        applicant: null,
        payee: null,
        remark: null
      },
      // 表单校验
      rules: {
        expenseType: [{ required: true, message: "请选择支出类型", trigger: "change" }],
        amount: [{ required: true, message: "请输入申请金额", trigger: "blur" }],
        expenseDate: [{ required: true, message: "请选择费用发生日期", trigger: "change" }],
        applicant: [{ required: true, message: "请输入申请人", trigger: "blur" }],
        payee: [{ required: true, message: "请输入收款方", trigger: "blur" }],
      }
    };
  },
  created() {
    this.form.merchantId = this.$store.state.user.merchantId;
    this.form.shopId = this.$store.state.user.shopId || 0;
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询支出报销列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then(response => {
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
        merchantId: this.$store.state.user.merchantId,
        shopId: this.$store.state.user.shopId || 0,
        expenseType: null,
        amount: null,
        expenseDate: null,
        applicant: null,
        payee: null,
        remark: null
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
    handleAdd() {
      this.reset()
      this.open=true
      this.title="添加支出报销"
    },
    handleApprove(row) {
      this.$modal.confirm('确定要审核通过这条申请吗？').then(() => {
        approve({ id: row.id, approver: this.$store.state.user.username }).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("审核成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      });
    },
    handlePay(row) {
      this.$modal.confirm('确定要支付这条申请吗？').then(() => {
        pay({ id: row.id, payer: this.$store.state.user.username }).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("支付成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      });
    },
    handleClose(row) {
      this.$modal.confirm('确定要关闭这条申请吗？').then(() => {
        close({ id: row.id }).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("关闭成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          add(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.reset();
              this.getList();
            } else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });
    }
  }
};
</script>
