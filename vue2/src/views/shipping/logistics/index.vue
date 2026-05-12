<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <div class="search-form">
      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog">添加快递公司</el-button>
    </div>

    <!-- 常用快递公司列表 -->
    <el-card title="常用快递公司">
      <div v-if="favoriteList.length === 0" class="empty-state">
        <el-empty description="暂无常用快递公司" />
        <el-button type="primary" @click="openAddDialog" style="margin-top: 20px;">添加快递公司</el-button>
      </div>

      <el-table v-else :data="favoriteList" border style="width: 100%">
        <el-table-column prop="logisticsName" label="快递公司名称" width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.isDefault === 1" class="default-tag">默认</span>
            {{ scope.row.logisticsName }}
          </template>
        </el-table-column>
        <el-table-column prop="shopType" label="平台" width="120">
          <template slot-scope="scope">
            {{ getPlatformName(scope.row.shopType) }}
          </template>
        </el-table-column>
        <el-table-column prop="logisticsCode" label="快递公司编码" width="150" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button 
              v-if="scope.row.isDefault !== 1" 
              type="text" 
              @click="handleSetDefault(scope.row)"
            >设为默认</el-button>
            <span v-else class="text-success">默认快递公司</span>
            <el-divider direction="vertical" />
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加快递公司弹窗 -->
    <el-dialog title="添加快递公司" :visible.sync="addDialogVisible" width="500px">
      <el-form :model="addForm" :rules="addRules" ref="addForm" label-width="100px">
        <el-form-item label="平台" prop="shopType">
          <el-select 
            v-model="addForm.shopType" 
            style="width: 300px;" 
            placeholder="请先选择平台"
            @change="handlePlatformChange"
            clearable
          >
            <el-option
              v-for="item in platformList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="快递公司" prop="logisticsId">
          <el-select 
            v-model="addForm.logisticsId" 
            style="width: 300px;" 
            placeholder="选择平台后搜索快递公司"
            filterable
            remote
            :remote-method="searchLogistics"
            :loading="logisticsLoading"
            :disabled="!addForm.shopType"
            @change="handleLogisticsChange"
          >
            <el-option
              v-for="item in logisticsOptions"
              :key="item.id"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.code || '-' }}</span>
            </el-option>
          </el-select>
          <div v-if="showNoDataTip" class="no-data-tip">
            <el-alert
              title="未找到该快递公司，请联系管理员添加到快递公司库"
              type="warning"
              :closable="false"
              show-icon
              size="small"
            />
          </div>
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="addForm.isDefault" active-value="1" inactive-value="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDialog">取 消</el-button>
        <el-button type="primary" @click="handleAdd">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getPlatformList,
  getFavoriteList, 
  getAvailableList, 
  addFavorite, 
  deleteFavorite, 
  setDefault 
} from '@/api/shipping/ship_logistics'

export default {
  name: 'VendorLogistics',
  data() {
    return {
      // 平台列表
      platformList: [],
      // 常用快递公司列表
      favoriteList: [],
      // 搜索结果选项（按需加载）
      logisticsOptions: [],
      // 搜索loading状态
      logisticsLoading: false,
      // 显示无数据提示
      showNoDataTip: false,
      // 添加弹窗
      addDialogVisible: false,
      addForm: {
        logisticsId: '',
        shopType: null,
        isDefault: '0'
      },
      addRules: {
        shopType: [
          { required: true, message: '请选择平台', trigger: 'change' }
        ],
        logisticsId: [
          { required: true, message: '请选择快递公司', trigger: 'change' }
        ]
      },
      // 平台映射（参考 EnumShopType）
      platformMap: {
        0: 'ERP内销订单',
        100: '淘宝天猫',
        200: '京东POP',
        280: '京东自营',
        300: '拼多多',
        400: '抖店',
        500: '微信小店',
        600: '快手小店',
        700: '小红书',
        901: '微店',
        911: '螳螂系统',
        999: '线下渠道',
        10000: '店铺订单'
      }
    }
  },
  created() {
    this.loadPlatformList()
    this.loadFavoriteList()
  },
  methods: {
    // 获取平台列表
    loadPlatformList() {
      getPlatformList().then(response => {
        this.platformList = response.data || []
      })
    },

    // 获取平台名称
    getPlatformName(platformId) {
      return this.platformMap[platformId] || '未知'
    },

    // 加载常用列表
    loadFavoriteList() {
      getFavoriteList().then(response => {
        this.favoriteList = response.data || []
      })
    },

    // 平台选择变化
    handlePlatformChange() {
      this.addForm.logisticsId = ''
      this.logisticsOptions = []
      this.showNoDataTip = false
    },

    // 远程搜索快递公司
    searchLogistics(keyword) {
      if (!keyword || keyword.trim() === '') {
        this.logisticsOptions = []
        this.showNoDataTip = false
        return
      }

      if (!this.addForm.shopType) {
        this.$message.warning('请先选择平台')
        return
      }
      
      this.logisticsLoading = true
      getAvailableList({ name: keyword, platformId: this.addForm.shopType }).then(response => {
        this.logisticsOptions = response.data || []
        this.showNoDataTip = this.logisticsOptions.length === 0
        this.logisticsLoading = false
      }).catch(() => {
        this.logisticsOptions = []
        this.showNoDataTip = false
        this.logisticsLoading = false
      })
    },

    // 选择快递公司时自动获取平台
    handleLogisticsChange(logisticsId) {
      const selected = this.logisticsOptions.find(item => item.id === logisticsId)
      if (selected) {
        this.addForm.shopType = selected.platformId
      }
    },

    // 打开添加弹窗
    openAddDialog() {
      this.addForm = {
        logisticsId: '',
        shopType: null,
        isDefault: '0'
      }
      this.logisticsOptions = []
      this.showNoDataTip = false
      this.addDialogVisible = true
    },

    // 取消弹窗
    cancelDialog() {
      this.addDialogVisible = false
      this.$refs.addForm && this.$refs.addForm.resetFields()
    },

    // 添加常用快递公司
    handleAdd() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          addFavorite({
            logisticsId: this.addForm.logisticsId,
            shopType: this.addForm.shopType,
            isDefault: parseInt(this.addForm.isDefault)
          }).then(response => {
            this.$message.success('添加成功')
            this.addDialogVisible = false
            this.loadFavoriteList()
          }).catch(() => {
            this.$message.error('添加失败')
          })
        }
      })
    },

    // 删除常用快递公司
    handleDelete(row) {
      this.$confirm('确定要删除该常用快递公司吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFavorite(row.id).then(response => {
          this.$message.success('删除成功')
          this.loadFavoriteList()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },

    // 设置默认快递公司
    handleSetDefault(row) {
      this.$confirm('确定要设为默认快递公司吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        setDefault(row.id).then(response => {
          this.$message.success('设置成功')
          this.loadFavoriteList()
        }).catch(() => {
          this.$message.error('设置失败')
        })
      })
    }
  }
}
</script>

<style scoped>
.default-tag {
  background: #67c23a;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  margin-right: 8px;
}

.empty-state {
  text-align: center;
  padding: 40px;
}

.search-form {
  margin-bottom: 20px;
}

.no-data-tip {
  margin-top: 8px;
}
</style>