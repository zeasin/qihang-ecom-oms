<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="套餐名称" prop="packageName">
        <el-input v-model="queryParams.packageName" placeholder="请输入套餐名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="handleQuery">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="套餐编号" align="center" prop="packageNo" width="180" />
      <el-table-column label="套餐名称" align="center" prop="packageName" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-goods" @click="handleManageItems(scope.row)">管理商品</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="套餐名称" prop="packageName">
          <el-input v-model="form.packageName" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="管理套餐商品" :visible.sync="itemsDialogVisible" width="900px" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button size="small" icon="el-icon-plus" @click="addItemGoods">添加商品</el-button>
        </el-col>
      </el-row>
      <el-table :data="currentItems" border stripe>
        <el-table-column label="商品ID" prop="goodsId" width="100" />
        <el-table-column label="SKU ID" prop="goodsSkuId" width="100" />
        <el-table-column label="商品名称" prop="goodsName" />
        <el-table-column label="SKU名称" prop="skuName" width="150" />
        <el-table-column label="数量" width="120">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.quantity" :min="1" size="mini" @change="updateItemQuantity(scope.$index)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="removeItemGoods(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitItems">保存商品</el-button>
        <el-button @click="itemsDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="1" ref="popup" />
  </div>
</template>

<script>
import { list, add, update, remove, getInfo, getItems, saveItems, updateStatus } from '@/api/order/sales_goods_package'
import { parseTime } from '@/utils/zhijian'
import PopupSkuList from '@/views/goods/PopupSkuList.vue'

export default {
  name: 'SalesGoodsPackage',
  components: { PopupSkuList },
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      list: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 20 },
      form: {
        id: undefined,
        packageName: '',
        status: 1,
        remark: '',
        items: []
      },
      rules: {
        packageName: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }]
      },
      itemsDialogVisible: false,
      currentItems: [],
      currentPackageId: undefined
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.loading = true
      list(this.queryParams).then(res => {
        this.list = res.rows || []
        this.total = res.total
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.handleQuery()
    },
    handleAdd() {
      this.form = { id: undefined, packageName: '', status: 1, remark: '', items: [] }
      this.title = '新增套餐'
      this.open = true
    },
    handleEdit(row) {
      this.form = { id: row.id, packageName: row.packageName, status: row.status, remark: row.remark }
      this.title = '编辑套餐'
      this.open = true
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id) {
            update(this.form).then(() => {
              this.$message.success('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            add(this.form).then(res => {
              if (res.code === 200) {
                this.$message.success('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
    },
    handleStatusChange(row) {
      updateStatus({ id: row.id, status: row.status }).then(() => {
        this.$message.success(row.status === 1 ? '启用成功' : '禁用成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该套餐吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    handleManageItems(row) {
      this.currentPackageId = row.id
      this.currentItems = []
      getItems(row.id).then(res => {
        this.currentItems = res.data || []
      })
      this.itemsDialogVisible = true
    },
    addItemGoods() {
      this.$refs.popup.openDialog()
    },
    handleDataFromPopup(goods) {
      goods.forEach(item => {
        const exists = this.currentItems.some(i => i.goodsSkuId === item.id)
        if (!exists) {
          this.currentItems.push({
            goodsId: item.goodsId,
            goodsSkuId: item.id,
            goodsName: item.goodsName,
            skuName: item.skuName || '',
            skuCode: item.skuCode || item.sku_code || '',
            skuImage: item.colorImage || '',
            quantity: 1
          })
        }
      })
    },
    updateItemQuantity(index) {
    },
    removeItemGoods(index) {
      this.currentItems.splice(index, 1)
    },
    submitItems() {
      saveItems(this.currentPackageId, this.currentItems).then(() => {
        this.$message.success('保存成功')
        this.itemsDialogVisible = false
      })
    }
  }
}
</script>
