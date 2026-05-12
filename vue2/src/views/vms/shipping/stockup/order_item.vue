<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品编码" prop="skuCode">
        <el-input
          v-model="queryParams.skuCode"
          placeholder="请输入商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>


<!--      <el-form-item label="发货状态" prop="sendStatus">-->
<!--        <el-select v-model="queryParams.sendStatus" placeholder="请选择状态" clearable @change="handleQuery">-->
<!--          <el-option label="待发货" value="1" ></el-option>-->
<!--          <el-option label="已发货" value="2"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->

<!--      <el-form-item label="售后状态" prop="refundStatus">-->
<!--        <el-select v-model="queryParams.refundStatus" placeholder="请选择状态" clearable @change="handleQuery">-->
<!--          <el-option label="无售后或售后关闭" value="1" ></el-option>-->
<!--          <el-option label="售后处理中" value="2"></el-option>-->
<!--          <el-option label="退款中" value="3"></el-option>-->
<!--          <el-option label="退款成功" value="4"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
      <el-button
        type="primary"
        plain
        icon="el-icon-printer"
        size="mini"
        :disabled="multiple"
        @click="handlePrint"
      >打印备货单</el-button>

      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="订单编号" align="left" prop="orderNum">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
          >{{scope.row.orderNum}} </el-button>
          <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" ></i>
        </template>
      </el-table-column>
      <el-table-column label="图片"  prop="goodsImg" width="50px">
        <template slot-scope="scope">
<!--              <el-image style="width: 70px; height: 70px;" :src="scope.row.goodsImg"></el-image>-->
          <el-image  style="width: 40px; height: 40px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品标题" align="left" prop="goodsName" width="250px">
        <template slot-scope="scope">
          {{scope.row.goodsName}}
          <!--                <el-tag size="small" v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>-->
          <el-tag size="small" v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 3">退款中</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 4">退款成功</el-tag>
          <el-tag size="small" v-if="scope.row.refundStatus === 11">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="SKU名" align="center" prop="skuName" >
        <template slot-scope="scope">
          {{ getSkuValues(scope.row.skuName)}}
        </template>
      </el-table-column>
      <el-table-column label="Sku编码" align="center" prop="skuCode" />
      <el-table-column label="数量" align="center" prop="quantity" />

      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="发货状态" align="center" prop="sendStatus" >
        <template slot-scope="scope">
           <el-tag v-if="scope.row.sendStatus === 1">待发货</el-tag>
           <el-tag v-if="scope.row.sendStatus === 2">已发货</el-tag>
        </template>
      </el-table-column>
      <!-- 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 -->
      <el-table-column label="售后状态" align="center" prop="refundStatus" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.refundStatus === 1">无售后或售后关闭</el-tag>
          <el-tag v-if="scope.row.refundStatus === 2">售后处理中</el-tag>
          <el-tag v-if="scope.row.refundStatus === 3">退款中</el-tag>
          <el-tag v-if="scope.row.refundStatus === 4">退款成功</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleEditErpSku(scope.row)"-->
<!--          >补充ERP SKU</el-button>-->
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

    <!-- 备货打印清单 -->
    <el-dialog title="备货打印清单" :visible.sync="open" width="800px" append-to-body >
      <div id="dialogContent">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>

          <el-divider content-position="center" >备货商品</el-divider>
          <el-table :data="skuList" :row-class-name="rowItemIndex" ref="skuItem">
            <!--          <el-table-column type="selection" width="50" align="center" />-->
            <el-table-column label="序号" align="center" prop="index" width="50"/>
            <el-table-column label="商品图片" prop="goodsImg" >
              <template slot-scope="scope">
                <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
            <el-table-column label="规格" prop="skuName" >
              <template slot-scope="scope">
                {{ getSkuValues(scope.row.skuName)}}
              </template>
            </el-table-column>
            <el-table-column label="sku编码" prop="skuCode" ></el-table-column>
            <el-table-column label="数量" prop="quantity"></el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-print="'#dialogContent'">打印</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listOrderItem} from "@/api/vms/order";
import {listPlatform} from "@/api/vms/shop";
import Clipboard from "clipboard";
export default {
  name: "StockUp",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      skuList: [],
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
      platformList:[],
      // 弹出层标题
      open:false,
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        sendStatus: 2,
      },
      // 表单参数
      form: {
        id:null,
        erpSkuId:null
      },
      // 表单校验
      rules: {
        erpSkuId: [{ required: true, message: "请输入ERP系统商品SkuId", trigger: "change" }],
      }
    };
  },
  created() {
    // listPlatform({status:0}).then(res => {
    //   this.platformList = res.rows;
    //   this.getList();
    // })
    this.getList();
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
      this.open = false
    },
    handlePrint(row) {
      const ids = row.id || this.ids;
      console.log("=====生成出库单=====", ids)
      if (!ids && ids.length === 0) {
        this.$modal.msgError("请选择备货订单");
        return
      }
      this.skuList = []
      ids.forEach(id => {
        // const obj = this.shippingList.find(y=>y.orderNum === orderNum)
        const objs = this.orderList.filter(y => y.id === id)
        console.log('======找到了勾选的订单=====', objs)
        if(objs){
          // 查找skuid是否存在
          const has = this.skuList.find(y => y.skuId === objs[0].skuId)
          if (has) {
            // 增加数量即可
            has.quantity = has.quantity + objs[0].quantity
            has.ids.push(id)
          } else {
            // 新增数据
            const ids1 = []
            ids1.push(id);
            this.skuList.push({
              ids: ids1,
              skuId: objs[0].skuId,
              goodsSkuId: objs[0].goodsSkuId,
              goodsImg: objs[0].goodsImg,
              goodsName: objs[0].goodsName,
              skuCode: objs[0].skuCode,
              skuName: objs[0].skuName,
              quantity: objs[0].quantity
            })
          }
        }


      })
      this.open = true
    }
  }
};
</script>
