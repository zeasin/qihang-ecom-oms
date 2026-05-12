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
      <el-form-item label="备货状态" prop="stockingStatus">
        <el-select v-model="queryParams.stockingStatus" placeholder="请选择" @change="handleQuery">
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
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-document-copy"
          size="mini"
          :disabled="single"
          @click="handleSelection"
        >生成出库单</el-button>
      </el-col>
      <el-col :span="1.5">
      <el-button
        type="primary"
        plain
        icon="el-icon-printer"
        size="mini"
        :disabled="single"
        @click="handleStatistics"
      >查看明细</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="shippingList" @selection-change="handleSelectionChange"  >
       <el-table-column type="selection" width="55"  align="center" />
<!--       <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="订单号" align="left" prop="orderNum" width="200">
        <template slot-scope="scope">
          {{scope.row.orderNum}}
          <el-badge value="补" class="item" type="primary" v-if="scope.row.orderType===80"></el-badge>
          <el-badge value="换" class="item" type="primary" v-if="scope.row.orderType===20"></el-badge>
        </template>
      </el-table-column>
      <el-table-column label="商品" width="450px">
        <template slot-scope="scope">
          <!--            <el-row v-for="item in scope.row.itemList" :key="item.id" :gutter="20">-->
          <div style="float: left;display: flex;align-items: center;padding-right: 20px" >
            <image-preview :src="scope.row.itemList[0].goodsImg" :width="40" :height="40"/>
            <div style="margin-left:10px">
              <div style="width: 350px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" :title="scope.row.itemList[0].goodsTitle">
                {{scope.row.itemList[0].goodsName}}
              </div>
              <div>
                <span style="color: #5a5e66;font-size: 11px">规格：</span>
                <el-tag size="small">{{getSkuValues(scope.row.itemList[0].skuName)}}</el-tag>&nbsp;
                <span v-if="scope.row.itemList[0].skuCode" style="color: #5a5e66;font-size: 11px">编码：{{scope.row.itemList[0].skuCode}}</span></div>
              <div>
                <span>
                    <span style="color: #5a5e66;font-size: 11px">商品库ID：</span>
                    {{scope.row.itemList[0].goodsSkuId}}&nbsp;&nbsp
                  </span>
                <span>
                    <span style="color: #5a5e66;font-size: 11px">数量：</span>
                    <el-tag size="small">x {{scope.row.itemList[0].quantity}}</el-tag>
                  </span>
              </div>
            </div>
          </div>
          <div style="float: left;display: flex;align-items: center;padding-left: 50px" >
            <el-button v-if="scope.row.itemList.length>1"
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleStatistics(scope.row,'orderItems')"
            >更多订单商品</el-button>
          </div>
          <!--            </el-row>-->
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="orderTime" >
        <template slot-scope="scope">
          {{ parseTime(scope.row.shippingTime) }}
        </template>
      </el-table-column>

<!--      <el-table-column label="快递公司" align="center" prop="shippingCompany" ></el-table-column>-->
      <el-table-column label="发货快递" align="center" prop="shippingNumber" >
        <template slot-scope="scope">
          {{ scope.row.shippingNumber}}
          <span style="padding-left: 10px" v-if="scope.row.shippingCompany">{{scope.row.shippingCompany}}</span>
        </template>
      </el-table-column>


      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.stockingStatus === 0">待备货</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 1">部分备货</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 2">全部备货</el-tag>
          <el-tag size="small" v-if="scope.row.stockingStatus === 3">已发货</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="订单状态" align="center" prop="orderStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.orderStatus === 0" style="margin-bottom: 6px;">新订单</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 1" style="margin-bottom: 6px;">待发货</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 2" style="margin-bottom: 6px;">已发货</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 3" style="margin-bottom: 6px;">已完成</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 21" style="margin-bottom: 6px;">待付款</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 22" style="margin-bottom: 6px;">锁定</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 29" style="margin-bottom: 6px;">删除</el-tag>-->
<!--          <el-tag v-if="scope.row.orderStatus === 11" style="margin-bottom: 6px;">已取消</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="打单状态" align="center" prop="waybillStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 0">未取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 1">已取号</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 2">已打印</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.waybillStatus === 3">已发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="发货状态" align="center" prop="sendStatus" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag size="small" v-if="scope.row.sendStatus === 0">待备货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.sendStatus === 1">待发货</el-tag>-->
<!--          <el-tag size="small" v-if="scope.row.sendStatus === 2">已发货</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
<!--        <el-form-item label="生成" prop="completeTime" v-if="isGen">-->
<!--          <el-date-picker clearable-->
<!--            v-model="form.completeTime"-->
<!--            type="datetime" disabled-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            placeholder="请选择时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="打印时间" prop="completeTime" v-if="!isGen">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.completeTime"-->
<!--                          type="datetime" disabled-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="请选择时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
        <el-divider content-position="center" v-if="isGen">备货商品</el-divider>
        <el-table :data="skuList" :row-class-name="rowItemIndex" ref="skuItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="ItemId" align="center" prop="id" width="65"/>
<!--          <el-table-column label="商品图片" prop="goodsImg" >-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格" prop="skuName" >
            <template slot-scope="scope">
              {{getSkuValues(scope.row.skuName)}}
            </template>
          </el-table-column>
          <el-table-column label="sku编码" prop="skuCode" ></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>

        </el-table>
        <el-row style="padding-top: 20px"></el-row>
        <el-form-item label="仓库" prop="warehouseId" v-if="isGen">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" clearable @change="handleQuery">
            <el-option
              v-for="item in warehouseList"
              :key="item.id"
              :label="item.warehouseName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
      <div slot="footer" class="dialog-footer" v-if="isGen">
        <el-button type="primary" @click="submitForm">生成出库单</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!isGen">
        <el-button v-print="'#dialogContent'">打印</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listShipStockup,
  shipStockupCompleteByOrder
} from "@/api/shipping/stocking";

import { listShop } from "@/api/shop/shop";

import {listSupplier} from "@/api/goods/supplier";
import { getOrder } from '@/api/order/order'
import { listWarehouse } from '@/api/wms/warehouse'
export default {
  name: "ShipStockupOrder",
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
      warehouseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      skuIdUpdateOpen: false,
      isGen:true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        shopId: null,
        stockingStatus:null,
        orderNum:null,
      },
      // 表单参数
      form: {
        stockingId:null,
        warehouseId:null
      },
      form1: {
        orderItemIds:[]
      },
      form2: {
        orderItemId:null
      },

      skuList:[],

      statusList: [
        {
          value: '0',
          label: '待备货'
        }, {
          value: '1',
          label: '部分备货'
        }, {
          value: '2',
          label: '全部备货'
        }
      ],
      // 表单校验
      rules: {
        stockOutNum: [{ required: true, message: "单号不能为空", trigger: "blur" }],
        warehouseId: [{ required: true, message: "请选择仓库", trigger: "blur" }],
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
    listWarehouse({pageSize:100,warehouseType:'LOCAL'}).then(response => {
      this.warehouseList = response.rows;
    });
    this.getList();
    // if(this.$route.query.status){
    //   this.queryParams.status = this.$route.query.status
    // }else {
    //   this.queryParams.status = '0'
    // }
    // this.getList();
  },
  methods: {
    getSkuValues(spec) {
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
      listShipStockup(this.queryParams).then(response => {
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 提交按钮 */
    submitForm() {
      console.log("=============备货完成提交===", this.ids)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.skuList || this.skuList.length === 0) {
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
    handleStatistics(row) {
      console.log("=======",row);
      this.handleSelection(row, false)
    },
    /** 按钮操作 */
    handleSelection(row, isGen) {

      const ids = row.id || this.ids;
      console.log("=====生成出库单=====",ids,row.id)
      if (!ids) {
        this.$modal.msgError("请选择备货订单");
        return
      }
      if (isGen === undefined) this.isGen = true
      else this.isGen = isGen
      if (this.isGen === false) this.title = "备货明细";
      else this.title = "生成出库单";
      this.skuList=[]
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

      if(row.id){
        const order = this.shippingList.filter(x=>x.id === row.id)
        console.log('==========0000000',order)
        this.form.stockingId=row.id
        if(order&&order.length > 0){
          this.skuList = order[0].itemList
        }
      }else{
        const order = this.shippingList.filter(x=>x.id === ids[0])
        console.log('==========1111111',order)
        this.form.stockingId=ids[0]
        if(order&&order.length > 0){
          this.skuList = order[0].itemList
        }
      }


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
