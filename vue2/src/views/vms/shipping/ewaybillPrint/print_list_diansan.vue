<template>
  <div class="app-container" element-loading-text="取号中" v-loading="pageLoading">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="订单号" prop="orderNum">
          <el-input
            v-model="queryParams.orderNum"
            placeholder="请输入订单号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="商户" prop="merchantId">
          <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable @change="merchantChange" >
            <el-option label="总部" value="0"></el-option>
            <el-option
              v-for="item in merchantList"
              :key="item.merchantId"
              :label="item.name"
              :value="item.merchantId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="店铺" prop="shopId">
          <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="shopChange">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.shopName"
              :value="item.shopId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-select v-model="form.printer" placeholder="请选择打印机" clearable>-->
<!--            <el-option-->
<!--              v-for="item in printerList"-->
<!--              :key="item"-->
<!--              :label="item"-->
<!--              :value="item">-->
<!--            </el-option>-->
<!--          </el-select>-->

<!--        </el-form-item>-->
      </el-form>

    </el-row>


    <el-row :gutter="10" class="mb8">

        <el-col :span="1.5">

          <el-select size="mini"  v-model="form.accountId" placeholder="请选择电子面单账户" clearable>
            <el-option
              v-for="item in deliverList"
              :key="item.id"
              :label="item.siteName"
              :value="item.id">
              <span style="float: left">{{item.deliveryId}} - {{ item.siteName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px" >{{item.available}}</span>
            </el-option>
          </el-select>

        </el-col>


      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-printer"
          size="mini"
          :loading="pageLoading"
          :disabled="multiple"
          @click="handleGetEwaybillCode"
        >打单发货</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-d-arrow-right"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleShipSend"-->
<!--        >电子面单发货</el-button>-->
<!--      </el-col>-->
            <el-col :span="1.5">
              <el-select size="mini" v-model="form.printer" placeholder="请选择打印机" clearable>
                <el-option
                  v-for="item in printerList"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
                </el-option>
              </el-select>
            </el-col>
      <el-col :span="1.5">
      <el-button type="primary" plain size="mini" @click="refreshPrinterStatus" :loading="printerOpenedStatusRefresh">连接打印组件</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-view"-->
<!--            @click="handleDetail(scope.row)"-->
<!--          >{{scope.row.orderNum}} </el-button>-->
          {{scope.row.orderNum}}
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
          <br/>
<!--          <el-tag type="info">{{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}</el-tag>-->
        </template>>
      </el-table-column>

      <el-table-column label="商品明细" align="center" width="800px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">规格</td>
              <td width="200" align="left">规格ID</td>
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
            <el-table-column label="商品标题" align="left" width="250px" prop="goodsName" >
              <template slot-scope="scope">
                {{scope.row.goodsName}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="规格" align="left" prop="skuName" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <div>{{getSkuValues(scope.row.skuName)}}&nbsp;<br/>
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

      <el-table-column label="收件人" prop="receiverName" width="200px">
        <template slot-scope="scope">
          {{scope.row.receiverName}}&nbsp;
          {{scope.row.receiverMobile}} <br />
          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />

        </template>
      </el-table-column>
      <el-table-column label="面单号" align="center" prop="waybillCode" />
      <el-table-column label="取单状态" align="center" prop="waybillStatus" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.waybillStatus==0">未取号</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==1">已取号</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==2">已打印</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==3">已发货</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==10">手动发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.waybillStatus===1"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleCancelWaybill(scope.row)"
          >回收单号</el-button>
          <el-button style="padding-right: 6px;padding-left: 6px"
            size="mini" plain
                     v-if="scope.row.waybillStatus===1"
                     type="success"
            icon="el-icon-guid"
            @click="handleShipSend(scope.row)"
          >发货</el-button>
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
    <!-- 取号 -->
    <el-dialog title="取号打单" :visible.sync="getCodeOpen" width="900px" append-to-body  :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
<!--        <el-form-item label="电子面单账户" prop="accountId">-->
<!--            <el-select v-model="form.accountId" placeholder="请选择电子面单账户" clearable>-->
<!--              <el-option-->
<!--                v-for="item in deliverList"-->
<!--                :key="item.id"-->
<!--                :label="item.providerName"-->
<!--                :value="item.id">-->
<!--                <span style="float: left">{{ item.providerName }}</span>-->
<!--                <span style="float: right; color: #8492a6; font-size: 13px" >{{item.branchName}}:{{item.amount}}</span>-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--        </el-form-item>-->
        <el-form-item  label="打印机" prop="printer" >
          <el-select v-model="form.printer" placeholder="请选择打印机" clearable>
            <el-option
              v-for="item in printerList"
              :key="item.name"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-table v-loading="loading" :data="shipWaybillList">
          <el-table-column type="index" width="55" label="序号" align="center" />
          <el-table-column label="订单号" align="center" prop="orderId" ></el-table-column>
          <el-table-column label="物流公司" align="center" prop="logisticsCode" ></el-table-column>
          <el-table-column label="物流单号" align="center" prop="waybillCode" ></el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="small" @click="handlePrintEwaybill(false)">开始打印</el-button>
        <el-button type="success" size="small" @click="handlePrintEwaybill(true)">打印并发货</el-button>
        <el-button @click="cancel" size="small">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="请启动打印组件" :visible.sync="printerOpenedStatus" width="500px" append-to-body  :close-on-click-modal="false">
      <el-row>
        <el-col :span="24">
        <el-steps :space="200" :active="1" finish-status="success">
          <el-step title="选择发货物流"></el-step>
          <el-step title="启动打印组件"></el-step>
          <el-step title="开始打印"></el-step>
        </el-steps>
        </el-col>
        <el-col :span="24" style="text-align: center;padding-top: 100px">
        <el-button type="primary" plain size="medium" @click="refreshPrinterStatus" :loading="printerOpenedStatusRefresh">刷新</el-button>
        </el-col>

      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {listOrder, getOrder } from "@/api/vms/order";
import {getShopWaybillAccountList} from "@/api/vms/ewaybillAccount";
import {copyActiveCode} from "@/utils/zhijian";
import {listPlatform,listShop} from "@/api/vms/shop";
import {getWaybillCodeDiansan,getWaybillPrintDataDiansan,cancelWaybillCodeDiansan,pushShipSendDiansan} from "@/api/vms/ewaybill";
import { listMerchant } from '@/api/vms/merchant'


export default {
  name: "printDiansan",
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: true,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      pageLoading: false,
      // 选中数组
      ids: [],
      merchantList: [],
      shopList: [],
      shipWaybillList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 取号弹出
      getCodeOpen: false,
      printerOpenedStatusRefresh: false,
      printerOpenedStatus: false,
      printerOpenedTao: false,
      printerOpenedJd: false,
      printerOpenedPdd: false,
      printerOpenedDou: false,
      printerOpenedWei: false,
      printerOpenedKai: false,
      printerOpenedXhs: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundStatus: 1,
        shopType:null,
        merchantId:'0',
        orderStatus: '1',
        shopId: null
      },

      // 表单参数
      form: {
        accountId:null,
        printer: null
      },
      orderList: [],
      printerList: [],
      deliverList: [],
      // 表单校验
      rules: {
        // accountId: [{ required: true, message: '请选择电子面单账户' }],
        printer: [{ required: true, message: '请选择打印机' }],
      }
    };
  },
  created() {
    this.queryParams.shopType = this.shopType
    // this.openWs()
    // listPlatform({status:0}).then(res => {
    //   this.platformList = res.rows;
    // })
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
      // if (this.merchantList && this.merchantList.length > 0) {
      //   this.queryParams.merchantId = this.merchantList[0].id
      // }
      listShop({type: this.shopType,merchantId:this.queryParams.merchantId}).then(response => {
        this.shopList = response.rows;
        if (this.shopList && this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].shopId
        }
        getShopWaybillAccountList({shopId: this.queryParams.shopId}).then(response => {
          this.deliverList = response.data;
          if (this.deliverList && this.deliverList.length > 0) {
            this.form.accountId = this.deliverList[0].id
          }
          this.getList();
        });

      });
    })

  },
  methods: {
    copyActiveCode,
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.deliverList = []
      this.queryParams.shopId = null
      this.form.accountId = null
      listShop({merchantId:nv,type:this.shopType }).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        this.handleQuery()
      });
    },
    shopChange(nv){
      console.log('==============',nv);
      this.deliverList = []
      this.form.accountId = null
      getShopWaybillAccountList({shopId: this.queryParams.shopId}).then(response => {
        this.deliverList = response.data;
        if (this.deliverList && this.deliverList.length > 0) {
          this.form.accountId = this.deliverList[0].id
        }
        this.getList();
      });
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
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.getCodeOpen = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        erpSkuId: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    openWsJd() {
      const ws = new WebSocket('ws://127.0.0.1:9113');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
        "orderType":"GET_Printers"
        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        console.log("====接收到命令========",e)
        const arr = ["Fax", "OneNote for Windows 10", "Microsoft XPS Document Writer", "Microsoft Print to PDF", "Adobe PDF"];
        if (resp.code === '6') {
           // resp.detailinfo.printers.filter(x=> !arr.includes(x))
          $this.printerList = []
          for (let i=0;i<resp.detailinfo.printers.length;i++){
            $this.printerList.push({name:resp.detailinfo.printers[i]})
          }


          // if(this.printerList && this.printerList.length>0) this.form.printer = this.printerList[0]
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedJd = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.detailinfo.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动京东云打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
      };
    },
    openWsTao() {
      const ws = new WebSocket('ws://127.0.0.1:13528');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          requestID: '12345',
          cmd: 'getPrinters',
          "version": "1.0"
        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.cmd === 'getPrinters') {
          this.printerList = resp.printers
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedTao = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动菜鸟云打印组件！");
        console.error('WebSocket error:', error);
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
        // alert('WebSocket error occurred. Check the console for more details.');
      };
    },
    openWsPdd() {
      const ws = new WebSocket('ws://127.0.0.1:5000');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          requestID: '1234554',
          cmd: 'getPrinters',
          "version": "1.0"
        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.cmd === 'getPrinters') {
          this.printerList = resp.printers
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedPdd = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动拼多多打印组件！");
        console.error('WebSocket error:', error);
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
        // alert('WebSocket error occurred. Check the console for more details.');

      };
    },
    openWsDou() {
      const ws = new WebSocket('ws://127.0.0.1:13888');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          requestID: '1234554',
          cmd: 'getPrinters',
          "version": "1.0"
        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.cmd === 'getPrinters') {
          this.printerList = resp.printers
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedDou = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动字节打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
      };
    },
    openWsWei() {
      const ws = new WebSocket('ws://127.0.0.1:12705');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          requestID: this.getUUID(8, 16),
          command: 'getPrinterList'
        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.command === 'getPrinterList') {
          this.printerList = resp.printerList
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedWei = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.printerList);
        }
      };
      // 当发生错误时触发
      ws.onerror = function(error) {
        obj.msgError("打印组件连接失败！请安装并启动微信小店打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
      };
    },
    openWsKai() {
      const ws = new WebSocket('ws://127.0.0.1:16888/ks/printer');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          "cmd": 'getPrinters',
          "requestID": this.getUUID(8, 16),
          "version": "1.0"

        }))
      };
      let obj = this.$modal;
      const $this = this;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.cmd === 'getPrinters') {
          this.printerList = [];//resp.printers
          resp.printers.forEach(x=>{
            this.printerList.push(x)
          })
          obj.msgSuccess("打印组件连接成功！");
          this.printerOpenedKai = true
          this.printerOpenedStatusRefresh = false
          this.printerOpenedStatus = false
          console.log('打印机列表: ', resp.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function(error) {
        obj.msgError("打印组件连接失败！请安装并启动快手打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
        setTimeout(function() {
          $this.printerOpenedStatusRefresh = false
        }, 1000);
      };
    },
    refreshPrinterStatus(){
      this.printerOpenedStatusRefresh = true
      if(this.shopType===100){
        // tao
        this.openWsTao()
      }else if(this.shopType===200){
        // jd
        this.openWsJd()
      }else if(this.shopType===300){
        // pdd
        this.openWsPdd()
      }else if(this.shopType===400){
        // dou
        this.openWsDou()
      }else if(this.shopType===500){
        // wei
        this.openWsWei()
      }else if(this.shopType===600){
        // kai
        this.openWsKai()
      }else {
        //
        this.$modal.msgError("暂时不支持")
        this.printerOpenedStatusRefresh = false
      }
    },
    // 取号弹窗
    handleGetEwaybillCode() {
      const ids = this.ids;
      if (!this.queryParams.shopId) {
        this.$modal.msgError("请选择店铺")
        return
      }
      if (!ids || ids.length === 0) {
        this.$modal.msgError("请选择订单")
        return;
      } else if (ids.length > 5) {
        this.$modal.msgError("同时最多只能选择5个订单")
        return;
      }
      if (!this.form.accountId) {
        this.$modal.msgError("请选择电子面单账户")
        return;
      }
      const deliver = this.deliverList.find(x=>x.id===this.form.accountId)
      console.log('====1=1111==',deliver)
      if(!deliver) {
        this.$modal.msgError("请选择电子面单账户")
        return;
      }
      if(deliver.shopType===100&&this.printerOpenedTao===false){
        console.log('====tao====')
        this.$modal.msgError("请启动菜鸟云打印组件")
        this.printerOpenedStatus = true
        return;
      } else if(deliver.shopType===200&&this.printerOpenedJd===false){
        console.log('====jd====')
        // this.openWsJd()
        this.$modal.msgError("请启动JD打印组件")
        this.printerOpenedStatus = true
        return;
      }else if(deliver.shopType===300&&this.printerOpenedPdd===false){
        console.log('====pdd====')
        this.$modal.msgError("请启动拼多多商家打印组件")
        this.printerOpenedStatus = true
        return;
      }else if(deliver.shopType===400&&this.printerOpenedDou===false){
        console.log('====dou====')
        this.$modal.msgError("请启动抖店云打印组件")
        this.printerOpenedStatus = true
        return;
      }else if(deliver.shopType===500&&this.printerOpenedWei===false){
        console.log('====wei====')
        this.$modal.msgError("请启动微信小店打印组件")
        this.printerOpenedStatus = true
        return;
      }else if(deliver.shopType===600&&this.printerOpenedKai===false){
        console.log('====wei====')
        this.$modal.msgError("请启动快手小店打印组件")
        this.printerOpenedStatus = true
        return;
      }else if(deliver.shopType===700&&this.printerOpenedXhs===false){
        console.log('====wei====')
        this.$modal.msgError("请启动小红书打印组件")
        this.printerOpenedStatus = true
        return;
      }


      if (ids && ids.length > 0 && this.queryParams.shopId) {
        console.log("===========取号并弹出结果=========")
        this.pageLoading = true
        getWaybillCodeDiansan({
          shopId: this.queryParams.shopId,
          ids: ids,
          accountId: this.form.accountId
        }).then(response => {
          this.$modal.msgSuccess("取号成功")
          this.shipWaybillList = response.data.list
          this.pageLoading = false
          this.getCodeOpen = true
        });
      }
    },
    handlePrintEwaybill(isSend) {
      const ids = this.ids;
      console.log('====ids====',ids)
      if (!ids || ids.length ===0 ) {
        this.$modal.msgError('请选择订单！');
        return;
      }else if(ids.length>5){
        this.$modal.msgWarning('最多支持5个订单多选！');
        return;
      }
      if (!this.form.printer) {
        this.$modal.msgError('请选择打印机！');
        return
      }

      getWaybillPrintDataDiansan({shopId: this.queryParams.shopId, ids: ids}).then(response => {
        if (response.code===200) {
          console.log("======开始打印======", response.data)
          axios.request({
            url: '//d3.diansan.com/app-web/open/router/rest.json?method=omni.print.open.appOpenPrintData.getExpressList&appKey='+response.data.appKey+'&timestamp='+response.data.timestamp+'&sign='+response.data.sign,
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8;'
            },
            data: JSON.stringify({
              'request': { 'waybillCodes': response.data.waybillCodes }
            })
          }).then(resp => {
            console.log("======获取打印数据：",resp.data);  // 打印响应数据
            if(resp.data.code === 200) {
              if (this.shopType === 200) {
                // jd 打印开始
                const ws = new WebSocket('ws://127.0.0.1:9113');
                ws.onopen = () => {
                  let printData = []
                  resp.data.data.forEach(x => printData.push(x.encryptedData))
                  console.log('开始打印: 组合打印数据：', printData);
                  // 打印
                  ws.send(JSON.stringify(
                    {
                      "orderType": "PRINT",
                      "key": this.getUUID(8, 16),
                      "parameters": {
                        "printName": this.form.printer,
                        "offsetTop": "0mm",
                        "offsetLeft": "0mm",
                        "tempUrl": "https://template-content.jd.com/template-oss?tempCode=ytokd76x130",
                        "printData": printData,
                        // "customTempUrl": "https://template-content.jd.com/template-open?templateCode=customer002",
                        "customData": [{
                          // "name": "123242332",
                          // "phone": "1111111",
                          "addData": {
                            // "sender": {
                            //   "name": "仓库1",
                            //   "mobile": "12345678901",
                            //   "phone": "12345678901",
                            //   "address": "上海市xxxx"
                            // }
                          }
                        }],
                        "addData": {
                          // "sender": {
                          //   "name": "统一替换场景........",
                          //   "mobile": "12345678901",
                          //   "phone": "12345678901",
                          //   "address": "北京市xxxxxxxx"
                          // }
                        }
                      }
                    }
                  ))
                };
                let obj = this.$modal;
                ws.onmessage = (e) => {
                  const resp = JSON.parse(e.data || '{}')
                  if (resp.cmd === 'print') {
                    console.log('打印结果: ', resp);
                    obj.msgSuccess("打印成功！" + JSON.stringify(resp));
                    // 请求回调
                    return pushWaybillPrintSuccess({shopId: this.queryParams.shopId, ids: ids})
                  }
                };


                // 当发生错误时触发
                ws.onerror = function (error) {
                  obj.msgError("打印失败！");
                  console.error('WebSocket error:', error);
                  // alert('WebSocket error occurred. Check the console for more details.');
                };
                // jd 打印结束
              }else{
                this.$modal.msgError("暂时不支持")
              }

            }
            // 开始发货

          })
            .catch(error => {
              console.error('Request failed:', error);  // 如果请求失败，打印错误
            });
        }
      });
      pushShipSendDiansan({shopId: this.queryParams.shopId, ids: ids}).then(response => {
        if(response.code===200){
          this.$modal.msgSuccess("发货成功！"+JSON.stringify(response.data));
          this.getList()
        }else{
          this.$modal.msgError("发货失败："+response.msg)
        }
      })

    },
    handleCancelWaybill(row){
      console.log("=====取消取号==",row)
      cancelWaybillCodeDiansan({orderId:row.id}).then(response => {
        this.$modal.msgSuccess("成功")
      });
    },
    handleShipSend(row){
      if(row){
        this.ids = []
        this.ids.push(row.id)
      }
      const ids = this.ids
      console.log('======',ids)
      pushShipSendDiansan({shopId: this.queryParams.shopId, ids: ids}).then(response => {
        if(response.code===200){
          this.$modal.msgSuccess("发货成功！"+JSON.stringify(response.data));
          this.getList()
        }else{
          this.$modal.msgError("发货失败："+response.msg)
        }

      })
    },
    getUUID(len, radix) {
      var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
      var uuid = [], i;
      radix = radix || chars.length;
      if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
      } else {
        var r;
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';
        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | Math.random() * 16;
            uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
          }
        }
      }
      return uuid.join('');
    }
  }
};
</script>
