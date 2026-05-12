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
      <el-form-item label="店铺" prop="shopId" v-if="!isShop">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" :loading="shopLoading"
                   remote reserve-keyword :remote-method="searchShop"
                   filterable clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >
             <el-tag>{{ typeList.find(x=>x.id === item.type) ? typeList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>
           </span>
          </el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="平台" prop="shopType">-->
<!--        <el-select v-model="queryParams.shopType" placeholder="请选择平台" clearable @change="handleQuery">-->
<!--          <el-option-->
<!--            v-for="item in typeList"-->
<!--            :key="item.id"-->
<!--            :label="item.name"-->
<!--            :value="item.id">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="店铺" prop="shopId">-->
<!--        <el-select v-model="queryParams.shopId" placeholder="请选择店铺"-->
<!--                   remote reserve-keyword :remote-method="searchShop"-->
<!--                   filterable clearable @change="handleQuery">-->
<!--          <el-option-->
<!--            v-for="item in shopList"-->
<!--            :key="item.id"-->
<!--            :label="item.name"-->
<!--            :value="item.id">-->
<!--            <span style="float: left">{{ item.name }}</span>-->
<!--            <span style="float: right; color: #8492a6; font-size: 13px"  >-->
<!--             <el-tag>{{ typeList.find(x=>x.id === item.type) ? typeList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>-->
<!--           </span>-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="下单时间" prop="orderTime">
          <el-date-picker clearable
            v-model="orderTime" value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
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
          :disabled="multiple"
          icon="el-icon-download"
          size="mini"
          @click="handleShip"
        >推送到云仓发货</el-button>
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
          icon="el-icon-view"
          @click="handleDetail(scope.row)"
        >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
          <br/>
          <el-tag type="success" v-if="scope.row.shopType===0">总部销售订单</el-tag>
          <el-tag type="info" v-else>{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : (typeList.find(x=>x.id === scope.row.shopType) ? typeList.find(x=>x.id === scope.row.shopType).name : '未知平台' ) }}</el-tag>

          <!--          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : (typeList.find(x=>x.id === scope.row.shopType) ? typeList.find(x=>x.id === scope.row.shopType).name : '未知平台' ) }}</el-tag>-->
<!--          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>-->
        </template>>
      </el-table-column>

      <el-table-column label="商品明细" align="center" width="760px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">规格</td>
              <td width="230" align="left">规格ID</td>
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
            <el-table-column label="商品标题" align="left" width="250px" prop="goodsTitle" >
              <template slot-scope="scope">
                {{scope.row.goodsTitle}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <div>{{getSkuValues(scope.row.goodsSpec)}}&nbsp;<br/>
                  <el-tag size="small" type="info" v-if="scope.row.skuNum">{{scope.row.skuNum}}</el-tag>
                </div>
                <!--                {{ getSkuValues(scope.row.goodsSpec)}}-->
                <!--                {{scope.row.goodsSkuId}}-->
              </template>
            </el-table-column>
            <el-table-column label="规格ID" align="left" prop="skuNum" width="230">
              <template slot-scope="scope">
                <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
                  <div style="margin-left:10px">
                    <div>
                      <span style="color: #5a5e66;font-size: 11px">平台ID：</span>
                      {{scope.row.skuId}}&nbsp
                    </div>
                    <div>
                      <span style="color: #5a5e66;font-size: 11px">商品库ID：</span>
                      {{scope.row.goodsSkuId}}&nbsp;&nbsp
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="left" prop="shopId" width="150px" v-if="!isShop">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <br/>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="收件人" prop="receiverName" width="200px">
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;
          {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />

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
          <br />

           <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-left: 8px;padding-right: 8px"
                     type="primary"
                     plain
            size="mini"
            icon="el-icon-edit"
            @click="handleShip(scope.row)"
          >推送到云仓发货</el-button>
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
    <el-dialog title="推送到云仓发货" :visible.sync="shipOpen" width="950px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-divider content-position="center">订单列表</el-divider>
        <el-table :data="form.orderList"  style="margin-bottom: 10px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>
          <el-table-column label="订单号" prop="orderNum" width="200"></el-table-column>
          <el-table-column label="商品" width="450">
              <template slot-scope="scope">
                <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">

                <div style="float: left;display: flex;align-items: center;" >
                  <image-preview :src="item.goodsImg" :width="40" :height="40"/>
                  <div style="margin-left:10px">
                  <div>{{item.goodsTitle}}</div>
                    <div> <span style="color: #5a5e66;font-size: 11px">规格：</span>{{getSkuValues(item.goodsSpec)}}&nbsp;
                      <el-tag size="small"> {{item.skuNum}}</el-tag>
                    </div>
                   <div>
                     <span style="color: #5a5e66;font-size: 11px">平台ID：</span> {{item.skuId}}
                     <span style="color: #5a5e66;font-size: 11px">规格ID：</span> {{item.goodsSkuId}}
                   </div>
                    <div><span style="color: #5a5e66;font-size: 11px">数量：</span><el-tag size="small">x {{item.quantity}}</el-tag>
                    </div>
                  </div>
                </div>
                </el-row>
              </template>
          </el-table-column>

          <el-table-column label="收件人" prop="receiverName" width="200px">
            <template slot-scope="scope">
              {{scope.row.receiverName}}&nbsp;
              {{scope.row.receiverMobile}} <br />
              {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />

            </template>
          </el-table-column>
        </el-table>
<!--        <el-form-item label="包裹尺寸" prop="height">-->
<!--          <el-input type="number" v-model.number="form.length" placeholder="长mm" style="width:90px" /> x-->
<!--          <el-input type="number"  v-model.number="form.width" placeholder="宽mm" style="width:90px" /> x-->
<!--          <el-input type="number" v-model.number="form.height" placeholder="高mm" style="width:90px" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="包裹重量" prop="weight">-->
<!--          <el-input type="number" v-model.number="form.weight" placeholder="请输入包裹重量（单位g）" style="width:300px" />-->
<!--        </el-form-item>-->
        <el-row>
          <el-col>
            <el-form-item label="云仓" prop="warehouseId">
              <el-select v-model="form.warehouseId" filterable r placeholder="选择云仓" style="width:300px" @change="cloudWarehouseChange">
                <el-option v-for="item in cloudWarehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">
                  <span style="float: left">{{ item.warehouseName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='JDYC'">京东云仓</span>
<!--                  <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='JDYC' && item.jdlApiType==0">京东云仓-仓配一体</span>-->
<!--                  <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='JDYC' && item.jdlApiType==1">京东云仓-ERP</span>-->
                  <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JKYYC'">吉客云云仓</span>
                  <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>
                  <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知云仓</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="云仓店铺" prop="shopId" v-if="shipJdSelect||shipJkySelect">
              <el-select v-model="form.shopId" filterable r placeholder="选择云仓店铺" style="width:300px">
                <el-option v-for="item in cloudWarehouseShopList" :key="item.id" :label="item.shopName" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="承运商" prop="shipperId" v-if="shipJdSelect">
              <el-select v-model="form.shipperId" filterable r placeholder="选择承运商" style="width:300px">
                <el-option v-for="item in cloudWarehouseShipperList" :key="item.id" :label="item.shipperName" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShipForm">推送到云仓</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {waitDistOrderList, getOrder } from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import {pushToCloudWarehouse} from "@/api/shipping/shipOrder";

import {listCloudWarehouseShipper, listCloudWarehouseShop} from '@/api/cloud_warehouse/index'
import Clipboard from 'clipboard'
import { getCloudWarehouseList } from '@/api/cloud_warehouse'
import { getUserProfile } from '@/api/system/user'
import {listMerchant} from '@/api/shop/merchant'
import {getShopListData} from "@/utils/shopUtils";
import {listMerchantCloudWarehouse} from "@/api/store/warehouse";

export default {
  name: "waitSendOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
      supplierShipOpen: false,
      shipJdSelect: true,
      shipJkySelect: true,
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
      isMerchant: false,
      isShop: false,
      // 总条数
      total: 0,
      // 店铺订单表格数据
      orderList: [],
      skuList: [],
      typeList: [],
      merchantList:[],
      shopList:[],
      cloudWarehouseList:[],
      cloudWarehouseShopList:[],
      cloudWarehouseShipperList:[],
      shopListAll:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      shipOpen:false,
      activeName: 'orderDetail',
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopId: null,
        merchantId: null,
        startTime: null,
        endTime: null,
        // refundStatus: 1,
        // orderStatus: 1,
        // shipStatus:0
      },
      // 表单参数
      form: {
        warehouseId:null,
        shipperId:null,
        shopId:null,
        orderList: [],
      },
      form1: {},
      // 表单校验
      rules: {
        warehouseId: [{ required: true, message: '不能为空' }],
        shopId: [{ required: true, message: '不能为空' }],
        shipperId: [{ required: true, message: '不能为空' }],
      },
    };
  },
  async mounted() {
    // this.shopListAll = await getShopListData(); // 获取并设置 shopList
    getUserProfile().then(res => {
      if (res.data.userType == 0) {
        // 总部
        this.isMerchant = false;
        this.isShop = false
        listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
          this.merchantList = resp.rows
          if (this.merchantList.length > 0) {
            this.queryParams.merchantId = this.merchantList[0].id
          }
          // if(resp.rows.length === 1&&resp.rows[0].id>0) {
          //   this.isMerchant = true;
          // }
          listShop({ merchantId: this.queryParams.merchantId }).then(response => {
            this.shopList = response.rows;
            this.shopLoading = false
            // if (this.shopList.length > 0) {
            //   this.queryParams.shopId = this.shopList[0].id
            // }
            this.getList()
          });

        })
      }else if (res.data.userType == 20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId
          listShop({ merchantId: this.queryParams.merchantId }).then(response => {
            this.shopList = response.rows;
            if (this.shopList.length > 0) {
              this.queryParams.shopId = this.shopList[0].id
            }
            this.shopLoading = false
            this.getList()
          });

      }else if (res.data.userType == 40) {
        // 店铺
        this.isMerchant = true;
        this.isShop = true
        this.queryParams.shopId = res.data.deptId
        this.getList()
      }
    })
  },
  created() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
    })
    // listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
    //   this.merchantList = resp.rows
    //   if (this.merchantList.length > 0) {
    //     this.queryParams.merchantId = this.merchantList[0].id
    //   }
    //   // if(resp.rows.length === 1&&resp.rows[0].id>0) {
    //   //   this.isMerchant = true;
    //   // }
    //   listShop({ merchantId: this.queryParams.merchantId }).then(response => {
    //     this.shopList = response.rows;
    //     this.shopLoading = false
    //   });
    //   this.getList()
    // })
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        this.handleQuery()
      });

    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({name:query}).then(response => {
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
    rowItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
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
      waitDistOrderList(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if(this.queryParams.shopType){
        listShop({type:this.queryParams.shopType}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        });
      }
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    reset(){

    },
    cancel(){
      this.form.id = null
      this.form1 = {}
      this.open = false
      this.shipOpen = false
    },
    // 发货按钮
    handleShip(row){
      console.log('==========',row)
      if(row.id){
        this.ids=[]
        this.ids.push(row.id)
      }
      this.form= {
          warehouseId:null,
          shipperId:null,
          shopId:null,
          orderList: [],
      }
      this.form.orderList = this.orderList.filter(item => this.ids.includes(item.id))
      console.log('====选中的订单list==',this.form.orderList)
      // if(this.queryParams.shopId){
      //   getJdCloudWarehouseList({}).then(resp=>{
      //     this.cloudWarehouseList = resp.data
      //     listJdCloudWarehouseShop().then(resp=>{
      //       this.cloudWarehouseShopList = resp.data
      //       listJdCloudWarehouseShipper({}).then(resp=> {
      //         this.cloudWarehouseShipperList = resp.data
      //         this.shipOpen = true;
      //       })
      //     })
      //
      //   })
      this.cloudWarehouseList = []
      getUserProfile().then(res=> {
        console.log(res)
        this.userType = res.data.userType;
        if (this.userType === '00') {
          console.log("=========admin========")
          getCloudWarehouseList({}).then(resp => {
            this.cloudWarehouseList = resp.rows
            this.shipOpen = true;
          })
        } else if (this.userType === '20' || this.userType === '40') {
          console.log("=========商户或者店铺========")
          listMerchantCloudWarehouse({}).then(response => {
            if(response.data && response.data.length > 0) {
              this.cloudWarehouseList = []
              this.cloudWarehouseList = response.data
              // response.data.forEach((item)=>{
              //   this.cloudWarehouseList.push({
              //     id:item.warehouseId,
              //     warehouseName:item.warehouseName,
              //     warehouseType:item.warehouseType,
              //   })
              // })
              this.form.warehouseId = response.data[0].id
              this.cloudWarehouseChange()
            }
            this.shipOpen = true;
          })
        }
      })
      // }else {
      //   this.$modal.msgError("请选择店铺")
      // }

    },
    cloudWarehouseChange(){
      var filter = this.cloudWarehouseList.filter(item => item.id === this.form.warehouseId)
      console.log('===========选择了仓库',filter)
      if(filter){
        // this.$modal.msgSuccess("选择了云仓："+filter[0].id + "  "+filter[0].warehouseName+"  "+filter[0].warehouseType)
        if(filter[0].warehouseType==='JDYC'){
          this.form.shopId = null
          this.form.shipperId = null
          this.shipJdSelect = true
          listCloudWarehouseShop({warehouseId:this.form.warehouseId}).then(resp=>{
            this.cloudWarehouseShopList = resp.data
            if(this.cloudWarehouseShopList&&this.cloudWarehouseShopList.length>0){
              this.form.shopId = this.cloudWarehouseShopList[0].id
            }
            listCloudWarehouseShipper({warehouseId:this.form.warehouseId}).then(resp=> {
              this.cloudWarehouseShipperList = resp.data
              if(this.cloudWarehouseShipperList&&this.cloudWarehouseShopList.length>0){
                this.form.shipperId = this.cloudWarehouseShipperList[1].id
              }
            })
          })
        }else if(filter[0].warehouseType==='JKYYC'){
          this.form.shopId = null
          this.form.shipperId = 0
          this.shipJdSelect = false
          this.shipJkySelect = true
          listCloudWarehouseShop({warehouseId:this.form.warehouseId}).then(resp=>{
            this.cloudWarehouseShopList = resp.data
            if(this.cloudWarehouseShopList&&this.cloudWarehouseShopList.length>0){
              this.form.shopId = this.cloudWarehouseShopList[0].id
            }
          })
        }else {
          this.shipJdSelect = false
          this.shipJkySelect = false
          this.form.shopId = 0
          this.form.shipperId = 0
        }
      }
    },
    // 发货表单
    submitShipForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          pushToCloudWarehouse(this.form).then(resp =>{
            if(resp.code===200) {
              this.$modal.msgSuccess("推送云仓成功");
              this.shipOpen = false
              this.getList()
            }else{
              this.$modal.msgError(resp.msg)
            }
          })
        }
      })
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

  }
};
</script>

