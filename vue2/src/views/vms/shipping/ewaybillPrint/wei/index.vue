<template>
  <div class="app-container">
    <el-form :model="printParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

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

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-time"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleGetEwaybillCode"-->
<!--        >电子面单取号&发货</el-button>-->
<!--      </el-col>-->

      <el-col :span="1.5">

        <el-button
          type="success"
          plain
          :disabled="multiple"
          icon="el-icon-printer"
          size="mini"
          @click="handlePrintEwaybill"
        >电子面单打印</el-button>
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
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="180">
        <template slot-scope="scope">
          <span>{{scope.row.orderNum}}</span>
          <el-tag>{{ parseTime(scope.row.orderTime) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品明细" align="center" width="900px" >
        <template slot="header">
          <table>
            <th>
              <td width="50px">图片</td>
              <td width="250px" align="left">标题</td>
              <td width="150" align="left">SKU名</td>
              <td width="200" align="left">Sku编码</td>
              <!--              <td width="150" align="left">平台SkuId</td>-->
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
            <el-table-column label="商品名" align="left" width="250px" prop="goodsName" >
              <template slot-scope="scope">
                {{scope.row.goodsName}}
                <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
                <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
                <el-tag size="small" v-if="scope.row.refundStatus === 11">已取消</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="SKU名" align="left" prop="skuName" width="150"  :show-overflow-tooltip="true">
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.skuName)}}
              </template>
            </el-table-column>
            <el-table-column label="Sku编码" align="left" prop="skuCode" width="200"/>
            <!--            <el-table-column label="平台SkuId" align="left" prop="skuId" width="150"/>-->
            <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
              <template slot-scope="scope">
                <el-tag size="small" type="danger">{{scope.row.quantity}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
<!--      <el-table-column label="下单时间" align="center" prop="orderCreateTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.orderTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="buyerMessage" >
        <template slot-scope="scope">
          <span v-if="scope.row.buyerMessage">买家留言:{{ scope.row.buyerMessage }}</span>
          <span v-if="scope.row.sellerMemo">卖家备注:{{ scope.row.sellerMemo }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="收件人" prop="receiverName" width="200px">-->
<!--        <template slot-scope="scope">-->
<!--          {{scope.row.receiverName}}&nbsp;-->
<!--          {{scope.row.receiverMobile}} <br />-->
<!--          {{scope.row.province}} {{scope.row.city}} {{scope.row.town}} <br />-->

<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="面单号" align="center" prop="waybillCode" />
      <el-table-column label="快递公司" align="center" prop="waybillCompany" />
      <el-table-column label="取号状态" align="center" prop="waybillStatus" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.waybillStatus==0">未取号</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==1">已取号</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==2">已打印</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==3">已发货</el-tag>
          <el-tag size="small" v-if="scope.row.waybillStatus==10">手动发货</el-tag>
          <br />
<!--          <el-button type="text" size="small"  @click="cancelCode(scope.row)" v-if="scope.row.waybillStatus==1 || scope.row.waybillStatus==2">取消取号</el-button>-->
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
    <el-dialog title="取号" :visible.sync="getCodeOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="电子面单账户" prop="accountId">
          <el-select v-model="form.accountId" placeholder="请选择电子面单账户" clearable>
            <el-option
              v-for="item in deliverList"
              :key="item.id"
              :label="item.deliveryId"
              :value="item.id">
              <span style="float: left">{{ item.deliveryId }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px" >{{item.siteName}}:{{item.available}}</span>
            </el-option>
          </el-select>

        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getCodeOpenForm">取号并发货</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {listOrder, getOrder,budadanListOrder } from "@/api/vms/order";
import {getWaybillAccountList,cancelWaybillCode,getWaybillPrintData, getWaybillCode, pushWaybillPrintSuccess,pushShipSend,getWaybillCodeAndSend} from "@/api/vms/ewaybill/wei";


export default {
  name: "printWei",
  data() {
    return {
      // 遮罩层
      loading: true,
      getCodeOpen: false,
      // 选中数组
      ids: [],
      shopList: [],
      orderList: [],
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
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        platformId:500,
        sendStatus: 2,
      },
      // 打印参数
      printParams: {
        deliver: null,
        printer: null
      },
      // 表单参数
      form: {},
      printerList: [],
      deliverList: [],
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.openWs()
    this.getList();
  },
  methods: {
    getSkuValues(spec){
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || [];

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value).join(', ') || '';
      } catch (error) {
        return spec; // 如果 JSON 解析出错，返回空字符串
      }
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      budadanListOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancelCode(row){
        console.log("=====取消取号==",row)
      cancelWaybillCode(row).then(response => {
        this.$modal.msgSuccess("成功")
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 取号弹窗
    handleGetEwaybillCode() {
      const ids = this.ids;
      if (ids) {
        getWaybillAccountList({shopId: this.queryParams.shopId}).then(response => {
          this.deliverList = response.data;
          if(response.data&&response.data.length >0){
            this.form.accountId = response.data[0].id
          }

          this.getCodeOpen = true
        });
      } else {
        this.$modal.msgError("请选择订单")
      }
    },

    /** 取号提交按钮 */
    getCodeOpenForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const ids = this.ids;
          console.log('=========3333========', ids)
          if (ids) {
            console.log('===请求参数=====', {shopId: this.queryParams.shopId, ids: ids, accountId: this.form.accountId})
            getWaybillCodeAndSend({
              shopId: this.queryParams.shopId,
              ids: ids,
              accountId: this.form.accountId
            }).then(response => {
              this.$modal.msgSuccess("取号成功")
              this.getList()
              this.getCodeOpen = false
            });
          } else {
            this.$modal.msgError("请选择订单")
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
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
    handleLink(row) {
      this.reset();
      const id = row.id || this.ids
      getGoodsSku(id).then(response => {
        console.log('=====00000000============',response)
        this.form = response.data;
        this.open = true;
      });
    },
    openWs() {
      const ws = new WebSocket('ws://127.0.0.1:12705');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          requestID: '12345',
          command: 'getPrinterList'
        }))
      };
      let obj = this.$modal;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        if (resp.command === 'getPrinterList') {
          this.printerList = resp.printerList
          obj.msgSuccess("打印组件连接成功！");
          console.log('打印机列表: ', resp.printerList);
        }
      };
      // 当发生错误时触发
      ws.onerror = function(error) {
        obj.msgError("打印组件连接失败！请安装并启动微信视频号小店打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
      };
    },
    handlePrintEwaybill() {
      if (!this.printParams.printer) {
        this.$modal.msgError('请选择打印机！');
        return
      }
      getWaybillPrintData({shopId: this.queryParams.shopId, ids: this.ids}).then(response => {
        console.log("======打印======", response.data)
        if (response.data && response.data.length>0) {
          const ws = new WebSocket('ws://127.0.0.1:12705');

          ws.onopen = () => {
            console.log('开始打印: ');
            // let printData = []
            // response.data.forEach(x => printData.push(x.printData))
            // 打印
            // https://mmec-shop-1258344707.cos.ap-shanghai.myqcloud.com/shop/public/2023-11-08/ad8e4daf-0e41-4a01-ad44-8db2c419e3f3.html
            response.data.forEach(x => {
              console.log('开始打印: ',x);
              ws.send(JSON.stringify({
                command: 'print',
                version: '2.0', // 必传
                requestID: this.getUUID(8, 16), // String, 调用方保证唯一
                taskList: [{
                  taskID: this.getUUID(8, 10),
                  // printInfo: 'JTdCJTIycHJpbnREYXRhJTIyJTNBJTdCJTIyd2F5YmlsbElkJTIyJTNBJTIyNzM2MTE0NjI1MzgzODUlMjIlMkMlMjJwcmludFRpbWUlMjIlM0ElMjIyMDI0JTJGMDYlMkYwMyUyMDE4JTNBNDUlMjIlMkMlMjJzZXJ2aWNlcyUyMiUzQSU1QiU1RCUyQyUyMnJlY2VpdmVyTmFtZSUyMiUzQSUyMiVFNSVBRSVBMyoqJTIyJTJDJTIycmVjZWl2ZXJQaG9uZSUyMiUzQSUyMjEzNyoqKiozODQwJTIyJTJDJTIycmVjZWl2ZXJBZGRyZXNzJTIyJTNBJTIyJUU0JUI4JThBJUU2JUI1JUI3JUU1JUI4JTgyJUU0JUI4JThBJUU2JUI1JUI3JUU1JUI4JTgyJUU2JUI1JUE2JUU0JUI4JTlDJUU2JTk2JUIwJUU1JThDJUJBJUU1JUJDJUEwJUU2JUIxJTlGJUU5JTk1JTg3JUU1JUFEJTk5JUU2JUExJUE1JUU4JUI3JUFGMjM4JUU1JUJDJTg0MzAlRTUlOEYlQjcyMDIlRTUlQUUlQTQlMjIlMkMlMjJzZW5kZXJOYW1lJTIyJTNBJTIyJUU0JUI4JTgzJUU5JTg3JThDJUU1JTlEJUFBJTIyJTJDJTIyc2VuZGVyUGhvbmUlMjIlM0ElMjIxNTgxODU5MDExOSUyMiUyQyUyMnNlbmRlckFkZHJlc3MlMjIlM0ElMjIlRTUlQjklQkYlRTQlQjglOUMlRTclOUMlODElRTYlQjclQjElRTUlOUMlQjMlRTUlQjglODIlRTUlQUUlOUQlRTUlQUUlODklRTUlOEMlQkF4eHh4eDElRTUlOEYlQjclRTUlOEMlOTclRTklOTclQTglMjIlMkMlMjJzaXRlQ29kZSUyMiUzQSUyMjU1ODMwJTIyJTJDJTIyZXdheWJpbGxPcmRlcklkJTIyJTNBJTIyMzQ4NjYxMzA5Mzg5ODE0MTY5OSUyMiUyQyUyMmJhZ0FkZHIlMjIlM0ElMjIlRTYlQjIlQUElRTQlQjglOUMlMjIlMkMlMjJtYXJrJTIyJTNBJTIyMzEwLSUyMFA2JTIwMDMxJTIwJTVCQjMxJTVEJTIyJTJDJTIyc3RvcmVOYW1lJTIyJTNBJTIyJUU5JUFBJTg0JUU5JUI5JUJGJUU2JTlDJTlCJUU1JUIxJUIxJUU0JUI4JTkzJUU1JThEJTk2JUU1JUJBJTk3JTIyJTJDJTIyY3VzdG9tZXJOb3RlcyUyMiUzQSUyMiUyMiUyQyUyMm1lcmNoYW50Tm90ZXMlMjIlM0ElMjIlMjIlMkMlMjJvcmRlcklkJTIyJTNBJTIyMzcyMDI5MzQxNTUwOTk1NDgxNiUyMiU3RCUyQyUyMnRlbXBsYXRlJTIyJTNBJTdCJTIydGVtcGxhdGVJZCUyMiUzQSUyMnNpbmdsZSUyMiUyQyUyMnRlbXBsYXRlTmFtZSUyMiUzQSUyMiVFOSVCQiU5OCVFOCVBRSVBNCVFNyVBOSVCQSVFNiVBOCVBMSVFNiU5RCVCRiUyMiUyQyUyMnRlbXBsYXRlRGVzYyUyMiUzQSUyMiVFNCVCOCU4MCVFOCU4MSU5NCVFNSU4RCU5NSVFNiVBMCU4NyVFNSU4NyU4NiVFNiVBOCVBMSVFNiU5RCVCRiUyMiUyQyUyMnRlbXBsYXRlVHlwZSUyMiUzQSUyMnNpbmdsZSUyMiUyQyUyMm9wdGlvbkxpc3QlMjIlM0ElN0IlN0QlMkMlMjJvcHRpb25zJTIyJTNBJTVCJTVEJTJDJTIyY29kZSUyMiUzQTAlMkMlMjJkZWxpdmVyeUlkJTIyJTNBJTIyWlRPJTIyJTJDJTIydGVtcGxhdGVVcmwlMjIlM0ElMjJodHRwcyUzQSUyRiUyRm1tZWMtc2hvcC0xMjU4MzQ0NzA3LmNvcy5hcC1zaGFuZ2hhaS5teXFjbG91ZC5jb20lMkZzaG9wJTJGcHVibGljJTJGMjAyMy0xMC0yNSUyRjNlY2JiM2FhLTViY2YtNDA0ZC05NzJhLThhMDhhODE2MjIzYy5odG1sJTIyJTJDJTIyY3VzdG9tQ29uZmlnJTIyJTNBJTdCJTIyd2lkdGglMjIlM0E2NTYlMkMlMjJoZWlnaHQlMjIlM0EzMDAlMkMlMjJsZWZ0JTIyJTNBNjAlMkMlMjJ0b3AlMjIlM0E5MzAlN0QlMkMlMjJ3aWR0aCUyMiUzQTc2JTJDJTIyaGVpZ2h0JTIyJTNBMTMwJTdEJTdE', // String, [获取打印报文]接口返回的print_info
                  printInfo: x.printData,
                  // printNum: {
                  //   curNum: 1, // 打印计数-当前张数
                  //   sumNum: 2, // 打印计数-总张数
                  // },
                  splitControl: 0,// 可不传， 默认为0， 根据自定义内容自动分页；1，禁止分页；2；强制分页， 内容打印在第二页
                  showDeliveryLogo: 0, // 可不传， 默认为1， 传0时不展示快递公司logo
                  // 自定义模板信息，没有自定义模板需求可不传
                  // customInfo: {
                  //   templateUrl: 'https://mmec-shop-1258344707.cos.ap-shanghai.myqcloud.com/shop/public/2023-11-10/a80c0110-3fb8-4190-bdcc-10124b7dd0ce.html',//'https://mmec-shop-1258344707.cos.ap-shanghai.myqcloud.com/shop/public/2023-11-08/ad8e4daf-0e41-4a01-ad44-8db2c419e3f3.html', // 自定义模板url
                  //   // 模板里面定义的数据字段
                  //   // 模板里使用{{{}}}，数据可以通过<br>换行
                  //   data: {
                  //     shopName: "测试小店",
                  //     productInfo: [
                  //       {name: "商品1", count: 1, code: "asd", description: "商品描述"},
                  //       // {name: "商品2", count: 2, code: "zxc123", description: "商品<br>描述"}
                  //     ],
                  //     imgSrc: "https://www.tencent.com/img/index/tencent_logo.png",
                  //     productQRcode: "https://www.tencent.com/zh-cn/",
                  //     productBarcode: "product1"
                  //   }
                  // },
                  // 面单补充信息，用来覆盖寄件人信息，没有这种需求可以不传

                }],
                printType: 1, // Number 打印类型，默认为 1，打印固定高度的面单；如果为2，则打印任意自定义内容，需要传递 size 参数指定纸张尺寸，printInfo 改为传递 base64 格式的 html
                size: {
                  width: 76, // 纸张尺寸，单位毫米，printType 为 2 时必传
                  height: 130
                },
                printer: this.printParams.printer, // 选中的打印机，printer.name
              }))
            })
          };


          let obj = this.$modal;
          ws.onmessage = (e) => {
            const resp = JSON.parse(e.data || '{}')
            if (resp.command === 'print') {
              console.log('打印结果: ', resp);
              // 请求回调
               pushWaybillPrintSuccess({shopId: this.queryParams.shopId, ids: this.ids}).then(resp=>{
                 this.getList()
               })
              // obj.msgError("打印结果！"+JSON.stringify(resp));
            }
          };


          // 当发生错误时触发
          ws.onerror = function (error) {
            obj.msgError("打印失败！");
            console.error('WebSocket error:', error);
            // alert('WebSocket error occurred. Check the console for more details.');
          };
        }else{
          this.$modal.msgError('没有电子面单信息！');
        }
      });
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
    },
    handleShipSend(){
      pushShipSend({shopId: this.queryParams.shopId, ids: this.ids}).then(response => {
        this.$modal.msgSuccess("发货成功！");
        this.getList()
      })
    },
  }
};
</script>
