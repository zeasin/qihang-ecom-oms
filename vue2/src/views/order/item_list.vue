<template xmlns="http://www.w3.org/1999/html">
  <div >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
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
      <el-form-item label="平台" prop="shopType" v-if="!isShop">
        <el-select v-model="queryParams.shopType"  placeholder="请选择平台"  @change="platformChange">
          <el-option label="销售订单" value="0"></el-option>
          <el-option
            v-for="item in platformList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId" v-if="!isShop">
        <el-select :loading="shopLoading" v-model="queryParams.shopId" placeholder="请选择店铺"
                   remote reserve-keyword :remote-method="searchShop"
                   filterable clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >
             <el-tag>{{ platformList.find(x=>x.id === item.type) ? platformList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>
           </span>
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

      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="新订单" value="0" ></el-option>
          <el-option label="待发货" value="1" ></el-option>
          <el-option label="已发货" value="2"></el-option>
          <el-option label="已完成" value="3"> </el-option>
          <el-option label="已取消" value="11"></el-option>
          <el-option label="已关闭" value="13"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="退款状态" prop="refundStatus">
        <el-select v-model="queryParams.refundStatus" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="无售后或售后关闭" value="1" ></el-option>
          <el-option label="售后处理中" value="2"></el-option>
          <el-option label="退款中" value="3"> </el-option>
          <el-option label="退款成功" value="4"></el-option>
          <el-option label="已取消" value="11"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="发货方" prop="shipType">
        <el-select v-model="queryParams.shipType" placeholder="发货方" clearable @change="handleQuery">
          <el-option label="未指定" value="0"></el-option>
          <el-option label="供应商发货" value="300"></el-option>
          <el-option label="云仓发货" value="200"></el-option>
          <el-option label="京东云仓发货" value="100"></el-option>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="是否关联" prop="hasLink">-->
<!--        <el-select v-model="queryParams.hasLink" placeholder="是否关联" clearable @change="handleQuery">-->
<!--          <el-option label="未关联" value="0"></el-option>-->
<!--          <el-option label="已关联" value="1"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
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
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="子订单编号" align="center" prop="subOrderNum" />-->

      <el-table-column label="订单编号" align="left" prop="orderNum" width="200" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
          >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
          <br/>
          {{ parseTime(scope.row.orderTime) }}
        </template>>
      </el-table-column>
<!--      <el-table-column label="供应商" align="left" prop="shipSupplier" >-->
<!--        <template slot-scope="scope">-->
<!--         {{ supplierList.find(x=>x.id === scope.row.shipSupplier) ? supplierList.find(x=>x.id === scope.row.shipSupplier).name : '' }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="图片"  prop="goodsImg" width="50px">
        <template slot-scope="scope">
<!--              <el-image style="width: 70px; height: 70px;" :src="scope.row.goodsImg"></el-image>-->
          <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品标题" align="left" prop="goodsTitle" width="250px"/>
      <el-table-column label="规格" align="center" prop="goodsSpec" >
        <template slot-scope="scope">
          {{ getSkuValues(scope.row.goodsSpec)}}
        </template>
      </el-table-column>
      <el-table-column label="Sku编码" align="center" prop="skuNum" />
      <el-table-column label="平台SkuId" align="center" prop="skuId" />
      <el-table-column label="商品库SkuId" align="center" prop="goodsSkuId" >
        <template slot-scope="scope">
          {{scope.row.goodsSkuId}}
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEditErpSku(scope.row)"
          ></el-button>

        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="子订单金额" align="center" prop="itemAmount" :formatter="amountFormatter"/>
      <el-table-column label="优惠总金额" align="center" prop="discountAmount" :formatter="amountFormatter"/>
<!--      <el-table-column label="是否赠品" prop="isGift">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag type="danger" v-if="scope.row.isGift === 1">赠品</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="店铺" align="left" prop="shopId" width="160px" v-if="!isShop">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" >
        <template slot-scope="scope">
          <!-- 订单状态1：待发货，2：已发货，3：已完成，11 已取消；21待付款 -->
          <el-tag v-if="scope.row.orderStatus === 1">待发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 2">已发货</el-tag>
          <el-tag v-if="scope.row.orderStatus === 3">已完成</el-tag>
          <el-tag v-if="scope.row.orderStatus === 11">已取消</el-tag>
          <el-tag v-if="scope.row.orderStatus === 21">待付款</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="refundStatus" >
        <template slot-scope="scope">
           <!-- 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
           <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
           <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
           <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>
           <el-tag v-if="scope.row.refundStatus === 11">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发货方" align="center" prop="shipStatus" >
        <template slot-scope="scope">
          <el-tag v-if="!scope.row.shipperType" type="info" style="margin-bottom: 6px;">未分配</el-tag>
          <el-tag v-if="scope.row.shipperType === 'SUPPLIER'" style="margin-bottom: 6px;">供应商发货</el-tag>
          <el-tag v-if="scope.row.shipperType === 'JD_CLOUD_WAREHOUSE'" style="margin-bottom: 6px;">京东云仓发货</el-tag>
          <el-tag v-if="scope.row.shipperType === 'CLOUD_WAREHOUSE'" style="margin-bottom: 6px;">云仓发货</el-tag>
          <el-col :span="24">
            <el-tag v-if="scope.row.shipStatus === 0"> 待发货</el-tag>
            <el-tag v-if="scope.row.shipStatus === 1"> 部分发货</el-tag>
            <el-tag v-if="scope.row.shipStatus === 2">全部发货</el-tag>
          </el-col>
        </template>
      </el-table-column>
<!--      <el-table-column label="下单时间" align="left" prop="orderTime" >-->
<!--        <template slot-scope="scope">-->
<!--          {{ parseTime(scope.row.orderTime) }}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleEditErpSku(scope.row)"-->
<!--          >修改商品SKU</el-button>-->
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
    <!-- 修改Erp Sku 对话框 -->
    <el-dialog title="修改ERP SKU ID" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="商品库商品SKU" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入ERP商品skuId或sku编码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listOrderItem, updateErpSkuId} from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import Clipboard from "clipboard";
import {listSupplier} from "@/api/goods/supplier";
import { listMerchant } from '@/api/shop/merchant'
import {getUserProfile} from "@/api/system/user";
export default {
  name: "OrderItemList",
  data() {
    return {
      // 遮罩层
      loading: true,
      shopLoading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSShopOrderItem: [],
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
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      platformList:[],
      supplierList:[],
      merchantList:[],
      // 弹出层标题
      open:false,
      orderTime: null,
      isMerchant: false,
      isShop: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        shopId: null,
        subOrderNum: null,
        startTime: null,
        endTime: null,
        shipSupplier: null,
        shipperType: null,
        orderStatus: null,
        // orderStatus: '1',
        refundStatus: null
        // refundStatus: '1'
      },
      // 表单参数
      form: {
        id:null,
        skuId:null
      },
      // 表单校验
      rules: {
        skuId: [{ required: true, message: "请输入商品库商品SkuId", trigger: "blur" }],
      }
    };
  },
  created() {
    listPlatform({status: 0}).then(res => {
      this.platformList = res.rows;
    })
    getUserProfile().then(res => {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        this.isMerchant = false
        this.isShop = false
        listMerchant({pageNum: 1, pageSize: 1000}).then(resp => {
          this.merchantList = resp.rows
          if (this.merchantList.length > 0) {
            this.queryParams.merchantId = this.merchantList[0].id
          }
          listShop({merchantId: this.queryParams.merchantId}).then(response => {
            this.shopList = response.rows;
            // if (this.shopList.length > 0) {
            //   this.queryParams.shopId = this.shopList[0].id
            // }
            this.shopLoading = false
            this.getList()
          });

        })
      } else if (res.data.userType == 20) {
        // 商户
        this.isMerchant = true;
        this.isShop = false
        this.queryParams.merchantId = res.data.deptId;
        this.merchantList = []
        this.shopList = []
        listShop({merchantId: this.queryParams.merchantId}).then(response => {
          this.shopList = response.rows;
          if (this.shopList.length > 0) {
            this.queryParams.shopId = this.shopList[0].id
          }
          this.shopLoading = false
          this.getList()
        });
      } else if (res.data.userType == 40) {
        this.isMerchant = true;
        this.isShop = true
        this.queryParams.shopId = res.data.deptId;
        this.merchantList = []
        this.shopList = []
        this.getList()
      }
    })
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
    amountFormatter(row, column, cellValue, index) {
      console.log('=========',cellValue)
      if(cellValue){
        return '￥' + cellValue?parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'):'';
      }else {
        return cellValue;
      }
      // return '￥' + cellValue?cellValue.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'):'';
    },
    platformChange(nv){
      console.log('========ShopType======',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({type:this.queryParams.shopType }).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv}).then(response => {
        this.shopList = response.rows;
        if (this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.shopLoading = false
        this.handleQuery()
      });

    },
    /** 查询店铺订单列表 */
    getList() {
      if(this.orderTime){
        this.queryParams.startTime = this.orderTime[0]
        this.queryParams.endTime = this.orderTime[1]
      }else {
        this.queryParams.startTime = null
        this.queryParams.endTime = null
      }
      this.loading = true;
      listOrderItem(this.queryParams).then(response => {
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
      this.form.erpSkuId = null
      this.open = false
    },
    handleEditErpSku(row){
      this.form.id = row.id
      if(row.goodsSkuId && row.goodsSkuId > 0) {
        this.form.skuId = row.skuId
      }else {
        this.form.skuId = null
      }
      this.open = true
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log('====修改参数====',this.form)
          updateErpSkuId(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });

        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/api/erp-api/order/item/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
