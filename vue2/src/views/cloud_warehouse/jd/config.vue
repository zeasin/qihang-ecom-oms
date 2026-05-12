<template>
  <div class="app-container">
    <el-form ref="ruleForm" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="服务器Url" prop="apiUrl">
        <el-input v-model="form.apiUrl" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="AppKey" prop="appKey">
        <el-input v-model="form.appKey" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="AppSecret" prop="appSecret">
        <el-input v-model="form.appSecret" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="AccountToken" prop="accountToken">
        <el-input v-model="form.accountToken" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="RefreshToken" prop="accountToken">
        <el-input v-model="form.refreshToken" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="ISV授权码" prop="bizPin">
        <el-input v-model="form.bizPin" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="CLPS事业部编码" prop="bizId">
        <el-input v-model="form.bizId" style="width: 300px"></el-input>
      </el-form-item>

<!--      <el-form-item label="开启推送" prop="isOn">-->
<!--        <el-switch v-model="form.isOn"></el-switch>-->
<!--      </el-form-item>-->
      <el-form-item label="ISV编码">
        <el-input v-model="form.isvSource" style="width: 300px"></el-input>
<!--        <el-input type="textarea" v-model="form.remark"></el-input>-->
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
      <el-col :span="24" style="padding-bottom: 50px;padding-top: 20px">
        <span>京东云仓物流回调URL：</span><span>{{form.callbackUrl}}</span>
      </el-col>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">保存配置</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>


import {getJdCloudWarehouseConfig,saveJdCloudWarehouseConfig} from "@/api/cloud_warehouse/jd";

export default {
  name: "PushSetting",
  data() {
    return {
      form: {
        name: '',
        apiUrl: 'https://api.jdl.com',
        appKey: '',
        appSecret: '',
        isOn: false,
        type: [],
        refundPath: '',
        orderPath: '',
        accountToken: '',
        refreshToken: '',
        bizPin: '',
        bizId: '',
        isvSource: '',
        remark: '',
        callbackUrl: 'https://erp.benshutech.com/prod-api/api/open/cloudWarehouse/feedback/jd/waybillFeedbackMsg',
      },
      rules: {
        name: [
          { required: true, message: '请输入系统名称', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        apiUrl: [
          { required: true, message: '请输入服务器请求API', trigger: 'change' }
        ],
        appKey: [
          { required: true, message: '请输入appKey', trigger: 'change' }
        ],
        appSecret: [
          { required: true, message: '请输入appSecret', trigger: 'change' }
        ],
        // type: [
        //   { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        // ],

      }
    }
  },
  created() {

  },
  mounted() {
    getJdCloudWarehouseConfig().then(response => {
      if(response.data){
        this.form = response.data;
      }

    });
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          saveJdCloudWarehouseConfig(this.form).then(resp=>{
            this.$modal.msgSuccess("保存成功")
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>
