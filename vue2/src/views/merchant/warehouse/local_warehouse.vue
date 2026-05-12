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

      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable >
          <el-option label="启用" value="1"></el-option>
          <el-option label="禁用" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-if="locationList.length===0"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增仓库</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="locationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" width="100"/>
      <el-table-column label="仓库编号" align="center" prop="warehouseNo" width="160"/>
      <el-table-column label="仓库名" align="center" prop="warehouseName" />
      <el-table-column label="仓库类型" align="center" prop="warehouseType" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.warehouseType === 'LOCAL'">本地仓</el-tag>
          <el-tag size="small" v-else-if="scope.row.warehouseType === 'CLOUD'">云仓库</el-tag>
          <el-tag size="small" v-else-if="scope.row.warehouseType === 'JDYC' && scope.row.jdlApiType==0">京东云仓-仓配一体</el-tag>
          <el-tag size="small" v-else-if="scope.row.warehouseType === 'JDYC' && scope.row.jdlApiType==1">京东云仓-ERP</el-tag>
          <el-tag size="small" v-else>未知仓库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="省市区" align="center" prop="province" >
        <template slot-scope="scope">
        {{scope.row.province}}
        {{scope.row.city}}
        {{scope.row.district}}
        {{scope.row.street}}
        </template>
      </el-table-column>
      <el-table-column label="详细地址" align="center" prop="address" />
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="仓库详情" align="center" prop="remark" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-button v-if="scope.row.warehouseType==='LOCAL'"-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-info"-->
<!--            @click="handlePostion(scope.row)"-->
<!--          >仓位信息</el-button>-->
<!--          <el-button v-if="scope.row.warehouseType==='JDYC'"-->
<!--                     size="mini"-->
<!--                     type="text"-->
<!--                     icon="el-icon-info"-->
<!--                     @click="handleJdyc(scope.row)"-->
<!--          >京东云仓信息</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.status == 0">禁用</el-tag>
          <el-tag size="small" v-if="scope.row.status == 1">启用</el-tag>
          <el-tag size="small" v-if="scope.row.status == 2">正常</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.warehouseType==='LOCAL'"
                     size="mini"
                     type="text"
                     icon="el-icon-info"
                     @click="handlePostion(scope.row)"
          >仓位信息</el-button>
          <el-row>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.warehouseType === 'LOCAL'"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.warehouseType === 'LOCAL'"
          >删除</el-button>
          </el-row>
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

    <!-- 添加或修改仓库货架对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="仓库编号" prop="warehouseNo">
          <el-input v-model="form.warehouseNo" placeholder="请输入仓库编号" />
        </el-form-item>
        <el-form-item label="仓库名" prop="warehouseName">
          <el-input v-model="form.warehouseName" placeholder="请输入仓库名" />
        </el-form-item>
        <el-form-item label="仓库类型" prop="warehouseType">
          <el-select v-model="form.warehouseType" placeholder="请选择仓库类型" clearable>
            <el-option label="本地仓" value="LOCAL"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="省市区" prop="provinces" v-if="form.warehouseType!=='JDYC'">
          <el-cascader style="width:250px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
<!--        <el-form-item label="街道" prop="address">-->
<!--          <el-input v-model="form.street" placeholder="请输入地址" />-->
<!--        </el-form-item>-->
        <el-form-item label="详细地址" prop="address" v-if="form.warehouseType!=='JDYC'">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" clearable >
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



  </div>
</template>

<script>
import {
  getLocation,
  delLocation,
  addLocation,
  updateLocation
} from '@/api/wms/warehouse'
import {pcaTextArr} from "element-china-area-data";

import {listMyLocalWarehouse} from '@/api/store/warehouse'

export default {
  name: "warehouseHome",
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
      // 仓库货架表格数据
      locationList: [],
      merchantList: [],
      accountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        warehouseNo: null,
        name: null,
        status: null,
      },
      // 表单参数
      form: {
        id: null,
        warehouseType: 'LOCAL',
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

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询仓库货架列表 */
    getList() {
      this.loading = true;
      listMyLocalWarehouse(this.queryParams).then(response => {
        this.locationList = response.data;
        this.total = response.total;
        this.loading = false;
      });
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
      this.title = "添加仓库";
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
        this.form.provinces.push(response.data.county)
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
              if(response.code===200) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }else{
                this.$modal.msgError(response.msg)
              }
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
      this.$router.push({path:"/wms/warehouse_info",query:{warehouseId:row.id}})
    },


  }
};
</script>
