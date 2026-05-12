<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="螳螂商品" name="TanglangGoodsList">
        <tanglang-goods-list></tanglang-goods-list>
      </el-tab-pane>
      <el-tab-pane label="发货SKU" name="TanglangMemberList" lazy>
        <goods-sku-ship></goods-sku-ship>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import GoodsSkuShip from "@/views/thirdSystem/tanglang/goods_sku_ship.vue";
import TanglangGoodsList from "@/views/thirdSystem/tanglang/goods_list.vue";



export default {
  name: "Tanglang",
  components:{
    GoodsSkuShip,TanglangGoodsList},
  data() {
    return {
      activeName: 'TanglangGoodsList',
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
