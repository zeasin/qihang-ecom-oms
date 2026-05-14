<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="供应商" prop="supplierId">
        <el-select v-model="queryParams.supplierId" clearable filterable placeholder="请选择供应商" style="width: 200px">
          <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入SKU编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格" prop="standard">
        <el-input
          v-model="queryParams.standard"
          placeholder="请输入规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" filterable clearable placeholder="状态">
          <el-option label="销售中" :value="1"></el-option>
          <el-option label="已下架" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="skuList" @selection-change="handleSelectionChange">
<!--      <el-table-column label="SKU ID" align="center" prop="id" width="80"/>-->
      <el-table-column label="商品名" align="left" prop="productName" width="200"/>
      <el-table-column label="规格" align="center" prop="standard" width="150">
        <template slot-scope="scope">
          {{scope.row.standard}}
        </template>
      </el-table-column>
      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150"/>
<!--      <el-table-column label="条码" align="center" prop="barCode" width="150"/>-->
      <el-table-column label="价格" align="center" prop="price" width="100">
        <template slot-scope="scope">
          ￥{{scope.row.price ? scope.row.price.toFixed(2) : '0.00'}}
        </template>
      </el-table-column>
      <el-table-column label="绑定商品库" align="center" prop="erpGoodsSkuId" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.erpGoodsSkuId" type="success" size="small">已绑定</el-tag>
          <el-tag v-else type="info" size="small">未绑定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品库商品名" align="left" width="200" v-if="hasBoundGoods">
        <template slot-scope="scope">
          {{scope.row.erpGoodsSku ? scope.row.erpGoodsSku.goodsName : '-'}}
        </template>
      </el-table-column>
      <el-table-column label="商品库规格" align="left" width="200" v-if="hasBoundGoods">
        <template slot-scope="scope">
          {{scope.row.erpGoodsSku ? scope.row.erpGoodsSku.skuName : '-'}}
        </template>
      </el-table-column>
      <el-table-column label="商品库SKU编码" align="left" width="150" v-if="hasBoundGoods">
        <template slot-scope="scope">
          {{scope.row.erpGoodsSku ? scope.row.erpGoodsSku.skuCode : '-'}}
        </template>
      </el-table-column>
      <el-table-column label="商品库商品编码" align="left" width="150" v-if="hasBoundGoods">
        <template slot-scope="scope">
          {{scope.row.erpGoodsSku ? scope.row.erpGoodsSku.goodsNum : '-'}}
        </template>
      </el-table-column>
<!--      <el-table-column label="状态" align="center" prop="status" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.status === 1">销售中</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 2">已下架</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >编辑</el-button>
          <el-button
            v-if="isHeadquarters"
            size="mini"
            type="text"
            icon="el-icon-link"
            @click="handleBind(scope.row)"
          >绑定商品库</el-button>
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

    <!-- 编辑供应商商品SKU对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="商品名" prop="productName">
          <el-input v-model="form.productName" disabled placeholder="商品名" />
        </el-form-item>
        <el-form-item label="规格" prop="standard">
          <el-input v-model="form.standard" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="SKU编码" prop="skuCode">
          <el-input v-model="form.skuCode" placeholder="请输入SKU编码" />
        </el-form-item>
        <el-form-item label="商品条码" prop="barCode">
          <el-input v-model="form.barCode" placeholder="请输入商品条码" />
        </el-form-item>
        <el-form-item label="颜色" prop="colorValue">
          <el-input v-model="form.colorValue" placeholder="请输入颜色" />
        </el-form-item>
        <el-form-item label="尺寸" prop="sizeValue">
          <el-input v-model="form.sizeValue" placeholder="请输入尺寸" />
        </el-form-item>
        <el-form-item label="款式" prop="styleValue">
          <el-input v-model="form.styleValue" placeholder="请输入款式" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" placeholder="价格" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" filterable placeholder="状态">
            <el-option label="销售中" :value="1"></el-option>
            <el-option label="已下架" :value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 绑定商品库SKU对话框 -->
    <el-dialog title="绑定商品库SKU" :visible.sync="bindOpen" width="800px" append-to-body>
      <el-form :model="bindForm" label-width="120px">
        <el-form-item label="当前供应商SKU">
          <el-tag>{{bindForm.skuCode}} - {{bindForm.standard}}</el-tag>
        </el-form-item>
        <el-form-item label="选择商品库SKU" prop="erpGoodsSkuId">
          <el-table
            ref="goodsSkuTable"
            :data="goodsSkuList"
            height="300"
            @selection-change="handleGoodsSkuSelectionChange"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column property="goodsName" label="商品名" width="200"></el-table-column>
            <el-table-column property="skuName" label="规格" width="150"></el-table-column>
            <el-table-column property="skuCode" label="SKU编码" width="150"></el-table-column>
            <el-table-column property="retailPrice" label="建议零售价" width="120">
              <template slot-scope="scope">
                ￥{{scope.row.retailPrice ? scope.row.retailPrice.toFixed(2) : '0.00'}}
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="goodsSkuTotal>0"
            :total="goodsSkuTotal"
            :page.sync="bindQueryParams.pageNum"
            :limit.sync="bindQueryParams.pageSize"
            @pagination="getGoodsSkuList"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBind">确 定</el-button>
        <el-button @click="bindCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listSupplierGoodsSku, getSupplierGoodsSku, updateSupplierGoodsSku, delSupplierGoodsSku, bindGoodsSku} from "@/api/goods/supplier_goods_sku";
import {listGoodsSpec} from "@/api/goods/goodsSpec";
import {listSupplier} from "@/api/goods/supplier";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "SupplierGoodsSku",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 用户身份
      userIdentity: null,
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
      // 供应商商品SKU列表数据
      skuList: [],
      // 供应商列表
      supplierList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        supplierId: null,
        skuCode: null,
        standard: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        standard: [{ required: true, message: "规格不能为空", trigger: "blur" }],
      },
      // 绑定商品库相关
      bindOpen: false,
      bindForm: {},
      goodsSkuList: [],
      goodsSkuTotal: 0,
      selectedGoodsSku: null,
      bindQueryParams: {
        pageNum: 1,
        pageSize: 10,
      }
    };
  },
  computed: {
    hasBoundGoods() {
      return this.skuList.some(item => item.erpGoodsSkuId);
    },
    isHeadquarters() {
      return this.userIdentity ==='00';
    }
  },
  created() {
    this.getUserInfo();
    this.getList();
    this.getSupplierList();
  },
  methods: {
    /** 获取用户信息 */
    getUserInfo() {
      getUserProfile().then(response => {
        this.userIdentity = response.data ? response.data.userType : null;
      });
    },
    /** 查询供应商商品SKU列表 */
    getList() {
      this.loading = true;
      listSupplierGoodsSku(this.queryParams).then(response => {
        this.skuList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取供应商列表 */
    getSupplierList() {
      listSupplier({pageNum: 1, pageSize: 100}).then(response => {
        this.supplierList = response.rows || [];
      });
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
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
        supplierProductId: null,
        supplierId: null,
        productName: null,
        skuCode: null,
        barCode: null,
        colorValue: null,
        colorImage: null,
        sizeValue: null,
        styleValue: null,
        standard: null,
        brandNo: null,
        brandName: null,
        price: 0,
        status: 1,
        erpGoodsSkuId: null,
      };
      this.resetForm("form");
    },
    /** 编辑按钮操作 */
    handleEdit(row) {
      this.reset();
      const id = row.id;
      getSupplierGoodsSku(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "编辑供应商商品SKU";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateSupplierGoodsSku(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除该SKU？').then(() => {
        return delSupplierGoodsSku(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 绑定商品库 */
    handleBind(row) {
      this.bindForm = {
        id: row.id,
        skuCode: row.skuCode,
        standard: row.standard,
        erpGoodsSkuId: null
      };
      this.selectedGoodsSku = null;
      this.bindQueryParams.pageNum = 1;
      this.getGoodsSkuList();
      this.bindOpen = true;
    },
    /** 获取商品库SKU列表 */
    getGoodsSkuList() {
      listGoodsSpec(this.bindQueryParams).then(response => {
        this.goodsSkuList = response.rows;
        this.goodsSkuTotal = response.total;
      });
    },
    /** 商品库SKU选择 */
    handleGoodsSkuSelectionChange(selection) {
      if (selection && selection.length > 0) {
        this.selectedGoodsSku = selection[0];
      } else {
        this.selectedGoodsSku = null;
      }
    },
    /** 取消绑定 */
    bindCancel() {
      this.bindOpen = false;
      this.bindForm = {};
      this.selectedGoodsSku = null;
    },
    /** 提交绑定 */
    submitBind() {
      if (!this.selectedGoodsSku) {
        this.$modal.msgWarning("请选择一个商品库SKU");
        return;
      }
      bindGoodsSku({
        id: this.bindForm.id,
        erpGoodsSkuId: this.selectedGoodsSku.id
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("绑定成功");
          this.bindOpen = false;
          this.getList();
        } else {
          this.$modal.msgError(response.msg);
        }
      });
    }
  }
};
</script>
