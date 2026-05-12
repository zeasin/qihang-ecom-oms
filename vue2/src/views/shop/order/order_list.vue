<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户" prop="merchantId" v-if="!isMerchant">
        <el-select v-model="queryParams.merchantId"  placeholder="请选择商户"  @change="merchantChange">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
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
      <el-form-item label="下单时间" prop="orderTime">
        <el-date-picker clearable
                        v-model="queryParams.startTime" value-format="yyyy-MM-dd"
                        type="date" @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <!--订单状态0：新订单，1：待发货，2：已发货，3：已完成，11已取消；12退款中；13已关闭；21待付款；22锁定，29删除，101部分发货-->
          <el-option label="新订单" value="0" ></el-option>
          <el-option label="待发货" value="1"></el-option>
          <el-option label="部分发货" value="101"> </el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="完成" value="3"></el-option>
          <el-option label="订单取消" value="11"></el-option>
          <el-option label="已关闭" value="13"></el-option>
        </el-select>
      </el-form-item>
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
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >手动添加店铺订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取店铺订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handleConfirm"
        >重新推送选中订单到订单库</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleConfirm"-->
<!--        >批量确认订单</el-button>-->
<!--      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="left" prop="orderId" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.orderId}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderId" @click="copyActiveCode($event,scope.row.orderId)" ></i>
          <br/>
<!--          <el-tag type="info">{{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}</el-tag>-->
        </template>>
      </el-table-column>
<!--      <el-table-column label="订单号" align="center" prop="orderId" />-->
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name :'' }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品" width="350">
          <template slot-scope="scope">
            <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">

            <div style="float: left;display: flex;align-items: center;" >
              <el-image  style="width: 50px; height: 50px;" :src="item.img" v-if="item.img"></el-image>
              <div style="margin-left:10px">
              <p style="width: 280px; white-space: nowrap; overflow: hidden;  text-overflow: ellipsis;">{{item.title}}</p>
              <p>
                <el-tag size="small" v-if="item.skuName">{{ getSkuValues(item.skuName)}}</el-tag>
                <el-tag size="small" v-else>{{(item.title.split(' ').length >1 ? item.title.split(' ')[item.title.split(' ').length-1] :'无')}}</el-tag>
              </p>
                <p>
                <span>数量： </span>
                <el-tag size="small" type="danger">{{item.quantity}}</el-tag>
                <span style="margin-left: 10px">SkuId：{{item.erpGoodsSkuId}} </span>

              </p>

              </div>
            </div>
            </el-row>
          </template>
      </el-table-column>
      <el-table-column label="订单金额" align="center" prop="orderPrice" :formatter="amountFormatter">
        <template slot-scope="scope">
          <span>
          {{ amountFormatter(null,null,scope.row.orderAmount/100,0) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="left" prop="shopId" width="150px">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <br/>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="订单创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="收件人" align="left" prop="receiverName" >
        <template slot-scope="scope">
         <div>{{scope.row.receiverName}} </div>
          <div>{{scope.row.receiverPhone}}</div>
        </template>
      </el-table-column>
      <el-table-column label="省市区" align="left" >
        <template slot-scope="scope">
          <span v-if="scope.row.province">{{scope.row.province}}</span>
        <span v-if="scope.row.city">{{scope.row.city}}</span>
        <span v-if="scope.row.county">{{scope.row.county}}</span>
          <div>{{scope.row.address}}</div>
        </template>
      </el-table-column>
      <el-table-column label="发货信息" align="left" prop="receiverName" >
        <template slot-scope="scope">
          <span v-if="scope.row.logisticsOrderNo">
          <div>{{scope.row.logisticsPartnerCode}} </div>
          <div>{{scope.row.logisticsOrderNo}}</div>
          <div>{{parseTime(scope.row.shipDoneTime)}}</div>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="订单备注" align="center" >
        <template slot-scope="scope">
          <div style="color: #ed5565">{{scope.row.remark}}</div>
          <div style="color: #ed5565">{{scope.row.buyerMemo}}</div>
          <div style="color: #ed5565">{{scope.row.sellerMemo}}</div>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="status" >
        <template slot-scope="scope">
          <!--订单状态0：新订单，1：待发货，2：已发货，3：已完成，11已取消；12退款中；21待付款；22锁定，29删除，101部分发货-->
          <el-tag v-if="scope.row.orderStatus === 0 " size="small">新订单</el-tag>
          <el-tag v-if="scope.row.orderStatus === 1 " size="small">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2 " size="small">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3 " size="small">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11 " size="small">已取消</el-tag>
          <el-tag v-if="scope.row.orderStatus === 12 " size="small">退款中</el-tag>
          <el-tag v-if="scope.row.orderStatus === 13 " size="small">已关闭</el-tag>
          <el-tag v-if="scope.row.orderStatus === 21 " size="small">待付款</el-tag>
          <el-tag v-if="scope.row.orderStatus === 22 " size="small">锁定</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="tenant" align="center" prop="tenantId" v-if="isAdmin"/>-->
<!--      <el-table-column label="快递单号" align="center" prop="logisticsCode" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-right: 6px;padding-left: 6px;"
            size="mini"
            type=""
            icon="el-icon-refresh"
            :loading="pullLoading"
            @click="handlePullUpdate(scope.row)"
          >更新订单</el-button>

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
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1100px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
        <el-descriptions title="订单信息">
          <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{form.orderId}}</el-descriptions-item>
          <el-descriptions-item label="店铺">
            <el-tag > {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">
            {{ parseTime(form.orderTime) }}
          </el-descriptions-item>


          <el-descriptions-item label="订单状态">
            <el-tag v-if="form.orderStatus === 1 " size="small">待发货</el-tag>
            <el-tag v-if="form.orderStatus === 2 " size="small">已发货</el-tag>
            <el-tag v-if="form.orderStatus === 3 " size="small">已完成</el-tag>
            <el-tag v-if="form.orderStatus === 11 " size="small">已取消</el-tag>
            <el-tag v-if="form.orderStatus === 12 " size="small">退款中</el-tag>
            <el-tag v-if="form.orderStatus === 21 " size="small">待付款</el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="订单备注">
          <el-descriptions-item label="买家留言">{{form.buyerMemo}}</el-descriptions-item>
          <el-descriptions-item label="商家备注">{{form.sellerMemo}}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="付款信息">

          <el-descriptions-item label="商品总价">{{amountFormatter(null,null,form.goodsAmount/100,null)}}</el-descriptions-item>
          <el-descriptions-item label="订单金额">{{amountFormatter(null,null,form.orderAmount/100,null)}}</el-descriptions-item>
          <el-descriptions-item label="支付金额">{{amountFormatter(null,null,form.paymentAmount/100,null)}}</el-descriptions-item>

          <el-descriptions-item label="运费">{{amountFormatter(null,null,form.freight/100 ,null)}}</el-descriptions-item>
          <el-descriptions-item label="商家优惠">{{amountFormatter(null,null,form.sellerDiscount/100 ,null)}}</el-descriptions-item>
          <el-descriptions-item label="平台优惠">{{amountFormatter(null,null,form.platformDiscount/100 ,null)}}</el-descriptions-item>
<!--          <el-descriptions-item label="卖家应收">{{amountFormatter(null,null,form.orderSellerPrice ,null)}}</el-descriptions-item>-->


        </el-descriptions>


        <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverPhone}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.county}}{{form.town}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="发货信息">
          <!-- <el-descriptions-item label="发货方式">
            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>
              <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>
          </el-descriptions-item> -->
          <el-descriptions-item label="物流公司">{{form.logisticsPartnerCode}}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{form.logisticsOrderNo}}</el-descriptions-item>
          <el-descriptions-item label="最后发货时间"> {{ parseTime(form.shipDoneTime) }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="form.items"  style="margin-bottom: 10px;">
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="商品图片" width="80">
            <template slot-scope="scope">
              <image-preview :src="scope.row.img" :width="45" :height="45"/>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="title" ></el-table-column>
          <el-table-column label="规格" prop="skuName" >
            <template slot-scope="scope">
              <span v-if="scope.row.skuName">{{ getSkuValues(scope.row.skuName)}}</span>
              <span v-else>{{(scope.row.title.split(' ').length >1 ? scope.row.title.split(' ')[scope.row.title.split(' ').length-1] :'无')}}</span>
            </template>
          </el-table-column>
          <el-table-column label="Sku编码" prop="outerSkuId"></el-table-column>
          <el-table-column label="平台SkuId" prop="skuId"></el-table-column>
          <el-table-column label="单价" prop="goodsPrice" :formatter="amountFormatter">
            <template slot-scope="scope">
              {{amountFormatter(null,null,scope.row.salePrice/100) }}
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>

        </el-table>

        <el-form-item label="收件人姓名" prop="userName" v-if="isAudit">
          <el-input v-model="form.userName" placeholder="请输入收件人姓名" style="width:250px" />
        </el-form-item>
        <el-form-item label="收件人电话" prop="telNumber" v-if="isAudit">
          <el-input v-model="form.telNumber" placeholder="请输入收件人电话" style="width:250px" />
        </el-form-item>

        <el-form-item label="详细地址" prop="detailInfo" v-if="isAudit">
          <el-input v-model="form.detailInfo" placeholder="请输入收件地址" style="width:250px" />
        </el-form-item>

      </el-form>

    </el-dialog>
  </div>
</template>

<script>

import {getShop, listShop} from "@/api/shop/shop";

import {MessageBox} from "element-ui";

import {listShopOrder, pullOrder, orderConfirm, pullOrderDetail} from "@/api/shop/order";
import Clipboard from "clipboard";
import {getUserProfile} from "@/api/system/user";
import { listMerchant } from '@/api/shop/merchant'
import {getShopListData} from "@/utils/shopUtils";

export default {
  name: "ShopOrderList",
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: false,
    },
    shopId: {
      type: Number,
      required: false,
    },
    merchantId: {
      type: Number,
      required: false,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      isMerchant: false,
      isShop: false,
      isAdmin: false,
      isAudit: false,
      detailOpen: false,
      detailTitle: '',
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      multiple: true,
      // 总条数
      total: 0,
      // 淘宝订单表格数据
      orderList: [],
      shopList:[],
      merchantList:[],
      orderTime:null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantId: null,
        shopId: null,
        shopType: null,
        startTime: null,
        endTime: null,
        orderId: null,
        status: null
      },
      // 表单参数
      form: {
      },
      rules: {
      }
    };
  },
  mounted() {

  },
  created() {
    getUserProfile().then(res => {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isMerchant = false
        this.isShop = false
        listMerchant({ }).then(resp => {
          this.merchantList = resp.rows
          if (this.merchantList.length > 0) {
            if (this.merchantId) this.queryParams.merchantId = parseInt(this.merchantId);
            else this.queryParams.merchantId = this.merchantList[0].id
          }
          listShop({ merchantId: this.queryParams.merchantId,type:this.shopType }).then(response => {
            this.shopList = response.rows;
            if (this.shopList.length > 0) {
              if(this.shopId) this.queryParams.shopId = parseInt(this.shopId);
              else this.queryParams.shopId = this.shopList[0].id
            }
            this.shopLoading = false
            this.getList()
          });
        })
      }else if (res.data.userType == 20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId;
        this.merchantList = []
        this.merchantList.push({id:res.data.deptId,name:res.data.nickName})
        this.shopList = []
        listShop({ merchantId: this.queryParams.merchantId,type:this.shopType }).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            if(this.shopId) this.queryParams.shopId = parseInt(this.shopId);
            else this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.getList()
        });
      }else if (res.data.userType == 40) {
        this.isMerchant = true;
        this.isShop = true
        this.merchantList = []
        this.shopList = []
        this.queryParams.shopId = res.data.deptId;
        getShop(res.data.deptId).then(resp => {
          this.shopList.push({id:resp.data.id,name:resp.data.name})
          this.shopType = resp.data.type
          console.log('重新赋值之后的shopType：',this.shopType )
          this.getList()
        })

      }

    })


    // listShop({ type: this.shopType }).then(response => {
    //   this.shopList = response.rows;
    //   // if (this.shopList && this.shopList.length > 0) {
    //   //   this.queryParams.shopId = this.shopList[0].id
    //   // }
    //   this.shopLoading = false;
    // });
    // this.getList();
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({type:this.shopType,name:query}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        })
      }
    },
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
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value || item.value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    /** 查询淘宝订单列表 */
    getList() {
      this.loading = true;
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        if(!this.queryParams.startTime){
          this.queryParams.startTime = null
          this.queryParams.endTime = null
        }
      }
      this.queryParams.shopType = this.shopType
      listShopOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.detailOpen = false;
      this.saleAfterOpen = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pullLoading = false
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
    handleAdd() {
      this.$router.push('shop_order_create?shopType=' + (this.shopType?this.shopType:'')+"&shopId=" + (this.queryParams.shopId?this.queryParams.shopId:''));
    },
    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullOrder({shopId:this.queryParams.shopId,startTime:this.queryParams.startTime}).then(response => {
          console.log('拉取订单接口返回=====',response)
          if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            this.pullLoading = false
            this.getList()
          }
          else if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/list",query:{type:3}})
            }).catch(() => {
              isRelogin.show = false;
            });
          }else {
            this.$modal.msgError(JSON.stringify(response));
            this.pullLoading = false
          }

        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }

      // this.$modal.msgSuccess("请先配置API");
    },
    handlePullUpdate(row) {
      // 接口拉取订单并更新
      this.pullLoading = true
      pullOrderDetail({shopId:row.shopId,orderId:row.orderId}).then(response => {
        console.log('拉取订单详情返回接口返回=====',response)
        // if(response.code>1000){
        //   this.$modal.msgError(response.msg);
        // }else{
        //   this.$modal.msgSuccess(JSON.stringify(response));
        //   this.pullLoading = false
        //   this.getList()
        // }
        if(response.code === 200){
          this.$modal.msgSuccess(JSON.stringify(response));
          this.pullLoading = false
          this.getList()
        }
        else if(response.code === 1401) {
          MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
            this.$router.push({path:"/shop/list",query:{type:3}})
          }).catch(() => {
            isRelogin.show = false;
          });
        }else {
          this.$modal.msgError(JSON.stringify(response));
          this.pullLoading = false
        }

      })
    },
    handleConfirm(row) {
      const ids = row.id || this.ids;
      console.log('批量确认订单:',ids)
      this.$modal.confirm('是否重新推送订单到订单库？').then(function() {
        return orderConfirm({ids:ids});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
    handleDetail(row) {
      this.reset();
      this.form = row;
      this.detailOpen = true;
      this.detailTitle = "订单详情";

      this.isAudit = false
    },
  }
};
</script>
