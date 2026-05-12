<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="item in typeList" :label="item.name" :name="String(item.id)" :key="item.id" lazy>
        <ShopOrder :shopType="item.id" :shopId="targetShopId" :merchantId="targetMerchantId"></ShopOrder>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import ShopOrder  from "@/views/shop/order/index";
import {listPlatform} from "@/api/shop/shop";

export default {
  name: "ShopOrderIndex",
  components:{ShopOrder},
  data() {
    return {
      activeName: '',
      typeList: [],
      targetMerchantId: 0,
      targetShopId: null,
      targetShopType: ''
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
      // this.activeName = this.typeList[0].code
      // if(this.$route.query.tab){
      //   this.activeName = this.$route.query.tab
      // }
      if(this.$route.query.shopType){
        this.targetShopType = this.$route.query.shopType
        this.activeName = String(this.targetShopType);
      }else{
        this.activeName = String(this.typeList[0].id)
      }
      if(this.$route.query.shopId){
        this.targetShopId = this.$route.query.shopId
      }
      if(this.$route.query.merchantId){
        this.targetMerchantId = this.$route.query.merchantId
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
