<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品编号" prop="productNum">
        <el-input
          v-model="queryParams.productNum"
          placeholder="请输入商品编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品分类" prop="categoryId">
        <treeselect :options="categoryTree" placeholder="请选择商品分类" v-model="queryParams.categoryId" style="width: 230px;"/>
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加供应商商品</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
<!--      <el-table-column label="ID" align="center" prop="id" width="80"/>-->
      <el-table-column label="商品编号" align="left" prop="productNum" width="150" >
        <template slot-scope="scope">
          {{scope.row.productNum || '-'}}<br/>
          <el-tag size="small">{{getCategoryName(scope.row.categoryId)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品图片" align="center" prop="imageUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="left" prop="productName" width="250"/>
      <el-table-column label="单位" align="center" prop="unitName" width="80"/>
      <el-table-column label="SKU数量" align="center" prop="skuCount" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleViewSku(scope.row)"
          >{{scope.row.skuCount || 0}}</el-button>
        </template>
      </el-table-column>
<!--      <el-table-column label="关联商品" align="center" prop="erpGoodsId" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.erpGoodsId" type="success" size="small">已关联</el-tag>-->
<!--          <el-tag v-else type="info" size="small">未关联</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small">销售中</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small">已下架</el-tag>
          <el-tag v-else size="small">待审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
          >编辑</el-button>
          <el-button
            v-if="scope.row.status === 1"
            size="mini"
            type="text"
            icon="el-icon-arrow-down"
            @click="handleChangeStatus(scope.row, 2)"
          >下架</el-button>
          <el-button
            v-else-if="scope.row.status === 2"
            size="mini"
            type="text"
            icon="el-icon-arrow-up"
            @click="handleChangeStatus(scope.row, 1)"
          >上架</el-button>
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

    <!-- SKU列表弹窗 -->
    <el-dialog title="SKU列表" :visible.sync="skuOpen" width="900px" append-to-body>
      <el-table :data="skuList" border>
        <el-table-column prop="skuCode" label="SKU编码" width="150"/>
        <el-table-column prop="standard" label="规格" width="150"/>
        <el-table-column prop="barCode" label="条码" width="150"/>
        <el-table-column prop="price" label="价格" width="100"/>
        <el-table-column prop="colorValue" label="颜色" width="100"/>
        <el-table-column prop="sizeValue" label="尺寸" width="100"/>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1" size="small">销售中</el-tag>
            <el-tag v-else-if="scope.row.status === 2" size="small">已下架</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 添加/编辑供应商商品弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="formOpen" width="1000px" append-to-body>
      <el-form ref="productForm" :model="productForm" :rules="productRules" label-width="120px">
        <el-form-item label="供应商" prop="supplierId" v-if="!isSupplier">
          <el-select v-model="productForm.supplierId" filterable clearable placeholder="请选择供应商" style="width: 300px;">
            <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="productForm.productName" placeholder="请输入商品名称" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="商品图片" prop="imageUrl">
          <image-upload v-model="productForm.imageUrl" :limit="1"/>
          <el-input v-model="productForm.imageUrl" placeholder="请输入商品图片Url" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="商品编号" prop="productNum">
          <el-input v-model="productForm.productNum" placeholder="请输入商品编号" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <treeselect :options="categoryTree" placeholder="请选择商品分类" v-model="productForm.categoryId" style="width: 300px;"/>
        </el-form-item>
        <el-form-item label="单位名称" prop="unitName">
          <el-input v-model="productForm.unitName" placeholder="请输入单位名称" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="productForm.remark" type="textarea" placeholder="请输入备注" style="width: 500px;" />
        </el-form-item>


        <!-- SKU表格 -->
        <el-form-item label="SKU列表">
          <div style="margin-bottom: 10px;">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addSkuItem">添加SKU</el-button>
          </div>
        </el-form-item>
        <el-table :data="productForm.itemList" border style="width: 100%;">
          <el-table-column prop="skuCode" label="SKU编码" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuCode" placeholder="SKU编码" size="small" />
            </template>
          </el-table-column>
          <el-table-column prop="standard" label="规格" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.standard" placeholder="规格" size="small" />
            </template>
          </el-table-column>
          <el-table-column prop="barCode" label="条码" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.barCode" placeholder="条码" size="small" />
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.price" placeholder="价格" size="small" :min="0" :precision="2" style="width: 100%;" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="danger" icon="el-icon-delete" size="small" @click="deleteSkuItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelForm">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listGoods, getGoodsItem, addGoodsItem, updateGoodsItem, delGoodsSpec, updateGoodsStatus } from "@/api/goods/supplier_goods";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { listCategory } from "@/api/goods/category";
import {getToken} from "@/utils/auth";
import { listBrand } from '@/api/goods/brand'
import {getUserProfile} from "@/api/system/user";
import {listSupplier} from "@/api/goods/supplier";

export default {
  name: "SupplierGoods",
  components: { Treeselect },
  data() {
    return {
      headers: { 'Authorization': 'Bearer ' + getToken() },
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      goodsList: [],
      brandList: [],
      title: "",
      skuOpen: false,
      skuList: [],
      formOpen: false,
      dialogTitle: '',
      categoryList: [],
      categoryTree: [],
      supplierList: [],
      userIdentity: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productName: null,
        productNum: null,
        categoryId: null,
        status: null,
      },
      productForm: {
        id: null,
        supplierId: null,
        productName: null,
        imageUrl: null,
        productNum: null,
        categoryId: null,
        unitName: null,
        remark: null,
        itemList: [],
      },
      productRules: {
        supplierId: [{ required: true, message: "不能为空", trigger: "blur" }],
        productName: [{ required: true, message: "商品名称不能为空", trigger: "blur" }],
      },
    };
  },
  computed: {
    isSupplier() {
      return this.userIdentity == 30;
    }
  },
  created() {
    this.getUserInfo();
    listCategory(this.queryParams).then(response => {
      this.categoryList = response.rows;
      this.categoryTree = this.buildTree(response.rows, 0);
      this.getList();
    });
    listBrand({pageNum: 1, pageSize: 100}).then(resp => {
      this.brandList = resp.rows;
    });
  },
  methods: {
    /** 获取用户信息 */
    getUserInfo() {
      getUserProfile().then(response => {
        this.userIdentity = response.data ? response.data.userType : null;
        // console.log('身份',this.userIdentity);
        // 如果是供应商身份，自动获取供应商列表
        if (this.userIdentity == 30) {
          listSupplier({pageNum: 1, pageSize: 100}).then(resp => {
            if (resp.rows && resp.rows.length > 0) {
              // 供应商默认选择自己的供应商
              this.productForm.supplierId = resp.rows[0].id;
            }
          });
        } else {
          // 总部身份，获取所有供应商列表
          listSupplier({pageNum: 1, pageSize: 100}).then(resp => {
            this.supplierList = resp.rows || [];
          });
        }
      });
    },
    normalizer(node) {
      return {
        id: node.id,
        label: node.name
      };
    },
    buildTree(list, parentId) {
      let tree = [];
      for (let i = 0; i < list.length; i++) {
        if (list[i].parentId === parentId) {
          let node = {
            id: list[i].id,
            label: list[i].name,
            children: this.buildTree(list, list[i].id)
          };
          tree.push(node);
        }
      }
      return tree;
    },
    getCategoryName(categoryId) {
      if (!categoryId) return '-';
      const category = this.categoryList.find(x => x.id === categoryId);
      return category ? category.name : '-';
    },
    /** 查询供应商商品列表 */
    getList() {
      this.loading = true;
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 查看SKU列表 */
    handleViewSku(row) {
      getGoodsItem(row.id).then(response => {
        if (response.data && response.data.itemList) {
          this.skuList = response.data.itemList;
        } else {
          this.skuList = [];
        }
        this.skuOpen = true;
      });
    },
    /** 添加供应商商品 */
    handleAdd() {
      this.resetForm();
      this.productForm.itemList = [];
      this.addSkuItem();
      this.dialogTitle = "添加供应商商品";
      // 如果是供应商身份，自动设置supplierId
      if (this.isSupplier && this.supplierList.length > 0) {
        this.productForm.supplierId = this.supplierList[0].id;
      }
      this.formOpen = true;
    },
    /** 编辑供应商商品 */
    handleEdit(row) {
      this.resetForm();
      getGoodsItem(row.id).then(response => {
        if (response.data) {
          this.productForm = {
            id: response.data.product.id,
            supplierId: response.data.product.supplierId,
            productName: response.data.product.productName,
            imageUrl: response.data.product.imageUrl,
            productNum: response.data.product.productNum,
            categoryId: response.data.product.categoryId,
            unitName: response.data.product.unitName,
            remark: response.data.product.remark,
            itemList: response.data.itemList || [],
          };
          // 确保每个SKU有id字段
          this.productForm.itemList = this.productForm.itemList.map(item => ({
            ...item,
            id: item.id,
          }));
          this.dialogTitle = "编辑供应商商品";
          this.formOpen = true;
        }
      });
    },
    /** 重置表单 */
    resetForm() {
      this.productForm = {
        id: null,
        supplierId: null,
        productName: null,
        imageUrl: null,
        productNum: null,
        categoryId: null,
        unitName: null,
        remark: null,
        itemList: [],
      };
      if (this.$refs.productForm) {
        this.$refs.productForm.resetFields();
      }
    },
    /** 添加SKU行 */
    addSkuItem() {
      if (!this.productForm.itemList) {
        this.productForm.itemList = [];
      }
      this.productForm.itemList.push({
        id: null,
        skuCode: '',
        standard: '',
        barCode: '',
        price: 0,
      });
    },
    /** 删除SKU行 */
    deleteSkuItem(index) {
      this.productForm.itemList.splice(index, 1);
    },
    /** 取消表单 */
    cancelForm() {
      this.formOpen = false;
      this.resetForm();
    },
    /** 提交表单 */
    submitForm() {
      this.$refs.productForm.validate(valid => {
        if (valid) {
          if (!this.productForm.itemList || this.productForm.itemList.length === 0) {
            this.$modal.msgWarning("请至少添加一个SKU");
            return;
          }
          // 验证SKU
          for (let i = 0; i < this.productForm.itemList.length; i++) {
            const sku = this.productForm.itemList[i];
            if (!sku.standard) {
              this.$modal.msgWarning(`第${i+1}个SKU的规格不能为空`);
              return;
            }
          }
          if (this.productForm.id) {
            // 编辑
            updateGoodsItem(this.productForm).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.formOpen = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          } else {
            // 新增
            addGoodsItem(this.productForm).then(response => {
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
    /** 修改状态 */
    handleChangeStatus(row, status) {
      const action = status === 1 ? '上架' : '下架';
      this.$modal.confirm(`是否确认${action}该商品？`).then(() => {
        return updateGoodsStatus({ id: row.id, status: status });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除该商品？').then(() => {
        return delGoodsSpec(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
