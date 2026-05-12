<template>
  <div class="app-container" >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierId">
        <el-select v-model="queryParams.supplierId" filterable  :placeholder="$t('system.vendor.select')" clearable @change="handleQuery">
          <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
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
<!--        <el-select v-model="queryParams.shopId"-->
<!--                   remote reserve-keyword :remote-method="searchShop"-->
<!--                   filterable   placeholder="选择店铺" clearable @change="handleQuery">-->
<!--          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">-->
<!--            <span style="float: left">{{ item.name }}</span>-->
<!--            <span style="float: right; color: #8492a6; font-size: 13px"  >-->
<!--             <el-tag>{{ typeList.find(x=>x.id === item.type) ? typeList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>-->
<!--           </span>-->
<!--          </el-option>-->
<!--          </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="发货状态" prop="sendStatus" >
        <el-select v-model="queryParams.sendStatus" placeholder="发货状态" clearable @change="handleQuery">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="shippingList" @selection-change="handleSelectionChange"  >
       <el-table-column type="selection" width="55"  align="center" />
<!--       <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200">
        <template slot-scope="scope">
        <el-button
          size="mini"
          type="text"
          icon="el-icon-view"
          @click="handleDetail(scope.row)"
        >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
<!--          <br/>-->
<!--          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : (typeList.find(x=>x.id === scope.row.shopType) ? typeList.find(x=>x.id === scope.row.shopType).name : '未知平台' ) }}</el-tag>-->
        </template>
      </el-table-column>
<!--       <el-table-column label="店铺" align="left" prop="shopId" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag>{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name:''  }}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--       <el-table-column label="子订单编号" align="center" prop="supplierId" />-->
<!--      <el-table-column label="订单日期" align="center" prop="orderDate" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="商品明细" align="center" width="760px" >-->
<!--        <template slot="header">-->
<!--          <table>-->
<!--            <th>-->
<!--              <td width="50px">图片</td>-->
<!--              <td width="250px" align="left">标题</td>-->
<!--              <td width="150" align="left">规格</td>-->
<!--              <td width="200" align="left">规格ID</td>-->
<!--              <td width="50" align="left">数量</td>-->
<!--            </th>-->
<!--          </table>-->
<!--        </template>-->
<!--        <template slot-scope="scope" >-->
<!--          <el-table :data="scope.row.itemList" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >-->
<!--            <el-table-column label="商品图片" width="50px">-->
<!--              <template slot-scope="scope">-->
<!--                &lt;!&ndash;                <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>&ndash;&gt;-->
<!--                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="商品标题" align="left" width="250px" prop="goodsName" >-->
<!--              <template slot-scope="scope">-->
<!--                {{scope.row.goodsName}}-->
<!--                &lt;!&ndash;                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>&ndash;&gt;-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true">-->
<!--              <template slot-scope="scope">-->
<!--                <div>{{getSkuValues(scope.row.skuName)}}&nbsp;<br/>-->
<!--                  <el-tag size="small" type="info" v-if="scope.row.skuCode">{{scope.row.skuCode}}</el-tag>-->
<!--                </div>-->
<!--                &lt;!&ndash;                {{ getSkuValues(scope.row.goodsSpec)}}&ndash;&gt;-->
<!--                &lt;!&ndash;                {{scope.row.goodsSkuId}}&ndash;&gt;-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="规格ID" align="left" prop="skuNum" width="230">-->
<!--              <template slot-scope="scope">-->
<!--                <div style="float: left;display: flex;align-items: center;padding-right: 20px" >-->
<!--                  <div style="margin-left:10px">-->
<!--                    <div>-->
<!--                      <span style="color: #5a5e66;font-size: 11px">平台ID：</span>-->
<!--                      {{scope.row.skuId}}&nbsp-->
<!--                    </div>-->
<!--                    <div>-->
<!--                      <span style="color: #5a5e66;font-size: 11px">规格ID：</span>-->
<!--                      {{scope.row.goodsSkuId}}&nbsp;&nbsp-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">-->
<!--              <template slot-scope="scope">-->
<!--                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品" width="450px">
        <template slot-scope="scope">
          <!--            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">-->
          <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
            <image-preview :src="scope.row.itemList[0].goodsImg" :width="40" :height="40"/>
            <div style="margin-left:10px">
              <div style="width: 350px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" :title="scope.row.itemList[0].goodsName">
                {{scope.row.itemList[0].goodsName}}
              </div>
              <div>
                <span style="color: #5a5e66;font-size: 11px">规格：</span>
                <el-tag size="small">{{getSkuValues(scope.row.itemList[0].skuName)}}</el-tag>&nbsp;
                <span>
                    <span style="color: #5a5e66;font-size: 11px">数量：</span>
                    <el-tag size="small">x {{scope.row.itemList[0].quantity}}</el-tag>
                  </span>
              </div>
<!--              <div>-->
<!--                  <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">平台ID：</span>-->
<!--                    {{scope.row.itemList[0].skuId}}&nbsp-->
<!--                  </span>-->
<!--                <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">商品库ID：</span>-->
<!--                    {{scope.row.itemList[0].goodsSkuId}}&nbsp;&nbsp-->
<!--                  </span>-->
<!--                <span v-if="scope.row.itemList[0].skuNum" style="color: #5a5e66;font-size: 11px">编码：{{scope.row.itemList[0].skuNum}}</span>-->

<!--              </div>-->
            </div>
          </div>
          <div style="float: left;display: flex;align-items: center;padding-left: 50px"  v-if="scope.row.itemList.length>1">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row,'orderItems')"
            >更多订单商品（{{scope.row.itemList.length}}）</el-button>
          </div>
          <!--            </el-row>-->
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="left" prop="shipperId" width="200px">
        <template slot-scope="scope">
          {{supplierList.find(x=>x.id == scope.row.shipperId)?supplierList.find(x=>x.id == scope.row.shipperId).name:''}}
        </template>
      </el-table-column>
<!--      <el-table-column label="物流公司" align="center" prop="waybillCompany" ></el-table-column>-->
      <el-table-column label="发货物流" align="center" prop="waybillCode">
        <template slot-scope="scope">
          <div>{{scope.row.waybillCompany}}</div>
          <el-button v-if="scope.row.waybillCode"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleWl(scope.row)"
          >{{scope.row.waybillCode}} </el-button>
        </template>
      </el-table-column>
<!--      <el-table-column label="订单状态" align="center" prop="orderStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 6px;">新订单</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 21" style="margin-bottom: 6px;">待付款</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 22" style="margin-bottom: 6px;">锁定</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 29" style="margin-bottom: 6px;">删除</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="打单状态" align="center" prop="waybillStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 0">未取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 1">已取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 2">已打印</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 3">已发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="发货状态" align="center" prop="sendStatus" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.sendStatus === 0">待备货</el-tag>
          <el-tag size="small" v-if="scope.row.sendStatus === 1">待发货</el-tag>
          <el-tag size="small" v-if="scope.row.sendStatus === 2">已发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="推送时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini" v-if="scope.row.sendStatus===1"
            plain
            @click="handleSupplierShipConfirm(scope.row,)"
          >供应商发货确认</el-button>
          <el-button
            size="mini"
            plain
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row,)"
          >详情</el-button>
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

    <!--发货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body >
      <div id="dialogContent">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >

        <el-form-item label="发货时间" prop="shipTime" >
          <el-date-picker clearable
            v-model="form.shipTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="物流公司" prop="logisticsCompany">
        <!--              <el-input v-model="form.shippingCompany" placeholder="请输入物流公司" style="width:300px" />-->
        <el-select v-model="form.logisticsCompany" filterable r placeholder="选择快递公司" style="width:300px">
          <el-option v-for="item in logisticsList" :key="item.logisticsId" :label="item.logisticsName" :value="item.logisticsId">
            <span style="float: left">{{ item.logisticsName }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px;">{{ getPlatformName(item.platformId) }}</span>
          </el-option>
        </el-select>
        </el-form-item>
        <el-form-item label="物流单号" prop="logisticsCode">
          <el-input v-model="form.logisticsCode" placeholder="请输入物流单号" style="width:300px" />
        </el-form-item>
      </el-form>
    </div>
      <div slot="footer" class="dialog-footer" >
        <el-button type="primary" @click="submitForm">确认发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1400px" append-to-body>
      <el-tabs v-model="activeName" >
        <el-tab-pane label="订单详情" name="orderDetail">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
            <el-descriptions title="订单信息">
              <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
              <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

              <el-descriptions-item label="平台">
                <el-tag size="small" v-if="form.shopType === 0">总部销售订单</el-tag>
                <el-tag size="small" v-if="form.shopType === 100">天猫</el-tag>
                <el-tag size="small" v-if="form.shopType === 300">拼多多</el-tag>
                <el-tag size="small" v-if="form.shopType === 400">抖店</el-tag>
                <el-tag size="small" v-if="form.shopType === 500">微信小店</el-tag>
                <el-tag size="small" v-if="form.shopType === 600">快手</el-tag>
                <el-tag size="small" v-if="form.shopType === 700">小红书</el-tag>
                <el-tag size="small" v-if="form.shopType === 200">京东POP</el-tag>
                <el-tag size="small" v-if="form.shopType === 280">京东自营</el-tag>
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
              </el-descriptions-item>
              <el-descriptions-item label="最后更新时间"> {{ parseTime(form.updateTime) }}</el-descriptions-item>
              <el-descriptions-item label=""> </el-descriptions-item>

              <el-descriptions-item label="发货状态">
                <el-tag v-if="form.sendStatus === 1">待发货</el-tag>
                <el-tag v-if="form.sendStatus === 2">已发货</el-tag>
              </el-descriptions-item>
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

          </el-form>

        </el-tab-pane>
        <el-tab-pane label="商品列表" name="orderItems" lazy>
          <el-table :data="form.itemList"  style="margin-bottom: 10px;">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>

            <el-table-column label="商品图片" prop="goodsImg" width="80">
              <template slot-scope="scope">
                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
              </template>
            </el-table-column>
            <el-table-column label="商品标题" prop="goodsName" width="300"></el-table-column>
            <el-table-column label="规格" prop="goodsSpec" width="150">
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.skuName)}}
              </template>
            </el-table-column>
            <el-table-column label="sku编码" prop="skuCode"></el-table-column>
            <el-table-column label="平台ID" prop="skuId"></el-table-column>
            <el-table-column label="商品库SkuId" prop="goodsSkuId"></el-table-column>

            <el-table-column label="数量" prop="quantity"></el-table-column>
<!--            <el-table-column label="是否赠品" prop="isGift">-->
<!--              <template slot-scope="scope">-->
<!--                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>-->
<!--                <el-tag type="success" v-else>非赠品</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->

          </el-table>
        </el-tab-pane>

      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>

import { listVendorShipOrder, supplierShipConfirm } from '@/api/shipping/vendor_shipping'
import {  listShop } from '@/api/shop/shop'
import {listAllSupplier} from "@/api/goods/supplier";
import Clipboard from 'clipboard'
import { getFavoriteList } from '@/api/shipping/ship_logistics'
import { amountFormatter } from '@/utils/zhijian'
import { wuliuguiji } from '@/api/shipping/logistics_tracking'

export default {
  name: "ShipStockupOrderList",
  // computed: {
  //   supplier() {
  //     return supplier
  //   }
  // },
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
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
      // 仓库订单发货表格数据
      shippingList: [],
      typeList:[],
      shopListAll:[],
      // 弹出层标题
      title: "",
      detailTitle: "",
      activeName: null,
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        shopType: null,
        shopId: null,
        supplierId: null,
        orderNum:null,
        sendStatus:null,
        // orderStatus:'1'
      },
      // 表单参数
      form: {
        orderId: null,
        logisticsCompany:null,
        logisticsCode:null,
        completeTime:null
      },
      // 平台映射（参考 EnumShopType）
      platformMap: {
        0: 'ERP内销订单',
        100: '淘宝天猫',
        200: '京东POP',
        280: '京东自营',
        300: '拼多多',
        400: '抖店',
        500: '微信小店',
        600: '快手小店',
        700: '小红书',
        901: '微店',
        911: '螳螂系统',
        999: '线下渠道',
        10000: '店铺订单'
      },
      shopList: [],
      skuList:[],
      supplierList:[],
      logisticsList:[],
      statusList: [
         {
          value: '1',
          label: '待发货'
        }, {
          value: '2',
          label: '已发货'
        }
      ],
      // 表单校验
      rules: {
        shipTime: [{ required: true, message: "不能为空", trigger: "blur" }],
        logisticsCompany: [{ required: true, message: "不能为空", trigger: "blur" }],
        logisticsCode: [{ required: true, message: "不能为空", trigger: "blur" }],
      },

    };
  },
  mounted() {

  },
  created() {
    listAllSupplier({}).then(response => {
      this.supplierList = response.rows;
      this.getList();
    });
    // listPlatform({status:0}).then(res => {
    //   this.typeList = res.rows;
    // })
    // listShop({}).then(response => {
    //   this.shopList = response.rows;
    //   this.shopLoading = false;
    // });
    this.getList();
    // if(this.$route.query.status){
    //   this.queryParams.status = this.$route.query.status
    // }else {
    //   this.queryParams.status = '0'
    // }
    // this.getList();
  },
  methods: {
    // 获取平台名称
    getPlatformName(platformId) {
      return this.platformMap[platformId] || '未知'
    },
    amountFormatter,
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
    handleWl(row){
      wuliuguiji({com:row.waybillCompany,code:row.waybillCode}).then(resp=>{
        if(resp.code === 200){
          this.traceDetailList = resp.data
          this.openWl = true
        }else {
          this.$modal.msgError("查询失败："+resp.msg)
        }

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
    /** 查询仓库订单发货列表 */
    getList() {
      this.loading = true;
      listVendorShipOrder(this.queryParams).then(response => {
        this.shippingList = response.rows;
        this.total = response.total;

        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.skuList = []
      this.open = false;
      this.skuIdUpdateOpen = false;
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderNum)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleSupplierShipConfirm(row){
      console.log("======供应商发货确认========",row)
      this.form.orderId = row.id
      getFavoriteList({shopType:row.shopType,status:1}).then(resp=>{
        this.logisticsList = resp.data || []
        this.form.shipTime = new Date()
        this.open =true
      })

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          supplierShipConfirm(this.form).then(response => {
            if(response.code === 200) {
              this.form = {}
              this.$modal.msgSuccess("确认发货成功");
              this.open = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg)
            }
          });

        }
      });
    },
    reset(){

    },
    /** 详情按钮操作 */
    handleDetail(row,tagInd) {
      this.reset();
      const id = row.id || this.ids
      this.form = row;
      if(tagInd){
        this.activeName =tagInd
      }else{
        this.activeName='orderDetail'
      }
      this.detailOpen = true;
      this.detailTitle = "订单详情";

      this.isAudit = false
    },
  }
};
</script>
