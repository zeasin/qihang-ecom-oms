<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
      <el-tab-pane label="店铺电子面单账户管理" name="ewaybillAccount">
        <waybill-account></waybill-account>
      </el-tab-pane>
      <el-tab-pane label="店铺管理" name="shop" lazy>
        <shop-list></shop-list>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>

import WaybillAccount from "@/views/vms/shipping/setting/shop_ewaybill_account/ewaybill_account.vue";
import ShopList from "@/views/vms/shipping/setting/shop_ewaybill_account/shop.vue";

import {listPlatform} from "@/api/vms/shop";
export default {
  name: "shopEwaybillAccount",
  components:{
    WaybillAccount,
    ShopList
  },
  data() {
    return {
      activeName: 'ewaybillAccount',
      typeList: [],
    };
  },
  created() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows.filter(x=>x.id!=999);
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
