<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="仓库商品" name="local" >
        <WarehouseGoodsList></WarehouseGoodsList>
      </el-tab-pane>
<!--      <el-tab-pane label="本地仓商品库存" name="local" >-->
<!--        <InventoryLocal></InventoryLocal>-->
<!--      </el-tab-pane>-->
      <el-tab-pane label="仓库库存" name="cloud" lazy>
        <WarehouseGoodsStockList></WarehouseGoodsStockList>
      </el-tab-pane>
<!--      <el-tab-pane label="京东云仓商品库存" name="jdcloud" lazy>-->
<!--        <InventoryCloudJd></InventoryCloudJd>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="系统云仓商品库存" name="cloud" lazy>-->
<!--        <InventoryCloud></InventoryCloud>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="商户商品库存" name="merchant" v-if="userType==='00'" lazy>-->
<!--        <InventoryMerchant></InventoryMerchant>-->
<!--      </el-tab-pane>-->
    </el-tabs>

  </div>
</template>

<script>
import WarehouseGoodsList from "@/views/wms/goods/index.vue"
import InventoryLocal  from "@/views/stock/local.vue";
import WarehouseGoodsStockList  from "@/views/stock/goods_stock.vue";
import InventoryMerchant  from "@/views/stock/merchant.vue";
import { getUserProfile } from '@/api/system/user'

export default {
  name: "GoodsInventory",
  components:{InventoryLocal,WarehouseGoodsStockList,InventoryMerchant,WarehouseGoodsList},
  data() {
    return {
      activeName: 'local',
      userType:'00'
    };
  },
  created() {
    getUserProfile().then(res=>{
      console.log(res);
      this.userType = res.data.userType;
    })
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
