<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
      <el-form-item label="退款单号" prop="refundNum">
        <el-input
          v-model="queryParams.refundNum"
          placeholder="请输入退款单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入源订单号"
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
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">其他</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="refundType">
        <el-select v-model="queryParams.refundType" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="售前退款" value="1" ></el-option>
          <el-option label="仅退款" value="11" ></el-option>
          <el-option label="退货" value="10"></el-option>
          <el-option label="换货" value="20"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="hasProcessing">
        <el-select v-model="queryParams.hasProcessing" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="已处理" value="1" ></el-option>
          <el-option label="待处理" value="0" ></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="推送ERP状态" prop="hasLink">-->
<!--        <el-select v-model="queryParams.erpPushStatus" placeholder="推送ERP状态" clearable @change="handleQuery">-->
<!--          <el-option label="推送成功" value="200"></el-option>-->
<!--          <el-option label="推送失败" value="500"></el-option>-->
<!--          <el-option label="未推送" value="0"></el-option>-->
<!--        </el-select>-->
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
          icon="el-icon-refresh"
          size="mini"
          :disabled="single"

          @click="handleRefundProcessing"
        >处理选中售后</el-button>
        <!--          :disabled="multiple"-->
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="returnedList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="退货单号" align="left" prop="refundNum" >
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
      <el-table-column label="类型" align="center" prop="refundType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.refundType === 1">售前退款</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 11">仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 10">退货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 20">换货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 30">维修</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 80">补发商品</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id === scope.row.shopId).name  }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="源订单号" align="center" prop="orderNum" />
      <el-table-column label="商品" align="center" prop="goodsName" />
      <el-table-column label="SKU ID" align="center" prop="goodsSkuId" />
<!--      <el-table-column label="商品图片" align="center" prop="goodsImage" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <image-preview :src="scope.row.goodsImage" :width="50" :height="50"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="退款金额" align="center" prop="refundFee" :formatter="amountFormatter"/>
<!--      <el-table-column label="物流单号" align="center" prop="logisticsCode" />-->
<!--      <el-table-column label="收货时间" align="center" prop="receiveTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.receiveTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="退款理由" align="center" prop="refundReason" />
      <el-table-column label="是否需要退货" align="center" prop="hasGoodReturn" >
        <template slot-scope="scope">

          <el-tag style="margin-top: 5px" size="small" v-if="scope.row.hasGoodReturn === 1"> 买家需要退货</el-tag>
          <el-tag style="margin-top: 5px" size="small" v-if="scope.row.hasGoodReturn === 0"> 买家不需要退货</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status === 10001"> 待审核</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10002"> 等待买家退货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10005"> 等待卖家确认收货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 14000"> 拒绝退款</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10011"> 退款关闭</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10010"> 退款完成</el-tag>
          <br />
          <el-tag type="info" style="margin-top: 5px" size="small" v-if="scope.row.hasProcessing === 1"> 已处理</el-tag>
          <el-tag type="danger" style="margin-top: 5px" size="small" v-if="scope.row.hasProcessing === 0"> 待处理</el-tag>
<!--          <br />-->
<!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 200">已推送到ERP</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="!scope.row.erpPushStatus || scope.row.erpPushStatus === 0">待推送到ERP</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus > 200">推送错误</el-tag>-->
<!--          <div style="margin-bottom: 6px;color:red" v-if="scope.row.erpPushStatus > 200">{{scope.row.erpPushResult}}</div>-->
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--           v-if="(scope.row.refundType === 10 || scope.row.refundType === 20) && scope.row.status === 10005"-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['api:returned:edit']"-->
<!--          >确认收货</el-button>-->
<!--          <el-button-->
<!--            v-if="(scope.row.refundType === 20 || scope.row.refundType === 80)"-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['api:returned:edit']"-->
<!--          >补发商品</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="是否已发货" prop="hasGoodsSend">
          <el-select v-model="form.hasGoodsSend" placeholder="是否已发货" clearable>
            <el-option label="未发货" value="0"></el-option>
            <el-option label="已发货" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理方式" prop="type">
          <el-select v-model="form.type" placeholder="售后处理方式" clearable @change="handleTypeChange">
            <el-option label="无需处理" value="0"></el-option>
            <el-option label="退货" value="10"></el-option>
            <el-option label="换货" value="20"></el-option>
            <el-option label="补发" value="80"></el-option>
            <el-option label="订单拦截" value="99"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="源订单号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入源订单号" />
        </el-form-item>
        <el-form-item label="发货单号" prop="sendLogisticsCode">
          <el-input v-model="form.sendLogisticsCode" placeholder="发货单号" />
        </el-form-item>
        <el-form-item label="退回物流单号" prop="returnLogisticsCode">
          <el-input v-model="form.returnLogisticsCode" placeholder="退回物流单号" />
        </el-form-item>
        <el-form-item label="补发/换货单号" prop="reissueLogisticsCode">
          <el-input v-model="form.reissueLogisticsCode" placeholder="补发/换货单号" />
        </el-form-item>
        <el-form-item label="收件人" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="收件人" />
        </el-form-item>
        <el-form-item label="收件手机号" prop="receiverTel">
          <el-input v-model="form.receiverTel" placeholder="收件手机号" />
        </el-form-item>
        <el-form-item label="收件地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress" placeholder="收件地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
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
import {
  listReturned,
  getReturned,
  refundProcessing
} from "@/api/refund/refund";
import {listShop} from "@/api/shop/shop";

export default {
  name: "Returned",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      shopList:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 退换货表格数据
      returnedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundNum: null,
        refundType: null,
        orderNum: null,
        shopId: null,
        shopType: null,
        hasProcessing: '0',
        status: null,
      },
      // 表单参数
      form: {
        refundId:undefined,
        hasGoodsSend:undefined,
        type:undefined,
        orderNum:undefined,
        sendLogisticsCode:undefined,
        returnLogisticsCode:undefined,
        reissueLogisticsCode:undefined,
        remark:undefined,
        receiverName:undefined,
        receiverTel:undefined,
        receiverAddress:undefined,
      },
      // 表单校验
      rules: {
        hasGoodsSend: [{ required: true, message: "请选择是否发货", trigger: "blur" }],
        type: [{ required: true, message: "请选择处理方式", trigger: "blur" }],
        orderNum: [{ required: true, message: "源订单号不能为空", trigger: "blur" }],
      }
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
    /** 查询退换货列表 */
    getList() {
      this.loading = true;
      listReturned(this.queryParams).then(response => {
        this.returnedList = response.rows;
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
        refundId: null,
        hasGoodsSend: null,
        type: null,
        orderNum: null,
        sendLogisticsCode: null,
        returnLogisticsCode: null,
        reissueLogisticsCode: null,
        remark: null
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
    /** 处理方式选择*/
    handleTypeChange(){

    },
    submitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          refundProcessing(this.form).then(response => {
            console.log('======返回====',response)
            this.$modal.msgSuccess("处理成功！");
            this.open = false
            this.getList()
          });
        }
      })
    },
    /** 售后处理*/
    handleRefundProcessing(row){
      const id = row.id || this.ids[0]
      if(this.returnedList.find(x=>x.id === id)) {
        const refund = this.returnedList.find(x=>x.id === id)
        this.open = true
        this.form.refundId = id
        this.form.orderNum = refund.orderNum

      }else{
        this.$modal.msgError("请选择数据！");
      }
    },
  }
};
</script>
