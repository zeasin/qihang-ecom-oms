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
      <el-form-item label="收件人" prop="receiver">
        <el-input
          v-model="queryParams.receiver"
          placeholder="请输入收件人手机号或姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="快递单号" prop="shippingNumber">
        <el-input
          v-model="queryParams.shippingNumber"
          placeholder="请输入快递单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="queryParams.merchantId" clearable placeholder="请选择商户"  @change="merchantChange">
          <el-option
            v-for="item in merchantList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId"
                   remote reserve-keyword :remote-method="searchShop"  :loading="shopLoading"
                   placeholder="请选择店铺" clearable @change="handleQuery" filterable>
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
<!--      <el-form-item label="收件人姓名" prop="receiverName">-->
<!--        <el-input-->
<!--          v-model="queryParams.receiverName"-->
<!--          placeholder="请输入收件人姓名"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="收件人手机号" prop="receiverMobile">-->
<!--        <el-input-->
<!--          v-model="queryParams.receiverMobile"-->
<!--          placeholder="请输入收件人手机号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->

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
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >创建内销订单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd2"
        >创建内销订单H5</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handlePushOms"-->
<!--        >重新推送选中订单到订单库</el-button>-->
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

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
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

        </template>
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
                  <el-tag size="small">{{scope.row.itemList[0].goodsSpec}}</el-tag>&nbsp;
<!--                  <span v-if="scope.row.itemList[0].skuNum" style="color: #5a5e66;font-size: 11px">编码：{{scope.row.itemList[0].skuNum}}</span></div>-->
<!--                <div>-->
<!--                  <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">平台ID：</span>-->
<!--                    {{scope.row.itemList[0].skuId}}&nbsp-->
<!--                  </span>-->
<!--                  <span>-->
<!--                    <span style="color: #5a5e66;font-size: 11px">商品库ID：</span>-->
<!--                    {{scope.row.itemList[0].goodsSkuId}}&nbsp;&nbsp-->
<!--                  </span>-->
                  <span>
                    <span style="color: #5a5e66;font-size: 11px">数量：</span>
                    <el-tag size="small">x {{scope.row.itemList[0].quantity}}</el-tag>
                  </span>
                </div>
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
<!--      <el-table-column label="商品明细" align="center" prop="shopId" width="750px">-->
<!--        <template slot-scope='scope'>-->
<!--            <el-collapse v-model='activeName'>-->
<!--              <el-collapse-item :title="'总共:'+scope.row.itemList.length+'个SKU'"  name='1'>-->
<!--                <el-main style='padding: 0 10px'>-->
<!--                  <el-table :data="scope.row.itemList" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >-->
<!--                    <el-table-column label="商品图片" width="50px">-->
<!--                      <template slot-scope="scope">-->
<!--                        &lt;!&ndash;                <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>&ndash;&gt;-->
<!--                        <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>-->
<!--                      </template>-->
<!--                    </el-table-column>-->
<!--                    <el-table-column label="商品名" align="left" width="250px" prop="goodsTitle" />-->
<!--                    <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true"/>-->
<!--                    <el-table-column label="Sku编码" align="left" prop="skuNum" width="200"/>-->
<!--                    <el-table-column label="商品数量" align="center" prop="quantity" width="50px">-->
<!--                      <template slot-scope="scope">-->
<!--                        <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>-->
<!--                      </template>-->
<!--                    </el-table-column>-->
<!--                  </el-table>-->
<!--                </el-main>-->
<!--              </el-collapse-item>-->
<!--            </el-collapse>-->

<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="商品明细" align="center" width="750px" >-->
<!--        <template slot="header">-->
<!--          <table>-->
<!--            <th>-->
<!--              <td width="50px">图片</td>-->
<!--              <td width="250px" align="left">标题</td>-->
<!--              <td width="150" align="left">规格</td>-->
<!--              <td width="200" align="left">Sku编码</td>-->
<!--&lt;!&ndash;              <td width="150" align="left">平台SkuId</td>&ndash;&gt;-->
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
<!--            <el-table-column label="商品名" align="left" width="250px" prop="goodsTitle" />-->
<!--            <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true"/>-->
<!--            <el-table-column label="Sku编码" align="left" prop="skuNum" width="200"/>-->
<!--&lt;!&ndash;            <el-table-column label="平台SkuId" align="left" prop="skuId" width="150"/>&ndash;&gt;-->
<!--            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">-->
<!--              <template slot-scope="scope">-->
<!--                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
      <el-table-column label="店铺" align="left" prop="shopId" width="150px">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" v-if="scope.row.merchantId>0" type="success">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <br/>
            <span v-if="shopListAll.find(x=>x.id == scope.row.shopId)">
              {{shopListAll.find(x=>x.id == scope.row.shopId).name}}
            </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="客户类型" align="center" prop="customerType" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.customerType === 0" style="margin-bottom: 6px;">消费者</el-tag>
          <el-tag v-if="scope.row.customerType === 20" style="margin-bottom: 6px;">集团商户</el-tag>
          <el-tag v-if="scope.row.customerType === 100" style="margin-bottom: 6px;">外部2B客户</el-tag>

                    <br />
                    <el-tag style="margin-bottom: 6px;" v-if="scope.row.customerType === 20 && scope.row.customerName">{{scope.row.customerName}}</el-tag>
          <!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.omsPushStatus === 1">已推送</el-tag>-->

          <!--          <br />-->
          <!--           &lt;!&ndash; 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 &ndash;&gt;-->
          <!--           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>-->
          <!--           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>-->
        </template>
      </el-table-column>
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
<!--      <el-table-column label="是否有赠品" prop="isGift">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.hasGift > 0">有赠品</el-tag>-->
<!--          <el-tag type="danger" v-if="scope.row.hasGift == -1">赠品订单</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="发货单号" prop="shippingNumber" ></el-table-column>-->
      <el-table-column label="发货信息" align="center" prop="shippingNumber" >
        <template slot-scope="scope">
          {{scope.row.shippingNumber}}&nbsp; {{scope.row.shippingCompany}}<br />
          {{ parseTime(scope.row.shippingTime) }}
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" ></el-table-column>
      <el-table-column label="取消理由" prop="cancelReason" ></el-table-column>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.orderStatus === 1"
                     size="mini"
                     type="text"
                     icon="el-icon-view"
                     @click="handleCancel(scope.row)"
          >取消订单</el-button>
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
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1400px" append-to-body  @close="handleClose">
      <el-tabs v-model="activeName" >
        <el-tab-pane label="订单详情" name="orderDetail">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
            <el-descriptions title="订单信息">
              <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
              <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>

              <el-descriptions-item label="店铺">
                {{ shopListAll.find(x=>x.id === form.shopId)?shopListAll.find(x=>x.id === form.shopId).name:'' }}
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
              <el-descriptions-item label="运费">{{amountFormatter(null,null,form.postFee,0)}}</el-descriptions-item>
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

          </el-form>

        </el-tab-pane>
        <el-tab-pane label="商品列表" name="orderItems" lazy>
          <el-table :data="form.itemList"  style="margin-bottom: 10px;" height="500px">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>
            <el-table-column label="SkuId" align="center" prop="goodsSkuId" width="100"/>

            <el-table-column label="图片" prop="goodsImg" width="60">
              <template slot-scope="scope">
                <image-preview :src="scope.row.goodsImg" :width="40" :height="40"/>
<!--                <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImg"></el-image>-->
              </template>
            </el-table-column>
            <el-table-column label="商品标题" prop="goodsTitle" width="266px">
              <template slot-scope="scope">
                {{scope.row.goodsTitle}}
                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>
<!--                <el-tag v-else>非赠品</el-tag>-->
              </template>
            </el-table-column>
            <el-table-column label="规格" prop="goodsSpec" width="150"></el-table-column>
            <el-table-column label="sku编码" prop="skuNum"></el-table-column>
<!--            <el-table-column label="外部ERP Sku编码" prop="outerErpSkuId"></el-table-column>-->
            <el-table-column label="单价" prop="goodsPrice" :formatter="amountFormatter"></el-table-column>
<!--            <el-table-column label="子订单金额" prop="itemAmount"></el-table-column>-->
<!--            <el-table-column label="实付金额" prop="payment"></el-table-column>-->
            <el-table-column label="数量" prop="quantity"></el-table-column>
<!--            <el-table-column label="是否赠品" prop="isGift">-->
<!--              <template slot-scope="scope">-->
<!--                <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>-->
<!--                <el-tag v-else>非赠品</el-tag>-->
<!--              </template>-->
<!--            </el-table-column>-->
            <el-table-column label="售后状态" prop="refundStatus">
              <template slot-scope="scope">
                <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
                <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
                <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag v-if="scope.row.refundStatus === 4">售后成功</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="订单状态" prop="refundStatus">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
                <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
                <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>
                <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="updateBy" ></el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button v-if="scope.row.orderStatus ===1 &&scope.row.refundStatus === 1" style="padding-left: 6px;padding-right: 6px"
                           size="mini"
                           icon="el-icon-delete"
                           @click="handleItemCancel(scope.row)"
                >取消</el-button>
                <el-button v-if="scope.row.orderStatus ===2 &&scope.row.refundStatus === 1" style="padding-left: 6px;padding-right: 6px"
                           size="mini"
                           icon="el-icon-delete"
                           @click="handleItemAfterSale(scope.row)"
                >售后</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

      </el-tabs>
    </el-dialog>

    <!-- 取消订单 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-form-item label="取消理由" prop="cancelReason" >
          <el-input v-model="form.cancelReason" type="textarea" placeholder="请输入内容" style="width:300px"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加订单售后对话框 -->
    <el-dialog title="添加售后" :visible.sync="afterSaleOpen" width="1000px" append-to-body>
      <el-form ref="afterSaleForm" :model="afterSaleForm" :rules="afterSaleRules" label-width="140px" inline>
        <el-row>
          <el-form-item label="售后类型" prop="type">
            <el-select v-model="afterSaleForm.type" placeholder="售后类型" style="width: 300px;">
              <el-option
                v-for="item in typeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="问题分类" prop="reasonType">
            <el-select v-model="afterSaleForm.reasonType" placeholder="问题分类" style="width: 300px;">
              <el-option v-for="item in reasonTypeList" :key="item.dictCode" :label="item.dictLabel" :value="item.dictValue">
              </el-option>
            </el-select>
          </el-form-item>

        </el-row>


        <el-row>
          <el-form-item label="退款金额"  prop="refundAmount">
            <el-input v-model="afterSaleForm.refundAmount" placeholder="请输入退款金额" style="width: 300px;" @input="handleRefundAmountInput"/>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="买家是否需要退货" prop="hasGoodReturn">
            <el-select v-model="afterSaleForm.hasGoodReturn" placeholder="买家是否需要退货" style="width: 300px;">
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
          </el-form-item>

        </el-row>
        <el-row>
          <el-form-item label="附件" prop="image">
            <image-upload v-model="afterSaleForm.attachment" :limit="1"/>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="afterSaleForm.remark"  style="width: 300px;" placeholder="请输入备注" />
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAfterSaleForm">添加售后</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listOrder, getOrder,cancelOrder, pushOms} from "@/api/order/sales_order";
import { getShop, listShop } from '@/api/shop/shop'
import Clipboard from "clipboard";
import { listAllMerchant } from '@/api/shop/merchant'
import { getShopListData } from '@/utils/shopUtils'
import {addSaleOrderAfter} from "@/api/refund/refund";
import {getDicts} from "@/api/system/dict/data";
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";



export default {
  name: "ErpSalesOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: false,
      pushLoading: false,
      open: false,
      afterSaleOpen: false,
      activeName: 'orderDetail',
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
      title: '',
      // 店铺订单表格数据
      orderList: [],
      shopListAll: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      merchantList:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      //售后类型。(1-售前退款 10-退货 20-换货 30-维修 40-大家电安装 50-大家电移机 60-大家电增值服务 70-上门维修 90-优鲜赔 80-补发商品 100-试用收回 11-仅退款)
      typeList: [
        // {
        //   value: '1',
        //   label: '售前退款'
        // }, {
        //   value: '11',
        //   label: '仅退款'
        // },
        {
          value: '10',
          label: '退货'
        }
        , {
          value: '20',
          label: '换货'
        }, {
          value: '30',
          label: '维修'
        }
        // , {
        //   value: '80',
        //   label: '订单补发'
        // }
      ],
      // 问题分类
      reasonTypeList: [],
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        merchantId: null,
        shopId: null,
        startTime: null,
        endTime: null,
        refundStatus: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      afterSaleForm: {
        orderItemId:null,
        orderId:null,
        refundAmount:0.0
      },
      // 表单校验
      rules: {},
      afterSaleRules: {
        type: [{ required: true, message: "请选择售后类型", trigger: "blur" }],
        reasonType: [{ required: true, message: "请选择售后原因", trigger: "blur" }],
        refundAmount: [{ required: true, message: "不能为空", trigger: "blur" }],
        hasGoodReturn: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    getDicts("refund_reason_type").then(resp=>{
      this.reasonTypeList = resp.data
    })
    listAllMerchant().then(response => {
      this.merchantList = response.data;
      this.merchantLoading = false;
    })
    this.shopLoading = true
    // listShop({merchantId:this.queryParams.merchantId}).then(response => {
    //   this.shopList = response.rows;
    //   // if (this.shopList && this.shopList.length > 0) {
    //   //   this.queryParams.shopId = this.shopList[0].id
    //   // }
    //   this.shopLoading = false
    // });

    this.getList();
  },
  async mounted() {
    this.shopListAll = await getShopListData(); // 获取并设置 shopList
  },
  methods: {
    handleClose() {
      // 刷新页面
      // location.reload();
      console.log("=====关闭，刷新数据=======")
      this.getList()
    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({merchantId:this.queryParams.merchantId,name:query}).then(response => {
          this.shopList = response.rows;
          this.shopLoading = false
        })
      }
    },
    async  getShopShow(shopId){
      try {
        const res = await getShop(shopId);
        console.log(res.data.name);
        return res.data.name;
      } catch (error) {
        console.error("Error fetching shop:", error);
        return null; // 或者根据需要返回其他值
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
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push('sales_order_create');
    },
    handleAdd2() {
      this.$router.push('sales_order_create_h5');
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
    handlePushOms(row) {
      const ids = row.orderNum || this.ids;
      this.$modal.confirm('是否手动推送到系统？').then(function() {
         pushOms({ids:ids}).then(resp=>{
           this.getList()
         });
      }).then(() => {
        // this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
    reset(){

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
    // 取消按钮
    cancel() {
      this.open = false;
      this.afterSaleOpen = false
      this.reset();
    },
    //取消订单
    handleCancel(row){
      this.form.id = row.id
      this.form.remark=''
      this.open = true;
      this.title = "取消订单";
    },
    handleItemCancel(row){
      this.form.id = row.orderId
      this.form.orderItemId = row.id
      this.form.remark=''
      this.open = true;
      this.title = "取消子订单";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("===========itemId====",this.form.orderItemId,this.form.id)
          const orderItemId = this.form.orderItemId
          const orderId = this.form.id
          cancelOrder(this.form).then(response => {
            this.$modal.msgSuccess("订单取消成功");
            // this.form.id = row.orderId
            // let a=this.form.itemList.filter(x=>x.id ===orderItemId)
            // if(a){
            //   console.log('====找到了item======')
            //   a.orderStatus = 11
            //   a.refundStatus =4
            // }
            getOrder(orderId).then(response => {
              this.form = response.data;
              this.activeName='orderItems'
              this.detailOpen = true;
              this.open = false;
              this.detailTitle = "订单详情";
            });
            // this.open = false;
            // this.getList();
          });
        }
      });
    },
    // 售后
    handleItemAfterSale(row){
      this.afterSaleForm.orderItemId = row.id
      this.afterSaleForm.orderId = row.orderId
      this.afterSaleOpen = true
    },
    submitAfterSaleForm(){
      this.$refs["afterSaleForm"].validate(valid => {
        if (valid) {
          if(!this.afterSaleForm.orderItemId){
            this.$modal.msgError("请选择订单明细")
          }
          // 将价格字段从字符串转换回数字类型
          this.afterSaleForm.refundAmount = stringToNumber(this.afterSaleForm.refundAmount);
          const orderId = this.afterSaleForm.orderId
          const orderItemId = this.afterSaleForm.orderItemId


          console.log('======添加售后====',this.afterSaleForm)
          addSaleOrderAfter(this.afterSaleForm).then(response => {
            console.log(response);
            if(response.code===200){
              this.$modal.msgSuccess("添加成功")
              getOrder(orderId).then(response => {
                this.form = response.data;
                this.activeName='orderItems'
                this.detailOpen = true;
                this.open = false;
                this.afterSaleForm = {}
                this.afterSaleOpen = false
                this.detailTitle = "订单详情";
              });

              // this.open =false
              // this.getList();
            }else{
              this.$modal.msgError(response.msg)
            }
          })
        }
      })
    },
    handleRefundAmountInput() {
      this.afterSaleForm.refundAmount = limitDecimalLength(this.afterSaleForm.refundAmount);
    }
  }
};
</script>
