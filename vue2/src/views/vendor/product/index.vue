<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="按商品展示" name="ProductList" >
        <product-list></product-list>
      </el-tab-pane>

      <el-tab-pane label="按规格展示" name="ProductSkuList" lazy>
        <product-sku-list></product-sku-list>
      </el-tab-pane>

      <el-tab-pane label="价格管理" name="ProductPrice" lazy>
        <product-price></product-price>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>

import ProductList  from "@/views/vendor/product/goods_list";
import ProductSkuList  from "@/views/vendor/product/goods_sku_list";
import ProductPrice  from "@/views/vendor/product/price";

export default {
  name: "SupplierProduct",
  components:{ProductList,ProductSkuList,ProductPrice},
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
