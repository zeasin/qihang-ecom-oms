<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="待分账订单" name="ProductList" >
        <fz></fz>
      </el-tab-pane>

      <el-tab-pane label="分账规则" name="ProductSkuList" lazy>
        <rules></rules>
      </el-tab-pane>
      <el-tab-pane label="分账方" name="ProductListMerchant" lazy>
        <people></people>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

import ShareList from '@/views/finance/share/share_list.vue'
import rules from '@/views/finance/share/rules.vue'
import people from '@/views/finance/share/people.vue'

export default {
  name: "OrderShare",
  components:{fz: ShareList,rules,people},
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
