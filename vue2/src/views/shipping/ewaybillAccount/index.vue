<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="item.code" lazy>
<!--        <waybill-account-tao v-if="item.id === 100"></waybill-account-tao>-->
<!--        <waybill-account-list :shop-type="item.id" v-if="item.id === 200"></waybill-account-list>-->
<!--        <waybill-account-list :shop-type="item.id" v-else-if="item.id === 100"></waybill-account-list>-->
<!--        <waybill-account-jd v-else-if="item.id === 200"></waybill-account-jd>-->
<!--        <waybill-account-jd v-else-if="item.id === 280"></waybill-account-jd>-->
<!--        <print-jd-vc v-if="item.id === 280"></print-jd-vc>-->
<!--        <waybill-account-pdd v-if="item.id === 300"></waybill-account-pdd>-->
<!--        <waybill-account-dou  v-else-if="item.id === 400"></waybill-account-dou>-->
<!--        <waybill-account-wei v-else-if="item.id === 500"></waybill-account-wei>-->
        <waybill-account-list :shop-type="item.id" ></waybill-account-list>

<!--        <print-offline v-if="item.id === 999"></print-offline>-->
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

import waybillAccountList from "@/views/shipping/ewaybillAccount/account.vue";
import {listPlatform} from "@/api/shop/shop";

export default {
  name: "print",
  components:{waybillAccountList},
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
      this.typeList = res.rows.filter(x=>x.id!=999&&x.id!=911);
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
