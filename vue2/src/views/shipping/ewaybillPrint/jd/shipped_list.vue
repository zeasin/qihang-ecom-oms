<template>
  <div class="app-container">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="订单号" prop="tid">
          <el-input
            v-model="queryParams.tid"
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
<!--        <el-form-item>-->
<!--          <el-select v-model="form.printer" placeholder="请选择打印机" clearable>-->
<!--            <el-option-->
<!--              v-for="item in printerList"-->
<!--              :key="item.name"-->
<!--              :label="item.name"-->
<!--              :value="item.name">-->
<!--            </el-option>-->
<!--          </el-select>-->

<!--        </el-form-item>-->
      </el-form>

    </el-row>


    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-time"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleGetEwaybillCode"-->
<!--        >电子面单取号</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-select size="mini" v-model="form.printer" placeholder="请选择打印机" clearable>
          <el-option
            v-for="item in printerList"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-printer"
          size="mini"
          @click="handlePrintEwaybill"
        >原单号打印</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
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
      <el-table-column label="面单号" align="center" prop="waybillCode" />
      <el-table-column label="快递公司" align="center" prop="waybillCompany" />
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

  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {selfShippedList } from "@/api/order/order";
import {listShop} from "@/api/shop/shop";
import {
  getWaybillPrintData,
  pushWaybillPrintSuccess
} from "@/api/jd/ewaybill";
import {getUUID} from "@/utils/zhijian";
import {listMerchant} from "@/api/shop/merchant";

export default {
  name: "printJd",
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundStatus: 1,
        shopType:null,
        orderStatus: '1',
        shopId: null
      },
      // 表单参数
      form: {
        printer: null
      },
      orderList: [],
      printerList: [],
      deliverList: [],
      // 表单校验
      rules: {
        accountId: [{ required: true, message: '请选择电子面单账户' }],
      }
    };
  },
  created() {
    this.openWs()
    console.log('==shoptype:',this.shopType)
    this.queryParams.shopType = this.shopType
    listMerchant({pageNum: 1, pageSize: 1000}).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if (resp.rows.length === 1 && resp.rows[0].id > 0) {
        this.isMerchant = true;
      }
      listShop({merchantId: this.queryParams.merchantId,type:this.shopType}).then(response => {
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
    //   if (this.shopList && this.shopList.length > 0) {
    //     this.queryParams.shopId = this.shopList[0].id
    //   }
    //   this.getList();
    // });
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.deliverList = []
      this.form.accountId = null
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.shopType}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        this.handleQuery()
      });
    },
    shopChange(nv){
      this.handleQuery()
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
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    openWs() {
      const ws = new WebSocket('ws://127.0.0.1:9113');
      ws.onopen = () => {
        console.log('与打印组件建立连接成功: ');
        // 或打印机
        ws.send(JSON.stringify({
          "orderType":"GET_Printers"
        }))
      };
      let obj = this.$modal;
      ws.onmessage = (e) => {
        const resp = JSON.parse(e.data || '{}')
        console.log("====接收到命令========",e)
        const arr = ["Fax", "OneNote for Windows 10", "Microsoft XPS Document Writer", "Microsoft Print to PDF", "Adobe PDF"];
        if (resp.code === '6') {
          this.printerList = resp.detailinfo.printers.filter(x=> !arr.includes(x))
          if(this.printerList && this.printerList.length>0) this.form.printer = this.printerList[0]
          obj.msgSuccess("打印组件连接成功！");
          console.log('打印机列表: ', resp.detailinfo.printers);
        }
      };
      // 当发生错误时触发
      ws.onerror = function (error) {
        obj.msgError("打印组件连接失败！请安装并启动京东云打印组件！");
        console.error('WebSocket error:', error);
        // alert('WebSocket error occurred. Check the console for more details.');
      };
    },
    handlePrintEwaybill() {
      if (!this.form.printer) {
        this.$modal.msgError('请选择打印机！');
        return
      }
      const ids = this.ids;
      if (!ids || ids.length === 0) {
        this.$modal.msgError("请选择订单")
        return;
      } else if (ids.length > 5) {
        this.$modal.msgError("同时最多只能选择5个订单")
        return;
      }
      getWaybillPrintData({shopId: this.queryParams.shopId, ids: ids}).then(response => {
        console.log("======打印======", response.data)
        if (response.data) {
          const ws = new WebSocket('ws://127.0.0.1:9113');
          ws.onopen = () => {
            let printData = []
            response.data.forEach(x => printData.push(JSON.parse(x.printData)))
            console.log('开始打印: 组合打印数据：', printData);
            // 打印
            ws.send(JSON.stringify({
              "cmd": "print",
              "requestID": getUUID(8, 16),
              "version": "1.0",
              "task": {
                "taskID": getUUID(8,10),
                "preview": false,
                "printer": this.form.printer,
                "previewType": "pdf",
                "firstDocumentNumber": 10,
                "totalDocumentCount": 100,
                "documents": [{
                  "documentID": getUUID(8,10),
                  "contents": printData
                }]
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
              return pushWaybillPrintSuccess({shopId: this.queryParams.shopId, ids: ids})
            }
          };


          // 当发生错误时触发
          ws.onerror = function (error) {
            obj.msgError("打印失败！");
            console.error('WebSocket error:', error);
            // alert('WebSocket error occurred. Check the console for more details.');
          };
        }
      });
    },


  }
};
</script>
