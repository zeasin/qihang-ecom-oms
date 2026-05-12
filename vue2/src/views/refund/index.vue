<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
      <el-form-item label="售后单号" prop="refundNum">
        <el-input
          v-model="queryParams.refundNum"
          placeholder="请输入售后单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable filterable @change="handleQuery">
          <el-option
            v-for="item in shopListAll"
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
      <el-form-item label="类型" prop="refundType">
        <el-select v-model="queryParams.refundType" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="售前退款" value="1" ></el-option>
          <el-option label="仅退款" value="11" ></el-option>
          <el-option label="退货" value="10"></el-option>
          <el-option label="换货" value="20"> </el-option>
          <el-option label="订单补发" value="80"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="hasProcessing">
        <el-select v-model="queryParams.hasProcessing" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="已处理" value="1" ></el-option>
          <el-option label="待处理" value="0" ></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="推送ERP状态" prop="hasLink">-->
<!--        <el-select v-model="queryParams.erpPushStatus" placeholder="推送ERP状态" clearable @change="handleQuery">-->
<!--          <el-option label="推送成功" value="200"></el-option>-->
<!--          <el-option label="推送失败" value="500"></el-option>-->
<!--          <el-option label="未推送" value="0"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" style="padding-left: 48px">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="single"-->

<!--          @click="handleRefundProcessing"-->
<!--        >处理选中售后</el-button>-->
<!--        &lt;!&ndash;          :disabled="multiple"&ndash;&gt;-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleAddRefund"-->
<!--        >添加销售订单售后</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="returnedList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="售后单号" align="left" prop="refundNum" width="230">
        <template slot-scope="scope">
          {{scope.row.refundNum}}
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.refundNum" @click="copyActiveCode($event,scope.row.refundNum)" ></i>
          <br />

          <el-tag type="info" v-if="scope.row.shopId===0">总部销售订单</el-tag>
          <el-tag type="info" v-else>{{ shopListAll.find(x=>x.id === scope.row.shopId) ? shopListAll.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="refundType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.refundType === 1">售前退款</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 11">仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 10">退货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 20">换货</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 30">维修</el-tag>
          <el-tag size="small" v-if="scope.row.refundType === 80">补发</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id === scope.row.shopId).name  }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="220">
        <template slot-scope="scope">
          {{scope.row.orderNum}}
        <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
        </template>
      </el-table-column>
      <el-table-column label="商品" align="left" prop="goodsName" width="300"/>
      <el-table-column label="规格" align="left" prop="goodsSku" />
      <el-table-column label="平台SkuId" align="left" prop="skuId" />
      <el-table-column label="商品库SkuId" align="left" prop="goodsSkuId" />
<!--      <el-table-column label="商品图片" align="center" prop="goodsImage" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <image-preview :src="scope.row.goodsImage" :width="50" :height="50"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="退款金额" align="center" prop="refundFee" :formatter="amountFormatter"/>
<!--      <el-table-column label="物流单号" align="center" prop="logisticsCode" />-->
<!--      <el-table-column label="收货时间" align="center" prop="receiveTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.receiveTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="是否需要退货" align="center" prop="hasGoodReturn" >
        <template slot-scope="scope">

          <el-tag style="margin-top: 5px" size="small" v-if="scope.row.hasGoodReturn === 1"> 买家需要退货</el-tag>
          <el-tag style="margin-top: 5px" size="small" v-if="scope.row.hasGoodReturn === 0"> 买家不需要退货</el-tag>

        </template>
      </el-table-column>
      <el-table-column label="退货物流" align="center" prop="trackingNumber" >
        <template slot-scope="scope">
          {{scope.row.returnLogisticsCompany}}<br/>
          {{scope.row.returnLogisticsCode}}
        </template>
      </el-table-column>
      <el-table-column label="售后理由" align="left" prop="refundReason" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <!--新状态-->
          <!--售后状态 0：售后申请 1：售后关闭，2：售后处理中，3：退款中，4： 售后成功-->
          <el-tag size="small" v-if="scope.row.status === 0"> 售后申请</el-tag>
          <el-tag size="small" v-if="scope.row.status === 1"> 售后关闭</el-tag>
          <el-tag size="small" v-if="scope.row.status === 2"> 售后处理中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 3"> 退款中</el-tag>
          <el-tag size="small" v-if="scope.row.status === 4"> 售后成功</el-tag>

          <!--旧状态-->
          <el-tag size="small" v-if="scope.row.status === 10002"> 等待买家退货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10005"> 等待卖家确认收货</el-tag>
          <el-tag size="small" v-if="scope.row.status === 14000"> 拒绝退款</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10011"> 售后关闭</el-tag>
          <el-tag size="small" v-if="scope.row.status === 10010"> 售后完成</el-tag>
          <br />
          <el-col type="info" style="margin-top: 5px" size="small" v-if="scope.row.hasProcessing === 1">
          处理结果：
            <el-tag size="small"  v-if="scope.row.processType == 0">无需处理</el-tag>
            <el-tag size="small" v-if="scope.row.processType == 10">退货</el-tag>
            <el-tag size="small" v-if="scope.row.processType == 20">换货</el-tag>
            <el-tag size="small" v-if="scope.row.processType == 30">维修</el-tag>
            <el-tag size="small"  v-if="scope.row.processType == 80">补发</el-tag>
            <el-tag size="small"  v-if="scope.row.processType == 99">订单拦截</el-tag>
          </el-col>
          <el-tag type="danger" style="margin-top: 5px" size="small" v-if="scope.row.hasProcessing === 0"> 待处理</el-tag>
<!--          <br />-->
<!--          <el-tag style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus === 200">已推送到ERP</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="!scope.row.erpPushStatus || scope.row.erpPushStatus === 0">待推送到ERP</el-tag>-->
<!--          <el-tag type="danger" style="margin-bottom: 6px;" v-if="scope.row.erpPushStatus > 200">推送错误</el-tag>-->
<!--          <div style="margin-bottom: 6px;color:red" v-if="scope.row.erpPushStatus > 200">{{scope.row.erpPushResult}}</div>-->
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" >
        <template slot-scope="scope">
          {{parseTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-left: 10px;padding-right: 10px;" v-if="scope.row.hasProcessing===0"
              type="primary"
              plain
              icon="el-icon-refresh"
              size="mini"
              @click="handleRefundProcessing(scope.row)"
            >处理</el-button>

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

    <!-- 添加或修改退换货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="订单号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="处理方式" prop="type">
          <el-select v-model="form.type" placeholder="售后处理方式" clearable @change="handleTypeChange">
            <el-option label="无需处理" value="0"></el-option>
            <el-option label="退货" value="10"></el-option>
            <el-option label="换货" value="20"></el-option>
            <el-option label="维修" value="30"></el-option>
            <el-option label="补发" value="80"></el-option>
            <el-option label="订单拦截" value="99"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否已发货" prop="hasGoodsSend">
          <el-select v-model="form.hasGoodsSend" placeholder="是否已发货" clearable>
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
<!--    <el-dialog title="手动添加售后" :visible.sync="addOpen" width="1000px" append-to-body>-->
<!--      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="140px" inline>-->
<!--        <el-row>-->
<!--          <el-form-item label="售后类型" prop="type">-->
<!--            <el-select v-model="addForm.type" placeholder="售后类型" style="width: 300px;">-->
<!--              <el-option-->
<!--                v-for="item in typeList"-->
<!--                :key="item.value"-->
<!--                :label="item.label"-->
<!--                :value="item.value">-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-form-item label="问题分类" prop="reasonType">-->
<!--            <el-select v-model="addForm.reasonType" placeholder="问题分类" style="width: 300px;">-->
<!--              <el-option v-for="item in reasonTypeList" :key="item.dictCode" :label="item.dictLabel" :value="item.dictValue">-->
<!--              </el-option>-->
<!--&lt;!&ndash;              <el-option&ndash;&gt;-->
<!--&lt;!&ndash;                v-for="item in reasonTypeList"&ndash;&gt;-->
<!--&lt;!&ndash;                :key="item.value"&ndash;&gt;-->
<!--&lt;!&ndash;                :label="item.label"&ndash;&gt;-->
<!--&lt;!&ndash;                :value="item.value">&ndash;&gt;-->
<!--&lt;!&ndash;              </el-option>&ndash;&gt;-->
<!--            </el-select>-->

<!--          </el-form-item>-->

<!--        </el-row>-->

<!--          <el-form-item label="订单" prop="orderId">-->
<!--            <el-row >-->
<!--            <el-select v-model="addForm.orderId" filterable remote reserve-keyword placeholder="订单号搜索"-->
<!--                       :remote-method="searchOrderByOrderId" :loading="orderListLoading" @change="orderChange" style="width: 300px;">-->
<!--              <el-option v-for="item in orderList" :key="item.id"-->
<!--                         :label="item.orderNum + '-' + item.province + ' ' + item.city + ' ' + item.town + ' ' + item.address + ' - ' + parseTime(item.createTime)"-->
<!--                         :value="item.id">-->
<!--              </el-option>-->
<!--            </el-select>-->


<!--              <el-table :data="orderItemList"  ref="singleTable" height="350px"  highlight-current-row @current-change="handleCurrentChange"  @row-click="handleRowClick">-->
<!--                <el-table-column type="selection" width="50" align="center" />-->
<!--                <el-table-column label="序号" align="center" type="index" width="50"/>-->
<!--&lt;!&ndash;                <el-table-column label="ItemId" prop="id" width="66"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;                <el-table-column label="子订单号" prop="subOrderNum" ></el-table-column>&ndash;&gt;-->
<!--                <el-table-column label="商品" prop="goodsTitle" width="260"></el-table-column>-->
<!--                <el-table-column label="规格" prop="goodsSpec" ></el-table-column>-->
<!--                <el-table-column label="图片" prop="goodsImg" width="55">-->
<!--                  <template slot-scope="scope">-->
<!--                    &lt;!&ndash; <el-input v-model="scope.row.goodsImg" placeholder="请输入商品图片" /> &ndash;&gt;-->
<!--                    <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImg"></el-image>-->
<!--                  </template>-->
<!--                </el-table-column>-->
<!--                <el-table-column label="SKU编码" prop="skuNum" ></el-table-column>-->
<!--                <el-table-column label="数量" prop="quantity" ></el-table-column>-->
<!--&lt;!&ndash;                <el-table-column label="退款状态" prop="refundStatus" width="150">&ndash;&gt;-->
<!--&lt;!&ndash;                  <template slot-scope="scope">&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;                    <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;                  </template>&ndash;&gt;-->
<!--&lt;!&ndash;                </el-table-column>&ndash;&gt;-->
<!--              </el-table>-->
<!--            </el-row>-->
<!--          </el-form-item>-->



<!--        <el-row>-->
<!--          <el-form-item label="退款金额"  prop="refundAmount">-->
<!--            <el-input type="number" v-model.number="addForm.refundAmount" placeholder="请输入退款金额" style="width: 300px;"/>-->
<!--          </el-form-item>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--          <el-form-item label="买家是否需要退货" prop="hasGoodReturn">-->
<!--            <el-select v-model="addForm.hasGoodReturn" placeholder="买家是否需要退货" style="width: 300px;">-->
<!--              <el-option label="是" value="1"></el-option>-->
<!--              <el-option label="否" value="0"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->

<!--        </el-row>-->
<!--        <el-row>-->
<!--        <el-form-item label="附件" prop="image">-->
<!--          <image-upload v-model="addForm.attachment" :limit="1"/>-->
<!--        </el-form-item>-->
<!--        </el-row>-->
<!--        <el-row>-->
<!--        <el-form-item label="备注" prop="remark">-->
<!--          <el-input type="textarea" v-model="addForm.remark"  style="width: 300px;" placeholder="请输入备注" />-->
<!--        </el-form-item>-->
<!--        </el-row>-->
<!--        &lt;!&ndash;        <el-form-item label="发货人" prop="shipOperator">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.shipOperator" placeholder="请输入发货人" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        &lt;!&ndash;        <el-form-item label="包裹重量" prop="packageWeight">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.packageWeight" placeholder="请输入包裹重量" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        &lt;!&ndash;        <el-form-item label="包裹长度" prop="packageLength">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.packageLength" placeholder="包裹长度" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        &lt;!&ndash;        <el-form-item label="包裹宽度" prop="packageWidth">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.width" placeholder="请输入包裹宽度" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        &lt;!&ndash;        <el-form-item label="包裹高度" prop="packageHeight">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.height" placeholder="请输入包裹高度" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitAddForm">添加售后</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
import {
  listReturned,
  getReturned,
  refundProcessing, addSaleOrderAfter
} from '@/api/refund/refund'
import { searchSaleOrder } from '@/api/order/sales_order'
import { getShopListData } from '@/utils/shopUtils'
import { getDicts } from '@/api/system/dict/data'
import {copyActiveCode, parseTime} from '@/utils/zhijian'

export default {
  name: "Returned",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      shopList:[],
      shopListAll:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 退换货表格数据
      returnedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      addOpen: false,
      orderListLoading: false,
      shopLoading: false,
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
        }
        // , {
        //   value: '80',
        //   label: '订单补发'
        // }
      ],
      // 问题分类
      reasonTypeList: [],
      orderList:[],
      orderItemList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundNum: null,
        refundType: null,
        orderNum: null,
        shopId: null,
        shopType: null,
        hasProcessing: null,
        status: null,
      },
      // 表单参数
      addForm: {

        orderItemId:null,
        reasonType:null,
        refundAmount:null,
        attachment:null,
        remark:null,
      },
      form: {
        refundId:undefined,
        hasGoodsSend:undefined,
        type:undefined,
        orderNum:undefined,
        sendLogisticsCode:undefined,
        returnLogisticsCode:undefined,
        reissueLogisticsCode:undefined,
        remark:undefined,
        receiverName:undefined,
        receiverTel:undefined,
        receiverAddress:undefined,
      },
      // 表单校验
      rules: {
        // orderNum: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        hasGoodsSend: [{ required: true, message: "请选择是否发货", trigger: "blur" }],
        type: [{ required: true, message: "请选择处理方式", trigger: "blur" }],

      },
      addRules: {
        type: [{ required: true, message: "请选择售后类型", trigger: "blur" }],
        reasonType: [{ required: true, message: "请选择售后原因", trigger: "blur" }],
        orderId: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        refundAmount: [{ required: true, message: "不能为空", trigger: "blur" }],
        hasGoodReturn: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    getDicts("refund_reason_type").then(resp=>{
      this.reasonTypeList = resp.data
    })
    this.getList();
  },
  async mounted() {
    this.shopListAll = await getShopListData(); // 获取并设置 shopList
  },
  methods: {
    parseTime,
    copyActiveCode,
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
    amountFormatter(row, column, cellValue, index) {
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询退换货列表 */
    getList() {
      this.loading = true;
      listReturned(this.queryParams).then(response => {
        this.returnedList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        refundId: null,
        hasGoodsSend: null,
        type: null,
        orderNum: null,
        sendLogisticsCode: null,
        returnLogisticsCode: null,
        reissueLogisticsCode: null,
        remark: null
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
    /** 处理方式选择*/
    handleTypeChange(nv){
      console.log("------",nv)
      if(nv===0){

      }
    },
    submitAddForm(){
      this.$refs["addForm"].validate(valid => {
        if (valid) {
          if(!this.addForm.orderItemId){
            this.$modal.msgError("请选择订单明细")
          }
          console.log('======添加售后====',this.addForm)
          addSaleOrderAfter(this.addForm).then(response => {
            console.log(response);
            if(response.code===200){
              this.$modal.msgSuccess("添加成功")
              this.addForm = {}
              this.orderList=[]
              this.orderItemList=[]
              this.addOpen = false
              this.getList();
            }else{
              this.$modal.msgError(response.msg)
            }
          })
        }
      })
    },
    submitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          refundProcessing(this.form).then(response => {
            console.log('======返回====',response)
            this.$modal.msgSuccess("处理成功！");
            this.open = false
            this.getList()
          });
        }
      })
    },
    /** 售后处理*/
    handleRefundProcessing(row){
      const id = row.id || this.ids[0]
        this.form.refundId = row.id
        this.form.orderNum = row.orderNum
        this.form.refundType = row.refundType
        this.form.sendLogisticsCode = row.sendLogisticsCode
      if(row.hasGoodReturn===1){
        this.form.hasGoodsSend = '1'
      }

      if(row.refundType===1){
        this.title = "售前退款"
        this.form.type = '0'
        this.form.hasGoodsSend = '0'
      }else  if(row.refundType===11){
        this.title = "仅退款"
        this.form.type = '0'
      }else  if(row.refundType===10){
        this.title = "退货"
        this.form.type = '10'
      }else  if(row.refundType===20){
        this.title = "换货"
        this.form.type = '20'
      }else  if(row.refundType===30){
        this.title = "维修"
        this.form.type = '30'
      }else  if(row.refundType===80){
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
    handleAddRefund() {
      this.addForm = {}
      this.orderList=[]
      this.orderItemList=[]
      this.addOpen = true
    },
  }
};
</script>
