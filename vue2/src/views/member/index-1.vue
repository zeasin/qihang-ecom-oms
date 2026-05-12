<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="螳螂系统" name="sfz" >
        <ShopMemberTl></ShopMemberTl>
      </el-tab-pane>


    </el-tabs>

  </div>
</template>

<script>
import ShopMemberTl  from "@/views/thirdSystem/tanglang/member_list.vue";

import {listPlatform} from "@/api/shop/shop";
export default {
  name: "Order",
  components:{ShopMemberTl},
  data() {
    return {
      activeName: 'sfz',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    // listPlatform({status:0}).then(res => {
    //   this.typeList = res.rows;
    //   this.activeName = this.typeList[0].code
    // })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
