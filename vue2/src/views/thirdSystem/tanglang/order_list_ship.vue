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
<!--      <el-form-item label="下单时间" prop="orderTime">-->
<!--          <el-date-picker clearable-->
<!--            v-model="orderTime" value-format="yyyy-MM-dd"-->
<!--            type="daterange"-->
<!--            range-separator="至"-->
<!--            start-placeholder="开始日期"-->
<!--            end-placeholder="结束日期">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="审核状态" prop="confirmStatus">-->
<!--        <el-select v-model="queryParams.confirmStatus" placeholder="请选择审核状态" clearable @change="handleQuery">-->
<!--          <el-option label="已确认" value="1" ></el-option>-->
<!--          <el-option label="待确认" value="2"></el-option>-->
<!--      1：待发货，2：已发货，3：已完成，11已取消；12退款中；21待付款；22锁定，29删除，101部分发货-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="待发货" value="1" ></el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="已完成" value="3"></el-option>
          <el-option label="已取消" value="11"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >手动添加发货订单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="pullLoading"-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handlePull()"-->
<!--        >API商品拉取订单</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handlePushOms"
        >重新推送选中订单到订单库</el-button>
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
            @click="handleDetail(scope.row)"
          >{{scope.row.orderId}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderId" @click="copyActiveCode($event,scope.row.orderId)" ></i>
        </template>
      </el-table-column>

      <el-table-column label="商品" width="350">
          <template slot-scope="scope">
<!--            <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">-->
<!--            <div style="float: left;display: flex;align-items: center;" >-->
<!--              <div style="margin-left:10px">-->
<!--              <p>{{item.title}}-->
<!--                <span v-if="item.skuName">【{{item.skuName}}&nbsp;】</span>-->
<!--              </p>-->
<!--              <p>-->
<!--                <span v-if="item.productId">商品ID：{{item.productId}}</span>-->
<!--                <span v-if="item.skuId">规格ID：{{item.skuId}}</span>-->
<!--                <span v-if="item.skuCode">规格编码：{{item.skuCode}}</span>-->
<!--                <el-tag size="small">x {{item.quantity}}</el-tag>-->
<!--                </p>-->
<!--              </div>-->
<!--            </div>-->
<!--            </el-row>-->
            <div style="float: left;display: flex;align-items: center;padding-right: 20px"  @click="handleDetail(scope.row,'orderItems')">
              <div style="margin-left:10px">
                <div style="width: 350px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" :title="scope.row.items[0].title">
                  {{scope.row.items[0].title}}
                </div>
                <div>
                  <span style="color: #5a5e66;font-size: 11px">规格：</span>
                  <el-tag size="small">{{scope.row.items[0].skuName}}</el-tag>&nbsp;
                  <span>
                    <span style="color: #5a5e66;font-size: 11px">数量：</span>
                    <el-tag size="small">x {{scope.row.items[0].quantity}}</el-tag>
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
            <div style="float: left;display: flex;align-items: center;padding-left: 5px"  v-if="scope.row.items.length>1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleDetail(scope.row,'orderItems')"
              >更多订单商品（{{scope.row.items.length}}）</el-button>
            </div>
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
      <el-table-column label="收件人" prop="receiverName" ></el-table-column>
      <el-table-column label="手机号" prop="receiverPhone" ></el-table-column>
      <el-table-column label="省" prop="province" ></el-table-column>
      <el-table-column label="市" prop="city" ></el-table-column>
      <el-table-column label="区" prop="county" ></el-table-column>
      <el-table-column label="详细地址" prop="address" ></el-table-column>
      <el-table-column label="状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <!--      1：待发货，2：已发货，3：已完成，11 已取消；12退款中；21待付款；22锁定，29删除，101部分发货-->
          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime * 1000) }}


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
    <!-- 订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1400px" append-to-body>
      <el-tabs v-model="activeName" >

        <el-tab-pane label="商品列表" name="orderItems" >
          <el-table :data="form.items"  style="margin-bottom: 10px;">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>

            <el-table-column label="商品标题" prop="title" width="260px">
              <template slot-scope="scope">
                {{scope.row.title}}
              </template>
            </el-table-column>
            <el-table-column label="规格" prop="skuName" width="150">
              <template slot-scope="scope">
                {{ scope.row.skuName}}
              </template>
            </el-table-column>
            <el-table-column label="sku编码" prop="skuCode"></el-table-column>
            <el-table-column label="平台SkuId" prop="skuId"></el-table-column>
            <el-table-column label="商品库SkuId" prop="erpGoodsSkuId"></el-table-column>
            <el-table-column label="数量" prop="quantity"></el-table-column>

          </el-table>
        </el-tab-pane>

      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {getToken} from "@/utils/auth";
import {
  listTanglangOrderList,
  pullTanglangSwOrderList,
  pushOms
} from '@/api/shop/tanglang'
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import Clipboard from "clipboard";
import { cancelOrder, getShopOrder } from '@/api/shop/order'
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
      shopList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      activeName: 'orderItems',
      detailTitle: '',
      orderTime: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deliverMethod: 0,
        orderMode: 1,
        orderId: null,
        shopId: null,
        startTime: null,
        endTime: null,
        refundStatus: null,
        orderStatus: null,
      },
      // 表单参数
      form:{
        id:null,
        cancelReason:null,
      },
      // 表单校验
      rules: {
        cancelReason: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    listShop({type:911}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
  },
  methods: {
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
      this.detailOpen = false;
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path:'/order/shop_order_create',query:{shopType:911}});
    },
    //取消订单
    handleCancel(row){
        this.form.id = row.id
        this.open = true;
        this.title = "取消订单";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          cancelOrder(this.form).then(response => {
            this.$modal.msgSuccess("订单取消成功");
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
      pullTanglangSwOrderList({shopId:this.queryParams.shopId}).then(resp => {
        if (resp.code == 200) {
          this.$modal.msgSuccess("拉取成功");
          this.getList()
        } else {
          this.$modal.msgError(resp.msg);
        }

        this.pullLoading = false
      })
      // this.$modal.msgSuccess("请先配置API");
    },
    /** 详情按钮操作 */
    handleDetail(row,tagInd) {
      this.reset();
      const id = row.id || this.ids
        this.form = row;
        console.log('====订单详情',this.form)
        this.detailOpen = true;
        this.detailTitle = "订单详情";

      this.isAudit = false
    },
    handlePushOms(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否手动推送到系统？').then(function() {
        return pushOms({ids:ids});
      }).then(() => {
        // this.getList();
        this.$modal.msgSuccess("推送成功");
      }).catch(() => {});
    },
  }
};
</script>
