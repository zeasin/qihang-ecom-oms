<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="店铺销售汇总" name="shopsaledaily">
        <shop-daily></shop-daily>
      </el-tab-pane>
      <el-tab-pane label="地区销售汇总" name="jdOrder" lazy>
        <region-sale-daily></region-sale-daily>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import ShopDaily  from "@/views/report/shop/shop_sale_report";
import RegionSaleDaily  from "@/views/report/shop/region_sale_report";


export default {
  name: "reportIndex",
  components:{ShopDaily,RegionSaleDaily},
  data() {
    return {
      activeName: 'shopsaledaily'
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
