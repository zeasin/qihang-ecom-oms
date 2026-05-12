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
      <el-form-item label="店铺" prop="shopId">
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
<!--          <el-option label="总部销售订单" value="0"></el-option>-->
<!--          <el-option-->
<!--            v-for="item in typeList"-->
<!--            :key="item.id"-->
<!--            :label="item.name"-->
<!--            :value="item.id">-->
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
          type="warning"
          plain
          :disabled="multiple"
          icon="el-icon-download"
          size="mini"
          @click="handlePushVendorShip"
        >推送供应商发货</el-button>
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
<!--          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>-->
          <el-tag type="success" v-if="scope.row.shopType===0">总部销售订单</el-tag>
          <el-tag type="info" v-else>{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : (typeList.find(x=>x.id === scope.row.shopType) ? typeList.find(x=>x.id === scope.row.shopType).name : '未知平台' ) }}</el-tag>

        </template>>
      </el-table-column>
      <el-table-column label="商品" width="450px">
        <template slot-scope="scope">
          <!--            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">-->
          <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
            <image-preview :src="scope.row.itemList[0].goodsImg" :width="40" :height="40"/>
            <div style="margin-left:10px">
              <div style="width: 350px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" :title="scope.row.itemList[0].goodsTitle">
                {{scope.row.itemList[0].goodsTitle}}
              </div>
              <div>
                <span style="color: #5a5e66;font-size: 11px">规格：</span>
                <el-tag size="small">{{getSkuValues(scope.row.itemList[0].goodsSpec)}}</el-tag>&nbsp;
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
          <div style="float: left;display: flex;align-items: center;padding-left: 50px" v-if="scope.row.itemList.length>1">
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
<!--&lt;!&ndash;                <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>&ndash;&gt;-->
<!--                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="商品标题" align="left" width="250px" prop="goodsTitle" >-->
<!--              <template slot-scope="scope">-->
<!--                {{scope.row.goodsTitle}}-->
<!--                &lt;!&ndash;                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>&ndash;&gt;-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
<!--                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true">-->
<!--              <template slot-scope="scope">-->
<!--                <div>{{getSkuValues(scope.row.goodsSpec)}}&nbsp;<br/>-->
<!--                  <el-tag size="small" type="info" v-if="scope.row.skuNum">{{scope.row.skuNum}}</el-tag>-->
<!--                </div>-->
<!--&lt;!&ndash;                {{ getSkuValues(scope.row.goodsSpec)}}&ndash;&gt;-->
<!--&lt;!&ndash;                {{scope.row.goodsSkuId}}&ndash;&gt;-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="规格ID" align="left" prop="skuNum" width="200">-->
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




      <el-table-column label="店铺" align="left" prop="shopId" width="150px" v-if="!isShop">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <br/>
            <el-tag size="small">{{shopListAll.find(x=>x.id == scope.row.shopId)?shopListAll.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-left: 8px;padding-right: 8px"
            type="primary"
            plain
            v-if="scope.row.orderStatus === 1"
            icon="el-icon-download"
            size="mini"
            @click="handlePushVendorShip(scope.row)"
          >推送供应商发货</el-button>
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
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1400px" append-to-body>
      <el-tabs v-model="activeName" >
        <el-tab-pane label="订单详情" name="orderDetail">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
            <el-descriptions title="订单信息">
              <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
              <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

              <el-descriptions-item label="店铺">
                {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}
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

              <el-descriptions-item label="订单状态">
                <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
                <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已出库</el-tag>
                <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已发货</el-tag>
              </el-descriptions-item>


            </el-descriptions>



            <el-descriptions title="收货信息">
              <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
              <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
              <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>
              <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>
            </el-descriptions>

          </el-form>

        </el-tab-pane>
        <el-tab-pane label="商品列表" name="orderItems" lazy>
          <el-table :data="form.itemVoList"  style="margin-bottom: 10px;">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>

            <el-table-column label="图片" prop="goodsImg" width="60">
              <template slot-scope="scope">
                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
              </template>
            </el-table-column>
            <el-table-column label="商品标题" prop="goodsTitle" width="260px">
              <template slot-scope="scope">
                {{scope.row.goodsTitle}}
                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="规格" prop="goodsSpec" width="150">
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.goodsSpec)}}
              </template>
            </el-table-column>
            <el-table-column label="sku编码" prop="skuNum"></el-table-column>
            <el-table-column label="平台SkuId" prop="skuId"></el-table-column>
            <el-table-column label="商品库SkuId" prop="goodsSkuId"></el-table-column>
            <el-table-column label="单价" prop="goodsPrice" :formatter="amountFormatter"></el-table-column>
            <!--            <el-table-column label="子订单金额" prop="itemAmount"></el-table-column>-->
            <!--            <el-table-column label="实付金额" prop="payment"></el-table-column>-->
            <el-table-column label="数量" prop="quantity"></el-table-column>
            <!--            <el-table-column label="是否赠品" prop="isGift">-->
            <!--              <template slot-scope="scope">-->
            <!--                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>-->
            <!--              </template>-->
            <!--            </el-table-column>-->
            <!--            <el-table-column label="状态" prop="orderStatus">-->
            <!--              <template slot-scope="scope">-->
            <!--                <el-tag v-if="scope.row.orderStatus === 1">待发货</el-tag>-->
            <!--                <el-tag v-if="scope.row.orderStatus === 2">已发货</el-tag>-->
            <!--                <el-tag v-if="scope.row.orderStatus === 3">已完成</el-tag>-->
            <!--                <el-tag v-if="scope.row.orderStatus === 11">已取消</el-tag>-->
            <!--              </template>-->
            <!--            </el-table-column>-->
            <el-table-column label="售后状态" prop="refundStatus">
              <template slot-scope="scope">
                <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
                <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
                <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

      </el-tabs>
    </el-dialog>

    <!-- 打包发货对话框 -->
    <el-dialog title="推送到供应商发货" :visible.sync="supplierShipOpen" width="950px" append-to-body :close-on-click-modal="false">
      <el-form ref="form3" :model="form3" :rules="rules3" label-width="80px" >
<!--        <el-divider content-position="center">订单列表</el-divider>-->
        <el-table :data="form3.orderList"  style="margin-bottom: 10px;">
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
<!--                    <div>-->
<!--                      <span style="color: #5a5e66;font-size: 11px">平台ID：</span> {{item.skuId}}-->
<!--                      <span style="color: #5a5e66;font-size: 11px">规格ID：</span> {{item.goodsSkuId}}-->
<!--                    </div>-->
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
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="form3.supplierId" filterable r placeholder="选择发货供应商" style="width:300px">
                <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVendorShipForm">推送到供应商发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {waitDistOrderList, getOrder } from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import { pushOrderToSupplier } from '@/api/shipping/shipOrder'
import Clipboard from 'clipboard'

import {listAllSupplier} from '@/api/goods/supplier'
import { listMerchant } from '@/api/shop/merchant'
import {getShopListData} from "@/utils/shopUtils";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "waitSendOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
      shipJdSelect: true,
      isMerchant: false,
      isShop: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
      cloudWarehouseShopList: [],
      cloudWarehouseShipperList: [],
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
      skuList: [],
      merchantList: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      typeList:[],
      logisticsList:[],
      supplierList:[],
      shopListAll:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      shipOpen:false,
      supplierShipOpen: false,
      activeName: 'orderDetail',
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopType: null,
        merchantId: null,
        shopId: null,
        startTime: null,
        endTime: null,
        // refundStatus: 1,
        // orderStatus: 1,
        // shipStatus:0
      },
      // 表单参数
      form: {
        itemVoList:[],
        warehouseId:null,
        shipperId:null,
        shopId:null,
        supplierId:null,
        orderList: [],
      },
      form1: {
        supplierId:null,
        orderList:[],
      },
      form2: {},
      form3: {
        supplierId:null,
        orderList:[]
      },

      // 表单校验
      rules: {
        // height: [{ required: true, message: '不能为空' }],
        // weight: [{ required: true, message: '不能为空' }],
        shippingNumber: [{ required: true, message: '不能为空' }],
        shippingCompany: [{ required: true, message: '不能为空' }],
        warehouseId: [{ required: true, message: '请选择仓库' }],
      },
      rules1: {
        supplierId: [{ required: true, message: '不能为空' ,trigger: 'change' }],
      },
      rules3: {
        warehouseId: [{ required: true, message: '不能为空' ,trigger: 'blur' }],
      }
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

          listShop({ merchantId: this.queryParams.merchantId }).then(response => {
            this.shopList = response.rows;
            // if (this.shopList.length > 0) {
            //   this.queryParams.shopId = this.shopList[0].id
            // }
            this.shopLoading = false
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
    listAllSupplier({isShipper:1}).then(response => {
      this.supplierList = response.rows;
    });
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
    })
    // listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
    //   this.merchantList = resp.rows
    //   if (this.merchantList.length > 0) {
    //     this.queryParams.merchantId = this.merchantList[0].id
    //   }
    //   if(resp.rows.length === 1&&resp.rows[0].id>0) {
    //     this.isMerchant = true;
    //   }
    //   listShop({ merchantId: this.queryParams.merchantId }).then(response => {
    //     this.shopList = response.rows;
    //     this.shopLoading = false
    //   });
    //   this.getList()
    // })
    //  listShop({}).then(response => {
    //     this.shopList = response.rows;
    //     this.shopLoading =false
    //   });
    // this.getList();
  },
  methods: {
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({name:query}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        })
      }
    },
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
      this.shipOpen = false
      this.open = false
      this.supplierShipOpen = false
    },
    /** 详情按钮操作 */
    handleDetail(row,tagInd) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        // this.$nextTick(()=>{
        //   this.form.shipType = response.data.shipType
        // })
        if(tagInd){
          this.activeName =tagInd
        }else{
          this.activeName='orderDetail'
        }
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
    /** 推送供应商发货 */
    handlePushVendorShip(row){
      console.log(row.id)
      if(row.id){
        this.ids=[]
        this.ids.push(row.id)
      }
      this.form3.orderList = this.orderList.filter(item => this.ids.includes(item.id))
      this.form3.warehouseId = null
      this.form3.shopId = null
      this.form3.shipperId = null
      this.supplierShipOpen = true;
    },
    // 推送到供应商发货Form
    submitVendorShipForm(){
      this.$refs["form3"].validate(valid => {
        if (valid) {
          pushOrderToSupplier(this.form3).then(resp =>{
            this.$modal.msgSuccess("推送供应商发货成功："+resp.msg);
            this.supplierShipOpen = false
            this.getList()
          })
        }
      })
    },
  }
};
</script>

