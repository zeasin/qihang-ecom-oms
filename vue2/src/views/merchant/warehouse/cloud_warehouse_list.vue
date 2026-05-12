<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="仓库编号" prop="warehouseNo">
        <el-input
          v-model="queryParams.warehouseNo"
          placeholder="请输入仓库编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库名" prop="warehouseName">
        <el-input
          v-model="queryParams.warehouseName"
          placeholder="请输入仓库名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

<!--      <el-form-item label="状态" prop="status">-->
<!--        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable >-->
<!--          <el-option label="启用" value="1"></el-option>-->
<!--          <el-option label="禁用" value="0"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">


      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="warehouseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" width="88"/>
      <el-table-column label="仓库编号" align="center" prop="warehouseNo" />
      <el-table-column label="仓库名" align="center" prop="warehouseName" />
      <el-table-column label="仓库类型" align="center" prop="warehouseType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.warehouseType === 'LOCAL'">本地仓</el-tag>
          <el-tag size="small" v-if="scope.row.warehouseType === 'JDYC'">京东云仓</el-tag>
          <el-tag size="small" v-if="scope.row.warehouseType === 'CLOUD'">系统云仓</el-tag>
          <el-tag size="small" v-if="scope.row.warehouseType === 'Other'">其他</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="仓库地址" align="center" prop="address" />
      <el-table-column label="备注" align="center" prop="remark" />


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
import {
  getLocation,
  delLocation,
  addLocation,
  updateLocation,
  shareMerchant, setWarehouseLoginName
} from '@/api/wms/warehouse'
import {pcaTextArr} from "element-china-area-data";
import { listMerchant } from '@/api/shop/merchant'
import {  listMerchantCloudWarehouse } from '@/api/store/warehouse'


export default {
  name: "CloudWarehouseList",
  data() {
    return {
      // 遮罩层
      loading: true,
      shareOpen: false,
      openPwd: false,
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
      // 仓库货架表格数据
      warehouseList: [],
      merchantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        warehouseNo: null,
        name: null,
        status: null,
      },
      // 表单参数
      form: {
        id: null,
        warehouseType: null,
        warehouseNo: null,
        warehouseName: null,
        provinces: [],
        merchantIds: null,
        status:'1'
      },
      pwdform:{
        id:null,
        loginName: null,
        loginPwd: null,
      },
      pcaTextArr,
      // 表单校验
      rules: {
        warehouseNo: [
          { required: true, message: "编号不能为空", trigger: "blur" }
        ],
        warehouseName: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        warehouseType: [{ required: true, message: "不能为空", trigger: "blur" }],
        provinces: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        parentId1: [
          { required: true, message: "一级类目id不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      },
      pwdrules: {
        loginName: [
          { required: true, message: "登录名不能为空", trigger: "blur" },
          { min: 6, max: 15, message: '登录名长度必须介于 6 和 15 之间', trigger: 'blur' }
        ],
        loginPwd: [
          { required: true, message: "登录密码不能为空", trigger: "blur" },
          { min: 8, max: 32, message: '登录密码长度必须介于 8 和 32 之间', trigger: 'blur' }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询仓库货架列表 */
    getList() {
      this.loading = true;
      listMerchantCloudWarehouse({}).then(response => {
        this.warehouseList = response.data;

        this.loading = false;
      });
      // listWarehouse(this.queryParams).then(response => {
      //   this.warehouseList = response.rows;
      //   this.total = response.total;
      //   this.loading = false;
      // });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.shareOpen = false;
      this.openPwd = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        name: null,
        parentId: null,
        depth: null,
        provinces: [],
        parentId2: null,
        address: null,
        remark: null,
        status: '1',

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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.warehouseType='LOCAL'
      this.open = true;
      this.title = "添加本地仓仓库";
    },
    handleAdd2() {
      this.reset();
      this.form.warehouseType='CLOUD'
      this.open = true;
      this.title = "添加系统云仓仓库";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLocation(id).then(response => {
        this.form = response.data;
        this.form.provinces = []
        this.form.provinces.push(response.data.province)
        this.form.provinces.push(response.data.city)
        this.form.provinces.push(response.data.district)
        this.form.status = response.data.status+''
        this.open = true;
        this.title = "修改仓库";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.county = this.form.provinces[2]
          if (this.form.id != null) {
            updateLocation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLocation(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除仓库ID编号为"' + ids + '"的数据项？').then(function() {
        return delLocation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /**  */
    handlePostion(row) {
      this.$router.push({path:"/wms/position",query:{warehouseId:row.id}})
    },
    handleJdyc(row){
      this.$router.push({path:"/wms/jdl_info",query:{warehouseId:row.id}})
    },
    handleShareSupplier(row) {
      this.form.id = row.id
      listMerchant({}).then(response => {
        this.merchantList = response.rows;
      })
      if (row.mechantIds) {
        let numArray = row.supplierIds.replace(/^,|,$/g, '').split(',').map(Number);
        this.form.mechantIds = numArray
      } else this.form.mechantIds = null
      this.shareOpen = true
    },
    shareSupplierSubmit(){
      console.log("=======分配商户ID=====",this.form.supplierIds)
      shareMerchant(this.form).then(resp=>{
        this.$modal.msgSuccess("保存成功")
        this.shareOpen =false
        this.getList()
      })
    },
    handleSetLoginName(row){
      this.pwdform.id = row.id
      this.pwdform.loginName = row.loginName?row.loginName:''
      this.pwdform.loginPwd = ''
      this.openPwd = true;
    },
    submitPwdForm(){
      this.$refs["pwdform"].validate(valid => {
        if (valid) {
          // 验证密码强度
          // if (!this.validatePassword(this.user.newPassword,this.user.userName)) {
          //   this.$modal.msgError(this.passwordError)
          //   return
          // }
          // const res = validatePassword(this.pwdform.loginPwd,this.pwdform.loginName)
          // if (!res.result) {
          //   this.$modal.msgError(res.msg)
          //   return
          // }
          setWarehouseLoginName(this.pwdform).then(response => {
            this.$modal.msgSuccess("设置登陆账户成功");
            this.openPwd = false;
            this.getList();
          });
        }
      })
    },
  }
};
</script>
