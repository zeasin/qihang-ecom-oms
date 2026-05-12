<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="店铺销售商品汇总" name="shopsaledaily">
        <shop-sale-goods-daily></shop-sale-goods-daily>
      </el-tab-pane>
      <el-tab-pane label="地区销售商品汇总" name="jdOrder" lazy>
        <region-sale-goods-daily></region-sale-goods-daily>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import ShopSaleGoodsDaily  from "@/views/report/goods/shop_sale_goods";
import RegionSaleGoodsDaily  from "@/views/report/goods/region_sale_goods";


export default {
  name: "goodsReport",
  components:{ShopSaleGoodsDaily,RegionSaleGoodsDaily},
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
