<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="螳螂订单" name="TanglangOrderList">
        <tanglang-order-index></tanglang-order-index>
      </el-tab-pane>
      <el-tab-pane label="系统参数配置" name="TanglangConfig" lazy>
        <tanglang-config></tanglang-config>
      </el-tab-pane>
      <el-tab-pane label="螳螂商品" name="TanglangGoodsList" lazy>
        <tanglang-goods-list></tanglang-goods-list>
      </el-tab-pane>
      <el-tab-pane label="螳螂学员" name="TanglangMemberList" lazy>
        <tanglang-member-list></tanglang-member-list>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import TanglangConfig from "@/views/thirdSystem/tanglang/config.vue";
import TanglangGoodsList from "@/views/thirdSystem/tanglang/goods_list.vue";
import TanglangMemberList from "@/views/thirdSystem/tanglang/member_list.vue";
import TanglangOrderIndex from "@/views/thirdSystem/tanglang/order_list.vue";


export default {
  name: "Tanglang",
  components:{
    TanglangConfig,TanglangGoodsList,TanglangMemberList,TanglangOrderIndex},
  data() {
    return {
      activeName: 'TanglangOrderList',
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
