<template>
  <div class="app-container">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="108px">
        <el-form-item label="快递公司编码" prop="deliveryId">
          <el-input
            v-model="queryParams.deliveryId"
            placeholder="请输入快递公司编码"
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
                <span style="float: left">{{ item.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 5">微信小店</span>
                  <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 3">拼多多</span>

                  <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 9">其他</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>

      </el-form>

    </el-row>


    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-time"
          size="mini"
          :loading="pullLoading"
          @click="updateWaybillAccount"
        >更新电子面单账户信息</el-button>
      </el-col>

<!--      <el-col :span="1.5">-->

<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-printer"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--        >手动添加</el-button>-->
<!--      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deliverList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->

      <el-table-column label="网点" align="left" prop="siteName" />
      <el-table-column label="网点编号" align="center" prop="siteCode" width="100px" />
      <el-table-column label="快递公司编码" align="center" prop="deliveryId" width="100px" />
<!--      <el-table-column label="客户编码" align="center" prop="sellerShopId" width="100px" />-->
      <el-table-column label="店铺" align="left" prop="netsiteName" width="200px">
        <template slot-scope="scope">
          {{ shopList.find(x=>x.id == scope.row.shopId) ? shopList.find(x=>x.id == scope.row.shopId).name : '' }}
        </template>
      </el-table-column>
      <el-table-column label="已取单号" align="center" prop="allocated" width="100px" />
      <el-table-column label="已取消单号" align="center" prop="cancel" width="100px" />
      <el-table-column label="已回收单号" align="center" prop="recycled" width="100px" />
      <el-table-column label="余额" align="center" prop="available" width="100px" />
<!--      <el-table-column label="省" align="center" prop="senderProvince"/>-->
<!--      <el-table-column label="市" align="center" prop="senderCity"/>-->
<!--      <el-table-column label="区" align="center" prop="senderCounty"/>-->
<!--      <el-table-column label="发货地址" align="center" prop="senderAddress"/>-->
      <el-table-column label="发货地址" align="left" prop="provinceName" width="200">
        <template slot-scope="scope">
          {{scope.row.senderProvince}} {{scope.row.senderCity}} {{scope.row.senderCounty}} <br/>
          {{scope.row.senderAddress}}
        </template>
      </el-table-column>
            <el-table-column label="发货人" align="left" prop="provinceName" width="200">
              <template slot-scope="scope">
                {{scope.row.name}} <br/>
                {{scope.row.mobile}}
              </template>
            </el-table-column>
<!--      <el-table-column label="发货人" align="center" prop="name"/>-->
<!--      <el-table-column label="发货手机号" align="center" prop="mobile"/>-->
      <el-table-column label="打印模版" align="left" prop="templateUrl"/>


      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >发货人&快递模版设置</el-button>
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

    <!-- 手动添加 -->
    <el-dialog title="手动添加" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
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
        <el-form-item label="卖家Id" prop="sellerShopId">
          <el-input v-model="form.sellerShopId" placeholder="请输入卖家Id名" />
        </el-form-item>
        <el-form-item label="appKey" prop="appKey" v-if="form.type===500">
          <el-input v-model="form.appKey" placeholder="请输入appKey" />
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="状态">
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="发货人&快递模版设置" :visible.sync="updateOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="网点名" prop="siteName">
          <el-input v-model="form.siteName" placeholder="请输入网点名" />
        </el-form-item>
        <el-form-item label="网点编码" prop="siteCode">
          <el-input v-model="form.siteCode" disabled placeholder="请输入网编码" />
        </el-form-item>
        <el-form-item label="快递公司编码" prop="siteName">
          <el-input v-model="form.deliveryId" disabled placeholder="请输入网点名" />
        </el-form-item>
        <el-form-item label="快递模块" prop="templateUrl">
          <el-select v-model="form.templateUrl" placeholder="请选择快递模版" clearable @change="handleQuery">
            <el-option
              v-for="item in templateList"
              :key="item.id"
              :label="item.templateName"
              :value="item.templateUrl">
            </el-option>
          </el-select>
          <el-button
          size="mini"
          type="text"
          @click="handlePullTemplate(form.deliveryId)"
        >更新快递模版</el-button>
        </el-form-item>
<!--        <el-form-item label="卖家Id" prop="sellerShopId">-->
<!--          <el-input v-model.number="form.sellerShopId" placeholder="请输入卖家Id名" />-->
<!--        </el-form-item>-->
        <el-form-item label="发货人" prop="name" >
          <el-input v-model="form.name" placeholder="请输入发货人" />
        </el-form-item>
        <el-form-item label="发货手机号" prop="mobile" >
          <el-input v-model="form.mobile" placeholder="请输入发货手机号" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {listShop} from "@/api/vms/shop";
import {
  getShareMeWaybillAccountList, getWaybillTemplateList,
  pullWaybillAccount, pullWaybillTemplat,
  updateAccount
} from "@/api/vms/ewaybillAccount";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";

export default {
  name: "WaybillAccountPdd",
  data() {
    return {
      // 遮罩层
      loading: true,
      pullLoading: false,
      // 选中数组
      ids: [],
      templateList: [],
      shopList: [],
      supplierList: [],
      deliverList: [],
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
      open:false,

      updateOpen:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type:0,
        shopId:null
      },
      // 打印参数
      printParams: {
        deliver: null,
        printer: null
      },
      // 表单参数
      form: {
        id:null,
        siteName:null,
        siteCode:null,
        sellerShopId:null,
        name:null,
        mobile:null,
        deliveryId: null,
        templateUrl: null
      },

      // 表单校验
      rules: {
        siteName: [{ required: true, message: '不能为空', trigger: "blur" }],
        siteCode: [{ required: true, message: '不能为空', trigger: "blur" }],
        templateUrl: [{ required: true, message: '不能为空', trigger: "blur" }],
        name: [{ required: true, message: '不能为空', trigger: "blur" }],
        mobile: [{ required: true, message: '不能为空', trigger: "blur" }],
      }
    };
  },
  created() {
    listShop().then(resp=>{
      this.shopList = resp.rows
      this.getList();
    })

  },
  methods: {
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      getShareMeWaybillAccountList(this.queryParams).then(response => {
        this.deliverList = response.rows;
        this.total = response.total;
        this.loading = false;
      });


    },
    // 取消按钮
    cancel() {
      this.open = false

      this.updateOpen = false
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
    // 更新快递模版
    handlePullTemplate(wpCode){
      if(this.queryParams.shopId){
        pullWaybillTemplat({shopId: this.queryParams.shopId,wpCode:wpCode}).then(response => {
          console.log('拉取订单接口返回=====',response)
          if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            getWaybillTemplateList({wpCode:row.deliveryId}).then(resp=>{
              this.templateList = resp.rows
             })
          }
          else if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              // this.$router.push({path:"/shop/list",query:{type:3}})
            }).catch(() => {
              isRelogin.show = false;
            });
          }else {
            this.$modal.msgError(JSON.stringify(response));
            this.pullLoading = false
          }
        });
      }else{
        this.$modal.msgError("请选择店铺")
      }
    },
    // 更新电子面单信息
    updateWaybillAccount() {
      if(this.queryParams.shopId){
        pullWaybillAccount({shopId: this.queryParams.shopId}).then(response => {
          console.log('拉取订单接口返回=====',response)
          if(response.code === 200){
            this.$modal.msgSuccess(JSON.stringify(response));
            this.pullLoading = false
            this.getList()
          }
          else if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              // this.$router.push({path:"/shop/list",query:{type:3}})
            }).catch(() => {
              isRelogin.show = false;
            });
          }else {
            this.$modal.msgError(JSON.stringify(response));
            this.pullLoading = false
          }
        });
      }else{
        this.$modal.msgError("请选择店铺")
      }

    },
    handleAdd() {
      this.open = true
    },
    addSubmit() {
      this.$refs["form"].validate(valid => {
        if (valid) {

        }
      })
    },
    handleUpdate(row){
      this.form.id = row.id
      this.form.siteName = row.siteName
      this.form.siteCode = row.siteCode
      this.form.sellerShopId = row.sellerShopId
      this.form.name = row.name
      this.form.mobile = row.mobile
      this.form.deliveryId = row.deliveryId
      getWaybillTemplateList({wpCode:row.deliveryId,shopType:3}).then(resp=>{
        this.templateList = resp.data
        this.form.templateUrl = row.templateUrl
        this.updateOpen = true
      })

    },
    updateSubmit(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateAccount(this.form).then(resp=>{
            this.$modal.msgSuccess("保存成功")
            this.updateOpen =false
            this.getList()
          })
        }
      })

    },
  }
};
</script>
