<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="退货单号" prop="afterSaleOrderId">
        <el-input
          v-model="queryParams.afterSaleOrderId"
          placeholder="请输入退货单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入源订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="sku编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入sku编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 500">视频号小店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 200">京东POP</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 280">京东自营</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 100">淘宝天猫</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 300">拼多多</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 400">抖店</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">线下渠道</span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="处理类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="无需处理" value="0" ></el-option>
          <el-option label="退货" value="10" ></el-option>
          <el-option label="换货" value="20" ></el-option>
          <el-option label="补发" value="80" ></el-option>
          <el-option label="订单拦截" value="99" ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择处理状态" clearable @change="handleQuery">
          <el-option label="待处理" value="0" ></el-option>
          <el-option label="已发出" value="1" ></el-option>
          <el-option label="已收货" value="2" ></el-option>
          <el-option label="已完结" value="10" ></el-option>

        </el-select>
      </el-form-item>

<!--      <el-form-item label="物流单号" prop="logisticsCode">-->
<!--        <el-input-->
<!--          v-model="queryParams.logisticsCode"-->
<!--          placeholder="请输入物流单号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->

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
        >手动处理售后</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleShippingLog"-->
<!--        >ERP售后处理推送记录</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="退货单号" align="left" prop="refundNum" width="150px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.refundNum}} </el-button><br/>
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="源订单号" align="left" prop="orderNum" width="150px"/>
      <el-table-column label="售后类型" align="center" prop="type" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.type === 0">无需处理</el-tag>
          <el-tag size="small" v-if="scope.row.type === 10">退货</el-tag>
          <el-tag size="small" v-if="scope.row.type === 20">换货</el-tag>
          <el-tag size="small" v-if="scope.row.type === 80">补发</el-tag>
          <el-tag size="small" v-if="scope.row.type === 99">订单拦截</el-tag>
        </template>
      </el-table-column>
       <el-table-column label="商品" align="left" prop="title" width="200px" />
       <el-table-column label="sku" align="center" prop="skuInfo" />
       <el-table-column label="SKU编码" align="center" prop="skuCode" />
      <el-table-column label="数量" align="center" prop="quantity" />
       <el-table-column label="是否发货" align="center" prop="hasGoodsSend" >
         <template slot-scope="scope">
           <el-tag size="small" v-if="scope.row.hasGoodsSend === 0"> 未发货</el-tag>
           <el-tag size="small" v-if="scope.row.hasGoodsSend === 1"> 已发货</el-tag>

         </template>
       </el-table-column>
<!--      <el-table-column label="物流单号" align="center" prop="shipWaybillCode" />-->
<!--      <el-table-column label="收货人" align="center" prop="receiverName" />-->
<!--      <el-table-column label="手机号" align="center" prop="receiverTel" />-->
<!--      <el-table-column label="收货地址" align="center" prop="receiverAddress" />-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 0"> 待处理</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1"> 已发出</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2"> 已收货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10"> 已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >确认处理</el-button>

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

    <!-- 添加或修改退换货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-form-item label="店铺" prop="shopId">
          <el-select v-model="form.shopId" filterable r placeholder="搜索店铺" >
            <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 100">淘宝天猫</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 200">京东</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 400">抖店</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 300">拼多多</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 500">微信小店</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">其他</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="退货单号" prop="afterSaleOrderId">
          <el-input v-model="form.afterSaleOrderId" placeholder="请输入退货单号" />
        </el-form-item>
        <el-form-item label="源订单号" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入源订单号" />
        </el-form-item>
        <el-form-item label="售后类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" clearable @change="handleQuery">
            <el-option label="退货" value="10" ></el-option>
            <el-option label="换货" value="20" ></el-option>
            <el-option label="维修" value="30" ></el-option>
            <el-option label="补发" value="80" ></el-option>
            <el-option label="订单拦截" value="99" ></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="子订单id" prop="subOrderId">-->
<!--          <el-input v-model="form.subOrderId" placeholder="请输入订单id" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="平台商品id" prop="productId">-->
<!--          <el-input v-model="form.productId" placeholder="请输入平台商品id" />-->
<!--        </el-form-item>-->
        <el-form-item label="平台SkuId" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入平台skuId" />
        </el-form-item>
<!--        <el-form-item label="Sku编码" prop="skuCode">-->
<!--          <el-input v-model="form.skuCode" placeholder="请输入Sku编码" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="ERP商品ID" prop="erpGoodsId">-->
<!--          <el-input v-model="form.erpGoodsId" placeholder="请输入ERP商品id" />-->
<!--        </el-form-item>-->
        <el-form-item label="ERP商品SkuId" prop="erpSkuId">
          <el-input v-model="form.erpSkuId" placeholder="请输入ERP商品SkuId" />
        </el-form-item>
<!--        <el-form-item label="商品名称" prop="title">-->
<!--          <el-input v-model="form.title" placeholder="请输入商品名称" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="商品SKU信息" prop="skuInfo">-->
<!--          <el-input v-model="form.skuInfo" placeholder="请输入商品SKU信息" />-->
<!--        </el-form-item>-->
        <el-form-item label="售后数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入售后数量" />
        </el-form-item>
        <el-form-item label="订单是否发货" prop="hasGoodsSend">
          <el-select v-model="form.hasGoodsSend" placeholder="是否已发货" >
            <el-option label="未发货" value="0"></el-option>
            <el-option label="已发货" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货物是否需要退回" prop="hasGoodsReturn">
          <el-select v-model="form.hasGoodsReturn" placeholder="货物是否需要退回" >
            <el-option label="无需退回" value="0"></el-option>
            <el-option label="需要退回" value="1"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="退回物流公司" prop="shipCompany">-->
<!--          <el-input v-model="form.shipCompany" placeholder="请输入发货物流公司" />-->
<!--        </el-form-item>-->
        <el-form-item label="退回物流单号" prop="shipWaybillCode" v-if="form.hasGoodsReturn==1">
          <el-input v-model="form.shipWaybillCode" placeholder="请输入发货物流单号" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiverName" v-if="form.type == 20||form.type == 30||form.type == 80">
          <el-input v-model="form.receiverName" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="收货人手机号" prop="receiverTel" v-if="form.type == 20||form.type == 30||form.type == 80">
          <el-input v-model="form.receiverTel" placeholder="请输入收货人手机号" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress" v-if="form.type == 20||form.type == 30||form.type == 80">
          <el-input v-model="form.receiverAddress" placeholder="请输入收货地址" />
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
import {list, addHandle} from "@/api/refund/after_sale";
import {listShop} from "@/api/shop/shop";

export default {
  name: "AfterSale",
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
      // 表格数据
      dataList: [],
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        afterSaleOrderId: null,
        shopId: null,
        orderId: null,
        status: null,
      },
      // 表单参数
      form: {
        type:null,
        hasGoodsReturn:null,
      },
      // 表单校验
      rules: {
        shopId: [{ required: true, message: "请选择店铺", trigger: "change" }],
        afterSaleOrderId: [{ required: true, message: "不能为空", trigger: "blur" }],
        orderId: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择售后类型", trigger: "blur" }],
        skuId: [{ required: true, message: "不能为空", trigger: "blur" }],
        erpSkuId: [{ required: true, message: "不能为空", trigger: "change" }],
        count: [{ required: true, message: "不能为空", trigger: "change" }],
        hasGoodsSend: [{ required: true, message: "不能为空", trigger: "change" }],
        hasGoodsReturn: [{ required: true, message: "不能为空", trigger: "change" }],
        // shipCompany: [{ required: true, message: "不能为空", trigger: "change" }],
        shipWaybillCode: [{ required: true, message: "不能为空", trigger: "change" }],
        // receiverName: [{ required: true, message: "不能为空", trigger: "change" }],
        // receiverTel: [{ required: true, message: "不能为空", trigger: "change" }],
        // receiverAddress: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    listShop({}).then(response => {
      this.shopList = response.rows;
    });
    this.getList();
  },
  methods: {
    /** 查询退换货列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then(response => {
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
      this.form = {};
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
    handleAdd(){
      this.reset()
      this.open=true
      this.title="手动添加售后处理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      shipAgainComplete(id).then(response => {
        this.$modal.msgSuccess("确认完成");
        this.getList()
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addHandle(this.form).then(response => {
            this.$modal.msgSuccess("售后处理成功");
            this.open = false;
            this.getList();
          });

        }
      });
    }
  }
};
</script>
