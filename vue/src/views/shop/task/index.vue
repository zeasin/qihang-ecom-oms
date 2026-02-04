<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="noticeTitle">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noticeList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="60" />
      <el-table-column label="任务名称" align="left" prop="taskName" width="180"/>
      <el-table-column label="表达式（-表示不运行）" align="center" prop="cron" />
      <el-table-column label="执行函数" align="center" prop="method" />

      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewLogs(scope.row.id)"
          >查看日志</el-button>
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
    <el-dialog title="任务运行日志" :visible.sync="logOpen" width="780px" append-to-body>
      <el-table v-loading="logLoading" :data="logList" >
        <el-table-column label="序号" align="center" prop="id" />
        <el-table-column label="说明" align="center" prop="result"  />
        <el-table-column label="开始时间" align="center" prop="startTime" >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="endTime" >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" />
      </el-table>

      <pagination
        v-show="logTotal>0"
        :total="logTotal"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="handleViewLogs(taskId)"
      />
    </el-dialog>
    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="form.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="表达式（-表示不运行）" prop="cron">
              <el-input v-model="form.cron" placeholder="请输入表达式（-表示不运行）" />
              <el-select v-model="cronType" @change="cronTypeChange" placeholder="表达式选择">
                <el-option label="不运行" value="0"></el-option>
                <el-option label="1分钟运行一次" value="1"></el-option>
                <el-option label="3分钟运行一次" value="3"></el-option>
                <el-option label="5分钟运行一次" value="5"></el-option>
                <el-option label="10分钟运行一次" value="10"></el-option>
                <el-option label="30分钟运行一次" value="30"></el-option>
                <el-option label="1小时运行一次" value="60"></el-option>
                <el-option label="12小时运行一次" value="720"></el-option>
                <el-option label="24小时运行一次" value="1440"></el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="24">
            <el-form-item label="执行函数" prop="method">
              <el-input v-model="form.method" placeholder="请输入执行函数" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容">
              <el-input type="textarea" v-model="form.remark" :min-height="192"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listTask, updateTask, getTask,getTaskLogs} from "@/api/system/task";

export default {
  name: "Notice",
  dicts: ['sys_notice_status', 'sys_notice_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      logLoading: true,
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
      // 公告表格数据
      noticeList: [],
      logList:[],
      logTotal:0,
      logOpen:false,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      taskId:undefined,
      cronType:undefined,
      // 表单参数
      form: {

      },
      // 表单校验
      rules: {
        taskName: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        cron: [
          { required: true, message: "不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  watch: {
    logOpen (nv) {
      if(nv){

      }else {
        this.taskId = undefined
      }
    }
  },
  methods: {
    cronTypeChange(){
      console.log("======表达式",this.cronType)
      if(this.cronType){
        if(this.cronType==0){
          this.form.cron="-"
        }else if(this.cronType == 1){
          this.form.cron = "0 0/1 * * * ?"
        } else if(this.cronType == 3){
          this.form.cron = "0 0/3 * * * ?"
        }else if(this.cronType == 5){
          this.form.cron = "0 0/5 * * * ?"
        }else if(this.cronType == 10){
          this.form.cron = "0 0/10 * * * ?"
        }else if(this.cronType == 30){
          this.form.cron = "0 0/30 * * * ?"
        }else if(this.cronType == 60){
          this.form.cron = "0 0 0/1 * * ?"
        }else if(this.cronType == 720){
          this.form.cron = "0 0 0/12 * * ?"
        }else if(this.cronType == 1440){
          this.form.cron = "0 0 0 0/1 * ?"
        }
      }
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.taskId = undefined;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        taskName: undefined,
        cron: undefined,
        method: undefined,
        remark: undefined
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
    handleViewLogs(taskId){
      this.taskId = taskId
      getTaskLogs(taskId).then(response => {
        this.logList = response.rows;
        this.logTotal = response.total;
        this.logLoading = false;
        this.logOpen = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTask(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
            updateTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
        }
      });
    }
  }
};
</script>
