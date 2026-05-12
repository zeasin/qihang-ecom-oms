<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
      <el-tab-pane label="订单发货" name="waitShipping" >
        <order-list-wait></order-list-wait>
      </el-tab-pane>
      <el-tab-pane label="打单发货" name="printHome" lazy>
        <print-home></print-home>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import OrderListWait  from "@/views/shipping/manual_ship/order_list.vue";
import PrintHome from "@/views/shipping/ewaybillPrint/index.vue";

import {listPlatform} from "@/api/shop/shop";

export default {
  name: "print",
  components:{
    PrintHome,OrderListWait},
  data() {
    return {
      activeName: 'waitShipping',
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
