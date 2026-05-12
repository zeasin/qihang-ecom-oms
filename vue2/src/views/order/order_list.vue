<template>
  <div >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
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
      <el-form-item label="平台" prop="shopType" v-if="!isShop">
        <el-select v-model="queryParams.shopType"  placeholder="请选择平台"  @change="platformChange">
          <el-option label="销售订单" value="0"></el-option>
          <el-option
            v-for="item in platformList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId" v-if="!isShop">
        <el-select :loading="shopLoading" v-model="queryParams.shopId" placeholder="请选择店铺"
                   remote reserve-keyword :remote-method="searchShop"
                   filterable clearable @change="handleQuery">
         <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
           <span style="float: right; color: #8492a6; font-size: 13px"  >
             <el-tag>{{ platformList.find(x=>x.id === item.type) ? platformList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>
           </span>
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
        <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable @change="handleQuery">
          <el-option label="新订单" value="0" ></el-option>
          <el-option label="待发货" value="1" ></el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="已完成" value="3"> </el-option>
          <el-option label="已取消" value="11"></el-option>
          <el-option label="已关闭" value="13"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="退款状态" prop="refundStatus">-->
<!--        <el-select v-model="queryParams.refundStatus" placeholder="请选择状态" clearable @change="handleQuery">-->
<!--          <el-option label="无售后或售后关闭" value="1" ></el-option>-->
<!--          <el-option label="售后处理中" value="2"></el-option>-->
<!--          <el-option label="退款中" value="3"> </el-option>-->
<!--          <el-option label="退款成功" value="4"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="三方发货推送" prop="distStatus">
        <el-select v-model="queryParams.distStatus" placeholder="三方推送发货状态" clearable @change="handleQuery">
          <el-option label="未推送" value="0"></el-option>
          <el-option label="部分推送" value="1"></el-option>
          <el-option label="全部推送" value="2"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-row :gutter="10" class="mb8" style="padding-left: 50px;">-->
<!--        <el-col :span="1.5">-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" style="padding-left: 58px">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-col>
      <el-col :span="1.5" >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-badge class="item" style="padding-left: 20px;">-->
<!--          <el-button size="small" @click="allList">全部</el-button>-->
<!--        </el-badge>-->
<!--        <el-badge class="item" style="padding-left: 20px;">-->
<!--          <el-button size="small" @click="waitShipList">待发货</el-button>-->
<!--        </el-badge>-->

<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="pushLoading"-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handlePushErp"-->
<!--        >手动将选中订单推送到ERP</el-button>-->
<!--      </el-col>-->
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

        </template>>
      </el-table-column>
      <el-table-column label="商品" width="450px">
        <template slot-scope="scope">
          <!--            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">-->
          <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
            <image-preview :src="scope.row.itemVoList[0].goodsImg" :width="40" :height="40"/>
            <div style="margin-left:10px">
              <div style="width: 350px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" :title="scope.row.itemVoList[0].goodsTitle">
                {{scope.row.itemVoList[0].goodsTitle}}
              </div>
              <div>
                <span style="color: #5a5e66;font-size: 11px">规格：</span>
                <el-tag size="small">{{scope.row.itemVoList[0].goodsSpec}}</el-tag>&nbsp;
                <span>
                    <span style="color: #5a5e66;font-size: 11px">数量：</span>
                    <el-tag size="small">x {{scope.row.itemVoList[0].quantity}}</el-tag>
                  </span>
              </div>
<!--              <div>-->
<!--                  <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">平台ID：</span>-->
<!--                    {{scope.row.itemVoList[0].skuId}}&nbsp-->
<!--                  </span>-->
<!--                <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">商品库SkuID：</span>-->
<!--                    {{scope.row.itemVoList[0].goodsSkuId}}&nbsp;&nbsp-->
<!--                  </span>-->
<!--                <span v-if="scope.row.itemVoList[0].skuNum" style="color: #5a5e66;font-size: 11px">编码：{{scope.row.itemVoList[0].skuNum}}</span>-->
<!--              </div>-->
            </div>
          </div>
          <div style="float: left;display: flex;align-items: center;padding-left: 50px"  v-if="scope.row.itemVoList.length>1">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row,'orderItems')"
            >更多订单商品（{{scope.row.itemVoList.length}}）</el-button>
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
<!--          <el-table :data="scope.row.itemVoList" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >-->
<!--            <el-table-column label="商品图片" width="50px">-->
<!--              <template slot-scope="scope">-->
<!--                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="商品标题" align="left" width="250px" prop="goodsTitle" >-->
<!--              <template slot-scope="scope">-->
<!--                {{scope.row.goodsTitle}}-->
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
<!--      <el-table-column label="订单金额" align="center" prop="amount" :formatter="amountFormatter" >-->
<!--      </el-table-column>-->
<!--      <el-table-column label="实付" align="center" prop="payment" :formatter="amountFormatter" ></el-table-column>-->
<!--      <el-table-column label="平台优惠" align="center" prop="platformDiscount" :formatter="amountFormatter" ></el-table-column>-->
<!--      <el-table-column label="商家优惠" align="center" prop="sellerDiscount" :formatter="amountFormatter" ></el-table-column>-->
      <el-table-column label="订单金额" align="left" prop="amount" :formatter="amountFormatter" >
        <template slot-scope="scope">
<!--          <div>-->
<!--            <span style="font-size: 10px">商品总额：</span>-->
<!--            <span>{{amountFormatter(null,null,scope.row.goodsAmount)}}</span>-->
<!--          </div>-->
          <div>
            <span style="font-size: 10px">订单金额：</span>
            <span>{{amountFormatter(null,null,scope.row.amount)}}</span>
          </div>
          <div><span style="font-size: 10px">平台优惠：</span>
            <span>{{amountFormatter(null,null,scope.row.platformDiscount)}}</span>
          </div>
          <div><span style="font-size: 10px">商家优惠：</span>
            <span>{{amountFormatter(null,null,scope.row.sellerDiscount)}}</span>
          </div>
<!--          <p>商品总额：{{amountFormatter(null,null,scope.row.goodsAmount)}}</p>-->
<!--          <p>实付金额：{{amountFormatter(null,null,scope.row.payment)}}</p>-->
<!--          <p>平台优惠：{{amountFormatter(null,null,scope.row.platformDiscount)}}</p>-->
<!--          <p>商家优惠：{{amountFormatter(null,null,scope.row.sellerDiscount)}}</p>-->
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="left" prop="shopId" width="160px" v-if="!isShop">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="Tag" prop="isGift">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.hasGift > 0">有赠品</el-tag>
          <el-tag type="danger" v-if="scope.row.hasGift == -1">赠品订单</el-tag>
          <el-tag type="success" v-if="scope.row.orderMode == 1">手工订单</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="实付金额" align="center" prop="payment" :formatter="amountFormatter" />-->
<!--      <el-table-column label="订单备注" align="center" prop="remark" :show-overflow-tooltip="true">-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.buyerMemo}}<br />-->
<!--          {{scope.row.sellerMemo}}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="收件人" prop="receiverName" width="200px">
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;
          {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />

        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 6px;">新订单</el-tag>
          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 21" style="margin-bottom: 6px;">待付款</el-tag>
          <el-tag v-if="scope.row.orderStatus === 22" style="margin-bottom: 6px;">锁定</el-tag>
          <el-tag v-if="scope.row.orderStatus === 29" style="margin-bottom: 6px;">删除</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>
          <el-tag v-if="scope.row.orderStatus === 13" style="margin-bottom: 6px;">已关闭</el-tag>
          <br />
<!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 200">订单推送成功</el-tag>-->
<!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 100">订单取消推送成功</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="!scope.row.erpPushStatus || scope.row.erpPushStatus === 0">待推送到ERP</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus > 200">推送错误</el-tag>-->
<!--          <div style="margin-bottom: 6px;color:red" v-if="scope.row.erpPushStatus > 200">{{scope.row.erpPushResult}}</div>-->
<!--          <br />-->
<!--           &lt;!&ndash; 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 &ndash;&gt;-->
<!--           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
<!--           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
<!--           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
<!--           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
        </template>
      </el-table-column>
      <el-table-column label="发货状态" align="center" prop="shipStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shipStatus === 0" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.shipStatus === 1" style="margin-bottom: 6px;">部分发货</el-tag>
          <el-tag v-if="scope.row.shipStatus === 2" style="margin-bottom: 6px;">全部发货</el-tag>
          <el-col :span="24">
          <span v-if="scope.row.shipStatus === 0 && scope.row.waybillStatus===1">已取号</span>
          <span v-if="scope.row.shipStatus === 2">{{scope.row.waybillCode}}</span>
          </el-col>
          <el-col :span="24">
          <el-tag v-if="scope.row.distStatus === 0" type="info" style="margin-bottom: 6px;">未推送三方发货</el-tag>
          <el-tag v-if="scope.row.distStatus === 1" type="info"  style="margin-bottom: 6px;">部分推送三方发货</el-tag>
          <el-tag v-if="scope.row.distStatus === 2" type="info"  style="margin-bottom: 6px;">全部推送三方发货</el-tag>
          </el-col>
          <!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 200">订单推送成功</el-tag>-->
          <!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 100">订单取消推送成功</el-tag>-->
          <!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="!scope.row.erpPushStatus || scope.row.erpPushStatus === 0">待推送到ERP</el-tag>-->
          <!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus > 200">推送错误</el-tag>-->
          <!--          <div style="margin-bottom: 6px;color:red" v-if="scope.row.erpPushStatus > 200">{{scope.row.erpPushResult}}</div>-->
          <!--          <br />-->
          <!--           &lt;!&ndash; 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 &ndash;&gt;-->
          <!--           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
        </template>
      </el-table-column>


<!--      <el-table-column label="发货信息" align="center" prop="shippingNumber" >-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.waybillCode}}&nbsp; {{scope.row.shippingCompany}}<br />-->
<!--          {{ parseTime(scope.row.shippingTime, '{y}-{m}-{d} {h}:{m}:{s}') }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          <span v-if="scope.row.orderFinishTime && scope.row.orderFinishTime>0">{{ parseTime(scope.row.orderFinishTime) }}</span>

        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-view"-->
<!--            @click="handleDetail(scope.row)"-->
<!--          >详情</el-button>-->
<!--&lt;!&ndash;          <div>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button&ndash;&gt;-->
<!--&lt;!&ndash;              size="mini"&ndash;&gt;-->
<!--&lt;!&ndash;              type="success"&ndash;&gt;-->
<!--&lt;!&ndash;              icon="el-icon-share"&ndash;&gt;-->
<!--&lt;!&ndash;              @click="handleShip(scope.row)"&ndash;&gt;-->
<!--&lt;!&ndash;            >订单发货</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->
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
                <!-- <el-date-picker
                disabled
                  v-model="form.orderCreateTime"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="请选择订单创建时间">
                </el-date-picker> -->
              </el-descriptions-item>
<!--              <el-descriptions-item label="支付时间"> {{ parseTime(form.payTime) }}</el-descriptions-item>-->
              <el-descriptions-item label="最后更新时间"> {{ parseTime(form.updateTime) }}</el-descriptions-item>

              <el-descriptions-item label="订单状态">
                <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
                <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已出库</el-tag>
                <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已发货</el-tag>
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
<!--            <el-descriptions title="发货信息">-->

<!--              <el-descriptions-item label="物流公司">{{form.shippingCompany}}</el-descriptions-item>-->
<!--              <el-descriptions-item label="物流单号">{{form.shippingNumber}}</el-descriptions-item>-->
<!--              <el-descriptions-item label="发货时间">{{form.shippingTime}}</el-descriptions-item>-->
<!--            </el-descriptions>-->

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
        <el-tab-pane label="优惠明细" name="orderCou" lazy>
          <el-table :data="form.discounts"  style="margin-bottom: 10px;">
            <el-table-column label="序号" align="center" type="index" width="50"/>
            <el-table-column label="优惠名称" prop="name" ></el-table-column>
            <el-table-column label="优惠金额" prop="discountAmount"></el-table-column>
            <el-table-column label="优惠描述" prop="description"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="物流单" name="orderLog" lazy>
          <el-table :data="form.logistics"  style="margin-bottom: 10px;">
            <el-table-column label="序号" align="center" type="index" width="50"/>
            <el-table-column label="发货人" prop="type" >
              <template slot-scope="scope">
              <el-tag v-if="scope.row.type === 0">本地发货</el-tag>
              <el-tag v-if="scope.row.type === 200">云仓发货</el-tag>
              <el-tag v-if="scope.row.type === 100">京东云仓发货</el-tag>
              <el-tag v-if="scope.row.type === 300">供应商发货</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="物流公司" prop="logisticsCompany"></el-table-column>
            <el-table-column label="物流单号" prop="waybillCode"></el-table-column>
            <el-table-column label="发货时间" align="center" prop="shippingTime" >
              <template slot-scope="scope">
                {{ parseTime(scope.row.shippingTime) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import {listOrder, getOrder, delOrder, addOrder, updateOrder, pushErp} from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import Clipboard from "clipboard";
import { listMerchant } from '@/api/shop/merchant'
import { getShopListData } from '@/utils/shopUtils'
import {getUserProfile} from "@/api/system/user";

export default {
  name: "Order",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
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
      platformList:[],
      shopList:[],
      shopListAll:[],
      merchantList: [],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      activeName: 'orderDetail',
      orderTime: null,
      waitShipCount: 0,
      isMerchant: false,
      isShop: false,
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
        distStatus: null,
        shipStatus: null,
        refundStatus: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {

      }
    };
  },
  created() {
    listPlatform({ status: 0 }).then(res => {
      this.platformList = res.rows;
    })
    getUserProfile().then(res=> {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isMerchant = false
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
      }else if(res.data.userType==20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId;
        this.merchantList = []
        this.shopList = []
        listShop({ merchantId: this.queryParams.merchantId }).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.getList()
        });
      }else if(res.data.userType==40){
        this.isMerchant = true;
        this.isShop = true
        this.queryParams.shopId = res.data.deptId;
        this.merchantList = []
        this.shopList = []
        this.getList()
      }

    })



  },
  async mounted() {
    this.shopListAll = await getShopListData(); // 获取并设置 shopList
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
    amountFormatter(row, column, cellValue, index) {
      return '￥' + parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push('/order/create');
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
        // if(this.queryParams.orderStatus=='1'){
        //   this.waitShipCount = response.total;
        // }
        this.loading = false;
      });
    },
    allList(){
      this.queryParams.pageNum = 1;
      this.queryParams.orderStatus = null;
      this.getList();
    },
    waitShipList(){
      this.queryParams.pageNum = 1;
      this.queryParams.orderStatus = '1';
      this.getList();
    },
    merchantChange(nv){
      console.log('==============',nv);
        this.shopList = []
        this.queryParams.shopId = null
        listShop({merchantId:nv}).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.handleQuery()
        });

    },
    platformChange(nv){
      console.log('========ShopType======',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({type:this.queryParams.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;

      // if(this.queryParams.shopType){
      //
      //   listShop({type:this.queryParams.shopType}).then(response => {
      //     this.shopList = response.rows;
      //     this.shopLoading = false
      //   });
      // }
      // if(this.queryParams.merchantId){
      //   listShop({ merchantId: this.queryParams.merchantId }).then(response => {
      //     this.shopList = response.rows;
      //     this.shopLoading = false
      //   });
      // }
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
    handlePushErp(row){
      const id = row.id || this.ids
      this.pushLoading = true
      pushErp(id).then(response => {
        console.log('======返回====',response)
        this.$modal.msgSuccess("推送成功！请在订单详情查看推送结果！");
        this.pushLoading = false
        this.getList()
      });
    },
    handleShip(row){

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
  }
};
</script>

