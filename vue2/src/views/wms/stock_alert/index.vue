<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane :label="$t('system.vendor.title')+'库存预警'" name="stock" >
        <template #label>
          <el-badge is-dot class="item">
            {{$t('system.vendor.title')}}库存预警
          </el-badge>
        </template>
        <stock></stock>
      </el-tab-pane>

      <el-tab-pane label="自发仓库库存预警" name="stockIn" lazy>
        <stock-in></stock-in>
      </el-tab-pane>
      <el-tab-pane label="库存预警设置" name="stockGoods" lazy>
        <stored-goods-sku></stored-goods-sku>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import Alert from "@/views/wms/stock_alert/alert.vue";
import StockIn from "@/views/wms/stock_alert/alert_local.vue";
import StoredGoodsSku from "@/views/wms/goods/stored_goods_sku.vue";

export default {
  name: "CloudStock",
  components:{
    Stock: Alert,StockIn,StoredGoodsSku
    },
  data() {
    return {
      activeName: 'stock',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {

  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
