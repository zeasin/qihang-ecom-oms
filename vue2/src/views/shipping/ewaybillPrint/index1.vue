<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
<!--      <el-tab-pane label="线下订单" name="offlineOrder" >-->
<!--        <print-list-offline :shop-type="0"></print-list-offline>-->
<!--      </el-tab-pane>-->
      <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="String(item.id)" lazy>
<!--        <ewaybill-print :platform="item.id"></ewaybill-print>-->
        <print-tao v-if="item.id === 100"></print-tao>
        <print-jd :shopType="200" v-if="item.id === 200"></print-jd>
        <print-jd :shopType="280" v-if="item.id === 280"></print-jd>
<!--        <print-jd-vc v-if="item.id === 280"></print-jd-vc>-->
        <print-pdd v-if="item.id === 300"></print-pdd>
        <print-dou v-if="item.id === 400"></print-dou>
        <print-wei v-if="item.id === 500"></print-wei>
        <print-ks v-if="item.id === 600"></print-ks>
        <print-xhs v-if="item.id === 700"></print-xhs>
        <print-list-offline v-else :shopType="item.id"></print-list-offline>
      </el-tab-pane>


    </el-tabs>

  </div>
</template>

<script>
import printTao from "@/views/shipping/ewaybillPrint/tao/index.vue";
import printPdd from "@/views/shipping/ewaybillPrint/pdd/index.vue";
import printDou from "@/views/shipping/ewaybillPrint/dou/index.vue";
import printJd from "@/views/shipping/ewaybillPrint/jd/index.vue";
import printWei from "@/views/shipping/ewaybillPrint/wei/index.vue";
import printKs from "@/views/shipping/ewaybillPrint/ks/index.vue";
import printXhs from "@/views/shipping/ewaybillPrint/xhs/index.vue";
import printListOffline from "@/views/shipping/ewaybillPrint/print_list_offline.vue";
import {listPlatform} from "@/api/shop/shop";

export default {
  name: "print",
  components:{
    printTao,printPdd,printDou,printJd,printWei,printListOffline,printKs,printXhs},
  data() {
    return {
      activeName: 'offlineOrder',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows.filter(x=>x.id<900);
      this.activeName = String(this.typeList[0].id)
    })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
