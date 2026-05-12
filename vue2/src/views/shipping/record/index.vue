<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="本地仓发货记录" name="localShip" >
        <local-shipment></local-shipment>
      </el-tab-pane>
      <el-tab-pane label="供应商发货记录" name="supplierShip" lazy>
        <supplier-shipment></supplier-shipment>
      </el-tab-pane>
      <el-tab-pane label="云仓发货记录" name="cloudWarehouseShip" lazy>
        <cloud-warehouse-shipment></cloud-warehouse-shipment>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import LocalShipment  from "@/views/shipping/record/local.vue";
import SupplierShipment  from "@/views/shipping/vendor_ship/assigned_list.vue";
import CloudWarehouseShipment from "@/views/shipping/cloud_warehouse/push_list.vue";


import {listPlatform} from "@/api/shop/shop";

export default {
  name: "print",
  components:{
    CloudWarehouseShipment,LocalShipment,SupplierShipment},
  data() {
    return {
      activeName: 'localShip',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows.filter(x=>x.id<900);
      // this.activeName = this.typeList[0].code
    })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
