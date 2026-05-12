<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="店铺名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入店铺名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
       <el-form-item label="平台" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择平台" clearable @change="handleQuery">
         <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="店铺ID" align="center" prop="id" />-->
      <el-table-column label="店铺名" align="center" prop="name" />
      <el-table-column label="平台" align="center" prop="type" >
        <template slot-scope="scope">
          <el-tag >{{typeList.find(x=>x.id === scope.row.type)?typeList.find(x=>x.id === scope.row.type).name:''}}</el-tag>
        </template>
      </el-table-column>

       <el-table-column label="平台卖家ID" align="center" prop="sellerId" />
       <el-table-column label="AppKey" align="center" prop="appKey" />
       <el-table-column label="AppSecret" align="center" prop="appSecret" />
       <el-table-column label="accessToken" align="center" prop="accessToken" show-overflow-tooltip/>
<!--      <el-table-column label="店铺负责人" align="center" prop="manageUserId" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag >{{groupList.find(x=>x.id == scope.row.shopGroupId)?groupList.find(x=>x.id == scope.row.shopGroupId).name:''}}</el-tag>-->
<!--          <el-tag >{{userList.find(x=>x.userId == scope.row.manageUserId)?userList.find(x=>x.userId == scope.row.manageUserId).nickName:''}}</el-tag>-->
<!--          <br />-->
<!--          <el-button-->
<!--            type="text"-->
<!--            plain-->
<!--            size="mini"-->
<!--            @click="handleSettingMan(scope.row)"-->
<!--          >设置店铺负责人</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="描述" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status===1">启用</el-tag>
          <el-tag v-if="scope.row.status===0">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-row>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          </el-row>
          <el-button
            v-if="scope.row.type===100 || scope.row.type===200 || scope.row.type===280 || scope.row.type===300"
            type="success"
            plain
            icon="el-icon-refresh"
            size="mini"
            @click="handleUpdateToken(scope.row)"
          >更新AccessToken</el-button>
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

    <!-- 添加或修改店铺对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="店铺名" prop="name">
          <el-input v-model="form.name" placeholder="请输入店铺名" />
        </el-form-item>
        <el-form-item label="平台" prop="type">
          <el-select v-model="form.type" placeholder="请选择平台">

            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="平台卖家ID" prop="sellerId">
          <el-input v-model.number="form.sellerId" placeholder="请输入卖家Id名" />
        </el-form-item>
        <el-form-item label="appKey" prop="appKey" >
          <el-input v-model="form.appKey" placeholder="请输入appKey" />
        </el-form-item>
        <el-form-item label="appSecret" prop="appSecret" >
          <el-input v-model="form.appSecret" placeholder="请输入appSecret" />
        </el-form-item>
        <el-form-item label="回调URL" prop="callbackUrl" >
          <el-input v-model="form.apiCallbackUrl" placeholder="请输入回调URL" />
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
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="authOpen" width="500px" append-to-body>
      <el-form ref="tokenForm" :model="tokenForm"  :rules="rules" label-width="80px">
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
import {listShopPage, listShop,listPlatform, getShop, delShop, addShop, updateShop} from "@/api/vms/shop";

import {getOAuthUrl,getToken} from "@/api/vms/pdd-oauth";
export default {
  name: "Shop",
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
      // 店铺表格数据
      shopList: [],
      typeList: [],
      regionList: [],
      userList: [],
      groupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      authOpen: false,
      pullTimeOpen: false,
      openManage: false,
      pullTimeFormEdit : true,
      apiOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,

      },
      // 表单参数
      form: {
        type:null,
        status:'1'
      },
      manageForm:{
        id:null,
        shopGroupId:null
      },
      // 获取token表单
      tokenForm:{
        shopId: null,
        shopType: null,
        code:null
      },
      pullTimeForm:{
        id:null,
        orderStartTime:null,
        refundStartTime:null
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "店铺名不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择平台", trigger: "change" }],

        sellerId: [{ required: true, message: "不能为空", trigger: "change" }],
        appKey: [{ required: true, message: "不能为空", trigger: "change" }],
        appSecret: [{ required: true, message: "不能为空", trigger: "change" }],
        status: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      manageRules:{
        manageUserId: [{ required: true, message: "不能为空", trigger: "blur" }],
        shopGroupId: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      pullRules: {
        orderStartTime: [{ required: true, message: "不能为空", trigger: "change" }],
        refundStartTime: [{ required: true, message: "不能为空", trigger: "change" }]
      }
    };
  },
  created() {
      listPlatform().then(res => {
        this.typeList = res.rows.filter(x=>x.id!=999);
        this.getList()
      });

  },
  methods: {
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      listShopPage(this.queryParams).then(response => {
        this.shopList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.apiOpen = false;
      this.authOpen = false;
      this.pullTimeOpen = false;
      this.openManage = false;
      this.reset();
    },
    // 表单重置
    reset() {

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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加店铺";
    },
    handleUpdateToken(row){
     console.log("获取token",row)
      if(row.type === 200 || row.type === 280){
        getJdOAuthUrl({shopId:row.id}).then(response => {
          console.log("获取token=====jd ",response)
          this.authOpen = true;
          this.title = "更新店铺授权";
          this.tokenForm.url = response.data
          this.tokenForm.shopId = row.id
          this.tokenForm.shopType = row.type
        })
      }else if(row.type ===100){
        getTaoOAuthUrl({shopId:row.id}).then(response => {
          console.log("获取token=====tao ",response)
          this.authOpen = true;
          this.title = "更新店铺授权";
          this.tokenForm.url = response.data
          this.tokenForm.shopId = row.id
          this.tokenForm.shopType = row.type
        })
      }else if(row.type ===300){
        getOAuthUrl({shopId:row.id}).then(response => {
          console.log("获取token=====pdd ",response)
          this.authOpen = true;
          this.title = "更新店铺授权";
          this.tokenForm.url = response.data
          this.tokenForm.shopId = row.id
          this.tokenForm.shopType = row.type
        })
      }

    },
    handleSettingPullLasttime(row){
      this.pullTimeForm.id = row.id
      this.pullTimeOpen = true
      this.pullTimeFormEdit = true
      this.pullTimeForm.orderStartTime = null
      this.pullTimeForm.refundStartTime = null
      getShopPullTime(row.id).then(resp=>{
        if(resp.data.orderStartTime) {
          this.pullTimeForm.orderStartTime = resp.data.orderStartTime
          this.pullTimeFormEdit = false
        }
        if(resp.data.refundStartTime) {
          this.pullTimeForm.refundStartTime = resp.data.refundStartTime
          this.pullTimeFormEdit = false
        }
      })
    },
    pullTimeSubmit(){
      this.$refs["pullTimeForm"].validate(valid => {
        if (valid) {
          saveShopPullTime(this.pullTimeForm).then(resp=>{
            this.$modal.msgSuccess("设置成功");
            this.pullTimeOpen = false
            this.pullTimeForm.id = null
            this.pullTimeForm.orderStartTime =null
            this.pullTimeForm.refundStartTime = null
          })
        }
      })
    },
    handleSettingMan(row){
      this.openManage = true;
      this.manageForm.id = null
      const id = row.id || this.ids
      getShop(id).then(response => {
        this.manageForm = response.data;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getShop(id).then(response => {
        this.form = response.data;
        this.form.status = response.data.status+''
        // this.$nextTick(()=>{
        //   this.form.type = response.data.type;
        // })

        this.open = true;
        this.title = "修改店铺";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShop(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.apiOpen = false
              this.getList();
            });
          } else {
            addShop(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    getTokenSubmit(){
      this.$refs["tokenForm"].validate(valid => {
        if (valid) {
          console.log("=====更新token=====",this.tokenForm)
          if(this.tokenForm.shopType === 100){
            saveSessionKey(this.tokenForm).then(resp=>{
              this.authOpen = false
              this.$modal.msgSuccess("SessionKey保存成功");
              this.getList()
            });
          } else if(this.tokenForm.shopType === 200 || this.tokenForm.shopType === 280){
            getJdToken(this.tokenForm).then(response => {
              this.authOpen = false
              this.$modal.msgSuccess("授权成功");
              this.getList()
            });
          }else if(this.tokenForm.shopType === 300){
            getToken(this.tokenForm).then(response => {
              this.authOpen = false
              this.$modal.msgSuccess("授权成功");
              this.getList()
            });
          }
        }
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除店铺编号为"' + ids + '"的数据项？').then(function() {
        return delShop(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleGoodsList(row){
      console.log('=======商品list=====',row)
      if(row.type === 1){
        this.$router.push({path:'/shop/goods/tao_goods_list',query:{shopId:row.id}})
      }else if(row.type === 2){
        this.$router.push({path:'/shop/goods/jd_goods_list',query:{shopId:row.id}})
      }else if(row.type === 3){
        this.$router.push({path:'/shop/goods/dou_goods_list',query:{shopId:row.id}})
      }else if(row.type === 4){
        this.$router.push({path:'/shop/goods/pdd_goods_list',query:{shopId:row.id}})
      }
    },
    handleLogistics(row) {
      if(row.type === 1){
        this.$router.push({path:"/shop/logistics_companies",query:{id:row.type}})
      }else  if(row.type === 2){
        this.$router.push({path:"/shop/logistics_companies",query:{id:row.type,shopId:row.id}})
      } else  if(row.type === 3){
        this.$router.push({path:"/shop/logistics_companies",query:{id:row.type,shopId:row.id}})
      } else  if(row.type === 4){
        this.$router.push({path:"/shop/logistics_companies",query:{id:row.type,shopId:row.id}})
      }
    }
  }
};
</script>
