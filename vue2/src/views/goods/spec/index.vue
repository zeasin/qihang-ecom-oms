<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
    <el-form-item label="商品名称" prop="goodsName">
      <el-input
        v-model="queryParams.goodsName"
        placeholder="请输入商品名称"
        clearable
        @keyup.enter.native="handleQuery"
      />
    </el-form-item>
      <el-form-item label="Sku Id" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入Sku Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="SKU编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入Sku编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外部SkuId" prop="outerErpSkuId">
        <el-input
          v-model="queryParams.outerErpSkuId"
          placeholder="请输入外部SkuId"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="商品SkuID" prop="id">-->
<!--        <el-input-->
<!--          v-model="queryParams.id"-->
<!--          placeholder="商品SkuID"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="商品ID" prop="goodsId">
        <el-input
          v-model="queryParams.goodsId"
          placeholder="商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" clearable placeholder="请选择商户" @change="handleQuery">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="商品编码" prop="goodsNum">-->
<!--        <el-input-->
<!--          v-model="queryParams.goodsNum"-->
<!--          placeholder="请输入商品编码"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->


      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" filterable  placeholder="状态">
          <el-option label="销售中" value="1"></el-option>
          <el-option label="已下架" value="2"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
    </el-form>

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5" style="padding-left: 26px;">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-col>
<!--        <el-col :span="1.5">-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            plain-->
<!--            icon="el-icon-plus"-->
<!--            size="mini"-->
<!--            @click="handleAdd"-->
<!--            v-hasPermi="['goods:goods:add']"-->
<!--          >添加商品SKU</el-button>-->
<!--        </el-col>-->
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsSpecList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="SkuId" align="center" prop="id" width="80px"/>
      <el-table-column label="商品Id" align="center" prop="goodsId" width="80px"/>
      <el-table-column label="图片" align="center" prop="colorImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.colorImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="商品名" align="left" prop="goodsName" width="300"/>
      <el-table-column label="规格" align="center" prop="skuName" />
      <el-table-column label="商品编码" align="left" prop="goodsNum" />
      <el-table-column label="Sku编码" align="left" prop="skuCode" />
      <el-table-column label="计价方式" align="center" prop="priceType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.priceType===0">一口价</el-tag>
          <el-tag size="small" v-if="scope.row.priceType===1">实时计价</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="库存模式" align="center" prop="inventoryMode" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.inventoryMode===0">传统SKU模式</el-tag>
          <el-tag size="small" v-if="scope.row.inventoryMode===1">一物一码模式</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="外部SkuId" align="center" prop="outerErpSkuId" />
      <el-table-column label="条形码" align="center" prop="barCode" />
      <el-table-column label="单位" align="center" prop="unit" />
<!--      <el-table-column label="规格" align="left" prop="colorValue" >-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.colorValue}} {{scope.row.sizeValue}} {{scope.row.styleValue}}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="规格2" align="center" prop="sizeValue" />-->
<!--      <el-table-column label="规格3" align="center" prop="styleValue" />-->
<!--      <el-table-column label="外部ErpSkuId" align="center" prop="outerErpSkuId" />-->
      <el-table-column label="零售价" align="center" prop="retailPrice" :formatter="amountFormatter" />
      <el-table-column label="采购价" align="center" prop="purPrice" :formatter="amountFormatter" />
<!--      <el-table-column label="发货方式" align="center" prop="shipType" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.shipType === 10">自营发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.shipType === 20">供应商发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status===1">销售中</el-tag>
          <el-tag size="small" v-if="scope.row.status===2">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:goodsSpec:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDelete(scope.row)"
            v-hasPermi="['api:goodsSpec:edit']"
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
        <el-form-item label="商品名" prop="goodsName">
          <el-input v-model="form.goodsName" disabled placeholder="请输入商品名" />
        </el-form-item>
<!--        <el-form-item label="规格" prop="skuName">-->
<!--          <el-input v-model="form.skuName" placeholder="请输入规格" />-->
<!--        </el-form-item>-->
        <el-form-item label="SKU编码" prop="skuCode">
          <el-input v-model="form.skuCode" placeholder="请输入SKU编码" />
        </el-form-item>
        <el-form-item label="条形码" prop="barCode">
          <el-input v-model="form.barCode" placeholder="请输入条形码" />
        </el-form-item>

        <el-form-item label="图片URL" prop="colorImage">
<!--          <image-upload v-model="form.colorImage" :limit="1" />-->
          <el-input v-model="form.colorImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="采购价" prop="purPrice">
          <el-input v-model="form.purPrice" placeholder="采购价" @input="handlePurPriceInput" />
        </el-form-item>
        <el-form-item label="零售价" prop="retailPrice">
          <el-input v-model="form.retailPrice" placeholder="建议零售价" @input="handleRetailPriceInput" />
        </el-form-item>

        <el-form-item label="规格1" prop="colorValue">
          <el-input v-model="form.colorValue" placeholder="请输入规格1" />
        </el-form-item>
        <el-form-item label="规格2" prop="sizeValue">
          <el-input v-model="form.sizeValue" placeholder="请输入规格2" />
        </el-form-item>
        <el-form-item label="规格3" prop="styleValue">
          <el-input v-model="form.styleValue" placeholder="请输入规格3" />
        </el-form-item>

        <el-form-item label="外部SkuId" prop="outerErpSkuId">
          <el-input  v-model="form.outerErpSkuId" placeholder="请输入外部SkuId" />
        </el-form-item>
        <el-form-item label="揽八方商品ID" prop="sellerId">
          <el-input v-model="form.sellerId" placeholder="揽八方商品ID" style="width: 230px;" />
        </el-form-item>
        <el-form-item label="揽八方品牌ID" prop="sellerBrandId">
          <el-input v-model="form.sellerBrandId" placeholder="揽八方商品品牌ID" style="width: 230px;" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" filterable  placeholder="状态">
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
import {listGoodsSpec, getGoodsSpec, updateGoodsSpec, addGoodsSpec,delGoodsSpec} from "@/api/goods/goodsSpec";
import {getToken} from "@/utils/auth";
import {delGoods} from "@/api/goods/goods";
import {listMerchant} from "@/api/shop/merchant";
import { limitDecimalLength, numberToString, stringToNumber } from "@/utils/numberInput";

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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        erpGoodsId: null,
        erpSkuId: null,
        skuName: null,
        skuNum: null,
        merchantId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // goodsName: [{ required: true, message: "不能为空", trigger: "blur" }],
        // skuName: [{ required: true, message: "不能为空", trigger: "blur" }],
        skuCode: [{ required: true, message: "SKU不能为空", trigger: "blur" }],
        purPrice: [{ required: true, message: "不能为空", trigger: "blur" }],
        retailPrice: [{ required: true, message: "不能为空", trigger: "blur" }],
        status: [{ required: true, message: "不能为空", trigger: "blur" }],

      }
    };
  },
  created() {
    listMerchant({pageNum:1,pageSize:100}).then(resp=>{
      this.merchantList = resp.rows
      if(resp.rows && resp.rows.length>0){
        this.queryParams.merchantId = resp.rows[0].id
      }
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
      listGoodsSpec(this.queryParams).then(response => {
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
      this.$modal.confirm('是否确认删除商品编号为"' + row.id + '"的数据项？').then(function() {
        return delGoodsSpec(row.id);
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
      getGoodsSpec(id).then(response => {
        this.form = response.data;
        this.form.disable = response.data.disable+''
        // 将价格字段转换为字符串类型，以保留小数部分
        this.form.purPrice = numberToString(this.form.purPrice);
        this.form.retailPrice = numberToString(this.form.retailPrice);
        this.open = true;
        this.title = "修改商品SKU信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将价格字段从字符串转换回数字类型
          this.form.purPrice = stringToNumber(this.form.purPrice);
          this.form.retailPrice = stringToNumber(this.form.retailPrice);
          if (this.form.id != null) {
            updateGoodsSpec(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }else{
            addGoodsSpec(this.form).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleAdd(){
      this.open = true
    },
    handleImport(){
      this.importOpen = true
    },
    handleExport(){
      this.download('api/erp-api/goods/sku/export', {
        ...this.queryParams
      }, `goods_sku_list_${new Date().getTime()}.xlsx`)
    },
    handlePurPriceInput() {
      this.form.purPrice = limitDecimalLength(this.form.purPrice);
    },
    handleRetailPriceInput() {
      this.form.retailPrice = limitDecimalLength(this.form.retailPrice);
    }
  }
};
</script>
