<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="云仓库" name="ProductList" >
        <cloud-warehouse-list></cloud-warehouse-list>
      </el-tab-pane>

      <el-tab-pane label="云仓商品" name="ProductSkuList" lazy>
        <cloud-warehouse-goods-list></cloud-warehouse-goods-list>
      </el-tab-pane>

<!--      <el-tab-pane label="总部商品库" name="headProductList" lazy>-->
<!--        <head-product-list></head-product-list>-->
<!--      </el-tab-pane>-->

    </el-tabs>

  </div>
</template>

<script>

import CloudWarehouseList  from "@/views/merchant/warehouse/cloud_warehouse_list";
import CloudWarehouseGoodsList  from "@/views/merchant/warehouse/cloud_warehouse_goods_list.vue";

export default {
  name: "MerchantCloudWarehouse",
  components:{CloudWarehouseList,CloudWarehouseGoodsList},
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
