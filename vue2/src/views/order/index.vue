<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="订单列表" name="orderList" >
        <order-list></order-list>
      </el-tab-pane>
      <el-tab-pane label="订单明细" name="orderItemList" lazy>
        <order-item></order-item>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import OrderList  from "@/views/order/order_list.vue";
import OrderItem  from "@/views/order/item_list.vue";


export default {
  name: "Order",
  components:{ OrderList, OrderItem},
  data() {
    return {
      activeName: 'orderList',
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
