<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="String(item.id)" lazy>
      <goods-tl v-if="item.id===911"></goods-tl>
      <ShopGoods v-else :shopType="item.id" :refresh="refresh" :targetShopId="targetShopId"></ShopGoods>
    </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

import ShopGoods  from "@/views/shop/goods/index";
import {listPlatform} from "@/api/shop/shop";
import GoodsTl from "@/views/thirdSystem/tanglang/goods_index.vue";

export default {
  name: "Goods",
  components:{
    GoodsTl,ShopGoods },
  data() {
    return {
      activeName: '',
      typeList: [],
      refresh: '',
      targetShopId: '',
      targetShopType: ''
    };
  },
  created() {
    // 获取路由参数
    this.refresh = this.$route.query.refresh || '';
    this.targetShopId = this.$route.query.shopId || '';
    this.targetShopType = this.$route.query.shopType || '';
  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
      // 如果有shopType参数，切换到对应的tab
      if (this.targetShopType) {
        const targetPlatform = this.typeList.find(p => p.id == this.targetShopType);
        if (targetPlatform) {
          this.activeName = String(targetPlatform.id);
        }
      } else {
        this.activeName = String(this.typeList[0].id)
      }
      // 如果有refresh参数，延迟刷新以确保组件已加载
      if (this.refresh === '1' && this.targetShopId) {
        setTimeout(() => {
          this.$router.replace({ query: {} });
        }, 500);
      }
    })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
