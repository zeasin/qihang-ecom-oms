<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="item.code" lazy>
        <ShopRefund :shopType="item.id"></ShopRefund>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ShopRefund from "@/views/shop/refund/index.vue"
import {listPlatform} from "@/api/shop/shop";

export default {
  name: "refund",
  components:{ShopRefund},
  data() {
    return {
      activeName: '',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
      this.activeName = this.typeList[0].code
    })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
