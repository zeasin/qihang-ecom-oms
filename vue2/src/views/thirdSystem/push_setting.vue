<template>
  <div class="app-container">
    <el-form ref="ruleForm" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="系统名称"  prop="name">
        <el-input v-model="form.name" style="width: 300px"></el-input>
      </el-form-item>
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
      <el-form-item label="BizPin" prop="bizPin">
        <el-input v-model="form.bizPin" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="BizId" prop="bizId">
        <el-input v-model="form.bizId" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="开启推送" prop="isOn">
        <el-switch v-model="form.isOn"></el-switch>
      </el-form-item>
      <el-form-item label="推送内容" prop="type">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="订单推送" value="order" name="type"></el-checkbox>
          <el-checkbox label="退款推送" value="refund" name="type"></el-checkbox>
<!--          <el-checkbox label="线下主题活动" name="type"></el-checkbox>-->
<!--          <el-checkbox label="单纯品牌曝光" name="type"></el-checkbox>-->
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="订单推送路径" prop="orderPath">
        <el-input v-model="form.orderPath" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="退款推送路径" prop="refundPath">
        <el-input v-model="form.refundPath" style="width: 300px"></el-input>
      </el-form-item>

      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">保存配置</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>


import {getInternalSystemConfig,saveInternalSystemConfig} from "@/api/third_system";

export default {
  name: "PushSetting",
  data() {
    return {
      form: {
        name: '',
        apiUrl: '',
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
        // type: [
        //   { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        // ],

      }
    }
  },
  created() {

  },
  mounted() {
    getInternalSystemConfig().then(response => {
      if(response.data){
        this.form = response.data;
      }

    });
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          saveInternalSystemConfig(this.form).then(resp=>{
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
