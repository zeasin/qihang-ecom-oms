<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="仓库发货" name="stockShip">
        <warehouse-shipment></warehouse-shipment>
      </el-tab-pane>

      <el-tab-pane label="供应商发货" name="supplierShip" lazy>
        <supplier-shipment></supplier-shipment>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import warehouseShipment  from "@/views/shipping/stockup/warehouse/index.vue";
import supplierShipment  from "@/views/shipping/stockup/supplier/index.vue";

export default {
  name: "stocking",
  components:{warehouseShipment,supplierShipment},
  data() {
    return {
      activeName: 'stockShip'
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
