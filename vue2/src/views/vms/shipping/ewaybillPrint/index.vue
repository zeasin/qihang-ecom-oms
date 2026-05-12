<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
      <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="item.code" lazy>
<!--        <print-tao v-if="item.id === 100"></print-tao>-->
<!--        <print-jd v-if="item.id === 200"></print-jd>-->
<!--        <print-jdvc v-if="item.id === 280"></print-jdvc>-->
<!--        <print-pdd v-if="item.id === 300"></print-pdd>-->
<!--        <print-dou v-if="item.id === 400"></print-dou>-->
<!--        <print-wei v-if="item.id === 500"></print-wei>-->
        <print-list-diansan :shopType="item.id"></print-list-diansan>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import printTao from "@/views/vms/shipping/ewaybillPrint/tao/index.vue";
import printPdd from "@/views/vms/shipping/ewaybillPrint/pdd/index.vue";
import printDou from "@/views/vms/shipping/ewaybillPrint/dou/index.vue";
import printJd from "@/views/vms/shipping/ewaybillPrint/jd/index.vue";
import PrintListDiansan from "@/views/vms/shipping/ewaybillPrint/print_list_diansan.vue";
import printJdvc from "@/views/vms/shipping/ewaybillPrint/jd/vc-order.vue";
import printWei from "@/views/vms/shipping/ewaybillPrint/wei/index.vue";
import {listPlatform} from "@/api/vms/shop";

export default {
  name: "print",
  components:{
    printTao,printPdd,printDou,printJd,PrintListDiansan,printWei,printJdvc},
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
