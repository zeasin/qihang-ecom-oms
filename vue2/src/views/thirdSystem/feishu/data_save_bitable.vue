<template xmlns="http://www.w3.org/1999/html">
  <div >
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="98px">
      <el-form-item label="类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择"  >
          <el-option label="WIKI" value="wiki"></el-option>
          <el-option label="BASE" value="base"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文档类型" prop="docType">
        <el-select v-model="form.docType" placeholder="请选择"  >
          <el-option label="多维表格" value="bitable" selected>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表格Token" prop="token">
        <el-input
          v-model="form.token"
          placeholder="请输入表格Token"
          clearable
        />
      </el-form-item>
      <el-form-item label="表格TableId" prop="tableId">
        <el-input
          v-model="form.tableId"
          placeholder="请输入表格TableId"
          clearable
        />
      </el-form-item>
      <el-form-item label="userToken" prop="userToken">
        <el-input
          v-model="form.userToken"
          placeholder="请输入userToken"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getUserToken">获取用户授权</el-button>
        <el-button type="success" icon="el-icon-search" size="mini" @click="handleQuery">查询数据</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleSave"
        >保存数据到飞书</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange" :row-class-name="rowSShopOrderItemIndex">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" type="index" width="50"/>
      <el-table-column label="产品编码" align="center" prop="goodsNum" />
      <el-table-column label="外部编码" align="center" prop="outerErpGoodsId" />
      <el-table-column label="产品名称" align="center" prop="goodsName" />
      <el-table-column label="规格" align="center" prop="skuName" />
<!--      <el-table-column label="品牌" align="center" prop="brand" />-->
<!--      <el-table-column label="公司" align="center" prop="companyName" />-->
<!--      <el-table-column label="仓库库存" align="center" prop="currentStock" />-->
<!--      <el-table-column label="仓库" align="center" prop="warehouseId" />-->
      <el-table-column v-for="(column, index) in tableColumns" :label="column.label" :key="index"  align="center" :prop="column.prop" />

<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            v-if="scope.row.status===0"-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleEditErpSku(scope.row)"-->
<!--          >保存库存数据</el-button>-->
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

    <el-dialog :title="title" :visible.sync="authOpen" width="500px" append-to-body>
      <el-form ref="tokenForm" :model="tokenForm"  :rules="rules" label-width="100px">
        <el-descriptions >
          <el-descriptions-item label="授权URL："> {{ tokenForm.url }}</el-descriptions-item>
        </el-descriptions>
        <el-descriptions v-if="tokenForm.shopType === 100">
          <el-descriptions-item label="请设置淘宝开放平台回调URL"> http://ip:8088/api/store-api/tao/code_callback</el-descriptions-item>
        </el-descriptions>

        <div slot="footer" class="dialog-footer">
          请手动复制上面的URL到浏览器中访问
        </div>
        <el-form-item label="top_session" prop="code" v-if="tokenForm.shopType===100">
          <el-input type="textarea" v-model="tokenForm.code" placeholder="请复制淘宝授权后跳转页面的top_session参数值到这里" />
        </el-form-item>
        <el-form-item label="code" prop="code" v-if="tokenForm.shopType!==100">
          <el-input type="textarea" v-model="tokenForm.code" placeholder="请把授权后的code复制到这里" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getTokenSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      <!--      <div slot="footer" class="dialog-footer">-->
      <!--        请手动复制上面的URL到浏览器中访问-->
      <!--      </div>-->
    </el-dialog>

  </div>
</template>

<script>
import {listOrderItem, updateErpSkuId} from "@/api/order/order";
import { listPlatform, listShop } from '@/api/shop/shop'
import Clipboard from "clipboard";
import {listSupplier} from "@/api/goods/supplier";
import {getFeiShuOAuthUrl, getUserToken, queryFeiShuDocs, saveFeiShuDocs} from '@/api/third_system/feishu'
import { listWarehouse } from '@/api/wms/warehouse'
import { batchUpdateInventory, updateInventory } from '@/api/goods/goodsInventory'
import {listGoodsSpec} from "@/api/goods/goodsSpec";
export default {
  name: "WikiTablePage",
  data() {
    return {
      // 遮罩层
      loading: false,
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
      authOpen: false,
      title: null,
      // 总条数
      total: 0,
      // 店铺订单表格数据
      orderList: [],
      tableColumns: [],
      // ${subTable.functionName}表格数据
      sShopOrderItemList: [],
      shopList:[],
      warehouseList:[],
      typeList:[],
      supplierList:[],
      // 弹出层标题
      open:false,
      batchSaveOpen:false,
      orderTime: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10000,

      },
      currRow:{},
      tokenForm:{},
      // 表单参数
      form: {
        dataType: 1,
        type: 'base',
        docType: 'bitable',
        tableId: 'tblZPUnASt2mTiIf',
        token: 'FVdEbkCsnaQKtvsszDMc9sWenUg',
        userToken: ''
      },
      // 表单校验
      rules: {
        warehouseId: [{ required: true, message: "不能为空", trigger: "change" }],
        productCode: [{ required: true, message: "不能为空", trigger: "change" }],
        quantity: [{ required: true, message: "不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    this.getList()
  },
  methods: {

    /** 列表 */
    getList() {
      this.loading = true;
      listGoodsSpec(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.loading = false;
      });
      // queryFeiShuDocs(this.queryParams).then(response => {
      //   if(response.data.items){
      //     this.orderList = response.data.items;
      //     this.tableColumns = response.data.tableColumns
      //   }else{
      //     this.orderList = response.data;
      //   }
      //   this.loading = false;
      // });
    },
    getUserToken(){
      getFeiShuOAuthUrl({state:'1111'}).then(response => {
        if(response.code==200){
          this.tokenForm.url = response.data.url;
          this.title = "获取飞书授权"
          this.authOpen = true;
        }

      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // if(!this.queryParams.userToken){
      //   this.$modal.msgError("请获取用户授权")
      //   return;
      // }
      // if(!this.queryParams.token){
      //   this.$modal.msgError("请多维表格Token")
      //   return;
      // }
      // if(!this.queryParams.tableId){
      //   this.$modal.msgError("请填写TableId")
      //   return;
      // }
      this.getList();
    },
    getTokenSubmit(){
      getUserToken({code:this.tokenForm.code,state:"11111"}).then(response => {
        if(response.code===200){
          this.$modal.msgSuccess("获取授权成功")
          this.form.userToken = response.data.token;
          this.authOpen = false;
        }else
          this.$modal.msgError(response.msg);
      })

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.orderTime=null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.productCode+":"+item.currentStock)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    reset(){

    },
    cancel(){
    },
    handleSave() {
      if(!this.form.userToken){
        this.$modal.msgError("请获取用户授权")
        return;
      }
      if(!this.form.token){
        this.$modal.msgError("请多维表格Token")
        return;
      }
      if(!this.form.tableId){
        this.$modal.msgError("请填写TableId")
        return;
      }
      saveFeiShuDocs(this.form).then(resp=>{
        console.log("=======保存到飞书======")
        if(resp.code===200){
          this.$modal.msgSuccess("保存成功")
        }else this.$modal.msgError(resp.msg)
      })
    },

  }
};
</script>
