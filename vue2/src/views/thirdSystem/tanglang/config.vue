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

      <el-form-item label="公司ID" prop="bizId">
        <el-input v-model="form.bizId" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="submitForm('ruleForm')">保存配置</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>


import {getTangLangConfig,saveTangLangConfig} from "../../../api/shop/tanglang";

export default {
  name: "PushSetting",
  data() {
    return {
      loading:false,
      form: {
        name: '',
        apiUrl: 'https://mcapi9.bjmantis.cn/hc-api',
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
        remark: ''
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
        bizId: [
          { required: true, message: '请输入公司ID', trigger: 'change' }
        ],


      }
    }
  },
  created() {

  },
  mounted() {
    getTangLangConfig().then(response => {
      if(response.data){
        this.form = response.data;
      }

    });
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading =true
          const $this = this
          saveTangLangConfig(this.form).then(resp=>{
            this.$modal.msgSuccess("保存成功")
            setTimeout(function() {
              // 10秒后执行的操作
              $this.loading = false
            }, 10000);  // 10000毫秒即10秒
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
