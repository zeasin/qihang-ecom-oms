<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入SKU编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" filterable clearable placeholder="状态">
          <el-option label="有效" :value="1"></el-option>
          <el-option label="无效" :value="0"></el-option>
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
        >添加报价</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="priceList" @selection-change="handleSelectionChange">
      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150"/>
      <el-table-column label="报价" align="center" prop="price" width="120">
        <template slot-scope="scope">
          <span style="color: #E6A23C; font-weight: bold;">¥{{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="原价" align="center" prop="originalPrice" width="120">
        <template slot-scope="scope">
          ¥{{ scope.row.originalPrice || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="有效期" align="center" width="220">
        <template slot-scope="scope">
          <span v-if="scope.row.validStartTime">{{ parseTime(scope.row.validStartTime) }}</span>
          <span v-else>-</span>
          <span> ~ </span>
          <span v-if="scope.row.validEndTime">{{ parseTime(scope.row.validEndTime) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success" size="small">有效</el-tag>
          <el-tag v-else type="danger" size="small">无效</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" prop="remark" width="200"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >编辑</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="formOpen" width="600px" append-to-body>
      <el-form ref="priceForm" :model="priceForm" :rules="priceRules" label-width="120px">
        <el-form-item label="商品SKU" prop="supplierProductItemId">
          <el-select v-model="priceForm.supplierProductItemId" filterable disabled placeholder="请选择商品SKU" style="width: 200px;">
            <el-option :key="selectedSku.id" :label="selectedSku.skuCode + ' - ' + selectedSku.standard" :value="selectedSku.id"></el-option>
          </el-select>
          <el-button type="primary" size="mini" icon="el-icon-search" @click="openGoodsSelect">选择商品</el-button>
          <div v-if="selectedSku.productName" style="margin-top: 5px; color: #666;">
            商品名称：{{ selectedSku.productName }}
          </div>
        </el-form-item>
        <el-form-item label="报价" prop="price">
          <el-input-number v-model="priceForm.price" placeholder="请输入报价" style="width: 200px;" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="priceForm.originalPrice" placeholder="请输入原价" style="width: 200px;" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="有效期开始" prop="validStartTime">
          <el-date-picker
            v-model="priceForm.validStartTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 250px;"
          />
        </el-form-item>
        <el-form-item label="有效期结束" prop="validEndTime">
          <el-date-picker
            v-model="priceForm.validEndTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 250px;"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="priceForm.remark" type="textarea" placeholder="请输入备注" style="width: 400px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelForm">取 消</el-button>
      </div>
    </el-dialog>

    <PopupSupplierGoods @data-from-select="handleGoodsSelect" ref="popupGoods" />
  </div>
</template>

<script>
import { listPrice, getPrice, addPrice, updatePrice, delPrice } from "@/api/vendor/price";
import { getUserProfile } from "@/api/system/user";
import PopupSupplierGoods from "@/views/vendor/PopupSupplierGoods";

export default {
  name: "SupplierPrice",
  components: { PopupSupplierGoods },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      priceList: [],
      formOpen: false,
      dialogTitle: '',
      userIdentity: null,
      selectedSku: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        skuCode: null,
        status: null,
      },
      priceForm: {
        id: null,
        supplierProductItemId: null,
        price: null,
        originalPrice: null,
        validStartTime: null,
        validEndTime: null,
        remark: null,
      },
      priceRules: {
        supplierProductItemId: [{ required: true, message: "请选择商品SKU", trigger: "blur" }],
        price: [{ required: true, message: "报价不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getUserInfo();
    this.getList();
  },
  methods: {
    getUserInfo() {
      getUserProfile().then(response => {
        this.userIdentity = response.data ? response.data.userType : null;
      });
    },
    getList() {
      this.loading = true;
      listPrice(this.queryParams).then(response => {
        this.priceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.resetForm();
      this.selectedSku = {};
      this.dialogTitle = "添加报价";
      this.formOpen = true;
    },
    handleEdit(row) {
      this.resetForm();
      getPrice(row.id).then(response => {
        if (response.data) {
          this.priceForm = {
            id: response.data.id,
            supplierProductItemId: response.data.supplierProductItemId,
            price: response.data.price,
            originalPrice: response.data.originalPrice,
            validStartTime: response.data.validStartTime,
            validEndTime: response.data.validEndTime,
            remark: response.data.remark,
          };
          this.selectedSku = {
            id: response.data.supplierProductItemId,
            skuCode: response.data.skuCode,
            standard: response.data.skuCode,
            productName: response.data.skuCode,
          };
          this.dialogTitle = "编辑报价";
          this.formOpen = true;
        }
      });
    },
    resetForm(formName) {
      if (formName && this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
      this.priceForm = {
        id: null,
        supplierProductItemId: null,
        price: null,
        originalPrice: null,
        validStartTime: null,
        validEndTime: null,
        remark: null,
      };
    },
    cancelForm() {
      this.formOpen = false;
      this.resetForm();
      this.selectedSku = {};
    },
    submitForm() {
      this.$refs.priceForm.validate(valid => {
        if (valid) {
          if (this.priceForm.id) {
            updatePrice(this.priceForm).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.formOpen = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          } else {
            addPrice(this.priceForm).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("添加成功");
                this.formOpen = false;
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
      this.$modal.confirm('是否确认删除该报价？').then(() => {
        return delPrice(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    openGoodsSelect() {
      this.$refs.popupGoods.openDialog();
    },
    handleGoodsSelect(data) {
      if (data && data.length > 0) {
        const sku = data[0];
        this.selectedSku = sku;
        this.priceForm.supplierProductItemId = sku.id;
      }
    },
  }
};
</script>
