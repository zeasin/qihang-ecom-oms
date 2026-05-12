<template>
  <div class="app-container" >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierId">
        <el-select v-model="queryParams.supplierId" filterable  placeholder="选择供应商" clearable @change="handleQuery">
          <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" filterable  placeholder="选择店铺" clearable @change="handleQuery">
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
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
<!--      <el-form-item label="商品ID" prop="goodsId">-->
<!--        <el-input-->
<!--          v-model="queryParams.goodsId"-->
<!--          placeholder="请输入erp系统商品id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->

<!--      <el-form-item label="规格ID" prop="specId">-->
<!--        <el-input-->
<!--          v-model="queryParams.specId"-->
<!--          placeholder="请输入erp系统商品规格id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="规格编码" prop="specNum">-->
<!--        <el-input-->
<!--          v-model="queryParams.specNum"-->
<!--          placeholder="请输入商品规格编码"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="订单状态" prop="orderStatus">-->
<!--        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">-->
<!--          <el-option label="待发货" value="1" ></el-option>-->
<!--          <el-option label="已发货" value="2"></el-option>-->
<!--          <el-option label="已完成" value="3"> </el-option>-->
<!--          <el-option label="已取消" value="11"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="发货状态" prop="sendStatus">
        <el-select v-model="queryParams.sendStatus" placeholder="请选择" @change="handleQuery">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-document-copy"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleSelection"-->
<!--        >备货完成</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--      <el-button-->
<!--        type="primary"-->
<!--        plain-->
<!--        icon="el-icon-printer"-->
<!--        size="mini"-->
<!--        :disabled="multiple"-->
<!--        @click="handleStatistics"-->
<!--      >打印备货单</el-button>-->

<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="shippingList" @selection-change="handleSelectionChange"  >
       <el-table-column type="selection" width="55"  align="center" />
<!--       <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="150">
        <template slot-scope="scope">
        <el-button
          size="mini"
          type="text"
          icon="el-icon-view"
          @click="handleDetail(scope.row)"
        >{{scope.row.orderNum}} </el-button><br/>
        <el-tag type="info">{{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
<!--       <el-table-column label="店铺" align="left" prop="shopId" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag>{{ shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name:''  }}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="发货供应商" align="left" prop="supplierId" width="200px">
        <template slot-scope="scope">
          {{supplierList.find(x=>x.id == scope.row.supplierId)?supplierList.find(x=>x.id == scope.row.supplierId).name:''}}
        </template>
      </el-table-column>
<!--       <el-table-column label="子订单编号" align="center" prop="supplierId" />-->
<!--      <el-table-column label="订单日期" align="center" prop="orderDate" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.orderDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="商品明细" align="left" width="680px">
        <template slot="header">
            <table>
              <th>
                <td width="50px">图片</td>
                <td width="250px" align="left">商品标题</td>
                <td width="150" align="left">规格</td>
                <td width="150" align="left">Sku编码</td>
<!--                <td width="150" align="left">系统SkuId</td>-->
                <td width="50" align="left">数量</td>
              </th>
            </table>
        </template>
        <template slot-scope="scope">
          <el-table :data="scope.row.itemList"  :show-header="false" style="width: 100%"  >
                  <el-table-column label="商品图片" width="50px">
                    <template slot-scope="scope">
                          <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品标题" align="left" width="250px" prop="goodsName" >
                    <template slot-scope="scope">
                    {{scope.row.goodsName}}
                    <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
                    <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
                    <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="规格" align="left" prop="skuName" width="150">
                      <template slot-scope="scope">
                        {{ getSkuValues(scope.row.skuName)}}
                      </template>
                  </el-table-column>
            <el-table-column label="Sku编码" align="left" prop="skuCode" width="150"/>
<!--                  <el-table-column label="规格编码" align="center" prop="specNum" />-->
<!--                  <el-table-column label="erp商品id" align="center" prop="goodsId" />-->
<!--                  <el-table-column label="商品SkuId" align="center" prop="specId" width="150">-->
<!--                    <template slot-scope="scope">-->
<!--                      <span style="margin-right: 15px">{{scope.row.specId}}</span>-->
<!--&lt;!&ndash;                      <a style="color:royalblue" href="javascript:void(0);"&ndash;&gt;-->
<!--&lt;!&ndash;                        v-if="!scope.row.specId||scope.row.specId === 0"&ndash;&gt;-->
<!--&lt;!&ndash;                        size="mini"&ndash;&gt;-->
<!--&lt;!&ndash;                        type="primary"&ndash;&gt;-->
<!--&lt;!&ndash;                        plain&ndash;&gt;-->
<!--&lt;!&ndash;                        @click="handleUpdateLink(scope.row)"&ndash;&gt;-->
<!--&lt;!&ndash;                      >修改商品SkuId</a>&ndash;&gt;-->
<!--                      <el-button icon="el-icon-edit" size="mini" type="text"  @click="handleUpdateLink(scope.row)"></el-button>-->

<!--                    </template>-->
<!--                  </el-table-column>-->

                   <el-table-column label="商品数量" align="center" prop="quantity" width="50px">
                     <template slot-scope="scope">
                     <el-tag size="small">{{scope.row.quantity}}</el-tag>
                     </template>
                   </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.orderTime) }}
        </template>
      </el-table-column>
      <el-table-column label="电子面单号" align="center" prop="waybillCode" >

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
        </template>
      </el-table-column>
<!--      <el-table-column label="打单状态" align="center" prop="waybillStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 0">未取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 1">已取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 2">已打印</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 3">已发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="发货状态" align="center" prop="sendStatus" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.sendStatus === 0">待备货</el-tag>
          <el-tag size="small" v-if="scope.row.sendStatus === 1">待发货</el-tag>
          <el-tag size="small" v-if="scope.row.sendStatus === 2">已发货</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" width="150">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            v-if="scope.row.status ===0 || scope.row.status === 1"-->
<!--            plain-->
<!--            type="success"-->
<!--            icon="el-icon-document-copy"-->
<!--            @click="stockupCompleteByOrder(scope.row)"-->
<!--          >确认备货完成</el-button>-->
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

    <!-- 对话框 -->

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body >
      <div id="dialogContent">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
<!--        <el-form-item label="单号" prop="stockOutNum" v-if="isGen">-->
<!--          <el-input v-model="form.stockOutNum" disabled placeholder="请输入单号" />-->
<!--        </el-form-item>-->
        <el-form-item label="完成时间" prop="completeTime" v-if="isGen">
          <el-date-picker clearable
            v-model="form.completeTime"
            type="datetime" disabled
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="打印时间" prop="completeTime" v-if="!isGen">
          <el-date-picker clearable
                          v-model="form.completeTime"
                          type="datetime" disabled
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
        <el-divider content-position="center" v-if="isGen">备货商品</el-divider>
        <el-table :data="skuList" :row-class-name="rowItemIndex" ref="skuItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品图片" prop="goodsImg" >
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="goodsTitle" ></el-table-column>
          <el-table-column label="规格" prop="goodsSpec" ></el-table-column>
          <el-table-column label="sku编码" prop="specNum" ></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <el-table-column label="仓库库存" prop="inventory"></el-table-column>

        </el-table>
      </el-form>
    </div>
      <div slot="footer" class="dialog-footer" v-if="isGen">
        <el-button type="primary" @click="submitForm">完成</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!isGen">
        <el-button v-print="'#dialogContent'">打印</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import {listVendorShipOrder } from "@/api/shipping/vendor_shipping";
import { listShop } from "@/api/shop/shop";

import {listAllSupplier} from "@/api/goods/supplier";
export default {
  name: "VendorShipOrderList",
  // computed: {
  //   supplier() {
  //     return supplier
  //   }
  // },
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 仓库订单发货表格数据
      shippingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      skuIdUpdateOpen: false,
      isGen:true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        shopId: null,
        supplierId: null,
        orderNum:null,
        sendStatus:'1',
        // orderStatus:'1'
      },
      // 表单参数
      form: {
        ids:[],
        completeTime:null
      },
      form1: {
        orderItemIds:[]
      },
      form2: {
        orderItemId:null
      },
      shopList: [],
      skuList:[],
      supplierList:[],
      statusList: [
         {
          value: '1',
          label: '待发货'
        }, {
          value: '2',
          label: '已发货'
        }
      ],
      // 表单校验
      rules: {
        stockOutNum: [{ required: true, message: "单号不能为空", trigger: "blur" }],
        completeTime: [{ required: true, message: "生成时间不能为空", trigger: "blur" }],
        goodsId: [{ required: true, message: "erp系统商品id不能为空", trigger: "blur" }],
        specId: [{ required: true, message: "erp系统商品规格id不能为空", trigger: "blur" }],
        quantity: [{ required: true, message: "商品数量不能为空", trigger: "blur" }],
      },
      rules1: {
        supplierId: [{ required: true, message: "请选择供应商", trigger: "blur" }],
      },
      rules2: {
        erpGoodsSpecId: [{ required: true, message: "请选择填写ERP商品SkuId", trigger: "blur" }],
      }
    };
  },
  mounted() {

  },
  created() {
    listAllSupplier({}).then(response => {
      this.supplierList = response.rows;
      listShop({}).then(response => {
        this.shopList = response.rows;
        this.getList();
      });

    });

    // if(this.$route.query.status){
    //   this.queryParams.status = this.$route.query.status
    // }else {
    //   this.queryParams.status = '0'
    // }
    // this.getList();
  },
  methods: {
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
    /** 查询仓库订单发货列表 */
    getList() {
      this.loading = true;
      listVendorShipOrder(this.queryParams).then(response => {
        this.shippingList = response.rows;
        this.total = response.total;

        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.skuList = []
      this.open = false;
      this.skuIdUpdateOpen = false;
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 提交按钮 */
    submitForm() {
      console.log("=============备货完成提交===",this.ids)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(!this.skuList || this.skuList.length === 0){
            this.$modal.msgError("请选择备货商品");
          }
          this.form.orderNums = this.ids;
          shipStockupCompleteByOrder(this.form).then(response => {
            this.$modal.msgSuccess("备货完成");
            this.open = false;
            this.getList();
          });

        }
      });
    },
    /** 单个备货 **/
    stockupCompleteByOrder(row) {
      this.form.orderNums = [];
      this.form.orderNums.push(row.orderNum)
      shipStockupCompleteByOrder(this.form).then(response => {
        this.$modal.msgSuccess("备货完成");
        this.open = false;
        this.getList();
      });


    },
    handleStatistics(row){
      this.handleSelection(row,false)
    },
    /** 按钮操作 */
    handleSelection(row,isGen) {
      const ids = row.orderNum || this.ids;
      // console.log("=====生成出库单=====",ids)
      if(!ids && ids.length===0){
        this.$modal.msgError("请选择备货订单");
        return
      }
      if(isGen===undefined) this.isGen = true
      else this.isGen = isGen
      if(this.isGen === false)this.title = "打印备货单";
      else this.title = "备货完成";

      // 创建一个包含年月日小时分钟秒的字符串作为基本编号
      var date = new Date();
      var year = date.getFullYear().toString(); // 四位数表示的年份
      var month = (date.getMonth() + 1).toString().padStart(2, '0'); // 两位数表示的月份（注意要加上补零）
      var day = date.getDate().toString().padStart(2, '0'); // 两位数表示的天数（同样需要补零）
      var hours = date.getHours().toString().padStart(2, '0'); // 两位数表示的小时数（同样需要补零）
      var minutes = date.getMinutes().toString().padStart(2, '0'); // 两位数表示的分钟数（同样需要补零）
      var seconds = date.getSeconds().toString().padStart(2, '0'); // 两位数表示的秒数（同样需要补零）
      // 生成随机数部分
      var randomNum = Math.floor((Math.random() * 9) + 1); // 生成1到9之间的随机整数
      // 将所有部分组合起来形成最终的编号
      var code = `${year}${month}${day}${hours}${minutes}${seconds}${randomNum}`;
      // console.log("生成的编号为：" + code);

      this.form.stockOutNum = code
      this.form.completeTime = new Date()
      this.skuList=[]

      ids.forEach(orderNum=>{
        // const obj = this.shippingList.find(y=>y.orderNum === orderNum)
        const objs= this.shippingListOrigin.filter(y=>y.orderNum === orderNum)
        objs.forEach(obj=>{


        const has = this.skuList.find(y=>y.originalSkuId === obj.originalSkuId)
        if(has){
          // 增加数量即可
          has.quantity = has.quantity + obj.quantity
          has.ids.push(orderNum)
        }else{
          // 新增数据
          const ids1 =[]
          ids1.push(orderNum);
          this.skuList.push({
            ids:ids1,
            specId:obj.specId,
            originalSkuId:obj.originalSkuId,
            goodsImg:obj.goodsImg,
            goodsNum:obj.goodsNum,
            goodsTitle:obj.goodsTitle,
            goodsSpec:obj.goodsSpec,
            specNum:obj.specNum,
            quantity:obj.quantity,
            inventory:obj.inventory
          })
        }
        })
      })


      this.open = true;
      // this.$modal.confirm('是否确认删除仓库订单发货编号为"' + ids + '"的数据项？').then(function() {
      //   return delShipping(ids);
      // }).then(() => {
      //   this.getList();
      //   this.$modal.msgSuccess("删除成功");
      // }).catch(() => {});

    },


  }
};
</script>
