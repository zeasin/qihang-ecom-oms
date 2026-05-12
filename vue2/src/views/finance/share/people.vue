<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="分账方名称" prop="partyName">
        <el-input
          v-model="queryParams.partyName"
          placeholder="请输入分账方名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分账方类型" prop="partyType">
        <el-select v-model="queryParams.partyType" placeholder="请选择分账方类型" clearable>
          <el-option label="内部" :value="0" />
          <el-option label="外部" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="禁用" :value="0" />
          <el-option label="启用" :value="1" />
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
        >添加分账方</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="partyList">
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="分账方名称" align="left" prop="partyName" min-width="150" />
      <el-table-column label="分账方类型" align="center" prop="partyType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.partyType === 0" type="success">内部</el-tag>
          <el-tag v-else type="warning">外部</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="关联名称" align="left" prop="relatedName" min-width="150" />
      <el-table-column label="账号" align="left" prop="accountNo" width="150" />
      <el-table-column label="账户名称" align="left" prop="accountName" width="150" />
      <el-table-column label="银行名称" align="left" prop="bankName" min-width="150" />
      <el-table-column label="联系人" align="center" prop="contactPerson" width="100" />
      <el-table-column label="联系电话" align="center" prop="contactMobile" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" prop="remark" min-width="200" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
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
          >删除</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="分账方名称" prop="partyName">
          <el-input v-model="form.partyName" placeholder="请输入分账方名称" />
        </el-form-item>
        <el-form-item label="分账方类型" prop="partyType">
          <el-radio-group v-model="form.partyType">
            <el-radio :label="0">内部</el-radio>
            <el-radio :label="1">外部</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联ID" prop="relatedId">
          <el-input v-model="form.relatedId" placeholder="请输入关联ID（商户/店铺/供应商ID）" />
        </el-form-item>
        <el-form-item label="关联名称" prop="relatedName">
          <el-input v-model="form.relatedName" placeholder="请输入关联名称" />
        </el-form-item>
        <el-form-item label="账号" prop="accountNo">
          <el-input v-model="form.accountNo" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="form.accountName" placeholder="请输入账户名称" />
        </el-form-item>
        <el-form-item label="银行名称" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入银行名称" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactMobile">
          <el-input v-model="form.contactMobile" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listShareParty, addShareParty, updateShareParty, delShareParty } from "@/api/finance/orderShare";

export default {
  name: "ShareParty",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      partyList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        partyName: null,
        partyType: null,
        status: null
      },
      form: {
        id: null,
        partyName: null,
        partyType: 0,
        relatedId: null,
        relatedName: null,
        accountNo: null,
        accountName: null,
        bankName: null,
        contactPerson: null,
        contactMobile: null,
        status: 1,
        remark: null
      },
      rules: {
        partyName: [{ required: true, message: "分账方名称不能为空", trigger: "blur" }],
        partyType: [{ required: true, message: "请选择分账方类型", trigger: "change" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listShareParty(this.queryParams).then(response => {
        this.partyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        partyName: null,
        partyType: 0,
        relatedId: null,
        relatedName: null,
        accountNo: null,
        accountName: null,
        bankName: null,
        contactPerson: null,
        contactMobile: null,
        status: 1,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加分账方";
    },
    handleUpdate(row) {
      this.reset();
      this.form = Object.assign({}, row);
      this.open = true;
      this.title = "修改分账方";
    },
    handleStatusChange(row) {
      updateShareParty(row).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("状态修改成功");
        } else {
          this.$modal.msgError(response.msg);
          this.getList();
        }
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = {
            ...this.form,
            id: this.form.id ? Number(this.form.id) : null,
            relatedId: this.form.relatedId ? Number(this.form.relatedId) : null,
            partyType: Number(this.form.partyType),
            status: Number(this.form.status)
          };
          if (data.id) {
            updateShareParty(data).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          } else {
            addShareParty(data).then(response => {
              if (response.code === 200) {
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
    handleDelete(row) {
      this.$modal.confirm('是否确认删除分账方"' + row.partyName + '"？')
        .then(() => {
          return delShareParty(row.id);
        })
        .then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("删除成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        })
        .catch(() => {});
    }
  }
};
</script>
