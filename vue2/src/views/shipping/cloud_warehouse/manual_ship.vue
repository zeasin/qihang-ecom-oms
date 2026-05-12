<template>
  <div class="app-container">
    <el-card shadow="never" class="mb20">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="订单号">{{ orderInfo.orderNum }}</el-descriptions-item>
        <el-descriptions-item label="仓库">{{ orderInfo.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="推送状态">
          <el-tag v-if="orderInfo.erpPushStatus === 1">推送成功</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发货状态">
          <el-tag v-if="orderInfo.sendStatus === 1">待发货</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推送时间">{{ parseTime(orderInfo.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="操作"></el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never">
      <template slot="header">
        <div class="card-header">
          <span>商品批次选择</span>
        </div>
      </template>
      <el-table v-loading="loading" :data="orderItems" border style="width: 100%">
        <el-table-column label="商品信息" width="400">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center">
              <image-preview :src="scope.row.goodsImg" :width="60" :height="60" />
              <div style="margin-left: 10px">
                <div style="font-weight: bold">{{ scope.row.goodsName }}</div>
                <div style="font-size: 12px; color: #666">规格：{{ getSkuValues(scope.row.skuName) }}</div>
                <div style="font-size: 12px; color: #666">数量：{{ scope.row.quantity }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="批次选择" width="400">
          <template slot-scope="scope">
            <el-select v-model="scope.row.batchId" placeholder="选择批次" style="width: 100%">
              <el-option
                v-for="batch in scope.row.batchList"
                :key="batch.id"
                :label="`批次号：${batch.batchNo}，数量：${batch.quantity}，入库时间：${parseTime(batch.createTime)}`"
                :value="batch.id"
              >
                <div style="display: flex; justify-content: space-between">
                  <span>批次号：{{ batch.batchNo }}</span>
                  <span>数量：{{ batch.quantity }}</span>
                  <span>入库时间：{{ parseTime(batch.createTime) }}</span>
                </div>
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="autoSelectBatch(scope.row)">自动选择</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div class="text-center mt20">
      <el-button type="primary" @click="handleManualShip">确认出库</el-button>
      <el-button @click="$router.back()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { queryCloudWarehouseOrder } from "@/api/cloud_warehouse";
import { getThirdPartyCloudWarehouseBatches } from "@/api/wms/thirdPartyCloudWarehouse";
import { manualShipment } from "@/api/wms/thirdPartyCloudWarehouse";

export default {
  name: "ManualShip",
  data() {
    return {
      loading: false,
      orderInfo: {},
      orderItems: [],
      orderId: null
    };
  },
  created() {
    this.orderId = this.$route.query.orderId;
    this.getOrderInfo();
  },
  methods: {
    /** 获取订单信息 */
    getOrderInfo() {
      this.loading = true;
      queryCloudWarehouseOrder({ id: this.orderId }).then(response => {
        if (response.code === 200) {
          this.orderInfo = response.data;
          this.orderItems = response.data.itemList || [];
          this.getBatches();
        } else {
          this.$modal.msgError(response.msg);
        }
        this.loading = false;
      });
    },
    /** 获取商品批次信息 */
    getBatches() {
      this.orderItems.forEach(item => {
        getThirdPartyCloudWarehouseBatches({
          warehouseId: this.orderInfo.warehouseId,
          goodsSkuId: item.goodsSkuId,
          quantity: item.quantity
        }).then(response => {
          if (response.code === 200) {
            item.batchList = response.data;
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      });
    },
    /** 自动选择批次（先进先出） */
    autoSelectBatch(item) {
      if (item.batchList && item.batchList.length > 0) {
        // 按入库时间排序，选择最早的批次
        const sortedBatches = [...item.batchList].sort((a, b) => new Date(a.createTime) - new Date(b.createTime));
        item.batchId = sortedBatches[0].id;
      }
    },
    /** 手动出库 */
    handleManualShip() {
      // 验证所有商品都已选择批次
      const unselectedItems = this.orderItems.filter(item => !item.batchId);
      if (unselectedItems.length > 0) {
        this.$modal.msgError("请为所有商品选择批次");
        return;
      }

      // 构建请求参数
      const requestData = {
        shipOrderId: this.orderInfo.id,
        warehouseId: this.orderInfo.warehouseId,
        items: this.orderItems.map(item => ({
          orderStockingItemId: item.id,
          goodsSkuId: item.goodsSkuId,
          quantity: item.quantity,
          batchId: item.batchId
        }))
      };

      this.$modal.confirm("确定要执行手动出库操作吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        manualShipment(requestData).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("出库成功");
            this.$router.back();
          } else {
            this.$modal.msgError(response.msg);
          }
        });
      });
    },
    /** 解析规格信息 */
    getSkuValues(spec) {
      try {
        const parsedSpec = JSON.parse(spec) || [];
        return parsedSpec.map(item => item.attr_value || item.value).join(', ') || '';
      } catch (error) {
        return spec;
      }
    }
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mb20 {
  margin-bottom: 20px;
}

.mt20 {
  margin-top: 20px;
}
</style>
