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
      </el-form-item><el-form-item label="商户" prop="merchantId" v-if="!isMerchant">
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
      <el-form-item label="平台" prop="shopType">
        <el-select v-model="queryParams.shopType" placeholder="请选择平台" clearable @change="handleQuery">
          <el-option label="总部销售订单" value="0"></el-option>
          <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
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
<!--          <el-tag type="success" v-if="scope.row.shopType===0">总部销售订单</el-tag>-->
<!--          <el-tag type="info" v-else>{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : (typeList.find(x=>x.id === scope.row.shopType) ? typeList.find(x=>x.id === scope.row.shopType).name : '未知平台' ) }}</el-tag>-->

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
            type="success"
            plain
            v-if="scope.row.orderStatus === 1"
            icon="el-icon-download"
            size="mini"
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
    <el-dialog title="打包发货" :visible.sync="shipOpen" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
<!--        <el-descriptions title="订单信息">-->
<!--          <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="订单号">{{form.orderNum}}</el-descriptions-item>-->

<!--          <el-descriptions-item label="店铺">-->
<!--            {{ shopList.find(x=>x.id == form.shopId)?shopList.find(x=>x.id == form.shopId).name:'' }}-->
<!--            <el-tag size="small" v-if="form.shopType === 5">微信小店</el-tag>-->

<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="买家留言">-->
<!--            {{form.buyerMemo}}-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="备注">-->
<!--            {{form.remark}}-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="创建时间">-->
<!--            {{ parseTime(form.createTime) }}-->
<!--          </el-descriptions-item>-->

<!--          <el-descriptions-item label="订单状态">-->
<!--            <el-tag v-if="form.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>-->
<!--            <el-tag v-if="form.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>-->
<!--            <el-tag v-if="form.orderStatus === 3" style="margin-bottom: 6px;">已签收</el-tag>-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="退款状态">-->
<!--            <el-tag v-if="form.refundStatus === 1">无售后或售后关闭</el-tag>-->
<!--            <el-tag v-if="form.refundStatus === 2">售后处理中</el-tag>-->
<!--            <el-tag v-if="form.refundStatus === 3">退款中</el-tag>-->
<!--            <el-tag v-if="form.refundStatus === 4">退款成功</el-tag>-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="">-->
<!--            &lt;!&ndash;            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>&ndash;&gt;-->
<!--            &lt;!&ndash;            <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>&ndash;&gt;-->
<!--          </el-descriptions-item>-->
<!--          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="省市区">{{form.province}}{{form.city}}{{form.town}}</el-descriptions-item>-->
<!--          <el-descriptions-item label="详细地址">{{form.address}}</el-descriptions-item>-->
<!--        </el-descriptions>-->
        <el-divider content-position="center">商品明细</el-divider>
        <el-table :data="form.itemVoList"  style="margin-bottom: 30px;">
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column label="序号" align="center" type="index" width="50"/>

<!--          <el-table-column label="商品图片" prop="goodsImg" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="商品标题" prop="goodsTitle" ></el-table-column>
          <el-table-column label="SKU" prop="goodsSpec" >
            <template slot-scope="scope">
              {{getSkuValues(scope.row.goodsSpec)}}
            </template>
          </el-table-column>
          <el-table-column label="sku编码" prop="skuNum"></el-table-column>
          <!--          <el-table-column label="单价" prop="goodsPrice"></el-table-column>-->
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <!-- <el-table-column label="商品金额" prop="itemAmount"></el-table-column> -->
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
            <el-form-item label="物流公司" prop="shippingCompany">
              <!--              <el-input v-model="form.shippingCompany" placeholder="请输入物流公司" style="width:300px" />-->
              <el-select v-model="form.shippingCompany" filterable r placeholder="选择快递公司" style="width:300px">
                <el-option v-for="item in logisticsList" :key="item.logisticsId" :label="item.logisticsName" :value="item.logisticsId">
                  <span style="float: left">{{ item.logisticsName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px;">{{ getPlatformName(item.platformId) }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="物流单号" prop="shippingNumber">
              <el-input v-model="form.shippingNumber" placeholder="请输入物流单号" style="width:300px" />
            </el-form-item>
<!--            <el-form-item label="仓库" prop="warehouseId" >-->
<!--              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width:300px">-->
<!--                <el-option-->
<!--                  v-for="item in warehouseList"-->
<!--                  :key="item.id"-->
<!--                  :label="item.warehouseName"-->
<!--                  :value="item.id">-->
<!--                </el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="包装费用" prop="packageAmount">-->
<!--              <el-input v-model="form.packageAmount" placeholder="请输入包装费用" style="width:300px" />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="物流费用" prop="shippingCost">-->
<!--              <el-input v-model="form.shippingCost" placeholder="请输入物流费用" style="width:300px" />-->
<!--            </el-form-item>-->
<!--            <el-form-item label="发货人" prop="shippingMan">-->
<!--              <el-input v-model="form.shippingMan" placeholder="请输入发货人" style="width:300px" />-->
<!--            </el-form-item>-->
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShipForm" :loading = "manualShipBtn">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {waitDistOrderList, getOrder } from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import { manualShipmentOrder, pushToCloudWarehouse } from '@/api/shipping/shipOrder'
import Clipboard from 'clipboard'
import { getFavoriteList } from '@/api/shipping/ship_logistics'
import {listMerchant} from "@/api/shop/merchant";

export default {
  name: "ManualShipOrderList",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
      shipJdSelect: true,
      isMerchant: false,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
      cloudWarehouseList: [],
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
      warehouseList:[],
      supplierList:[],
      // 弹出层标题
      detailTitle:'订单详情',
      detailOpen:false,
      isAudit:false,
      shipOpen:false,
      manualShipBtn:false,
      cloudWarehouseShipOpen:false,
      supplierShipOpen: false,
      activeName: 'orderDetail',
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopType: null,
        shopId: null,
        startTime: null,
        endTime: null,
        refundStatus: 1,
        orderStatus: 1,
        shipStatus:0
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
        warehouseId:null,
        shopId:null,
        shipperId:null,
        orderList:[]
      },

      // 表单校验
      rules: {
        // height: [{ required: true, message: '不能为空' }],
        // weight: [{ required: true, message: '不能为空' }],
        shippingNumber: [{ required: true, message: '不能为空' }],
        shippingCompany: [{ required: true, message: '不能为空' }],
        // warehouseId: [{ required: true, message: '请选择仓库' }],
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
  },
  created() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
    })
    listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if(resp.rows.length === 1&&resp.rows[0].id>0) {
        this.isMerchant = true;
      }
      listShop({ merchantId: this.queryParams.merchantId }).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
      });
      this.getList()
    })
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
      this.cloudWarehouseShipOpen = false
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
        this.form.shippingCost=4.0
        this.form.packageAmount=1.0
        getFavoriteList({shopType:response.data.shopType,status:1}).then(resp=>{
          this.logisticsList = resp.data || []
          this.shipOpen = true;
        })
        // this.detailTitle = "订单详情";
      });
    },
    // 获取平台名称
    getPlatformName(platformId) {
      return this.platformMap[platformId] || '未知'
    },
    // 手动发货表单
    submitShipForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.manualShipBtn = true
          manualShipmentOrder(this.form).then(resp =>{
            if(resp.code===200) {
              this.$modal.msgSuccess("手动发货成功");
              this.shipOpen = false
              this.getList()
            }else this.$modal.msgError(resp.msg);
            this.manualShipBtn = false
          })
        }
      })
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

