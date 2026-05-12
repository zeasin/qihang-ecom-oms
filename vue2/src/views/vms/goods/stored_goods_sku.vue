<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="商品名称" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库商品编码" prop="goodsNo">
        <el-input
          v-model="queryParams.goodsNo"
          placeholder="仓库商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家商品编码" prop="erpGoodsNo">
        <el-input
          v-model="queryParams.erpGoodsNo"
          placeholder="商家商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外部系统ID" prop="erpGoodsSign">
        <el-input
          v-model="queryParams.erpGoodsSign"
          placeholder="请输入外部系统ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable @change="handleQuery">
          <el-option label="总部" value="0"></el-option>
          <el-option
              v-for="item in merchantList"
              :key="item.merchantId"
              :label="item.name"
              :value="item.merchantId">
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="状态" prop="status">-->
<!--        <el-select v-model="queryParams.status" filterable  placeholder="状态">-->
<!--          <el-option label="销售中" value="1"></el-option>-->
<!--          <el-option label="已下架" value="2"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加商品资料</el-button>
      </el-col>
<!--        <el-col :span="1.5">-->
<!--          <el-button-->
<!--            type="success"-->
<!--            plain-->
<!--            icon="el-icon-edit"-->
<!--            size="mini"-->
<!--            @click="handleImport"-->
<!--            v-hasPermi="['goods:goods:edit']"-->
<!--          >导入商品SKU</el-button>-->
<!--        </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsSpecList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="仓库商品ID" align="center" prop="id" width="180"/>
      <el-table-column label="图片" align="center" prop="colorImage" width="60">
        <template slot-scope="scope">
          <image-preview :src="scope.row.imageUrl" :width="35" :height="35"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名" align="left" prop="goodsName" width="300"/>
      <el-table-column label="规格" align="center" prop="standard" />
      <el-table-column label="仓库商品编码" align="center" prop="goodsNo" />
      <el-table-column label="外部系统ID" align="center" prop="erpGoodsSign" />
      <el-table-column label="商家商品编码" align="left" prop="erpGoodsNo" />
      <el-table-column label="商户" align="left" prop="merchantId" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.merchantId===0">总部</el-tag>
          <el-tag v-else>
            {{ merchantList.find(x=>x.id === scope.row.merchantId) ? merchantList.find(x=>x.id === scope.row.merchantId).name : '' }}
          </el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="外部ERP SkuId" align="center" prop="outerErpSkuId" />-->
<!--      <el-table-column label="规格" align="left" prop="colorValue" >-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.colorValue}} {{scope.row.sizeValue}} {{scope.row.styleValue}}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="颜色" align="center" prop="color" />
      <el-table-column label="尺码" align="center" prop="size" />
<!--      <el-table-column label="外部ErpSkuId" align="center" prop="outerErpSkuId" />-->
<!--      <el-table-column label="建议零售价" align="center" prop="retailPrice" :formatter="amountFormatter" />-->
<!--      <el-table-column label="采购价" align="center" prop="purPrice" :formatter="amountFormatter" />-->

<!--      <el-table-column label="状态" align="center" prop="status" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.status===1">销售中</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status===2">已下架</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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

    <!-- 添加或修改商品规格库存管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="商户" prop="merchantId">
          <el-select v-model="form.merchantId" placeholder="请选择商户"  :disabled="isEdit">
            <el-option label="总部" :value="0"></el-option>
            <el-option
              v-for="item in merchantList"
              :key="item.merchantId"
              :label="item.name"
              :value="item.merchantId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入商品名" />
        </el-form-item>
        <el-form-item label="规格" prop="standard">
          <el-input v-model="form.standard" placeholder="请输入规格" />
        </el-form-item>

        <el-form-item label="仓库商品编码" prop="goodsNo">
          <el-input v-model="form.goodsNo" placeholder="请输入仓库商品编码" />
        </el-form-item>
        <el-form-item label="商家商品编码" prop="erpGoodsNo">
          <el-input v-model="form.erpGoodsNo" placeholder="请输入商家商品编码" />
        </el-form-item>
<!--        <el-form-item label="外部系统ID" prop="erpGoodsSign">-->
<!--          <el-input v-model="form.erpGoodsSign" placeholder="请输入外部系统ID" />-->
<!--        </el-form-item>-->
        <el-form-item label="图片URL" prop="imageUrl">
<!--          <image-upload v-model="form.colorImage" :limit="1" />-->
          <el-input v-model="form.imageUrl" placeholder="图片URL" />
        </el-form-item>
<!--        <el-form-item label="售价" prop="retailPrice">-->
<!--          <el-input type="number" v-model.number="form.retailPrice" placeholder="售价" />-->
<!--        </el-form-item>-->

        <el-form-item label="颜色" prop="color">
          <el-input v-model="form.color" placeholder="请输入颜色" />
        </el-form-item>
        <el-form-item label="尺码" prop="size">
          <el-input v-model="form.size" placeholder="请输入尺码" />
        </el-form-item>
<!--        <el-form-item label="规格3" prop="styleValue">-->
<!--          <el-input v-model="form.styleValue" placeholder="请输入规格3" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="ERP商品ID" prop="outerErpGoodsId">-->
<!--          <el-input type="number" v-model.number="form.outerErpGoodsId" placeholder="请输入ERP商品ID" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="ERP商品SkuID" prop="outerErpSkuId">-->
<!--          <el-input type="number" v-model.number="form.outerErpSkuId" placeholder="请输入ERP商品SkuID" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="状态" prop="status">-->
<!--          <el-select v-model="form.status" filterable  placeholder="状态">-->
<!--            <el-option label="销售中" :value="1"></el-option>-->
<!--            <el-option label="已下架" :value="2"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 导入ERP商品sku -->
    <el-dialog title="导入商品SKU" :visible.sync="importOpen" width="400px" append-to-body>
      <el-upload
        class="upload-demo"
        :headers="headers"
        drag
        action="/dev-api/api/oms-api/goods/goods_sku_import"
        accept="xlsx"
        multiple >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script>
import {
  addWarehouseGoodsItem,
  delWarehouseGoods,
  listStoredGoodsSku,
  updateWarehouseGoodsItem
} from '@/api/vms/goods/stored_goods_sku'
import {getToken} from "@/utils/auth";
import {listMerchant} from "@/api/vms/merchant";



export default {
  name: "GoodsSpec",
  data() {
    return {
      importOpen:false,
      headers: { 'Authorization': 'Bearer ' + getToken() },
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
      // 商品规格库存管理表格数据
      goodsSpecList: [],
      merchantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      isEdit: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        erpGoodsId: null,
        erpSkuId: null,
        standard: null,
        erpGoodsSign: null,
        skuNum: null,
      },
      // 表单参数
      form: {
        merchantId:null,
        imageUrl:null,
      },
      // 表单校验
      rules: {
        merchantId: [{ required: true, message: "不能为空", trigger: "blur" }],
        goodsName: [{ required: true, message: "不能为空", trigger: "blur" }],
        standard: [{ required: true, message: "不能为空", trigger: "blur" }],
        erpGoodsNo: [{ required: true, message: "SKU不能为空", trigger: "blur" }],
        // status: [{ required: true, message: "不能为空", trigger: "blur" }],

      }
    };
  },
  created() {
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
      this.getList();
    })

  },
  methods: {
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询商品规格库存管理列表 */
    getList() {
      this.loading = true;
      listStoredGoodsSku(this.queryParams).then(response => {
        this.goodsSpecList = response.rows;
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
        goodsId: null,
        skuName: null,
        skuNum: null,
        colorValue: null,
        colorImage: null,
        sizeValue: null,
        styleValue: null,
        erpGoodsId: null,
        erpSkuId: null
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除商品编号为"' + ids + '"的数据项？').then(function() {
        return delWarehouseGoods(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.form = row;
      this.isEdit = true;
      this.open = true;
      this.title = "修改仓库商品资料";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWarehouseGoodsItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }else{
            addWarehouseGoodsItem(this.form).then(response => {
              this.$modal.msgSuccess("成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleAdd(){
      this.title="添加仓库商品资料"
      this.isEdit = false;
      this.open = true
    },
    handleImport(){
      this.importOpen = true
    }
  }
};
</script>
