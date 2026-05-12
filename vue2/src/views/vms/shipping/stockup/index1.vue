<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="发货订单" name="OrderList" lazy>
        <OrderList></OrderList>
      </el-tab-pane>
      <el-tab-pane label="发货商品" name="OrderItem" lazy>
        <OrderItem></OrderItem>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import OrderList from "@/views/vms/shipping/stockup/index.vue";
import OrderItem from "@/views/vms/shipping/stockup/order_item.vue";


export default {
  name: "orderShipOut",
  components:{
    OrderList,OrderItem
  },
  data() {
    return {
      activeName: 'OrderList',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    if(this.$route.query.tag){
      this.activeName = this.$route.query.tag
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
