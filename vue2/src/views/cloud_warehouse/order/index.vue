<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="待发货订单" name="unshipped" >
        <unshipped-list></unshipped-list>
      </el-tab-pane>
      <el-tab-pane label="全部订单" name="OrderList" >
        <order-list></order-list>
      </el-tab-pane>
    </el-tabs>
   </div>
</template>

<script>
import OrderList from "@/views/cloud_warehouse/order/order_list.vue";
import UnshippedList from "@/views/cloud_warehouse/order/unshiped_list.vue";

export default {
  name: "manual_ship",
  components:{OrderList,UnshippedList},
  data() {
    return {
      activeName: 'unshipped',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    // listPlatform({status:0}).then(res => {
    //   this.typeList = res.rows;
    //   this.activeName = this.typeList[0].code
    // })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
