<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="按商品备货" name="stockupOrderItem" lazy>
        <ship-stockup-order-item></ship-stockup-order-item>
      </el-tab-pane>

      <el-tab-pane label="按订单备货" name="stockupOrder" lazy>
        <ship-stockup-order></ship-stockup-order>
      </el-tab-pane>


<!--      <el-tab-pane label="供应商备货订单" name="vendorShipOrder" lazy>-->
<!--        <vendor-ship-order-list></vendor-ship-order-list>-->
<!--      </el-tab-pane>-->

    </el-tabs>

  </div>
</template>

<script>
import ShipStockupOrder  from "@/views/shipping/stocking/stocking_order.vue";
import ShipStockupOrderItem  from "@/views/shipping/stocking/stocking_order_item.vue";
import VendorShipOrderList  from "@/views/shipping/stocking/vendor_ship_order_list.vue";

export default {
  name: "stockup",
  components:{ShipStockupOrder,ShipStockupOrderItem,VendorShipOrderList},
  data() {
    return {
      activeName: 'stockupOrder'
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
