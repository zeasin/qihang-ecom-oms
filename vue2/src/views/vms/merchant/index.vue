<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item :label="$t('system.merchant.name')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('system.merchant.inputName')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('system.merchant.code')" prop="number">
        <el-input
          v-model="queryParams.number"
          :placeholder="$t('system.merchant.inputCode')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="联系人" prop="linkman">
        <el-input
          v-model="queryParams.linkman"
          placeholder="请输入联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="状态" prop="disable">-->
<!--        <el-input-->
<!--          v-model="queryParams.disable"-->
<!--          placeholder="请输入0启用   1禁用"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--&lt;!&ndash;      <el-col :span="1.5">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button&ndash;&gt;-->
<!--&lt;!&ndash;          type="warning"&ndash;&gt;-->
<!--&lt;!&ndash;          plain&ndash;&gt;-->
<!--&lt;!&ndash;          icon="el-icon-download"&ndash;&gt;-->
<!--&lt;!&ndash;          size="mini"&ndash;&gt;-->
<!--&lt;!&ndash;          @click="handleExport"&ndash;&gt;-->
<!--&lt;!&ndash;          v-hasPermi="['scm:supplier:export']"&ndash;&gt;-->
<!--&lt;!&ndash;        >导出</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="merchantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="商户名称" align="left" prop="name" />
      <el-table-column label="商户编码" align="left" prop="number" />

      <el-table-column label="社会信用代码" align="left" prop="usci" />
<!--      <el-table-column label="营业执照" align="center" prop="image" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <image-preview :src="scope.row.bl" :width="50" :height="50"/>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="法人" align="left" prop="faren" />
      <el-table-column label="联系人" align="left" prop="linkMan" />
      <el-table-column label="手机号" align="left" prop="mobile" />
      <el-table-column label="联系地址" align="left" prop="address" />

<!--      <el-table-column label="登录账户" align="left" prop="loginName" />-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['scm:supplier:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['scm:supplier:remove']"-->
<!--          >删除</el-button>-->
<!--          <el-row>-->
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="success"-->
<!--              @click="handleSetLoginName(scope.row)"-->
<!--            >重置登录账号或密码</el-button>-->
<!--          </el-row>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-share"-->
<!--            @click="handleShareSupplier(scope.row)"-->
<!--          >分配给{{$t('system.vendor.title')}}</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改供应商管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="128px">
        <el-form-item label="商户名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商户名称" />
        </el-form-item>
        <el-form-item label="商户编码" prop="number">
          <el-input v-model="form.number" placeholder="请输入商户编码" style="width: 230px" />
        </el-form-item>
        <el-form-item label="社会信用代码" prop="usci">
          <el-input v-model="form.usci" placeholder="请输入社会信用代码" />
        </el-form-item>
<!--        <el-form-item label="营业执照" prop="bl">-->
<!--          <image-upload v-model="form.bl" :limit="1"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="营业执照有效期" prop="orderTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.blPeriod" value-format="yyyy-MM-dd"-->
<!--                          type="date">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item label="法人" prop="faren">
          <el-input v-model="form.faren" placeholder="请输入法人" />
        </el-form-item>
<!--        <el-form-item label="开户银行" prop="bank">-->
<!--          <el-input v-model="form.bank" placeholder="请输入开户银行" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="开户名" prop="bankAccountName">-->
<!--          <el-input v-model="form.bankAccountName" placeholder="请输入开户名" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="银行账号" prop="bankAccount">-->
<!--          <el-input v-model="form.bankAccount" placeholder="请输入开户银行账号" />-->
<!--        </el-form-item>-->
        <el-form-item label="联系人" prop="linkMan">
          <el-input v-model="form.linkMan" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="联系地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入联系地址" />
        </el-form-item>
        <el-form-item label="登录账号" prop="loginName" v-if="!form.id">
          <el-input v-model="form.loginName" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="登录密码" prop="loginPwd" v-if="!form.id">
          <el-input v-model="form.loginPwd" placeholder="请输入登录密码" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="设置登陆账户" :visible.sync="openPwd" width="500px" append-to-body>
      <el-form ref="pwdform" :model="form" :rules="pwdrules" label-width="88px">
        <el-form-item label="登录名" prop="loginName">
          <el-input v-model="form.loginName" placeholder="请输入登录名" />
        </el-form-item>
        <el-form-item label="登录密码" prop="loginPwd">
          <el-input v-model="form.loginPwd" placeholder="请输入登录密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPwdForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'分配'+$t('system.vendor.title')" :visible.sync="shareOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="供应商" prop="supplierIds">
          <el-select v-model="form.supplierIds" placeholder="请选择供应商" clearable multiple >
            <el-option
              v-for="item in supplierList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="shareSupplierSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMerchant, getMerchant} from "@/api/vms/merchant";
import {validatePassword} from "@/utils/validate";


export default {
  name: "Supplier",
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
      // 供应商管理表格数据
      supplierList: [],
      merchantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openPwd: false,
      shareOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        number: null
      },
      // 表单参数
      form: {
        id:null,
        loginName:null,
        loginPwd:null,
        supplierIds:null
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "blur" }],
        number: [{ required: true, message: "不能为空", trigger: "blur" }],
        usci: [{ required: true, message: "不能为空", trigger: "blur" }],
        faren: [{ required: true, message: "不能为空", trigger: "blur" }],
        linkMan: [{ required: true, message: "不能为空", trigger: "blur" }],
        mobile: [{ required: true, message: "不能为空", trigger: "blur" }],
        address: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      pwdrules: {
        loginName: [
          { required: true, message: "登录名不能为空", trigger: "blur" },
          { min: 6, max: 15, message: '登录名长度必须介于 6 和 15 之间', trigger: 'blur' }
        ],
        loginPwd: [
          { required: true, message: "登录密码不能为空", trigger: "blur" },
          { min: 8, max: 32, message: '登录密码长度必须介于 8 和 32 之间', trigger: 'blur' }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询供应商管理列表 */
    getList() {
      this.loading = true;
      listMerchant(this.queryParams).then(response => {
        this.merchantList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openPwd = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        number: null,
        taxRate: null,
        amount: null,
        periodMoney: null,
        difMoney: null,
        beginDate: null,
        remark: null,
        place: null,
        linkMan: null,
        contact: null,
        province: null,
        city: null,
        county: null,
        address: null,
        pinYin: null,
        disable: null,
        isDelete: null,
        purchaserName: null,
        createTime: null
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
      this.title = "添加商户";
    },
    handleSetLoginName(row){
      this.form.id = row.id
      this.form.loginName = row.loginName?row.loginName:''
      this.form.loginPwd = ''
      this.openPwd = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMerchant(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改商户";
      });
    },
    submitPwdForm(){
      this.$refs["pwdform"].validate(valid => {
        if (valid) {
          // 验证密码强度
          // if (!this.validatePassword(this.user.newPassword,this.user.userName)) {
          //   this.$modal.msgError(this.passwordError)
          //   return
          // }
          // const res = validatePassword(this.form.loginPwd,this.form.loginName)
          // if (!res.result) {
          //   this.$modal.msgError(res.msg)
          //   return
          // }
          setMerchantLoginName(this.form).then(response => {
            this.$modal.msgSuccess("设置登陆账户成功");
            this.openPwd = false;
            this.getList();
          });
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMerchant(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMerchant(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除供应商管理编号为"' + ids + '"的数据项？').then(function() {
        return delMerchant(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleShareSupplier(row) {
      this.form.id = row.id
      if (row.supplierIds) {
        let numArray = row.supplierIds.replace(/^,|,$/g, '').split(',').map(Number);
        this.form.supplierIds = numArray
      } else this.form.supplierIds = null
      this.shareOpen = true
    },
    shareSupplierSubmit(){
      console.log("=======分配供应商ID=====",this.form.supplierIds)
      shareSupplier(this.form).then(resp=>{
        this.$modal.msgSuccess("保存成功")
        this.shareOpen =false
        this.getList()
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('scm/supplier/export', {
        ...this.queryParams
      }, `supplier_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
