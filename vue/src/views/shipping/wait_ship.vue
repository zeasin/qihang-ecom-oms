<template>
  <div class="app-container">
    <el-form :model="printParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" @change="handleQuery">
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
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">线下渠道</span>
          </el-option>
        </el-select>
      </el-form-item>
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
          :disabled="single"
          icon="el-icon-download"
          size="mini"
          @click="handleShip"
        >手动发货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type=""
          plain
          :disabled="single"
          icon="el-icon-download"
          size="mini"
          @click="allocateShipmentToSupplier"
        >分配给供应商发货</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200">
        <template slot-scope="scope">
          <span>{{scope.row.orderNum}}</span>
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}</el-tag>
          <el-tag style="margin-left: 6px" v-if="scope.row.shopType===100">淘宝天猫</el-tag>
          <el-tag style="margin-left: 6px" v-if="scope.row.shopType===200">京东POP</el-tag>
          <el-tag style="margin-left: 6px" v-if="scope.row.shopType===300">拼多多</el-tag>
          <el-tag style="margin-left: 6px" v-if="scope.row.shopType===400">抖店</el-tag>
          <el-tag style="margin-left: 6px" v-if="scope.row.shopType===500">微信小店</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品明细" align="center" width="900px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">SKU名</td>
              <td width="200" align="left">Sku编码</td>
              <td width="150" align="left">系统SkuId</td>
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
            <el-table-column label="商品名" align="left" width="250px" prop="goodsTitle" >
              <template slot-scope="scope">
                {{scope.row.goodsTitle}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 11">已取消</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="SKU名" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.goodsSpec)}}
              </template>
            </el-table-column>
            <el-table-column label="Sku编码" align="left" prop="skuNum" width="200"/>
            <el-table-column label="系统SkuId" align="left" prop="erpSkuId" width="150"/>
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="amount" :formatter="amountFormatter"/>
      <el-table-column label="下单时间" align="center" prop="orderCreateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderTime) }}</span>
        </template>
      </el-table-column>


      <el-table-column label="收件人" prop="receiverName" width="200px">
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;
          {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />
        </template>
      </el-table-column>
      <el-table-column label="订单备注" align="center" >
        <template slot-scope="scope">
          <div style="color: #ed5565">{{scope.row.remark}}</div>
          <div style="color: #ed5565">{{scope.row.buyerMemo}}</div>
          <div style="color: #ed5565">{{scope.row.sellerMemo}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-right: 6px;padding-left: 6px"
            size="mini"  plain
            type="primary"
            icon="el-icon-view"
            @click="allocateShipmentToSupplier(scope.row)"
          >供应商发货</el-button>
          <el-button style="padding-right: 6px;padding-left: 6px"
                     size="mini" type="success" plain
                     icon="el-icon-guide"
                     @click="handleShip(scope.row)"
          >手动发货</el-button>

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



    <!-- 打包发货对话框 -->
    <el-dialog title="打包发货" :visible.sync="shipOpen" width="1100px" append-to-body :close-on-click-modal="false">

      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-descriptions title="订单信息">
          <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

          <el-descriptions-item label="店铺">
            {{ shopList.find(x=>x.id == form.shopId)?shopList.find(x=>x.id == form.shopId).name:'' }}
            <el-tag size="small" v-if="form.shopType === 5">微信小店</el-tag>

          </el-descriptions-item>
          <el-descriptions-item label="买家留言">
            {{form.buyerMemo}}
          </el-descriptions-item>
          <el-descriptions-item label="备注">
            {{form.remark}}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ parseTime(form.createTime) }}
          </el-descriptions-item>

          <el-descriptions-item label="订单状态">
            <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
            <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
            <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已签收</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="退款状态">
            <el-tag v-if="form.refundStatus === 1">无售后或售后关闭</el-tag>
            <el-tag v-if="form.refundStatus === 2">售后处理中</el-tag>
            <el-tag v-if="form.refundStatus === 3">退款中</el-tag>
            <el-tag v-if="form.refundStatus === 4">退款成功</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="">
<!--            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>-->
<!--            <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>-->
          </el-descriptions-item>
          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>



        </el-descriptions>

        <el-divider content-position="center">商品明细</el-divider>
        <el-table :data="form.itemList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="图片" prop="goodsImg" width="60">
            <template slot-scope="scope">
              <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImg"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="标题" prop="goodsTitle" ></el-table-column>
          <el-table-column label="规格" prop="goodsSpec"></el-table-column>
          <el-table-column label="sku编码" prop="skuNum"  width="180"></el-table-column>
          <!--          <el-table-column label="单价" prop="goodsPrice"></el-table-column>-->
          <el-table-column label="数量" prop="quantity" width="50"></el-table-column>
          <!-- <el-table-column label="商品金额" prop="itemAmount"></el-table-column> -->
        </el-table>
        <el-form-item label="包裹尺寸" prop="height">
          <el-input type="number" v-model.number="form.length" placeholder="长mm" style="width:90px" /> x
          <el-input type="number"  v-model.number="form.width" placeholder="宽mm" style="width:90px" /> x
          <el-input type="number" v-model.number="form.height" placeholder="高mm" style="width:90px" />
        </el-form-item>
        <el-form-item label="包裹重量" prop="weight">
          <el-input type="number" v-model.number="form.weight" placeholder="请输入包裹重量（单位g）" style="width:300px" />
        </el-form-item>
        <el-row>
          <el-col>
            <el-form-item label="物流公司" prop="shippingCompany">
              <!--              <el-input v-model="form.shippingCompany" placeholder="请输入物流公司" style="width:300px" />-->
              <el-select v-model="form.shippingCompany" filterable r placeholder="选择快递公司" style="width:300px">
                <el-option v-for="item in logisticsList" :key="item.id" :label="item.name" :value="item.id">
                  <span style="float: left">{{ item.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px" >{{item.number}}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="物流单号" prop="shippingNumber">
              <el-input v-model="form.shippingNumber" placeholder="请输入物流单号" style="width:300px" />
            </el-form-item>
            <el-form-item label="包装费用" prop="packageAmount">
              <el-input v-model="form.packageAmount" placeholder="请输入包装费用" style="width:300px" />
            </el-form-item>
            <el-form-item label="物流费用" prop="shippingCost">
              <el-input v-model="form.shippingCost" placeholder="请输入物流费用" style="width:300px" />
            </el-form-item>
            <el-form-item label="发货人" prop="shippingMan">
              <el-input v-model="form.shippingMan" placeholder="请输入发货人" style="width:300px" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShipForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配给供应商发货对话框 -->
    <el-dialog title="分配给供应商发货" :visible.sync="allocateShipmentOpen" width="1100px" append-to-body :close-on-click-modal="false">

      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-descriptions title="订单信息">
          <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

          <el-descriptions-item label="买家留言">
            {{form.buyerMemo}}
          </el-descriptions-item>
          <el-descriptions-item label="卖家留言">
            {{form.sellerMemo}}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ parseTime(form.createTime) }}
          </el-descriptions-item>


          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>



        </el-descriptions>

        <el-divider content-position="center">商品明细</el-divider>
        <el-table :data="form.itemList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="图片" prop="goodsImg" width="60">
            <template slot-scope="scope">
              <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImg"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="标题" prop="goodsTitle" ></el-table-column>
          <el-table-column label="规格" prop="goodsSpec"></el-table-column>
          <el-table-column label="sku编码" prop="skuNum"  width="180"></el-table-column>
          <!--          <el-table-column label="单价" prop="goodsPrice"></el-table-column>-->
          <el-table-column label="数量" prop="quantity" width="50"></el-table-column>
          <!-- <el-table-column label="商品金额" prop="itemAmount"></el-table-column> -->
        </el-table>
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="form.supplierId" filterable  placeholder="请选择供应商名称">
            <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收件人" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="请输入收件人" style="width:300px" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverMobile">
          <el-input type="number" v-model.number="form.receiverMobile" placeholder="请输入收件人" style="width:300px" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" style="width:300px" />
        </el-form-item>
        <el-form-item label="买家留言" prop="buyerMemo">
          <el-input type="textarea" v-model="form.buyerMemo" placeholder="请输入买家留言" style="width:300px" />
        </el-form-item>
        <el-form-item label="卖家留言" prop="sellerMemo">
          <el-input type="textarea" v-model="form.sellerMemo" placeholder="请输入卖家留言" style="width:300px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAllocateShipmentForm">分配给供应商发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  getOrder,
  manualShipmentOrder,
  allocateShipmentOrder,
  waitShipmentList
} from "@/api/order/order";

import {listShop} from "@/api/shop/shop";
import {listLogisticsStatus} from "@/api/shipping/logistics";
import {amountFormatter, parseTime} from "@/utils/zhijian";
import {getDicts} from "@/api/system/dict/data";
import { listSupplier } from '@/api/goods/supplier'

export default {
  name: "ShipmentWait",
  data() {
    return {
      // 遮罩层
      loading: true,
      getCodeOpen: false,
      // 选中数组
      ids: [],
      shopList: [],
      orderList: [],
      logisticsList: [],
      supplierList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      shipOpen: false,
      allocateShipmentOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopType:null,
        shopId:null,
      },
      // 打印参数
      printParams: {
        deliver: null,
        printer: null
      },
      // 表单参数
      form: {
        weight:0.0
      },
      printerList: [],
      deliverList: [],
      // 表单校验
      rules: {
        height: [{ required: true, message: '不能为空' }],
        weight: [{ required: true, message: '不能为空' }],
        shippingNumber: [{ required: true, message: '不能为空' }],
        shippingCompany: [{ required: true, message: '不能为空' }],
        packageAmount: [{ required: true, message: '不能为空' }],
        shippingCost: [{ required: true, message: '不能为空' }],
        supplierId: [{ required: true, message: '不能为空' }],
        receiverName: [{ required: true, message: '不能为空' }],
        receiverMobile: [{ required: true, message: '不能为空' }],
        address: [{ required: true, message: '不能为空' }],
      }
    };
  },
  created() {

    listShop().then(response => {
      this.shopList = response.rows;

      this.getList();
    });

  },
  methods: {
    amountFormatter,
    parseTime,
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      waitShipmentList(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.allocateShipmentOpen = false;
      this.shipOpen = false;
      this.getCodeOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
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
    handleLink(row) {
      this.reset();
      const id = row.id || this.ids
      getGoodsSku(id).then(response => {
        console.log('=====00000000============',response)
        this.form = response.data;
        this.open = true;
      });
    },

    // 分配给供应商发货
    allocateShipmentToSupplier(row){
      this.reset();
      const id = row.id || this.ids
      console.log('======',id)
      getOrder(id).then(response => {
        this.form = response.data;
        this.form.length=0
        this.form.width=0
        this.form.height=0
        this.form.weight=0.0
        this.form.shippingCost=0.0
        this.form.packageAmount=0.0
        this.allocateShipmentOpen = true;
        // this.detailTitle = "订单详情";
        listSupplier({pageNum: 1, pageSize: 100}).then(resp=>{
          this.supplierList = resp.rows
        })
      });
    },
    // 手动发货按钮
    handleShip(row){
      this.reset();
      const id = row.id || this.ids
      console.log('======',id)
      getOrder(id).then(response => {
        this.form = response.data;
        this.form.length=0
        this.form.width=0
        this.form.height=0
        this.form.weight=0.0
        this.form.shippingCost=0.0
        this.form.packageAmount=0.0
        listLogisticsStatus({shopType:response.data.shopType}).then(resp=>{
          this.logisticsList = resp.rows
        })
        // 获取系统配置的物流费用
        getDicts('logistics_cost').then(resp=>{
          console.log("=====获取配置的物流费用=====",resp)
        })
        this.shipOpen = true;
        // this.detailTitle = "订单详情";
      });
    },
    // 手动发货表单
    submitShipForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          manualShipmentOrder(this.form).then(resp =>{
            this.$modal.msgSuccess("手动发货成功");
            this.shipOpen = false
            this.getList()
          })
        }
      })
    },
    // 分配给供应商发货
    submitAllocateShipmentForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          allocateShipmentOrder(this.form).then(resp =>{
            if(resp.code==200){
              this.$modal.msgSuccess("分配发货成功");
              this.allocateShipmentOpen = false
              this.getList()
            }else{
              this.$modal.msgError(resp.msg);
            }

          })
        }
      })
    }
  }
};
</script>
