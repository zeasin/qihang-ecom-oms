<template>
  <div class="app-container" element-loading-text="取号中" v-loading="pageLoading">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="订单号" prop="orderId">
          <el-input
            v-model="queryParams.orderId"
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
          <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="shopChange">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="店铺" prop="shopId">-->
<!--          <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">-->
<!--            <el-option-->
<!--              v-for="item in shopList"-->
<!--              :key="item.id"-->
<!--              :label="item.name"-->
<!--              :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-select v-model="printParams.printer" placeholder="请选择打印机" clearable>
            <el-option
              v-for="item in printerList"
              :key="item.name"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>

        </el-form-item>
      </el-form>

    </el-row>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-select size="mini" v-model="printParams.printer" placeholder="请选择打印机" clearable>
          <el-option
            v-for="item in printerList"
            :key="item.name"
            :label="item.name"
            :value="item.name">
          </el-option>
        </el-select>

      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-time"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleGetEwaybillCode"-->
      <!--        >新单号打印</el-button>-->
      <!--      </el-col>-->

      <el-col :span="1.5">

        <el-button
          type="success"
          plain
          :disabled="multiple"
          icon="el-icon-printer"
          size="mini"
          @click="handlePrintEwaybill"
        >原单号打印</el-button>
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

      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handlePrintEwaybill"-->
      <!--        >打印电子面单</el-button>-->
      <!--      </el-col>-->


      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" :selectable="selectable" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >{{scope.row.orderNum}} </el-button><br/>
          <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>>
      </el-table-column>
      <el-table-column label="商品明细" align="center" width="760px" >
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
            <el-table-column label="商品标题" align="left" width="250px" prop="goodsTitle" >
              <template slot-scope="scope">
                {{scope.row.goodsTitle}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="规格" align="left" prop="goodsSpec" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <div>{{getSkuValues(scope.row.goodsSpec)}}&nbsp;<br/>
                  <el-tag size="small" type="info" v-if="scope.row.skuNum">{{scope.row.skuNum}}</el-tag>
                </div>
                <!--                {{ getSkuValues(scope.row.goodsSpec)}}-->
                <!--                {{scope.row.goodsSkuId}}-->
              </template>
            </el-table-column>
            <el-table-column label="规格ID" align="left" prop="skuNum" width="200">
              <template slot-scope="scope">
                <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
                  <div style="margin-left:10px">
                    <div>
                      <span style="color: #5a5e66;font-size: 11px">平台ID：</span>
                      {{scope.row.skuId}}&nbsp
                    </div>
                    <div>
                      <span style="color: #5a5e66;font-size: 11px">规格ID：</span>
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
      <el-table-column label="订单备注" align="center" >
        <template slot-scope="scope">
          <div style="color: #ed5565">{{scope.row.remark}}</div>
          <div style="color: #ed5565">{{scope.row.buyerMemo}}</div>
          <div style="color: #ed5565">{{scope.row.sellerMemo}}</div>
        </template>
      </el-table-column>
      <el-table-column label="面单号" align="center" prop="waybillCode" />
      <el-table-column label="快递公司" align="center" prop="waybillCompany" />
<!--      <el-table-column label="面单号" align="center" prop="waybillCode" />-->
<!--      <el-table-column label="取单状态" align="center" prop="waybillStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus==0">未取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus==1">已取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus==2">已打印</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus==3">已发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus==10">手动发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
      <el-table-column label="发货状态" align="center" prop="shipStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shipStatus === 0" style="margin-bottom: 6px;">待发货</el-tag>
          <el-tag v-if="scope.row.shipStatus === 1" style="margin-bottom: 6px;">部分发货</el-tag>
          <el-tag v-if="scope.row.shipStatus === 2" style="margin-bottom: 6px;">全部发货</el-tag>

<!--          <el-col :span="24">-->
<!--            <el-tag v-if="scope.row.distStatus === 0" type="info" style="margin-bottom: 6px;">未推送三方发货</el-tag>-->
<!--            <el-tag v-if="scope.row.distStatus === 1" type="info"  style="margin-bottom: 6px;">部分推送三方发货</el-tag>-->
<!--            <el-tag v-if="scope.row.distStatus === 2" type="info"  style="margin-bottom: 6px;">全部推送三方发货</el-tag>-->
<!--          </el-col>-->
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
    <el-dialog title="打印快递单" :visible.sync="getCodeOpen" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
<!--        <el-form-item label="电子面单账户" prop="accountId">-->
<!--            <el-select v-model="form.accountId" placeholder="请选择电子面单账户" clearable>-->
<!--              <el-option-->
<!--                v-for="item in deliverList"-->
<!--                :key="item.id"-->
<!--                :label="item.company"-->
<!--                :value="item.id">-->
<!--                <span style="float: left">{{ item.company }}</span>-->
<!--                <span style="float: right; color: #8492a6; font-size: 13px" >{{item.netsiteName}}:{{item.amount}}</span>-->
<!--              </el-option>-->
<!--            </el-select>-->
<!--          <el-button type="success" plain @click="updateWaybillAccount" >更新电子面单账户信息</el-button>-->
<!--        </el-form-item>-->
        <el-form-item label="电子面单账户" prop="printer">
          <el-select v-model="printParams.printer" placeholder="请选择打印机" clearable>
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
<!--        <el-button type="primary" @click="getCodeOpenForm">确 定</el-button>-->
        <el-button type="primary" size="small" @click="handlePrintEwaybill(false)">开始打印</el-button>
        <el-button type="primary" size="small" @click="handlePrintEwaybill(true)">打印并发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

import {listShop} from "@/api/shop/shop";
import {listOrder, getOrder,selfShippedList } from "@/api/order/order";
import {
  getWaybillAccountList,
  pullWaybillAccount,
  getWaybillCode,
  pushShipSend
} from "@/api/dou/ewaybill";
import {listMerchant} from "@/api/shop/merchant";

export default {
  name: "ShippedList",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      shopList: [],
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
      pageLoading: false,
      isMerchant: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundStatus: 1,
        shopType:400,
        orderStatus: '1',
        shopId: null
      },
      // 打印参数
      printParams: {
        deliver: null,
        printer: null
      },
      // 表单参数
      form: {},
      orderList: [],
      printerList: [],
      deliverList: [],
      shipWaybillList: [],
      merchantList: [],
      // 表单校验
      rules: {
        accountId: [{ required: true, message: '请选择电子面单账户' }],
        printer: [{ required: true, message: '请选择打印机' }],
      }
    };
  },
  created() {
    this.openWs()
    listMerchant({pageNum: 1, pageSize: 1000}).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if (resp.rows.length === 1 && resp.rows[0].id > 0) {
        this.isMerchant = true;
      }
      listShop({merchantId: this.queryParams.merchantId,type:400}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        if(this.shopList&&this.shopList.length>0){
          this.queryParams.shopId = this.shopList[0].id
        }
        this.getList()
      })
    });
    // listShop({type: this.queryParams.shopType}).then(response => {
    //   this.shopList = response.rows;
    //   // if (this.shopList && this.shopList.length > 0) {
    //   //   this.queryParams.shopId = this.shopList[0].id
    //   // }
    //   getWaybillAccountList({shopId: this.queryParams.shopId}).then(response => {
    //     this.deliverList = response.data;
    //     this.getList();
    //   });
    //
    // });
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.deliverList = []
      this.form.accountId = null
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:400}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        this.handleQuery()
      });
    },
    shopChange(nv){
      this.handleQuery()
    },
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    selectable(row) {
      // 根据 `isDisabled` 字段来禁用复选框
      // if(!row.waybillStatus && row.waybillStatus ===0){
      //   return true
      // }else return false
      return true
    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      selfShippedList(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.getCodeOpen = false;
      this.reset();
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
      this.ids = selection.map(item => item.orderNum)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    openWs() {
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
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.cmd === 'getPrinters') {
          this.printerList = resp.printers
          obj.msgSuccess("打印组件连接成功！");
          console.log('打印机列表: ', resp.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动字节打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
      };
    },
    // 更新电子面单信息
    updateWaybillAccount() {
      pullWaybillAccount({shopId: this.queryParams.shopId}).then(response => {
        this.deliverList = response.data;
      });
    },
    /** 取号提交按钮 */
    getCodeOpenForm() {
        const ids = this.ids;
        console.log('=========3333========', ids)
        if (ids) {
          console.log('===请求参数=====', {shopId: this.queryParams.shopId, ids: ids, accountId: this.form.accountId})
          if (!this.form.accountId) {
            this.$modal.msgError("请选择电子面单账户")
            return
          }
          getWaybillCode({
            shopId: this.queryParams.shopId,
            ids: ids,
            accountId: this.form.accountId
          }).then(response => {
            this.$modal.msgSuccess("取号成功")
            this.getCodeOpen = true
            this.pageLoading = false
            this.shipWaybillList = response.data.list
            // this.getList()
            // this.getCodeOpen = false
          });
        } else {
          this.$modal.msgError("请选择订单")
        }


    },
    handlePrintEwaybill(isSend) {
      // if (!this.ws) {
      //   this.$modal.msgError('打印组件连接失败！请安装并启动微信视频号小单打印组件！');
      //   this.openWs()
      // }
      // if(!this.printParams.deliver){
      //   this.$modal.msgError('请选择快递公司！');
      //   return
      // }
      if (!this.printParams.printer) {
        this.$modal.msgError('请选择打印机！');
        return
      }

      getWaybillPrintData({shopId: this.queryParams.shopId, ids: this.ids}).then(response => {
        let printData = []
        if(response.data && response.data.length>0){
          response.data.forEach(x => {
            console.log('开始打印: 组合打印数据：', x);
            // 组合打印数据
            let document ={
              "documentID": x.waybillCode,
              "contents": [{
                "params": x.params,
                "signature":x.sign,
                "encryptedData":x.printData,//实体打印机打印后是明文，虚拟打印机打印后是收件人脱敏信息
                "templateURL":x.templateUrl,
                //密文中有发件人信息，修改发件人信息，可选
                "addData":{},
                "config":{
                  "packageNumber": "1/3"//包裹号
                }
              }, {
                "data": {
                  "detail": x.goodsDetail,
                },
                "templateURL":"https://cloudprint.douyinec.com/api/logistics/davinci/template/xml/D1l4jpyxn4gf8k"
              }
              ]
            }
            printData.push(document)
          })
        }else{
          this.$modal.msgError('没有要打印的数据！');
          return
        }
        console.log('开始打印: 组合打印数据：', printData);
        const ws = new WebSocket('ws://127.0.0.1:13888');
        ws.onopen = () => {
            // 打印
            ws.send(JSON.stringify({
              "cmd": "print",
              "requestID": this.getUUID(8, 16),
              "version": "1.0",
              "task": {
                "taskID": this.getUUID(8, 10),
                "preview": false,
                "printer": this.printParams.printer,
                "previewType": "pdf",
                "firstDocumentNumber": 10,
                "totalDocumentCount": 100,
                "documents": printData
              }
            }))

        };
        let obj = this.$modal;
        ws.onmessage = (e) => {
          const resp = JSON.parse(e.data || '{}')
          if (resp.cmd === 'print') {
            console.log('打印结果: ', resp);
            obj.msgSuccess("打印成功！" + JSON.stringify(resp));
            // 请求回调
            if (isSend) {
              console.log('=====打印成功=====请求发货====', this.ids)
              return pushShipSend({shopId: this.queryParams.shopId, ids: this.ids})
            }
          }
        };


        // 当发生错误时触发
        ws.onerror = function (error) {
          obj.msgError("打印失败！");
          console.error('WebSocket error:', error);
          // alert('WebSocket error occurred. Check the console for more details.');
        };
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
