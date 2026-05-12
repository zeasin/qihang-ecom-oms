<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="订单发货推送" name="CompleteShipment">
        <CompleteShipment></CompleteShipment>
      </el-tab-pane>

      <el-tab-pane label="拆单发货推送" name="SplitShipment" lazy>
        <SplitShipment></SplitShipment>
      </el-tab-pane>


      <el-tab-pane label="推送记录" name="pushList" lazy>
        <PushList></PushList>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import CompleteShipment  from "@/views/shipping/vendor_ship/order_list.vue";
import SplitShipment  from "@/views/shipping/vendor_ship/order_item.vue";
import PushList  from "@/views/shipping/vendor_ship/assigned_list.vue";

export default {
  name: "vendorShip",
  components:{CompleteShipment,SplitShipment,PushList},
  data() {
    return {
      activeName: 'CompleteShipment'
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
