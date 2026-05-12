<template>
  <div class="purchase-inbound">
    <el-card class="form-card">
      <div slot="header">
        <span>采购入库单</span>
        <el-button type="primary" size="small" style="float: right;" @click="selectPurchaseOrder">选择采购单</el-button>
      </div>
      <el-form :model="inboundForm" label-width="120px" size="small">
        <el-form-item label="采购单号">
          <el-input v-model="inboundForm.purchaseOrderNo" disabled></el-input>
        </el-form-item>
        <el-form-item label="供应商">
          <el-input v-model="inboundForm.supplierName" disabled></el-input>
        </el-form-item>
        <el-form-item label="入库日期">
          <el-date-picker v-model="inboundForm.inboundDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="inboundForm.remark" type="textarea" rows="2"></el-input>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品入库明细列表 -->
    <el-card class="items-card" style="margin-top: 20px;">
      <div slot="header">
        <span>入库商品明细</span>
        <el-button type="primary" size="small" style="float: right;" @click="addItem" :disabled="!inboundForm.purchaseOrderId">添加商品</el-button>
      </div>
      <el-table :data="inboundItems" border stripe>
        <el-table-column label="SKU编码" prop="skuCode" width="120"></el-table-column>
        <el-table-column label="商品名称" prop="skuName" min-width="150"></el-table-column>
        <el-table-column label="库存模式" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.inventoryMode === 1 ? 'success' : 'info'">
              {{ scope.row.inventoryMode === 1 ? '一物一码' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="计划数量" prop="planQuantity" width="80" align="center"></el-table-column>
        <el-table-column label="已入库数量" prop="inboundQuantity" width="100" align="center"></el-table-column>
        <el-table-column label="本次入库" width="180" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.inventoryMode === 0">
              <el-input-number v-model="scope.row.thisInboundQuantity" :min="0" :max="scope.row.planQuantity - scope.row.inboundQuantity" controls-position="right" size="small"></el-input-number>
            </div>
            <div v-else>
              <el-button type="text" size="small" @click="openItemDetail(scope.row)">配置条码明细</el-button>
              <span v-if="scope.row.batchList && scope.row.batchList.length">（已录 {{ scope.row.batchList.length }} 件）</span>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 一物一码商品明细弹窗 -->
      <el-dialog :title="`录入条码明细 - ${currentSku && currentSku.skuName}`" :visible.sync="itemDetailVisible" width="800px" @close="resetBatchForm">
        <div class="batch-actions">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="addBatchRow">添加条码</el-button>
          <el-button type="info" size="small" icon="el-icon-upload" @click="importExcel">批量导入</el-button>
          <div class="scan-hint">
            <i class="el-icon-camera-solid"></i> 支持扫码枪连续扫描，每次扫描后自动添加一行
          </div>
        </div>
        <el-table :data="batchFormList" border stripe>
          <el-table-column label="条码" width="160">
            <template slot-scope="scope">
              <el-input v-model="scope.row.barcode" size="small" placeholder="扫描或输入" @keyup.enter.native="onBarcodeScan(scope.$index)"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="金重(g)" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.goldWeight" :min="0" :precision="2" size="small" controls-position="right"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="银重(g)" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.silverWeight" :min="0" :precision="2" size="small" controls-position="right"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="工费(元)" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.laborCost" :min="0" :precision="2" size="small" controls-position="right"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="removeBatchRow(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="batch-total">共 {{ batchFormList.length }} 件，总计金重 {{ totalGoldWeight }}g，银重 {{ totalSilverWeight }}g</div>
        <span slot="footer">
          <el-button @click="itemDetailVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBatchList">保存明细</el-button>
        </span>
      </el-dialog>
    </el-card>

    <div class="submit-bar">
      <el-button type="primary" @click="submitInbound" :loading="submitting">确认入库</el-button>
      <el-button @click="saveDraft">暂存</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PurchaseInbound',
  data() {
    return {
      inboundForm: {
        purchaseOrderId: null,
        purchaseOrderNo: '',
        supplierName: '',
        inboundDate: new Date().toISOString().slice(0,10),
        remark: ''
      },
      inboundItems: [],   // 从采购单加载的商品列表
      submitting: false,
      // 一物一码明细弹窗
      itemDetailVisible: false,
      currentSku: null,    // 当前编辑的SKU对象
      currentItemIndex: -1,
      batchFormList: []    // 临时存储当前SKU的条码列表
    }
  },
  computed: {
    totalGoldWeight() {
      return this.batchFormList.reduce((sum, item) => sum + (item.goldWeight || 0), 0).toFixed(2)
    },
    totalSilverWeight() {
      return this.batchFormList.reduce((sum, item) => sum + (item.silverWeight || 0), 0).toFixed(2)
    }
  },
  methods: {
    // 1. 选择采购单
    selectPurchaseOrder() {
      // 模拟打开采购单选择弹窗
      this.$prompt('请输入采购单号', '选择采购单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        // 实际调用接口查询采购单详情
        this.loadPurchaseOrder(value)
      })
    },
    loadPurchaseOrder(orderNo) {
      // 模拟接口：根据单号获取采购单信息及商品列表
      setTimeout(() => {
        this.inboundForm.purchaseOrderNo = orderNo
        this.inboundForm.supplierName = '某某珠宝供应商'
        this.inboundForm.purchaseOrderId = 1001
        // 商品列表，包含inventoryMode字段（0-普通，1-一物一码）
        this.inboundItems = [
          {
            skuId: 101,
            skuCode: 'GOLD-RING-001',
            skuName: '足金戒指',
            inventoryMode: 1,   // 一物一码
            planQuantity: 10,
            inboundQuantity: 0,   // 历史已入库数量
            thisInboundQuantity: 0,   // 本次普通模式数量
            batchList: []        // 已保存的条码明细（后端返回）
          },
          {
            skuId: 102,
            skuCode: 'SILVER-BRACELET-001',
            skuName: '银手镯',
            inventoryMode: 0,
            planQuantity: 20,
            inboundQuantity: 0,
            thisInboundQuantity: 0,
            batchList: []
          }
        ]
      }, 300)
    },
    // 2. 添加商品（手动补充，但通常由采购单带出）
    addItem() {
      // 可手动选择SKU，此处简化，实际可打开商品选择弹窗
      this.$message.info('请先选择采购单，商品由采购单自动带出')
    },
    // 3. 打开一物一码明细录入弹窗
    openItemDetail(row, index) {
      this.currentSku = row
      this.currentItemIndex = index
      // 如果已有batchList，复制到临时列表
      this.batchFormList = JSON.parse(JSON.stringify(row.batchList || []))
      if (this.batchFormList.length === 0) {
        // 默认添加一行空数据
        this.addBatchRow()
      }
      this.itemDetailVisible = true
    },
    // 添加一行条码录入
    addBatchRow() {
      this.batchFormList.push({
        barcode: '',
        goldWeight: 0,
        silverWeight: 0,
        laborCost: 0,
        tempId: Date.now() + Math.random()
      })
    },
    // 删除一行
    removeBatchRow(index) {
      this.batchFormList.splice(index, 1)
    },
    // 扫码枪连续扫描：当某行条码输入完成后，自动新增一行并聚焦到新行
    onBarcodeScan(index) {
      const currentRow = this.batchFormList[index]
      if (currentRow.barcode && currentRow.barcode.trim()) {
        // 可选：自动查询商品信息填充默认金重/工费（如果有预设值）
        this.addBatchRow()
        // 下一行聚焦需要使用 nextTick 操作DOM，略
      }
    },
    // 保存条码明细
    saveBatchList() {
      // 校验所有条码不能为空
      const emptyBarcode = this.batchFormList.some(item => !item.barcode || !item.barcode.trim())
      if (emptyBarcode) {
        this.$message.error('请完整填写所有条码')
        return
      }
      // 更新当前SKU的batchList
      this.currentSku.batchList = JSON.parse(JSON.stringify(this.batchFormList))
      // 本次入库数量 = batchList长度
      this.currentSku.thisInboundQuantity = this.batchFormList.length
      this.itemDetailVisible = false
      this.$message.success('明细已保存')
    },
    resetBatchForm() {
      this.batchFormList = []
      this.currentSku = null
    },
    // 批量导入Excel（简化示例）
    importExcel() {
      this.$message.info('导入功能待实现，可解析Excel文件生成batchFormList')
    },
    // 4. 提交入库（确认）
    submitInbound() {
      // 构造提交数据
      const items = this.inboundItems.map(item => {
        if (item.inventoryMode === 0) {
          return {
            skuId: item.skuId,
            inventoryMode: 0,
            quantity: item.thisInboundQuantity
          }
        } else {
          return {
            skuId: item.skuId,
            inventoryMode: 1,
            batchList: item.batchList
          }
        }
      }).filter(item => item.inventoryMode === 0 ? item.quantity > 0 : (item.batchList && item.batchList.length > 0))
      if (items.length === 0) {
        this.$message.warning('请至少录入一种商品')
        return
      }
      this.submitting = true
      // 模拟API调用
      setTimeout(() => {
        this.$message.success('入库成功，库存已更新')
        this.submitting = false
        // 清空或跳转
        this.resetForm()
      }, 1000)
    },
    saveDraft() {
      this.$message.success('暂存成功')
    },
    resetForm() {
      this.inboundForm = { purchaseOrderId: null, purchaseOrderNo: '', supplierName: '', inboundDate: '', remark: '' }
      this.inboundItems = []
    }
  }
}
</script>

<style scoped>
.purchase-inbound {
  padding: 20px;
  background-color: #f0f2f5;
}
.form-card, .items-card {
  border-radius: 8px;
}
.batch-actions {
  margin-bottom: 15px;
  display: flex;
  gap: 10px;
  align-items: center;
}
.scan-hint {
  font-size: 12px;
  color: #909399;
  margin-left: auto;
}
.batch-total {
  margin-top: 15px;
  text-align: right;
  font-size: 14px;
  color: #409eff;
}
.submit-bar {
  margin-top: 20px;
  text-align: center;
}
</style>
