<template>
  <div class="purchase-inbound">
    <el-card class="form-card">
      <div slot="header">
        <span>入库单</span>
      </div>
      <el-form :model="form" label-width="120px" size="small">
        <el-form-item label="单号">
          <el-input v-model="form.stockInNum" disabled style="width: 320px"></el-input>
        </el-form-item>
        <el-form-item label="供应商" prop="supplierId" style="width: 320px">
          <el-tag size="small" v-if="form.stockInType ===1 ">采购入库</el-tag>
          <el-tag size="small" v-if="form.stockInType ===2 ">退货入库</el-tag>
          <el-tag size="small" v-if="form.stockInType ===3 ">盘盈入库</el-tag>
        </el-form-item>
        <el-form-item label="来源单号">
          <el-input v-model="form.sourceNo" disabled style="width: 320px"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker v-model="form.createTime" disabled type="datetime" style="width: 320px" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
        <el-form-item label="入库仓库">
          <el-input v-model="form.warehouseName" disabled style="width: 320px"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" disabled style="width: 320px"></el-input>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品入库明细列表 -->
    <el-card class="items-card" style="margin-top: 20px;">
      <div slot="header">
        <span>入库商品明细</span>
      </div>
      <el-table :data="inboundItems" border stripe>
        <el-table-column label="SKU编码" prop="skuCode" width="120"></el-table-column>
        <el-table-column label="商品名称" prop="goodsName" min-width="150"></el-table-column>
        <el-table-column label="规格" prop="skuName" min-width="150"></el-table-column>
        <el-table-column label="库存模式" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.inventoryMode === 1 ? 'success' : 'info'">
              {{ scope.row.inventoryMode === 1 ? '一物一码' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="计划数量" prop="quantity" width="90" align="center"></el-table-column>
        <el-table-column label="已入库数量" prop="inQuantity" width="100" align="center"></el-table-column>
        <el-table-column label="剩余数量" width="90" align="center">
          <template slot-scope="scope">
            <span :class="{'text-danger': scope.row.remainingQuantity === 0}">{{ scope.row.remainingQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="本次入库" width="240" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.inventoryMode === 0">
              <el-input-number
                v-model="scope.row.intoQuantity"
                :min="0"
                :max="scope.row.remainingQuantity"
                controls-position="right"
                size="small"
                :disabled="scope.row.remainingQuantity === 0 || isPurchaseOrderCompleted"
              ></el-input-number>
            </div>
            <div v-else>
              <el-button
                type="text"
                size="small"
                @click="openItemDetail(scope.row, scope.$index)"
                :disabled="scope.row.remainingQuantity === 0 || isPurchaseOrderCompleted"
              >配置条码明细</el-button>
              <span v-if="scope.row.pendingBatchCount && scope.row.pendingBatchCount > 0" class="pending-badge">
                （本次已录 {{ scope.row.pendingBatchCount }} 件）
              </span>
              <span v-if="scope.row.remainingQuantity === 0" class="text-muted">已完库</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div class="submit-bar" v-if="!isPurchaseOrderCompleted">
      <el-button type="primary" @click="submitInbound" :loading="submitting">确认入库</el-button>
      <el-button @click="saveDraft">取消</el-button>
    </div>
    <div class="submit-bar" v-else>
      <el-alert title="该采购单已全部入库完成，不可再操作" type="success" :closable="false" show-icon></el-alert>
    </div>

    <!-- 一物一码商品明细弹窗 -->
    <el-dialog
      :title="`录入条码明细 - ${currentSku && currentSku.skuName}`"
      :visible.sync="itemDetailVisible"
      width="800px"
      @close="resetBatchForm"
    >
      <div class="batch-actions">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="addBatchRow">添加条码</el-button>
        <el-button type="info" size="small" icon="el-icon-upload" @click="importExcel">批量导入</el-button>
        <div class="scan-hint">
          <i class="el-icon-camera-solid"></i> 支持扫码枪连续扫描，每次扫描后自动添加一行
        </div>
        <div class="remain-hint">剩余可入库数量：{{ currentSku ? currentSku.remainingQuantity : 0 }} 件</div>
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
  </div>
</template>

<script>
import {getWmsStockInEntry, stockInLocal} from "@/api/wms/stockIn";

export default {
  name: 'PurchaseInbound',
  data() {
    return {
      form: {
        id:null,
        status:null,
      },
      inboundItems: [],
      submitting: false,
      itemDetailVisible: false,
      currentSku: null,
      currentItemIndex: -1,
      batchFormList: []
    }
  },
  computed: {
    // 采购单是否已完成（整体）
    isPurchaseOrderCompleted() {
      return this.form.status === 1
    },
    totalGoldWeight() {
      return this.batchFormList.reduce((sum, item) => sum + (item.goldWeight || 0), 0).toFixed(2)
    },
    totalSilverWeight() {
      return this.batchFormList.reduce((sum, item) => sum + (item.silverWeight || 0), 0).toFixed(2)
    }
  },
  created() {
    if(this.$route.query.id) {
      this.getDetail(this.$route.query.id);
    }
  },
  methods: {
    getDetail(id) {
      getWmsStockInEntry(id).then(response => {
        this.form = response.data;
        this.inboundItems = response.data.itemList;
        this.inboundItems.forEach(item => {
          item.remainingQuantity = item.quantity - item.inQuantity
        })
      });
    },

    openItemDetail(row, index) {
      if (row.remainingQuantity <= 0) {
        this.$message.warning('该商品已全部入库，不能再录入')
        return
      }
      this.currentSku = row
      this.currentItemIndex = index
      // 复制本次暂存的明细到弹窗
      this.batchFormList = JSON.parse(JSON.stringify(row.pendingBatchList || []))
      if (this.batchFormList.length === 0) {
        this.addBatchRow()
      }
      this.itemDetailVisible = true
    },
    addBatchRow() {
      if (this.batchFormList.length >= this.currentSku.remainingQuantity) {
        this.$message.warning(`最多只能录入 ${this.currentSku.remainingQuantity} 件，已达上限`)
        return
      }
      this.batchFormList.push({
        barcode: '',
        goldWeight: 0,
        silverWeight: 0,
        laborCost: 0,
        tempId: Date.now() + Math.random()
      })
    },
    removeBatchRow(index) {
      this.batchFormList.splice(index, 1)
    },
    onBarcodeScan(index) {
      const currentRow = this.batchFormList[index]
      if (currentRow.barcode && currentRow.barcode.trim()) {
        this.addBatchRow()
      }
    },
    saveBatchList() {
      // 校验条码不能重复
      const barcodes = this.batchFormList.map(item => item.barcode)
      const hasDuplicate = barcodes.some((code, idx) => barcodes.indexOf(code) !== idx && code)
      if (hasDuplicate) {
        this.$message.error('条码不能重复')
        return
      }
      const emptyBarcode = this.batchFormList.some(item => !item.barcode || !item.barcode.trim())
      if (emptyBarcode) {
        this.$message.error('请完整填写所有条码')
        return
      }
      if (this.batchFormList.length > this.currentSku.remainingQuantity) {
        this.$message.error(`最多只能录入 ${this.currentSku.remainingQuantity} 件，当前已录入 ${this.batchFormList.length} 件`)
        return
      }
      // 保存到当前SKU的pendingBatchList
      this.$set(this.currentSku, 'pendingBatchList', JSON.parse(JSON.stringify(this.batchFormList)))
      this.$set(this.currentSku, 'pendingBatchCount', this.batchFormList.length)
      this.currentSku.intoQuantity = this.batchFormList.length
      this.itemDetailVisible = false
      this.$message.success('明细已保存')
    },
    resetBatchForm() {
      this.batchFormList = []
      this.currentSku = null
    },
    importExcel() {
      this.$message.info('导入功能待实现')
    },
    submitInbound() {
      const itemsToSubmit = []
      for (const item of this.inboundItems) {
        if (item.inventoryMode === 0) {
          if (item.intoQuantity > 0) {
            itemsToSubmit.push({
              itemId: item.id,
              skuId: item.skuId,
              inventoryMode: 0,
              quantity: item.intoQuantity
            })
          }
        } else {
          if (item.pendingBatchList && item.pendingBatchList.length > 0) {
            itemsToSubmit.push({
              itemId: item.id,
              skuId: item.skuId,
              inventoryMode: 1,
              batchList: item.pendingBatchList
            })
          }
        }
      }
      if (itemsToSubmit.length === 0) {
        this.$message.warning('请至少录入一种商品')
        return
      }
      const subForm = {
        id:this.form.id,
        items: itemsToSubmit,
      }
      console.log('submitInbound', subForm)
      this.submitting = true
      stockInLocal(subForm).then(response => {
        if (response.code === 200) {
          // 更新已入库数量
          for (const item of this.inboundItems) {
            if (item.inventoryMode === 0) {
              if (item.intoQuantity > 0) {
                item.inQuantity += item.intoQuantity
                item.remainingQuantity = item.quantity - item.inQuantity
                item.intoQuantity = 0
              }
            } else {
              if (item.pendingBatchList && item.pendingBatchList.length > 0) {
                const count = item.pendingBatchList.length
                item.inQuantity += count
                item.remainingQuantity = item.quantity - item.inQuantity
                // 清空本次暂存明细
                item.pendingBatchList = []
                item.pendingBatchCount = 0
                item.intoQuantity = 0
              }
            }
          }
          const allCompleted = this.inboundItems.every(item => item.remainingQuantity === 0)
          if (allCompleted) {
            this.purchaseOrderStatus = 1
            this.$message.success('采购单所有商品已入库完成')
          } else {
            this.$message.success('入库成功')
          }
          this.$modal.msgSuccess('入库成功')
        }else this.$modal.msgError(response.msg)
        this.submitting = false
      })
      // this.submitting = true
      // setTimeout(() => {
      //   // 更新已入库数量
      //   for (const item of this.inboundItems) {
      //     if (item.inventoryMode === 0) {
      //       if (item.intoQuantity > 0) {
      //         item.inQuantity += item.intoQuantity
      //         item.remainingQuantity = item.quantity - item.inQuantity
      //         item.intoQuantity = 0
      //       }
      //     } else {
      //       if (item.pendingBatchList && item.pendingBatchList.length > 0) {
      //         const count = item.pendingBatchList.length
      //         item.inQuantity += count
      //         item.remainingQuantity = item.quantity - item.inQuantity
      //         // 清空本次暂存明细
      //         item.pendingBatchList = []
      //         item.pendingBatchCount = 0
      //         item.intoQuantity = 0
      //       }
      //     }
      //   }
      //   const allCompleted = this.inboundItems.every(item => item.remainingQuantity === 0)
      //   if (allCompleted) {
      //     this.purchaseOrderStatus = 1
      //     this.$message.success('采购单所有商品已入库完成')
      //   } else {
      //     this.$message.success('入库成功')
      //   }
      //   this.submitting = false
      // }, 1000)
    },
    saveDraft() {
      // 调用全局挂载的方法,关闭当前标签页
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push('stock_in_list');
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
  flex-wrap: wrap;
}
.scan-hint {
  font-size: 12px;
  color: #909399;
  margin-left: auto;
}
.remain-hint {
  font-size: 12px;
  color: #e6a23c;
  margin-left: 10px;
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
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
.text-muted {
  color: #c0c4cc;
  font-size: 12px;
  margin-left: 5px;
}
.pending-badge {
  color: #409eff;
  font-size: 12px;
  margin-left: 5px;
}
</style>
