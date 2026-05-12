<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="报价日期" prop="priceDate">
        <el-date-picker clearable
                        v-model="queryParams.priceDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="报价日期">
        </el-date-picker>
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
        >添加金价</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="center" prop="id" width="80"/>
      <el-table-column label="报价日期" align="left" prop="priceDate" width="150" ></el-table-column>
      <el-table-column label="计价方式" align="center" prop="priceType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.priceType===0">采购价</el-tag>
          <el-tag size="small" v-if="scope.row.priceType===1">零售价</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="金价(g/元)" align="center" prop="price1" :formatter="amountFormatter"/>
      <el-table-column label="银价(g/元)" align="center" prop="price2" :formatter="amountFormatter"/>
      <el-table-column label="工费" align="center" prop="price3" :formatter="amountFormatter"/>

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0">启用</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="shipType" >
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['goods:goods:edit']"
          >编辑</el-button>

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



    <!-- 添加或修改 对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="报价日期" prop="priceDate">
          <el-date-picker v-model="form.priceDate" value-format="yyyy-MM-dd"
                          type="date" placeholder="报价日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报价类型" prop="priceType">
          <el-select v-model="form.priceType"   placeholder="报价类型">
            <el-option label="采购价" :value="0"></el-option>
            <el-option label="零售价" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="金价(g)" prop="price1">
          <el-input v-model="form.price1" placeholder="今日金价(g)" @input="handlePriceInput('price1')"  style="width: 230px;"/>
        </el-form-item>
        <el-form-item label="银价(g)" prop="price2">
          <el-input v-model="form.price2" placeholder="今日银价(g)" @input="handlePriceInput('price2')"  style="width: 230px;"/>
        </el-form-item>
        <el-form-item label="工时费" prop="price3">
          <el-input v-model="form.price3" placeholder="今日工时费" @input="handlePriceInput('price3')"  style="width: 230px;"/>
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
import {addGoldPrice, delGoldPrice, listGoldPrice, updateGoldPrice} from "@/api/goods/goods_price";
import { limitDecimalLength, numberToString, stringToNumber } from "../../../utils/numberInput";

export default {
  name: "GoldPrice",
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        priceDate: null,
        name: null,
        image: null,
        number: null,
      },
      // 表单参数
      form: {
        id: null,
        priceDate:null,
        priceType:null,
        price1:null,
        price2:null,
        price3:null,
      },
      // 表单校验
      rules: {
        priceDate: [{ required: true, message: "不能为空", trigger: "blur" }],
        priceType: [{ required: true, message: "不能为空", trigger: "change" }],
        price1: [{ required: true, message: "不能为空", trigger: "change" }],
        price2: [{ required: true, message: "不能为空", trigger: "change" }],
        price3: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.form.priceDate = new Date();
      this.getList();
  },
  methods: {
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
    handlePriceInput(field) {
      this.form[field] = limitDecimalLength(this.form[field]);
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listGoldPrice(this.queryParams).then(response => {
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
        priceDate:null,
        priceType:null,
        price1:null,
        price2:null,
        price3:null,
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
      this.title = "今日金价";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.form = row;
      // 将价格字段转换为字符串类型，以保留小数部分
      this.form.price1 = numberToString(this.form.price1);
      this.form.price2 = numberToString(this.form.price2);
      this.form.price3 = numberToString(this.form.price3);
      this.open = true;
      this.title = "今日金价";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function() {
        return delGoldPrice(ids);
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
          this.form.price1 = stringToNumber(this.form.price1);
          this.form.price2 = stringToNumber(this.form.price2);
          this.form.price3 = stringToNumber(this.form.price3);
          if (this.form.id != null) {
            updateGoldPrice(this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg)
              }
            });
          } else {
            addGoldPrice(this.form).then(response => {
              if (response.code == 200) {
                this.$modal.msgSuccess("添加成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg)
              }

            });
          }
        }
      });
    },

  }
};
</script>
