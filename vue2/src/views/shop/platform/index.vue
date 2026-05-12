<template>
  <div class="app-container">


<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="shopList" >
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="ID" align="left" prop="id"  width="100"/>

<!--      <el-table-column label="国家/地区" align="center" prop="regionId" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag >{{regionList.find(x=>x.id === scope.row.regionId)?regionList.find(x=>x.id === scope.row.regionId).name:''}}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="平台" align="left" prop="name" width="200"/>
      <el-table-column label="平台编码" align="left" prop="code" width="200"/>
       <el-table-column label="AppKey" align="center" prop="appKey" />
<!--       <el-table-column label="AppSecret" align="center" prop="appSecret" />-->
       <el-table-column label="回调URL" align="center" prop="redirectUri" />
      <el-table-column label="SignSecret" align="center" prop="serverUrl" />
      <el-table-column label="是否开启" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
          <el-link type="primary" style="color: #ffffff" @click="handleUpdate(scope.row)">主要链接</el-link>
        </template>
      </el-table-column>
<!--      <el-table-column label="描述" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.id !== 500 "
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['shop:shop:edit']"
          >设置参数</el-button>

<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-location"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['shop:shop:edit']"-->
<!--          >省市区地址库</el-button>-->
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="平台名" prop="name">
          <el-input v-model="form.name"  placeholder="请输入平台名" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" :disabled="isEdit" placeholder="请输入平台编码" />
        </el-form-item>
        <el-form-item label="AppKey" prop="appKey">
          <el-input v-model="form.appKey" placeholder="请输入AppKey" />
        </el-form-item>
        <el-form-item label="AppSecret" prop="appSecret">
          <el-input v-model="form.appSecret" placeholder="请输入AppSecret" />
        </el-form-item>
        <el-form-item label="回调URL" prop="redirectUri">
          <el-input v-model="form.redirectUri" placeholder="请输入回调URL" />
        </el-form-item>
        <el-form-item label="SignSecret" prop="serverUrl">
          <el-input v-model="form.serverUrl" placeholder="请输入SignSecret" />
        </el-form-item>

<!--        <el-form-item label="描述" prop="remark">-->
<!--          <el-input type="textarea" v-model="form.remark" placeholder="请输入描述" />-->
<!--        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

<!--    &lt;!&ndash; API参数设置对话框 &ndash;&gt;-->
<!--    <el-dialog :title="title" :visible.sync="apiOpen" width="500px" append-to-body>-->
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="120px">-->
<!--        <el-form-item label="appkey" prop="appkey">-->
<!--          <el-input v-model="form.appkey" placeholder="请输入appkey" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="appSercet" prop="appSercet">-->
<!--          <el-input v-model="form.appSercet" placeholder="请输入appSercet" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="API请求URL" prop="apiRequestUrl">-->
<!--          <el-input v-model="form.apiRequestUrl" placeholder="请输入API请求URL" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="卖家UserId" prop="sellerUserId">-->
<!--          <el-input v-model="form.sellerUserId" placeholder="请输入sellerUserId" />-->
<!--        </el-form-item>-->
<!--&lt;!&ndash;        <el-form-item label="描述" prop="remark">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-input type="textarea" v-model="form.remark" placeholder="请输入描述" />&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
import {listPlatform, updatePlatform, getPlatform, changePlatformStatus,addPlatform} from "@/api/shop/shop";



export default {
  name: "ShopPlatform",
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

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      isEdit:true,
      apiOpen: false,
      // 查询参数
      queryParams: {},
      // 表单参数
      form: {
        redirectUri:null,
        serverUrl:null,
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: "不能为空", trigger: "blur" }],
        code: [{ required: true, message: "不能为空", trigger: "blur" }],
        // appKey: [{ required: true, message: "不能为空", trigger: "blur" }],
        // appSecret: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
      this.getList();
  },
  methods: {
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      listPlatform().then(response => {
        this.shopList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.apiOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.isEdit=true
      this.form = {
        id: null,
        name: null,
        code: null,
        appKey: null,
        appSecret: null,
        redirectUri: null,
        serverUrl: null
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
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.name + '"平台吗？').then(function() {
        return changePlatformStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset();
    //   this.open = true;
    //   this.isEdit = false
    //   this.title = "添加";
    // },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPlatform(id).then(response => {
        this.form = response.data;
        this.$nextTick(()=>{
          this.form.type = response.data.type+'';
        })

        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlatform(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.apiOpen = false
              this.getList();
            });
          }else{
            // 新增
            addPlatform(this.form).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.open = false;
              this.apiOpen = false
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
