<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="待发货" name="unshipped" >
        <unshipped-list></unshipped-list>
      </el-tab-pane>
      <el-tab-pane label="全部" name="OrderList" >
        <order-list></order-list>
      </el-tab-pane>
    </el-tabs>
   </div>
</template>

<script>
import OrderList from "@/views/vendor/order/order_list.vue";
import UnshipList from "@/views/vendor/order/unship_list.vue";

export default {
  name: "SupplierOrder",
  components:{OrderList, UnshippedList: UnshipList},
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
