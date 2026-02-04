  <template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺" prop="shopId">
        <!-- <el-input
          v-model="queryParams.shopId"
          placeholder="请输入店铺ID"
          clearable
          @keyup.enter.native="handleQuery"
        /> -->
        <el-select v-model="queryParams.shopId" filterable  placeholder="搜索店铺" >
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">其他</span>
          </el-option>
          </el-select>
      </el-form-item>


      <el-form-item label="订单编号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type" >
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="收入" value="1"></el-option>
          <el-option label="支出" value="2"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="bizTime">
        <el-date-picker clearable
          v-model="queryParams.bizTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择日期" @change="handleQuery">
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
          type="danger"
          plain
          icon="el-icon-document-checked"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['scm:agentShipping:export']"
          >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

       <el-table-column label="时间" align="center" prop="bizTime" />
      <el-table-column label="类型" align="center" prop="type" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.type == 1">收入</el-tag>
          <el-tag size="small" type="danger" v-if="scope.row.type == 2">支出</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="amount"  :formatter="amountFormatter"/>
      <el-table-column label="订单编号" align="center" prop="orderId" />
      <el-table-column label="业务类型" align="center" prop="bizType" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="业务描述" align="center" prop="detail" />
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
          <span>{{ shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''  }}</span>
        </template>
      </el-table-column>
      <el-table-column label="店铺平台" align="center" prop="shopType" >
         <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType === 5">微信小店</el-tag>
          <el-tag size="small" v-if="scope.row.shopType === 3">拼多多</el-tag>
          <el-tag size="small" v-if="scope.row.shopType === 9">其他</el-tag>
         </template>
      </el-table-column>
<!--      <el-table-column label="状态" align="center" prop="status" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.status === 0">待发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 1">已发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.status === 2">已结算</el-tag>-->
<!--         </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" >
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

    <el-dialog title="导入拼多多支出账单" :visible.sync="importOpen" width="400px" append-to-body>
      <el-upload
        class="upload-demo"
        :headers="headers"
        drag
        :action="importUrl"
        accept="csv"
        multiple >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-dialog>

    <!-- 添加或修改供应商代发货对话框 -->

  </div>
</template>

<script>

import { listShop } from "@/api/shop/shop";
import {getToken} from "@/utils/auth";
import {delBill, listOrderBill} from "@/api/financial/order_bill";

export default {
  name: "AgentShipping",
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
      // 供应商代发货表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      importOpen: false,
      importUrl: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        shopType: null,
        bizTime: null,
        orderId: null,
        type: null

      },
      shopList: [],
      logisticsList:[],
      // 表单参数
      form: {},

      // 表单校验
      rules: {
        goodsPrice: [{ required: true, message: '不能为空' }],
        shipCompany: [{ required: true, message: '不能为空' }],
        shipNo: [{ required: true, message: '不能为空' }],
        shipCost: [{ required: true, message: '不能为空' }],
        shipTime: [{ required: true, message: '不能为空' }],
      },
      supplierLoading:false,
      supplierList:[]
    };
  },
  created() {
    listShop({}).then(response => {
      this.shopList = response.rows;
      this.getList();
    });


  },
  methods: {
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询供应商代发货列表 */
    getList() {
      this.loading = true;
      listOrderBill(this.queryParams).then(response => {
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
        id: null,
        shopId: null,
        shopType: null,
        supplierId: null,
        orderNum: null,
        orderItemId: null,
        orderDate: null,
        goodsId: null,
        specId: null,
        goodsTitle: null,
        goodsImg: null,
        goodsNum: null,
        goodsSpec: null,
        specNum: null,
        goodsPrice: null,
        quantity: null,
        remark: null,
        shipCompany: null,
        shipNo: null,
        shipCost: null,
        shipTime: null,
        status: null,
        itemAmount: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null
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
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的账单记录呢？').then(function() {
        return delBill(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('scm/agentShipping/export', {
        ...this.queryParams
      }, `agentShipping_${new Date().getTime()}.xlsx`)
    },
    handleImport(){
      if(this.queryParams.shopId){
        this.importUrl= '/prod-api/financial/order_bill/pdd_bill_import?shopId='+this.queryParams.shopId,
          this.importOpen=true
      }else {
        this.$modal.msgError("请选择店铺")
      }

    }
  }
};
</script>
