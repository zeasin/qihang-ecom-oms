<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="按商品展示" name="ProductList" >
        <product-list></product-list>
      </el-tab-pane>

      <el-tab-pane label="按规格展示" name="ProductSkuList" lazy>
        <product-sku-list></product-sku-list>
      </el-tab-pane>
<!--      <el-tab-pane label="商户商品" name="ProductListMerchant" lazy>-->
<!--        <product-list-merchant></product-list-merchant>-->
<!--      </el-tab-pane>-->
    </el-tabs>

  </div>
</template>

<script>

import ProductList  from "@/views/goods/goods_list";
import ProductSkuList  from "@/views/goods/spec/index";
// import ProductListMerchant  from "@/views/goods/merchant_goods_list";

export default {
  name: "GoodsLibs",
  components:{ProductList,ProductSkuList},
  data() {
    return {
      activeName: 'ProductList'
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
