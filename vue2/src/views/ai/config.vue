<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>AI配置管理</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="DeepSeek" name="deepseek">
          <el-form :model="deepseekForm" :rules="rules" ref="deepseekForm" label-width="120px">
            <el-form-item label="模型名称" prop="name">
              <el-input v-model="deepseekForm.name" placeholder="请输入模型名称" />
            </el-form-item>
            <el-form-item label="API地址" prop="apiUrl">
              <el-input v-model="deepseekForm.apiUrl" placeholder="请输入API地址" />
            </el-form-item>
            <el-form-item label="API密钥" prop="appKey">
              <el-input v-model="deepseekForm.appKey" type="password" placeholder="请输入API密钥" show-password />
            </el-form-item>
            <el-form-item label="是否开启" prop="isOn">
              <el-switch v-model="deepseekForm.isOn" active-value="1" inactive-value="0" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="deepseekForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveConfig('deepseek')">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="阿里云百炼" name="alibaba">
          <el-form :model="alibabaForm" :rules="rules" ref="alibabaForm" label-width="120px">
            <el-form-item label="模型名称" prop="name">
              <el-input v-model="alibabaForm.name" placeholder="请输入模型名称" />
            </el-form-item>
            <el-form-item label="API地址" prop="apiUrl">
              <el-input v-model="alibabaForm.apiUrl" placeholder="请输入API地址" />
            </el-form-item>
            <el-form-item label="API密钥" prop="appKey">
              <el-input v-model="alibabaForm.appKey" type="password" placeholder="请输入API密钥" show-password />
            </el-form-item>
            <el-form-item label="是否开启" prop="isOn">
              <el-switch v-model="alibabaForm.isOn" active-value="1" inactive-value="0" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="alibabaForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveConfig('alibaba')">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getAiConfig, saveAiConfig } from '@/api/ai/config'

export default {
  data() {
    return {
      activeTab: 'deepseek',
      deepseekForm: {
        systemType: 'deepseek',
        name: 'deepseek-chat',
        apiUrl: 'https://api.deepseek.com/v1',
        appKey: '',
        isOn: 1,
        remark: ''
      },
      alibabaForm: {
        systemType: 'alibaba',
        name: 'qwen-plus',
        apiUrl: 'https://dashscope.aliyuncs.com/compatible-mode/v1',
        appKey: '',
        isOn: 1,
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入模型名称', trigger: 'blur' }
        ],
        apiUrl: [
          { required: true, message: '请输入API地址', trigger: 'blur' }
        ],
        appKey: [
          { required: true, message: '请输入API密钥', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadConfig()
  },
  methods: {
    loadConfig() {
      getAiConfig('deepseek').then(res => {
        if (res.data) {
          this.deepseekForm = { ...this.deepseekForm, ...res.data }
        }
      })
      getAiConfig('alibaba').then(res => {
        if (res.data) {
          this.alibabaForm = { ...this.alibabaForm, ...res.data }
        }
      })
    },
    handleTabClick(tab) {
      // 切换标签时不需要额外操作
    },
    saveConfig(type) {
      const form = type === 'deepseek' ? this.deepseekForm : this.alibabaForm
      const formRef = type === 'deepseek' ? this.$refs.deepseekForm : this.$refs.alibabaForm
      
      formRef.validate(valid => {
        if (valid) {
          saveAiConfig(form).then(res => {
            this.$message.success('保存成功')
          }).catch(err => {
            this.$message.error('保存失败')
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
