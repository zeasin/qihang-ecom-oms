<template>
  <div >
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="课程订单" name="kcOrderList">
        <order-list-kc></order-list-kc>
      </el-tab-pane>
      <el-tab-pane label="商品发货订单" name="swOrderList">
        <order-list-sw></order-list-sw>
      </el-tab-pane>


    </el-tabs>
  </div>
</template>

<script>

import OrderListKc from "@/views/thirdSystem/tanglang/order_list.vue";
import OrderListSw from "@/views/thirdSystem/tanglang/order_list_ship.vue";


export default {
  name: "TanglangOrder",
  components:{OrderListKc,OrderListSw},
  data() {
    return {
      activeName: 'kcOrderList',
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
