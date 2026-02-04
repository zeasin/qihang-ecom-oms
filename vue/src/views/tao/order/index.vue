<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="tid">
        <el-input
          v-model="queryParams.tid"
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
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间" prop="startTime">
        <el-date-picker clearable @change="handleQuery"
                        v-model="queryParams.startTime" value-format="yyyy-MM-dd"
                        type="date" placeholder="下单时间">
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="下单日期" prop="orderCreateTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.orderCreateTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择订单创建时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item label="订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="等待卖家发货" value="WAIT_SELLER_SEND_GOODS" ></el-option>
          <el-option label="等待买家确认收货" value="WAIT_BUYER_CONFIRM_GOODS"></el-option>
          <el-option label="交易成功" value="TRADE_FINISHED"> </el-option>
          <el-option label="交易自动关闭" value="TRADE_CLOSED"></el-option>
          <el-option label="卖家或买家主动关闭交易" value="TRADE_CLOSED_BY_TAOBAO"></el-option>
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
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取订单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="pullLoading"-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handlePullDetailByTid"-->
<!--        >API拉取单个订单</el-button>-->
<!--      </el-col>-->

<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handlePullUpdate"-->
<!--        >手动更新订单</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="订单号" align="center" prop="tid" />-->
      <el-table-column label="订单号" align="left" prop="tid" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.tid}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.tid" @click="copyActiveCode($event,scope.row.tid)" ></i>
          <br/>
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="店铺" align="center" prop="shopId" >-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name:''  }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品明细" align="center" width="900px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">SKU名</td>
              <td width="200" align="left">Sku编码</td>
              <td width="150" align="left">平台SkuId</td>
              <td width="50" align="left">数量</td>
            </th>
          </table>
        </template>
        <template slot-scope="scope" >
          <el-table :data="scope.row.items" :show-header="false" :cell-style="{border:0 + 'px' }"  :row-style="{border:0 + 'px' }" >
            <el-table-column label="商品图片" width="50px">
              <template slot-scope="scope">
                <!--                <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>-->
                <image-preview :src="scope.row.picPath" :width="40" :height="40"/>
              </template>
            </el-table-column>
            <el-table-column label="商品名" align="left" width="250px" prop="title" />
            <el-table-column label="SKU名" align="left" prop="skuPropertiesName" width="150"  :show-overflow-tooltip="true"/>
            <el-table-column label="Sku编码" align="left" prop="outerSkuId" width="200"/>
            <el-table-column label="平台SkuId" align="left" prop="skuId" width="150"/>
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.num}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
<!--      <el-table-column label="商品" width="350">-->
<!--          <template slot-scope="scope">-->
<!--            <el-row v-for="item in scope.row.items" :key="item.id" :gutter="20">-->

<!--            <div style="float: left;display: flex;align-items: center;" >-->
<!--              <el-image  style="width: 70px; height: 70px;" :src="item.picPath"></el-image>-->
<!--              <div style="margin-left:10px">-->
<!--              <p>{{item.title}}【{{item.skuPropertiesName}}】-->
<!--              </p>-->
<!--                <p>SKU编码：{{item.outerSkuId}}</p>-->
<!--                <p>数量：<el-tag size="small">x {{item.num}}</el-tag></p>-->
<!--              </div>-->
<!--            </div>-->
<!--            </el-row>-->
<!--          </template>-->
<!--      </el-table-column>-->
      <el-table-column label="实付总金额" align="center" prop="payment" :formatter="amountFormatter" />
      <el-table-column label="订单创建时间" align="center" prop="orderCreateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.created) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="买家留言" align="center" prop="buyerMessage" />
<!--      <el-table-column label="卖家备注" align="center" prop="sellerMemo" />-->
      <el-table-column label="订单状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'WAIT_BUYER_PAY'">等待买家付款</el-tag>
          <el-tag v-if="scope.row.status === 'SELLER_CONSIGNED_PART'">卖家部分发货</el-tag>
          <el-tag v-if="scope.row.status === 'WAIT_SELLER_SEND_GOODS'">待发货</el-tag>
          <el-tag v-if="scope.row.status === 'WAIT_BUYER_CONFIRM_GOODS'">待买家收货</el-tag>
          <el-tag v-if="scope.row.status === 'TRADE_FINISHED'">交易成功</el-tag>
          <el-tag v-if="scope.row.status === 'TRADE_CLOSED'">交易自动关闭</el-tag>
          <el-tag v-if="scope.row.status === 'TRADE_CLOSED_BY_TAOBAO'">关闭交易</el-tag>
          <el-tag v-if="scope.row.status === 'PAID_FORBID_CONSIGN'">禁止发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="确认状态" align="center" prop="auditStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.auditStatus === 0" type="warning" style="margin-bottom: 6px;">待确认</el-tag>
          <el-tag v-if="scope.row.auditStatus === 1" style="margin-bottom: 6px;">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button style="padding-right: 6px;padding-left: 6px"
                     v-if="scope.row.auditStatus === 0"
                     size="mini"
                     type="success" plain
                     icon="el-icon-success"
                     @click="handleConfirm(scope.row)"
                     v-hasPermi="['dou:order:edit']"
          >确认订单</el-button>
          <el-button style="padding-right: 6px;padding-left: 6px"
            :loading="pullLoading"
            size="mini"
            icon="el-icon-refresh"
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
    <!-- 订单审核、订单详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="1000px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
        <el-descriptions title="订单信息">
          <el-descriptions-item label="ID">{{form.id}}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{form.tid}}</el-descriptions-item>
          <el-descriptions-item label="店铺">
            {{ shopList.find(x=>x.id === form.shopId)?shopList.find(x=>x.id === form.shopId).name:'' }}
          </el-descriptions-item>
          <el-descriptions-item label="订单类型">
            <el-tag size="small" v-if="form.type==='fixed'">一口价订单</el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="订单状态">
            <el-tag v-if="form.status === 'WAIT_BUYER_PAY'">等待买家付款</el-tag>
            <el-tag v-if="form.status === 'SELLER_CONSIGNED_PART'">卖家部分发货</el-tag>
            <el-tag v-if="form.status === 'WAIT_SELLER_SEND_GOODS'">待发货</el-tag>
            <el-tag v-if="form.status === 'WAIT_BUYER_CONFIRM_GOODS'">待买家收货</el-tag>
            <el-tag v-if="form.status === 'TRADE_FINISHED'">交易成功</el-tag>
            <el-tag v-if="form.status === 'TRADE_CLOSED'">交易自动关闭</el-tag>
            <el-tag v-if="form.status === 'TRADE_CLOSED_BY_TAOBAO'">关闭交易</el-tag>
            <el-tag v-if="form.status === 'PAID_FORBID_CONSIGN'">禁止发货</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否有买家留言">
            <el-tag size="small" v-if="form.hasBuyerMessage ==='false' ">否</el-tag>
            <el-tag size="small" v-if="form.hasBuyerMessage ==='true' ">是</el-tag>
          </el-descriptions-item>


          <el-descriptions-item label="买家备注">
            {{form.buyerMessage}}
          </el-descriptions-item>
          <el-descriptions-item label="卖家备注">
            {{form.sellerMemo}}
          </el-descriptions-item>

          <el-descriptions-item label="创建时间">{{ form.created }}</el-descriptions-item>
          <el-descriptions-item label="支付时间"> {{form.payTime}}</el-descriptions-item>
          <el-descriptions-item label="更新时间"> {{ form.modified }}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions title="付款信息">

          <el-descriptions-item label="优惠金额">{{amountFormatter(null,null,form.discountFee) }}</el-descriptions-item>
          <el-descriptions-item label="手工调整金额">{{amountFormatter(null,null,form.adjustFee) }}</el-descriptions-item>
          <el-descriptions-item label="运费">{{amountFormatter(null,null,form.postFee) }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">{{amountFormatter(null,null,form.totalFee) }}</el-descriptions-item>
          <el-descriptions-item label="支付金额"> {{ amountFormatter(null,null,form.payment) }}</el-descriptions-item>
        </el-descriptions>


        <el-descriptions title="收货信息">
          <el-descriptions-item label="收件人姓名">{{form.receiverName}}</el-descriptions-item>
          <el-descriptions-item label="收件人手机号">{{form.receiverMobile}}</el-descriptions-item>
          <el-descriptions-item label="省市区">{{form.receiverState}}{{form.receiverCity}}{{form.receiverDistrict}}{{form.receiverTown}}</el-descriptions-item>
          <el-descriptions-item label="详细地址">{{form.receiverAddress}}</el-descriptions-item>
        </el-descriptions>
        <!--        <el-descriptions title="发货信息">-->
        <!--          &lt;!&ndash; <el-descriptions-item label="发货方式">-->
        <!--            <el-tag v-if="form.shipType === 1"  type="danger">供应商代发</el-tag>-->
        <!--              <el-tag v-if="form.shipType === 0" type="danger">仓库发货</el-tag>-->
        <!--          </el-descriptions-item> &ndash;&gt;-->
        <!--          <el-descriptions-item label="物流公司">{{form.logisticsCompany}}</el-descriptions-item>-->
        <!--          <el-descriptions-item label="物流单号">{{form.logisticsCode}}</el-descriptions-item>-->
        <!--          <el-descriptions-item label="发货时间">{{form.logisticsTime}}</el-descriptions-item>-->
        <!--        </el-descriptions>-->
        <el-divider content-position="center">订单商品</el-divider>
        <el-table :data="form.items"  style="margin-bottom: 10px;">
          <el-table-column label="序号" align="center" type="index" width="50"/>

          <el-table-column label="图片" width="50">
            <template slot-scope="scope">
              <el-image style="width: 45px; height: 45px" :src="scope.row.picPath"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="标题" prop="title" ></el-table-column>
          <el-table-column label="规格" prop="skuPropertiesName" width="150"></el-table-column>
          <el-table-column label="sku编码" prop="outerSkuId"></el-table-column>
          <el-table-column label="单价" prop="price" :formatter="amountFormatter"></el-table-column>
          <el-table-column label="数量" prop="num"></el-table-column>
          <el-table-column label="实付金额" prop="payment" :formatter="amountFormatter"></el-table-column>
        </el-table>

        <el-divider content-position="center"  v-if="isAudit" >收件人</el-divider>

        <el-form-item label="收件人姓名" prop="receiverName" v-if="isAudit">
          <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" style="width:350px" />
        </el-form-item>
        <el-form-item label="收件人电话" prop="receiverMobile" v-if="isAudit">
          <el-input v-model="form.receiverMobile" placeholder="请输入收件人电话" style="width:350px" />
        </el-form-item>
        <el-form-item label="省市区" prop="provinces" v-if="isAudit">
          <el-cascader style="width:350px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="receiverAddress" v-if="isAudit">
          <el-input v-model="form.receiverAddress" placeholder="请输入收件地址" style="width:350px" />
        </el-form-item>
        <!--        <el-form-item label="发货方式" prop="shipType" v-if="isAudit">-->
        <!--          <el-select v-model="form.shipType" placeholder="发货类型" style="width:350px">-->
        <!--            <el-option label="供应商代发" value="1"></el-option>-->
        <!--            <el-option label="仓库发货" value="0"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer" v-if="isAudit">
        <el-button type="primary" @click="submitConfirmForm" v-if="form.auditStatus===0">确认发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listOrder, pullOrder, getOrder, pullOrderDetail,confirmOrder} from "@/api/tao/order";
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "../../../utils/request";
import Clipboard from "clipboard";
import {pcaTextArr} from "element-china-area-data";
import {float} from "quill/ui/icons";
export default {
  name: "OrderTao",
  data() {
    return {
      pcaTextArr,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      pullLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      detailOpen: false,
      multiple: true,
      isAudit: false,
      detailTitle:'',
      // 总条数
      total: 0,
      // 淘宝订单表格数据
      orderList: [],
      shopList:[],
      orderTime:null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        tid: null,
        startTime: null,
        endTime: null,
        status: null
      },
      // 表单参数
      form: {
      },
      rules: {
        receiverName:[{ required: true, message: "不能为空", trigger: "blur" }],
        receiverMobile:[{ required: true, message: "不能为空", trigger: "blur" }],
        provinces:[{ required: true, message: "不能为空", trigger: "blur" }],
        receiverAddress:[{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    listShop({type: 100}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
    // this.getList();
  },
  methods: {
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
      cellValue = parseFloat(cellValue)
      return '￥' + cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询淘宝订单列表 */
    getList() {
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        if(!this.queryParams.startTime) {
          this.queryParams.startTime = null
          this.queryParams.endTime = null
        }
      }
      this.loading = true;
      listOrder(this.queryParams).then(response => {
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
      this.orderTime=null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tid)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handlePullDetailByTid(){
      if(this.queryParams.shopId && this.queryParams.tid) {
        this.pullLoading = true
        pullOrderDetail({shopId:this.queryParams.shopId,orderId:this.queryParams.tid}).then(response => {
          console.log('拉取淘宝订单接口返回=====',response)
          this.$modal.msgSuccess(JSON.stringify(response));
          this.pullLoading = false
        })
      }else{
        this.$modal.msgSuccess("请先输入订单号并且选择店铺");
      }
    },
    handlePull() {
      if (!this.queryParams.shopId) {
        this.$modal.msgError("请先选择店铺");
        return
      }
      if (!this.queryParams.startTime) {
        this.$modal.msgError("请选择下单时间")
        return
      }
        this.pullLoading = true
        pullOrder({shopId:this.queryParams.shopId,updType:0,orderDate: this.queryParams.startTime}).then(response => {
          console.log('拉取淘宝订单接口返回=====',response)
          if(response.code === 1401) {
              MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
                this.$router.push({path:"/shop/shop_list",query:{type:1}})
                // isRelogin.show = false;
                // store.dispatch('LogOut').then(() => {
                // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
                // })
              }).catch(() => {
                isRelogin.show = false;
              });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            this.getList()
          }else{
            this.$modal.msgError(response.msg);
          }
          this.pullLoading = false
        })


      // this.$modal.msgSuccess("请先配置API");
    },
    handlePullUpdate(row) {
      const id = row.tid || this.ids[0]
      if(!this.queryParams.shopId){
        this.$modal.msgError("请选择店铺");
        return
      }
      console.log("======更新订单==",id)
      // 接口拉取订单并更新
      this.pullLoading = true
      pullOrderDetail({shopId:this.queryParams.shopId,orderId:id}).then(response => {
          console.log('拉取淘宝订单接口返回=====',response)
        this.$modal.msgSuccess(JSON.stringify(response));
        this.pullLoading = false
      })
    },
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.detailOpen = true;
        this.detailTitle = "订单详情";
      });
      this.isAudit = false
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

        }
      });
    },

    handleConfirm(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.form.provinces = []
        this.form.provinces.push(response.data.receiverState)
        this.form.provinces.push(response.data.receiverCity)
        this.form.provinces.push(response.data.receiverDistrict)
        this.detailOpen = true;
        this.detailTitle = "确认订单";
        this.isAudit = true
      });
    },
    submitConfirmForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          const form = {
            orderId:this.form.id,
            province:this.form.provinces[0],
            city:this.form.provinces[1],
            town:this.form.provinces[2],
            address:this.form.receiverAddress,
            receiver:this.form.receiverName,
            mobile:this.form.receiverMobile
          }

          confirmOrder(form).then(response => {
            if(response.code===200){
              this.$modal.msgSuccess("订单确认成功");
              this.detailOpen = false;
              this.isAudit = false
              this.getList();
            }else{
              this.$modal.msgError(response.msg);
            }

          });

        }
      })
    },
  }
};
</script>
