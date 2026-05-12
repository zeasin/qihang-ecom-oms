<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="采购订单" name="orderList" >
        <order-list></order-list>
      </el-tab-pane>
      <el-tab-pane label="采购商品明细" name="orderItemList" lazy>
        <order-item></order-item>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import OrderList  from "@/views/purchase/order/list.vue";
import OrderItem  from "@/views/purchase/order/item.vue";


export default {
  name: "PurchaseOrder",
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
