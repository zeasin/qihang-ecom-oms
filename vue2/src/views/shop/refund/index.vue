<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="退款单号" prop="afterId">
        <el-input
          v-model="queryParams.afterId"
          placeholder="请输入退款单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="tid">
        <el-input
          v-model="queryParams.tid"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable @change="handleQuery">
          <!--售后类型。(1-售前退款(取消订单) 10-退货 20-换货 30-维修 40-上门服务 80-补发商品 90-补款 91-返现 11-仅退款)-->
          <el-option label="售前退款" value="1" ></el-option>
          <el-option label="仅退款" value="11" ></el-option>
          <el-option label="退货" value="10"></el-option>
          <el-option label="换货" value="20"> </el-option>
          <el-option label="维修" value="30"> </el-option>
          <el-option label="补发商品" value="80"> </el-option>
          <el-option label="补款" value="90"> </el-option>
          <el-option label="返现" value="91"> </el-option>
        </el-select>
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
<!--      <el-form-item label="申请时间" prop="created">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.created"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择退款申请时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->

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
          icon="el-icon-download"
          size="mini"
          @click="handleAddRefund"
        >手动添加售后</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取新退款</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handlePushOms"-->
<!--        >重新推送到订单库</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taoRefundList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <!-- <el-table-column label="${comment}" align="center" prop="id" /> -->
      <el-table-column label="退款单号" align="center" prop="afterId" width="220px"/>
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
          <span>{{ shopList.find(x=>x.id == scope.row.shopId).name  }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type" >
        <template slot-scope="scope">
          <!--售后类型。(1-售前退款(取消订单) 10-退货 20-换货 30-维修 40-上门服务 80-补发商品 90-补款 91-返现 11-仅退款)-->
          <el-tag size="small" v-if="scope.row.type === 1"> 售前退款</el-tag>
          <el-tag size="small" v-if="scope.row.type === 11"> 仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.type === 10"> 退货</el-tag>
          <el-tag size="small" v-if="scope.row.type === 20"> 换货</el-tag>
          <el-tag size="small" v-if="scope.row.type === 30"> 维修</el-tag>
          <el-tag size="small" v-if="scope.row.type === 40"> 上门服务</el-tag>
          <el-tag size="small" v-if="scope.row.type === 80"> 补发商品</el-tag>
          <el-tag size="small" v-if="scope.row.type === 90"> 补款</el-tag>
          <el-tag size="small" v-if="scope.row.type === 91"> 返现</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="orderId" />
      <el-table-column label="退货数量" align="center" prop="count" />
       <el-table-column label="商品skuId" align="center" prop="skuId" />

      <el-table-column label="退款金额" align="center" prop="refundAmount" >
        <template slot-scope="scope">
          <span>{{ amountFormatter(null,null,scope.row.refundAmount/100)  }}</span>
        </template>
      </el-table-column>
      <el-table-column label="售后阶段" align="center" prop="refundPhase" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.refundPhase === 'ON_SALE'">售中</el-tag>
          <el-tag size="small" v-if="scope.row.refundPhase === 'AFTER_SALE'">售后</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单发货状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <!--订单状态：CLOSE已关闭、CANCEL已取消、DELETED已删除、UNPAID未付款、PART_PAID部分付款、NOT_SHIPPED未发货、PART_SHIPPED部分发货、
          SHIPPED已发货、REJECTED已拒收、BILL_SHIPPED、已寄票、BILL_COMPLETE已收票、PAUSE暂停、LOCKED锁定、COMPLETE已完成-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'CLOSE'"> 已关闭</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'CANCEL'"> 已取消</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'DELETED'"> 已删除</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'UNPAID'"> 未付款</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'PART_PAID'"> 部分付款</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'NOT_SHIPPED'"> 未发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'PART_SHIPPED'"> 部分发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'SHIPPED'"> 已发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'REJECTED'"> 已拒收</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'BILL_SHIPPED'"> 已寄票</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'BILL_COMPLETE'"> 已收票</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'PAUSE'"> 暂停</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'LOCKED'"> 锁定</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.orderStatus === 'COMPLETE'"> 已完成</el-tag>-->
<!--          <el-tag size="small" v-else> 未发货</el-tag>-->
          <el-tag size="small" v-if="scope.row.orderShipStatus === 1"> 已发货</el-tag>
          <el-tag size="small" v-else-if="scope.row.orderShipStatus === 0"> 未发货</el-tag>
          <el-tag size="small" v-else>未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款原因" align="center" prop="reason" />
<!--       <el-table-column label="说明" align="center" prop="details" />-->
      <el-table-column label="退货物流" align="center" prop="returnWaybillId" />
       <el-table-column label="售后状态" align="center" prop="status" >
         <template slot-scope="scope">
           <!-- 售后状态 0：售后申请 1：售后关闭，2：售后处理中，3：退款中，4： 售后成功 -->
           <el-tag size="small" v-if="scope.row.status === 0"> 售后申请</el-tag>
           <el-tag size="small" v-if="scope.row.status === 1"> 售后关闭</el-tag>
           <el-tag size="small" v-if="scope.row.status === 2">售后处理中</el-tag>
           <el-tag size="small" v-if="scope.row.status === 3">退款中</el-tag>
           <el-tag size="small" v-if="scope.row.status === 4">售后成功</el-tag>
           <el-tag size="small" v-if="scope.row.status === 5">待用户处理</el-tag>
           <el-tag size="small" v-if="scope.row.status === 6">待买家发货</el-tag>
           <el-tag size="small" v-if="scope.row.status === 8">平台处理中</el-tag>
         </template>
       </el-table-column>
      <el-table-column label="处理状态" align="center" prop="confirmStatus" >
        <template slot-scope="scope">
          <el-tag size="small" type="warning" v-if="scope.row.confirmStatus === 0"> 待确认</el-tag>
          <el-tag size="small" v-if="scope.row.confirmStatus === 1"> 已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            v-if="scope.row.type === 11 && scope.row.confirmStatus==0"-->
<!--            size="mini"-->
<!--            style="padding-right: 8px;padding-left: 8px;"-->
<!--            @click="handleReturnedConfirm(scope.row) "-->
<!--          >无需处理</el-button>-->
<!--            <el-button-->
<!--              v-if="scope.row.type === 10 && scope.row.confirmStatus==0"-->
<!--              size="mini" style="padding-right: 8px;padding-left: 8px;"-->
<!--              @click="handleReturnedConfirm(scope.row) "-->
<!--            >退货确认</el-button>-->
<!--          <el-button-->
<!--            v-if="scope.row.type === 20 && scope.row.confirmStatus==0"-->
<!--            size="mini"-->
<!--            @click="handleOrderIntercept(scope.row) "-->
<!--          >换货确认</el-button>-->
<!--          <el-button-->
<!--            v-if="scope.row.type === 30 && scope.row.confirmStatus==0"-->
<!--            size="mini"-->
<!--            @click="handleOrderIntercept(scope.row) "-->
<!--          >维修登记</el-button>-->
<!--          <el-button-->
<!--            v-if="scope.row.type === 80 && scope.row.confirmStatus==0"-->
<!--            size="mini"-->
<!--            @click="handleOrderIntercept(scope.row) "-->
<!--          >补发确认</el-button>-->
<!--          <el-button-->
<!--              v-if="scope.row.type === 20 && scope.row.confirmStatus==0"-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-connection"-->
<!--              @click="handleOrderIntercept(scope.row) "-->
<!--            >订单拦截</el-button>-->
          <el-button style="padding-left: 10px;padding-right: 10px;"
                     v-if="scope.row.confirmStatus===0"
                     type="primary"
                     plain
                     icon="el-icon-refresh"
                     size="mini"
                     @click="handleRefundProcessing(scope.row)"
          >处理</el-button>
          <el-row>
          <el-button
            :loading="pullLoading"
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handlePullDetail(scope.row)"
            v-hasPermi="['shop:shopRefund:edit']"
          >更新</el-button>
          </el-row>
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
    <!-- 处理售后 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="订单号" prop="orderNum">
          <el-input v-model="form.afterId" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="处理方式" prop="type">
          <el-select v-model="form.type" placeholder="售后处理方式" >
            <el-option label="无需处理" value="0"></el-option>
            <el-option label="退货" value="10"></el-option>
            <el-option label="换货" value="20"></el-option>
            <el-option label="维修" value="30"></el-option>
            <el-option label="补发" value="80"></el-option>
            <el-option label="订单拦截" value="99"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否已发货" prop="hasGoodsSend">
          <el-select v-model="form.hasGoodsSend" placeholder="是否已发货" >
            <el-option label="未发货" value="0"></el-option>
            <el-option label="已发货" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发货单号" prop="sendLogisticsCode">
          <el-input v-model="form.sendLogisticsCode" placeholder="发货单号" />
        </el-form-item>
        <el-form-item label="退回物流单号" prop="returnLogisticsCode">
          <el-input v-model="form.returnLogisticsCode" placeholder="退回物流单号" />
        </el-form-item>
        <!--        <el-form-item label="补发/换货单号" prop="reissueLogisticsCode">-->
        <!--          <el-input v-model="form.reissueLogisticsCode" placeholder="补发/换货单号" />-->
        <!--        </el-form-item>-->
        <el-form-item label="收件人" prop="receiverName" v-if="form.type == 80">
          <el-input v-model="form.receiverName" placeholder="收件人" />
        </el-form-item>
        <el-form-item label="收件手机号" prop="receiverTel" v-if="form.type == 80">
          <el-input v-model="form.receiverTel" placeholder="收件手机号" />
        </el-form-item>
        <el-form-item label="收件地址" prop="receiverAddress" v-if="form.type == 80">
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
    <!-- 添加订单售后对话框 -->
        <el-dialog title="手动添加售后" :visible.sync="addOpen" width="1000px" append-to-body>
          <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="140px">

            <el-form-item label="店铺订单" prop="orderId">
              <el-input v-model="addForm.orderId" placeholder="请选择店铺订单" disabled style="width: 300px;"/>
              <el-button size="small" style="margin-left: 20px;" @click="addGoodsDialog"> 选择店铺订单</el-button>

                <el-table :data="orderItemList"  ref="singleTable"  highlight-current-row @current-change="handleCurrentChange"  @row-click="handleRowClick">
<!--                  <el-table-column label="" prop="id" width="66"></el-table-column>-->
                  <!--                <el-table-column label="子订单号" prop="subOrderNum" ></el-table-column>-->
                  <el-table-column label="" prop="img" width="55">
                    <template slot-scope="scope">
                      <el-image style="width: 40px; height: 40px" :src="scope.row.img"></el-image>
                    </template>
                  </el-table-column>
                  <el-table-column label="" prop="title" width="260"></el-table-column>
                  <el-table-column label="" prop="skuName" ></el-table-column>
                  <el-table-column label="" prop="skuCode" ></el-table-column>
                  <el-table-column label="" prop="quantity" width="60"></el-table-column>
                  <!--                <el-table-column label="退款状态" prop="refundStatus" width="150">-->
                  <!--                  <template slot-scope="scope">-->
                  <!--                    <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                  <!--                    <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
                  <!--                    <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
                  <!--                    <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
                  <!--                  </template>-->
                  <!--                </el-table-column>-->
                </el-table>
            </el-form-item>
            <el-form-item label="售后单号"  prop="refundId">
              <el-input v-model="addForm.refundId" placeholder="售后单号,不填自动生成" style="width: 300px;"/>
            </el-form-item>
              <el-form-item label="售后类型" prop="type">
                <el-select v-model="addForm.type" placeholder="售后类型" style="width: 300px;">
                  <el-option
                    v-for="item in typeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="问题分类" prop="reasonType">
                <el-select v-model="addForm.reasonType" placeholder="问题分类" style="width: 300px;">
                  <el-option v-for="item in reasonTypeList" :key="item.dictCode" :label="item.dictLabel" :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            <el-row>
              <el-form-item label="退款金额"  prop="refundAmount">
                <el-input type="number" v-model.number="addForm.refundAmount" placeholder="请输入退款金额" style="width: 300px;"/>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="买家是否需要退货" prop="hasGoodReturn">
                <el-select v-model="addForm.hasGoodReturn" placeholder="买家是否需要退货" style="width: 300px;">
                  <el-option label="是" value="1"></el-option>
                  <el-option label="否" value="0"></el-option>
                </el-select>
              </el-form-item>

            </el-row>
            <el-row>
              <el-form-item label="退货物流"  prop="returnLogistics" v-if="addForm.hasGoodReturn==1">
              <el-input  v-model="addForm.returnLogistics" placeholder="请输入退货物流" style="width: 300px;"/>
            </el-form-item>
<!--            <el-form-item label="附件" prop="image">-->
<!--              <image-upload v-model="addForm.attachment" :limit="1"/>-->
<!--            </el-form-item>-->
            </el-row>
            <el-row>
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="addForm.remark"  style="width: 300px;" placeholder="请输入备注" />
            </el-form-item>
            </el-row>
            <!--        <el-form-item label="发货人" prop="shipOperator">-->
            <!--          <el-input v-model="form.shipOperator" placeholder="请输入发货人" />-->
            <!--        </el-form-item>-->
            <!--        <el-form-item label="包裹重量" prop="packageWeight">-->
            <!--          <el-input v-model="form.packageWeight" placeholder="请输入包裹重量" />-->
            <!--        </el-form-item>-->
            <!--        <el-form-item label="包裹长度" prop="packageLength">-->
            <!--          <el-input v-model="form.packageLength" placeholder="包裹长度" />-->
            <!--        </el-form-item>-->
            <!--        <el-form-item label="包裹宽度" prop="packageWidth">-->
            <!--          <el-input v-model="form.width" placeholder="请输入包裹宽度" />-->
            <!--        </el-form-item>-->
            <!--        <el-form-item label="包裹高度" prop="packageHeight">-->
            <!--          <el-input v-model="form.height" placeholder="请输入包裹高度" />-->
            <!--        </el-form-item>-->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitAddForm">添加售后</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
    <!-- 添加品对话框 -->
    <PopupShopOrderItemList @data-from-select="handleDataFromPopup" :btn="2" ref="popup"></PopupShopOrderItemList>
  </div>
</template>

<script>
import {getShop, listShop} from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {
  listShopRefund,
  orderIntercept,
  pullRefund,
  returnedConfirm,
  pullOrderDetail,
  pushOms,
  addShopRefund
} from "@/api/shop/refund";
import { listShopOrder } from '@/api/shop/order'
import {listMerchant} from "@/api/shop/merchant";
import {isRelogin} from "@/utils/request";
import {amountFormatter} from "../../../utils/zhijian";

import PopupShopOrderItemList from "@/views/shop/order/PopupShopOrderItemList.vue";
import {getDicts} from "@/api/system/dict/data";
import {getUserProfile} from "@/api/system/user";

export default {
  name: "ShopRefund",
  components: {PopupShopOrderItemList},
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: false,
    },
  },
  data() {
    return {
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
      // 淘宝退款订单表格数据
      taoRefundList: [],
      shopList:[],
      merchantList:[],
      orderList:[],
      orderItemList:[],
      // 问题分类
      reasonTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      addOpen: false,
      shopLoading: false,
      isMerchant: false,
      isShop: false,
      orderListLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundId: null,
        afterId: null,
        type:null,
        shopType: null,
        orderId: null
      },
      //售后类型。(1-售前退款          10-退货 20-换货 30-维修 40-大家电安装 50-大家电移机 60-大家电增值服务 70-上门维修 90-优鲜赔 80-补发商品 100-试用收回 11-仅退款)
      //售后类型。(1-售前退款(取消订单) 10-退货 20-换货 30-维修 40-上门服务 80-补发商品 90-补款 91-返现 11-仅退款)
      typeList: [
        {
          value: '11',
          label: '仅退款'
        }, {
          value: '10',
          label: '退货'
        }
        , {
          value: '20',
          label: '换货'
        }, {
          value: '30',
          label: '维修'
        },{
          value: '80',
          label: '补发商品'
        }
      ],
      // 表单参数
      form: {
        type:null,
      },
      addForm: {
        refundId:null,
        orderId:null,
        subOrderId:null,
        orderItemId:null,
        itemAmount:null,
        title:null,
        skuId:null,
        skuCode:null,
        skuName:null,
        type:null,
        reasonType:null,
        refundAmount:null,
        hasGoodReturn:null,
        returnLogistics:null,
        remark:null,
      },
      // 表单校验
      rules: {
        num: [
          { required: true, message: "退货数量不能为空", trigger: "blur" }
        ],
        logisticsCompany: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        logisticsCode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        sendTime: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      },
      addRules: {
        orderId: [{ required: true, message: "退货数量不能为空", trigger: "blur" }],
        type: [{ required: true, message: "不能为空", trigger: "change" }],
        reasonType: [{ required: true, message: "不能为空", trigger: "blur" }],
        refundAmount: [{ required: true, message: "不能为空", trigger: "blur" }],
        hasGoodReturn: [{ required: true, message: "不能为空", trigger: "blur" }],
    }
    };
  },
  created() {
    getDicts("refund_reason_type").then(resp=>{
      this.reasonTypeList = resp.data
    })
    getUserProfile().then(res => {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isMerchant = false
        this.isShop = false
        this.merchantList = []
        this.shopList = []
        listMerchant({ }).then(resp => {
          this.merchantList = resp.rows
          if (this.merchantList.length > 0) {
            this.queryParams.merchantId = this.merchantList[0].id
          }
          listShop({ merchantId: this.queryParams.merchantId,type:this.shopType }).then(response => {
            this.shopList = response.rows;
            if (this.shopList.length > 0) {
              this.queryParams.shopId = this.shopList[0].id
            }
            this.shopLoading = false
            this.getList()
          });
        })
      } else if (res.data.userType == 20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId;
        this.merchantList = []
        this.merchantList.push({id: res.data.deptId, name: res.data.nickName})
        this.shopList = []
        listShop({ merchantId: this.queryParams.merchantId,type:this.shopType }).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.getList()
        });
      } else if (res.data.userType == 40) {
        this.isMerchant = true;
        this.isShop = true
        this.merchantList = []
        this.shopList = []
        this.queryParams.shopId = res.data.deptId;
        getShop(res.data.deptId).then(resp => {
          this.shopList.push({id: resp.data.id, name: resp.data.name})
          this.shopType = resp.data.type
          console.log('重新赋值之后的shopType：', this.shopType)
          this.getList()
        })
      }
    })
    // listShop({type:this.shopType}).then(response => {
    //     this.shopList = response.rows;
    //     this.shopLoading = false
    //   if (this.shopList && this.shopList.length > 0) {
    //     this.queryParams.shopId = this.shopList[0].id
    //   }
    //   });
    // this.getList();

  },
  methods: {
    amountFormatter,
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.shopType }).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        this.handleQuery()
      });

    },
    /** 查询退款订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.shopType=this.shopType
      listShopRefund(this.queryParams).then(response => {
        this.taoRefundList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.pullLoading = false
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.addOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        refundId: null
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
    submitAddForm(){
      this.$refs["addForm"].validate(valid => {
        if (valid) {
          addShopRefund(this.addForm).then(res => {
            if(res.code==200){
              this.$modal.msgSuccess("售后添加成功");
              this.addOpen = false;
              this.getList();
            }else{
              this.$modal.msgError(res.msg)
            }
          })
        }
      })
    },
    submitForm(){},
    handlePull() {
      if(this.queryParams.shopId){
        this.pullLoading = true
        pullRefund({shopId:this.queryParams.shopId,updType:0}).then(response => {
          console.log('拉取jd订单接口返回=====',response)
          if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            this.pullLoading = false
            this.getList()
          }else{
            this.$modal.msgError(JSON.stringify(response));
            this.pullLoading = false
          }

        })
      }else{
        this.$modal.msgSuccess("请先选择店铺");
      }
    },
    // 更新详情
    handlePullDetail(row){
      pullOrderDetail({afterId:row.afterId,shopId:row.shopId}).then(response => {
        console.log('拉取退款接口返回=====',response)
        if(response.code === 200){
          this.$modal.msgSuccess(JSON.stringify(response));
          this.pullLoading = false
          this.getList()
        }else {
          this.$modal.msgError(JSON.stringify(response));
          this.pullLoading = false
        }

      })
    },
    handleReturnedConfirm(row){
      returnedConfirm(row.id).then(response => {
        this.$modal.msgSuccess("签收完成");
        this.getList()
      });
    },
    handleOrderIntercept(row){
      orderIntercept(row.id).then(response => {
        this.$modal.msgSuccess("拦截成功");
        this.getList()
      });
    },
    handlePushOms(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否手动推送到OMS？').then(function() {
        return pushOms({ids:ids});
      }).then(() => {
        // this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
    /** 售后处理*/
    handleRefundProcessing(row){
      const id = row.id || this.ids[0]
      this.form.id = row.id
      this.form.afterId = row.afterId
      this.form.orderId = row.orderId

      this.form.sendLogisticsCode = row.sendLogisticsCode
      if(row.hasGoodReturn===1){
        this.form.hasGoodsSend = '1'
      }

      if(row.type===1){
        this.title = "售前退款"
        this.form.type = '0'//处理方式
        this.form.hasGoodsSend = '0'
      }else  if(row.type===11){
        this.title = "仅退款"
        this.form.type = '0'
      }else  if(row.type===10){
        this.title = "退货"
        this.form.type = '10'
      }else  if(row.type===20){
        this.title = "换货"
        this.form.type = '20'
      }else  if(row.type===30){
        this.title = "维修"
        this.form.type = '30'
      }else  if(row.type===80){
        this.title = "补发"
        this.form.type = '80'
      }
      this.open = true;
      // if(this.returnedList.find(x=>x.id === id)) {
      //   const refund = this.returnedList.find(x=>x.id === id)
      //   this.open = true
      //   this.form.refundId = id
      //   this.form.orderNum = refund.orderNum
      //
      // }else{
      //   this.$modal.msgError("请选择数据！");
      // }
    },
    /**手动添加售后*/
    handleAddRefund() {
      this.addForm = {
        refundId:null,
        orderId:null,
        subOrderId:null,
        orderItemId:null,
        itemAmount:null,
        title:null,
        skuId:null,
        skuCode:null,
        skuName:null,
        type:null,
        reasonType:null,
        refundAmount:null,
        hasGoodReturn:null,
        returnLogistics:null,
        remark:null,
      }
      this.orderItemList = []
      this.addOpen = true
    },
    addGoodsDialog() {
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data) {
        this.orderItemList = data
        if (data[0]) {
          this.addForm.orderItemId = data[0].id
          this.addForm.orderId = data[0].orderId
          this.addForm.subOrderId = data[0].subOrderId
          this.addForm.skuId = data[0].skuId
          this.addForm.skuCode = data[0].skuCode
          this.addForm.img = data[0].img
          this.addForm.title = data[0].title
          this.addForm.skuName = data[0].skuName
          this.addForm.itemAmount = data[0].itemAmount
          this.addForm.refundAmount = data[0].itemAmount /100
        }
      }
      console.log('====添加后=====', this.addForm)
    },
    /** 搜索订单 */
    searchOrderByOrderId(query){
      // console.log('====搜索订单待发货联系人,',this.form.consignee)
      if(query) {
        this.orderListLoading = true;
        searchSaleOrder({orderNum: query}).then(response => {
          this.orderList = response.rows;
          this.orderListLoading = false;
        });
      }
    },
    orderChange(nv){
      console.log('===orderChange==',nv)
      var orderList = this.orderList.filter(item => item.id ===nv)
      if(orderList&&orderList.length>0){
        this.orderItemList = orderList[0].itemList;
      }

    },
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(val) {
      console.log('-----00000----',val);
      this.addForm.orderItemId = val.id;
    },
    handleRowClick(row, column, event) {
      const table = this.$refs.singleTable;
      const allRows = table.data;

      // Unselect all rows first
      table.clearSelection();

      // Select the clicked row
      table.toggleRowSelection(row, true);
      // const allSelections = table.selection;
      //
      // // Check if the row is already selected
      // const isRowSelected = allSelections.includes(row);
      // if (isRowSelected) {
      //   table.toggleRowSelection(row, false);
      // } else {
      //   table.toggleRowSelection(row, true);
      // }
    },
  }

};
</script>
