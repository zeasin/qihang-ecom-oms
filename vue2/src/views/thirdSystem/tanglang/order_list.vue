<template>
  <div >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">

      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品ID" prop="orderId">
        <el-select v-model="queryParams.goodsId" placeholder="商品过滤" clearable filterable @change="handleQuery">
          <el-option
            v-for="item in goodsList"
            :key="item.productId"
            :label="item.title"
            :value="item.productId">
          </el-option>
<!--          <el-option label="一气解伤寒" value="71842" ></el-option>-->
<!--          <el-option label="王氏五行方阵中医" value="71841"></el-option>-->
<!--          <el-option label="岐黄妙法-五艺贯通" value="75515"></el-option>-->
<!--          <el-option label="岭南医宗长寿秘法" value="71840"></el-option>-->
<!--          <el-option label="古法九龙回阳功" value="75735"></el-option>-->
<!--          <el-option label="王氏一针调气百病消" value="73579"></el-option>-->
<!--          <el-option label="王氏岭南医宗正骨3天2夜" value="76151"></el-option>-->
        </el-select>
      </el-form-item>
      <el-form-item label="会员手机号" prop="platformAccount">
        <el-input
          v-model="queryParams.platformAccount"
          placeholder="请输入会员手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下单时间" prop="orderTime">
          <el-date-picker clearable @change="handleQuery"
            v-model="orderTime" value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="confirmStatus">
        <el-select v-model="queryParams.confirmStatus" placeholder="请选择审核状态" clearable @change="handleQuery">
          <el-option label="已确认" value="1" ></el-option>
          <el-option label="待确认" value="0"></el-option>

        </el-select>
      </el-form-item>
      <el-form-item label="订单状态" prop="platformOrderStatus">
        <el-select v-model="queryParams.platformOrderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="已支付" value="ORDER_STATUS_PAID" ></el-option>
          <el-option label="已作废" value="ORDER_INVALID"></el-option>
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
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull()"
        >API拉取教务订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="primary"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull2()"
        >API拉取营销订单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="订单ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
          >{{scope.row.orderId}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderId" @click="copyActiveCode($event,scope.row.orderId)" ></i>
          <br />
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="商品" width="350">
          <template slot-scope="scope">
            <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">

            <div style="float: left;display: flex;align-items: center;" >
              <div style="margin-left:10px">
              <p>{{item.title}}
                <span v-if="item.skuName">【{{item.skuName}}&nbsp;】</span>
              </p>
              <p>
                <span v-if="item.productId">商品ID：{{item.productId}}</span>
                <span v-if="item.skuId">规格ID：{{item.skuId}}</span>
                <span v-if="item.skuCode">规格编码：{{item.skuCode}}</span>
                <el-tag size="small">x {{item.quantity}}</el-tag>
                </p>
              </div>
            </div>
            </el-row>
          </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="amount" >
        <template slot-scope="scope">
          <div>
            <span style="font-size: 10px">实付：</span>
            <span>{{amountFormatter(null,null,scope.row.paymentAmount/100)}}</span>
          </div>
          <div><span style="font-size: 10px">优惠：</span>
            <span>{{amountFormatter(null,null,scope.row.discountAmount/100)}}</span>
          </div>
        </template>
      </el-table-column>
<!--      <el-table-column label="Tag" prop="isGift">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.hasGift > 0">有赠品</el-tag>-->
<!--          <el-tag type="danger" v-if="scope.row.hasGift == -1">赠品订单</el-tag>-->
<!--          <el-tag type="success" v-if="scope.row.orderMode == 1">手工订单</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="订单备注" align="center" prop="remark" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div v-if="scope.row.buyerMemo">{{scope.row.buyerMemo}}</div>
          <div v-if="scope.row.sellerMemo">{{scope.row.sellerMemo}}</div>
          <div v-if="scope.row.remark"> {{scope.row.remark}}</div>
          <el-tag v-if="scope.row.hasGift > 0">有赠品</el-tag>
          <el-tag type="danger" v-if="scope.row.hasGift == -1">赠品订单</el-tag>
          <el-tag type="success" v-if="scope.row.orderMode == 1">手工订单</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会员ID" prop="platformUserId" width="200px"></el-table-column>
      <el-table-column label="会员手机号" prop="platformAccount" width="200px"></el-table-column>
      <el-table-column label="状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <el-tag  style="margin-bottom: 6px;">{{ scope.row.platformOrderStatusText }}</el-tag>
          <br />
          <el-tag v-if="scope.row.confirmStatus === 0" style="margin-bottom: 6px;">待确认</el-tag>
          <el-tag v-if="scope.row.confirmStatus === 1" style="margin-bottom: 6px;">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
<!--          {{ parseTime(scope.row.orderTime * 1000) }}  -->
          {{scope.row.orderTimeText}}
<!--          {{ scope.row.orderTime }}-->
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.confirmStatus === 0"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleConfirm(scope.row)"
          >审核订单</el-button>

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


    <!-- 审核订单 -->
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body :close-on-click-modal="false">

            <el-descriptions title="订单信息">
              <el-descriptions-item label="ID">{{detailForm.id}}</el-descriptions-item>
              <el-descriptions-item label="订单号">{{detailForm.orderId}}</el-descriptions-item>
              <el-descriptions-item label="备注">
                {{detailForm.remark}}
              </el-descriptions-item>
              <el-descriptions-item label="下单时间">
                {{ parseTime(detailForm.orderTime) }}
              </el-descriptions-item>
              <el-descriptions-item label="订单状态">
                <el-tag style="margin-bottom: 6px;">{{detailForm.platformOrderStatusText}}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="退款状态">
                <el-tag v-if="detailForm.refundStatus === 1">无售后或售后关闭</el-tag>
                <el-tag v-if="detailForm.refundStatus === 2">售后处理中</el-tag>
                <el-tag v-if="detailForm.refundStatus === 3">退款中</el-tag>
                <el-tag v-if="detailForm.refundStatus === 4">退款成功</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="螳螂学员ID">{{detailForm.platformUserId}}</el-descriptions-item>
              <el-descriptions-item label="螳螂学员手机号">{{detailForm.platformAccount}}</el-descriptions-item>
            </el-descriptions>
          <el-table :data="detailForm.items"  style="margin-bottom: 10px;">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>
            <el-table-column label="平台ID" prop="productId"  width="100"></el-table-column>
            <el-table-column label="商品标题" prop="title"  ></el-table-column>
<!--            <el-table-column label="规格" prop="skuName" width="150"></el-table-column>-->
<!--            <el-table-column label="规格编码" prop="skuNum"></el-table-column>-->
            <el-table-column label="单价" prop="salePrice">
              <template slot-scope="scope">
                {{amountFormatter(null,null,scope.row.salePrice/100)}}
              </template>
            </el-table-column>
<!--            <el-table-column label="子订单金额" prop="itemAmount"></el-table-column>-->
<!--            <el-table-column label="实付金额" prop="payment"></el-table-column>-->
            <el-table-column label="数量" prop="quantity"></el-table-column>
<!--            <el-table-column label="是否赠品" prop="isGift">-->
<!--              <template slot-scope="scope">-->
<!--                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
          </el-table>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-form-item label="商品明细">
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5">
              <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSShopOrderItem">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-table style="padding-left: 108px;padding-bottom: 20px" :data="form.itemList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品" prop="goodsName" width="200">
          </el-table-column>
          <el-table-column label="" prop="goodsImg" width="70">
            <template slot-scope="scope">
              <image-preview :src="scope.row.colorImage" :width="40" :height="40"/>
            </template>
          </el-table-column>
          <el-table-column label="商品规格" prop="skuName"></el-table-column>
          <el-table-column label="Sku编码" prop="skuCode" ></el-table-column>
          <el-table-column label="商品数量" prop="quantity" >
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.quantity" placeholder="请输入商品数量" style="width: 100px"/>
            </template>
          </el-table-column>
        </el-table>
        <el-col :span="24" style="padding-top: 20px"></el-col>
          <el-form-item label="智能识别" prop="addressAll">
            <el-input type="textarea" v-model="form.addressAll" placeholder="" style="width: 800px" /><br/>
            <el-button size="mini" @click="parseAddress">识别</el-button>
          </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" style="width:300px"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" style="width:300px"/>
        </el-form-item>
        <el-form-item label="省市区" prop="provinces">
          <el-cascader style="width:300px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" style="width:600px"/>
        </el-form-item>
        <el-form-item label="订单备注" prop="remark" >
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" style="width:300px"/>
        </el-form-item>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="1" :merchantId="5" ref="popup"></PopupSkuList>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getToken} from "@/utils/auth";
import {
  listTanglangOrderList,
  virtualOrderConfirmAndNew,
  listTanglangGoodsItems
} from '@/api/shop/tanglang'

import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import Clipboard from "clipboard";
import {getShopOrder, pullKcOrder, pullOrder, pullScrmOrder} from "@/api/shop/order";
import PopupSkuList from '@/views/goods/PopupSkuList.vue'
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import {parseTime} from "../../../utils/zhijian";
import { listShop } from '@/api/shop/shop'

export default {
  name: "GoodsList",
  components: { Treeselect,PopupSkuList },
  data() {
    return {
      pcaTextArr,
      importOpen:false,
      headers: { 'Authorization': 'Bearer ' + getToken() },
      // 遮罩层
      loading: true,
      pullLoading: false,
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
      orderList: [],
      goodsList: [],
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      activeName: '',
      orderTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        goodsId: null,
        shopId: null,
        startTime: null,
        endTime: null,
        refundStatus: null,
        orderStatus: null,
        confirmStatus: '0',
        platformAccount: null,
      },
      // 表单参数
      detailForm: {
        items:[]
      },
      form:{
        shopOrderId:null,
        name:null,
        phone:null,
        province:null,
        city:null,
        county:null,
        address:null,
        addressAll:null,
        remark:null,
        provinces:[],
        itemList:[],
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "change" }],
        phone: [{ required: true, message: "不能为空", trigger: "change" }],
        provinces: [{ required: true, message: "不能为空", trigger: "change" }],
        address: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    listTanglangGoodsItems({bindShipSku:1,pageNum:1,pageSize:100}).then(response => {
      this.goodsList = response.rows;
      // if(this.goodsList && this.goodsList.length > 0) {
      //   this.queryParams.goodsId = this.goodsList[0].productId;
      // }
      listShop({type:911}).then(response => {
        this.shopList = response.rows;
        if (this.shopList && this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.getList();
      });

    });
    let currentDate = new Date();

    let year = currentDate.getFullYear();
    let month = String(currentDate.getMonth() + 1).padStart(2, '0');
    let day = String(currentDate.getDate()).padStart(2, '0');

    let formattedDate = `${year}-${month}-${day}`;
    // this.orderTime.push(formattedDate);
    // this.orderTime.push(formattedDate);
  },
  methods: {
    /** 将时间选择器默认选为当天的日期 */

    parseTime,
    copyActiveCode(event, queryParams) {
      console.log(queryParams)
      const clipboard = new Clipboard(".tag-copy")
      clipboard.on('success', e => {
        this.$message({type: 'success', message: '复制成功'})
        // 释放内存
        clipboard.destroy()
      })
      clipboard.on('error', e => {
        // 不支持复制
        this.$message({type: 'waning', message: '该浏览器不支持自动复制'})
        // 释放内存
        clipboard.destroy()
      })
    },
    parseAddress() {
      if (this.form.addressAll) {
        // 正则表达式匹配省、市、区。包括直辖市、自治区等特殊情况。
        // const regex = /([^\d]+(?:省|自治区|特别行政区))(?:([^\d]+市))?(?:([^\d]+区|县|自治县|旗))/;
        // const regex = /([^\d]+(?:省|自治区|特别行政区))(?:([^\d]+市))?(?:([^\d]+(?:区|县|自治县|旗)))?/;
        const pattern = /^(.*?[省|自治区|市])(.*?[市|区|县])(.*?[区|县|市])(.*)$/;
        // const match = address.match(pattern);
        const match = this.form.addressAll.match(pattern);
        this.form.provinces = [];
        if (match) {
          const province = match[1]
          const city = match[2] || match[1]
          const district = match[3]
          const address = match[4].trim()
          // this.form.provinces.push(match[1])
          // this.form.provinces.push(match[2] || match[1])
          // this.form.provinces.push(match[3])
          // // this.form.provinces = [];
          this.form.provinces.push(province)
          this.form.provinces.push(city)
          this.form.provinces.push(district)
          this.form.address = address
          console.log('==============省市区=======',province,city,district,address)
        }


      }
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 查询商品管理列表 */
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
      listTanglangOrderList(this.queryParams).then(response => {
        this.orderList = response.rows;
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
      this.detailForm = {};
      this.detailForm.items = []
      this.detailForm.member={}
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.pullLoading =false
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      this.form.itemList.push(...data)
    },
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    handleSShopOrderItemSelectionChange(selection) {
      this.checkedSShopOrderItem = selection.map(item => item.index)
    },
    handleDeleteSShopOrderItem() {
      if (this.checkedSShopOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的商品数据");
      } else {
        const sShopOrderItemList = this.form.itemList;
        const checkedSShopOrderItem = this.checkedSShopOrderItem;
        this.form.itemList = sShopOrderItemList.filter(function(item) {
          return checkedSShopOrderItem.indexOf(item.index) == -1
        });
      }
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    //审核订单
    handleConfirm(row){
      this.reset()
      getShopOrder(row.id).then(response => {
        this.detailForm = response.data;
        this.form.shopOrderId = response.data.id
        this.form.itemList = response.data.itemList;
        this.open = true;
        this.title = "审核订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(!this.form.itemList || this.form.itemList.length ==0){
            this.$modal.msgError("请添加商品");
            return
          }
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.county = this.form.provinces[2]
          virtualOrderConfirmAndNew(this.form).then(response => {
            this.$modal.msgSuccess("订单确认成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    // 拉取订单
    handlePull() {
      if(!this.queryParams.shopId){
        this.$modal.msgError('请选择店铺');
        return;
      }
      this.pullLoading = true
      pullKcOrder({shopId:this.queryParams.shopId,startTime:this.queryParams.startTime,endTime:this.queryParams.endTime}).then(resp => {
        if (resp.code === 200) {
          this.$modal.msgSuccess(resp.msg);
          this.getList()
        } else {
          this.$modal.msgError(resp.msg);
        }

        this.pullLoading = false
      })
      // this.$modal.msgSuccess("请先配置API");
    },
    // 拉取订单
    handlePull2() {
      if(!this.queryParams.shopId){
        this.$modal.msgError('请选择店铺');
        return;
      }
      this.pullLoading = true
      pullScrmOrder({shopId:this.queryParams.shopId,startTime:this.queryParams.startTime,endTime:this.queryParams.endTime}).then(resp => {
        if (resp.code === 200) {
          this.$modal.msgSuccess(resp.msg);
          this.getList()
        } else {
          this.$modal.msgError(resp.msg);
        }

        this.pullLoading = false
      })
      // this.$modal.msgSuccess("请先配置API");
    },
  }
};
</script>
