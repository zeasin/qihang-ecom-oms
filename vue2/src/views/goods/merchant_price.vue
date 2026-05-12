<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="商品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="Sku编码" prop="skuNum">
        <el-input
          v-model="queryParams.skuNum"
          placeholder="请输入Sku编码"
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
          v-hasPermi="['goods:goods:add']"
        >添加价格</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="center" prop="id" width="80"/>
      <el-table-column label="商户" align="left" prop="merchantId" width="150" >
        <template slot-scope="scope">
          <el-tag size="small">{{merchantList.find(x=>x.id === scope.row.merchantId)?merchantList.find(x=>x.id === scope.row.merchantId).name:''}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="left" prop="goodsName" min-width="300"/>
      <el-table-column label="规格" align="left" prop="skuName" width="200"/>
      <el-table-column label="SKU编码" align="left" prop="skuCode" width="150"/>
      <el-table-column label="商品库ID" align="center" prop="goodsSkuId" width="150"/>


      <el-table-column label="价格" align="center" prop="purPrice" :formatter="amountFormatter" width="120"/>
<!--      <el-table-column label="建议批发" align="center" prop="wholePrice" />-->
<!--      <el-table-column label="零售价" align="center" prop="retailPrice" :formatter="amountFormatter"/>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime)}}
        </template>
      </el-table-column>
<!--      <el-table-column label="更新时间" align="center" prop="updateTime" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          {{parseTime(scope.row.updateTime)}}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['goods:goods:remove']"
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




    <!-- 添加商品价格 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" >
        <el-form-item label="商品：" prop="goodsList">
          <el-col :span="24">
            <el-button size="small" @click="addGoodsDialog1"> 添加商品</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <image-preview :src="form.image" :width="80" :height="80"/>
        </el-form-item>
          <el-form-item label="商品名称" prop="goodsName">
            <el-input v-model="form.goodsName" placeholder="请输入商品名称" disabled  style="width: 560px;"/>
          </el-form-item>
        <el-form-item label="规格" prop="skuName">
          <el-input v-model="form.skuName" placeholder="请输入商品规格" disabled style="width: 360px;" />
        </el-form-item>
        <el-form-item label="商户" prop="merchantId">
          <el-select v-model="form.merchantId" filterable  placeholder="请选择商户">
            <el-option v-for="item in merchantList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
<!--          <el-form-item label="采购价" prop="purPrice">
            <el-input type="number" v-model.number="form.purPrice" placeholder="请输入采购价" @input="limitDecimalLength"  style="width: 230px;"/>
          </el-form-item>-->
          <el-form-item label="价格" prop="price">
            <el-input v-model="form.price" placeholder="请输入价格" @input="handlePriceInput" style="width: 230px;"/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="2" ref="popup"></PopupSkuList>
  </div>
</template>

<script>
import {
  addGoodsPrice, listMerchantGoodsPriceList,delGoodsPrice
} from '@/api/goods/merchant_price'
import {getToken} from "@/utils/auth";
import {listMerchant} from "@/api/shop/merchant"
import { rowDataItemIndex } from '@/utils/zhijian'
import PopupSkuList from "@/views/goods/PopupSkuList.vue";
import {listPlatform} from "@/api/shop/shop";
import { limitDecimalLength, stringToNumber } from "../../utils/numberInput";
export default {
  name: "MerchantGoodsPrice",
  components: {PopupSkuList },
  data() {
    return {
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
      // 商品管理表格数据
      goodsList: [],
      merchantList: [],
      platformList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      skuOpen: false,
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        image: null,
        number: null,
        unitName: null,
        categoryId: null,
        barCode: null,
        status: null,
        disable: null,
        supplierId: null,
        brandId: null,

      },
      // 表单参数
      form: {
        goodsName:null,
        goodsNum:null,
        skuNum:null,
        skuName:null,
        goodsId:null,
        goodsSkuId:null,
        image:null,
        price:null,
        merchantId:null,
      },
      historyList: [],
      // 表单校验
      rules: {
        goodsName: [{ required: true, message: "不能为空", trigger: "change" }],
        id: [{ required: true, message: "不能为空", trigger: "blur" }],
        merchantId: [{ required: true, message: "不能为空", trigger: "blur" }],
        price: [{ required: true, message: "不能为空", trigger: "blur" }],

      },

    };
  },
  created() {
    listMerchant({pageNum:1,pageSize:100}).then(resp=>{
      this.merchantList = resp.rows.filter(x=>x.id>0)
        listPlatform({status:0}).then(res => {
          this.platformList = res.rows;
          this.getList()
        })
    })
  },
  methods: {
    rowDataItemIndex,
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    limitDecimalLength() {
      if (this.form.purPrice) {
        const value = this.form.purPrice.toString();
        // 如果输入的值有小数点，限制小数位数为2位
        if (value.includes('.')) {
          const parts = value.split('.');
          if (parts[1] && parts[1].length > 2) {
            this.form.purPrice = parseFloat(value).toFixed(2);
          }
        }
      }
    },
    handlePriceInput() {
      this.form.price = limitDecimalLength(this.form.price);
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listMerchantGoodsPriceList(this.queryParams).then(response => {
        this.goodsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.skuOpen = false;
      this.open = false;
      this.historyList = []
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        goodsName: null,
        goodsNum: null,
        skuNum: null,
        skuName: null,
        goodsId: null,
        goodsSkuId: null,
        image: null,
        price: null,
        merchantId: null,
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
    addGoodsDialog1() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data) {
        if (data[0]) {
          this.form.goodsId = data[0].goodsId
          this.form.goodsSkuId = data[0].id
          this.form.image = data[0].colorImage
          this.form.goodsName = data[0].goodsName
          this.form.skuName = data[0].skuName
        }
      }
      console.log('====添加后=====', this.form)
    },
    // 添加
    handleAdd() {
      this.skuOpen = false;
      this.open = true;
      this.title = "添加价格";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除商品编号为"' + ids + '"的数据项？').then(function () {
        return delGoodsPrice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    submitAddForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将价格字段从字符串转换回数字类型
          this.form.price = stringToNumber(this.form.price);
          addGoodsPrice(this.form).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }
          });
        }
      });
    },

  }
};
</script>
