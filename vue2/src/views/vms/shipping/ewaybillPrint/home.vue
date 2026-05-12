<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="线下渠道" name="offline" >
        <print-offline></print-offline>
      </el-tab-pane>
      <el-tab-pane label="电商渠道" name="online" lazy>
        <print-online></print-online>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import printOffline from "@/views/vms/shipping/ewaybillPrint/offline.vue";
import printOnline from "@/views/vms/shipping/ewaybillPrint/index.vue";

export default {
  name: "printHome",
  components:{
    printOffline,printOnline},
  data() {
    return {
      activeName: 'offline',
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
