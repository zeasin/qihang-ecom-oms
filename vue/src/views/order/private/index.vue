<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
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
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 500">微信小店</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 200">京东POP</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 280">京东自营</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 100">淘宝天猫</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 300">拼多多</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 400">抖店</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">其他</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间" prop="orderTime">
          <el-date-picker clearable
            v-model="orderTime" value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待发货" value="1" ></el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="已完成" value="3"> </el-option>
          <el-option label="已取消" value="11"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="收件人姓名" prop="receiverName">
        <el-input
          v-model="queryParams.receiverName"
          placeholder="请输入收件人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人手机号" prop="receiverMobile">
        <el-input
          v-model="queryParams.receiverMobile"
          placeholder="请输入收件人手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="快递单号" prop="receiverMobile">
        <el-input
          v-model="queryParams.shippingNumber"
          placeholder="请输入快递单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >手动创建订单</el-button>
      </el-col>

<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['shop:order:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="订单ID" align="center" prop="id" />-->
<!--      <el-table-column label="订单号" align="center" prop="orderNum" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id === scope.row.shopId).name  }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品明细" align="center" width="750px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">SKU名</td>
              <td width="200" align="left">Sku编码</td>
<!--              <td width="150" align="left">平台SkuId</td>-->
              <td width="50" align="left">数量</td>
            </th>
          </table>
        </template>
        <template slot-scope="scope" >
          <el-table :data="scope.row.itemList" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >
            <el-table-column label="商品图片" width="50px">
              <template slot-scope="scope">
<!--                <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>-->
                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
              </template>
            </el-table-column>
            <el-table-column label="商品名" align="left" width="250px" prop="goodsTitle" />
            <el-table-column label="SKU名" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true"/>
            <el-table-column label="Sku编码" align="left" prop="skuNum" width="200"/>
<!--            <el-table-column label="平台SkuId" align="left" prop="skuId" width="150"/>-->
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
<!--      <el-table-column label="商品" width="350">-->
<!--          <template slot-scope="scope">-->
<!--            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">-->

<!--            <div style="float: left;display: flex;align-items: center;" >-->
<!--              <el-image  style="width: 70px; height: 70px;" :src="item.goodsImg"></el-image>-->
<!--              <div style="margin-left:10px">-->
<!--              <p>{{item.goodsTitle}}【{{item.goodsSpec}}&nbsp;】</p>-->
<!--              <p>外部ERP编码：{{item.outerErpSkuId}}-->
<!--                <el-tag size="small">x {{item.quantity}}</el-tag>-->
<!--                </p>-->
<!--              </div>-->
<!--            </div>-->
<!--            </el-row>-->
<!--          </template>-->
<!--      </el-table-column>-->
      <el-table-column label="订单金额" align="center" prop="amount" :formatter="amountFormatter" >
        <template slot-scope="scope">
<!--          <p>商品总额：{{amountFormatter(null,null,scope.row.goodsAmount)}}</p>-->
          <div>
<!--            <span style="font-size: 10px">实付：</span>-->
            <span>{{amountFormatter(null,null,scope.row.payment)}}</span>
          </div>
<!--          <div><span style="font-size: 10px">平台优惠：</span>-->
<!--            <span>{{amountFormatter(null,null,scope.row.platformDiscount)}}</span>-->
<!--          </div>-->
<!--          <div><span style="font-size: 10px">商家优惠：</span>-->
<!--            <span>{{amountFormatter(null,null,scope.row.sellerDiscount)}}</span>-->
<!--          </div>-->
        </template>
      </el-table-column>
<!--      <el-table-column label="实付金额" align="center" prop="payment" :formatter="amountFormatter" />-->
<!--      <el-table-column label="订单备注" align="center" prop="remark" :show-overflow-tooltip="true">-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.buyerMemo}}<br />-->
<!--          {{scope.row.sellerMemo}}-->
<!--        </template>-->
<!--      </el-table-column>-->


      <el-table-column label="收件信息" prop="receiverName" >
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;
          {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />

        </template>
      </el-table-column>
      <el-table-column label="发货单号" prop="shippingNumber" >
      </el-table-column>
<!--      <el-table-column label="发货信息" align="center" prop="shippingNumber" >-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.shippingNumber}}&nbsp; {{scope.row.shippingCompany}}<br />-->
<!--          {{ parseTime(scope.row.shippingTime, '{y}-{m}-{d} {h}:{m}:{s}') }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 6px;">新订单</el-tag>
          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 21" style="margin-bottom: 6px;">待付款</el-tag>
          <el-tag v-if="scope.row.orderStatus === 22" style="margin-bottom: 6px;">锁定</el-tag>
          <el-tag v-if="scope.row.orderStatus === 29" style="margin-bottom: 6px;">删除</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>
          <!--          <br />-->
          <!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.omsPushStatus === 0">待推送</el-tag>-->
          <!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.omsPushStatus === 1">已推送</el-tag>-->

          <!--          <br />-->
          <!--           &lt;!&ndash; 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 &ndash;&gt;-->
          <!--           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
        </template>
      </el-table-column>
      <el-table-column label="确认状态" align="center" prop="auditStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.auditStatus === 0" style="margin-bottom: 6px;">待确认</el-tag>
          <el-tag v-if="scope.row.auditStatus === 1" style="margin-bottom: 6px;">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <div>
            <el-button style="padding-left: 6px;padding-right: 6px;" plain
              size="mini" v-if="scope.row.auditStatus === 0"
              type="success"
              icon="el-icon-share"
              @click="handleConfirm(scope.row)"
            >确认订单</el-button>
          </div>
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

    <!-- 订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
            <el-descriptions title="订单信息">
              <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
              <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

              <el-descriptions-item label="店铺">
                {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}
              </el-descriptions-item>
              <el-descriptions-item label="买家留言">
                {{form.buyerMemo}}
              </el-descriptions-item>
              <el-descriptions-item label="卖家留言">
                {{form.sellerMemo}}
              </el-descriptions-item>

              <el-descriptions-item label="备注">
                {{form.remark}}
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">
                {{ parseTime(form.createTime) }}
                <!-- <el-date-picker
                disabled
                  v-model="form.orderCreateTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="请选择订单创建时间">
                </el-date-picker> -->
              </el-descriptions-item>
              <el-descriptions-item label="支付时间"> {{ form.payTime }}</el-descriptions-item>
              <el-descriptions-item label="最后更新时间"> {{ form.updateTime }}</el-descriptions-item>

              <el-descriptions-item label="订单状态">
                <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
                <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已出库</el-tag>
                <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已发货</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="退款状态">
                <el-tag v-if="form.refundStatus === 1">无售后或售后关闭</el-tag>
                <el-tag v-if="form.refundStatus === 2">售后处理中</el-tag>
                <el-tag v-if="form.refundStatus === 3">退款中</el-tag>
                <el-tag v-if="form.refundStatus === 4">退款成功</el-tag>
              </el-descriptions-item>

            </el-descriptions>
            <el-descriptions title="付款信息">
              <el-descriptions-item label="商品总额">{{amountFormatter(null,null,form.goodsAmount,0)}}</el-descriptions-item>
              <el-descriptions-item label="订单金额">{{amountFormatter(null,null,form.amount,0)}}</el-descriptions-item>
              <el-descriptions-item label="实际支付金额">{{amountFormatter(null,null,form.payment,0)}}</el-descriptions-item>
              <el-descriptions-item label="平台优惠金额">{{amountFormatter(null,null,form.platformDiscount,0)}}</el-descriptions-item>
              <el-descriptions-item label="商家优惠金额">{{amountFormatter(null,null,form.sellerDiscount,0)}}</el-descriptions-item>
            </el-descriptions>


            <el-descriptions title="收货信息">
              <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
              <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
              <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>
              <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>
            </el-descriptions>
            <el-descriptions title="发货信息">

              <el-descriptions-item label="物流公司">{{form.shippingCompany}}</el-descriptions-item>
              <el-descriptions-item label="物流单号">{{form.shippingNumber}}</el-descriptions-item>
              <el-descriptions-item label="发货时间">{{form.shippingTime}}</el-descriptions-item>
            </el-descriptions>

            <el-divider content-position="center">订单商品</el-divider>
            <el-table :data="form.itemList"  style="margin-bottom: 10px;">
              <el-table-column label="序号" align="center" type="index" width="50"/>

              <el-table-column label="图片" width="60">
                <template slot-scope="scope">
                  <el-image style="width: 50px; height: 50px" :src="scope.row.goodsImg"></el-image>
                </template>
              </el-table-column>
              <el-table-column label="商品标题" prop="goodsTitle" width="300"></el-table-column>
              <el-table-column label="SKU" prop="goodsSpec" width="150"></el-table-column>
              <el-table-column label="sku编码" prop="skuNum"></el-table-column>
              <el-table-column label="单价" prop="goodsPrice"></el-table-column>
              <el-table-column label="数量" prop="quantity"></el-table-column>
              <el-table-column label="商品金额" prop="itemAmount"></el-table-column>
            </el-table>

            <el-divider content-position="center"  v-if="isAudit" >收件人</el-divider>

            <el-form-item label="收件人姓名" prop="receiverName" v-if="isAudit">
              <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" style="width:350px" />
            </el-form-item>
            <el-form-item label="收件人电话" prop="receiverMobile" v-if="isAudit">
              <el-input v-model="form.receiverMobile" placeholder="请输入收件人电话" style="width:350px" />
            </el-form-item>
            <el-form-item label="省市区" prop="provinces" v-if="isAudit">
              <el-cascader style="width:350px"
                           size="large"
                           :options="pcaTextArr"
                           v-model="form.provinces">
              </el-cascader>
            </el-form-item>
            <el-form-item label="详细地址" prop="address" v-if="isAudit">
              <el-input v-model="form.address" placeholder="请输入收件地址" style="width:350px" />
            </el-form-item>
            <!--        <el-form-item label="发货方式" prop="shipType" v-if="isAudit">-->
            <!--          <el-select v-model="form.shipType" placeholder="发货类型" style="width:350px">-->
            <!--            <el-option label="供应商代发" value="1"></el-option>-->
            <!--            <el-option label="仓库发货" value="0"></el-option>-->
            <!--          </el-select>-->
            <!--        </el-form-item>-->

          </el-form>
          <div slot="footer" class="dialog-footer" v-if="isAudit">
            <el-button type="primary" @click="submitConfirmForm" v-if="form.auditStatus===0">确认发货</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>

    </el-dialog>
  </div>
</template>

<script>
import {listOrder, getOrder,confirmOrder } from "@/api/offline/order";

import { listShop } from "@/api/shop/shop";
import Clipboard from "clipboard";

import {pcaTextArr} from "element-china-area-data";

export default {
  name: "OrderOffline",
  data() {
    return {
      pcaTextArr,
      // 遮罩层
      loading: true,
      pushLoading: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 店铺订单表格数据
      orderList: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      activeName: 'orderDetail',
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopId: null,
        startTime: null,
        endTime: null,
        refundStatus: null,
        orderStatus: null
      },
      // 表单参数
      form: {
        id:null,
        auditStatus:0
      },
      // 表单校验
      rules: {
        receiverName: [{ required: true, message: '不能为空' }],
        receiverMobile: [{ required: true, message: '不能为空' }],
        provinces: [{ required: true, message: '不能为空' }],
        address: [{ required: true, message: '不能为空' }],
      }
    };
  },
  created() {
    listShop({status: 1}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });

  },
  methods: {
    copyActiveCode(event,queryParams) {
      console.log(queryParams)
      const clipboard = new Clipboard(".tag-copy")
      clipboard.on('success', e => {
        this.$message({ type: 'success', message: '复制成功' })
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        // 不支持复制
        this.$message({ type: 'waning', message: '该浏览器不支持自动复制' })
        // 释放内存
        clipboard.destroy()
      })
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push('/order/offline_order_create');
    },
    /** 查询店铺订单列表 */
    getList() {
      console.log('=====搜索条件：=====',this.queryParams)
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
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
      this.orderTime=null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderNum)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    reset(){

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detailOpen = false;
      this.isAudit = false
      this.reset();
    },
    handleConfirm(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.form.provinces = []
        this.form.provinces.push(response.data.province)
        this.form.provinces.push(response.data.city)
        this.form.provinces.push(response.data.town)
        this.detailOpen = true;
        this.detailTitle = "确认订单";
        this.isAudit = true
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        // this.$nextTick(()=>{
        //   this.form.shipType = response.data.shipType
        // })
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
    submitConfirmForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          const form = {
            id:this.form.id,
            province:this.form.provinces[0],
            city:this.form.provinces[1],
            town:this.form.provinces[2],
            address:this.form.address,
            receiver:this.form.receiverName,
            mobile:this.form.receiverMobile
          }

          confirmOrder(form).then(response => {
            if(response.code===200){
              this.$modal.msgSuccess("订单确认成功");
              this.detailOpen = false;
              this.isAudit = false
              this.getList();
            }else{
              this.$modal.msgError(response.msg);
            }

          });

        }
      })
    },
  }
};
</script>
