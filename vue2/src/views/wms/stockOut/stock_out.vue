<template>
  <div class="outbound-work">
    <el-card class="work-card" shadow="never">
      <div slot="header">
        <span>出库作业 - {{ outboundNo }}</span>
        <el-button icon="el-icon-back" size="small" style="float: right;" @click="goBack">返回</el-button>
      </div>

      <!-- 出库单基本信息 -->
      <el-form :model="outboundOrder" label-width="100px" size="small" disabled inline>
        <el-form-item label="源单号">{{ outboundOrder.sourceNum }}</el-form-item>
        <el-form-item label="仓库">{{ outboundOrder.warehouseName }}</el-form-item>
        <el-form-item label="出库类型">
          <el-tag size="small" v-if="outboundOrder.type === 1">订单发货出库</el-tag>
          <el-tag size="small" v-if="outboundOrder.type === 2">采购退货出库</el-tag>
          <el-tag size="small" v-if="outboundOrder.type === 3">盘亏出库</el-tag>
          <el-tag size="small" v-if="outboundOrder.type === 4">报损出库</el-tag>
        </el-form-item>
        <el-form-item label="创建时间">{{ parseTime(outboundOrder.createTime )}}</el-form-item>
      </el-form>

      <!-- 商品明细与出库操作 -->
      <el-table :data="outboundItems" border stripe style="margin-top: 20px;">
        <el-table-column prop="skuCode" label="SKU编码" width="120"></el-table-column>
        <el-table-column prop="skuName" label="商品名称" min-width="150"></el-table-column>
        <el-table-column prop="planQuantity" label="应出库数量" width="100" align="center"></el-table-column>
        <el-table-column prop="outboundQuantity" label="已出库数量" width="100" align="center"></el-table-column>
        <el-table-column label="本次出库" width="300" align="center">
          <template slot-scope="scope">
            <!-- 一物一码模式 -->
            <div v-if="scope.row.inventoryMode === 1">
              <div v-if="scope.row.lockedBarcodes && scope.row.lockedBarcodes.length">
                <el-tag v-for="bc in scope.row.lockedBarcodes" :key="bc.barcode" size="small" style="margin-right: 5px;">
                  {{ bc.barcode }}
                </el-tag>
                <div class="text-muted">（已锁定，无需扫码）</div>
              </div>
              <div v-else>
                <el-button type="text" size="small" @click="openScanDialog(scope.row, scope.$index)" :disabled="scope.row.remainingQuantity === 0">
                  扫码出库
                </el-button>
                <span v-if="scope.row.scannedCount > 0">（已扫 {{ scope.row.scannedCount }} 件）</span>
              </div>
            </div>
            <!-- 普通模式 -->
            <div v-else>
              <el-select v-model="scope.row.batchStrategy" size="small" placeholder="批次策略" style="width: 120px;">
                <el-option label="自动分配批次" value="auto"></el-option>
                <el-option label="手动选择批次" value="manual"></el-option>
              </el-select>
              <el-input-number
                v-model="scope.row.thisQuantity"
                :min="0"
                :max="scope.row.remainingQuantity"
                size="small"
                controls-position="right"
                style="margin-left: 8px; width: 110px;"
                :disabled="scope.row.remainingQuantity === 0"
              ></el-input-number>
              <el-button
                v-if="scope.row.batchStrategy === 'manual'"
                type="text"
                size="small"
                @click="openBatchSelection(scope.row, scope.$index)"
                style="margin-left: 5px;"
              >选批次</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="remainingQuantity" label="剩余可出库" width="100" align="center">
          <template slot-scope="scope">
            <span :class="{'text-danger': scope.row.remainingQuantity === 0}">{{ scope.row.remainingQuantity }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="submit-bar">
        <el-button type="primary" size="large" @click="submitOutbound" :loading="submitting" :disabled="!canSubmit">确认出库</el-button>
        <el-button @click="saveDraft">暂存</el-button>
      </div>
    </el-card>

    <!-- 一物一码商品扫码弹窗 -->
    <el-dialog :title="`扫码出库 - ${currentSku && currentSku.skuName}`" :visible.sync="scanDialogVisible" width="750px" @close="resetScanData">
      <div class="scan-input-area">
        <el-input
          v-model="scanBarcode"
          placeholder="请扫描商品条码"
          size="large"
          @keyup.enter.native="addScannedBarcode"
          clearable
          ref="scanInput"
        >
          <el-button slot="append" icon="el-icon-search" @click="addScannedBarcode">添加</el-button>
        </el-input>
        <div class="scan-tip">剩余需扫描数量：{{ remainingToScan }} 件</div>
      </div>
      <el-table :data="scannedBarcodes" border stripe>
        <el-table-column prop="barcode" label="条码" width="180"></el-table-column>
        <el-table-column prop="goldWeight" label="金重(g)" width="100"></el-table-column>
        <el-table-column prop="silverWeight" label="银重(g)" width="100"></el-table-column>
        <el-table-column prop="laborCost" label="工费(元)" width="100"></el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="removeScannedBarcode(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer">
        <el-button @click="scanDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveScannedBarcodes" :disabled="scannedBarcodes.length === 0">保存</el-button>
      </span>
    </el-dialog>

    <!-- 手动选择批次弹窗（普通商品） -->
    <el-dialog title="选择出库批次" :visible.sync="batchDialogVisible" width="750px">
      <el-table :data="availableBatches" border stripe>
        <el-table-column prop="batchNo" label="批次号" width="180"></el-table-column>
        <el-table-column prop="remainingQuantity" label="剩余数量" width="100" align="center"></el-table-column>
        <el-table-column prop="costPrice" label="成本价(元)" width="100" align="right"></el-table-column>
        <el-table-column prop="productionDate" label="生产日期" width="120"></el-table-column>
        <el-table-column label="本次出库数量" width="150" align="center">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.thisQuantity"
              :min="0"
              :max="scope.row.remainingQuantity"
              size="small"
              controls-position="right"
              @change="calcTotalSelected"
            ></el-input-number>
          </template>
        </el-table-column>
      </el-table>
      <div class="batch-summary">总计：{{ totalSelectedQuantity }} 件</div>
      <span slot="footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchSelection" :disabled="totalSelectedQuantity === 0">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getStockOutEntry} from "@/api/wms/stockOut";

export default {
  name: 'OutboundWork',
  props: {
    outboundId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      outboundOrder: {
        outNum: '',
        sourceNum: '',
        warehouseName: '',
        type: '',
        createTime: null
      },
      outboundItems: [],
      submitting: false,
      // 扫码弹窗
      scanDialogVisible: false,
      currentSku: null,
      currentItemIndex: -1,
      scanBarcode: '',
      scannedBarcodes: [],
      remainingToScan: 0,
      // 批次选择弹窗
      batchDialogVisible: false,
      currentBatchSku: null,
      currentBatchItemIndex: -1,
      availableBatches: [],
      totalSelectedQuantity: 0
    }
  },
  computed: {
    outboundNo() {
      return this.outboundOrder.outNum || '加载中...'
    },
    canSubmit() {
      if (!this.outboundOrder.outNum) return false
      for (const item of this.outboundItems) {
        if (item.remainingQuantity > 0) {
          if (item.inventoryMode === 1 && item.scannedCount === 0 && (!item.lockedBarcodes || item.lockedBarcodes.length === 0)) return false
          if (item.inventoryMode === 0 && (!item.thisQuantity || item.thisQuantity === 0)) return false
        }
      }
      return true
    }
  },
  created() {
    if(this.$route.query.id) {
      this.loadOutboundOrder(this.$route.query.id);
    }
  },
  methods: {
    // 加载出库单详情
    loadOutboundOrder(id) {
      getStockOutEntry(id).then(response => {
        this.outboundOrder = response.data;
        this.outboundItems = response.data.itemList;
      });
      // 模拟API调用：根据outboundId获取出库单数据
      // 实际替换为 this.$http.get(`/api/outbound/${this.outboundId}`)
      // setTimeout(() => {
      //   // 模拟数据
      //   this.outboundOrder = {
      //     outboundNo: 'CK202404160001',
      //     sourceNo: 'SO202404160001',
      //     warehouseName: '上海南京路店仓',
      //     outboundTypeText: '销售出库',
      //     createdTime: '2024-04-16 10:30:00'
      //   }
      //   this.outboundItems = [
      //     {
      //       skuId: 101,
      //       skuCode: 'GOLD-RING-001',
      //       skuName: '足金戒指',
      //       inventoryMode: 1,        // 一物一码
      //       planQuantity: 1,
      //       outboundQuantity: 0,     // 已出库数量
      //       remainingQuantity: 1,
      //       lockedBarcodes: [        // 订单创建时已锁定的条码（POS场景）
      //         { barcode: '123456', goldWeight: 3.26, silverWeight: 0, laborCost: 180 }
      //       ],
      //       scannedCount: 0,
      //       scannedBarcodes: []
      //     },
      //     {
      //       skuId: 102,
      //       skuCode: 'SILVER-BRACELET-001',
      //       skuName: '银手镯',
      //       inventoryMode: 0,
      //       planQuantity: 2,
      //       outboundQuantity: 0,
      //       remainingQuantity: 2,
      //       batchStrategy: 'auto',
      //       thisQuantity: 0,
      //       selectedBatches: []
      //     }
      //   ]
      // }, 300)
    },
    // 一物一码扫码弹窗（仅用于未锁定的商品）
    openScanDialog(row, index) {
      if (row.remainingQuantity <= 0) {
        this.$message.warning('该商品已无剩余可出库数量')
        return
      }
      this.currentSku = row
      this.currentItemIndex = index
      this.scannedBarcodes = JSON.parse(JSON.stringify(row.scannedBarcodes || []))
      this.remainingToScan = row.remainingQuantity
      this.scanDialogVisible = true
      this.$nextTick(() => this.$refs.scanInput.focus())
    },
    addScannedBarcode() {
      if (!this.scanBarcode) return
      if (this.scannedBarcodes.length >= this.remainingToScan) {
        this.$message.warning(`最多只能扫描 ${this.currentSku.remainingQuantity} 件，已达上限`)
        this.scanBarcode = ''
        return
      }
      // 模拟查询条码信息（实际应调用后端接口验证条码是否属于当前SKU且库存可用）
      this.fetchBarcodeInfo(this.scanBarcode).then(info => {
        const exist = this.scannedBarcodes.find(b => b.barcode === this.scanBarcode)
        if (exist) {
          this.$message.warning('条码已扫描，请勿重复')
        } else {
          this.scannedBarcodes.push({
            barcode: this.scanBarcode,
            goldWeight: info.goldWeight,
            silverWeight: info.silverWeight,
            laborCost: info.laborCost
          })
          this.$message.success('已添加')
        }
        this.scanBarcode = ''
        this.$nextTick(() => this.$refs.scanInput.focus())
      }).catch(() => {
        this.$message.error('条码无效或不属于当前商品')
        this.scanBarcode = ''
      })
    },
    fetchBarcodeInfo(barcode) {
      // 模拟API
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          if (barcode === '123456') {
            resolve({ goldWeight: 3.26, silverWeight: 0, laborCost: 180 })
          } else {
            reject('未找到')
          }
        }, 200)
      })
    },
    removeScannedBarcode(index) {
      this.scannedBarcodes.splice(index, 1)
    },
    saveScannedBarcodes() {
      this.currentSku.scannedBarcodes = JSON.parse(JSON.stringify(this.scannedBarcodes))
      this.currentSku.scannedCount = this.scannedBarcodes.length
      this.scanDialogVisible = false
      this.$message.success(`已扫描 ${this.currentSku.scannedCount} 件`)
    },
    resetScanData() {
      this.scanBarcode = ''
      this.scannedBarcodes = []
      this.currentSku = null
    },
    // 普通商品手动选择批次
    openBatchSelection(row, index) {
      this.currentBatchSku = row
      this.currentBatchItemIndex = index
      this.fetchAvailableBatches(row.skuId).then(batches => {
        if (row.selectedBatches && row.selectedBatches.length) {
          batches.forEach(b => {
            const saved = row.selectedBatches.find(sb => sb.batchId === b.batchId)
            if (saved) b.thisQuantity = saved.quantity
            else b.thisQuantity = 0
          })
        } else {
          batches.forEach(b => { b.thisQuantity = 0 })
        }
        this.availableBatches = batches
        this.calcTotalSelected()
        this.batchDialogVisible = true
      })
    },
    fetchAvailableBatches(skuId) {
      // 模拟API
      return new Promise(resolve => {
        setTimeout(() => {
          resolve([
            { batchId: 1, batchNo: 'PO20240415001', remainingQuantity: 5, costPrice: 320, productionDate: '2024-04-15', thisQuantity: 0 },
            { batchId: 2, batchNo: 'PO20240416002', remainingQuantity: 3, costPrice: 325, productionDate: '2024-04-16', thisQuantity: 0 }
          ])
        }, 200)
      })
    },
    calcTotalSelected() {
      this.totalSelectedQuantity = this.availableBatches.reduce((sum, b) => sum + (Number(b.thisQuantity) || 0), 0)
    },
    confirmBatchSelection() {
      if (this.totalSelectedQuantity > this.currentBatchSku.remainingQuantity) {
        this.$message.error(`所选批次总数量不能超过剩余可出库数量（${this.currentBatchSku.remainingQuantity}）`)
        return
      }
      this.currentBatchSku.selectedBatches = this.availableBatches.filter(b => b.thisQuantity > 0).map(b => ({
        batchId: b.batchId,
        batchNo: b.batchNo,
        quantity: b.thisQuantity
      }))
      this.currentBatchSku.thisQuantity = this.totalSelectedQuantity
      this.batchDialogVisible = false
      this.$message.success(`已选择批次，总数量 ${this.totalSelectedQuantity}`)
    },
    // 暂存（保存本次出库进度，不扣减库存）
    saveDraft() {
      const draftData = {
        outboundId: this.outboundId,
        items: this.outboundItems.map(item => ({
          skuId: item.skuId,
          scannedBarcodes: item.scannedBarcodes,
          thisQuantity: item.thisQuantity,
          selectedBatches: item.selectedBatches
        }))
      }
      // 模拟暂存接口
      setTimeout(() => {
        this.$message.success('暂存成功')
      }, 300)
    },
    // 确认出库
    submitOutbound() {
      const items = []
      for (const item of this.outboundItems) {
        if (item.inventoryMode === 1) {
          // 一物一码：优先使用锁定的条码，其次使用本次扫描的条码
          const barcodesToOut = []
          if (item.lockedBarcodes && item.lockedBarcodes.length) {
            barcodesToOut.push(...item.lockedBarcodes.map(bc => bc.barcode))
          }
          if (item.scannedBarcodes && item.scannedBarcodes.length) {
            barcodesToOut.push(...item.scannedBarcodes.map(bc => bc.barcode))
          }
          if (barcodesToOut.length !== item.planQuantity - item.outboundQuantity) {
            this.$message.error(`${item.skuName} 出库数量与应出库数量不一致`)
            return
          }
          for (const bc of barcodesToOut) {
            items.push({
              skuId: item.skuId,
              barcode: bc,
              quantity: 1,
              mode: 'barcode'
            })
          }
        } else {
          if (item.thisQuantity > 0) {
            if (item.batchStrategy === 'auto') {
              items.push({
                skuId: item.skuId,
                quantity: item.thisQuantity,
                mode: 'auto_allocate'
              })
            } else if (item.selectedBatches && item.selectedBatches.length) {
              for (const sb of item.selectedBatches) {
                items.push({
                  skuId: item.skuId,
                  batchId: sb.batchId,
                  quantity: sb.quantity,
                  mode: 'manual_batch'
                })
              }
            }
          }
        }
      }
      if (items.length === 0) {
        this.$message.warning('没有可出库的商品')
        return
      }
      const params = {
        outboundId: this.outboundId,
        items: items
      }
      this.submitting = true
      // 调用后端出库确认接口
      // this.$http.post('/api/outbound/confirm', params).then(() => {
      //   this.$message.success('出库成功')
      //   this.$router.push('/outbound-workbench')
      // }).finally(() => { this.submitting = false })
      setTimeout(() => {
        this.$message.success('出库成功')
        this.submitting = false
        this.$router.push('/outbound-workbench')
      }, 1000)
    },
    goBack() {
      this.$router.push('/outbound-workbench')
    }
  }
}
</script>

<style scoped>
.outbound-work {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 100px);
}
.work-card {
  border-radius: 8px;
}
.scan-input-area {
  margin-bottom: 20px;
}
.scan-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
.submit-bar {
  margin-top: 30px;
  text-align: center;
}
.text-muted {
  color: #c0c4cc;
  font-size: 12px;
}
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
.batch-summary {
  margin-top: 15px;
  text-align: right;
  font-weight: bold;
}
</style>
